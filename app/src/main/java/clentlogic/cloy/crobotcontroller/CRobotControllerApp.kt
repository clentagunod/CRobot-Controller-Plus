package clentlogic.cloy.crobotcontroller

import android.app.Application
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CRobotControllerApp: Application(){
    companion object {
        const val LOCAL_MODE = "local_mode"
        const val GLOBAL_MODE = "global_mode"
    }
}


