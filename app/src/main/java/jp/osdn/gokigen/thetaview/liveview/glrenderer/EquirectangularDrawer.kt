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

        //makeSphere0(1.0f, 40, 20)
        makeSphere(1.0f, 80, 40)
        //makeSphere1(1.0f, 20, 10)

        mFVertexBuffer?.position(0)
        mTexBuffer?.position(0)
        mIndexBuffer?.position(0)

    }

    private fun makeSphere1(radius: Float, nSlices: Int, nStacks: Int)
    {
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

    }
    private fun makeSphere0(radius: Float, nSlices: Int, nStacks: Int)
    {

        // Allocation
        mIndices = ((nStacks - 1) * nSlices + 2 - 2) * 2 + 4 * nSlices - 2

        val vbb: ByteBuffer = ByteBuffer.allocateDirect((((nStacks - 1) * nSlices + 2) * 3) * 4)
        vbb.order(ByteOrder.nativeOrder())
        mFVertexBuffer = vbb.asFloatBuffer()

        val tbb: ByteBuffer = ByteBuffer.allocateDirect((((nStacks - 1) * nSlices + 2) * 2) * 4)
        tbb.order(ByteOrder.nativeOrder())
        mTexBuffer = tbb.asFloatBuffer()

        val ibb: ByteBuffer = ByteBuffer.allocateDirect((mIndices) * 2)
        ibb.order(ByteOrder.nativeOrder())
        mIndexBuffer = ibb.asShortBuffer()

        //  Vertex
        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(radius)
        mFVertexBuffer?.put(0.0f)

        for (i in 0 until (nStacks - 1))
        {
            for (j in 1..nSlices)
            {
                val theta = (nStacks - i - 1).toDouble() / nStacks.toDouble() * 3.14159265f - 3.14159265f * 0.5f
                val phi = j.toDouble() / nSlices.toDouble() * 2.0f * 3.14159265f

                mFVertexBuffer?.put((radius * cos(theta) * sin(phi)).toFloat())
                mFVertexBuffer?.put((radius * sin(theta)).toFloat())
                mFVertexBuffer?.put((radius * cos(theta) * cos(phi)).toFloat())
            }
        }
        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(-radius)
        mFVertexBuffer?.put(0.0f)

        //  Texture
        mTexBuffer?.put(0.0f)
        mTexBuffer?.put(-radius)

        for (i in 0 until (nStacks - 1))
        {
            for (j in 1..nSlices)
            {
                val theta = ((nStacks - 1) - i).toDouble() / nStacks.toDouble() * 3.14159265f - 3.14159265f * 0.5f
                val phi = j.toDouble() / nSlices.toDouble() * 3.14159265f * 2.0f

                mTexBuffer?.put((radius * cos(theta) * sin(phi)).toFloat())
                mTexBuffer?.put((radius * sin(theta)).toFloat())
            }
        }
        mTexBuffer?.put(0.0f)
        mTexBuffer?.put(radius)

        //   Index
        for (i in 0 until nSlices)
        {
            mIndexBuffer?.put(0)
            for (j in 0 until (nStacks - 1))
            {
                mIndexBuffer?.put((j * nSlices + i + 1).toShort())
                mIndexBuffer?.put((j * nSlices + 1 + (i + 1) % nSlices).toShort())
            }
            mIndexBuffer?.put(((nStacks - 1) * nSlices + 1).toShort())
            mIndexBuffer?.put(0)
        }
        mIndexBuffer?.put(((nStacks - 1) * nSlices + 1).toShort())
    }


    private fun makeSphere(radius: Float, numLatitudeLines : Int, numLongitudeLines : Int)
    {

        // AREA Allocation
        mIndices =  numLatitudeLines * (numLongitudeLines + 1) + 2

        val vbb: ByteBuffer = ByteBuffer.allocateDirect((mIndices * 3) * 4)
        vbb.order(ByteOrder.nativeOrder())
        mFVertexBuffer = vbb.asFloatBuffer()

        val tbb: ByteBuffer = ByteBuffer.allocateDirect((mIndices * 2) * 4)
        tbb.order(ByteOrder.nativeOrder())
        mTexBuffer = tbb.asFloatBuffer()

        val ibb: ByteBuffer = ByteBuffer.allocateDirect(((mIndices) * 2) * 2)
        ibb.order(ByteOrder.nativeOrder())
        mIndexBuffer = ibb.asShortBuffer()

        mFVertexBuffer?.put(0.0f)
        mFVertexBuffer?.put(radius)
        mFVertexBuffer?.put(0.0f)

        mTexBuffer?.put(0.0f)
        mTexBuffer?.put(1.0f)

        val latitudeSpacing = 1.0f / (numLatitudeLines + 1.0f)
        val longitudeSpacing = 1.0f / (numLongitudeLines)

        for (latitude   in 0 until numLatitudeLines)
        {
            for (longitude  in 0 .. numLongitudeLines)
            {
                mTexBuffer?.put(longitude * longitudeSpacing)
                mTexBuffer?.put(1.0f - (latitude + 1) * latitudeSpacing)


                val theta = (longitude * longitudeSpacing) * 2.0f  * 3.14159265f
                val phi = ((1.0f - (latitude + 1) * latitudeSpacing) - 0.5f) * 3.14159265f
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
        mTexBuffer?.put(0.0f)

        //   Index
        for (i in 0 until numLatitudeLines)
        {
            mIndexBuffer?.put(0)
            for (j in 0 until (numLongitudeLines - 1))
            {
                mIndexBuffer?.put((j * numLatitudeLines + i + 1).toShort())
                mIndexBuffer?.put((j * numLatitudeLines + 1 + (i + 1) % numLatitudeLines).toShort())
            }
            mIndexBuffer?.put(((numLongitudeLines - 1) * numLatitudeLines + 1).toShort())
            mIndexBuffer?.put(0)
        }
        mIndexBuffer?.put(((numLongitudeLines - 1) * numLatitudeLines + 1).toShort())
    }
}
