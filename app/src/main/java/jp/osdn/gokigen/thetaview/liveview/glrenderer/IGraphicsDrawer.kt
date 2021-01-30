package jp.osdn.gokigen.thetaview.liveview.glrenderer

import jp.osdn.gokigen.thetaview.liveview.image.IImageProvider
import javax.microedition.khronos.opengles.GL10

interface IGraphicsDrawer
{
    /** テクスチャ画像提供オブジェクト **/
    fun setImageProvider(provider: IImageProvider)

    /** 拡大・縮小サイズ **/
    fun setScaleFactor(scaleFactor: Float)

    /** 準備クラス  */
    fun prepareObject()

    /** 準備クラス(その２)  */
    fun prepareDrawer(gl: GL10?)

    /** 描画前の処理を実行する  */
    fun preprocessDraw(gl: GL10?)

    /** 描画を実行する  */
    fun drawObject(gl: GL10?)
}
