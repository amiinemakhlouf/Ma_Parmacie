package esprims.gi2.ma_pharmacie.data.local.entity

import esprims.gi2.ma_pharmacie.data.local.enums_helpers.AgeCategory
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.Gender
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineForm
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import java.sql.Timestamp
@Ent
data  class Medicine(
    val name :String,
    val codabar:String,
    val gender: Gender,
    val ageCategory: AgeCategory,
    val form: MedicineForm,
    val type: MedicineType,
    val expirationDate:Timestamp,
    val additionalDescription:String,




    )