package esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model

data class MedicationTypeItem (
    val name:String,
    val explanation:String
        )
{
    override fun toString()=name

}