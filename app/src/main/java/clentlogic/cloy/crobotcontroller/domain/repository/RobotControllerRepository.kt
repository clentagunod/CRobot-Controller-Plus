package clentlogic.cloy.crobotcontroller.domain.repository

import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.collection.MutableVector
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.RemoteConnectionStatusModel
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.domain.model.WifiConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.WifiState
import clentlogic.cloy.crobotcontroller.domain.model.firebase.RobotModel
import kotlinx.coroutines.flow.MutableStateFlow

interface RobotControllerRepository {


    val deviceDataFlow: MutableStateFlow<Map<String, BluetoothDevice>>
    val connectionState: MutableStateFlow<BleConnectionState>
    val bluetoothState: MutableStateFlow<BluetoothState>
    val scanningState: MutableStateFlow<ScanningState>

    val wifiState: MutableStateFlow<WifiState>
    val wifiConnectionState: MutableStateFlow<WifiConnectionState>
    val wifiHasInternet: MutableStateFlow<Boolean>
    val remoteConnectionState: MutableStateFlow<RemoteConnectionStatusModel>
    val robotModel: MutableStateFlow<List<RobotModel>>


    fun startScan(wait: Long)
    fun stopScanning()
    fun connectRobot(device: BluetoothDevice)
    fun disconnectRobot()
    fun sendDataToRobot(data: ByteArray)
    fun updateData(data: Boolean)




}