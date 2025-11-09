package clentlogic.cloy.crobotcontroller.domain.model

sealed class WifiState {
    object WifiOn: WifiState()
    object WifiOff: WifiState()
}