package esprims.gi2.ma_pharmacie

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import esprims.gi2.ma_pharmacie.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val image = resources.getDrawable(R.drawable.code2).toBitmap()

        binding.scanButton.setOnClickListener {
          barcodeLauncher.launch(ScanOptions())

        }
    }

    val barcodeLauncher = registerForActivityResult(ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@MainActivity,
                result.contents,
                Toast.LENGTH_LONG).show()
            Log.d("my result",result.contents)
        }

}}