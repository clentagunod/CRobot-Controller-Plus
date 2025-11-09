package clentlogic.cloy.crobotcontroller.data.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import clentlogic.cloy.crobotcontroller.data.local.roomdb.entity.RobotEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RobotDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addRobot(cmd: RobotEntity)

    @Query("SELECT * FROM robots")
    fun getAllRobots(): Flow<List<RobotEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRobot(cmd: RobotEntity)

    @Delete
    suspend fun deleteRobot(cmd: RobotEntity)
}