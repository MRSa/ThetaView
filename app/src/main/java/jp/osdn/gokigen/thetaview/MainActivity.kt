package jp.osdn.gokigen.thetaview

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jp.osdn.gokigen.thetaview.camera.ICameraStatusReceiver
import jp.osdn.gokigen.thetaview.preference.PreferenceValueInitializer
import jp.osdn.gokigen.thetaview.scene.*

class MainActivity : AppCompatActivity(), IShowInformation, ICameraStatusReceiver, ICameraConnectionStatus
{
    private val mainButtonHandler : MainButtonHandler = MainButtonHandler(this, this, this)
    private val showMessage : ShowMessage = ShowMessage(this)
    private val accessPermission : IScopedStorageAccessPermission? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { StorageOperationWithPermission(this) } else { null }
    private val sceneChanger : SceneChanger = SceneChanger(this, showMessage, accessPermission, this, this)
    private var connectionStatus : ICameraConnectionStatus.CameraConnectionStatus = ICameraConnectionStatus.CameraConnectionStatus.UNKNOWN

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.v(TAG, " ----- onCreate() -----")
        super.onCreate(savedInstanceState)
        mainButtonHandler.setSceneChanger(sceneChanger)

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        try
        {
            PreferenceValueInitializer().initializePreferences(this)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        if (allPermissionsGranted())
        {
            checkMediaWritePermission()
            sceneChanger.initializeFragment()
            mainButtonHandler.initialize()
        }
        else
        {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        sceneChanger.finish()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkMediaWritePermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            StorageOperationWithPermission(this).requestStorageAccessFrameworkLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    )
    {
        if (requestCode == REQUEST_CODE_PERMISSIONS)
        {
            if (allPermissionsGranted())
            {
                checkMediaWritePermission()
                sceneChanger.initializeFragment()
                mainButtonHandler.initialize()
            }
            else
            {
                Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show()
                //Snackbar.make(main_layout,"Permissions not granted by the user.", Snackbar.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MEDIA_EDIT)
        {
            accessPermission?.responseAccessPermission(resultCode, data)
        }
        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT_TREE)
        {
            accessPermission?.responseStorageAccessFrameworkLocation(resultCode, data)
        }
    }

    companion object
    {
        private val TAG = MainActivity::class.java.simpleName

        private const val REQUEST_CODE_PERMISSIONS = 10
        const val REQUEST_CODE_MEDIA_EDIT = 12
        const val REQUEST_CODE_OPEN_DOCUMENT_TREE = 20

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun showToast(rscId: Int, appendMessage: String, duration: Int)
    {
        try
        {
            runOnUiThread {
                try
                {
                    val message = if (rscId != 0) getString(rscId) + appendMessage else appendMessage
                    Toast.makeText(applicationContext, message, duration).show()
                }
                catch (e: java.lang.Exception)
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

    override fun vibrate(vibratePattern: IShowInformation.VibratePattern)
    {
        try
        {
            // バイブレータをつかまえる
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (!vibrator.hasVibrator())
            {
                Log.v(TAG, " not have Vibrator...")
                return
            }
            @Suppress("DEPRECATION") val thread = Thread {
                try
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                    else
                    {
                        when (vibratePattern)
                        {
                            IShowInformation.VibratePattern.SIMPLE_SHORT ->  vibrator.vibrate(50)
                            IShowInformation.VibratePattern.SIMPLE_LONG ->  vibrator.vibrate(150)
                            else -> { }
                        }
                    }
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    override fun invalidate()
    {
        try
        {
            runOnUiThread {
                //if (liveView != null) {
                //    liveView.invalidate()
                //}
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onStatusNotify(message: String?)
    {
        Log.v(TAG, " onStatusNotify() $message ")
        updateConnectionIcon(ICameraConnectionStatus.CameraConnectionStatus.CONNECTING)

        try
        {
            runOnUiThread {
                //if (liveView != null) {
                //    liveView.invalidate()
                //}
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onCameraConnected()
    {
        Log.v(TAG, " onCameraConnected() ")
        updateConnectionIcon(ICameraConnectionStatus.CameraConnectionStatus.CONNECTED)

        try
        {
            runOnUiThread {
                //if (liveView != null) {
                //    liveView.invalidate()
                //}
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onCameraDisconnected()
    {
        Log.v(TAG, " onCameraDisconnected() ")
        try
        {
            updateConnectionIcon(ICameraConnectionStatus.CameraConnectionStatus.DISCONNECTED)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun updateConnectionIcon(connectionStatus : ICameraConnectionStatus.CameraConnectionStatus)
    {
        Log.v(TAG, " onCameraDisconnected() $connectionStatus")
        this.connectionStatus = connectionStatus
        try
        {
            runOnUiThread {
                try
                {
                    val view : ImageButton = this.findViewById(R.id.button_connect)
                    val iconId = when (connectionStatus)
                    {
                        ICameraConnectionStatus.CameraConnectionStatus.DISCONNECTED -> { R.drawable.ic_baseline_cloud_off_24 }
                        ICameraConnectionStatus.CameraConnectionStatus.UNKNOWN -> { R.drawable.ic_baseline_cloud_off_24 }
                        ICameraConnectionStatus.CameraConnectionStatus.CONNECTING -> { R.drawable.ic_baseline_cloud_queue_24 }
                        ICameraConnectionStatus.CameraConnectionStatus.CONNECTED -> { R.drawable.ic_baseline_cloud_done_24 }
                    }
                    view.setImageDrawable(ContextCompat.getDrawable(this, iconId))
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

    override fun onCameraConnectError(msg: String?)
    {
        Log.v(TAG, " onCameraConnectError() $msg ")
    }

    override fun getConnectionStatus(): ICameraConnectionStatus.CameraConnectionStatus
    {
        return (connectionStatus)
    }
}