package jp.osdn.gokigen.thetaview.liveview

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import jp.osdn.gokigen.thetaview.liveview.focusframe.FocusFrameDrawer
import jp.osdn.gokigen.thetaview.liveview.gridframe.GridFrameFactory
import jp.osdn.gokigen.thetaview.liveview.gridframe.IGridFrameDrawer
import jp.osdn.gokigen.thetaview.liveview.image.IImageProvider
import jp.osdn.gokigen.thetaview.liveview.message.InformationDrawer
import kotlin.math.min

class LiveImageView : View, ILiveView, ILiveViewRefresher, IShowGridFrame
{
    private var rotationDegrees : Int = 0
    private var showGrid : Boolean = false
    private lateinit var imageProvider : IImageProvider
    private lateinit var gridFrameDrawer : IGridFrameDrawer
    private lateinit var focusFrameDrawer : FocusFrameDrawer
    private lateinit var informationDrawer : InformationDrawer
/*
    private lateinit var renderer : GokigenGLRenderer
*/

    companion object
    {
        private val TAG = toString()
    }

    constructor(context: Context) : super(context)
    {
        initComponent(context)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initComponent(context)
    }
/**/
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        initComponent(context)
    }
    //constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    //{
    //    initComponent(context)
    //}
/**/

/*
    override fun surfaceCreated(holder : SurfaceHolder)
    {
        Log.v(TAG, " surfaceCreated() ")

    }
*/

    private fun initComponent(context: Context)
    {
        Log.v(TAG, " initComponent() ")
        gridFrameDrawer = GridFrameFactory().getGridFrameDrawer(0)
        focusFrameDrawer = FocusFrameDrawer(context)
        informationDrawer = InformationDrawer()

/*
        ////// for GLSurfaceView
        setEGLContextClientVersion(1)
        renderer = GokigenGLRenderer(context, LiveViewDrawer(context))
        setRenderer(renderer)
        holder.setFormat(PixelFormat.TRANSLUCENT)  // 透過
        renderMode = RENDERMODE_CONTINUOUSLY       // Continuousモード
*/
    }

    override fun getMessageDrawer() : IMessageDrawer
    {
        return (informationDrawer)
    }

    override fun refresh()
    {
        refreshCanvas()
    }

    override fun setImageProvider(provider: IImageProvider)
    {
        this.imageProvider = provider
    }

    override fun updateImageRotation(degrees: Int)
    {
        this.rotationDegrees = degrees
        refreshCanvas()
    }

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)
/**/
        //if ((canvas == null)||(!(::imageProvider.isInitialized)))
        if (!(::imageProvider.isInitialized))
        {
            Log.v(TAG, " ===== onDraw : canvas is not ready. ==== ")
            return
        }
        //Log.v(TAG, " ----- onDraw() ----- ")
        canvas.drawARGB(255, 0, 0, 0)
        val imageRectF = drawImage(canvas)
        if (showGrid)
        {
            gridFrameDrawer.drawFramingGrid(canvas, imageRectF)
        }
        focusFrameDrawer.drawFocusFrame(canvas)
        informationDrawer.drawInformationMessages(canvas, imageRectF)
        informationDrawer.drawLevelGauge(canvas, rotationDegrees)
/**/
    }

    override fun showGridFrame(isShowGrid: Boolean)
    {
        this.showGrid = isShowGrid
        refreshCanvas()
    }

    private fun drawImage(canvas: Canvas) : RectF
    {
        val centerX = canvas.width / 2
        val centerY = canvas.height / 2

        val paint = Paint(Color.LTGRAY)
        paint.strokeWidth = 1.0f
        paint.style = Paint.Style.STROKE

        val imageBitmap = imageProvider.getImage()

        var addDegrees = 0
        try
        {
            val config = context.resources.configuration
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                addDegrees = 90
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        val degrees = rotationDegrees + addDegrees

        val viewRect = decideViewRect(canvas, imageBitmap, degrees)
        val imageRect = Rect(0, 0, imageBitmap.width, imageBitmap.height)

        //Log.v(TAG, " canvas:   ${canvas.width} x ${canvas.height} (D: ${rotationDegrees}) ")
        //Log.v(TAG, " bitmap:   ${imageBitmap.width} x ${imageBitmap.height} (D: ${rotationDegrees}) ")
        //Log.v(TAG, " imageRect: [${imageRect.left},${imageRect.top}]-[${imageRect.right},${imageRect.bottom}] ")
        //Log.v(TAG, " viewRect: [${viewRect.left},${viewRect.top}]-[${viewRect.right},${viewRect.bottom}] ")

        canvas.rotate(degrees.toFloat(), centerX.toFloat(), centerY.toFloat())
        canvas.drawBitmap(imageBitmap, imageRect, viewRect, paint)
        canvas.rotate(-degrees.toFloat(), centerX.toFloat(), centerY.toFloat())

        return (viewRect)
    }

    private fun refreshCanvas()
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

    private fun decideViewRect(canvas: Canvas, bitmapToShow: Bitmap, rotationDegrees: Int): RectF
    {
        val srcWidth: Int
        val srcHeight: Int
        if ((rotationDegrees == 0)||(rotationDegrees == 180))
        {
            srcWidth = bitmapToShow.width
            srcHeight = bitmapToShow.height
        }
        else
        {
            srcWidth = bitmapToShow.height
            srcHeight = bitmapToShow.width
        }
        val maxWidth = canvas.width
        val maxHeight = canvas.height
        val centerX = canvas.width / 2
        val centerY = canvas.height / 2
        val widthRatio = maxWidth / srcWidth.toFloat()
        val heightRatio = maxHeight / srcHeight.toFloat()
        val smallRatio = min(widthRatio, heightRatio)
        val dstWidth: Float
        val dstHeight: Float
        if (widthRatio < heightRatio)
        {
            dstWidth = maxWidth.toFloat()
            dstHeight = (smallRatio * srcHeight)
        }
        else
        {
            dstHeight = maxHeight.toFloat()
            dstWidth = (smallRatio * srcWidth)
        }
        val halfWidth = dstWidth * 0.5f
        val halfHeight = dstHeight * 0.5f
        return (if (rotationDegrees == 0 || rotationDegrees == 180)
        {
            RectF((centerX - halfWidth), (centerY - halfHeight), ((centerX - halfWidth) + dstWidth), ((centerY - halfHeight) + dstHeight))
        }
        else
        {
            RectF((centerX - halfHeight), (centerY - halfWidth), ((centerX - halfHeight) + dstHeight), ((centerY - halfWidth) + dstWidth))
        })
    }
}
