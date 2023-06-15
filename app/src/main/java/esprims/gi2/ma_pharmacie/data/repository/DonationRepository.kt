package esprims.gi2.ma_pharmacie.data.repository

import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.remote.donationService.DonationService
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res
class DonationRepository @Inject constructor(
    private val donationService: DonationService
)
{
     suspend fun saveDonation(donation: Donation):Res<String>
    {
       val res= donationService.saveDonation(donation)

        if(res.isSuccessful)
        {
            return  Res.Success()
        }
        return  Res.Error()
    }
}