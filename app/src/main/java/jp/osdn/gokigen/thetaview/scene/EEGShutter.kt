package jp.osdn.gokigen.thetaview.scene

import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothConnection
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothScanResult
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothStatusNotify
import jp.osdn.gokigen.thetaview.bluetooth.connection.eeg.MindWaveConnection
import jp.osdn.gokigen.thetaview.brainwave.BrainwaveDataHolder
import jp.osdn.gokigen.thetaview.brainwave.IDetectSensingReceiver
import jp.osdn.gokigen.thetaview.camera.theta.operation.IThetaShutter
import jp.osdn.gokigen.thetaview.preference.IPreferencePropertyAccessor

class EEGShutter(private val activity : AppCompatActivity, private val bluetoothStatusNotify : IBluetoothStatusNotify, private val shutter: IThetaShutter) : IDetectSensingReceiver, IBluetoothScanResult
{
    private val bluetoothConnection = MindWaveConnection(activity, BrainwaveDataHolder(this), bluetoothStatusNotify, this)
    private var useEEGSignalType : Int = 0
    private lateinit var indicator : IIndicator

    companion object
    {
        private val  TAG = EEGShutter::class.java.simpleName
    }

    fun setIndicator(indicator : IIndicator)
    {
        this.indicator = indicator
    }

    fun connectToEEG()
    {
        Log.v(TAG, " connectToEEG()")
        try
        {
            bluetoothConnection.connect("MindWave Mobile")
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        updateUseEEGSignal()
    }

    fun updateUseEEGSignal()
    {
        try
        {
            val signalType = PreferenceManager.getDefaultSharedPreferences(activity).getString(IPreferencePropertyAccessor.EEG_SIGNAL_USE_TYPE, IPreferencePropertyAccessor.EEG_SIGNAL_USE_TYPE_DEFAULT_VALUE)?.toInt()
            if (signalType != null)
            {
                useEEGSignalType = signalType
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    fun disconnectFromEEG()
    {
        try
        {
            val thread = Thread {
                try
                {
                    Log.v(TAG, " disconnectFromEEG()")
                }
                catch (e : Exception)
                {
                    e.printStackTrace()

                }
            }
            thread.start()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun startSensing()
    {
        Log.v(TAG, " startSensing()")
    }

    override fun detectAttention()
    {
        Log.v(TAG, " detectAttention()")
    }

    override fun lostAttention()
    {
        Log.v(TAG, " lostAttention()")
        if (useEEGSignalType == 0)
        {
            // シャッターを止める
            shutter.doShutterOff()
        }
    }

    override fun detectAttentionThreshold()
    {
        Log.v(TAG, " detectAttentionThreshold()")
        if (useEEGSignalType == 0)
        {
            // シャッターを稼働させる
            shutter.doShutter()
        }
    }

    override fun detectMediation()
    {
        Log.v(TAG, " detectMediation()")
    }

    override fun lostMediation()
    {
        Log.v(TAG, " lostMediation()")
        if (useEEGSignalType != 0)
        {
            // シャッターを止める
            shutter.doShutterOff()
        }
    }

    override fun detectMediationThreshold()
    {
        Log.v(TAG, " detectMediationThreshold()")
        if (useEEGSignalType != 0)
        {
            // シャッターを稼働させる
            shutter.doShutter()
        }
    }

    override fun foundBluetoothDevice(device: BluetoothDevice)
    {
        Log.v(TAG, " foundBluetoothDevice() : ${device.name}")
    }

    override fun notFindBluetoothDevice()
    {
        bluetoothStatusNotify.updateBluetoothStatus(IBluetoothConnection.ConnectionStatus.Ready)
    }

    override fun updateSummaryValue(attention: Int, mediation: Int)
    {
        Log.v(TAG, "  ATTENTION : $attention   MEDIATION : $mediation")
        try
        {
            if (::indicator.isInitialized)
            {
                val colorAttention = when {
                    attention > 90 -> { Color.GREEN }
                    attention > 70 -> { Color.YELLOW }
                    attention > 50 -> { Color.WHITE }
                    attention > 30 -> { Color.LTGRAY }
                    else -> { Color.DKGRAY }
                }
                indicator.setMessage(IIndicator.Area.AREA_A, colorAttention, "ATTENTION : $attention")

                val colorMediation = when {
                    mediation > 90 -> { Color.GREEN }
                    mediation > 70 -> { Color.YELLOW }
                    mediation > 50 -> { Color.WHITE }
                    mediation > 30 -> { Color.LTGRAY }
                    else -> { Color.DKGRAY }
                }
                indicator.setMessage(IIndicator.Area.AREA_B, colorMediation, "MEDIATION : $mediation")
                indicator.invalidate()
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }
}
