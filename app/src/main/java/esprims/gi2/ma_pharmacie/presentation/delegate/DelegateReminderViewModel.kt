package esprims.gi2.ma_pharmacie.presentation.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.domain.usecases.reminder.AddReminderUseCase
import esprims.gi2.ma_pharmacie.domain.usecases.reminder.GetReminderById
import esprims.gi2.ma_pharmacie.domain.usecases.reminder.UpdateReminderById
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
@HiltViewModel
class DelegateReminderViewModel @Inject constructor(
    private val addReminderUseCase: AddReminderUseCase,
    private val getReminderByIdUseCase: GetReminderById
):ViewModel()
{
    private val mutableStateOfReminder  = MutableStateFlow<UIState<Reminder>>(UIState.Default)
     val stateOfReminder  = mutableStateOfReminder

    private val mutableStateOfCheckReminder  = MutableStateFlow<UIState<Reminder>>(UIState.Default)
    val  stateOfCheckReminder=mutableStateOfCheckReminder


    suspend fun insertDelegate(reminder: Reminder)
    {

        mutableStateOfReminder.emit(UIState.Loading)


        viewModelScope.launch(IO){


       val res= addReminderUseCase(reminder)

        when(res)
        {
            is Result.Success -> mutableStateOfReminder.emit(UIState.Success(res.data!!))
            is Result.Error   -> mutableStateOfReminder.emit(UIState.Error())
        }

    }
    }

    suspend fun getReminderById(id:Int)
    {

       val res= getReminderByIdUseCase(id)

         when(res)
         {
             is Result.Success ->{
                 mutableStateOfCheckReminder.emit(UIState.Success())
             }
             is Result.Error ->{
                 mutableStateOfCheckReminder.emit(UIState.Error())
             }
         }

    }
}

