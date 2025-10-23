package clentlogic.cloy.crobotcontroller.domain.model

sealed class BluetoothState {
    object BluetoothEnabled: BluetoothState()
    object BluetoothDisabled: BluetoothState()
}