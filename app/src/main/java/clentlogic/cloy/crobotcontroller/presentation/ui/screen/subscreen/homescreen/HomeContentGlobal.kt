package clentlogic.cloy.crobotcontroller.presentation.ui.screen.subscreen.homescreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.LOCAL_MODE
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.domain.model.WifiConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.WifiState
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSizeModel
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LimeGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeContentGlobal(
    viewModel: MainViewContract,
    screenSize: ScreenSizeModel,
    onOpenSettings: () -> Unit,
    onOpenDeviceGlobal: (Map.Entry<String, Boolean>) -> Unit

) {
    val currentControlMode by viewModel.controlModeFlow.collectAsState(LOCAL_MODE)
    val isOnline by viewModel.deviceConnectionStateGlobal.collectAsState()

    val wifiState by viewModel.wifiState.collectAsState()
    val wifiConnectionState by viewModel.wifiConnectionState.collectAsState()
    val wifiHasInternet by viewModel.wifiHasInternetConnection.collectAsState()

    var topStatus by remember {  mutableStateOf("Offline")}
    var robotNameStatus by remember { mutableStateOf("None") }



    TopStatusGlobal(
        topStatus,
        screenSize,
        onOpenSettings
    )

    RobotNameStatusGlobal(
        viewModel,
        screenSize,
        robotNameStatus,
        currentControlMode,
    )

    AvailableRobotFoundGlobal(
        screenSize,
        viewModel,
        isOnline,
        wifiState,
        wifiConnectionState,
        wifiHasInternet,
        onOpenDeviceGlobal,
        onChangeTopStatus = {
            topStatus = it
        },
        onChangeRobotName = {
            robotNameStatus = it
        }
    )

}


@Composable
private fun TopStatusGlobal(
    topStatus: String,
    screenSize: ScreenSizeModel,
    onOpenSettings: () -> Unit

) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val settingsImgScale by animateFloatAsState(
        targetValue = if (isPressed) 0.75f else 1f,
        label = "Settings Image"
    )

    val layout = remember(screenSize) {
        val h = screenSize.h * 0.20f
        val padding = (screenSize.h + screenSize.w) * 0.01f
        val imgSize = (screenSize.h + screenSize.w) * 0.03f
        LayoutModel(h, padding = padding, imgSize = imgSize)
    }

    val textColor by animateColorAsState(
        targetValue = when (topStatus) {
            "Offline" -> Color.Red
            "Online" -> Color.Green
            else -> Color.White
        }
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(DeepTeal)
            .fillMaxWidth()
            .height(layout.screenSizeH)
            .padding(layout.padding)
    ) {

        Column {

            Text(
                topStatus,
                color = textColor,
                style = MaterialTheme.typography.displayMedium
            )

            Text(
                "Status",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.alpha(layout.alpha)
            )
        }

        Image(
            painterResource(R.drawable.settings),
            contentDescription = "Settings Icon",
            modifier = Modifier
                .size(layout.imgSize)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onOpenSettings()
                }
                .scale(settingsImgScale)
        )

    }


}

@Composable
private fun RobotNameStatusGlobal(
    viewModel: MainViewContract,
    screenSize: ScreenSizeModel,
    robotNameStatus: String,
    currentControlMode: String
) {


    var currentModeIcon by remember { mutableIntStateOf(R.drawable.local) }

    LaunchedEffect(currentControlMode) {
        currentModeIcon =
            if (currentControlMode == LOCAL_MODE) R.drawable.local else R.drawable.global_small_icon

    }

    val layout = remember(screenSize) {
        if (screenSize.w > screenSize.h) {
            val screenSizeH = screenSize.h * 0.30f
            val padding = (screenSize.h + screenSize.w) * 0.01f
            val arrangement = Arrangement.Top
            LayoutModel(
                screenSizeH = screenSizeH,
                padding = padding,
                arrangementV = arrangement
            )
        } else {
            val screenSizeH = screenSize.h * 0.40f
            val padding = (screenSize.h + screenSize.w) * 0.01f
            LayoutModel(
                screenSizeH = screenSizeH,
                padding = padding
            )

        }

    }


    Box(
        modifier = Modifier
            .background(DeepTeal)
            .fillMaxWidth()
            .height(layout.screenSizeH)
            .padding(layout.padding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                robotNameStatus,
                color = Color.White,
                style = MaterialTheme.typography.displayLarge,

                )

            Text(
                "Robot",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.alpha(layout.alpha)
            )

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                if (currentControlMode == LOCAL_MODE) "Local" else "Global",
                color = Color.White,
                fontSize = 10.sp,
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(Modifier.width(5.dp))

            Icon(
                painterResource(currentModeIcon),
                tint = Color.Unspecified,
                contentDescription = null
            )

        }


    }
}


@Composable
private fun AvailableRobotFoundGlobal(
    screenSize: ScreenSizeModel,
    viewModel: MainViewContract,
    isOnline: Map<String, Boolean>,
    wifiState: WifiState,
    wifiConnectionState: WifiConnectionState,
    wifiHasInternet: Boolean,
    onOpenDeviceGlobal: (Map.Entry<String, Boolean>) -> Unit,
    onChangeTopStatus: (String) -> Unit,
    onChangeRobotName: (String) -> Unit,
) {

    var hasInternetConnectivity by remember { mutableStateOf(false) }

    val layout = remember(screenSize) {
        if (screenSize.w > screenSize.h) {
            val screenSizeH = screenSize.h * 0.58f
            val imgSize = (screenSize.h + screenSize.w) * 0.02f
            val padding = (screenSize.h + screenSize.w) * 0.01f
            val itemHeight = screenSizeH * 0.30f
            LayoutModel(
                screenSizeH = screenSizeH,
                padding = padding,
                imgSize = imgSize,
                itemHeight = itemHeight
            )
        } else {
            val screenSizeH = screenSize.h * 0.53f
            val imgSize = (screenSize.h + screenSize.w) * 0.02f
            val padding = (screenSize.h + screenSize.w) * 0.01f
            val itemHeight = screenSizeH * 0.20f
            LayoutModel(
                screenSizeH = screenSizeH,
                padding = padding,
                imgSize = imgSize,
                itemHeight = itemHeight
            )
        }

    }

    LaunchedEffect(wifiHasInternet, wifiConnectionState, wifiState) {
        if (wifiState == WifiState.WifiOn) {
            if (wifiConnectionState == WifiConnectionState.WifiConnected){

                if (wifiHasInternet) {
                    hasInternetConnectivity = true
                    println("Wifi has internet!")
                }else{
                    println("WIfi no internet!")
                    hasInternetConnectivity = false
                }

            }else{
                println("Wifi disconnected!")
                hasInternetConnectivity = false
            }

        }else {
            println("Wifi off")
            hasInternetConnectivity = false
        }
    }


    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(layout.screenSizeH)
            .padding(layout.padding)
            .consumeWindowInsets(WindowInsets.systemBars.asPaddingValues())

    ) {
        Row {
            Text(
                "List of Robots Globally: ",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = layout.padding)
            )

            Spacer(Modifier.width(layout.padding))
        }

        if (hasInternetConnectivity) {
            RobotListGlobal(
                layout,
                viewModel,
                isOnline,
                onOpenDeviceGlobal,
                onChangeTopStatus,
                onChangeRobotName,
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(layout.screenSizeH)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No Internet Access",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "You don't have internet access or please enable your wifi!",
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.labelSmall

                    )

                }

            }
        }


    }

}

@Composable
private fun RobotListGlobal(
    layout: LayoutModel,
    viewModel: MainViewContract,
    isOnline: Map<String, Boolean>,
    onOpenDeviceGlobal: (Map.Entry<String, Boolean>) -> Unit,
    onChangeTopStatus: (String) -> Unit,
    onChangeRobotName: (String) -> Unit,
) {


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(layout.padding),
        userScrollEnabled = true
    ) {
        items(
            isOnline.entries.toList(),
            key = { it.key }
        ) { device ->

            RobotListItemGlobal(
                layout,
                device,
                onChangeTopStatus,
                onChangeRobotName,
                onOpenDeviceGlobal
            )


        }
    }
}

@Composable
private fun RobotListItemGlobal(
    layout: LayoutModel,
    device: Map.Entry<String, Boolean>,
    onChangeTopStatus: (String) -> Unit,
    onChangeRobotName: (String) -> Unit,
    onOpenDeviceGlobal: (Map.Entry<String, Boolean>) -> Unit

    ) {
    val shape = remember { RoundedCornerShape(layout.borderRadius) }
    var isConnected by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(device.value, device.key) {
        if (!device.value) {
            onChangeTopStatus("Offline")
        }

    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(layout.itemHeight)
            .background(LimeGreen, shape = shape)
            .padding(layout.padding)
    ) {
        Column {
            Text(
                device.key,
                color = DeepTeal,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = if (device.value) "Online" else "Offline",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.alpha(layout.alpha)
            )
        }
        Spacer(Modifier.weight(1f))

        if (device.value) {
            when {
                isConnected -> {
                    TextButton(
                        onClick = {
                            onOpenDeviceGlobal(device)
                        }
                    ) {
                        Text(
                            "Open",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black
                        )

                    }

                    onChangeRobotName(device.key)
                    onChangeTopStatus("Online")
                }
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(layout.imgSize),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                    onChangeTopStatus("Linking")
                }
                else -> {
                    Text(
                        "Connect",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.clickable{
                            isLoading = true

                            coroutineScope.launch {
                                delay(3000)
                                isLoading = false
                                isConnected = true
                            }
                        }
                    )


                }

            }


        }


    }
}
