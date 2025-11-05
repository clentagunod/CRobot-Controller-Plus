package clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository

class SendDataToRobot(private val repository: RobotControllerRepository) {
    operator fun invoke(data: ByteArray) = repository.sendDataToRobot(data)
}