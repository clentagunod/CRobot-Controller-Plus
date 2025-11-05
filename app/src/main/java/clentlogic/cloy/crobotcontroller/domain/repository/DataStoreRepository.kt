package clentlogic.cloy.crobotcontroller.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository  {

    val permissionState: Flow<Boolean>
    val toggleControlButtonState: Flow<Boolean>
    val controlModeState: Flow<String>

    suspend fun setToggleControlState(isToggled: Boolean)
    suspend fun setPermissionState(isPermitted: Boolean)
    suspend fun setControlMode(setMode: String)
}
