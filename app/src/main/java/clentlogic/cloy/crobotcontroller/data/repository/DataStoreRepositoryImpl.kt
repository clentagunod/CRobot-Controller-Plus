package clentlogic.cloy.crobotcontroller.data.repository

import android.content.Context
import clentlogic.cloy.crobotcontroller.data.local.datastore.observeControlModeState
import clentlogic.cloy.crobotcontroller.data.local.datastore.observePermissionState
import clentlogic.cloy.crobotcontroller.data.local.datastore.observeToggleControlButtonState
import clentlogic.cloy.crobotcontroller.data.local.datastore.setControlModeState
import clentlogic.cloy.crobotcontroller.data.local.datastore.setPermissionState
import clentlogic.cloy.crobotcontroller.data.local.datastore.setToggleControlButtonState
import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext val app: Context,
) : DataStoreRepository {

    override val permissionState: Flow<Boolean> = app.observePermissionState()
    override val toggleControlButtonState: Flow<Boolean> = app.observeToggleControlButtonState()
    override val controlModeState: Flow<String> = app.observeControlModeState()

    override suspend fun setToggleControlState(isToggled: Boolean) =
        app.setToggleControlButtonState(isToggled)

    override suspend fun setPermissionState(isPermitted: Boolean) =
        app.setPermissionState(isPermitted)

    override suspend fun setControlMode(setMode: String) {
        app.setControlModeState(setMode)
    }
}