package esprims.gi2.ma_pharmacie.presentation.donation.show_donation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.domain.usecases.GetOtherPeopleDonationsUseCase
import esprims.gi2.ma_pharmacie.domain.usecases.donation.GetDonationByEmailUseCase
import esprims.gi2.ma_pharmacie.domain.usecases.donation.GetDonationsUseCase
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res

@HiltViewModel
class ShowDonationViewModel @Inject constructor(
    private  val getDonationUseCase: GetDonationsUseCase,
    private val getDonationByEmailUseCase: GetDonationByEmailUseCase,
    private val  getOtherPeopleDonationsUseCase: GetOtherPeopleDonationsUseCase
):ViewModel()
{
    private val mutableStateFlowOfDonations:MutableStateFlow<UIState<List<Donation>>> = MutableStateFlow(UIState.Default)
    val stateflowOfDonations= mutableStateFlowOfDonations
    val mutableStateFlowOfChoice= MutableStateFlow<Int>(2)
    val mutableStateFlowOfChoice1= mutableStateFlowOfChoice
    private val mutableStateFlowOFOtherPeopleDonation  = MutableStateFlow<UIState<List<Donation>>>(UIState.Default)
    val StateFlowOFOtherPeopleDonation=mutableStateFlowOFOtherPeopleDonation
    private  val mutableStateOfDonationByUser=MutableStateFlow<UIState<List<Donation>>>(UIState.Default)
    val stateFlowOfDonationByEmail =mutableStateOfDonationByUser

     suspend fun getDonations()
    {
        mutableStateFlowOfDonations.emit(UIState.Loading)

        val res=getDonationUseCase.getAllDonations()

        when(res)
        {
            is Res.Success -> mutableStateFlowOfDonations.emit(UIState.Success(res.data!!))
            is Res.Error ->mutableStateFlowOfDonations.emit(UIState.Error())

        }

    }

    suspend fun getOtherPeopleDonation(email: String)
    {
        mutableStateFlowOFOtherPeopleDonation.emit(UIState.Loading)
        val res=getOtherPeopleDonationsUseCase.getOtherPeopleDonationUseCase(email)
        when(res)
        {
            is Res.Success -> mutableStateFlowOFOtherPeopleDonation.emit(UIState.Success(res.data!!))
            is Res.Error ->mutableStateFlowOFOtherPeopleDonation.emit(UIState.Error())

        }
    }

    suspend fun getDonationByEmail(email:String)
    {
        Log.d("ShowMedicationViewModel",  " houssem tka ")
        mutableStateFlowOfDonations.emit(UIState.Loading)


        val res=getDonationByEmailUseCase.getDonationByEmail(email)
        when(res)
        {
         is Res.Success -> {
             mutableStateOfDonationByUser.emit(UIState.Success(res.data))
             for (data in res.data!!){
                 Log.d("ShowMedicationViewModel",  "  "+data.email)

             }
         }
            is Res.Error ->mutableStateOfDonationByUser.emit(UIState.Error(""))
        }
    } }
