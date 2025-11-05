package clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository

class SetControlMode(private val repository: DataStoreRepository) {
    suspend operator fun invoke(setMode: String) = repository.setControlMode(setMode)
}