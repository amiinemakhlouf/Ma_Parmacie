package esprims.gi2.ma_pharmacie.presentation.medication

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.domain.usecases.medication.SaveMedicationUseCase
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddMedicationViewModel @Inject constructor (
     private val saveMedicationUseCase: SaveMedicationUseCase

) :ViewModel() {
     private val mutableSharedOfSaveResponse= MutableStateFlow<UIState<String>> (UIState.Default)
     val sharedFlowOfSaveResponse= mutableSharedOfSaveResponse
     var medicationImageUri: Uri?=null
     var file: File?=null
     public fun saveMedication(medication: Medication, multipartBody: MultipartBody.Part){

          Log.d("ScannerFragmentViewModel","  i'm called")


          viewModelScope.launch(Dispatchers.IO)
          {
               mutableSharedOfSaveResponse.emit(UIState.Loading)
               val result=saveMedicationUseCase.saveMedication(medication,multipartBody)
               when(result){

                    is Result.Success ->{
                         mutableSharedOfSaveResponse.emit(UIState.Success(data = ""))
                    }
                    is Result.Error ->{
                         mutableSharedOfSaveResponse.emit(UIState.Error(""))
                    }
               }
          }


     }


}