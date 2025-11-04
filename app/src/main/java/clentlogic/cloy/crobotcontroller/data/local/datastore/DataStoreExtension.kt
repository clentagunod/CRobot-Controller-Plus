package clentlogic.cloy.crobotcontroller.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.PERMISSIONS_OK
import clentlogic.cloy.crobotcontroller.data.local.datastore.AppPreferences.TOGGLE_CONTROL_BUTTON
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")



fun Context.observePermissionState(): Flow<Boolean> = dataStore.data
    .map { value -> value[PERMISSIONS_OK] ?: false}

fun Context.observeToggleControlButtonState() : Flow<Boolean> = dataStore.data
    .map {value -> value[TOGGLE_CONTROL_BUTTON] ?: false }



suspend fun Context.setPermissionState(isPermitted: Boolean) {
    dataStore.edit {
        it[PERMISSIONS_OK] = isPermitted
    }
}

suspend fun Context.setToggleControlButtonState(isToggled: Boolean){
    dataStore.edit {
        it[TOGGLE_CONTROL_BUTTON] = isToggled
    }

}