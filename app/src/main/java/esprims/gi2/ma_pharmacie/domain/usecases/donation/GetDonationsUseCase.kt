package esprims.gi2.ma_pharmacie.domain.usecases.donation

import esprims.gi2.ma_pharmacie.data.repository.DonationRepository
import javax.inject.Inject
import  esprims.gi2.ma_pharmacie.presentation.shared.Result as Res
class GetDonationsUseCase @Inject constructor(
   private val donationRepository: DonationRepository
) {
     suspend fun getAllDonations():Res<String>
    {
        donationRepository.getAllDonations()

    }
}