package clentlogic.cloy.crobotcontroller.domain.model

sealed class BleConnectionState {
    object Connecting: BleConnectionState()
    object Connected: BleConnectionState()
    object Disconnected: BleConnectionState()
    data class Error(val message: String): BleConnectionState()
}