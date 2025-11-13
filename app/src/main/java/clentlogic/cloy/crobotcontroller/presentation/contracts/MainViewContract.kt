package clentlogic.cloy.crobotcontroller.presentation.contracts

import android.bluetooth.BluetoothDevice
import androidx.datastore.preferences.core.Preferences
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.model.LoginCredential
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.domain.model.ToggleControls
import clentlogic.cloy.crobotcontroller.domain.model.WifiConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.WifiState
import clentlogic.cloy.crobotcontroller.domain.model.firebase.RobotModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MainViewContract {

    val selectedDeviceLocal: StateFlow<Map.Entry<String, BluetoothDevice>?>
    val selectedDeviceGlobal: StateFlow<String>


    val cmd: StateFlow<List<CmdModel>>
    val connectionState: StateFlow<BleConnectionState>
    val device: StateFlow<Map<String, BluetoothDevice>>
    val bluetoothState: StateFlow<BluetoothState>
    val scanningState: StateFlow<ScanningState>


    val permissionFlow: Flow<Boolean>
    val toggleControls: Flow<ToggleControls>
    val controlModeFlow: Flow<String>
    val loginCredential: Flow<LoginCredential>
    val loginStatus: Flow<Boolean>


    val robotModel: MutableStateFlow<List<RobotModel>>
    val wifiState: MutableStateFlow<WifiState>
    val wifiConnectionState: MutableStateFlow<WifiConnectionState>
    val wifiHasInternetConnection: MutableStateFlow<Boolean>


    suspend fun startScanning(wait: Long)
    fun stopScan()
    fun connectToDevice(device: BluetoothDevice)
    fun disconnectDevice()
    fun sendDataToRobot(data: ByteArray)
    fun updateRobotData(data: Boolean)


    fun addCommand(cmdModel: CmdModel)
    fun deleteCommand(cmdModel: CmdModel)


    fun selectBleDevice(device: Map.Entry<String, BluetoothDevice>)
    fun selectDevice(device: String)


    fun setPermission(isPermitted: Boolean)
    fun setToggleControls(isToggled: Boolean, key: Preferences.Key<Boolean>)
    fun setControlModeState(setMode: String)
    fun setLoginCredential(username: String, email: String, password: String)
    fun signOut()



}