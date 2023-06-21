package esprims.gi2.ma_pharmacie.data.remote.medicationService

import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.remote.util.MedicationResponse
import esprims.gi2.ma_pharmacie.data.remote.util.SimpleStringResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MedicationService {

    @POST("medication/save")
    suspend fun saveMedication(@Body  medication: Medication,
    ):Response<Medication>

    @GET("get/all/medications")
    suspend fun getAllMedications(@Query("userEmail") userEmail: String):Response<List<Medication>>

    @GET("medication/{id}")
    suspend fun getMedication(@Path("id") id: String): Response<MedicationResponse>

    @PUT("/medication/update/{id}")
    suspend fun updateMedication(
        @Path("id") id: Int,
        @Body updatedMedication: Medication
    ): Response<Unit>

}