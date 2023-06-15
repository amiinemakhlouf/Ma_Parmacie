package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.remote.donationService.DonationService
import esprims.gi2.ma_pharmacie.data.repository.DonationRepository
import esprims.gi2.ma_pharmacie.domain.usecases.donation.SaveDonation
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object DonationModule {
    @Provides
    public fun donationService(retrofit: Retrofit)= retrofit.create(DonationService::class.java)

    @Provides
    public fun donationRepository(donationService: DonationService)=
        DonationRepository(donationService)

    @Provides
     fun donationUseCase(donationRepository: DonationRepository)=
        SaveDonation(donationRepository)
}