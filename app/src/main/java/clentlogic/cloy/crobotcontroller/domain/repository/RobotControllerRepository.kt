package clentlogic.cloy.crobotcontroller.domain.repository

import android.bluetooth.BluetoothDevice
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import kotlinx.coroutines.flow.MutableStateFlow

interface RobotControllerRepository {


    val deviceDataFlow: MutableStateFlow<Map<String, BluetoothDevice>>
    val connectionState: MutableStateFlow<BleConnectionState>
    val bluetoothState: MutableStateFlow<BluetoothState>
    val scanningState: MutableStateFlow<ScanningState>

    fun startScan(wait: Long)
    fun stopScanning()
    fun connectRobot(device: BluetoothDevice)
    fun disconnectRobot()
    fun sendDataToRobot(data: ByteArray)




}