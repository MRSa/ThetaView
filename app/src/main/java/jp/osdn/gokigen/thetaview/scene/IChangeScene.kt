package jp.osdn.gokigen.thetaview.scene

interface IChangeScene
{
    fun initializeFragment()
    fun connectToCamera()
    fun disconnectFromCamera()
    fun connectToEEG()
    fun disconnectFromEEG()
    fun changeToLiveView()
    fun changeToPreview()
    fun changeToConfiguration()
    fun changeToDebugInformation()
    fun exitApplication()
    fun changeCaptureMode()
}
