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
import kotlinx.coroutines.flow.collectLatest
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
class PharmacyFragment : Fragment(),PinchSaceListenner,DataPassListener {
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
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var showOnlyopenedPharmacy=false
    private  val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private lateinit var  barCodeLauncher : ActivityResultLauncher<ScanOptions>

    private val binding:FragmentPharmacyBinding by lazy {
        FragmentPharmacyBinding.inflate(layoutInflater)
    }
    private val controller: IMapController by lazy {
        binding.maps.controller
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpMap()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideAppBar(requireActivity() as AppCompatActivity)
        handlePinchGestureToZoomIn()
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
        barCodeLauncher=provideBarCodeLauncher()





    }

    private fun handleSearchMedication() {

        binding.searchMedicationEt.setOnEditorActionListener {
                v, actionId, event ->
            handleSearchMedicationLogic()
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
                    showAllPharmacies(viewmodel.listOfPharmacies)
                    binding.maps.invalidate()
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

    private fun handleSearchMedicationLogic()  {
        if (isSearchIcon){

            if(binding.searchMedicationEt.text.toString().isNullOrBlank()){

                return
            }

            requireActivity().hideKeyboard(binding.searchMedication)
            lifecycleScope.launch {
                //loadingDialog.showDialog()
                delay(2000)
              //  loadingDialog.hideDialog()
                binding.maps.overlays.removeAll(binding.maps.overlays)
               // if(binding.searchMedicationEt.text.toString().uppercase()=="Efferalgan".uppercase()){
                  lifecycleScope.launch(Main){
                      viewmodel._pharmaciesState.collect{
                          val data =it as UIState.Success
                          it.data?.let { list->
                          viewmodel.listOfPharmacies=list

                          }
                          viewmodel.listOfPharmacies=it.data!!
                          showPharmaciesWhereMedicationAvailableMarker(viewmodel.listOfPharmacies[1],"disponible ici")

                      }
                  }

                binding.searchMedication.endIconDrawable=requireActivity().getDrawable(R.drawable.ic_baseline_close_24)
            }
            isSearchIcon=false
        }
        else
        {
            lifecycleScope.launch {

                removeMarker()
                showAllPharmacies(viewmodel.listOfPharmacies)
                binding.maps.invalidate()
                isSearchIcon=true
                binding.searchMedicationEt.setText("")
                binding.searchMedication.clearFocus()
                binding.searchMedication.endIconDrawable=requireActivity().getDrawable(R.drawable.barcode_scan)
                binding.searchMedication.setEndIconTintList(ColorStateList.valueOf(resources.getColor(R.color.dark_green)))


            }

        }
    }

    private fun showPharmaciesWhereMedicationAvailableMarker(pharmacy: Pharmacy, message:String?) {

         marker=Marker(binding.maps).apply {

            position=GeoPoint(pharmacy.longitude,pharmacy.latitude)
            setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM)
            icon=resources.getDrawable(R.drawable.opened_pharmacy)
            title=message
            binding.maps.overlays.add( this)


        }
            // add your code here
            marker?.showInfoWindow()
            binding.maps.overlays.add(marker)
            binding.searchMedication.endIconDrawable=resources.getDrawable(R.drawable.ic_baseline_close_24)
            binding.searchMedication.setEndIconTintList(ColorStateList.valueOf(Color.RED))






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
                requestLocationUpdates()


            }
            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

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
    private fun focusOnCenter() {


        val point=GeoPoint(35.7603745.toDouble(),10.8112995)

        controller.setCenter(point)

        controller.setZoom(10.toDouble())
    }

    private fun showPharmacyBottomSheet(marker: Marker,pharmacyModel: Pharmacy) {

        marker.setOnMarkerClickListener { marker, mapView ->

            val pharmacyBottomSheet= PharmacyBottomSheet()
            val args = Bundle().apply {
                putString("name", pharmacyModel.name)
                putString("address", pharmacyModel.streetName)
                pharmacyModel.isOpen?.let {isOpen->
                    putBoolean("isOpen",isOpen )
                }
                pharmacyModel.rate?.let { rates->
                    putFloat("rates",rates)
                }
                putString("phoneNumber",pharmacyModel.phoneNumber)


                pharmacyBottomSheet.arguments=this
                pharmacyBottomSheet.show(requireFragmentManager(),null)

            }
            controller.zoomIn()
            true

        }

    }

    private fun getRequestPermissionLauncher(): ActivityResultLauncher<String> {

        return registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                requestLocationUpdates()

            } else {

               // showDialogToInformUserTheNeedToLocationPermission()

            }
        }

    }

    private fun showDialogToInformUserTheNeedToLocationPermission() {
        AlertDialog.Builder(requireContext(), R.style.permission_dialog)
            .setTitle("Autorisation de localisation requise")
            .setMessage("L'application a besoin de l'autorisation d'accès à la localisation pour fonctionner correctement.")
            .setPositiveButton("Autoriser") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton("Annuler") { _, _ ->
                // Do nothing
            }
            .setCancelable(false)
            .show()
    }

    private fun addMarker(latitude:Double,longitude:Double) :Marker {

        val marker= Marker(binding.maps).apply {
            position=GeoPoint(35.7603745.toDouble(),10.8112995)
            setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM)
            binding.maps.overlays.add( this)

        }
        return marker
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handlePinchGestureToZoomIn() {
        val pinchDetector = ScaleGestureDetector(requireContext(), MyPinchListener(this))
        binding.maps.setOnTouchListener { v, event ->


            if(event.action== ACTION_MOVE &&isPinchGesture(event)  ){

                pinchDetector.onTouchEvent(event)

                true
            }
            false
        }
    }

    override fun zoomIn() {

        controller.zoomIn()
        Log.d("zoomi","i'm zoomed in")


    }

    override fun zoomOut() {
        controller.zoomOut()
        Log.d("zoomi","i'm zoomed out")


    }

    private fun isPinchGesture(event: MotionEvent): Boolean {
        if (event.pointerCount == 2) {
                return true
            }

        return false
    }




    private fun setUpMap() {
        val provider: IConfigurationProvider = Configuration.getInstance()
        provider.userAgentValue = BuildConfig.APPLICATION_ID
        provider.osmdroidBasePath = getStorage()
        provider.osmdroidTileCache = getStorage()
    }



    private fun requestLocationUpdates() :esprims.gi2.ma_pharmacie.presentation.pharmacie.Location?{

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                         userLat = location.latitude
                           userLong = location.longitude
                    val altitude = location.altitude
                    val geoPoint=GeoPoint(userLat!!,userLong!!,altitude)
                    controller.setCenter(geoPoint)
                    controller.setZoom(13.toDouble())
                    val marker= Marker(binding.maps)
                    marker.position=geoPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM)
                    controller.setCenter(geoPoint)
                    binding.maps.overlays.add( marker)


                }

            }
        binding.maps.invalidate()
        return null
    }

    private fun showAllPharmacies(allPharmacies:List<Pharmacy>){

        for(pharmacy in allPharmacies)
        {
            showMarker(pharmacy)
        }
        binding.maps.invalidate()

    }


    private fun showMarker(pharmacy: Pharmacy,myTitle:String?=null) {

        val marker=Marker(binding.maps).apply {

            position=GeoPoint(pharmacy.longitude,pharmacy.latitude)
            setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM)
            if(pharmacy.isOpen!!){
                icon=resources.getDrawable(R.drawable.opened_pharmacy)
                title="ouverte"
            }
            else{
                icon=resources.getDrawable(R.drawable.closed_pharmacy)
                title="fermé"
            }
            binding.maps.overlays.add( this)

        }
        showPharmacyBottomSheet(marker = marker,pharmacy)


    }

    override fun onDataPassed(applyOpenFilter: Boolean, distance: Float, minimumEvaluation: Float) {
        val allPharmacies = viewmodel.listOfPharmacies
        selectedDistance=distance
        selectedRate=minimumEvaluation
        showOnlyopenedPharmacy=applyOpenFilter
        binding.maps.overlays.removeAll(binding.maps.overlays)
        binding.maps.invalidate()
        requestLocationUpdates()
        if(applyOpenFilter){
            for(pharmacie in allPharmacies)
            {
                if(pharmacie.isOpen==applyOpenFilter){

                    showMarker(pharmacie)

                }

                binding.maps.invalidate()

            }

        }
        else{
            for(pharmacie in allPharmacies)
            {

                    showMarker(pharmacie)

                binding.maps.invalidate()

            }
        }

        binding.maps.invalidate()
        var j=0
        for(i in 0 .. allPharmacies.lastIndex){
            Log.d("distance "+i,allPharmacies[i].longitude.toString())
            Log.d("distance "+i,allPharmacies[i].latitude.toString())
            val result= calculateDistanceBetweenUserAndPharmacy(
                lon1 = userLong!!.toDouble(),
                lat1 = userLat!!.toDouble()
                ,

                lon2 = allPharmacies[i].latitude,
                lat2 = allPharmacies[i].longitude


            )
            if(result>distance){
                binding.maps.overlays.remove(binding.maps.overlays[j])
                j--
            }
            j++
            binding.maps.invalidate()
        }


    }

    private  fun provideBarCodeLauncher(): ActivityResultLauncher<ScanOptions>
    {
        return registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (result.contents == null) {
                Handler(Looper.getMainLooper()).postDelayed({
                    // Your Code
                    Toasty.error(requireActivity(),"barcode n'est pas reconnue").show()

                }, 2000)

            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    // Your Code

                     showPharmaciesWhereMedicationAvailableMarker(viewmodel.listOfPharmacies[1],"disponible ici")
                    binding.searchMedication.endIconDrawable=requireActivity().getDrawable(R.drawable.ic_baseline_close_24)
                    isSearchIcon=false



                }, 2000)


                }

            }
        }


    }



