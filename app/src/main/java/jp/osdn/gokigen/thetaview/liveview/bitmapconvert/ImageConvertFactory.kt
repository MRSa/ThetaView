package jp.osdn.gokigen.thetaview.liveview.bitmapconvert

import jp.osdn.gokigen.thetaview.liveview.IPreviewImageConverter

class ImageConvertFactory
{
    fun getImageConverter(id: Int): IPreviewImageConverter
    {
        return (ConvertNothing())
    }
}
