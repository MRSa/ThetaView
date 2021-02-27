package jp.osdn.gokigen.thetaview.bluetooth.connection

interface IBluetoothStatusNotify
{
    fun updateBluetoothStatus(status : IBluetoothConnection.ConnectionStatus)
    fun updateBluetoothStatus()

}