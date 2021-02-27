package jp.osdn.gokigen.thetaview.preference


interface IPreferencePropertyAccessor
{

    companion object
    {
        // --- PREFERENCE KEY AND DEFAULT VALUE ---
        const val PREFERENCE_NOTIFICATIONS = "show_notifications"
        const val PREFERENCE_NOTIFICATIONS_DEFAULT_VALUE = false
        const val PREFERENCE_SAVE_LOCAL_LOCATION = "save_local_location"
        const val PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE = false
        const val PREFERENCE_USE_CAMERA_X_PREVIEW = "use_camera_x_preview"
        const val PREFERENCE_USE_CAMERA_X_PREVIEW_DEFAULT_VALUE = false

        const val CACHE_LIVE_VIEW_PICTURES = "cache_live_view_pictures"
        const val CACHE_LIVE_VIEW_PICTURES_DEFAULT_VALUE = false

        const val NUMBER_OF_CACHE_PICTURES = "nof_cache_pictures"
        const val NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE = "500"

        const val CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW = "capture_both_camera_and_live_view"
        const val CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE = false

        const val CAPTURE_ONLY_LIVE_VIEW = "capture_only_live_view"
        const val CAPTURE_ONLY_LIVE_VIEW_DEFAULT_VALUE = false

        const val SHOW_CAMERA_STATUS = "show_camera_status"
        const val SHOW_CAMERA_STATUS_DEFAULT_VALUE = false

        const val USE_MINDWAVE_EEG = "use_mindwave_eeg"
        const val USE_MINDWAVE_EEG_DEFAULT_VALUE= false

        // --- SCREEN TRANSACTION LABEL ---
        const val LABEL_EXIT_APPLICATION = "exit_application"
        const val LABEL_WIFI_SETTINGS = "wifi_settings"
        const val LABEL_INSTRUCTION_LINK = "instruction_link"
        const val LABEL_PRIVACY_POLICY = "privacy_policy"
        const val LABEL_DEBUG_INFO = "debug_info"

        // --- HIDDEN
        const val SHOW_GRID_STATUS = "show_grid"
        const val SHOW_GRID_STATUS_DEFAULT_VALUE = false
        const val EXTERNAL_STORAGE_LOCATION = "external_storage_location"
        const val EXTERNAL_STORAGE_LOCATION_DEFAULT_VALUE = ""
    }

}
