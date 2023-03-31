package esprims.gi2.ma_pharmacie.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    val myServerLocalIpAdress="http://192.168.1.16:8080"
    fun build(): Retrofit = Retrofit.Builder()
        .baseUrl(myServerLocalIpAdress+"/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}
