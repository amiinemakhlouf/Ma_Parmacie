package esprims.gi2.ma_pharmacie.data.entity

data class Donation(
    val city: String?=null,
    override val medicationName: String?=null,
    override val quantity: Float?=null,
    val availability: String?=null,
    val image1: ByteArray?=null,
    val image2: ByteArray?=null,
    val phoneNumber: String?=null,
    val isTaken: Boolean?=null,
    val email:String?=null
):Med(
    medicationName=medicationName,
    quantity=quantity
)