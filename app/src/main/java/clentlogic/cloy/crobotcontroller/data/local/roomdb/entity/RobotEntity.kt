package clentlogic.cloy.crobotcontroller.data.local.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("robots")
data class RobotEntity(
    @PrimaryKey val id: Int,
    val cmd: String,
    val value: Int,
)
