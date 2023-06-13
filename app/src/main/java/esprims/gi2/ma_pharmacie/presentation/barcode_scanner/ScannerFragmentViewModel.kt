package esprims.gi2.ma_pharmacie.presentation.barcode_scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.domain.usecases.medication.SaveMedicationUseCase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerFragmentViewModel @Inject constructor(
    private val saveMedicationUseCase: SaveMedicationUseCase
) :ViewModel() {

    public fun saveMedication(medication: Medication){


        viewModelScope.launch(IO)
        {
            saveMedicationUseCase.saveMedication(medication)
        }


    }
}