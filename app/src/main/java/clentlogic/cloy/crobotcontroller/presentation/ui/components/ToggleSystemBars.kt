package clentlogic.cloy.crobotcontroller.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun ToggleSystemBars(hide: Boolean = true) {
    val view = LocalView.current
    val window = (view.context as ComponentActivity).window
    val windowController = WindowInsetsControllerCompat(window, view)

    if (hide) {
        windowController.hide(WindowInsetsCompat.Type.systemBars())
        windowController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        windowController.show(WindowInsetsCompat.Type.systemBars())
    }

}