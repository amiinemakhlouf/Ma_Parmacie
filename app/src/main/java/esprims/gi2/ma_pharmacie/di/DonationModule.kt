package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.remote.donationService.DonationService
import esprims.gi2.ma_pharmacie.data.repository.DonationRepository
import esprims.gi2.ma_pharmacie.domain.usecases.reminder.GetOtherPeopleDonationsUseCase
import esprims.gi2.ma_pharmacie.domain.usecases.donation.GetDonationsUseCase
import esprims.gi2.ma_pharmacie.domain.usecases.donation.SaveDonationUseCase
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DonationModule {
    @Provides
    public fun donationService(retrofit: Retrofit) = retrofit.create(DonationService::class.java)

    @Provides
    public fun donationRepository(donationService: DonationService) =
        DonationRepository(donationService)

    @Provides
    fun donationUseCase(donationRepository: DonationRepository) =
        SaveDonationUseCase(donationRepository)

    @Provides
    fun provideGetDonations(donationRepository: DonationRepository) =
        GetDonationsUseCase(donationRepository)

    @Provides
    fun provideOtherPeopleDonation(donationRepository: DonationRepository) =
        GetOtherPeopleDonationsUseCase(donationRepository)


}