package jp.osdn.gokigen.thetaview.scene

import android.graphics.Color
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import jp.osdn.gokigen.thetaview.IScopedStorageAccessPermission
import jp.osdn.gokigen.thetaview.IShowInformation
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.camera.ICameraStatusReceiver
import jp.osdn.gokigen.thetaview.camera.theta.ThetaControl
import jp.osdn.gokigen.thetaview.liveview.LiveImageViewFragment
import jp.osdn.gokigen.thetaview.operation.CameraControl
import jp.osdn.gokigen.thetaview.operation.ICameraControl
import jp.osdn.gokigen.thetaview.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.thetaview.preference.MainPreferenceFragment
import jp.osdn.gokigen.thetaview.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.thetaview.preview.PreviewFragment
import jp.osdn.gokigen.thetaview.utils.ConfirmationDialog
import jp.osdn.gokigen.thetaview.utils.logcat.LogCatFragment


class SceneChanger(private val activity: AppCompatActivity, private val informationNotify: IInformationReceiver, accessRequest : IScopedStorageAccessPermission?, showInformation : IShowInformation, statusReceiver : ICameraStatusReceiver) : IChangeScene
{
    private val cameraControl: ICameraControl = CameraControl(activity, accessRequest)
    private val thetaControl : ThetaControl = ThetaControl(activity, showInformation, statusReceiver)
    private lateinit var liveViewFragment : LiveImageViewFragment
    private lateinit var previewFragment : PreviewFragment
    private lateinit var logCatFragment : LogCatFragment
    private lateinit var mainPreferenceFragment : MainPreferenceFragment

    init
    {
        Log.v(TAG, " SceneChanger is created. ")
        try
        {
            cameraControl.initialize()
            thetaControl.initialize()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun initializeFragmentForPreview()
    {
        if (!::previewFragment.isInitialized)
        {
            previewFragment = PreviewFragment.newInstance()
            previewFragment.setCameraControl(cameraControl)
        }
        setDefaultFragment(previewFragment)
        cameraControl.startCamera()

        val msg = activity.getString(R.string.app_name) + " : " + " camerax"
        informationNotify.updateMessage(msg, isBold = false, isColor = true, color = Color.LTGRAY)
    }

    private fun initializeFragmentForLiveView()
    {
        if (!::liveViewFragment.isInitialized)
        {
            liveViewFragment = LiveImageViewFragment.newInstance()
            liveViewFragment.setCameraControl(thetaControl)
            thetaControl.setIndicator(liveViewFragment)
        }
        setDefaultFragment(liveViewFragment)
        thetaControl.startCamera(false)

        val msg = activity.getString(R.string.app_name) + " : " + " STARTED."
        informationNotify.updateMessage(msg, isBold = false, isColor = true, color = Color.LTGRAY)
    }

    override fun initializeFragment()
    {
        try
        {
            val isCameraXPreview  = PreferenceAccessWrapper(activity).getBoolean(IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW, IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW_DEFAULT_VALUE)
            if (isCameraXPreview)
            {
                initializeFragmentForPreview()
            }
            else
            {
                initializeFragmentForLiveView()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun connectToCamera()
    {
        Log.v(TAG, " connectToCamera()")
        thetaControl.startCamera()
    }

    override fun disconnectFromCamera()
    {
        thetaControl.finishCamera()
    }

    override fun changeToLiveView()
    {
        if (!::liveViewFragment.isInitialized)
        {
            liveViewFragment = LiveImageViewFragment.newInstance()
            liveViewFragment.setCameraControl(thetaControl)
            thetaControl.setIndicator(liveViewFragment)
        }
        changeFragment(liveViewFragment)
        thetaControl.startCamera()
    }

    override fun changeToPreview()
    {
        if (!::previewFragment.isInitialized)
        {
            previewFragment = PreviewFragment.newInstance()
            previewFragment.setCameraControl(cameraControl)
        }
        changeFragment(previewFragment)
        cameraControl.startCamera()
    }

    override fun changeToConfiguration()
    {
        if (!::mainPreferenceFragment.isInitialized)
        {
            mainPreferenceFragment = MainPreferenceFragment.newInstance()
            mainPreferenceFragment.setSceneChanger(this)
        }
        changeFragment(mainPreferenceFragment)
    }

    override fun changeToDebugInformation()
    {
        if (!::logCatFragment.isInitialized)
        {
            logCatFragment = LogCatFragment.newInstance()
        }
        changeFragment(logCatFragment)
    }

    override fun exitApplication()
    {
        val dialog = ConfirmationDialog.newInstance(activity)
        dialog.show(
            R.string.dialog_title_exit_application,
            R.string.dialog_message_exit_application,
            object : ConfirmationDialog.Callback {
                override fun confirm()
                {
                    activity.finish()
                }
            }
        )
    }

    private fun changeFragment(fragment: Fragment)
    {
        val transaction : FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setDefaultFragment(fragment: Fragment)
    {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragment.retainInstance = true
        transaction.replace(R.id.fragment1, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun finish()
    {
        try
        {
            cameraControl.finishCamera()
            thetaControl.finishCamera()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = this.toString()
    }
}
