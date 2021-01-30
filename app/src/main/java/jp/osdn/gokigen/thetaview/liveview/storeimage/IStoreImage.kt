package jp.osdn.gokigen.thetaview.liveview.storeimage

import android.graphics.Bitmap

interface IStoreImage
{
    fun doStore(target: Bitmap? = null)
}
