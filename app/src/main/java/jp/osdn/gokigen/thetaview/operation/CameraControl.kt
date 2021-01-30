package jp.osdn.gokigen.thetaview.operation

import android.util.Log
import android.view.Surface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import jp.osdn.gokigen.thetaview.IScopedStorageAccessPermission
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.liveview.ILiveView
import jp.osdn.gokigen.thetaview.liveview.ILiveViewRefresher
import jp.osdn.gokigen.thetaview.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.thetaview.liveview.storeimage.StoreImage
import jp.osdn.gokigen.thetaview.operation.imagefile.FileControl
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraControl(private val activity : AppCompatActivity, private val accessPermission : IScopedStorageAccessPermission?) : ICameraControl
{
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var liveViewListener : CameraLiveViewListenerImpl
    private lateinit var fileControl : FileControl
    private lateinit var storeImage : StoreImage
    private var cameraIsStarted = false

    override fun initialize()
    {
        Log.v(TAG, " initialize()")
        liveViewListener = CameraLiveViewListenerImpl(activity)
        cameraExecutor = Executors.newSingleThreadExecutor()
        storeImage = StoreImage(activity, liveViewListener)
        fileControl = FileControl(activity, storeImage)
    }

    override fun setRefresher(refresher: ILiveViewRefresher, imageView : ILiveView)
    {
        liveViewListener.setRefresher(refresher)
        imageView.setImageProvider(liveViewListener)
    }

    override fun startCamera(isPreviewView : Boolean)
    {
        Log.v(TAG, " startCamera()")
        if (cameraIsStarted)
        {
            Log.v(TAG, " ALREADY STARTED...")
            try
            {
                val cameraProvider: ProcessCameraProvider = ProcessCameraProvider.getInstance(activity).get()
                cameraProvider.unbindAll()
                cameraIsStarted = false
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
        cameraIsStarted = true
        if (isPreviewView)
        {
            // Preview View
            startCameraForPreviewView()
        }
        else
        {
            // Liveview View
            startCameraForLiveView()
        }
    }

    private fun startCameraForPreviewView()
    {
        Log.v(TAG, " startCameraPreviewView()")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener( {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(activity.findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder).surfaceProvider)
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val imageCapture = fileControl.prepare()

            try
            {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture)
            }
            catch(e : Exception)
            {
                Log.e(TAG, "Use case binding failed", e)
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    private fun getImageRotation() : Int
    {
        return (Surface.ROTATION_0)
    }

    private fun startCameraForLiveView()
    {
        Log.v(TAG, " startCameraForLiveView()")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener( {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
/*
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(activity.findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder).createSurfaceProvider())
                }
*/
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val imageCapture = fileControl.prepare()

            try
            {
                val imageAnalyzer = ImageAnalysis.Builder()
                    //.setTargetResolution(Size(800, 600))
                    .setTargetRotation(getImageRotation())
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, liveViewListener)
                    }
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(activity, cameraSelector, imageCapture, imageAnalyzer)
            }
            catch(e : Exception)
            {
                Log.e(TAG, "Use case binding failed", e)
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    override fun finishCamera()
    {
        try
        {
            val cameraProvider: ProcessCameraProvider = ProcessCameraProvider.getInstance(activity).get()
            cameraProvider.unbindAll()
            cameraIsStarted = false
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }

        try
        {
            cameraExecutor.shutdown()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        fileControl.finish()
    }

    override fun connectToCamera()
    {
        // TODO("Not yet implemented")
    }

    override fun captureButtonReceiver() : View.OnClickListener
    {
        return (fileControl)
    }

    companion object
    {
        private val  TAG = this.toString()
    }
}
