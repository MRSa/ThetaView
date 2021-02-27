package jp.osdn.gokigen.thetaview.scene

import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothConnection

interface ICameraConnectionStatus
{
    enum class CameraConnectionStatus
    {
        UNKNOWN,  DISCONNECTED, CONNECTING, CONNECTED
    }

    fun getConnectionStatus(): CameraConnectionStatus
    fun getBluetoothConnectionStatus(): IBluetoothConnection.ConnectionStatus
}

