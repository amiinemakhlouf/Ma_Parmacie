package esprims.gi2.ma_pharmacie.data.remote.util

import esprims.gi2.ma_pharmacie.data.entity.Medication

data class MedicationResponse(
    val medication: Medication,
    val image: String
)