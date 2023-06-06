package esprims.gi2.ma_pharmacie.data.remote.pharmacieService

import esprims.gi2.ma_pharmacie.data.entity.Pharmacy
import retrofit2.Response
import retrofit2.http.GET

interface PharmacyService {

    @GET("api/pharmacy")
       public suspend  fun getAllPharmacies() :Response<List<Pharmacy>>
}