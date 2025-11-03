package clentlogic.cloy.crobotcontroller.presentation.ui.components.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.presentation.ui.components.AnimatedLottieJson
import kotlinx.coroutines.delay


@Composable
fun SplashLoadingScreen(
    navController: NavController,
    delay: Long,
    modifier: Modifier = Modifier
) {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            AnimatedLottieJson(R.raw.animated_hand_loading, 150.dp)
        }

    }

    LaunchedEffect(Unit) {
        delay(delay)
        navController.navigate("manage_device"){
            popUpTo("splash") { inclusive = true}
        }
        showSplash = false


    }

}