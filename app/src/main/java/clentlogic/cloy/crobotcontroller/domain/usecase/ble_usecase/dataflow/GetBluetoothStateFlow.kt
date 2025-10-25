package clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow

import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository


class GetBluetoothStateFlow(private val repository: BleRepository) {
    operator fun invoke() = repository.bluetoothState
}