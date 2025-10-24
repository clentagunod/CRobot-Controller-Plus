package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LimeGreen

@SuppressLint("RememberReturnType")
@Composable
fun CheckPermissionCompose(
    blePermissionHandler: BlePermissionHandler,
    onPermitted: () -> Unit
) {


    LaunchedEffect(Unit) {
        blePermissionHandler.checkBlePermission()

    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f)

    var titleY by remember { mutableStateOf(0.dp) }
    var descY by remember { mutableStateOf(0.dp) }
    var buttonY by remember { mutableStateOf(0.dp) }
    var imageY by remember { mutableStateOf(0.dp) }
    var imageSize by remember { mutableStateOf(0.dp) }
    var buttonW by remember { mutableFloatStateOf(0f) }
    var buttonH by remember { mutableFloatStateOf(0f) }


    Scaffold(
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DeepTeal)
        ) {
            val screenHeight = maxHeight
            val screenWidth = maxWidth

            if (screenWidth > screenHeight) {
                titleY = screenHeight * 0.55f
                descY = screenHeight * 0.70f
                buttonY = screenHeight * 0.80f
                imageY = screenHeight * 0.10f
                imageSize = 150.dp
                buttonW = 0.40f
                buttonH = 0.12f
            } else {
                titleY = screenHeight * 0.63f
                descY = screenHeight * 0.70f
                buttonY = screenHeight * 0.80f
                imageY = screenHeight * 0.20f
                imageSize = 300.dp
                buttonW = 0.45f
                buttonH = 0.08f
            }

            Image(
                painterResource(R.drawable.background),
                contentDescription = "backgroundImage",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )


            Image(
                painterResource(R.drawable.firmware),
                contentDescription = "homeImage",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = imageY)
                    .size(imageSize)
            )


            Text(
                text = "CRobot Controller+",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = titleY)
            )

            Text(
                text = "Monitor, control and manage your CRobot.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = descY)
                    .width(screenWidth * 0.7f)
            )

            Button(
                onClick = {
                    onPermitted()
                },
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LimeGreen,
                    contentColor = DeepTeal,
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = buttonY)
                    .size(screenWidth * buttonW, screenHeight * buttonH)
                    .scale(scale)
            ) {
                Text(
                    text = "Let's start",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


