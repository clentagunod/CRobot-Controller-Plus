package clentlogic.cloy.crobotcontroller.data.mapper

import clentlogic.cloy.crobotcontroller.data.local.roomdb.entity.RobotEntity
import clentlogic.cloy.crobotcontroller.domain.model.CmdModel


fun RobotEntity.toDomain(): CmdModel = CmdModel(id = id, cmd = cmd, value = value)
fun CmdModel.toEntity(): RobotEntity= RobotEntity(id = id, cmd = cmd, value = value)
