package clentlogic.cloy.crobotcontroller.data.repository

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.LOCAL_MODE
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import clentlogic.cloy.crobotcontroller.data.communication.wifi.WifiHelper
import clentlogic.cloy.crobotcontroller.data.local.datastore.observeControlModeState
import clentlogic.cloy.crobotcontroller.data.remote.firebase.FirebaseRealtimeDbHelper
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.BluetoothState
import clentlogic.cloy.crobotcontroller.domain.model.RemoteConnectionStatusModel
import clentlogic.cloy.crobotcontroller.domain.model.ScanningState
import clentlogic.cloy.crobotcontroller.domain.model.WifiConnectionState
import clentlogic.cloy.crobotcontroller.domain.model.WifiState
import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class RobotControllerRepositoryImpl @Inject constructor(
    @ApplicationContext app: Context,
    private val bleHelper: BleHelper,
    private val wifiHelper: WifiHelper,
    private val firebaseHelper: FirebaseRealtimeDbHelper,
) : RobotControllerRepository {

    private val controlMode = app.observeControlModeState()
    private var currentMode = LOCAL_MODE

    // Local Mode
    private val _deviceDataFlow = MutableStateFlow<Map<String, BluetoothDevice>>(emptyMap())
    override val deviceDataFlow: MutableStateFlow<Map<String, BluetoothDevice>> = _deviceDataFlow

    private val _connectionState =
        MutableStateFlow<BleConnectionState>(BleConnectionState.Disconnected)
    override val connectionState: MutableStateFlow<BleConnectionState> = _connectionState

    private val _bluetoothState =
        MutableStateFlow(if (bleHelper.isBluetoothEnabled()) BluetoothState.BluetoothEnabled else BluetoothState.BluetoothDisabled)
    override val bluetoothState: MutableStateFlow<BluetoothState> = _bluetoothState

    private val _scanningState = MutableStateFlow<ScanningState>(ScanningState.ScanningFinished)
    override val scanningState: MutableStateFlow<ScanningState> = _scanningState

    // Global Mode
    private val _wifiState =
        MutableStateFlow(if (wifiHelper.isWifiEnabled()) WifiState.WifiOn else WifiState.WifiOff)
    override val wifiState: MutableStateFlow<WifiState> = _wifiState

    private val _wifiConnectionState =
        MutableStateFlow(if (wifiHelper.isWifiConnected()) WifiConnectionState.WifiConnected else WifiConnectionState.WifiDisconnected)
    override val wifiConnectionState: MutableStateFlow<WifiConnectionState> = _wifiConnectionState

    private val _remoteConnectionState = MutableStateFlow<RemoteConnectionStatusModel>(
        RemoteConnectionStatusModel.Offline
    )
    override val remoteConnectionState: MutableStateFlow<RemoteConnectionStatusModel> =
        _remoteConnectionState

    private val _wifiHasInternet = MutableStateFlow(wifiHelper.hasWifiInternet())
    override val wifiHasInternet: MutableStateFlow<Boolean> = _wifiHasInternet


    private val _deviceConnectionStateGlobal = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    override val deviceConnectionStateGlobal: MutableStateFlow<Map<String, Boolean>> =
        _deviceConnectionStateGlobal


    init {

        CoroutineScope(Dispatchers.IO).launch {
            controlMode.collect {
                currentMode = it
                if (currentMode == LOCAL_MODE) {
                    firebaseHelper.stopRealtimeListener()
                } else {
                    bleHelper.disconnectBle()
                    firebaseHelper.realtimeListener()
                    firebaseHelper.signIn()
                }
                Log.d("BleImpl", "Current Mode: $it, CurrentModeFromController: $currentMode")
            }
        }


        wifiHelper.onWifiHasInternet = {
            _wifiHasInternet.value = true
        }

        wifiHelper.onWifiNoInternet = {
            _wifiHasInternet.value = false
        }

        wifiHelper.onWifiConnected = {
            _wifiConnectionState.value = WifiConnectionState.WifiConnected
        }

        wifiHelper.onWifiDisconnected = {
            _wifiConnectionState.value = WifiConnectionState.WifiDisconnected
        }

        wifiHelper.onWifiStateEnabled = {
            _wifiState.value = WifiState.WifiOn
        }


        wifiHelper.onWifiStateDisabled = {
            _wifiState.value = WifiState.WifiOff
        }


        firebaseHelper.onDeviceConnectionState = {
            _deviceConnectionStateGlobal.value = it

        }


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


    override fun startScan(wait: Long) = bleHelper.startScanBle(wait)
    override fun stopScanning() = bleHelper.stopScanBle()
    override fun connectRobot(device: BluetoothDevice) = bleHelper.connectBle(device)
    override fun disconnectRobot() = bleHelper.disconnectBle()


    override fun sendDataToRobot(data: ByteArray) {
        if (currentMode == LOCAL_MODE) {
            bleHelper.sendDataBle(data)
        } else {
            val dataInt = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).int
            firebaseHelper.sendDataFirebase(dataInt)
        }

    }


}