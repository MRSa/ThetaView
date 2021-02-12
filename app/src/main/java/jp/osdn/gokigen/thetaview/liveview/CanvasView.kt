package jp.osdn.gokigen.thetaview.liveview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View


class CanvasView : View
{
    private var showCameraStatus = false


    constructor(context: Context): super(context)
    {
        initComponent(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    {
        initComponent(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    {
        initComponent(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes)
    {
        initComponent(context)
    }

    private fun initComponent(context: Context)
    {
        Log.v(TAG, "initComponent")


    }

    override fun onDraw(canvas: Canvas?)
    {
        super.onDraw(canvas)
        if (canvas == null)
        {
            Log.v(TAG, " ===== onDraw : canvas is not ready. ==== ")
            return
        }
        //Log.v(TAG, " ----- onDraw() ----- ")
        //canvas.drawARGB(255, 0, 0, 0)

        if (showCameraStatus)
        {
            drawInformationMessages(canvas)
        }
    }

    fun refresh()
    {
        //Log.v(TAG, " refreshCanvas()")
        if (Looper.getMainLooper().thread === Thread.currentThread())
        {
            invalidate()
        }
        else
        {
            postInvalidate()
        }
    }

    /**
     * 　 画面にメッセージを表示する
     */
    private fun drawInformationMessages(canvas: Canvas)
    {
        var message: String
        val viewRect =  RectF(5.0f, 0.0f, canvas.width - 5.0f, canvas.height - 55.0f)

        message = " HELLO "
        val paint = Paint()
        paint.color = Color.GREEN
        paint.textSize = 16.0f
        paint.isAntiAlias = true
        paint.setShadowLayer(5.0f, 3.0f, 3.0f, Color.BLACK)
        val fontMetrics = paint.fontMetrics
        //val cx = canvas.width / 2.0f - paint.measureText(message) / 2.0f
        //val cy = canvas.height / 2.0f - (fontMetrics.ascent + fontMetrics.descent) / 2.0f
        val cx = 10.0f
        val cy = 20.0f
        canvas.drawText(message, cx, cy, paint)


/*
        // 画面の中心に表示する
        message = messageHolder.getMessage(ShowMessageHolder.MessageArea.CENTER)
        if (message != null && message.length > 0) {
            val paint = Paint()
            paint.color = messageHolder.getColor(ShowMessageHolder.MessageArea.CENTER)
            paint.textSize = messageHolder.getSize(ShowMessageHolder.MessageArea.CENTER)
            paint.isAntiAlias = true
            val fontMetrics = paint.fontMetrics
            val cx = canvas.width / 2.0f - paint.measureText(message) / 2.0f
            val cy = canvas.height / 2.0f - (fontMetrics.ascent + fontMetrics.descent) / 2.0f
            canvas.drawText(message, cx, cy, paint)
        }

        // 画面上部左側に表示する
        message = messageHolder.getMessage(ShowMessageHolder.MessageArea.UPLEFT)
        if (message != null && message.length > 0) {
            val paintUp = Paint()
            paintUp.color = messageHolder.getColor(ShowMessageHolder.MessageArea.UPLEFT)
            paintUp.textSize = messageHolder.getSize(ShowMessageHolder.MessageArea.UPLEFT)
            paintUp.isAntiAlias = true
            val fontMetrics = paintUp.fontMetrics
            canvas.drawText(message, viewRect.left + 3.0f, viewRect.top + (fontMetrics.descent - fontMetrics.ascent), paintUp)
        }

        // 画面上部右側に表示する
        message = messageHolder.getMessage(ShowMessageHolder.MessageArea.UPRIGHT)
        if (message != null && message.length > 0) {
            val paintUp = Paint()
            paintUp.color = messageHolder.getColor(ShowMessageHolder.MessageArea.UPRIGHT)
            paintUp.textSize = messageHolder.getSize(ShowMessageHolder.MessageArea.UPRIGHT)
            paintUp.isAntiAlias = true
            val width = paintUp.measureText(message)
            val fontMetrics = paintUp.fontMetrics
            canvas.drawText(message, viewRect.right - 3.0f - width, viewRect.top + (fontMetrics.descent - fontMetrics.ascent), paintUp)
        }

        // 画面下部左側に表示する
        message = messageHolder.getMessage(ShowMessageHolder.MessageArea.LOWLEFT)
        if (message != null && message.length > 0) {
            val paint = Paint()
            paint.color = messageHolder.getColor(ShowMessageHolder.MessageArea.LOWLEFT)
            paint.textSize = messageHolder.getSize(ShowMessageHolder.MessageArea.LOWLEFT)
            paint.isAntiAlias = true
            val fontMetrics = paint.fontMetrics
            canvas.drawText(message, viewRect.left + 3.0f, viewRect.bottom - fontMetrics.bottom, paint)
        }

        // 画面下部右側に表示する
        message = messageHolder.getMessage(LOWRIGHT)
        if (message != null && message.length > 0) {
            val paint = Paint()
            paint.color = messageHolder.getColor(LOWRIGHT)
            paint.textSize = messageHolder.getSize(LOWRIGHT)
            paint.isAntiAlias = true
            val width = paint.measureText(message)
            val fontMetrics = paint.fontMetrics
            canvas.drawText(message, viewRect.right - 3.0f - width, viewRect.bottom - fontMetrics.bottom, paint)
        }

        // 画面上部中央に表示する
        message = messageHolder.getMessage(ShowMessageHolder.MessageArea.UPCENTER)
        if (message != null && message.length > 0) {
            val paintUp = Paint()
            paintUp.color = messageHolder.getColor(ShowMessageHolder.MessageArea.UPCENTER)
            paintUp.textSize = messageHolder.getSize(ShowMessageHolder.MessageArea.UPCENTER)
            paintUp.isAntiAlias = true
            val width = paintUp.measureText(message) / 2.0f
            val fontMetrics = paintUp.fontMetrics
            canvas.drawText(message, viewRect.centerX() - width, viewRect.top + (fontMetrics.descent - fontMetrics.ascent), paintUp)
        }

        // 画面下部中央に表示する
        message = messageHolder.getMessage(ShowMessageHolder.MessageArea.LOWCENTER)
        if (message != null && message.length > 0) {
            val paint = Paint()
            paint.color = messageHolder.getColor(ShowMessageHolder.MessageArea.LOWCENTER)
            paint.textSize = messageHolder.getSize(ShowMessageHolder.MessageArea.LOWCENTER)
            paint.isAntiAlias = true
            val width = paint.measureText(message) / 2.0f
            val fontMetrics = paint.fontMetrics
            canvas.drawText(message, viewRect.centerX() - width, viewRect.bottom - fontMetrics.bottom, paint)
        }

        // 画面中央左に表示する
        message = messageHolder.getMessage(ShowMessageHolder.MessageArea.LEFTCENTER)
        if (message != null && message.length > 0) {
            val paint = Paint()
            paint.color = messageHolder.getColor(ShowMessageHolder.MessageArea.LEFTCENTER)
            paint.textSize = messageHolder.getSize(ShowMessageHolder.MessageArea.LEFTCENTER)
            paint.isAntiAlias = true
            paint.setShadowLayer(5.0f, 3.0f, 3.0f, Color.BLACK) // これで文字に影をつけたい
            val fontMetrics = paint.fontMetrics
            val cy = canvas.height / 2.0f - (fontMetrics.ascent + fontMetrics.descent) / 2.0f
            canvas.drawText(message, viewRect.left + 3.0f, cy, paint)
        }

        // 画面中央右に表示する
        message = messageHolder.getMessage(ShowMessageHolder.MessageArea.RIGHTCENTER)
        if (message != null && message.length > 0) {
            val paint = Paint()
            paint.color = messageHolder.getColor(ShowMessageHolder.MessageArea.RIGHTCENTER)
            paint.textSize = messageHolder.getSize(ShowMessageHolder.MessageArea.RIGHTCENTER)
            paint.isAntiAlias = true
            paint.setShadowLayer(5.0f, 3.0f, 3.0f, Color.BLACK) // これで文字に影をつけたい
            val width = paint.measureText(message)
            val fontMetrics = paint.fontMetrics
            val cy = canvas.height / 2.0f - (fontMetrics.ascent + fontMetrics.descent) / 2.0f
            canvas.drawText(message, viewRect.right - 3.0f - width, cy, paint)
        }
*/
    }

    fun setShowCameraStatus(isEnable : Boolean)
    {
        showCameraStatus = isEnable
    }

    companion object
    {
        private val TAG = CanvasView::class.java.simpleName
    }
}