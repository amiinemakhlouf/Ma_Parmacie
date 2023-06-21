package esprims.gi2.ma_pharmacie.domain.usecases.reminder

import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.repository.DonationRepository
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res

class GetOtherPeopleDonationsUseCase
    (
    private val donationRepository: DonationRepository
)

{
        suspend fun getOtherPeopleDonationUseCase(email: String):Res<List<Donation>>
        {
            return donationRepository.getOtherDonations(email)
        }
}