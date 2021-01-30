package jp.osdn.gokigen.thetaview.operation

import android.view.View
import jp.osdn.gokigen.thetaview.liveview.ILiveView
import jp.osdn.gokigen.thetaview.liveview.ILiveViewRefresher

interface ICameraControl
{
    fun initialize()
    fun startCamera(isPreviewView : Boolean = true)
    fun finishCamera()

    fun connectToCamera()

    fun setRefresher(refresher : ILiveViewRefresher, imageView : ILiveView)
    fun captureButtonReceiver() : View.OnClickListener
}
