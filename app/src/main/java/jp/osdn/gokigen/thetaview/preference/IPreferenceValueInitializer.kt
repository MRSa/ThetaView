package jp.osdn.gokigen.thetaview.preference

import android.content.Context

interface IPreferenceValueInitializer
{
    fun initializePreferences(context : Context)
}
