package esprims.gi2.ma_pharmacie.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Donation(
     val city: String?=null,
     val medicationName: String?=null,
     val quantity: String? =null,
     val availability: String?=null,
     val image1: String?=null,
     val image2: String?=null,
     val phoneNumber: String?=null,
     val isTaken: Boolean?=null,
     val email:String?=null
):Parcelable