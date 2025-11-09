package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.LOCAL_MODE
import clentlogic.cloy.crobotcontroller.data.communication.ble.BlePermissionHandler
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSizeModel
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ToggleSystemBars
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.subscreen.homescreen.HomeContentGlobal
import clentlogic.cloy.crobotcontroller.presentation.ui.screen.subscreen.homescreen.HomeContentLocal
import clentlogic.cloy.crobotcontroller.presentation.ui.util.getScreenSize
import javax.annotation.meta.When


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomeScreen(
    viewModel: MainViewContract,
    blePermissionHandler: BlePermissionHandler,
    onOpenSettings: () -> Unit,
    onOpenDeviceLocal: (Map.Entry<String, BluetoothDevice>) -> Unit,
    onOpenDeviceGlobal: (Map.Entry<String, Boolean>) -> Unit
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
        onOpenDeviceLocal,
        onOpenDeviceGlobal
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
    onOpenDevice: (Map.Entry<String, BluetoothDevice>) -> Unit,
    onOpenDeviceGlobal: (Map.Entry<String, Boolean>) -> Unit,
) {

    val controlMode by viewModel.controlModeFlow.collectAsState(LOCAL_MODE)

    Column {

        if (controlMode == LOCAL_MODE){
            // Local Mode Content
            HomeContentLocal(
                viewModel,
                screenSize,
                scanningState,
                bluetoothState,
                blePermissionHandler,
                deviceName,
                connectionState,
                onDeviceNameChange,
                onOpenSettings,
                onOpenDevice
            )
        }else {
            // Global Mode Content
            HomeContentGlobal(
                viewModel,
                screenSize,
                onOpenSettings,
                onOpenDeviceGlobal
            )
        }





    }
}

