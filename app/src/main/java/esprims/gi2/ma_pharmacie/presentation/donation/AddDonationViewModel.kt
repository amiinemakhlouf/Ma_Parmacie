package esprims.gi2.ma_pharmacie.presentation.donation

import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.domain.usecases.donation.SaveDonation
import esprims.gi2.ma_pharmacie.domain.usecases.medication.GetAllMedicationUseCase
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res
@HiltViewModel
class AddDonationViewModel @Inject constructor(
val donationUseCase: SaveDonation
):ViewModel() {
    val city: String?=null
    private val mutableStaFlowOfAddReminder= MutableStateFlow<UIState<String>>(UIState.Default)
    val StaFlowOfAddReminder= MutableStateFlow<UIState<String>>(UIState.Default)
    var imageUri1:Uri?=null
    var imageUri2:Uri?=null
    var imageView1:ImageView?=null
    var imageView2:ImageView?=null
    var imageLauncherDispacher=0

    suspend fun saveDonation(donation:Donation)
    {
        mutableStaFlowOfAddReminder.emit(UIState.Loading)
        val result=donationUseCase.saveDonation(donation)
        when(result)
        {
            is Res.Success -> mutableStaFlowOfAddReminder.emit(UIState.Success("ok"))

            is Res.Error ->   mutableStaFlowOfAddReminder.emit(UIState.Success("Error"))
        }
    }

}