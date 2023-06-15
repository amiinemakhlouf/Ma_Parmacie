package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.repository.MedicationRepository
import esprims.gi2.ma_pharmacie.data.repository.ReminderRepository
import esprims.gi2.ma_pharmacie.domain.usecases.medication.GetAllMedicationUseCase
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res

@HiltViewModel
class AddReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val getAllMedicationUseCase: GetAllMedicationUseCase
) :ViewModel() {
    var isFirstActivityLifeCycle=true
    private val reminderState: MutableSharedFlow<UIState<String>> = MutableSharedFlow()
    val _reminderState: MutableSharedFlow<UIState<String>> = reminderState

    private val mutableStateOfMedication=MutableStateFlow<UIState<List<Medication>>>(UIState.Default)
     val _mutableStateOfMedcations=mutableStateOfMedication
    var items:MutableList<Medication> ? = null


    public  fun saveReminder(reminder: Reminder){

        viewModelScope.launch(IO) {
            reminderState.emit(UIState.Loading)
            val result=reminderRepository.saveReminder(reminder)
            when(result){

                is Result.Success -> {
                    reminderState.emit(UIState.Success(""))
                }
                is Result.Error  ->reminderState.emit(UIState.Error(""))
            }

        }

    }

     public suspend fun getMedications()
    {
        mutableStateOfMedication.emit(UIState.Loading)
        viewModelScope.launch (IO)
        {
            val res=getAllMedicationUseCase.getAllMedications()
            when(res)
            {
                is Res.Success -> {

                    mutableStateOfMedication.emit(UIState.Success(res.data!!))
                    items=res.data!!.toMutableList()
                }
                else -> mutableStateOfMedication.emit(UIState.Error(""))
            }

        }


    }


}