package esprims.gi2.ma_pharmacie.data.remote.donationService

import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.remote.util.SimpleStringResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DonationService {

    @POST("donations")
    suspend fun saveDonation(@Body donation: Donation): Response<Donation>

    @GET("donations")
    suspend fun getDonations(): Response<List<Donation>>
    @GET("donations/{email}")
    suspend fun getDonationsByEmail(@Path("email") email: String): Response<List<Donation>>
    @GET("donations/differentEmails/{email}")
    suspend fun getOtherPeopleDonations(@Path("email") email: String): Response<List<Donation>>

}