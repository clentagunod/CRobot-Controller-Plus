package clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase

import androidx.datastore.preferences.core.Preferences
import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository

class SetToggleControls(private val repository: DataStoreRepository) {
    suspend operator fun invoke(isToggled: Boolean, key: Preferences.Key<Boolean>) = repository.setToggleControls(isToggled, key)
}