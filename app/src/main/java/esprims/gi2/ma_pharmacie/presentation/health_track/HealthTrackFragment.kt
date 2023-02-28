package esprims.gi2.ma_pharmacie.presentation.health_track

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentHealthTrackBinding


class HealthTrackFragment : Fragment() {
    private  lateinit var binding:FragmentHealthTrackBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHealthTrackBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_health_track, container, false)
    }

}