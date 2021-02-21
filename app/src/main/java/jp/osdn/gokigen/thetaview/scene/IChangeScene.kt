package jp.osdn.gokigen.thetaview.scene

interface IChangeScene
{
    fun initializeFragment()
    fun connectToCamera()
    fun disconnectFromCamera()
    fun changeToLiveView()
    fun changeToPreview()
    fun changeToConfiguration()
    fun changeToDebugInformation()
    fun exitApplication()
    fun changeCaptureMode()
}
