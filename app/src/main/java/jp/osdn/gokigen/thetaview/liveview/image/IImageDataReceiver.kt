package jp.osdn.gokigen.thetaview.liveview.image

interface IImageDataReceiver
{
    fun onUpdateLiveView(data: ByteArray, metadata: Map<String, Any>?)
}
