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
import kotlin.math.cos
import kotlin.math.sin

class EquirectangularDrawer(context: Context) : IGraphicsDrawer
{
    private lateinit var imageProvider : IImageProvider

    private val mGlUtils = GokigenGLUtilities(context)

    private var mFVertexBuffer: FloatBuffer? = null
    private var mTexBuffer: FloatBuffer? = null
    private var mIndexBuffer: ShortBuffer? = null
    private var mIndices : Int = 0

    private var mDroidTextureID = 0
    private var mScaleFactor = 1.0f
    private var mAngleX = -180.0f
    private var mAngleY = -180.0f
    private var mAngleZ = 0.0f

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

    override fun resetView()
    {
        mScaleFactor = 1.0f
        mAngleX = 0.0f
        mAngleY = 0.0f
        mAngleZ = 0.0f
    }

    override fun setViewMove(x: Float, y: Float, z: Float)
    {
        mAngleX += x
        mAngleY += y
        mAngleZ += z
        //Log.v(TAG, "setView : ($mAngleX, $mAngleY, $mAngleZ)")
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

        // 画像を回転させる
        gl?.glRotatef(mAngleX, 1.0f, 0.0f, 0.0f) // X軸周りに回転
        gl?.glRotatef(mAngleY, 0.0f, 1.0f, 0.0f) // Y軸周りに回転
        gl?.glRotatef(mAngleZ, 0.0f, 0.0f, 1.0f) // Z軸周りに回転
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
        //makeSphere(1.0f, 360, 180)
        //makeSphere(1.0f, 36, 18)
        //makeSphere(1.0f, 72, 36)
        makeSphere(1.0f, 360, 180)

        mFVertexBuffer?.position(0)
        mTexBuffer?.position(0)
        mIndexBuffer?.position(0)

    }

    private fun makeSphere(radius: Float, numLatitudeLines : Int, numLongitudeLines : Int)
    {

        // AREA Allocation
        mIndices =  (numLatitudeLines + 1) * (numLongitudeLines + 2) + 2

        val vbb: ByteBuffer = ByteBuffer.allocateDirect((mIndices * 3) * 4)
        vbb.order(ByteOrder.nativeOrder())
        mFVertexBuffer = vbb.asFloatBuffer()

        val tbb: ByteBuffer = ByteBuffer.allocateDirect((mIndices * 2) * 4)
        tbb.order(ByteOrder.nativeOrder())
        mTexBuffer = tbb.asFloatBuffer()

        val ibb: ByteBuffer = ByteBuffer.allocateDirect(((mIndices) * 3) * 2)
        ibb.order(ByteOrder.nativeOrder())
        mIndexBuffer = ibb.asShortBuffer()

        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(radius)
        mFVertexBuffer?.put(0.0f)

        mTexBuffer?.put(0.0f)
        mTexBuffer?.put(1.0f)

        val latitudeSpacing = 1.0f / (numLatitudeLines + 1.0f)
        val longitudeSpacing = 1.0f / (numLongitudeLines)

        for (latitude in 0 .. numLatitudeLines)  // 緯度 (横)
        {
            for (longitude in 0 until numLongitudeLines)  // 経度 (縦)
            {
                mTexBuffer?.put(longitude * longitudeSpacing)
                mTexBuffer?.put(1.0f - (latitude + 1) * latitudeSpacing)


                val theta = (longitude * longitudeSpacing) * 2.0f  * 3.14159265359f
                val phi = ((1.0f - (latitude + 1) * latitudeSpacing) - 0.5f) * 3.14159265359f
                val c = cos(phi)

                mFVertexBuffer?.put(radius * c * cos(theta))
                mFVertexBuffer?.put(radius * sin(phi))
                mFVertexBuffer?.put(radius * c * sin(theta))
            }
        }
        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(-radius)
        mFVertexBuffer?.put(0.0f)

        mTexBuffer?.put(0.0f)
        //mTexBuffer?.put(0.0f)
        mTexBuffer?.put(-latitudeSpacing)

        //   Index
        mIndexBuffer?.put(0)
        for (i in 0 until numLatitudeLines)
        {
            for (j in 0 until (numLongitudeLines - 1))
            {
                mIndexBuffer?.put((j * numLatitudeLines + i + 1).toShort())
                mIndexBuffer?.put((j * numLatitudeLines + i + 1 + 1).toShort())
            }
            mIndexBuffer?.put(((numLongitudeLines - 1) * numLatitudeLines + 1).toShort())
            mIndexBuffer?.put(0)
        }
        mIndexBuffer?.put(((numLongitudeLines - 1) * numLatitudeLines + 1).toShort())
    }
}
