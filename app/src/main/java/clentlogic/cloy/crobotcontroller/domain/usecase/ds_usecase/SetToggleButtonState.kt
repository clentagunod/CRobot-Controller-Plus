package clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository

class SetToggleButtonState(private val repository: DataStoreRepository) {
    suspend operator fun invoke(isToggled: Boolean) = repository.setToggleControlState(isToggled)
}