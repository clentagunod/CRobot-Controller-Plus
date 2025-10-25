package clentlogic.cloy.crobotcontroller.data.repository

import android.bluetooth.BluetoothDevice
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class BleRepositoryImpl @Inject constructor(
    private val bleHelper: BleHelper,
): BleRepository {

    private val _deviceDataFlow = MutableStateFlow<Map<String, BluetoothDevice>>(emptyMap())
    override val deviceDataFlow: MutableStateFlow<Map<String, BluetoothDevice>> = _deviceDataFlow

    private val _connectionState = MutableStateFlow<BleConnectionState>(BleConnectionState.Disconnected)
    override val connectionState: MutableStateFlow<BleConnectionState> = _connectionState

    private val _bluetoothState = MutableStateFlow(if (bleHelper.isBluetoothEnabled()) BluetoothState.BluetoothEnabled else BluetoothState.BluetoothDisabled )
    override val bluetoothState: MutableStateFlow<BluetoothState> = _bluetoothState

    private val _scanningState = MutableStateFlow<ScanningState>(ScanningState.ScanningFinished)
    override val scanningState: MutableStateFlow<ScanningState> = _scanningState



    init {

        bleHelper.onDeviceFound = {
            _deviceDataFlow.value = it
        }

        bleHelper.onConnecting = {
            _connectionState.value = BleConnectionState.Connecting
        }

        // BLe Connection Status
        bleHelper.onConnected = {
            _connectionState.value = BleConnectionState.Connected
        }

        bleHelper.onDisconnected = {
            _connectionState.value = BleConnectionState.Disconnected
        }


        // Scanning State
        bleHelper.onStoppedScanning = {
            _scanningState.value = ScanningState.ScanningFinished
        }

        bleHelper.onScanning = {
            _scanningState.value = ScanningState.Scanning
        }


        //Bluetooth Status

        bleHelper.onBluetoothEnabled = {
            _bluetoothState.value = BluetoothState.BluetoothEnabled

        }

        bleHelper.onBluetoothDisabled = {
            _bluetoothState.value = BluetoothState.BluetoothDisabled
        }

    }



    override fun startScan(wait: Long) = bleHelper.startScan(wait)
    override fun stopScanning() = bleHelper.stopScan()
    override fun connectBleDevice(device: BluetoothDevice) = bleHelper.connect(device)
    override fun disconnectBleDevice() = bleHelper.disconnect()
    override fun sendDataToBle(data: String) = bleHelper.sendData(data)

}