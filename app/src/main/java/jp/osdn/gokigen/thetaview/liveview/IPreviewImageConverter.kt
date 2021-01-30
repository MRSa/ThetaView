package jp.osdn.gokigen.thetaview.liveview

import android.graphics.Bitmap

interface IPreviewImageConverter
{
    fun getModifiedBitmap(src: Bitmap): Bitmap
}
