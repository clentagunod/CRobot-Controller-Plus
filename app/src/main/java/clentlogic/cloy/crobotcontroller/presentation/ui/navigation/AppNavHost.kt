package clentlogic.cloy.crobotcontroller.presentation.ui.navigation

import android.bluetooth.BluetoothDevice
import android.content.Context.MODE_PRIVATE
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.ui.activity.MainCompose
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.CheckPermissionCompose
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.ManageDeviceScreen
import clentlogic.cloy.crobotcontroller.presentation.viewmodel.MainViewModel

@Composable
fun AppNavHost(
    activity: ComponentActivity,
    blePermissionHandler: BlePermissionHandler
) {
    val navController = rememberNavController()
    val prefs = activity.getSharedPreferences("app_prefs", MODE_PRIVATE)
    val permissionGranted = prefs.getBoolean("permissions_ok", false)

    lateinit var device: (Map.Entry<String, BluetoothDevice>)


    NavHost(
        navController = navController,
        startDestination = if (permissionGranted) "main_graph" else "check_permission"
    ) {
        composable(
            "check_permission"
        ) {
            CheckPermissionCompose(
                blePermissionHandler,
                onPermitted = {
                    prefs.edit { putBoolean("permissions_ok", true) }
                    navController.navigate("main_graph") {
                        popUpTo("check_permission") { inclusive = true }
                    }
                }
            )
        }

        navigation(startDestination = "main", route = "main_graph"){
            composable("main") {backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("main_graph")
                }

                val viewModel: MainViewContract = hiltViewModel<MainViewModel>(parentEntry)

                MainCompose(
                    viewModel,
                    blePermissionHandler,
                    onOpenDevice = {
                        device = it
                        navController.navigate("manage_device")
                    }
                )
            }

            composable("manage_device") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("main_graph")
                }

                val viewModel: MainViewContract = hiltViewModel<MainViewModel>(parentEntry)
                ManageDeviceScreen(
                    device,
                    viewModel
                )
            }


        }


    }

}