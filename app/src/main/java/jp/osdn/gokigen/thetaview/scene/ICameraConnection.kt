package jp.osdn.gokigen.thetaview.scene

interface ICameraConnection
{

    fun alertConnectingFailed(message: String?)
    fun forceUpdateConnectionStatus(status: ICameraConnectionStatus.CameraConnectionStatus)
}
