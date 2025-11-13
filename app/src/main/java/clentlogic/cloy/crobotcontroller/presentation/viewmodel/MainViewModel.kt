package clentlogic.cloy.crobotcontroller.presentation.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.AddCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.DeleteCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.GetAllCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.dataflow.GetControlModeDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.dataflow.GetPermissionsDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.dataflow.GetToggleButtonControlsFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetControlMode
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetLoginCredential
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetPermissionState
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetToggleControls
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SignOutFb
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.dataflow.GetLoginCredential
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.dataflow.GetLoginStatus
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.ConnectRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.DisconnectRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.SendDataToRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.StartScan
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.StopScanning
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetBluetoothStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetConnectionStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetRobotModel
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetDeviceDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetScanningStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetWifiConnectionStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetWifiHasInternet
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetWifiStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.UpdateData
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCmd: GetAllCmd,
    private val addCmd: AddCmd,
    private val deleteCmd: DeleteCmd,
    private val startScan: StartScan,
    private val stopScanning: StopScanning,
    private val connectBleDevice: ConnectRobot,
    private val disconnectBleDevice: DisconnectRobot,
    private val sendDataToBle: SendDataToRobot,
    private val updateData: UpdateData,
    private val getDeviceDataFlow: GetDeviceDataFlow,
    private val getConnectionStateFlow: GetConnectionStateFlow,
    private val getBluetoothStateFlow: GetBluetoothStateFlow,
    private val getScanningStateFlow: GetScanningStateFlow,
    private val getPermissionsDataFlow: GetPermissionsDataFlow,
    private val setPermissionsOk: SetPermissionState,
    private val getToggleButtonControlDataFlow: GetToggleButtonControlsFlow,
    private val setToggleButtonState: SetToggleControls,
    private val getControlModeDataFlow: GetControlModeDataFlow,
    private val setControlMode: SetControlMode,
    private val getRobotModel: GetRobotModel,
    private val getWifiStateFlow: GetWifiStateFlow,
    private val getWifiConnectionStateFlow: GetWifiConnectionStateFlow,
    private val getWifiHasInternet: GetWifiHasInternet,
    private val getEmailAndPassword: GetLoginCredential,
    private val setEmailAndPassword: SetLoginCredential,
    private val getLoginStatus: GetLoginStatus,
    private val signOutFb: SignOutFb,



    ) : ViewModel(), MainViewContract {


    private val _selectedDevice = MutableStateFlow<Map.Entry<String, BluetoothDevice>?>(null)
    override val selectedDeviceLocal: StateFlow<Map.Entry<String, BluetoothDevice>?> = _selectedDevice

    private val _selectedDeviceGlobal = MutableStateFlow("")
    override val selectedDeviceGlobal: StateFlow<String> = _selectedDeviceGlobal

    private val _cmd = MutableStateFlow<List<CmdModel>>(emptyList())
    override val cmd: StateFlow<List<CmdModel>> = _cmd

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    override val device = getDeviceDataFlow()
    override val connectionState = getConnectionStateFlow()
    override val bluetoothState = getBluetoothStateFlow()
    override val scanningState = getScanningStateFlow()

    override val permissionFlow = getPermissionsDataFlow()
    override val toggleControls = getToggleButtonControlDataFlow()
    override val controlModeFlow = getControlModeDataFlow()
    override val loginCredential = getEmailAndPassword()
    override val loginStatus = getLoginStatus()

    override val robotModel = getRobotModel()
    override val wifiState = getWifiStateFlow()
    override val wifiConnectionState = getWifiConnectionStateFlow()
    override val wifiHasInternetConnection = getWifiHasInternet()


    var bluetoothIsOn = false


    init {
        loadCmd()
        bluetoothStatus()
        if (bluetoothIsOn){
            autoScan()
        }

    }


    private fun autoScan(){
        viewModelScope.launch {
            while (true){
                startScan(3000L)
                val randomDelay = Random.nextLong(10_000L, 60_000L)
                delay(randomDelay)
            }
        }
    }

    private fun bluetoothStatus(){
        viewModelScope.launch {
            bluetoothState.collect { bluetoothState ->
                bluetoothIsOn = bluetoothState == BluetoothState.BluetoothEnabled
            }

        }
    }


    private fun loadCmd() {
        viewModelScope.launch {
            getAllCmd().flowOn(Dispatchers.IO).collect { cmdList ->
                _cmd.value = cmdList
            }
        }
    }


    override fun addCommand(cmdModel: CmdModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addCmd(cmdModel)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    override fun deleteCommand(cmdModel: CmdModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCmd(cmdModel)
        }
    }

    override fun selectBleDevice(device: Map.Entry<String, BluetoothDevice>) {
        _selectedDevice.value = device
    }

    override fun selectDevice(device: String) {
        _selectedDeviceGlobal.value = device
    }

    override suspend fun startScanning(wait: Long) = startScan(wait)
    override fun stopScan() = stopScanning()
    override fun connectToDevice(device: BluetoothDevice) = connectBleDevice(device)
    override fun disconnectDevice() = disconnectBleDevice()
    override fun sendDataToRobot(data: ByteArray) = sendDataToBle(data)
    override fun updateRobotData(data: Boolean) = updateData(data)

    override fun setPermission(isPermitted: Boolean) {
        viewModelScope.launch {
            setPermissionsOk(isPermitted)
        }
    }

    override fun setToggleControls(isToggled: Boolean, key: Preferences.Key<Boolean>) {
        viewModelScope.launch {
            setToggleButtonState(isToggled, key)
        }
    }

    override fun setControlModeState(setMode: String) {
        viewModelScope.launch {
            setControlMode(setMode)
        }
    }

    override fun setLoginCredential(username: String, email: String, password: String) =
        setEmailAndPassword(username, email, password)


    override fun signOut() = signOutFb()

}