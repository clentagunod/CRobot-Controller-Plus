package clentlogic.cloy.crobotcontroller.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository  {

    val permissionState: Flow<Boolean>
    val toggleControlButtonState: Flow<Boolean>

    suspend fun setToggleControlState(isToggled: Boolean)
    suspend fun setPermissionState(isPermitted: Boolean)
}
