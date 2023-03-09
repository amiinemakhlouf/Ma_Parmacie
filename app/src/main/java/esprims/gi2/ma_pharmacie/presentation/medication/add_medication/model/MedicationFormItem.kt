package esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model

import androidx.annotation.DrawableRes

data class MedicationFormItem (


    val name:String,
    @DrawableRes val image:Int
        )


{
    companion object{
        var index=1
    }
    override fun toString():String{

        return  name.substring(name.lastIndex-2,name.lastIndex+1)
    }


}
