package jp.osdn.gokigen.thetaview.camera.theta.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.thetaview.scene.IIndicator
import jp.osdn.gokigen.thetaview.utils.communication.SimpleHttpClient
import org.json.JSONObject

class ThetaCameraStatusWatcher(private val sessionIdProvider: IThetaSessionIdProvider, private val captureModeReceiver : ICaptureModeReceiver, private val executeUrl : String = "http://192.168.1.1") : ICameraStatusWatcher, IThetaStatusHolder
{
    private val httpClient = SimpleHttpClient()
    private var whileFetching = false
    private var currentIsoSensitivity : Int = 0
    private var currentBatteryLevel : Double = 0.0
    private var currentAperture : Double = 0.0
    private var currentShutterSpeed : Double = 0.0
    private var currentExposureCompensation : Double = 0.0
    private var currentCaptureMode : String = ""
    private var currentExposureProgram : String = ""
    private var currentCaptureStatus : String = ""
    private var currentWhiteBalance : String = ""
    private var currentFilter : String = ""

    private var showInformation: IIndicator? = null

    override fun startStatusWatch(indicator : IIndicator?)
    {
        if (whileFetching)
        {
            Log.v(TAG, "startStatusWatch() already starting.")
            return
        }
        showInformation = indicator
        whileFetching = true

        try
        {
            setMessage(IIndicator.Area.AREA_1, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_2, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_3, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_4, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_5, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_6, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_7, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_8, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_9, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_10, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_11, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_12, Color.WHITE, "")

            setMessage(IIndicator.Area.AREA_A, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_B, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_C, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_D, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_E, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_F, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_G, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_H, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_I, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_J, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_K, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_L, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_M, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_N, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_O, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_P, Color.WHITE, "")
            setMessage(IIndicator.Area.AREA_Q, Color.WHITE, "")
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }

        try
        {
            val thread = Thread {
                try
                {
                    val getOptionsUrl = "$executeUrl/osc/commands/execute"
                    val getStateUrl = "$executeUrl/osc/state"

                    val postDataCaptureMode = if (sessionIdProvider.sessionId.isEmpty()) "{\"name\":\"camera.getOptions\",\"parameters\":{\"timeout\":0, \"optionNames\" : [ \"captureMode\"] }}" else "{\"name\":\"camera.getOptions\",\"parameters\":{\"sessionId\": \"" + sessionIdProvider.sessionId + "\", \"optionNames\" : [ \"captureMode\" ] }}"
                    val postDataImage = if (sessionIdProvider.sessionId.isEmpty()) "{\"name\":\"camera.getOptions\",\"parameters\":{\"timeout\":0, \"optionNames\" : [ \"aperture\",\"captureMode\",\"exposureCompensation\",\"exposureProgram\",\"iso\",\"shutterSpeed\",\"_filter\",\"whiteBalance\"] }}" else "{\"name\":\"camera.getOptions\",\"parameters\":{\"sessionId\": \"" + sessionIdProvider.sessionId + "\", \"optionNames\" : [ \"aperture\",\"captureMode\",\"exposureCompensation\",\"exposureProgram\",\"iso\",\"shutterSpeed\",\"_filter\",\"whiteBalance\"] }}"
                    val postDataVideo = if (sessionIdProvider.sessionId.isEmpty()) "{\"name\":\"camera.getOptions\",\"parameters\":{\"timeout\":0, \"optionNames\" : [ \"aperture\",\"captureMode\",\"exposureCompensation\",\"exposureProgram\",\"iso\",\"shutterSpeed\",\"whiteBalance\"] }}" else "{\"name\":\"camera.getOptions\",\"parameters\":{\"sessionId\": \"" + sessionIdProvider.sessionId + "\", \"optionNames\" : [ \"aperture\",\"captureMode\",\"exposureCompensation\",\"exposureProgram\",\"iso\",\"shutterSpeed\",\"_filter\",\"whiteBalance\"] }}"
                    Log.v(TAG, " >>>>> START STATUS WATCH : $getOptionsUrl")
                    while (whileFetching)
                    {
                        val response0: String? = httpClient.httpPostWithHeader(getOptionsUrl, postDataCaptureMode, null, "application/json;charset=utf-8", timeoutMs)
                        if (!(response0.isNullOrEmpty()))
                        {
                            // 設定データ受信、解析する
                            checkStatus0(response0)
                        }
                        val postData = if (currentCaptureMode != "image") { postDataVideo } else { postDataImage }
                        val response1: String? = httpClient.httpPostWithHeader(getOptionsUrl, postData, null, "application/json;charset=utf-8", timeoutMs)
                        if (!(response1.isNullOrEmpty()))
                        {
                            // 設定データ受信、解析する
                            checkStatus1(response1)
                        }
                        val response2: String? = httpClient.httpPostWithHeader(getStateUrl, "", null, "application/json;charset=utf-8", timeoutMs)
                        if (!(response2.isNullOrEmpty()))
                        {
                            // ステータスデータ受信、解析する
                            checkStatus2(response2)
                        }
                        try
                        {
                            // 表示を更新する
                            invalidate()

                            // ちょっと休む
                            Thread.sleep(loopWaitMs)
                        }
                        catch (e: Exception)
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
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun checkStatus0(response: String)
    {
        try
        {
            //Log.v(TAG, " STATUS0 : $response")
            val stateObject = JSONObject(response).getJSONObject("results").getJSONObject("options")
            try
            {
                val captureMode = stateObject.getString(THETA_CAPTURE_MODE)
                if (captureMode != currentCaptureMode)
                {
                    Log.v(TAG, " CapMode : $currentCaptureMode -> $captureMode")
                    currentCaptureMode = captureMode
                    captureModeReceiver.changedCaptureMode(captureMode)
                    setMessage(IIndicator.Area.AREA_2, Color.WHITE, "Capture Mode : $captureMode")
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun checkStatus1(response: String)
    {
        try
        {
            //Log.v(TAG, " STATUS1 : $response")
            val stateObject = JSONObject(response).getJSONObject("results").getJSONObject("options")
            try
            {
                val exposureCompensation = stateObject.getDouble(THETA_EXPOSURE_COMPENSATION)
                if (exposureCompensation != currentExposureCompensation)
                {
                    Log.v(TAG, " XV : $currentExposureCompensation => $exposureCompensation")
                    currentExposureCompensation = exposureCompensation
                    if (currentExposureCompensation == 0.0)
                    {
                        // 補正なしの時には数値を表示しない
                        setMessage(IIndicator.Area.AREA_7, Color.WHITE, "")
                    }
                    else
                    {
                        setMessage(IIndicator.Area.AREA_7, Color.WHITE, String.format("%1.1f", currentExposureCompensation))
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

            try
            {
                val whiteBalance = stateObject.getString(THETA_WHITE_BALANCE)
                if (whiteBalance != currentWhiteBalance)
                {
                    Log.v(TAG, " WB : $currentWhiteBalance => $whiteBalance")
                    currentWhiteBalance = whiteBalance
                    setMessage(IIndicator.Area.AREA_6, Color.WHITE, "WB : $currentWhiteBalance")
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

            try
            {
                val exposureProgram = stateObject.getString(THETA_EXPOSURE_PROGRAM)
                if (exposureProgram != currentExposureProgram)
                {
                    Log.v(TAG, " ExpPrg : $currentExposureProgram -> $exposureProgram")
                    currentExposureProgram = exposureProgram

                    var mode = ""
                    when (currentExposureProgram) {
                        "1" -> mode = "Manual"
                        "2" -> mode = "Normal"
                        "3" -> mode = "Aperture"
                        "4" -> mode = "Shutter"
                        "9" -> mode = "ISO"
                    }
                    setMessage(IIndicator.Area.AREA_3, Color.WHITE, "Exposure Program : $mode")
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

            if (currentCaptureMode == "image")
            {
                try
                {
                    val filterValue = stateObject.getString(THETA_FILTER)
                    if (filterValue != currentFilter)
                    {
                        Log.v(TAG, " FILTER : $currentFilter -> $filterValue")
                        currentFilter = filterValue
                        setMessage(IIndicator.Area.AREA_5, Color.WHITE, "FILTER : $currentFilter")
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            else
            {
                currentFilter = ""
            }

            try
            {
                val isoSensitivity = stateObject.getInt(THETA_ISO_SENSITIVITY)
                if (isoSensitivity != currentIsoSensitivity)
                {
                    Log.v(TAG, " ISO : $currentIsoSensitivity -> $isoSensitivity")
                    currentIsoSensitivity = isoSensitivity
                    if (currentIsoSensitivity == 0)
                    {
                        setMessage(IIndicator.Area.AREA_8, Color.WHITE, "ISO : AUTO")
                    }
                    else
                    {
                        setMessage(IIndicator.Area.AREA_8, Color.WHITE, "ISO : $currentIsoSensitivity")
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

            try
            {
                val aperture = stateObject.getDouble(THETA_APERTURE)
                if (aperture != currentAperture)
                {
                    Log.v(TAG, " A : $currentAperture -> $aperture")
                    currentAperture = aperture
                    if ((currentExposureProgram == "1")||(currentExposureProgram == "3"))
                    {
                        if (currentAperture == 0.0)
                        {
                            setMessage(IIndicator.Area.AREA_10, Color.WHITE, "F:auto")
                        }
                        else
                        {
                            setMessage(IIndicator.Area.AREA_10, Color.WHITE, "F$currentAperture")
                        }
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

            try
            {
                val shutterSpeed = stateObject.getDouble(THETA_SHUTTER_SPEED)
                if (shutterSpeed != currentShutterSpeed)
                {
                    Log.v(TAG, " SS : $currentShutterSpeed -> $shutterSpeed")
                    currentShutterSpeed = shutterSpeed
                    if (currentShutterSpeed == 0.0)
                    {
                        setMessage(IIndicator.Area.AREA_9, Color.WHITE, "")
                    }
                    else
                    {
                        setMessage(IIndicator.Area.AREA_9, Color.WHITE, convertShutterSpeedString(currentShutterSpeed))
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

        }
        catch (ee: Exception)
        {
            ee.printStackTrace()
        }
    }

    private fun convertShutterSpeedString(shutterSpeed : Double) : String
    {
        var inv = 0.0
        var stringValue = ""
        try
        {
            if (shutterSpeed  < 1.0)
            {
                inv = 1.0 / shutterSpeed
            }
            if (inv < 2.0) // if (inv < 10.0)
            {
                inv = 0.0
            }
            if (inv > 0.0f)
            {
                // シャッター速度を分数で表示する
                var intValue = inv.toInt()
                val modValue = intValue % 10
                if (modValue == 9 || modValue == 4)
                {
                    // ちょっと格好が悪いけど...切り上げ
                    intValue++
                }
                stringValue = "1/$intValue"
            }
            else
            {
                // シャッター速度を数値(秒数)で表示する
                stringValue = "${shutterSpeed}s "
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (stringValue)
    }

    private fun checkStatus2(response: String)
    {
        try
        {
            //Log.v(TAG, " STATUS2 : $response")
            val stateObject = JSONObject(response).getJSONObject("state")
            try
            {
                val captureStatus = stateObject.getString(THETA_CAPTURE_STATUS)
                if (captureStatus != currentCaptureStatus)
                {
                    Log.v(TAG, " CapStatus : $currentCaptureStatus -> $captureStatus")
                    setMessage(IIndicator.Area.AREA_4, Color.WHITE, "STATUS : $captureStatus")
                    currentCaptureStatus = captureStatus
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
            try
            {
                val batteryLevel = stateObject.getDouble(THETA_BATTERY_LEVEL)
                if (batteryLevel != currentBatteryLevel)
                {
                    Log.v(TAG, " BATTERY : $currentBatteryLevel => $batteryLevel")
                    currentBatteryLevel = batteryLevel
                    updateRemainBattery(currentBatteryLevel)
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

        }
        catch (ee: Exception)
        {
            ee.printStackTrace()
        }
    }

    private fun updateRemainBattery(percentageDouble: Double)
    {
        var color = Color.WHITE
        if (percentageDouble < 0.5)
        {
            color = Color.YELLOW
        }
        if (percentageDouble < 0.3)
        {
                color = Color.RED
        }
        try
        {
            val percentage = kotlin.math.ceil(percentageDouble * 100.0).toInt()
            setMessage(IIndicator.Area.AREA_1, color, "BATTERY : $percentage%")
        }
        catch (ee: java.lang.Exception)
        {
            ee.printStackTrace()
        }
    }

    private fun setMessage(area: IIndicator.Area, color: Int, message: String)
    {
        if (showInformation != null)
        {
            showInformation?.setMessage(area, color, message)
        }
    }

    private fun invalidate()
    {
        if (showInformation != null)
        {
            showInformation?.invalidate()
        }
    }

    override fun stopStatusWatch()
    {
        whileFetching = false
    }

    companion object
    {
        private val TAG = ThetaCameraStatusWatcher::class.java.simpleName
        private const val timeoutMs = 3300
        private const val loopWaitMs : Long = 400

        private const val THETA_BATTERY_LEVEL = "batteryLevel"
        private const val THETA_CAPTURE_STATUS = "_captureStatus"

/*
        private const val THETA_BLUETOOTH_ROLE  = "_bluetoothRole"
        private const val THETA_BLUETOOTH_POWER = "_bluetoothPower"
        private const val THETA_BLUETOOTH_CLASSIC_ENABLE  = "_bluetoothClassicEnable"

        private const val THETA_RECORDING_SEC = "_recordedTime"
        private const val THETA_BATTERY_STATE = "_batteryState"
        private const val THETA_CURRENT_API_LEVEL = "_apiVersion"
        private const val THETA_SHOOTING_FUNCTION = "_function"
        private const val THETA_CAMERA_ERROR = "_cameraError"
 */

        private const val THETA_APERTURE = "aperture"
        private const val THETA_CAPTURE_MODE = "captureMode"
        private const val THETA_EXPOSURE_COMPENSATION = "exposureCompensation"
        private const val THETA_EXPOSURE_PROGRAM = "exposureProgram"
        private const val THETA_ISO_SENSITIVITY = "iso"
        private const val THETA_SHUTTER_SPEED = "shutterSpeed"
        private const val THETA_WHITE_BALANCE = "whiteBalance"
        private const val THETA_FILTER = "_filter"
    }

    override var captureMode: String
        get() = currentCaptureMode
        set(value) { currentCaptureMode = value }
}
