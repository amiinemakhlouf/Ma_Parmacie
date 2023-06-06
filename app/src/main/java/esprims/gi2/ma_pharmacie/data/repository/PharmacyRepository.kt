package esprims.gi2.ma_pharmacie.data.repository
import android.util.Log
import esprims.gi2.ma_pharmacie.presentation.shared.Result as res
import esprims.gi2.ma_pharmacie.data.entity.Pharmacy
import esprims.gi2.ma_pharmacie.data.remote.pharmacieService.PharmacyService
import javax.inject.Inject

class PharmacyRepository @Inject constructor(
  private val pharmacyService: PharmacyService
) {
      public suspend fun getAllPharmacies():res<List<Pharmacy>>
    {
       val response= pharmacyService.getAllPharmacies()


      return  when(response.isSuccessful && response.body()!=null){

            true -> {
                val listOfPharmacies= response.body()
                for (pharmacy in listOfPharmacies!!){

                    Log.d("my barmacy ",pharmacy.workingHourStart!! )
                }

                res.Success(response.body())


            }
            else -> {
                Log.d("my name is    ",response.errorBody().toString())
                res.Error()
            }
        }
    }
}