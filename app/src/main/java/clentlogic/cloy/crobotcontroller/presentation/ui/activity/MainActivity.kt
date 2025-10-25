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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSize
import clentlogic.cloy.crobotcontroller.presentation.ui.components.EnableBluetoothAlertDialog
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ScanningLoadingScreen
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ToggleSystemBars
import clentlogic.cloy.crobotcontroller.presentation.ui.navigation.AppNavHost
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.CRobotControllerTheme
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LightPink
import clentlogic.cloy.crobotcontroller.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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
    blePermissionHandler: BlePermissionHandler,
    viewModel: MainViewContract = hiltViewModel<MainViewModel>()
) {

    val bluetoothState by viewModel.bluetoothState.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()

    ToggleSystemBars()

    val configuration = LocalConfiguration.current

    val screenSize = remember(configuration) {
        ScreenSize(
            configuration.screenWidthDp.dp,
            configuration.screenHeightDp.dp
        )
    }

    var deviceName by rememberSaveable(bluetoothState) { mutableStateOf( if (bluetoothState == BluetoothState.BluetoothDisabled) "BT disabled" else "No Device" )}

    MainContent(
        screenSize,
        viewModel,
        bluetoothState,
        connectionState,
        blePermissionHandler,
        deviceName,
         { deviceName = it }
    )

}

@Composable
fun MainContent(
    screenSize: ScreenSize,
    viewModel: MainViewContract,
    bluetoothState: BluetoothState,
    connectionState: BleConnectionState,
    blePermissionHandler: BlePermissionHandler,
    deviceName: String,
    onDeviceNameChange: (String) -> Unit
) {

    Column {
        //TopView
        TopView(
            screenSize,
            connectionState
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
            blePermissionHandler,
            connectionState,
            deviceName,
            onDeviceNameChange

        )


    }

}

@Composable
fun TopView(
    screenSize: ScreenSize,
    connectionState: BleConnectionState

) {

    var connState by rememberSaveable { mutableStateOf("Disconnected") }

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

    LaunchedEffect(connectionState) {
        connState = when (connectionState){
            BleConnectionState.Connecting -> "Connecting"
            BleConnectionState.Connected -> "Connected"
            BleConnectionState.Disconnected -> "Disconnected"
            is BleConnectionState.Error -> "Error"
            BleConnectionState.Scanning -> "Scanning"
            BleConnectionState.TimeOut -> "Time Out"

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
                color = Color.Green,
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
    screenSize: ScreenSize,
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
            "Device",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.alpha(layout.alpha)
        )

    }
}

@Composable
fun AvailableDevicesView(
    screenSize: ScreenSize,
    viewModel: MainViewContract,
    bluetoothState: BluetoothState,
    blePermissionHandler: BlePermissionHandler,
    connectionState: BleConnectionState,
    deviceName: String,
    onDeviceNameChange: (String) -> Unit
) {
    val devices by viewModel.device.collectAsState()

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


    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(layout.screenSizeH)
            .padding(layout.padding)
    ) {
        Text(
            "Devices Found:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = layout.padding)
        )

        DeviceListView(
            layout,
            devices,
            viewModel,
            connectionState,
            deviceName,
            onDeviceNameChange

        )

    }

    ScanButton(
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
    deviceName: String,
    onDeviceNameChange: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

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
            BleConnectionState.TimeOut -> {
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
                Text(
                    "Connected",
                    color = color,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.alpha(layout.alpha)
                )
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
    layout: LayoutModel,
    bluetoothState: BluetoothState,
    connectionState: BleConnectionState,
    blePermissionHandler: BlePermissionHandler,
    viewModel: MainViewContract
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.80f else 1f,
        label = "Scan Button"
    )

    var enableAlertDialog by remember { mutableStateOf(false)}

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

    ScanningLoadingScreen(connectionState == BleConnectionState.Scanning)

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(layout.screenSizeH + 5.dp)
            .padding(layout.padding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.scan_svgrepo_com),
                contentDescription = null,
                modifier = Modifier
                    .scale(scale)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) {
                        println("Scan button is clicked!")

                        if (bluetoothState == BluetoothState.BluetoothDisabled){
                            enableAlertDialog = true
                        }else{
                            viewModel.startScanning(2000L)
                        }


                    }
            )
            Text(
                "Scan",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.alpha(layout.alpha)
            )
            Button(onClick = { viewModel.disconnectDevice() }) { Text("Disconnect") }
        }
        Button(onClick = { viewModel.sendDataToBleDevice("yeahh") }) { Text("Send") }

    }

}

