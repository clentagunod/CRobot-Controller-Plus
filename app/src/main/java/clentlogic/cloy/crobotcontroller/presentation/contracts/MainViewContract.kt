package clentlogic.cloy.crobotcontroller.presentation.contracts

import android.bluetooth.BluetoothDevice
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.domain.model.WifiConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.WifiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MainViewContract {

    val selectedDeviceLocal: StateFlow<Map.Entry<String, BluetoothDevice>?>
    val selectedDeviceGlobal: StateFlow<Map.Entry<String, Boolean>?>
    val cmd: StateFlow<List<CmdModel>>
    val connectionState: StateFlow<BleConnectionState>
    val device: StateFlow<Map<String, BluetoothDevice>>
    val bluetoothState: StateFlow<BluetoothState>
    val scanningState: StateFlow<ScanningState>
    val permissionFlow: Flow<Boolean>
    val toggleControlButtonFlow: Flow<Boolean>
    val controlModeFlow: Flow<String>

    val deviceConnectionStateGlobal: MutableStateFlow<Map<String, Boolean>>

    val wifiState: MutableStateFlow<WifiState>
    val wifiConnectionState: MutableStateFlow<WifiConnectionState>
    val wifiHasInternetConnection: MutableStateFlow<Boolean>

    suspend fun startScanning(wait: Long)
    fun stopScan()
    fun connectToDevice(device: BluetoothDevice)
    fun disconnectDevice()

    fun sendDataToBleDevice(data: ByteArray)
    fun addCommand(cmdModel: CmdModel)
    fun deleteCommand(cmdModel: CmdModel)

    fun selectBleDevice(device: Map.Entry<String, BluetoothDevice>)
    fun selectDevice(device: Map.Entry<String, Boolean>)

    fun setPermission(isPermitted: Boolean)
    fun setToggleControlButtonState(isToggled: Boolean)
    fun setControlModeState(setMode: String)



}