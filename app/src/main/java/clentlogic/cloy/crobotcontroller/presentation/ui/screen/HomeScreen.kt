package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSizeModel
import clentlogic.cloy.crobotcontroller.presentation.ui.components.EnableBluetoothAlertDialog
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ToggleSystemBars
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LightPink
import clentlogic.cloy.crobotcontroller.presentation.ui.util.getScreenSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomeScreen(
    viewModel: MainViewContract,
    blePermissionHandler: BlePermissionHandler,
    onOpenSettings: () -> Unit,
    onOpenDevice: (Map.Entry<String, BluetoothDevice>) -> Unit
) {

    val bluetoothState by viewModel.bluetoothState.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val scanningState by viewModel.scanningState.collectAsState()

    ToggleSystemBars()

    val screenSize = getScreenSize()

    var deviceName by rememberSaveable(bluetoothState) {
        mutableStateOf(if (bluetoothState == BluetoothState.BluetoothDisabled) "BT disabled" else "None")
    }


    HomeScreenContent(
        screenSize,
        viewModel,
        bluetoothState,
        connectionState,
        scanningState,
        blePermissionHandler,
        deviceName,
        { deviceName = it },
        onOpenSettings,
        onOpenDevice,
    )

}

@Composable
private fun HomeScreenContent(
    screenSize: ScreenSizeModel,
    viewModel: MainViewContract,
    bluetoothState: BluetoothState,
    connectionState: BleConnectionState,
    scanningState: ScanningState,
    blePermissionHandler: BlePermissionHandler,
    deviceName: String,
    onDeviceNameChange: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenDevice: (Map.Entry<String, BluetoothDevice>) -> Unit
) {

    Column {
        //TopView
        TopStatus(
            screenSize,
            scanningState,
            connectionState,
            onOpenSettings,
        )

        //DeviceView (middle)
        RobotNameStatus(
            viewModel,
            screenSize,
            deviceName,

            )
        //
        AvailableRobotFound(
            screenSize,
            viewModel,
            bluetoothState,
            scanningState,
            blePermissionHandler,
            connectionState,
            deviceName,
            onDeviceNameChange,
            onOpenDevice,

            )


    }

}

@Composable
private fun TopStatus(
    screenSize: ScreenSizeModel,
    scanningState: ScanningState,
    connectionState: BleConnectionState,
    onOpenSettings: () -> Unit,

    ) {


    var connState by rememberSaveable { mutableStateOf("Disconnected") }
    val textColor by animateColorAsState(
        targetValue = when (connState) {
            "Connected" -> Color.Green
            "Disconnected", "Error" -> Color.Red
            "Scanning" -> Color.White.copy(alpha = 0.85f)
            else -> Color.White
        },
        label = "Text Color"

    )


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

    LaunchedEffect(connectionState, scanningState) {
        println("Scanning State: $scanningState and Connection: $connectionState")
        connState = when (scanningState) {
            ScanningState.Scanning -> "Scanning"
            ScanningState.ScanningFinished -> {
                when (connectionState) {
                    BleConnectionState.Connected -> "Connected"
                    BleConnectionState.Connecting -> "Connecting"
                    BleConnectionState.Disconnected -> "Disconnected"
                    is BleConnectionState.Error -> "Error"
                }
            }

            is ScanningState.ErrorScanning -> TODO()
        }

    }

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
                connState,
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
private fun RobotNameStatus(
    viewModel: MainViewContract,
    screenSize: ScreenSizeModel,
    deviceName: String,

    ) {

    val currentControlMode by viewModel.controlModeFlow.collectAsState(LOCAL_MODE)
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
                deviceName,
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
private fun AvailableRobotFound(
    screenSize: ScreenSizeModel,
    viewModel: MainViewContract,
    bluetoothState: BluetoothState,
    scanningState: ScanningState,
    blePermissionHandler: BlePermissionHandler,
    connectionState: BleConnectionState,
    deviceName: String,
    onDeviceNameChange: (String) -> Unit,
    onOpenDevice: (Map.Entry<String, BluetoothDevice>) -> Unit
) {
    val devices by viewModel.device.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }

    var devicesInfo by rememberSaveable(devices) {
        mutableStateOf(
            if (devices.isEmpty()) "No Robots Found: " else "Found Robots: "
        )
    }

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


    var enableAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(scanningState) {
        isLoading = scanningState == ScanningState.Scanning
    }

    EnableBluetoothAlertDialog(
        enableAlertDialog,
        onDismiss = {
            println("Dismissed")
            enableAlertDialog = false

        },
        onConfirm = {
            println("Confirmed")
            enableAlertDialog = false
            blePermissionHandler.enableBluetooth()

        }

    )


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
                devicesInfo,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = layout.padding)
            )

            Spacer(Modifier.width(layout.padding))

            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = DeepTeal,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(layout.imgSize * 0.90f)
                    )

                }

                else -> {
                    Icon(
                        painterResource(R.drawable.retry),
                        contentDescription = null,
                        modifier = Modifier
                            .size(layout.imgSize * 0.90f)
                            .clickable {
                                coroutineScope.launch {
                                    if (bluetoothState == BluetoothState.BluetoothDisabled) {
                                        enableAlertDialog = true
                                    } else {
                                        viewModel.startScanning(3000L)
                                    }
                                }
                            }
                    )

                }

            }


        }


        RobotList(
            layout,
            devices,
            viewModel,
            connectionState,
            coroutineScope,
            deviceName,
            onDeviceNameChange,
            onOpenDevice,

            )

    }

}


@Composable
private fun RobotList(
    layout: LayoutModel,
    devices: Map<String, BluetoothDevice>,
    viewModel: MainViewContract,
    connectionState: BleConnectionState,
    coroutineScope: CoroutineScope,
    deviceName: String,
    onDeviceNameChange: (String) -> Unit,
    onOpenDevice: (Map.Entry<String, BluetoothDevice>) -> Unit
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(layout.padding),
        userScrollEnabled = true
    ) {
        items(
            devices.entries.toList(),
            key = { it.key }
        ) { device ->

            RobotListItem(
                device = device,
                layout = layout,
                coroutineScope = coroutineScope,
                viewModel = viewModel,
                connectionState = connectionState,
                deviceName = deviceName,
                onDeviceNameChange = onDeviceNameChange,
                onOpenDevice = onOpenDevice,
            )
        }
    }
}

@Composable
private fun RobotListItem(
    device: Map.Entry<String, BluetoothDevice>,
    layout: LayoutModel,
    coroutineScope: CoroutineScope,
    viewModel: MainViewContract,
    connectionState: BleConnectionState,
    deviceName: String,
    onDeviceNameChange: (String) -> Unit,
    onOpenDevice: (Map.Entry<String, BluetoothDevice>) -> Unit
) {

    val isConnected = remember(connectionState, deviceName, device.key) {
        connectionState == BleConnectionState.Connected && deviceName == device.key
    }


    var isLoading by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color by animateColorAsState(
        targetValue = if (isPressed) DeepTeal else Color.Black,
        animationSpec = tween(durationMillis = 100),
        label = "Connect Button Text"
    )

    val shape = remember { RoundedCornerShape(layout.borderRadius) }

    LaunchedEffect(connectionState) {
        println(connectionState)
        when (connectionState) {
            BleConnectionState.Connected -> {
                if (deviceName == device.key) {
                    isLoading = false
                    Log.d("DeviceListItem", "Device connected: ${device.key}")
                }
            }

            BleConnectionState.Disconnected -> {
                isLoading = false

            }

            is BleConnectionState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(layout.itemHeight)
            .background(LightPink, shape = shape)
            .padding(layout.padding)
    ) {
        Column {
            Text(
                device.key,
                color = DeepTeal,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                "${device.value}",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.alpha(layout.alpha)
            )
        }

        when {
            isConnected -> {
                TextButton(onClick = {
                    onOpenDevice(device)
                }) {
                    Text(
                        "Open",
                        color = color,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.alpha(layout.alpha)
                    )
                }

            }

            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(layout.imgSize),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            }

            else -> {
                Text(
                    "Connect",
                    color = color,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .alpha(layout.alpha)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            isLoading = true
                            onDeviceNameChange(device.key)

                            coroutineScope.launch {
                                try {
                                    if (connectionState == BleConnectionState.Connected) {
                                        viewModel.disconnectDevice()
                                    }
                                    viewModel.connectToDevice(device.value)
                                } catch (e: Exception) {
                                    Log.e("DeviceListItem", "Connection failed", e)
                                    isLoading = false
                                    onDeviceNameChange("No Device")
                                }
                            }
                        }
                )
            }
        }
    }
}
