package jp.osdn.gokigen.thetaview.liveview.bitmapconvert

import android.graphics.Bitmap
import jp.osdn.gokigen.thetaview.liveview.IPreviewImageConverter

class ConvertNothing : IPreviewImageConverter
{
    override fun getModifiedBitmap(src: Bitmap): Bitmap
    {
        return (src)
    }
}
