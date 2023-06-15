package esprims.gi2.ma_pharmacie.domain.usecases.medication

import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.repository.MedicationRepository
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Inject
import    esprims.gi2.ma_pharmacie.presentation.shared.Result as Res


class GetAllMedicationUseCase @Inject constructor
    (private  val medicationRepository: MedicationRepository)
{


    suspend fun getAllMedications():Res<List<Medication>>
    {
       return medicationRepository.getAll()
    }

}