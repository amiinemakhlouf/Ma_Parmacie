package esprims.gi2.ma_pharmacie.data.entity

data class Donation(
    val city: String?=null,
    val medicationName: String?=null,
    val quantity: Float?=null,
    val availability: String?=null,
    val image1: ByteArray?=null,
    val image2: ByteArray?=null,
    val phoneNumber: String?=null,
    val isTaken: Boolean?=null
)