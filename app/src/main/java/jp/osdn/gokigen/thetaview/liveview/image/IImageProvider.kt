package jp.osdn.gokigen.thetaview.liveview.image

import android.graphics.Bitmap

interface IImageProvider
{
    fun getImage(position: Float = 0.0f) : Bitmap
}
