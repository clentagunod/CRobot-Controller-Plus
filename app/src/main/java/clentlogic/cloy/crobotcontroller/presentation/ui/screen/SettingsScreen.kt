package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ToggleSystemBars
import clentlogic.cloy.crobotcontroller.presentation.ui.util.getScreenSize


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {

    val screenSize = getScreenSize()

    val layout = remember {
        val padding = (screenSize.w + screenSize.h) * 0.01f
        val screenSizeH = screenSize.h
        LayoutModel(padding = padding, screenSizeH = screenSizeH)
    }

    ToggleSystemBars()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(layout.padding)
    ) {
        Spacer(modifier.height(40.dp))
        Text("Settings", style = MaterialTheme.typography.displayMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) {
                    //
                }
        ) {
            Icon(painterResource(R.drawable.user),
                tint = Color.Unspecified,
                contentDescription = null
            )
            Spacer(modifier.width(10.dp))
            Column {
                Text("Username", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "user@gmail.com",
                    color = Color.Black.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier.weight(1f))
            Icon(
                painterResource(R.drawable.settings_right),
                tint = Color.Unspecified,
                contentDescription = null,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) {
                    //
                }

        ) {
            Icon(
                painterResource(R.drawable.control),
                tint = Color.Unspecified,
                contentDescription = null,
            )
            Spacer(modifier.width(10.dp))
            Text(
                "Control Mode",
                fontSize = 15.sp,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier.weight(1f))
            Text(
                "Local",
                color = Color.Black.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall
            )
            Icon(
                painterResource(R.drawable.settings_right),
                tint = Color.Unspecified,
                contentDescription = null,
            )

        }
        HorizontalDivider(thickness = 0.2.dp, color = Color.Black.copy(alpha = 0.5f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) {
                    //
                }

        ) {
            Icon(
                painterResource(R.drawable.link),
                tint = Color.Unspecified,
                contentDescription = null,
            )
            Spacer(modifier.width(10.dp))
            Text(
                "Camera Link",
                fontSize = 15.sp,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier.weight(1f))
            Icon(
                painterResource(R.drawable.settings_right),
                tint = Color.Unspecified,
                contentDescription = null,
            )

        }


    }


}

@Composable
fun SettingsScreenContent() {

}