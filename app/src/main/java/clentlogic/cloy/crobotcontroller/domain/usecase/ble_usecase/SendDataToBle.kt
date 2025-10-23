package clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository

class SendDataToBle(private val repository: BleRepository) {
    operator fun invoke(data: String) = repository.sendDataToBle(data)
}