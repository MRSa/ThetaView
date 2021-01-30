package jp.osdn.gokigen.thetaview.liveview

import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import jp.osdn.gokigen.thetaview.liveview.glrenderer.*
import jp.osdn.gokigen.thetaview.liveview.image.IImageProvider

class GokigenGLView : GLSurfaceView, ILiveViewRefresher, ILiveView, IMessageDrawer
{
    //private var graphicsDrawer : IGraphicsDrawer = SquareDrawer(context)
    private var graphicsDrawer : IGraphicsDrawer = EquirectangularDrawer(context)
    private lateinit var imageProvider : IImageProvider

    companion object
    {
        private val TAG = toString()
    }

    constructor(context: Context) : super(context)
    {
        initializeSelf(context, null)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initializeSelf(context, attrs)
    }

    /**
     * クラスの初期化処理...レンダラを設定する
     *
     */
    private fun initializeSelf(context: Context, attrs: AttributeSet?)
    {
        try
        {
            //setEGLConfigChooser(false);        // これだと画面透過はダメ！
            setEGLContextClientVersion(1)
            super.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            //setEGLConfigChooser(5,6,5, 8, 16, 0);
            isFocusable = true
            isFocusableInTouchMode = true

            // レンダラを設定する
            val renderer = GokigenGLRenderer(context, graphicsDrawer)
            setRenderer(renderer)

            // 画面を透過させる
            holder.setFormat(PixelFormat.TRANSLUCENT)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun refresh()
    {
        invalidate()
    }

    override fun setImageProvider(provider: IImageProvider)
    {
        try
        {
            imageProvider = provider
            graphicsDrawer.setImageProvider(provider)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun updateImageRotation(degrees: Int)
    {
        //TODO("Not yet implemented")
    }

    override fun getMessageDrawer(): IMessageDrawer
    {
        return (this)
    }

    override fun setMessageToShow(message: String, area: IMessageDrawer.MessageArea, color: Int, size: Int)
    {
        //TODO("Not yet implemented")
    }

    override fun setLevelToShow(value: Float, area: IMessageDrawer.LevelArea) {
        //TODO("Not yet implemented")
    }

}