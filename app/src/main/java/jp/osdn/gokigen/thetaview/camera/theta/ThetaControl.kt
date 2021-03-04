package jp.osdn.gokigen.thetaview.camera.theta

import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import jp.osdn.gokigen.thetaview.IShowInformation
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.camera.ICameraStatusReceiver
import jp.osdn.gokigen.thetaview.camera.theta.connection.ThetaCameraConnection
import jp.osdn.gokigen.thetaview.camera.theta.liveview.ThetaLiveViewControl
import jp.osdn.gokigen.thetaview.camera.theta.operation.IThetaShutter
import jp.osdn.gokigen.thetaview.camera.theta.operation.ThetaMovieRecordingControl
import jp.osdn.gokigen.thetaview.camera.theta.operation.ThetaOptionSetControl
import jp.osdn.gokigen.thetaview.camera.theta.operation.ThetaSingleShotControl
import jp.osdn.gokigen.thetaview.camera.theta.status.ICaptureModeReceiver
import jp.osdn.gokigen.thetaview.camera.theta.status.ThetaCameraStatusWatcher
import jp.osdn.gokigen.thetaview.camera.theta.status.ThetaSessionHolder
import jp.osdn.gokigen.thetaview.liveview.ILiveView
import jp.osdn.gokigen.thetaview.liveview.ILiveViewController
import jp.osdn.gokigen.thetaview.liveview.ILiveViewRefresher
import jp.osdn.gokigen.thetaview.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.thetaview.operation.ICameraControl
import jp.osdn.gokigen.thetaview.scene.ICameraConnectionStatus
import jp.osdn.gokigen.thetaview.scene.IIndicator

class ThetaControl(private val context: AppCompatActivity, private val showInformation : IShowInformation, statusReceiver : ICameraStatusReceiver) : ILiveViewController, ICameraControl, View.OnClickListener, ICaptureModeReceiver, IThetaShutter
{
    private val sessionIdHolder = ThetaSessionHolder()
    private val cameraConnection = ThetaCameraConnection(context, statusReceiver, sessionIdHolder, sessionIdHolder, this)
    private var liveViewListener = CameraLiveViewListenerImpl(context)
    private val liveViewControl = ThetaLiveViewControl(liveViewListener)
    private var indicator : IIndicator? = null

    private val statusWatcher = ThetaCameraStatusWatcher(sessionIdHolder, this)
    private var isStatusWatch = false
    private var isMovieRecording = false

    fun setIndicator(indicator : IIndicator)
    {
        this.indicator = indicator
    }

    fun changeCaptureMode()
    {
        val options = if (statusWatcher.captureMode.contains("image"))
        {
            // image -> video
            "\"captureMode\" : \"video\""
        }
        else
        {
            // video -> image
            "\"captureMode\" : \"image\""
        }
        ThetaOptionSetControl(sessionIdHolder).setOptions(options, sessionIdHolder.isApiLevelV21())
    }

    override fun changedCaptureMode(captureMode : String)
    {
        try
        {
            val isImage = captureMode.contains("image")
            context.runOnUiThread {
                try
                {
                    val view : ImageButton = context.findViewById(R.id.button_camera)
                    val iconId = if (isImage) { R.drawable.ic_baseline_videocam_24 } else { R.drawable.ic_baseline_camera_alt_24 }
                    view.setImageDrawable(ContextCompat.getDrawable(context, iconId))
                    view.invalidate()
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun initialize()
    {
        // TODO("Not yet implemented")
    }

    override fun startCamera(isPreviewView: Boolean)
    {
        try
        {
            if (cameraConnection.getConnectionStatus() != ICameraConnectionStatus.CameraConnectionStatus.CONNECTED)
            {
                cameraConnection.startWatchWifiStatus(context)
            }
            else
            {
                cameraConnection.connect()
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun finishCamera()
    {
        try
        {
            if (isStatusWatch)
            {
                statusWatcher.stopStatusWatch()
                isStatusWatch = false
            }
            cameraConnection.disconnect(false)
            cameraConnection.stopWatchWifiStatus(context)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun connectToCamera()
    {
        Log.v(TAG, " connectToCamera() ")
        try
        {
            cameraConnection.connect()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun setRefresher(refresher: ILiveViewRefresher, imageView: ILiveView)
    {
        try
        {
            liveViewListener.setRefresher(refresher)
            imageView.setImageProvider(liveViewListener)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun captureButtonReceiver(): View.OnClickListener
    {
        return (this)
    }

    override fun onClick(v: View?)
    {
        if (v == null)
        {
            return
        }
        when (v.id)
        {
            R.id.button_camera -> { doShutter() }
            else -> { }
        }
    }

    override fun doShutter()
    {
        try
        {
            if (statusWatcher.captureMode.contains("image"))
            {
                // image
                ThetaSingleShotControl(sessionIdHolder, showInformation, liveViewControl).singleShot(sessionIdHolder.isApiLevelV21())
            }
            else
            {
                // video
                ThetaMovieRecordingControl(sessionIdHolder, showInformation, liveViewControl).movieControl(sessionIdHolder.isApiLevelV21())
                isMovieRecording = true
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
            isMovieRecording = false
        }
    }

    override fun doShutterOff()
    {
        try
        {
            if ((isMovieRecording)&&(!statusWatcher.captureMode.contains("image")))
            {
                // video
                ThetaMovieRecordingControl(sessionIdHolder, showInformation, liveViewControl).movieControl(sessionIdHolder.isApiLevelV21())
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        isMovieRecording = false
    }

    companion object
    {
        private val  TAG = this.toString()
    }

    override fun startLiveView()
    {
        Log.v(TAG, " startLiveView() ")
        try
        {
            if (!isStatusWatch)
            {
                statusWatcher.startStatusWatch(indicator)
                isStatusWatch = true
            }
            liveViewControl.setSessionIdProvider(sessionIdHolder)
            liveViewControl.startLiveView()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun stopLiveView()
    {
        Log.v(TAG, " stopLiveView() ")
        try
        {
            liveViewControl.stopLiveView()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }
}
