package clentlogic.cloy.crobotcontroller.data.local.roomdb.db

import androidx.room.Database
import androidx.room.RoomDatabase
import clentlogic.cloy.crobotcontroller.data.local.roomdb.dao.RobotDao
import clentlogic.cloy.crobotcontroller.data.local.roomdb.entity.RobotEntity

@Database([RobotEntity::class], version = 1, exportSchema = false)
abstract class CmdDatabase: RoomDatabase() {
    abstract fun cmdDao(): RobotDao
}