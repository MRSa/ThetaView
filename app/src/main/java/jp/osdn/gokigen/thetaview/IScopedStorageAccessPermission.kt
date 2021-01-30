package jp.osdn.gokigen.thetaview

import android.content.Intent
import android.net.Uri
import jp.osdn.gokigen.thetaview.operation.imagefile.IImageStoreGrant

interface IScopedStorageAccessPermission
{
    fun requestStorageAccessFrameworkLocation()
    fun responseStorageAccessFrameworkLocation(resultCode: Int, data: Intent?)

    fun requestAccessPermission(requestUri : Uri, grantResponse : IImageStoreGrant)
    fun responseAccessPermission(resultCode: Int, data: Intent?)
}
