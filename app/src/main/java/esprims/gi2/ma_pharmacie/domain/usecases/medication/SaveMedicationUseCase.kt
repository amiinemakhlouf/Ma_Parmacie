package esprims.gi2.ma_pharmacie.domain.usecases.medication

import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.repository.MedicationRepository
import okhttp3.MultipartBody
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res

class SaveMedicationUseCase @Inject constructor
    (
    private val medicationRepository: MedicationRepository
            ){

    suspend fun saveMedication(medication: Medication,image:MultipartBody.Part):Res<String>{


        return medicationRepository.saveMedication(medication,image)


    }
}