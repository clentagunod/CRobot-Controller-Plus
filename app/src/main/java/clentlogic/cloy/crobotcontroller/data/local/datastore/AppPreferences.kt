package clentlogic.cloy.crobotcontroller.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object AppPreferences {
    val PERMISSIONS_OK = booleanPreferencesKey("permission_ok")
    val TOGGLE_CONTROL_BUTTON = booleanPreferencesKey("toggle_control_button")
}