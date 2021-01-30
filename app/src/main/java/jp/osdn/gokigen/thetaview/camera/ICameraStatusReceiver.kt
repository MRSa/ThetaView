package jp.osdn.gokigen.thetaview.camera

interface ICameraStatusReceiver
{
    fun onStatusNotify(message: String?)
    fun onCameraConnected()
    fun onCameraDisconnected()
    fun onCameraConnectError(msg: String?)
}
