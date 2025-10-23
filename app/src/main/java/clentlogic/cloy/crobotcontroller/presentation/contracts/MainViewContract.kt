package clentlogic.cloy.crobotcontroller.presentation.contracts

import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.State
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MainViewContract {

    val cmd: StateFlow<List<CmdModel>>
    val connectionState: StateFlow<BleConnectionState>
    val device: StateFlow<Map<String, BluetoothDevice>>

    fun startScanning(wait: Long)
    fun connectToDevice(device: BluetoothDevice)
    fun disconnectDevice()

    fun sendDataToBleDevice(data: String)
    fun addCommand(cmdModel: CmdModel)
    fun deleteCommand(cmdModel: CmdModel)




}