package jp.osdn.gokigen.thetaview.scene

interface ICameraConnectionStatus
{
    enum class CameraConnectionStatus
    {
        UNKNOWN,  DISCONNECTED, CONNECTING, CONNECTED
    }

    fun getConnectionStatus(): CameraConnectionStatus
}