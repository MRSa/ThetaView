package jp.osdn.gokigen.thetaview.scene

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothConnection
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothScanResult
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothStatusNotify
import jp.osdn.gokigen.thetaview.bluetooth.connection.eeg.MindWaveConnection
import jp.osdn.gokigen.thetaview.brainwave.BrainwaveDataHolder
import jp.osdn.gokigen.thetaview.brainwave.IDetectSensingReceiver
import jp.osdn.gokigen.thetaview.camera.theta.operation.IThetaShutter

class EEGShutter(activity : AppCompatActivity, private val bluetoothStatusNotify : IBluetoothStatusNotify, private val shutter: IThetaShutter) : IDetectSensingReceiver, IBluetoothScanResult
{
    private val bluetoothConnection = MindWaveConnection(activity, BrainwaveDataHolder(this), this)

    companion object
    {
        private val  TAG = EEGShutter::class.java.simpleName
    }

    fun connectToEEG()
    {
        try
        {
            Log.v(TAG, " connectToEEG()")
            bluetoothConnection.connect("MindWave Mobile")
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    fun disconnectFromEEG()
    {
        Log.v(TAG, " disconnectFromEEG()")
    }

    override fun startSensing()
    {
        TODO("Not yet implemented")
    }

    override fun detectAttention()
    {
        TODO("Not yet implemented")
    }

    override fun lostAttention()
    {
        TODO("Not yet implemented")
    }

    override fun detectAttentionThreshold()
    {
        TODO("Not yet implemented")
    }

    override fun detectMediation()
    {
        TODO("Not yet implemented")
    }

    override fun lostMediation()
    {
        TODO("Not yet implemented")
    }

    override fun detectMediationThreshold()
    {
        TODO("Not yet implemented")
    }

    override fun foundBluetoothDevice(device: BluetoothDevice)
    {
        TODO("Not yet implemented")
    }

    override fun notFindBluetoothDevice()
    {
        bluetoothStatusNotify.updateBluetoothStatus(IBluetoothConnection.ConnectionStatus.Ready)
    }
}