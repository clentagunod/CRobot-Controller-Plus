package clentlogic.cloy.crobotcontroller.domain.model

import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository

sealed class WifiConnectionState {
    object WifiConnected: WifiConnectionState()
    object WifiDisconnected: WifiConnectionState()
}