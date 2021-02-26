package jp.osdn.gokigen.thetaview.bluetooth.connection

import android.bluetooth.BluetoothDevice

interface IBluetoothScanResult
{
    fun foundBluetoothDevice(device: BluetoothDevice)
    fun notFindBluetoothDevice()
}
