package esprims.gi2.ma_pharmacie.data.entity

import android.graphics.drawable.Drawable
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType

data class Medication(
    var id:Int?=null,
    val name :String?=null,
    val codabar:String?=null,
    val type: MedicineType?=null,
    val additionalDescription:String?=null,
    val unit :String?=null,
    val quantity:Float?=null,
    val image: Drawable?=null
)