package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.domain.usecases.reminder.GetAllRemindersUseCase
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import esprims.gi2.ma_pharmacie.useCase.deleteJwtLocally
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Result

@HiltViewModel
class ReminderFragmentViewModel @Inject constructor(
    private val getAllRemindersUseCase: GetAllRemindersUseCase

) :ViewModel() {

    var isFirstStartUp=true
    var listByTime= mutableListOf<List<Reminder>>()


    private  val mutableSharedFlowOfLogout:MutableSharedFlow<UIState<String>> = MutableSharedFlow()
    val sharedFlowOfLogout=mutableSharedFlowOfLogout as SharedFlow<UIState<String>>
    private  val mutableStateFlowOfReminders:MutableStateFlow<UIState<List<Reminder>>> = MutableStateFlow(UIState.Default)
    val stateFlowOfReminders=mutableStateFlowOfReminders as StateFlow<UIState<List<Reminder>>>
    suspend fun logout(context:Context){
         mutableSharedFlowOfLogout.emit(UIState.Loading)
        viewModelScope.launch (IO)
        {
            try {
                deleteJwtLocally(context)
                 mutableSharedFlowOfLogout.emit(UIState.Success(""))
            }
            catch (e:java.lang.Exception)
            {
                mutableSharedFlowOfLogout.emit(UIState.Error(""))
            }

        }
    }
    suspend fun getAllReminders(jwt:String){
        mutableStateFlowOfReminders.emit(UIState.Loading)
        viewModelScope.launch(IO)
         {

           when(val result= getAllRemindersUseCase.invoke(jwt)){

                 is Result.Success -> {

                     val data=result.data
                     for (reminder in data!!)
                     {
                         Log.d("ReminderFragmentViewModel  ",reminder.medicationName)

                     }
                     mutableStateFlowOfReminders.emit(UIState.Success(data))
                 }
             }

         }


    }

}