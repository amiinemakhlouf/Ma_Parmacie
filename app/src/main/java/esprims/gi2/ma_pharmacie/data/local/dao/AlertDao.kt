package esprims.gi2.ma_pharmacie.data.local.dao

import androidx.room.*
import esprims.gi2.ma_pharmacie.data.local.entity.Alert
import esprims.gi2.ma_pharmacie.data.local.relations.AlertWithMedicineSchedules

@Dao
interface AlertDao {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(alert: Alert): Int

    @Delete
    fun delete(alert: Alert): Int

    @Query("select * from alert")
    fun getAll():List<AlertWithMedicineSchedules>
}