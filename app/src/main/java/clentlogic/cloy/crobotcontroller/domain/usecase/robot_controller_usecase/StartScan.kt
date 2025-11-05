package clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository

class StartScan(private val repository: RobotControllerRepository) {
    operator fun invoke(wait: Long) = repository.startScan(wait)
}