package esprims.gi2.ma_pharmacie.data.local.dao

import androidx.room.*
import esprims.gi2.ma_pharmacie.data.local.entity.MedicineSchedule

@Dao
interface ScheduleDao {


    @Insert
    fun insert(schedule: MedicineSchedule):Long

    @Insert
    fun insertAll(schedule: List<MedicineSchedule>):List<Long>

    @Update
    fun update(schedule: MedicineSchedule):Int

    @Update
    fun updateAll(schedule: List<MedicineSchedule>):Int

    @Delete
    fun delete(schedule: MedicineSchedule):Int

    @Delete
    fun deleteAll(schedules: List<MedicineSchedule>):Int



}