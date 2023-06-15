package esprims.gi2.ma_pharmacie.presentation.medication.show_medication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.domain.usecases.medication.GetAllMedicationUseCase
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import    esprims.gi2.ma_pharmacie.presentation.shared.Result as Res
import javax.inject.Inject

@HiltViewModel
class ShowMedicationViewModel @Inject constructor
    (
    private val getAllMedicationUseCase: GetAllMedicationUseCase
            ):ViewModel()
{

    var isFirstStartup=true
        private  val mutableStateOfMedications =MutableStateFlow<UIState<List<Medication>>>(UIState.Default)
        val _mutableStateOfMedication=mutableStateOfMedications
        suspend fun getAllMedications() {

            mutableStateOfMedications.emit(UIState.Loading)

            val result= getAllMedicationUseCase.getAllMedications()

            viewModelScope.launch(IO){
                when(result)
            {
                is Res.Success -> {
                    mutableStateOfMedications.emit(UIState.Success(data=result.data!!))
                }
                is Res.Error -> result.message?.let {
                    UIState.Error(
                        it
                    )
                }?.let { mutableStateOfMedications.emit(it) }
            }
        }
        }
}