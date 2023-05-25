package esprims.gi2.ma_pharmacie.domain.usecases.pharmacies

import esprims.gi2.ma_pharmacie.data.repository.PharmacyRepository
import javax.inject.Inject

class GetAllPharmaciesUseCase @Inject constructor(
    private  val pharmacyRepository: PharmacyRepository
)

{
    operator  fun  invoke(){


    }

}
