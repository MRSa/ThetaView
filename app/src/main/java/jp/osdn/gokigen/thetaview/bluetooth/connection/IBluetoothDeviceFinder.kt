package jp.osdn.gokigen.thetaview.bluetooth.connection

interface IBluetoothDeviceFinder
{
    fun startScan(targetDeviceName: String)
    fun stopScan()
    fun reset()
}