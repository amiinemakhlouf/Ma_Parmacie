package esprims.gi2.ma_pharmacie.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.AgeCategory
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.Gender
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineForm
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import java.sql.Timestamp

@Entity(tableName = "medicine")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val name :String,
    val codabar:String,
    val gender: Gender,
    @ColumnInfo(name="age_category")
    val ageCategory: String,
    @ColumnInfo(name="form")
    val form: MedicineForm,
    val type: MedicineType,
    @ColumnInfo(name="expiration_date")
    val expirationDate:Long,
    @ColumnInfo(name="additional_description")
    val additionalDescription:String,


    )