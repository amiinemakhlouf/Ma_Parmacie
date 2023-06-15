package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.remote.medicationService.MedicationService
import esprims.gi2.ma_pharmacie.data.repository.MedicationRepository
import esprims.gi2.ma_pharmacie.domain.usecases.medication.SaveMedicationUseCase
import esprims.gi2.ma_pharmacie.domain.usecases.medication.UpdateMedicationUseCase
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)

object MedicationModule {

    @Provides
    fun provideMedicationService(retrofit: Retrofit)= retrofit.create(MedicationService::class.java)

    @Provides
    fun provideMedicationRepository(medicationService: MedicationService)=
        MedicationRepository(medicationService)

    @Provides
    fun provideSaveMedicationUseCase(medicationRepository: MedicationRepository)=

        SaveMedicationUseCase(medicationRepository)

    @Provides
    fun provideUpdateMedicationById(medicationRepository: MedicationRepository)=
        UpdateMedicationUseCase(medicationRepository)
}