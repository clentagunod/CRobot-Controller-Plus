package clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase

import android.bluetooth.BluetoothDevice
import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository

class ConnectBleDevice(private val repository: BleRepository) {
    operator fun invoke(device: BluetoothDevice) = repository.connectBleDevice(device)
}