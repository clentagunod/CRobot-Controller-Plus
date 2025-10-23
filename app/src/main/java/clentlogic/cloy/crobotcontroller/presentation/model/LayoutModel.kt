package clentlogic.cloy.crobotcontroller.presentation.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class LayoutModel(
    val screenSizeH: Dp = 0.dp,
    val screenSizeW: Dp = 0.dp,
    val padding: Dp = 0.dp,
    val imgSize: Dp = 0.dp,
    val alpha: Float = 0.6f,
    val borderRadius: Dp = 3.dp
)