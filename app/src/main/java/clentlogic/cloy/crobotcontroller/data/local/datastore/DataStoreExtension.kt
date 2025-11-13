package clentlogic.cloy.crobotcontroller.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.LOCAL_MODE
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.CONTROL_MODE
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.EMAIL_LOGIN
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.LOGIN_STATUS
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.PASSWORD_LOGIN
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.PERMISSIONS_OK
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.TOGGLE_CAMERA
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.TOGGLE_CONTROL_BUTTON
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.TOGGLE_FLASH
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.TOGGLE_SLEEP_MODE
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.USERNAME
import clentlogic.cloy.crobotcontroller.domain.model.LoginCredential
import clentlogic.cloy.crobotcontroller.domain.model.ToggleControls
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")


fun Context.observePermissionState(): Flow<Boolean> = dataStore.data
    .map { value -> value[PERMISSIONS_OK] ?: false}

fun Context.observeToggleControlButtonState() : Flow<ToggleControls> = dataStore.data
    .map {value ->
        ToggleControls(
            value[TOGGLE_CONTROL_BUTTON] ?: false,
            value[TOGGLE_SLEEP_MODE] ?: false,
            value[TOGGLE_CAMERA] ?: false,
            value[TOGGLE_FLASH] ?: false
        )

    }

fun Context.observeControlModeState(): Flow<String> = dataStore.data
    .map { value -> value[CONTROL_MODE]  ?: LOCAL_MODE}

fun Context.observeLoginCredential(): Flow<LoginCredential> = dataStore.data
    .map { value ->
        LoginCredential(
            value[USERNAME] ?: "",
            value[EMAIL_LOGIN] ?: "",
            value[PASSWORD_LOGIN] ?: ""
        )
    }

fun Context.observeLoginStatus(): Flow<Boolean> = dataStore.data
    .map { value -> value[LOGIN_STATUS] ?: false }


suspend fun Context.setPermissionState(isPermitted: Boolean) {
    dataStore.edit {
        it[PERMISSIONS_OK] = isPermitted
    }
}

suspend fun Context.setToggleControls(isToggled: Boolean, key: Preferences.Key<Boolean>){
    dataStore.edit {
        it[key] = isToggled
    }

}

suspend fun Context.setControlModeState(setMode: String){
    dataStore.edit {
        it[CONTROL_MODE] = setMode
    }
}

suspend fun Context.setLoginCredential(username: String, email: String, password: String){
    dataStore.edit {
        it[USERNAME] = username
        it[EMAIL_LOGIN] = email
        it[PASSWORD_LOGIN] = password
    }
}

suspend fun Context.setLoginStatus(isLoggedIn: Boolean){
    dataStore.edit {
        it[LOGIN_STATUS] = isLoggedIn
    }
}
