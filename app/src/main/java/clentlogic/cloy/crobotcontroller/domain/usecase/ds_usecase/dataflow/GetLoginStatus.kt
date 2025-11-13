package clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.dataflow

import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository

class GetLoginStatus(private val repository: DataStoreRepository) {
    operator fun invoke() = repository.loginStatus
}