package jp.osdn.gokigen.thetaview.scene

import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothConnection
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothScanResult
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothStatusNotify
import jp.osdn.gokigen.thetaview.bluetooth.connection.eeg.MindWaveConnection
import jp.osdn.gokigen.thetaview.brainwave.BrainwaveDataHolder
import jp.osdn.gokigen.thetaview.brainwave.BrainwaveSummaryData
import jp.osdn.gokigen.thetaview.brainwave.IDetectSensingReceiver
import jp.osdn.gokigen.thetaview.camera.theta.operation.IThetaShutter
import jp.osdn.gokigen.thetaview.preference.IPreferencePropertyAccessor
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class EEGShutter(private val activity : AppCompatActivity, private val bluetoothStatusNotify : IBluetoothStatusNotify, private val shutter: IThetaShutter) : IDetectSensingReceiver, IBluetoothScanResult
{
    private val bluetoothConnection = MindWaveConnection(activity, BrainwaveDataHolder(this), bluetoothStatusNotify, this)
    private var useEEGSignalType : Int = 0
    private var showEEGSignal = false
    private var storeEEGSignal = false
    private lateinit var indicator : IIndicator
    private var outputCsvFile : File? = null

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
            val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val signalType = preferences.getString(IPreferencePropertyAccessor.EEG_SIGNAL_USE_TYPE, IPreferencePropertyAccessor.EEG_SIGNAL_USE_TYPE_DEFAULT_VALUE)?.toInt()
            if (signalType != null)
            {
                useEEGSignalType = signalType
            }
            showEEGSignal = preferences.getBoolean(IPreferencePropertyAccessor.SHOW_EEG_WAVE_SIGNAL, IPreferencePropertyAccessor.SHOW_EEG_WAVE_SIGNAL_DEFAULT_VALUE)
            storeEEGSignal = preferences.getBoolean(IPreferencePropertyAccessor.RECORD_EEG_WAVE_SIGNAL, IPreferencePropertyAccessor.RECORD_EEG_WAVE_SIGNAL_DEFAULT_VALUE)

            if (storeEEGSignal)
            {
                val now = System.currentTimeMillis()
                @Suppress("DEPRECATION") val path =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator
                val fileName = "EEG" + SimpleDateFormat("_yyyyMMddHHmmss", Locale.US).format(now) + ".csv"
                try
                {
                    outputCsvFile = File(path + fileName)
                    outputCsvFile?.createNewFile()
                    if (outputCsvFile != null)
                    {
                        outputCsvFile?.appendText("; Date, attention, mediation, delta, theta, Alpha-Low, Alpha-High, Beta-Low, Beta-High, Gamma-Low, Gamma-Mid, PoorSignal, ;\r\n", Charset.defaultCharset())
                    }
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
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

    @ExperimentalUnsignedTypes
    override fun updateSummaryValue(summaryValue : BrainwaveSummaryData)
    {
        val attention = summaryValue.getAttention()
        val mediation = summaryValue.getMediation()

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

                if (showEEGSignal)
                {
                    // とりうる値 : 3bytes
                    indicator.setMessage(IIndicator.Area.AREA_D, Color.DKGRAY, "${summaryValue.getDelta()} : 0.5-2.75Hz")
                    indicator.setMessage(IIndicator.Area.AREA_E, Color.DKGRAY, "${summaryValue.getTheta()} : 3.5-6.75Hz")
                    indicator.setMessage(IIndicator.Area.AREA_F, Color.DKGRAY, "${summaryValue.getLowAlpha()} : 7.5-9.25Hz")
                    indicator.setMessage(IIndicator.Area.AREA_G, Color.DKGRAY, "${summaryValue.getHighAlpha()} : 10-11.75Hz")
                    indicator.setMessage(IIndicator.Area.AREA_H, Color.DKGRAY, "${summaryValue.getLowBeta()} : 13-16.75Hz")
                    indicator.setMessage(IIndicator.Area.AREA_I, Color.DKGRAY, "${summaryValue.getHighBeta()} : 18-29.75Hz")
                    indicator.setMessage(IIndicator.Area.AREA_J, Color.DKGRAY, "${summaryValue.getLowGamma()} : 31-39.75Hz")
                    indicator.setMessage(IIndicator.Area.AREA_K, Color.DKGRAY, "${summaryValue.getMidGamma()} : 41-49.75Hz")
                }

                if (!summaryValue.isSkinConnected())
                {
                    indicator.setMessage(IIndicator.Area.AREA_Q, Color.YELLOW, "CHECK EEG CONTACT")
                }
                indicator.invalidate()

                outputEEGDataFile(summaryValue)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    @ExperimentalUnsignedTypes
    private fun outputEEGDataFile(summaryValue : BrainwaveSummaryData)
    {
        try
        {
            val date = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS", Locale.US).format(System.currentTimeMillis())
            val outputData = "$date,${summaryValue.getAttention()},${summaryValue.getMediation()},${summaryValue.getDelta()},${summaryValue.getTheta()},${summaryValue.getLowAlpha()},${summaryValue.getHighAlpha()},${summaryValue.getLowBeta()},${summaryValue.getHighBeta()},${summaryValue.getLowGamma()},${summaryValue.getMidGamma()},${summaryValue.getPoorSignal()},;\r\n"
            if (outputCsvFile != null)
            {
                outputCsvFile?.appendText(outputData, Charset.defaultCharset())
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

}
