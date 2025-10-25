package clentlogic.cloy.crobotcontroller.presentation.ui.navigation

import android.content.Context.MODE_PRIVATE
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.presentation.ui.activity.MainCompose
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.CheckPermissionCompose

@Composable
fun AppNavHost(
    activity: ComponentActivity,
    blePermissionHandler: BlePermissionHandler
) {
    val navController = rememberNavController()
    val prefs = activity.getSharedPreferences("app_prefs", MODE_PRIVATE)
    val permissionGranted = prefs.getBoolean("permissions_ok", false)


    NavHost(
        navController = navController,
        startDestination = if (permissionGranted) "main" else "check_permission"
    ) {
        composable(
            "check_permission"
        ) {
            CheckPermissionCompose(
                blePermissionHandler,
                onPermitted = {
                    prefs.edit { putBoolean("permissions_ok", true) }
                    navController.navigate("main") {
                        popUpTo("check_permission") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainCompose(
                blePermissionHandler
            )
        }
    }

}