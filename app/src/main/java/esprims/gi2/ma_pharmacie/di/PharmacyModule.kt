package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.remote.pharmacieService.PharmacyService
import esprims.gi2.ma_pharmacie.data.repository.PharmacyRepository
import esprims.gi2.ma_pharmacie.domain.usecases.pharmacies.GetAllPharmaciesUseCase
import retrofit2.Retrofit
@Module
@InstallIn(SingletonComponent::class)
object PharmacyModule {

    @Provides
    fun providePharmacyService(retrofit: Retrofit)=retrofit.create(PharmacyService::class.java)

    @Provides
    fun providePharmacyRepository(pharmacyService: PharmacyService)=
        PharmacyRepository(pharmacyService)

    @Provides
    fun  provideGetAllPharmaciesUseCase(pharmacyRepository: PharmacyRepository)=
        GetAllPharmaciesUseCase(pharmacyRepository)

}