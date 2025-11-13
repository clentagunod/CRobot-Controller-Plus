package clentlogic.cloy.crobotcontroller.domain.repository

import androidx.datastore.preferences.core.Preferences
import clentlogic.cloy.crobotcontroller.domain.model.LoginCredential
import clentlogic.cloy.crobotcontroller.domain.model.ToggleControls
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository  {

    val permissionState: Flow<Boolean>
    val toggleControls: Flow<ToggleControls>
    val controlModeState: Flow<String>
    val loginCredential: Flow<LoginCredential>
    val loginStatus: Flow<Boolean>

    suspend fun setToggleControls(isToggled: Boolean, key: Preferences.Key<Boolean>)
    suspend fun setPermissionState(isPermitted: Boolean)
    suspend fun setControlMode(setMode: String, )
    fun setLoginCredential(username: String, email: String, password: String)
    fun signOutFb()
}
