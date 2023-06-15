package esprims.gi2.ma_pharmacie.domain.usecases.donation

import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.repository.DonationRepository
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res
class GetDonationByEmailUseCase @Inject constructor(
    private  val donationRepository: DonationRepository
) {
    suspend fun getDonationByEmail(email:String):Res<List<Donation>>
    {
       return donationRepository.getDonationByEmail(email)
    }
}