package clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository

class StopScanning(private val repository: BleRepository) {
    operator fun invoke() = repository.stopScanning()
}