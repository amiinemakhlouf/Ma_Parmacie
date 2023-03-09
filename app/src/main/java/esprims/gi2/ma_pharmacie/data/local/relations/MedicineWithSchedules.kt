package esprims.gi2.ma_pharmacie.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import esprims.gi2.ma_pharmacie.data.local.entity.Medicine
import esprims.gi2.ma_pharmacie.data.local.entity.MedicineSchedule
import org.jetbrains.annotations.NotNull

data class MedicineWithSchedules(
    @Embedded val medicine: Medicine,
    @Relation(
     parentColumn = "id",
    entityColumn = "medicine_id",

         )
    val schedule: List<MedicineSchedule>


)