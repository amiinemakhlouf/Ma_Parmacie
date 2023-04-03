package esprims.gi2.ma_pharmacie.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {



    //val myServerLocalIpAdress="http://192.168.1.16:8080/"
    val myServerLocalIpAdress="http://10.0.2.2:8080/"
    fun build(): Retrofit = Retrofit.Builder()
        .baseUrl(myServerLocalIpAdress)
        .addConverterFactory(GsonConverterFactory.create())

        .build()

}
