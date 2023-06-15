package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.data.repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Provides
    fun provideRetrofitInstance():Retrofit{
        val myServerLocalIpAdress="http://192.168.188.34:3010/"
        //val myServerLocalIpAdress="http://10.0.2.2:3000/"
        val retrofit = Retrofit.Builder()
            .baseUrl(myServerLocalIpAdress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return   retrofit
    }

    @Provides
    fun provideUserService(retrofit: Retrofit):UserService
    {
        return retrofit.create(UserService::class.java)
    }
    @Provides
    fun provideUserRepository(userService: UserService)=UserRepository(userService)

}

