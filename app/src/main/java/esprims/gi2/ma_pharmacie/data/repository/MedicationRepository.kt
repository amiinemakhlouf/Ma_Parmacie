package esprims.gi2.ma_pharmacie.data.repository

import android.util.Log
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.remote.medicationService.MedicationService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.PUT
import javax.inject.Inject
import    esprims.gi2.ma_pharmacie.presentation.shared.Result as Res

class MedicationRepository @Inject constructor
    (
    private  val medicationService: MedicationService
            )
{



     suspend fun updateMedication(id:Int,medication: Medication):Res<String>
     {
         val result=medicationService.updateMedication(id,medication)
         if(result.isSuccessful)
         {
             return  Res.Success()
         }
         return Res.Error()
     }
    suspend fun saveMedication(medication: Medication,image:MultipartBody.Part):Res<String>
    {

        val result=    medicationService.saveMedication(medication)
        if(result.isSuccessful)
        {
            Log.d("MedicationRepository"," the id is"+result.body()!!   .id)
            return Res.Success("succes")
            Log.d("MedicationRepository","succes")

        }
        Log.d("MedicationRepository",result.code().toString())

        return  Res.Error(result.message())


    }
    suspend fun getAll():Res<List<Medication>>
    {
       val result= medicationService.getAllMedications()

        if(result.isSuccessful){


            return Res.Success(result.body())
        }
        else{
            Log.d("MedicationRepository", result.message())
            return  Res.Error(result.errorBody().toString())
        }
    }
}