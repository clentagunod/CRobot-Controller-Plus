package clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase

import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.repository.CmdRepository

class AddCmd(private val repository: CmdRepository) {
    suspend operator fun invoke(cmdModel: CmdModel) = repository.addCmd(cmdModel)
}