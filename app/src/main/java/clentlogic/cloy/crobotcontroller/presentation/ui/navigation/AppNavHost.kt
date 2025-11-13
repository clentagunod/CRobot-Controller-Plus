package clentlogic.cloy.crobotcontroller.presentation.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.ui.components.AnimatedLottieJson
import clentlogic.cloy.crobotcontroller.presentation.ui.components.splash.SplashLoadingScreen
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.CheckPermissionCompose
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.HomeScreen
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.ManageDeviceScreen
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.SettingsScreen
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.LoginScreen
import clentlogic.cloy.crobotcontroller.presentation.ui.util.LandscapeCompose
import clentlogic.cloy.crobotcontroller.presentation.viewmodel.MainViewModel

@Composable
fun AppNavHost(
    blePermissionHandler: BlePermissionHandler,
    viewModel: MainViewContract = hiltViewModel<MainViewModel>()
) {
    val navController = rememberNavController()
    val permission by viewModel.permissionFlow.collectAsState(null)
    val loginStatus by viewModel.loginStatus.collectAsState(null)


    NavHost(
        navController = navController,
        startDestination = "check_permission"
    ) {
        composable(
            "check_permission"
        ) {

            when (permission) {
                true -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("login_screen") {
                            popUpTo("check_permission") { inclusive = true }
                        }
                    }
                }

                false -> {
                    CheckPermissionCompose(
                        blePermissionHandler,
                        onPermitted = {
                            viewModel.setPermission(true)
                        }
                    )

                }

                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedLottieJson(R.raw.animated_hand_loading, 150.dp)
                    }
                }


            }

        }

        composable(
            route = "login_screen"
        ) {

            when (loginStatus) {
                true -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("main_graph") {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    }

                }

                false -> {
                    LoginScreen(
                        viewModel,
                        onLocal = {
                            navController.navigate("main_graph")
                        }
                    )

                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedLottieJson(R.raw.animated_hand_loading, 150.dp)
                    }

                }
            }


        }

        navigation(startDestination = "main", route = "main_graph") {
            composable("main") { backStackEntry ->

                HomeScreen(
                    viewModel,
                    blePermissionHandler,
                    onOpenSettings = {
                        navController.navigate("settings_screen")

                    },
                    onOpenDeviceLocal = {
                        viewModel.selectBleDevice(it)
                        navController.navigate("splash")
                    },
                    onOpenDeviceGlobal = {
                        viewModel.selectDevice(it)
                        navController.navigate("splash")
                    },


                    )
            }

            composable("splash") {
                SplashLoadingScreen(navController, 3000)

            }

            composable("manage_device") { backStackEntry ->
                LandscapeCompose {
                    ManageDeviceScreen(
                        viewModel,
                        onBackHandler = {
                            navController.popBackStack()
                        },
                        onDisconnectRobot = {
                            navController.popBackStack()

                        },
                    )

                }

            }
            composable("settings_screen") {
                SettingsScreen(
                    viewModel,
                    onLoginSettings = {
                        navController.navigate("login_screen")
                    }

                )
            }


        }


    }

}