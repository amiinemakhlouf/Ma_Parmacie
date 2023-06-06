package esprims.gi2.ma_pharmacie.domain.usecases.pharmacies

import esprims.gi2.ma_pharmacie.data.entity.Pharmacy
import esprims.gi2.ma_pharmacie.data.repository.PharmacyRepository
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class GetAllPharmaciesUseCase @Inject constructor(
    private  val pharmacyRepository: PharmacyRepository
)

{
     suspend operator fun  invoke(): Result<List<Pharmacy>> {

           return pharmacyRepository.getAllPharmacies()
    }

}
