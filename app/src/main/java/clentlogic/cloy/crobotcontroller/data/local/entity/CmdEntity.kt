package clentlogic.cloy.crobotcontroller.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("commands")
data class CmdEntity(
    @PrimaryKey val id: Int,
    val cmd: String,
    val value: Int,
)
