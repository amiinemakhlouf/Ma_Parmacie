package esprims.gi2.ma_pharmacie.data.local.dao

import androidx.room.*
import esprims.gi2.ma_pharmacie.data.local.entity.Medicine
import esprims.gi2.ma_pharmacie.data.local.relations.MedicineWithSchedules
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {

    @Insert
     suspend  fun insert(medicine: Medicine): Long

    @Insert
     fun insertAll(medicines: List<Medicine>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
     fun update(medicine: Medicine): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
     fun updateAll(vararg medicine: Medicine): Int

    @Query("select * from medicine")
     fun getAll():   Flow<List<Medicine>>

    @Query("select *  from medicine   ")
     fun getMedicineWithSchedule(): List<MedicineWithSchedules>

    @Delete
     fun delete(medicine: Medicine): Int

    @Delete
    fun deleteAll(medicine: List<Medicine>): Int
}