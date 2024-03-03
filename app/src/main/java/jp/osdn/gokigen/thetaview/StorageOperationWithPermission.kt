package jp.osdn.gokigen.thetaview

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity

/**
 *
 *
 */
@RequiresApi(api = Build.VERSION_CODES.R)
class StorageOperationWithPermission(private val activity: FragmentActivity) : IScopedStorageAccessPermission
{
   // private var callbackOwner : IImageStoreGrant? = null


    override fun requestStorageAccessFrameworkLocation()
    {
/*
        try
        {
            val mediaLocation = PreferenceAccessWrapper(activity).getString(IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION, "")
            if (mediaLocation.length > 1)
            {
                // 既に権限を確保しているときは権限を二重に要求しない
                return
            }

            val path = Environment.DIRECTORY_DCIM + File.separator + activity.getString(R.string.app_location)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
*/
            //intent.type = "*/*"
/*
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, path)

            val launcher = activity.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
                if (uri != null)
                {
                    Log.v(TAG, " DOCUMENT TREE GRANTED ($uri)")

                    // アクセス権限の永続化
                    activity.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    PreferenceAccessWrapper(activity).putString(IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION, uri.toString())
                }
            }
            launcher.launch(null)
        }
        catch (e : Exception)
        {
           e.printStackTrace()
        }
*/
    }

/*
    override fun responseStorageAccessFrameworkLocation(resultCode: Int, data: Intent?)
    {
        if (resultCode == AppCompatActivity.RESULT_OK)
        {
            Log.v(TAG, " DOCUMENT TREE GRANTED  $data")
            data?.data?.also { uri ->
                val contentResolver = activity.applicationContext.contentResolver
                val takeFlags: Int =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                PreferenceAccessWrapper(activity).putString(IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION, uri.toString())
            }
        }
        else
        {
            Log.v(TAG, " DOCUMENT TREE DENIED  $resultCode")
        }
    }
*/
/*
    override fun requestAccessPermission(requestUri : Uri, grantResponse : IImageStoreGrant)
    {
        try
        {
            callbackOwner = grantResponse
            val urisToModify: List<Uri> = listOf( requestUri )
            val contentResolver: ContentResolver = activity.contentResolver
            val editPendingIntent = MediaStore.createWriteRequest(contentResolver, urisToModify)
            activity.startIntentSenderForResult(editPendingIntent.intentSender, MainActivity.REQUEST_CODE_MEDIA_EDIT,null, 0, 0, 0)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }
 */
/*
    override fun responseAccessPermission(resultCode: Int, data: Intent?)
    {
        if (resultCode == AppCompatActivity.RESULT_OK)
        {
            Log.v(TAG, " WRITE PERMISSION GRANTED  $data")
        }
        else
        {
            Log.v(TAG, " WRITE PERMISSION DENIED  $resultCode")
        }
    }
*/
    companion object
    {
        private val  TAG = StorageOperationWithPermission::class.java.simpleName
    }

}
