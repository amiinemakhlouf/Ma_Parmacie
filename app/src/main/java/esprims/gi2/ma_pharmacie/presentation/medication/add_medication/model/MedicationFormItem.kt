package esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model

import androidx.annotation.DrawableRes

data class MedicationFormItem (


    val name:String,
    @DrawableRes val image:Int
        )


{

    override fun toString():String{
        //name.substring(name.lastIndex-2,name.lastIndex+1)
        return  name
    }


}
