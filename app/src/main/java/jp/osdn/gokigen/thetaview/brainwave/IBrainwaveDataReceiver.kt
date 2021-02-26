package jp.osdn.gokigen.thetaview.brainwave

interface IBrainwaveDataReceiver
{
    fun receivedRawData(value: Int)
    fun receivedSummaryData(data: ByteArray?)
}
