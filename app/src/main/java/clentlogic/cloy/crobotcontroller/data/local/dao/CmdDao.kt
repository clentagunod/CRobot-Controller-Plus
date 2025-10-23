package clentlogic.cloy.crobotcontroller.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import clentlogic.cloy.crobotcontroller.data.local.entity.CmdEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CmdDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addCmd(cmd: CmdEntity)

    @Query("SELECT * FROM commands")
    fun getAllCmd(): Flow<List<CmdEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCmd(cmd: CmdEntity)

    @Delete
    suspend fun deleteCmd(cmd: CmdEntity)
}