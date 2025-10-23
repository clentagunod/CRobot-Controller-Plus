package clentlogic.cloy.crobotcontroller.domain.model

sealed class BleConnectionState {
    object Connected: BleConnectionState()
    object Disconnected: BleConnectionState()
    object Scanning: BleConnectionState()
    object TimeOut: BleConnectionState()
    data class Error(val message: String): BleConnectionState()
}