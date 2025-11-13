package clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository

class SetLoginCredential(private val repository: DataStoreRepository) {
    operator fun invoke(username: String, email: String, password: String) =
        repository.setLoginCredential(username, email, password)
}