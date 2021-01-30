package jp.osdn.gokigen.thetaview.liveview.image

interface ILiveViewListener
{
    fun getImageByteArray() : ByteArray
    fun setCameraLiveImageView(target: IImageDataReceiver)
}
