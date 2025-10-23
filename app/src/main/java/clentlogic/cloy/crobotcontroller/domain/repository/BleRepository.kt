package clentlogic.cloy.crobotcontroller.domain.repository

import android.bluetooth.BluetoothDevice
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

interface BleRepository {


    val deviceDataFlow: MutableStateFlow<Map<String, BluetoothDevice>>
    val connectionState: MutableStateFlow<BleConnectionState>
    val bluetoothState: SharedFlow<BluetoothState>

    fun startScan(wait: Long)
    fun connectBleDevice(device: BluetoothDevice)
    fun disconnectBleDevice()
    fun sendDataToBle(data: String)


}