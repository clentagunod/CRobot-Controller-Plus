package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.GLOBAL_MODE
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.LOCAL_MODE
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.domain.model.LoginCredential
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ToggleSystemBars
import clentlogic.cloy.crobotcontroller.presentation.ui.util.getScreenSize
import kotlin.math.log


@Composable
fun SettingsScreen(
    viewModel: MainViewContract,
    onLoginSettings: () -> Unit,
    modifier: Modifier = Modifier
) {

    val loginCredential by viewModel.loginCredential.collectAsState(LoginCredential("", "", ""))
    val loginStatus by viewModel.loginStatus.collectAsState(false)

    val screenSize = getScreenSize()

    val layout = remember {
        val padding = (screenSize.w + screenSize.h) * 0.01f
        val screenSizeH = screenSize.h
        LayoutModel(padding = padding, screenSizeH = screenSizeH)
    }

    var isControlModeVisible by remember { mutableStateOf(false) }
    var isLoginCredentialVisible by remember { mutableStateOf(false) }

    val currentMode by viewModel.controlModeFlow.collectAsState(LOCAL_MODE)

    ToggleSystemBars()

    BackHandler(isControlModeVisible) {
        isControlModeVisible = false
    }



    Box {
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
                        if (loginStatus){
                            isLoginCredentialVisible = !isLoginCredentialVisible
                        }else {
                            onLoginSettings()
                        }

                    }
            ) {
                Icon(
                    painterResource(R.drawable.user),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
                Spacer(modifier.width(10.dp))
                Column {
                    Text(text =
                        if(loginStatus) loginCredential.username else "LOGIN",
                        style = MaterialTheme.typography.headlineSmall)
                    if (loginStatus) {
                        Text(
                            text = loginCredential.email,
                            color = Color.Black.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                }
                Spacer(modifier.weight(1f))
                Icon(
                    painterResource(R.drawable.settings_right),
                    tint = Color.Unspecified,
                    contentDescription = null,
                )
            }

            if (loginStatus)
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
                        isControlModeVisible = !isControlModeVisible

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
                    text = if (currentMode == LOCAL_MODE) "Local" else "Global",
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

    ControlMode(
        viewModel,
        currentMode,
        layout,
        isControlModeVisible,
        onControlModeVisibleChange = {
            isControlModeVisible = false

        }
    )

    AccountSettings(
        viewModel,
        layout,
        isLoginCredentialVisible,
        onLoginSettings,
        onAccountSettingsVisible = {
            isLoginCredentialVisible = false
        }

    )


}



@Composable
private fun AccountSettings(
    viewModel: MainViewContract,
    layout: LayoutModel,
    visible: Boolean,
    onLoginSettings: () -> Unit,
    onAccountSettingsVisible: () -> Unit,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(400)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(400)

        ),
    ) {
        Box(
            modifier = modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(layout.padding)
                .clickable(false) {}
        ) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                Spacer(modifier.height(40.dp))
                Row {
                    Icon(
                        painterResource(R.drawable.back),
                        contentDescription = null,
                        modifier = modifier.clickable {
                            onAccountSettingsVisible()

                        }
                    )
                    Spacer(modifier.width(10.dp))
                    Text("Account Settings", style = MaterialTheme.typography.displayMedium)
                }
                Spacer(modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.signOut()
                        onLoginSettings()
                    }
                ){
                    Text("Sign Out")
                }


            }


        }
    }

}



@Composable
private fun ControlMode(
    viewModel: MainViewContract,
    currentMode: String,
    layout: LayoutModel,
    visible: Boolean,
    onControlModeVisibleChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(400)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(400)

        ),
    ) {
        Box(
            modifier = modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(layout.padding)
                .clickable(false) {}
        ) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                Spacer(modifier.height(40.dp))
                Row {
                    Icon(
                        painterResource(R.drawable.back),
                        contentDescription = null,
                        modifier = modifier.clickable {
                            onControlModeVisibleChange()

                        }
                    )
                    Spacer(modifier.width(10.dp))
                    Text("Control Mode", style = MaterialTheme.typography.displayMedium)
                }
                Spacer(modifier.height(20.dp))
                ControlModeRadioButtons(
                    viewModel,
                    currentMode,
                )


            }


        }
    }

}

@Composable
fun ControlModeRadioButtons(
    viewModel: MainViewContract,
    currentMode: String,
    modifier: Modifier = Modifier

) {
    val listOfModes by remember { mutableStateOf(listOf(LOCAL_MODE, GLOBAL_MODE)) }


    Column(modifier.selectableGroup()
        .fillMaxSize()) {

        listOfModes.forEach { mode ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .selectable(
                        selected = (mode == currentMode),
                        onClick = { viewModel.setControlModeState(mode) }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (mode == currentMode),
                    onClick = null
                )
                Spacer(modifier.width(15.dp))

                Text(
                    text = when(mode){
                        LOCAL_MODE -> "Local Mode"
                        GLOBAL_MODE -> "Global Mode"
                        else -> ""

                    },
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.headlineLarge
                )

            }


        }

    }

}