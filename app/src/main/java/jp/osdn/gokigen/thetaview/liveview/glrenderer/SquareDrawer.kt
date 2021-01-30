package jp.osdn.gokigen.thetaview.liveview.glrenderer

import android.content.Context
import android.opengl.GLUtils
import android.util.Log
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.liveview.image.IImageProvider
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.opengles.GL10

class SquareDrawer(context: Context) : IGraphicsDrawer
{
    private lateinit var imageProvider : IImageProvider

    private val mGlUtils = GokigenGLUtilities(context)

    private var mFVertexBuffer: FloatBuffer? = null
    private var mTexBuffer: FloatBuffer? = null
    private var mIndexBuffer: ShortBuffer? = null
    private var mIndices : Int = 0

    private var mDroidTextureID = 0

    private var mScaleFactor = 1.0f


    companion object
    {
        private val  TAG = this.toString()
    }

    override fun setImageProvider(provider: IImageProvider)
    {
        Log.v(TAG, "setImageProvider()")
        this.imageProvider = provider
    }

    override fun setScaleFactor(scaleFactor: Float)
    {
        mScaleFactor *= scaleFactor
    }

    override fun prepareDrawer(gl: GL10?)
    {
        Log.v(TAG, "prepareDrawer()")
        mDroidTextureID = mGlUtils.prepareTexture(gl, R.drawable.sample)
    }

    override fun preprocessDraw(gl: GL10?)
    {
        //Log.v(TAG, "preprocessDraw()")

        // ビットマップを取得 (初期化が住んでいた場合...)
        if (::imageProvider.isInitialized)
        {
            //Log.v(TAG, " texSubImage2D() ")
            GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, 0, 0, imageProvider.getImage())
        }

        gl?.glActiveTexture(GL10.GL_TEXTURE0) // テクスチャユニット0を設定
        gl?.glBindTexture(GL10.GL_TEXTURE_2D, mDroidTextureID) // テクスチャをバインド (mTextureIDのビットマップを利用)
        gl?.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT) // テクスチャをs軸方向に繰り返す
        gl?.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT) // テクスチャをt軸方向に繰り返す
    }

    override fun drawObject(gl: GL10?)
    {
        //Log.v(TAG, "drawObject()")
        gl?.glScalef(mScaleFactor, mScaleFactor, mScaleFactor)
        gl?.glFrontFace(GL10.GL_CCW)
        gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer)
        gl?.glEnable(GL10.GL_TEXTURE_2D)
        gl?.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer)
        gl?.glDrawElements(GL10.GL_TRIANGLE_STRIP, mIndices, GL10.GL_UNSIGNED_SHORT, mIndexBuffer)
    }

    override fun prepareObject()
    {
        //Log.v(TAG, "prepareObject()")

        mIndices = 6

        val numOfVertex = 4
        val vbb: ByteBuffer = ByteBuffer.allocateDirect(((numOfVertex) * 3) * 4)
        vbb.order(ByteOrder.nativeOrder())
        mFVertexBuffer = vbb.asFloatBuffer()

        val tbb: ByteBuffer = ByteBuffer.allocateDirect(((numOfVertex) * 2) * 4)
        tbb.order(ByteOrder.nativeOrder())
        mTexBuffer = tbb.asFloatBuffer()

        val ibb: ByteBuffer = ByteBuffer.allocateDirect((mIndices) * 2)
        ibb.order(ByteOrder.nativeOrder())
        mIndexBuffer = ibb.asShortBuffer()


        // 頂点座標０
        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(1.0f)
        mFVertexBuffer?.put(0.0f)

        // 頂点座標１
        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(0.0f)

        // 頂点座標２
        mFVertexBuffer?.put(1.0f)
        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(0.0f)

        // 頂点座標３
        mFVertexBuffer?.put(1.0f)
        mFVertexBuffer?.put(1.0f)
        mFVertexBuffer?.put(0.0f)

        // 三角形１
        mIndexBuffer?.put(0)
        mIndexBuffer?.put(1)
        mIndexBuffer?.put(2)

        // 三角形２
        mIndexBuffer?.put(0)
        mIndexBuffer?.put(2)
        mIndexBuffer?.put(3)

        // テクスチャ０
        mTexBuffer?.put(0.0f)
        mTexBuffer?.put(1.0f)

        // テクスチャ１
        mTexBuffer?.put(0.0f)
        mTexBuffer?.put(0.0f)

        // テクスチャ２
        mTexBuffer?.put(1.0f)
        mTexBuffer?.put(0.0f)

        // テクスチャ３
        mTexBuffer?.put(1.0f)
        mTexBuffer?.put(1.0f)


        mFVertexBuffer?.position(0)
        mTexBuffer?.position(0)
        mIndexBuffer?.position(0)
    }
}
