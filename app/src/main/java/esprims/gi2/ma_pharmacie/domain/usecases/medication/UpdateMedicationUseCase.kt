package esprims.gi2.ma_pharmacie.domain.usecases.medication

import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.repository.MedicationRepository
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Res

class UpdateMedicationUseCase @Inject constructor(
    val medicationRepository: MedicationRepository
)
{

    public suspend fun updateMedicationByID(id:Int,medication: Medication):Res<String>{

       return medicationRepository.updateMedication(id,medication)

    }
}