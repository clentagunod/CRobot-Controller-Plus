package clentlogic.cloy.crobotcontroller.data.repository

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.core.Preferences
import clentlogic.cloy.crobotcontroller.data.local.datastore.observeControlModeState
import clentlogic.cloy.crobotcontroller.data.local.datastore.observeLoginCredential
import clentlogic.cloy.crobotcontroller.data.local.datastore.observeLoginStatus
import clentlogic.cloy.crobotcontroller.data.local.datastore.observePermissionState
import clentlogic.cloy.crobotcontroller.data.local.datastore.observeToggleControlButtonState
import clentlogic.cloy.crobotcontroller.data.local.datastore.setControlModeState
import clentlogic.cloy.crobotcontroller.data.local.datastore.setLoginCredential
import clentlogic.cloy.crobotcontroller.data.local.datastore.setLoginStatus
import clentlogic.cloy.crobotcontroller.data.local.datastore.setPermissionState
import clentlogic.cloy.crobotcontroller.data.local.datastore.setToggleControls
import clentlogic.cloy.crobotcontroller.data.remote.firebase.FirebaseRealtimeDbHelper
import clentlogic.cloy.crobotcontroller.domain.model.LoginCredential
import clentlogic.cloy.crobotcontroller.domain.model.ToggleControls
import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext val app: Context,
    val firebaseRealtimeDbHelper: FirebaseRealtimeDbHelper
) : DataStoreRepository {

    override val permissionState: Flow<Boolean> = app.observePermissionState()
    override val toggleControls: Flow<ToggleControls> = app.observeToggleControlButtonState()
    override val controlModeState: Flow<String> = app.observeControlModeState()
    override val loginCredential: Flow<LoginCredential> = app.observeLoginCredential()
    override val loginStatus: Flow<Boolean> = app.observeLoginStatus()

    override suspend fun setToggleControls(isToggled: Boolean, key: Preferences.Key<Boolean>) =
        app.setToggleControls(isToggled, key)

    override suspend fun setPermissionState(isPermitted: Boolean) =
        app.setPermissionState(isPermitted)

    override suspend fun setControlMode(setMode: String) {
        app.setControlModeState(setMode)
    }

    override fun setLoginCredential(username: String, email: String, password: String) {

        try{
            firebaseRealtimeDbHelper.signIn(email, password) { success, authMessage ->
                if (success) {
                    CoroutineScope(Dispatchers.IO).launch {
                        app.setLoginCredential(username, email, password)
                        app.setLoginStatus(true)
                    }
                    Toast.makeText(app, "Login email: $authMessage", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(app, authMessage, Toast.LENGTH_SHORT).show()
                }

            }

        }catch (e: IllegalArgumentException){
            Toast.makeText(app, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun signOutFb() {
        firebaseRealtimeDbHelper.signOut { signOut, message ->
            if (signOut){
                CoroutineScope(Dispatchers.IO).launch {
                    app.setLoginStatus(false)
                    app.setLoginCredential("", "", "")
                }

                Toast.makeText(app, "Signed Out Successfully: $message", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(app, "Signed Out Error: $message", Toast.LENGTH_SHORT).show()
            }
        }

    }

}