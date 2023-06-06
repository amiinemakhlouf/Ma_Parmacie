package esprims.gi2.ma_pharmacie.presentation.pharmacie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Pharmacy
import esprims.gi2.ma_pharmacie.domain.usecases.pharmacies.GetAllPharmaciesUseCase
import esprims.gi2.ma_pharmacie.presentation.register.Utils
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Result

@HiltViewModel
class PharmacyViewModel @Inject constructor(
     private  val getAllPharmaciesUseCase: GetAllPharmaciesUseCase
)  :ViewModel() {
    private val pharmaciesState: MutableStateFlow<UIState<List<Pharmacy>>> = MutableStateFlow(UIState.Success(
        listOf()
    ))
    val _pharmaciesState: StateFlow<UIState<List<Pharmacy>>> = pharmaciesState
    var listOfPharmacies= listOf<Pharmacy>()

      fun getAllPharmacies(){

        viewModelScope.launch(IO) {
            val result=getAllPharmaciesUseCase()
            val rightNow = Calendar.getInstance()
            val currentHourIn24Format: Int =rightNow.get(Calendar.HOUR_OF_DAY) // return the hour in 24 hrs format (ranging from 0-23)
            Log.d("current time",currentHourIn24Format.toString())
            when(result){

                is  Result.Success ->{
                    Log.d("PharmacyViewModel"," inside result"+result.data!!.size)
                    for(pharmacy in result.data){
                         val startDateHour=pharmacy.workingHourStart!!.substring(0, 2).toInt()
                         val endDateHour=pharmacy.workingHourEnd!!.substring(0,2).toInt()
                        Log.d("startDateHour",pharmacy.workingHourStart.substring(0, 2))
                        Log.d("EndDateHour",pharmacy.workingHourEnd.substring(0, 2))
                        pharmacy.isOpen = currentHourIn24Format in startDateHour .. endDateHour
                     }
                    pharmaciesState.emit(UIState.Success(data=result.data))
                }

                else -> Log.d("PharmacyViewModel"," outside result")

            }

        }
    }


}