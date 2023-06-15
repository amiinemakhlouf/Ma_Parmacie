package esprims.gi2.ma_pharmacie.data.remote.donationService

import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.remote.util.SimpleStringResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DonationService {

    @POST("/donations")
    suspend fun saveDonation(@Body donation: Donation): Response<List<SimpleStringResponse>>

}