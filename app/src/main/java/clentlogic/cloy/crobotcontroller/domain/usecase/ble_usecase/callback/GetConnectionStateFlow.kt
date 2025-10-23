package clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.callback

import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository

class GetConnectionStateFlow(private val repository: BleRepository) {
    operator fun invoke() = repository.connectionState
}