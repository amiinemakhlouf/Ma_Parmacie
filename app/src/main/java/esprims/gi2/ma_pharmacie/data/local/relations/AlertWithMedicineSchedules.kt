package esprims.gi2.ma_pharmacie.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import esprims.gi2.ma_pharmacie.data.local.entity.Alert
import esprims.gi2.ma_pharmacie.data.local.entity.MedicineSchedule

data class AlertWithMedicineSchedules(
    @Embedded val alert: Alert,
    @Relation(
        parentColumn = "timestamp",
        entityColumn = "alert_timestamp",
    )
    val medicineSchedules:List<MedicineSchedule>

)