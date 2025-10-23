package clentlogic.cloy.crobotcontroller.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import clentlogic.cloy.crobotcontroller.data.local.dao.CmdDao
import clentlogic.cloy.crobotcontroller.data.local.entity.CmdEntity

@Database([CmdEntity::class], version = 1, exportSchema = false)
abstract class CmdDatabase: RoomDatabase() {
    abstract fun cmdDao(): CmdDao
}