package jp.osdn.gokigen.thetaview.liveview.focusframe

import android.graphics.RectF

interface IFocusFrameControl
{
    enum class FocusFrameStatus
    {
        Running, Focused, Failed, Errored
    }

    fun showFocusFrame(rect : RectF, status : FocusFrameStatus, duration : Float)
    fun hideFocusFrame()
}
