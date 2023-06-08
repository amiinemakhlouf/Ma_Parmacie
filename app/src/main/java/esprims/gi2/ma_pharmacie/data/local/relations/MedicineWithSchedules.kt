package esprims.gi2.ma_pharmacie.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.entity.MedicineSchedule

data class MedicineWithSchedules(
    @Embedded val medication: Medication,
    @Relation(
     parentColumn = "id",
    entityColumn = "medicine_id",

         )
    val schedule: List<MedicineSchedule>


)