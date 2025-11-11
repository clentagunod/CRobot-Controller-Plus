package clentlogic.cloy.crobotcontroller.presentation.fakes.models

import android.bluetooth.BluetoothDevice
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.LOCAL_MODE
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.domain.model.WifiConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.WifiState
import clentlogic.cloy.crobotcontroller.domain.model.firebase.RobotModel
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class FakeViewModel: MainViewContract {
    override val cmd: StateFlow<List<CmdModel>> = MutableStateFlow(emptyList())
    override val selectedDeviceGlobal: StateFlow<String> = MutableStateFlow("")
    override val connectionState: StateFlow<BleConnectionState> = MutableStateFlow(BleConnectionState.Connected)
    override val device: StateFlow<Map<String, BluetoothDevice>> = MutableStateFlow(emptyMap())
    override val bluetoothState: StateFlow<BluetoothState> = MutableStateFlow(BluetoothState.BluetoothDisabled)
    override val scanningState: StateFlow<ScanningState> = MutableStateFlow(ScanningState.Scanning)
    override val selectedDeviceLocal: StateFlow<Map.Entry<String, BluetoothDevice>?> = MutableStateFlow(null)
    override val permissionFlow: Flow<Boolean> = flowOf(false)
    override val toggleControlButtonFlow: Flow<Boolean> = flowOf(false)
    override val controlModeFlow: Flow<String> = flowOf(LOCAL_MODE)
    override val robotModel: MutableStateFlow<List<RobotModel>> = MutableStateFlow(emptyList())
    override val wifiState: MutableStateFlow<WifiState> = MutableStateFlow(WifiState.WifiOff)
    override val wifiConnectionState: MutableStateFlow<WifiConnectionState> = MutableStateFlow(
        WifiConnectionState.WifiDisconnected)
    override val wifiHasInternetConnection: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override suspend fun startScanning(wait: Long) = Unit
    override fun stopScan() = Unit
    override fun connectToDevice(device: BluetoothDevice) = Unit
    override fun disconnectDevice() = Unit
    override fun sendDataToBleDevice(data: ByteArray) =  println("Data sent successfully: $data! (Fake)")
    override fun updateRobotData(data: Boolean) = Unit

    override fun addCommand(cmdModel: CmdModel) = Unit
    override fun deleteCommand(cmdModel: CmdModel) = Unit
    override fun selectBleDevice(device: Map.Entry<String, BluetoothDevice>) = Unit
    override fun selectDevice(device: String) = Unit

    override fun setPermission(isPermitted: Boolean) = Unit
    override fun setToggleControlButtonState(isPermitted: Boolean) = Unit
    override fun setControlModeState(setMode: String) = Unit

}