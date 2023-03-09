package esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model

import androidx.annotation.DrawableRes

class MedicationMeantForItem (
    val meantForText:MeantFor,
    @DrawableRes val meantForIcon :Int
        )

{
    override fun toString()=meantForText.toString()
}