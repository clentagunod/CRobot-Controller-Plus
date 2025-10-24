package clentlogic.cloy.crobotcontroller.presentation.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.ConnectBleDevice
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.DisconnectBleDevice
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.SendDataToBle
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.StartScan
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.callback.GetBluetoothStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.callback.GetConnectionStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.callback.GetDeviceDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.AddCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.DeleteCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.GetAllCmd
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
    private val connectBleDevice: ConnectBleDevice,
    private val disconnectBleDevice: DisconnectBleDevice,
    private val sendDataToBle: SendDataToBle,
    private val getDeviceDataFlow: GetDeviceDataFlow,
    private val getConnectionStateFlow: GetConnectionStateFlow,
    private val getBluetoothStateFlow: GetBluetoothStateFlow



): ViewModel(), MainViewContract{



    private val _cmd = MutableStateFlow<List<CmdModel>>(emptyList())
    override val cmd: StateFlow<List<CmdModel>> = _cmd

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    override val device = getDeviceDataFlow()
    override val connectionState = getConnectionStateFlow()
    override val bluetoothState = getBluetoothStateFlow()

    init {
        loadCmd()


    }


    private fun loadCmd(){
        viewModelScope.launch {
            getAllCmd().flowOn(Dispatchers.IO).collect { cmdList ->
                _cmd.value = cmdList
            }
        }
    }


    override fun addCommand(cmdModel: CmdModel){
        viewModelScope.launch(Dispatchers.IO){
            try {
                addCmd(cmdModel)
            }catch (e: Exception){
                _errorMessage.value = e.message
            }
        }
    }

    override fun deleteCommand(cmdModel: CmdModel){
        viewModelScope.launch(Dispatchers.IO) {
            deleteCmd(cmdModel)
        }
    }


    override fun startScanning(wait: Long) = startScan(wait)
    override fun connectToDevice(device: BluetoothDevice) = connectBleDevice(device)
    override fun disconnectDevice() = disconnectBleDevice()
    override fun sendDataToBleDevice(data: String) = sendDataToBle(data)






}