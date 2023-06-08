package esprims.gi2.ma_pharmacie.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import esprims.gi2.ma_pharmacie.data.entity.Alert
import esprims.gi2.ma_pharmacie.data.entity.MedicineSchedule

@Database(entities = [ MedicineSchedule::class, Alert::class], version = 3)
abstract class AppDatabase : RoomDatabase() {


    companion object{
        val databaseName="MyDatabase"
    }

}