package esprims.gi2.ma_pharmacie.presentation.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import esprims.gi2.ma_pharmacie.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {
    private  lateinit var  binding:ActivityAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}