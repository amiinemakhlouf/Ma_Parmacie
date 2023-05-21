package esprims.gi2.ma_pharmacie.presentation.pharmacie

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.MotionEvent.ACTION_MOVE
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import esprims.gi2.ma_pharmacie.BuildConfig
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentPharmacyBinding
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.config.IConfigurationProvider
import org.osmdroid.tileprovider.util.StorageUtils.getStorage
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class PharmacyFragment : Fragment(),PinchSaceListenner {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private  val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

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
        returnToPreviousFragment()
        handlePinchGestureToZoomIn()
        val list= getListOfPharmacies()
        showAllPharmacies(list)
        requestPermissionLauncher = getRequestPermissionLauncher()
        getUserLocation()
        showFilterBottomSheet()



    }



    private fun showFilterBottomSheet() {
        binding.filter.setOnClickListener {
            val pharmacyFilterBottomSheet =PharmacyFilterBottomSheet()
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

    private fun getListOfPharmacies(): List<PharmacyModel> {
         return listOf<PharmacyModel>(PharmacyModel(longitude = .723074, latitude = 10.953292, isOpen = true, address = "Bessatine",
            rate = 2.5f, phoneNumber = "93317311", name = "Ben slama"
        ),
            PharmacyModel(longitude = 35.4496846, latitude = 10.825411, isOpen = false, address = "Stah jaber",name = "Bouzgarrou",
                rate = 4.5f, phoneNumber = "73555666"),
            PharmacyModel(longitude =  35.511241, latitude = 10.806337, isOpen = true,address = "omrane",name = "Jilani",
                rate = 1.5f, phoneNumber = "73222122"),
            PharmacyModel(longitude =  35.61893, latitude = 10.810091, isOpen = true,address = "skanes", name = "jawhari",
                rate = 1.5f, phoneNumber = "70111222"),
            PharmacyModel(longitude =  35.714239, latitude = 10.808729, isOpen = false,address = "bni khiar", name = "Dhifallah",
                rate = 5f, phoneNumber = "21414516"),


            )
    }

    private fun focusOnCenter() {


        val point=GeoPoint(35.7603745.toDouble(),10.8112995)

        controller.setCenter(point)

        controller.setZoom(10.toDouble())
    }

    private fun showPharmacyBottomSheet(marker: Marker,pharmacyModel: PharmacyModel) {

        marker.setOnMarkerClickListener { marker, mapView ->

            val pharmacyBottomSheet= PharmacyBottomSheet()
            val args = Bundle().apply {
                putString("name", pharmacyModel.name)
                putString("address", pharmacyModel.address)
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

                showDialogToInformUserTheNeedToLocationPermission()

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

    private  fun returnToPreviousFragment(){
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }
    private fun setUpMap() {
        val provider: IConfigurationProvider = Configuration.getInstance()
        provider.userAgentValue = BuildConfig.APPLICATION_ID
        provider.osmdroidBasePath = getStorage()
        provider.osmdroidTileCache = getStorage()
    }



    private fun requestLocationUpdates() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val altitude = location.altitude
                    val geoPoint=GeoPoint(latitude,longitude,altitude)
                    controller.setCenter(geoPoint)
                    controller.setZoom(10.toDouble())
                    val marker= Marker(binding.maps)
                    marker.position=geoPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM)
                    controller.setCenter(geoPoint)
                    binding.maps.overlays.add( marker)
                    marker.title="vous etes ici"

                }
            }
    }

    private fun showAllPharmacies(allPharmacies:List<PharmacyModel>){

        for(pharmacy in allPharmacies)
        {
            showMarker(pharmacy)
        }

    }


    private fun showMarker(pharmacyModel: PharmacyModel) {

        val marker=Marker(binding.maps).apply {

            position=GeoPoint(pharmacyModel.longitude,pharmacyModel.latitude)
            setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM)
            if(pharmacyModel.isOpen!!){
                icon=resources.getDrawable(R.drawable.opened_pharmacy)
                title="ouverte"
            }
            else{
                icon=resources.getDrawable(R.drawable.closed_pharmacy)
                title="ouverte"
            }
            binding.maps.overlays.add( this)

        }
        showPharmacyBottomSheet(marker = marker,pharmacyModel)


    }


}