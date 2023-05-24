package esprims.gi2.ma_pharmacie.presentation.pharmacie


fun calculateDistanceBetweenUserAndPharmacy(userLocation: Location ,pharmacieLocation: Location ):Float
{
    val location1 = android.location.Location("")
    location1.latitude = userLocation.latitude.toDouble()
    location1.longitude = userLocation.longitude.toDouble()

    val location2 = android.location.Location("")
    location2.latitude = pharmacieLocation.latitude.toDouble()
    location2.longitude = pharmacieLocation.longitude.toDouble()

    return location1.distanceTo(location2)

}