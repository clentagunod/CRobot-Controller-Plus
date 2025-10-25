package clentlogic.cloy.crobotcontroller.domain.model

sealed class ScanningState {
    object Scanning: ScanningState()
    object ScanningFinished: ScanningState()
    data class ErrorScanning(val message: String): ScanningState()
}