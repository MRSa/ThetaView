package jp.osdn.gokigen.thetaview.liveview.storeimage

import android.content.ContentValues
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.liveview.image.IImageProvider
import jp.osdn.gokigen.thetaview.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.thetaview.preference.PreferenceAccessWrapper

import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class StoreImage(private val context: FragmentActivity, private val imageProvider: IImageProvider, private val dumpLog : Boolean = false) : IStoreImage
{

    override fun doStore(target: Bitmap?)
    {
        try
        {
            // 保存処理(プログレスダイアログ（「保存中...」）を表示

            val bitmapToStore = target ?: imageProvider.getImage()
            val isLocalLocation  = PreferenceAccessWrapper(context).getBoolean(IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION, IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE)
            if (isLocalLocation)
            {
                saveImageLocal(bitmapToStore)
            }
            else
            {
                saveImageExternal(bitmapToStore)
            }

            // 保存処理(プログレスダイアログ（「保存中...」）を削除
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
        }
    }

    private fun storeImageImpl(target: Bitmap)
    {
/*
        // 保存処理(プログレスダイアログ（「保存中...」）を表示して処理する)
        val saveDialog = ProgressDialog(context)
        saveDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        saveDialog.setMessage(context!!.getString(R.string.data_saving))
        saveDialog.isIndeterminate = true
        saveDialog.setCancelable(false)
        saveDialog.show()
        val thread = Thread {
            System.gc()
            saveImageImpl(target)
            System.gc()
            saveDialog.dismiss()
        }
        try
        {
            thread.start()
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
            System.gc()
        }
*/
        try
        {
            val isLocalLocation  = PreferenceAccessWrapper(context).getBoolean(IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION, IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE)
            if (isLocalLocation)
            {
                saveImageLocal(target)
            }
            else
            {
                saveImageExternal(target)
            }
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
        }
    }

    private fun prepareLocalOutputDirectory(): File
    {
        val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mediaDir?.mkdirs()
        return (if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir)
    }

    /**
     * ビットマップイメージをファイルに出力する
     *
     * @param targetImage  出力するビットマップイメージ
     */
    private fun saveImageLocal(targetImage: Bitmap)
    {
        try
        {
            val fileName = "L" + SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
            val photoFile = File(prepareLocalOutputDirectory(), fileName)
            val outputStream = FileOutputStream(photoFile)
            targetImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
        }
    }

    private fun getExternalOutputDirectory(): File
    {
        @Suppress("DEPRECATION") val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path + "/" + context.getString(R.string.app_location) + "/"
        val target = File(directoryPath)
        try
        {
            target.mkdirs()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        if (dumpLog)
        {
            Log.v(TAG, "  ----- RECORD Directory PATH : $directoryPath -----")
        }
        return (target)
    }

    /**
     * ビットマップイメージを外部ストレージのファイルに出力する
     *
     * @param targetImage  出力するビットマップイメージ
     */
    private fun saveImageExternal(targetImage: Bitmap)
    {
        try
        {
            if (!isExternalStorageWritable())
            {

                saveImageLocal(targetImage)
                return
            }

            val outputDir = getExternalOutputDirectory()
            val resolver = context.contentResolver
            val mimeType = "image/jpeg"
            val now = System.currentTimeMillis()
            val path = Environment.DIRECTORY_DCIM + File.separator + context.getString(R.string.app_location) // Environment.DIRECTORY_PICTURES  + File.separator + "gokigen" //"DCIM/aira01a/"
            val fileName = "L" + SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(now) + ".jpg"

            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, fileName)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            val extStorageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.Media.RELATIVE_PATH, path)
                values.put(MediaStore.Images.Media.IS_PENDING, true)
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                @Suppress("DEPRECATION")
                values.put(MediaStore.Images.Media.DATA, outputDir.absolutePath + File.separator + fileName)
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            val imageUri = resolver.insert(extStorageUri, values)
            if (imageUri != null)
            {
                //resolver.update(imageUri, values, null, null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                {
                    Log.v(TAG, "  ===== StorageOperationWithPermission() : $imageUri =====")
                    //StorageOperationWithPermission(context).requestAndroidRMediaPermission(imageUri)
                }

                ////////////////////////////////////////////////////////////////
                if (dumpLog)
                {
                    val cursor = resolver.query(imageUri, null, null, null, null)
                    DatabaseUtils.dumpCursor(cursor)
                    cursor!!.close()
                }
                ////////////////////////////////////////////////////////////////

                val outputStream = resolver.openOutputStream(imageUri)
                if (outputStream != null)
                {
                    targetImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    resolver.update(imageUri, values, null, null)

                }
            }
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
        }
    }

    private fun isExternalStorageWritable(): Boolean
    {
        return (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
    }

    companion object
    {
        private val  TAG = this.toString()
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    }

}
