package clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository

class SignOutFb(private val repository: DataStoreRepository) {
    operator fun invoke() = repository.signOutFb()
}