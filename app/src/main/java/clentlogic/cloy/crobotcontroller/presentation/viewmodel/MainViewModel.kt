package clentlogic.cloy.crobotcontroller.presentation.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.ConnectRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.DisconnectRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.SendDataToRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.StartScan
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.StopScanning
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetBluetoothStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetConnectionStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetDeviceDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetScanningStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.AddCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.DeleteCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.GetAllCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetControlMode
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetPermissionState
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetToggleButtonState
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.dataflow.GetControlModeDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.dataflow.GetPermissionsDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.dataflow.GetToggleButtonControlDataFlow
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
    private val getDeviceDataFlow: GetDeviceDataFlow,
    private val getConnectionStateFlow: GetConnectionStateFlow,
    private val getBluetoothStateFlow: GetBluetoothStateFlow,
    private val getScanningStateFlow: GetScanningStateFlow,
    private val getPermissionsDataFlow: GetPermissionsDataFlow,
    private val setPermissionsOk: SetPermissionState,
    private val getToggleButtonControlDataFlow: GetToggleButtonControlDataFlow,
    private val setToggleButtonState: SetToggleButtonState,
    private val getControlModeDataFlow: GetControlModeDataFlow,
    private val setControlMode: SetControlMode,


    ) : ViewModel(), MainViewContract {


    private val _selectedDevice = MutableStateFlow<Map.Entry<String, BluetoothDevice>?>(null)
    override val selectedDevice: StateFlow<Map.Entry<String, BluetoothDevice>?> = _selectedDevice

    private val _cmd = MutableStateFlow<List<CmdModel>>(emptyList())
    override val cmd: StateFlow<List<CmdModel>> = _cmd

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    override val device = getDeviceDataFlow()
    override val connectionState = getConnectionStateFlow()
    override val bluetoothState = getBluetoothStateFlow()
    override val scanningState = getScanningStateFlow()

    override val permissionFlow = getPermissionsDataFlow()
    override val toggleControlButtonFlow = getToggleButtonControlDataFlow()
    override val controlModeFlow = getControlModeDataFlow()


    init {
        loadCmd()
        autoScan()
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

    override suspend fun startScanning(wait: Long) = startScan(wait)
    override fun stopScan() = stopScanning()
    override fun connectToDevice(device: BluetoothDevice) = connectBleDevice(device)
    override fun disconnectDevice() = disconnectBleDevice()
    override fun sendDataToBleDevice(data: ByteArray) = sendDataToBle(data)

    override fun setPermission(isPermitted: Boolean) {
        viewModelScope.launch {
            setPermissionsOk(isPermitted)
        }
    }

    override fun setToggleControlButtonState(isToggled: Boolean){
        viewModelScope.launch {
            setToggleButtonState(isToggled)
        }
    }

    override fun setControlModeState(setMode: String) {
        viewModelScope.launch {
            setControlMode(setMode)
        }
    }


}