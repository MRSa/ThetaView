package jp.osdn.gokigen.thetaview.scene

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.thetaview.IShowInformation
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothConnection

class MainButtonHandler(private val activity : AppCompatActivity, private val connectionStatus : ICameraConnectionStatus, private val showInformation : IShowInformation) : View.OnClickListener
{
    private lateinit var sceneChanger : SceneChanger

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.button_connect -> connect()
            R.id.button_camera -> camera()
            R.id.button_configure -> configure()
            R.id.button_bluetooth -> connectEEG()
            R.id.message -> message()
            else -> Log.v(TAG, " onClick : " + v?.id)
        }
    }

    fun setSceneChanger(changer : SceneChanger)
    {
        sceneChanger = changer
    }

    fun initialize()
    {
        activity.findViewById<ImageButton>(R.id.button_camera).setOnClickListener(this)
        activity.findViewById<ImageButton>(R.id.button_configure).setOnClickListener(this)
        activity.findViewById<ImageButton>(R.id.button_connect).setOnClickListener(this)
        activity.findViewById<ImageButton>(R.id.button_bluetooth).setOnClickListener(this)
        activity.findViewById<TextView>(R.id.message).setOnClickListener(this)
    }

    private fun connect()
    {
        if (connectionStatus.getConnectionStatus() != ICameraConnectionStatus.CameraConnectionStatus.CONNECTED)
        {
            Log.v(TAG, " - - - - - - - - - CONNECT - - - - - - - - -")
            showInformation.vibrate(IShowInformation.VibratePattern.SIMPLE_SHORT)
            sceneChanger.connectToCamera()
        }
        else
        {
            Log.v(TAG, " - - - - - - - - - DISCONNECT - - - - - - - - -")
            showInformation.vibrate(IShowInformation.VibratePattern.SIMPLE_LONG)
            sceneChanger.disconnectFromCamera()
        }
    }

    private fun connectEEG()
    {
        if (connectionStatus.getBluetoothConnectionStatus() != IBluetoothConnection.ConnectionStatus.Ready)
        {
            Log.v(TAG, " - - - - - - - - - CONNECT TO EEG - - - - - - - - -")
            showInformation.vibrate(IShowInformation.VibratePattern.SIMPLE_SHORT)
            sceneChanger.connectToEEG()
        }
        else
        {
            Log.v(TAG, " - - - - - - - - - DISCONNECT FROM EEG - - - - - - - - -")
            showInformation.vibrate(IShowInformation.VibratePattern.SIMPLE_LONG)
            sceneChanger.disconnectFromEEG()
        }
    }

    private fun camera()
    {
        Log.v(TAG, " - - - - - - - - - CAMERA - - - - - - - - -")
        sceneChanger.changeCaptureMode()
    }

    private fun configure()
    {
        Log.v(TAG, " - - - - - - - - - CONFIGURE - - - - - - - - -")
        showInformation.vibrate(IShowInformation.VibratePattern.SIMPLE_SHORT)
        sceneChanger.changeToConfiguration()
    }

    private fun message()
    {
        Log.v(TAG, " - - - - - - - - - MESSAGE - - - - - - - - -")
    }

    companion object
    {
        private val  TAG = MainButtonHandler::class.java.simpleName
    }
}
