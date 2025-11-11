package clentlogic.cloy.crobotcontroller.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.data.communication.wifi.WifiHelper
import clentlogic.cloy.crobotcontroller.data.remote.firebase.FirebaseRealtimeDbHelper
import clentlogic.cloy.crobotcontroller.presentation.ui.navigation.AppNavHost
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.CRobotControllerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var bleHelper: BleHelper

    @Inject
    lateinit var wifiHelper: WifiHelper

    @Inject
    lateinit var firebaseHelper: FirebaseRealtimeDbHelper

    private lateinit var blePermissionHandler: BlePermissionHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        blePermissionHandler = BlePermissionHandler(this, bleHelper)

        setContent {
            CRobotControllerTheme {
                AppNavHost(blePermissionHandler)
            }

        }

        wifiHelper.registerWifiStateListener()
        bleHelper.registerBluetoothStateReceiver()

    }

    override fun onDestroy() {
        super.onDestroy()
        bleHelper.unregisterBluetoothStateReceiver()
        wifiHelper.unregisterWifiStateListener()
        firebaseHelper.stopRealtimeListener()
    }
}

