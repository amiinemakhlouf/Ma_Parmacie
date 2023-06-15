package esprims.gi2.ma_pharmacie.presentation.donation.show_donation

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.domain.usecases.donation.GetDonationByEmailUseCase
import esprims.gi2.ma_pharmacie.domain.usecases.donation.GetDonationsUseCase
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res

@Module
@InstallIn(SingletonComponent::class)
class ShowDonationViewModel @Inject constructor(
    private  val getDonationViewModel: GetDonationsUseCase,
    private val getDonationByEmailUseCase: GetDonationByEmailUseCase
):ViewModel()
{
    private val mutableStateFlowOfDonations:MutableStateFlow<UIState<List<Donation>>> = MutableStateFlow(UIState.Default)
    val stateflowOfDonations= mutableStateFlowOfDonations as StateFlow<UIState<List<Donation>>>
    val mutableStateFlowOfChoice= MutableStateFlow<Int>(0)

    private  val mutableStateOfDonationByUser=MutableStateFlow(UIState.Default)
    val stateFlowOfDonationByEmail =mutableStateOfDonationByUser as StateFlow<UIState<List<Donation>>>

     suspend fun getDonations()
    {
        mutableStateFlowOfDonations.emit(UIState.Loading)

        val res=getDonationViewModel.getAllDonations()

        when(res)
        {
            is Res.Success -> mutableStateFlowOfDonations.emit(UIState.Success())
            is Res.Error ->mutableStateFlowOfDonations.emit(UIState.Error())

        }

    }

    suspend fun getDonationByEmail(email:String)
    {
        mutableStateFlowOfDonations.emit(UIState.Loading)
        val res=getDonationByEmailUseCase.getDonationByEmail(email)
        when(res)
        {
            is Res.Success -> mutableStateFlowOfDonations.emit(UIState.Success(res.data))
            is Res.Error ->mutableStateFlowOfDonations.emit(UIState.Error(""))
        }
    }
}