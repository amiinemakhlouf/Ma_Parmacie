package esprims.gi2.ma_pharmacie.data.remote.medicationService

import esprims.gi2.ma_pharmacie.data.entity.Medication
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MedicationService {

    @POST("medication/save")
    suspend fun saveMedication(@Body medication: Medication):Response<Medication>

    @GET("medication/get/all")
    suspend fun getAllMedications():Response<List<Medication>>

}