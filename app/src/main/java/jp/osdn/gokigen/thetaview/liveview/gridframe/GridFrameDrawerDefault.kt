package jp.osdn.gokigen.thetaview.liveview.gridframe
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class GridFrameDrawerDefault : IGridFrameDrawer
{
    override fun drawFramingGrid(canvas: Canvas, rect: RectF)
    {
        val paint = Paint()
        paint.color = Color.argb(130, 235, 235, 235)
        paint.strokeWidth = 1.0f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        val width = (rect.right - rect.left) / 3.0f
        val height = (rect.bottom - rect.top) / 3.0f

        canvas.drawLine(rect.left + width, rect.top, rect.left + width, rect.bottom, paint)
        canvas.drawLine(rect.left + 2.0f * width, rect.top, rect.left + 2.0f * width, rect.bottom, paint)
        canvas.drawLine(rect.left, rect.top + height, rect.right, rect.top + height, paint)
        canvas.drawLine(rect.left, rect.top + 2.0f * height, rect.right, rect.top + 2.0f * height, paint)
        canvas.drawRect(rect, paint)
    }
}
