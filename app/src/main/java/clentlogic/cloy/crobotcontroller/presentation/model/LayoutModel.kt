package clentlogic.cloy.crobotcontroller.presentation.model

import android.text.Layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class LayoutModel(
    val screenSizeH: Dp = 0.dp,
    val screenSizeW: Dp = 0.dp,
    val padding: Dp = 0.dp,
    val imgSize: Dp = 0.dp,
    val alpha: Float = 0.6f,
    val borderRadius: Dp = 3.dp,
    val itemHeight: Dp = 0.dp,
    val alignmentH: Alignment.Horizontal = Alignment.CenterHorizontally,
    val alignmentV: Alignment.Vertical = Alignment.CenterVertically,
    val arrangementH: Arrangement.Horizontal = Arrangement.Center,
    val arrangementV: Arrangement.Vertical = Arrangement.Center,
)