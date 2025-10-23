package clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository

class StartScan(private val repository: BleRepository) {
    operator fun invoke(wait: Long) = repository.startScan(wait)
}