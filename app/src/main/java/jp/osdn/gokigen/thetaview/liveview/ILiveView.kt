package jp.osdn.gokigen.thetaview.liveview

import jp.osdn.gokigen.thetaview.liveview.image.IImageProvider

interface ILiveView
{
    fun setImageProvider(provider : IImageProvider)
    fun updateImageRotation(degrees : Int)

    fun getMessageDrawer() : IMessageDrawer
}
