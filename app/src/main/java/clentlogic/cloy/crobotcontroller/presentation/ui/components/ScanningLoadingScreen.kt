package clentlogic.cloy.crobotcontroller.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSizeModel


@Composable
fun ScanningLoadingScreen(
    screenSize: ScreenSizeModel,
    viewModel: MainViewContract,
    show: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(200),
        label = "Stop Scanning button"
    )

    if(show){
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = {}

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(false) {}
            ) {
                Row {
                    Text(
                        "Scanning devices..",
                        color = Color.White,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 5.dp,
                        modifier = Modifier.size(20.dp).align(Alignment.CenterVertically)
                    )
                }

                Image(
                    painterResource(R.drawable.stop),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .offset( y = screenSize.h * -0.10f)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ){
                           viewModel.stopScan()
                        }.scale(scale)
                )
            }

        }

    }



}