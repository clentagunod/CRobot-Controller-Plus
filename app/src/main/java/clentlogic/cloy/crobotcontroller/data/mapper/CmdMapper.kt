package clentlogic.cloy.crobotcontroller.data.mapper

import clentlogic.cloy.crobotcontroller.data.local.entity.CmdEntity
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel


fun CmdEntity.toDomain(): CmdModel = CmdModel(id = id, cmd = cmd, value = value)
fun CmdModel.toEntity(): CmdEntity= CmdEntity(id = id, cmd = cmd, value = value)
