package clentlogic.cloy.crobotcontroller.data.repository

import android.app.Application
import android.util.Log
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.data.local.dao.CmdDao
import clentlogic.cloy.crobotcontroller.data.mapper.toDomain
import clentlogic.cloy.crobotcontroller.data.mapper.toEntity
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel
import clentlogic.cloy.crobotcontroller.domain.repository.CmdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CmdRepositoryImpl @Inject constructor(
    private val dao: CmdDao,
    private val appContext: Application

): CmdRepository {

    init {
        val appContextName = appContext.getString(R.string.app_name)
        Log.d("DEBUG", "This is from $appContextName")
    }


    override suspend fun addCmd(cmdModel: CmdModel) {
        dao.addCmd(cmdModel.toEntity())
    }

    override fun getAllCmd(): Flow<List<CmdModel>> {
        return dao.getAllCmd().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateCmd(cmdModel: CmdModel) {
        dao.updateCmd(cmdModel.toEntity())
    }

    override suspend fun deleteCmd(cmdModel: CmdModel) {
        dao.deleteCmd(cmdModel.toEntity())
    }
}