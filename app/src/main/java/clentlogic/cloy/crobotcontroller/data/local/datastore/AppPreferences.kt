package clentlogic.cloy.crobotcontroller.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object AppPreferences {
    val PERMISSIONS_OK = booleanPreferencesKey("permission_ok")
    val TOGGLE_CONTROL_BUTTON = booleanPreferencesKey("toggle_control_button")
    val TOGGLE_SLEEP_MODE = booleanPreferencesKey("toggle_sleep_mode")
    val TOGGLE_CAMERA = booleanPreferencesKey("toggle_camera")
    val TOGGLE_FLASH = booleanPreferencesKey("toggle_flash")

    val CONTROL_MODE = stringPreferencesKey("control_mode")

    val USERNAME = stringPreferencesKey("username")
    val EMAIL_LOGIN = stringPreferencesKey("email")
    val PASSWORD_LOGIN = stringPreferencesKey("password")

    val LOGIN_STATUS = booleanPreferencesKey("login_status")

}