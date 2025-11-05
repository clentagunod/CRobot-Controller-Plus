package clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase

import android.bluetooth.BluetoothDevice
import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository

class ConnectRobot(private val repository: RobotControllerRepository) {
    operator fun invoke(device: BluetoothDevice) = repository.connectRobot(device)
}