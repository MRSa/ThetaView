package jp.osdn.gokigen.thetaview.operation.imagefile

import androidx.camera.core.ImageCapture

interface IImageStore
{
    fun takePhoto(imageCapture : ImageCapture?) : Boolean
}
