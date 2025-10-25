package clentlogic.cloy.crobotcontroller.presentation.ui.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSizeModel
import clentlogic.cloy.crobotcontroller.presentation.ui.components.EnableBluetoothAlertDialog
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ToggleSystemBars
import clentlogic.cloy.crobotcontroller.presentation.ui.navigation.AppNavHost
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.CRobotControllerTheme
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LightPink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var bleHelper: BleHelper

    private lateinit var blePermissionHandler: BlePermissionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        blePermissionHandler = BlePermissionHandler(this, bleHelper)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContent {
            CRobotControllerTheme {
                AppNavHost(this, blePermissionHandler)

            }
        }

        bleHelper.registerBluetoothStateReceiver()

    }

    override fun onDestroy() {
        super.onDestroy()
        bleHelper.unregisterBluetoothStateReceiver()
    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainCompose(
    viewModel: MainViewContract,
    blePermissionHandler: BlePermissionHandler,
    onOpenDevice:(Map.Entry<String, BluetoothDevice>) -> Unit
) {

    val bluetoothState by viewModel.bluetoothState.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val scanningState by viewModel.scanningState.collectAsState()

    ToggleSystemBars()

    val configuration = LocalConfiguration.current

    val screenSize = remember(configuration) {
        ScreenSizeModel(
            configuration.screenWidthDp.dp,
            configuration.screenHeightDp.dp,
        )
    }

    var deviceName by rememberSaveable(bluetoothState) {
        mutableStateOf(if (bluetoothState == BluetoothState.BluetoothDisabled) "BT disabled" else "None")}



    MainContent(
        screenSize,
        viewModel,
        bluetoothState,
        connectionState,
        scanningState,
        blePermissionHandler,
        deviceName,
        { deviceName = it },
        onOpenDevice,
    )

}

@Composable
fun MainContent(
    screenSize: ScreenSizeModel,
    viewModel: MainViewContract,
    bluetoothState: BluetoothState,
    connectionState: BleConnectionState,
    scanningState: ScanningState,
    blePermissionHandler: BlePermissionHandler,
    deviceName: String,
    onDeviceNameChange: (String) -> Unit,
    onOpenDevice: (Map.Entry<String, BluetoothDevice>) -> Unit
) {

    Column {
        //TopView
        TopStatusView(
            screenSize,
            scanningState,
            connectionState,
        )

        //DeviceView (middle)
        DeviceView(
            screenSize,
            deviceName,

            )
        //
        AvailableDevicesView(
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
fun TopStatusView(
    screenSize: ScreenSizeModel,
    scanningState: ScanningState,
    connectionState: BleConnectionState

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
        connState =  when (scanningState){
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
                    println("Settings Button Clicked!")
                }
                .scale(settingsImgScale)
        )

    }


}

@Composable
fun DeviceView(
    screenSize: ScreenSizeModel,
    deviceName: String,

    ) {

    val layout = remember(screenSize) {
        val screenSizeH = screenSize.h * 0.40f
        val padding = (screenSize.h + screenSize.w) * 0.01f
        LayoutModel(screenSizeH = screenSizeH, padding = padding)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(DeepTeal)
            .fillMaxWidth()
            .height(layout.screenSizeH)
            .padding(layout.padding)
    ) {

        Text(
            deviceName,
            color = Color.White,
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            "Robot",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.alpha(layout.alpha)
        )

    }
}

@Composable
fun AvailableDevicesView(
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

    var devicesInfo by rememberSaveable(devices) { mutableStateOf(
        if (devices.isEmpty()) "No Robots Found: " else "Found Robots: ")}

    val layout = remember(screenSize) {
        val screenSizeH = screenSize.h * 0.35f
        val imgSize = (screenSize.h + screenSize.w) * 0.02f
        val padding = (screenSize.h + screenSize.w) * 0.01f
        LayoutModel(
            screenSizeH = screenSizeH,
            padding = padding,
            imgSize = imgSize
        )
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
                    Image(
                        painterResource(R.drawable.retry),
                        contentDescription = null,
                        modifier = Modifier.size(layout.imgSize * 0.90f).clickable {
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


        DeviceListView(
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

    ScanButton(
        screenSize,
        layout,
        bluetoothState,
        connectionState,
        blePermissionHandler,
        viewModel
    )


}


@Composable
fun DeviceListView(
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

            DeviceListItem(
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
fun DeviceListItem(
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
            .height(layout.screenSizeH * 0.25f)
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


@Composable
fun ScanButton(
    screenSize: ScreenSizeModel,
    layout: LayoutModel,
    bluetoothState: BluetoothState,
    connectionState: BleConnectionState,
    blePermissionHandler: BlePermissionHandler,
    viewModel: MainViewContract
) {

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(layout.screenSizeH + 5.dp)
            .padding(layout.padding)
    ) {

        Button(onClick = { viewModel.disconnectDevice() }) { Text("Disconnect") }
        Button(onClick = { viewModel.sendDataToBleDevice("yeahh") }) { Text("Send") }

    }

}

