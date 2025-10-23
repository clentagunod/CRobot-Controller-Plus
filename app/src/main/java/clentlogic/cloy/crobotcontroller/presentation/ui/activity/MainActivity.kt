package clentlogic.cloy.crobotcontroller.presentation.ui.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.core.content.edit
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSize
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.CheckPermissionCompose
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.CRobotControllerTheme
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LightPink
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.Pink
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

    var deviceName by rememberSaveable { mutableStateOf( "No Device" )}


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
            deviceName

        )
        //
        AvailableDevicesView(
            screenSize,
            viewModel,
            bluetoothState,
            blePermissionHandler,
            connectionState,
            onDeviceNameChange

        )


    }


}

@Composable
fun TopView(
    screenSize: ScreenSize,
    connectionState: BleConnectionState

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
            if (connectionState == BleConnectionState.Connected) {
                Text(
                    "Connected",
                    color = Color.Green,
                    style = MaterialTheme.typography.displayMedium
                )
            } else {
                Text(
                    "Disconnected",
                    color = Color.Red,
                    style = MaterialTheme.typography.displayMedium
                )

            }

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
    deviceName: String

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
                            viewModel.startScanning(9000L)
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

    }

}


@Composable
fun DeviceListView(
    layout: LayoutModel,
    devices: Map<String, BluetoothDevice>,
    viewModel: MainViewContract,
    connectionState: BleConnectionState,
    onDeviceNameChange: (String) -> Unit
) {


    val coroutineScope = rememberCoroutineScope()


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(layout.padding),
        userScrollEnabled = true
    ) {
        items(
            devices.entries.toList()
        ) { device ->

            DeviceListItems(
                device,
                layout,
                coroutineScope,
                viewModel,
                connectionState,
                onDeviceNameChange
            )

        }

    }
}


@Composable
fun DeviceListItems(
    device: Map.Entry<String, BluetoothDevice>,
    layout: LayoutModel,
    coroutineScope: CoroutineScope,
    viewModel: MainViewContract,
    connectionState: BleConnectionState,
    onDeviceNameChange: (String) -> Unit

) {

    var isConnected by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color by animateColorAsState(
        targetValue = if (isPressed) DeepTeal else Color.Black,
        animationSpec = tween(durationMillis = 100),
        label = "Connect Button Text"
    )

    val shape = remember { RoundedCornerShape(layout.borderRadius) }


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
                    modifier = Modifier
                        .alpha(layout.alpha)
                )

                if (connectionState != BleConnectionState.Connected) {
                    isConnected = false
                    onDeviceNameChange("No Device")

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

                            coroutineScope.launch {
                                viewModel.connectToDevice(device.value)
                                delay(1000L)
                                isLoading = false
                                onDeviceNameChange(device.key)
                                isConnected = connectionState != BleConnectionState.Connected

                            }
                        }
                )


            }

        }


    }
}

@Composable
fun ToggleSystemBars(hide: Boolean = true) {
    val view = LocalView.current
    val window = (view.context as ComponentActivity).window
    val windowController = WindowInsetsControllerCompat(window, view)

    if (hide) {
        windowController.hide(WindowInsetsCompat.Type.systemBars())
        windowController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        windowController.show(WindowInsetsCompat.Type.systemBars())
    }

}

@Composable
fun ScanningLoadingScreen(show: Boolean) {

    if(show){
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = {}

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(false) {}
            ) {
                Row() {
                    Text(
                        "Scanning devices..",
                        color = Color.White,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                }
            }

        }

    }



}

@Composable
fun EnableBluetoothAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show){
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text("Enable Bluetooth")
            },
            text = {

                Text("Bluetooth is disabled. Please turn on your Bluetooth to start scanning.")

            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text("Yes")
                }},
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text("No")
                }},
            shape = RoundedCornerShape(4.dp),
            containerColor = DeepTeal,
            titleContentColor = Color.White,
            textContentColor = Color.White,
        )

    }

}

@Composable
fun AppNavHost(
    activity: ComponentActivity,
    blePermissionHandler: BlePermissionHandler
) {
    val navController = rememberNavController()
    val prefs = activity.getSharedPreferences("app_prefs", MODE_PRIVATE)
    val permissionGranted = prefs.getBoolean("permissions_ok", false)

    NavHost(
        navController = navController,
        startDestination = if (permissionGranted) "main" else "check_permission"
    ) {
        composable(
            "check_permission"
        ) {
            CheckPermissionCompose(
                blePermissionHandler,
                onPermitted = {
                    prefs.edit { putBoolean("permissions_ok", true) }
                    navController.navigate("main") {
                        popUpTo("check_permission") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainCompose(
                blePermissionHandler
            )
        }
    }


}