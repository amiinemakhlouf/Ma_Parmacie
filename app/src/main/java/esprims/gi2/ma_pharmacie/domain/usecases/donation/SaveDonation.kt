package esprims.gi2.ma_pharmacie.domain.usecases.donation

import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.repository.DonationRepository
import javax.inject.Inject

class SaveDonation @Inject constructor(
    val donationRepository: DonationRepository
) {
    suspend fun saveDonation(donation:Donation):esprims.gi2.ma_pharmacie.presentation.shared.Result<String>
    {
      return  donationRepository.saveDonation(donation)
    }
}