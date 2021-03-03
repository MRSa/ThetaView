package jp.osdn.gokigen.thetaview.preference

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.thetaview.scene.IChangeScene
import jp.osdn.gokigen.thetaview.R


class MainPreferenceFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener
{
    private lateinit var preferences : SharedPreferences
    private lateinit var changeScene : IChangeScene

    fun setSceneChanger(changer : IChangeScene)
    {
        this.changeScene = changer
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.preference_main, rootKey)

        prepareClickListener(IPreferencePropertyAccessor.LABEL_EXIT_APPLICATION)
        prepareClickListener(IPreferencePropertyAccessor.LABEL_WIFI_SETTINGS)
        prepareClickListener(IPreferencePropertyAccessor.LABEL_DEBUG_INFO)

        val useEEG  = findPreference<DropDownPreference>(IPreferencePropertyAccessor.EEG_SIGNAL_USE_TYPE)
        if (useEEG != null)
        {
            useEEG.summary = useEEG.entry

            useEEG.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                useEEG.summary = newValue  as CharSequence // とりあえず番号を表示した
                true
            }
        }
    }

    private fun prepareClickListener(label: String)
    {
        val settings : Preference? = findPreference(label)
        settings?.onPreferenceClickListener = this
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        Log.v(TAG, " onAttach() : ")

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        PreferenceValueInitializer().initializePreferences(context)
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?)
    {
        var value = false
        when (key)
        {
            IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS_DEFAULT_VALUE)
            IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW_DEFAULT_VALUE)
            IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE)
            IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE)
            IPreferencePropertyAccessor.CACHE_LIVE_VIEW_PICTURES -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.CACHE_LIVE_VIEW_PICTURES_DEFAULT_VALUE)
            IPreferencePropertyAccessor.CAPTURE_ONLY_LIVE_VIEW -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.CAPTURE_ONLY_LIVE_VIEW_DEFAULT_VALUE)
            IPreferencePropertyAccessor.SHOW_CAMERA_STATUS -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.SHOW_CAMERA_STATUS_DEFAULT_VALUE)
            IPreferencePropertyAccessor.USE_MINDWAVE_EEG -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.USE_MINDWAVE_EEG_DEFAULT_VALUE)
            IPreferencePropertyAccessor.EEG_SIGNAL_USE_TYPE -> {
                try
                {
                    val indexString = preferences.getString(IPreferencePropertyAccessor.EEG_SIGNAL_USE_TYPE, IPreferencePropertyAccessor.EEG_SIGNAL_USE_TYPE_DEFAULT_VALUE)
                    indexString?.toInt()

                }
                catch (e : Exception)
                {

                }
            }
            // else -> Log.v(TAG, " onSharedPreferenceChanged() : + $key ")
        }
        Log.v(TAG, " onSharedPreferenceChanged() : + $key, $value")
    }

    override fun onPreferenceClick(preference: Preference?): Boolean
    {
        var ret = true
        when (preference?.key)
        {
            IPreferencePropertyAccessor.LABEL_WIFI_SETTINGS -> activity?.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            IPreferencePropertyAccessor.LABEL_EXIT_APPLICATION -> changeScene.exitApplication()
            IPreferencePropertyAccessor.LABEL_DEBUG_INFO -> changeScene.changeToDebugInformation()
            else -> { Log.v(TAG, " onPreferenceClick() : " + preference?.key); ret = false; }
        }
        return (ret)
    }

    companion object
    {
        fun newInstance() = MainPreferenceFragment().apply { }
        private val TAG = MainPreferenceFragment::class.java.simpleName
    }
}
