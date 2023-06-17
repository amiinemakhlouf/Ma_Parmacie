package esprims.gi2.ma_pharmacie.data.repository

import android.util.Log
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
    suspend fun getAllDonations():Res<List<Donation>>
    {
        val res=donationService.getDonations()
        if(res.isSuccessful)
        {
            return  Res.Success(res.body())
        }

            return  Res.Error()

    }

    suspend fun getOtherDonations(email: String):Res<List<Donation>>
    {
        val res=donationService.getOtherPeopleDonations(email)
        if(res.isSuccessful)
        {
            return  Res.Success(res.body()!!)
        }
        return Res.Error()
    }

    suspend fun getDonationByEmail(email:String):Res<List<Donation>>
    {
        val res=donationService.getDonationsByEmail(email)
        for (data in res.body()!!)
        {
            Log.d("DonationRepository","  "+data.email)
        }
        if(res.isSuccessful)
        {
            return Res.Success(data = res.body())
        }
        return  Res.Error()
    }
}