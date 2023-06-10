package esprims.gi2.ma_pharmacie.presentation.donation

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class AddDonationViewModel @Inject constructor(

):ViewModel() {

    var imageUri1:Uri?=null
    var imageUri2:Uri?=null
    var imageView1:ImageView?=null
    var imageView2:ImageView?=null
    var imageLauncherDispacher=0

}