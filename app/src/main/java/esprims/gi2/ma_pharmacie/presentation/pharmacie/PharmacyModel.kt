package esprims.gi2.ma_pharmacie.presentation.pharmacie

data class PharmacyModel(
    val longitude:Double,
    val latitude:Double,
    val altitude:Double?=null,
    val isOpen:Boolean?=null,
    val name:String?=null,
    val address:String?=null,
    val rate:Float?=null,
    val closeTime:String?=null,
    val phoneNumber:String?=null

)
