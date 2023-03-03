package esprims.gi2.ma_pharmacie.data.local.entity

import java.sql.Timestamp

data class Notification(
    val beginningDate:Timestamp,
    val endDate:Timestamp,
    val patient:String,
    val dose:Int,
  )
