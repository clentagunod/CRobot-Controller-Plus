package clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow

import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository

class UpdateData(private val repository: RobotControllerRepository) {
    operator fun invoke(data: Boolean) = repository.updateData(data)
}