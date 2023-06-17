package esprims.gi2.ma_pharmacie.presentation.pharmacie

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.MotionEvent.ACTION_MOVE
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.BuildConfig
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Pharmacy
import esprims.gi2.ma_pharmacie.databinding.FragmentPharmacyBinding
import esprims.gi2.ma_pharmacie.databinding.SearchByCodabarDialogBinding
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.onBoarding.dataStore
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.config.IConfigurationProvider
import org.osmdroid.tileprovider.util.StorageUtils.getStorage
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


@AndroidEntryPoint
class PharmacyFragment : Fragment(),DataPassListener,OnMapReadyCallback {
    private var marker: Marker?=null
    private var isSearchIcon: Boolean=true
    private var userLong:Double?=null
    private var userLat:Double?=null
    private  var selectedDistance:Float?=null
    private  var selectedRate :Float?=null
    private   val viewmodel :PharmacyViewModel by viewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }
    private var showOnlyopenedPharmacy=false
    private  val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private lateinit var  barCodeLauncher : ActivityResultLauncher<ScanOptions>

    private val binding:FragmentPharmacyBinding by lazy {
        FragmentPharmacyBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideAppBar(requireActivity() as AppCompatActivity)
        /*handlePinchGestureToZoomIn()
        lifecycleScope.launch(Main)
        {
        viewmodel._pharmaciesState.collectLatest{
            Log.d("PharmacyFragment","tunisia sat")

             for (pharmacie in (it as (UIState.Success)).data){
                 Log.d("PharmacyFragment",pharmacie.name)
             }

            it.data.let {
                viewmodel.listOfPharmacies=it
                showAllPharmacies(it)
            }


        }
        }
        viewmodel.getAllPharmacies()
        requestPermissionLauncher = getRequestPermissionLauncher()
        getUserLocation()
        showFilterBottomSheet()
        handleFocusOnSearchEt()
        handleSearchMedication()
        searchMedicationByBarcode()
        barCodeLauncher=provideBarCodeLauncher()*/

        val mapFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)



    }


    private fun handleSearchMedication() {

        binding.searchMedicationEt.setOnEditorActionListener {
                v, actionId, event ->
            true

        }
    }

    private  fun searchMedicationByBarcode(){

        val dialogBinding=SearchByCodabarDialogBinding.inflate(layoutInflater)
        var dialog=MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)

        val alertDialog=dialog.show()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.dismiss()
        binding.searchMedication.setEndIconOnClickListener {
            if(isSearchIcon){


            lifecycleScope.launch(IO)
            {
                if(shouldBeOpened()){

                    scanBarCode()


                }
                else{
                    withContext(Main){
                        alertDialog.show()
                        saveInfoInLocalStorage()
                    }


                }

            }
            }
            else{
                lifecycleScope.launch {

                    removeMarker()

                    isSearchIcon=true
                    binding.searchMedicationEt.setText("")
                    binding.searchMedication.clearFocus()
                    binding.searchMedication.endIconDrawable=requireActivity().getDrawable(R.drawable.barcode_scan)
                    binding.searchMedication.setEndIconTintList(ColorStateList.valueOf(resources.getColor(R.color.dark_green)))


                }
            }

        }
        alertDialog.setOnDismissListener {
            lifecycleScope.launch(IO)
            {
                saveInfoInLocalStorage()

            }

        }
        dialogBinding.okay.setOnClickListener {
            scanBarCode()
            alertDialog.hide()

        }

        dialogBinding.okay.setOnClickListener {
            alertDialog.hide()

        }

    }

      private suspend fun shouldBeOpened(): Boolean {


        val key = stringPreferencesKey("keyCodabar")

              val preferences = requireActivity().dataStore.data.first()
              val isKeyAvailable = preferences.contains(key)

              if (isKeyAvailable) {
                 return true
              }
          return false


    }


    private fun scanBarCode() {

            barCodeLauncher.launch(ScanOptions())



    }

    suspend  private fun saveInfoInLocalStorage() {
        val key = stringPreferencesKey("keyCodabar")

        requireActivity().dataStore.edit { pref ->
            pref[key] = "1"


        }
    }

    private fun handleFocusOnSearchEt() {
        binding.searchMedicationEt.setOnFocusChangeListener {
                v, hasFocus ->
            when(hasFocus)
            {
                true -> binding.searchMedication.hint=""
                false -> binding.searchMedication.hint=requireActivity().getString(R.string.choose_medication_hint)
            }

        }
    }


    private  fun removeMarker()
    {

        marker?.closeInfoWindow()
        marker?.setInfoWindow(null)


    }


    private fun showFilterBottomSheet() {
        binding.filter.setOnClickListener {
            val pharmacyFilterBottomSheet =PharmacyFilterBottomSheet(this,selectedDistance,selectedRate)
            val bundle = Bundle()
            bundle.putBoolean("open", showOnlyopenedPharmacy) // Replace "key" with the desired key and "value" with the actual data
            pharmacyFilterBottomSheet.arguments = bundle
            val activity = requireActivity() as FragmentActivity
            pharmacyFilterBottomSheet.show(activity.supportFragmentManager,null)

        }
    }

    private fun getUserLocation() {
        when(requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
        {
            PackageManager.PERMISSION_GRANTED ->{
               // requestLocationUpdates()


            }
            else ->{}

        }
    }

    /*private fun getListOfPharmacies(): List<Pharmacy> {
         return listOf<Pharmacy>(


            Pharmacy(longitude =   35.7660459, latitude = 10.8287102, isOpen = false,
                streetName =  "bni khiar",name = "Dhifallah",
                rate = 5f, phoneNumber = "21414511", workingHourStart = "", workingHourEnd = ""),
             Pharmacy(longitude =   35.730438, latitude = 10.818563,
                 isOpen = true, streetName =  "bni khiar",
                 name = "Dhifallah",
                 rate = 5f, phoneNumber = "21414512"),
             Pharmacy(longitude =   35.760688,latitude = 10.753312, isOpen = false, streetName = "bni khiar", name = "Dhifallah",
                 rate = 5f, phoneNumber = "21414513"),


            )
    }
*/


    override fun onMapReady(p0: GoogleMap) {

       Toast.makeText(requireContext(),"viva la tunisia",Toast.LENGTH_SHORT).show()

    }

    override fun onDataPassed(applyOpenFilter: Boolean, distance: Float, minimumEvaluation: Float) {
        TODO("Not yet implemented")
    }


}



