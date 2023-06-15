package esprims.gi2.ma_pharmacie.presentation.medication.medication_details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.domain.usecases.medication.UpdateMedicationUseCase
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res
@HiltViewModel
class MedicationDetailsViewModel @Inject constructor(
    private val updateMedicationUseCase: UpdateMedicationUseCase
):ViewModel()
{
    private  val mutableSharedFlowOfUpdate =MutableSharedFlow<UIState<String>>()
    val _mutableSharedFlowOfUpdate=mutableSharedFlowOfUpdate
     suspend fun updateMedication(id:Int,medication:Medication)
    {
        mutableSharedFlowOfUpdate.emit(UIState.Loading)

        val res=updateMedicationUseCase.updateMedicationByID(id,medication)

        when(res)
        {
            is  Res.Success -> mutableSharedFlowOfUpdate.emit(UIState.Success(""))
            is Res.Error -> mutableSharedFlowOfUpdate.emit(UIState.Error(""))
        }

    }
}