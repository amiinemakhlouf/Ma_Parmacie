package esprims.gi2.ma_pharmacie.domain.usecases.medication

import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.repository.MedicationRepository
import javax.inject.Inject

class SaveMedicationUseCase @Inject constructor
    (
    private val medicationRepository: MedicationRepository
            ){

    suspend fun saveMedication(medication: Medication){


        medicationRepository.saveMedication(medication)

    }
}