package esprims.gi2.ma_pharmacie.presentation.reminder_detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddReminderBinding
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderBinding
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderDetailsBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder.AddReminderDaysAdapter
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackButtonClicked
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder.AddReminderDaysAdapter.DayListener as DayListener

class ReminderDetailsFragment : Fragment(),DayListener {

    private lateinit var binding: FragmentAddReminderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddReminderBinding.inflate(layoutInflater)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSystemBackButtonClicked(this)
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed();

        }
        binding.NameETT.setText("Jhon")
        showFirstReminderTime()
        showSecondReminderTime()
        showThirdReminderTime()
        setUpDaysRv()
        handleSelectAllDays()


        // Access and manipulate the child view
        // Example: childView?.findViewById<TextView>(R.id.textView)?.text = "New Text"


    }

    private fun showSecondReminderTime() {
        binding.second.visibility = VISIBLE
        binding.secondTime.visibility = VISIBLE
        binding.secondTrash.visibility = VISIBLE
    }

    private fun setUpDaysRv() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val myDataList = listOf<String>("lun", "mar", "mer", "jeu", "ven", "sam", "dim")
        val adapter = AddReminderDaysAdapter(myDataList, this)

        binding.daysRv.adapter = adapter
        binding.daysRv.adapter?.notifyDataSetChanged()

        //val cardView = binding.daysRv.getChildAt(0) as MaterialCardView
        // cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))


    }

    private fun showThirdReminderTime() {
        binding.third.visibility = VISIBLE
        binding.thirdTime.visibility = VISIBLE
        binding.thirdTrash.visibility = VISIBLE
    }

    private fun showFirstReminderTime() {
        binding.first.visibility = VISIBLE
        binding.firstTime.visibility = VISIBLE
        binding.firstTrash.visibility = VISIBLE
    }

    override fun onDayClick(position: Int) {
        val adapter = binding.daysRv.adapter as AddReminderDaysAdapter

        val cardView = binding.daysRv.getChildAt(position) as MaterialCardView
        cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))


    }
    private fun handleSelectAllDays() {
        binding.daysSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->



                for (i in 0 until binding.daysRv.childCount) {
                    val cardView = binding.daysRv.getChildAt(i) as MaterialCardView
                    cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))

                }

            })

    }

    /*override fun onResume() {
        super.onResume()
        val cardView = binding.daysRv.getChildAt(1) as MaterialCardView
        cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))

    }*/
}







/* binding.doseValue.inputType=InputType.TYPE_NULL
       binding.programValue.isEnabled=false
       binding.quantityValue.isEnabled=false
       binding.editDose.setOnClickListener {
           binding.doseValue.inputType=InputType.TYPE_CLASS_TEXT
           binding.doseValue.requestFocus()
           binding.doseValue.isCursorVisible=true

       }
       binding.editProgram.setOnClickListener {
           binding.programValue.isEnabled=true
           binding.programValue.requestFocus()
           binding.programValue.isCursorVisible=true
       }
       binding.modify.setOnClickListener {
          val loadingDialog= LoadingDialog(requireActivity())
           loadingDialog.showDialog()
           it.isEnabled=false
           lifecycleScope.launch(IO){
               delay(500)
               withContext(Main){
                   loadingDialog.hideDialog()
                   Toasty.success(requireActivity(),"Informations mises à jour avec succès").show()
                   it.isEnabled=true

               }
           }
       }
       /*binding.editQuantity.setOnClickListener {
           binding.quantityValue.inputType=InputType.TYPE_CLASS_TEXT
           binding.quantityValue.requestFocus()
           binding.quantityValue.isCursorVisible=true
       }*/*/

