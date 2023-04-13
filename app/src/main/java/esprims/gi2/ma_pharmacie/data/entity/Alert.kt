package esprims.gi2.ma_pharmacie.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
@Entity(tableName = "alert")
data class Alert(
    @PrimaryKey
    val timestamp: Long
)
