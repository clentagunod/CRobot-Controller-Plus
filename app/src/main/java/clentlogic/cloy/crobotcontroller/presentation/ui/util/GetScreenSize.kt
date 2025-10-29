package clentlogic.cloy.crobotcontroller.presentation.ui.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSizeModel


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun getScreenSize(): ScreenSizeModel {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        ScreenSizeModel(
            configuration.screenWidthDp.dp,
            configuration.screenHeightDp.dp
    )}
}