package clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase

import clentlogic.cloy.crobotcontroller.domain.repository.CmdRepository

class GetAllCmd(private val repository: CmdRepository) {
    operator fun invoke() = repository.getAllCmd()
}