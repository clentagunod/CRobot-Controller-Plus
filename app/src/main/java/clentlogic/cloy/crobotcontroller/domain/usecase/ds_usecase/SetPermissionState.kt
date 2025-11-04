package clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository

class SetPermissionState(val repository: DataStoreRepository) {
    suspend operator fun invoke(isPermitted: Boolean) = repository.setPermissionState(isPermitted)
}