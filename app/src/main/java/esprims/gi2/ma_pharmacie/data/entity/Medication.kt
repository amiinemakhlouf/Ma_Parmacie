package esprims.gi2.ma_pharmacie.data.entity

import android.graphics.drawable.Drawable
import android.os.Parcelable
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Medication(
    var id:Int?=null,
    val name :String?=null,
    val codabar:String?=null,
    val type: String?=null,
    val additionalDescription:String?=null,
    val unit :String?=null,
    val quantity:Float?=null,
    val state:Int?=null,
    val image: String?=null,
    val userEmail:String?=null
):Parcelable