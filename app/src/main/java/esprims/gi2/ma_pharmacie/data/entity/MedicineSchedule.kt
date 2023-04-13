package esprims.gi2.ma_pharmacie.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
@Entity(tableName = "medicine_schedule")
data class MedicineSchedule(
    @PrimaryKey(autoGenerate = true)
    val id:Int=1,
    @ColumnInfo(name = "medicine_id")
    val medicineId:Int,
    @ColumnInfo(name = "alert_timestamp")
    val alertTimestamp:Long,
    @ColumnInfo(name ="first_day")
    val firstDay:Long,
    @ColumnInfo(name ="last_day")
    val lastDay:Long,
    val times:Long,
    val dose:Int,
    val patient:String


  )
