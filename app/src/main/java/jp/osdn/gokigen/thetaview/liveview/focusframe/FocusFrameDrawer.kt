package jp.osdn.gokigen.thetaview.liveview.focusframe


import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.util.TypedValue


class FocusFrameDrawer(private val context : Context): IFocusFrameDrawer, IFocusFrameControl
{
    override fun drawFocusFrame(canvas: Canvas)
    {
/*
        val focusRectOnImage: RectF = convertRectOnViewfinderIntoLiveImage(
            focusFrameRect,
            imageWidth,
            imageHeight,
            imageRotationDegrees
        )
        val focusRectOnView: RectF = convertRectFromImageArea(focusRectOnImage)
*/
        // Draw a rectangle to the canvas.

/*        // Draw a rectangle to the canvas.
        val focusFramePaint = Paint()
        focusFramePaint.style = Paint.Style.STROKE
        when (focusFrameStatus) {
            IFocusFrameControl.FocusFrameStatus.Running -> focusFramePaint.color = Color.WHITE
            IFocusFrameControl.FocusFrameStatus.Focused -> focusFramePaint.color = Color.GREEN
            IFocusFrameControl.FocusFrameStatus.Failed -> focusFramePaint.color = Color.RED
            IFocusFrameControl.FocusFrameStatus.Errored -> focusFramePaint.color = Color.YELLOW
        }
        val focusFrameStrokeWidth = 2.0f
        val dm: DisplayMetrics = context.getResources().getDisplayMetrics()
        val strokeWidth =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, focusFrameStrokeWidth, dm)
        focusFramePaint.strokeWidth = strokeWidth
        canvas.drawRect(focusRectOnView, focusFramePaint)
*/
    }

    override fun showFocusFrame(rect : RectF, status : IFocusFrameControl.FocusFrameStatus, duration : Float)
    {

    }

    override fun hideFocusFrame()
    {

    }


    private fun convertRectOnViewfinderIntoLiveImage(
        rect: RectF,
        width: Float,
        height: Float,
        rotatedDegrees: Int
    ): RectF
    {
        var top = 0.0f
        var bottom = 1.0f
        var left = 0.0f
        var right = 1.0f
        try
        {
            if (rotatedDegrees == 0 || rotatedDegrees == 180)
            {
                top = rect.top * height
                bottom = rect.bottom * height
                left = rect.left * width
                right = rect.right * width
            }
            else
            {
                left = rect.top * height
                right = rect.bottom * height
                top = rect.left * width
                bottom = rect.right * width
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return RectF(left, top, right, bottom)
    }

    private fun convertRectFromImageArea(rect: RectF, imageBitmap : Bitmap, imageRotationDegrees : Int): RectF
    {
        val imageTopLeft = PointF(rect.left, rect.top)
        val imageBottomRight = PointF(rect.right, rect.bottom)
        val viewTopLeft: PointF = convertPointFromImageArea(imageTopLeft, imageBitmap, imageRotationDegrees)
        val viewBottomRight: PointF = convertPointFromImageArea(imageBottomRight, imageBitmap, imageRotationDegrees)
        return RectF(viewTopLeft.x, viewTopLeft.y, viewBottomRight.x, viewBottomRight.y)
    }

    private fun convertPointFromImageArea(point: PointF, imageBitmap : Bitmap, imageRotationDegrees : Int): PointF
    {
        var viewPointX = point.x
        var viewPointY = point.y
/*
        val imageSizeWidth: Float
        val imageSizeHeight: Float
        if (imageRotationDegrees == 0 || imageRotationDegrees == 180) {
            imageSizeWidth = imageBitmap.getWidth().toFloat()
            imageSizeHeight = imageBitmap.getHeight().toFloat()
        } else {
            imageSizeWidth = imageBitmap.getHeight().toFloat()
            imageSizeHeight = imageBitmap.getWidth().toFloat()
        }
        val viewSizeWidth: Float = this.getWidth().toFloat()
        val viewSizeHeight: Float = this.getHeight().toFloat()
        val ratioX = viewSizeWidth / imageSizeWidth
        val ratioY = viewSizeHeight / imageSizeHeight
        val scale: Float
        when (imageScaleType)
        {
            ImageView.ScaleType.FIT_XY -> {
                viewPointX *= ratioX
                viewPointY *= ratioY
            }
            ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.CENTER_INSIDE -> {
                scale = Math.min(ratioX, ratioY)
                viewPointX *= scale
                viewPointY *= scale
                viewPointX += (viewSizeWidth - imageSizeWidth * scale) / 2.0f
                viewPointY += (viewSizeHeight - imageSizeHeight * scale) / 2.0f
            }
            ImageView.ScaleType.CENTER_CROP -> {
                scale = Math.max(ratioX, ratioY)
                viewPointX *= scale
                viewPointY *= scale
                viewPointX += (viewSizeWidth - imageSizeWidth * scale) / 2.0f
                viewPointY += (viewSizeHeight - imageSizeHeight * scale) / 2.0f
            }
            ImageView.ScaleType.CENTER -> {
                (viewPointX += viewSizeWidth / 2.0 - imageSizeWidth / 2.0f).toFloat()
                (viewPointY += viewSizeHeight / 2.0 - imageSizeHeight / 2.0f).toFloat()
            }
            else -> {
            }
        }
*/
        return PointF(viewPointX, viewPointY)
    }

}