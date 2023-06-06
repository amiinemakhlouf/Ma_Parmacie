package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.repository.ReminderRepository
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReminderViewModel @Inject constructor(
    val reminderRepository: ReminderRepository
) :ViewModel() {
    var isFirstActivityLifeCycle=true
    private val reminderState: MutableSharedFlow<UIState<String>> = MutableSharedFlow()
    val _reminderState: MutableSharedFlow<UIState<String>> = reminderState


    public  fun saveReminder(reminder: Reminder){

        viewModelScope.launch(IO) {
            reminderState.emit(UIState.Loading)
            val result=reminderRepository.saveReminder(reminder)
            when(result){

                is Result.Success -> reminderState.emit(UIState.Success(""))
                is Result.Error  ->reminderState.emit(UIState.Error(""))
            }

        }

    }


}