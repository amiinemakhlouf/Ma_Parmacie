package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.domain.usecases.reminder.ConfirmDelegatedReminder
import esprims.gi2.ma_pharmacie.domain.usecases.reminder.GetAllRemindersUseCase
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
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
    private val getAllRemindersUseCase: GetAllRemindersUseCase,
    private val confirmDelegatedReminder: ConfirmDelegatedReminder
) :ViewModel() {

    var isFirstStartUp=true
    var listByTime= mutableListOf<List<Reminder>>()
    var othersList= mutableListOf<List<Reminder>>()


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

    suspend fun RespondReminderDelegation(reminder: Reminder)
    {

        Log.d("RespondReminderDelegation",reminder.statu.toString())
        confirmDelegatedReminder.invoke(reminder)
    }
    suspend fun getAllReminders(jwt:String){
        mutableStateFlowOfReminders.emit(UIState.Loading)
        viewModelScope.launch(IO)
         {
             Log.d("ReminderViewMdodel", "i'm here bto")

           when(val result= getAllRemindersUseCase.invoke(jwt)){
                 is Result.Success -> {
                     Log.d("ReminderViewMdodel", "success")

                     val data=result.data
                     if(data!!.isEmpty())
                     {
                         mutableStateFlowOfReminders.emit(UIState.Success(listOf<Reminder>()))
                         return@launch
                     }
                     val finalList= mutableListOf<Reminder>()
                     for (reminder in data!!)
                     {
                         Log.d("ReminderFragmentViewModel  ",reminder.reminderTime!!)
                         val mysub=sliceString(reminder.reminderTime!!,9)
                         for (datad in mysub)
                         {
                             val reminder1= Reminder().apply {
                                 this.reminderTime=datad
                                 this.days=reminder.days
                                 this.dose=reminder.dose!!
                                 this.image=null
                                 this.medicationName=reminder.medicationName
                                 this.personName=reminder.personName
                                 this.type=reminder.type
                                 this.moment=reminder.moment
                                 this.endDate=reminder.endDate
                                 this.startDate=reminder.startDate
                                 this.isDelegated=reminder.isDelegated
                                 this.source=reminder.source
                                 this.statu=reminder.statu
                                 this.userEmail=reminder.userEmail
                                 this.statu=reminder.statu

                             }
                             finalList.add(reminder1)

                         }


                     }

                         Log.d("ReminderFragmentViewModel",data.size.toString())


                     mutableStateFlowOfReminders.emit(UIState.Success(finalList))
                 }

               else ->Log.d("ReminderViewMdodel"," erreur")
             }

         }


    }

    fun sliceString(str: String, length: Int): List<String> {
        return str.chunked(length)
    }


}