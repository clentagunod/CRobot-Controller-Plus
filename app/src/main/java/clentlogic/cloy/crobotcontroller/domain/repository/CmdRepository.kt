package clentlogic.cloy.crobotcontroller.domain.repository

import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import kotlinx.coroutines.flow.Flow


interface CmdRepository {

    suspend fun addCmd(cmdModel: CmdModel)

    fun getAllCmd(): Flow<List<CmdModel>>

    suspend fun updateCmd(cmdModel: CmdModel)

    suspend fun deleteCmd(cmdModel: CmdModel)


}