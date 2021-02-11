package jp.osdn.gokigen.thetaview.camera.theta

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.thetaview.IShowInformation
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.camera.ICameraStatusReceiver
import jp.osdn.gokigen.thetaview.camera.theta.connection.ThetaCameraConnection
import jp.osdn.gokigen.thetaview.camera.theta.liveview.ThetaLiveViewControl
import jp.osdn.gokigen.thetaview.camera.theta.operation.ThetaSingleShotControl
import jp.osdn.gokigen.thetaview.camera.theta.status.ThetaCameraStatusWatcher
import jp.osdn.gokigen.thetaview.camera.theta.status.ThetaSessionHolder
import jp.osdn.gokigen.thetaview.liveview.ILiveView
import jp.osdn.gokigen.thetaview.liveview.ILiveViewController
import jp.osdn.gokigen.thetaview.liveview.ILiveViewRefresher
import jp.osdn.gokigen.thetaview.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.thetaview.operation.ICameraControl
import jp.osdn.gokigen.thetaview.scene.ICameraConnectionStatus

class ThetaControl(private val context: AppCompatActivity, private val showInformation : IShowInformation, statusReceiver : ICameraStatusReceiver) : ILiveViewController, ICameraControl, View.OnClickListener
{
    private val sessionIdHolder = ThetaSessionHolder()
    private val cameraConnection = ThetaCameraConnection(context, statusReceiver, sessionIdHolder, sessionIdHolder, this)
    private var liveViewListener = CameraLiveViewListenerImpl(context)
    private val liveViewControl = ThetaLiveViewControl(liveViewListener)
    //private val singleShotControl = ThetaSingleShotControl(sessionIdHolder, showInformation)
    //private val optionSetControl = ThetaOptionUpdateControl(sessionIdHolder)
    private val statusWatcher = ThetaCameraStatusWatcher(sessionIdHolder, showInformation)
    private var isStatusWatch = false

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
            R.id.button_camera -> { ThetaSingleShotControl(sessionIdHolder, showInformation, liveViewControl).singleShot(sessionIdHolder.isApiLevelV21()) }
            else -> { }
        }
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
                statusWatcher.startStatusWatch()
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
