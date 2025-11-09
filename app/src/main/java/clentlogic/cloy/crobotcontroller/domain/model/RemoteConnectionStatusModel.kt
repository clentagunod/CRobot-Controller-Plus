package clentlogic.cloy.crobotcontroller.domain.model

sealed class RemoteConnectionStatusModel {
    object Online: RemoteConnectionStatusModel()
    object EstablishingConnection: RemoteConnectionStatusModel()
    object Offline: RemoteConnectionStatusModel()
    data class Error(val message: String): RemoteConnectionStatusModel()
}