package esprims.gi2.ma_pharmacie.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import esprims.gi2.ma_pharmacie.data.local.dao.MedicineDao
import esprims.gi2.ma_pharmacie.data.local.dao.ScheduleDao
import esprims.gi2.ma_pharmacie.data.entity.Alert
import esprims.gi2.ma_pharmacie.data.entity.Medicine
import esprims.gi2.ma_pharmacie.data.entity.MedicineSchedule

@Database(entities = [Medicine::class, MedicineSchedule::class, Alert::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicineDao() : MedicineDao
    abstract  fun scheduleDao() :ScheduleDao

    companion object{
        val databaseName="MyDatabase"
    }

}