package jp.osdn.gokigen.thetaview.operation.imagefile

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import jp.osdn.gokigen.thetaview.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageStoreLocal(private val context: FragmentActivity)
{
    /**
     *   保存用ディレクトリを準備する（ダメな場合はアプリ領域のディレクトリを確保する）
     *
     */
    private fun prepareLocalOutputDirectory(): File
    {
        val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mediaDir?.mkdirs()
        return (if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir)
    }

    fun takePhoto(imageCapture : ImageCapture?)
    {
        if (imageCapture == null)
        {
            Log.v(TAG, " takePhotoLocal() : ImageCapture is null.")
            return
        }
        Log.v(TAG, " takePhotoLocal()")
        try
        {
            val photoFile = File(prepareLocalOutputDirectory(), "P" + SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback
                {
                    override fun onError(exc: ImageCaptureException)
                    {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults)
                    {
                        val savedUri = Uri.fromFile(photoFile)
                        val msg = context.getString(R.string.capture_success) + " $savedUri"
                        Snackbar.make(context.findViewById<ConstraintLayout>(R.id.main_layout), msg, Snackbar.LENGTH_SHORT).show()
                        Log.v(TAG, msg)
                    }
                }
            )
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val  TAG = this.toString()
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    }
}