package clentlogic.cloy.crobotcontroller.presentation.contracts

import android.bluetooth.BluetoothDevice
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MainViewContract {

    val cmd: StateFlow<List<CmdModel>>
    val connectionState: StateFlow<BleConnectionState>
    val device: StateFlow<Map<String, BluetoothDevice>>
    val bluetoothState: StateFlow<BluetoothState>
    val scanningState: StateFlow<ScanningState>

    suspend fun startScanning(wait: Long)
    fun stopScan()
    fun connectToDevice(device: BluetoothDevice)
    fun disconnectDevice()

    fun sendDataToBleDevice(data: String)
    fun addCommand(cmdModel: CmdModel)
    fun deleteCommand(cmdModel: CmdModel)


}