package clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.dataflow

import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository

class GetToggleButtonControlDataFlow(private val repository: DataStoreRepository) {
    operator fun invoke() = repository.toggleControlButtonState
}