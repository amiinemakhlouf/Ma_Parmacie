package esprims.gi2.ma_pharmacie.data.repository

import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.remote.medicationService.MedicationService
import retrofit2.Response
import javax.inject.Inject

class MedicationRepository @Inject constructor
    (
    private  val medicationService: MedicationService
            )
{

     suspend fun saveMedication(medication: Medication)
    {
        medicationService.saveMedication(medication)

    }
    suspend fun getAll():Response<List<Medication>>
    {
       return medicationService.getAllMedications()
    }
}