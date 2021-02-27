package jp.osdn.gokigen.thetaview.bluetooth.connection

interface IBluetoothConnection
{
    fun connect(deviceName: String, loggingFlag: Boolean = false)
    fun disconnect()

    enum class ConnectionStatus
    {
        Undefined,
        Ready,
        Searching,
        Connected,
    }
}
