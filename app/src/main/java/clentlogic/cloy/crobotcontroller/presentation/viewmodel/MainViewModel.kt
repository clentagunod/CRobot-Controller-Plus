package clentlogic.cloy.crobotcontroller.presentation.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.ConnectBleDevice
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.DisconnectBleDevice
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.SendDataToBle
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.StartScan
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.StopScanning
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow.GetBluetoothStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow.GetConnectionStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow.GetDeviceDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow.GetScanningStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.AddCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.DeleteCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.GetAllCmd
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCmd: GetAllCmd,
    private val addCmd: AddCmd,
    private val deleteCmd: DeleteCmd,
    private val startScan: StartScan,
    private val stopScanning: StopScanning,
    private val connectBleDevice: ConnectBleDevice,
    private val disconnectBleDevice: DisconnectBleDevice,
    private val sendDataToBle: SendDataToBle,
    private val getDeviceDataFlow: GetDeviceDataFlow,
    private val getConnectionStateFlow: GetConnectionStateFlow,
    private val getBluetoothStateFlow: GetBluetoothStateFlow,
    private val getScanningStateFlow: GetScanningStateFlow


) : ViewModel(), MainViewContract {


    private val _cmd = MutableStateFlow<List<CmdModel>>(emptyList())
    override val cmd: StateFlow<List<CmdModel>> = _cmd

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    override val device = getDeviceDataFlow()
    override val connectionState = getConnectionStateFlow()
    override val bluetoothState = getBluetoothStateFlow()
    override val scanningState = getScanningStateFlow()

    init {
        loadCmd()
        autoScan()
    }



    private fun autoScan(){
        viewModelScope.launch {
            while (true){
                startScan(3000L)
                delay(30_000)
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


    override suspend fun startScanning(wait: Long) = startScan(wait)
    override fun stopScan() = stopScanning()
    override fun connectToDevice(device: BluetoothDevice) = connectBleDevice(device)
    override fun disconnectDevice() = disconnectBleDevice()
    override fun sendDataToBleDevice(data: String) = sendDataToBle(data)


}