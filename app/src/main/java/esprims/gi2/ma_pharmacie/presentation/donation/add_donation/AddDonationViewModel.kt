package esprims.gi2.ma_pharmacie.presentation.donation.add_donation

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.domain.usecases.donation.SaveDonationUseCase
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res
@HiltViewModel
class AddDonationViewModel @Inject constructor(
val donationUseCase: SaveDonationUseCase
):ViewModel() {
    var selectedCity=""
    val city: String?=null
    private val mutableStaFlowOfAddReminder= MutableStateFlow<UIState<String>>(UIState.Default)
    val StaFlowOfAddReminder= mutableStaFlowOfAddReminder
    var imageUri1:Uri?=null
    var imageUri2:Uri?=null
    var imageView1:ImageView?=null
    var imageView2:ImageView?=null
    var imageLauncherDispacher=0

    suspend fun saveDonation(donation:Donation)
    {
        mutableStaFlowOfAddReminder.emit(UIState.Loading)
        Log.d("AddDonationViewModel","save donation in vm called")
        val result=donationUseCase.saveDonation(donation)
        when(result)
        {
            is Res.Success -> mutableStaFlowOfAddReminder.emit(UIState.Success("ok"))

            is Res.Error ->   mutableStaFlowOfAddReminder.emit(UIState.Success("Error"))
        }
    }

}