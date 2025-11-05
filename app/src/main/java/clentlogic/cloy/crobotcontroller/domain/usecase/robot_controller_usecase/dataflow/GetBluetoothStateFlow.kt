package clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow

import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository


class GetBluetoothStateFlow(private val repository: RobotControllerRepository) {
    operator fun invoke() = repository.bluetoothState
}