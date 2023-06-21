package esprims.gi2.ma_pharmacie.presentation.donation.show_donation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.entity.Event
import esprims.gi2.ma_pharmacie.databinding.FragmentShowDonationBinding
import esprims.gi2.ma_pharmacie.presentation.donation.DonationFragmentListener
import esprims.gi2.ma_pharmacie.presentation.donation.EventAdapter
import esprims.gi2.ma_pharmacie.presentation.donation.FilterDonationFragment
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@AndroidEntryPoint
class ShowDonationFragment : Fragment(), DonationAdapterListener,DonationFragmentListener {
    private lateinit var timer: Timer
    lateinit var  callback:ViewPager2.OnPageChangeCallback
    val firstPoint :ImageView by lazy {
        requireActivity().findViewById<ImageView>(R.id.firstPoint)
    }
    val secondPoint:ImageView by lazy {
        requireActivity().findViewById<ImageView>(R.id.secondPoint)
    }

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }
    var currentPage = 0;

    private val showDonationViewModel: ShowDonationViewModel by viewModels()

    private val binding: FragmentShowDonationBinding by lazy {
        FragmentShowDonationBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback=MyPageChangeCallback()
        binding.viewPager.orientation=ViewPager2.ORIENTATION_HORIZONTAL
        val listOfEvents= mutableListOf<Event>(
            Event(
                title = "Cancer du sain",
             description = "Lundi à hopital bourguiba Monastir, participez à notre événement de don de sang et aidez à sauver des vies.",
                contact = "93317311",
                date = "28/12/1998"
            ),
            Event(
                title = "Cancer du fois",
                description = "Lundi à hopital bourguiba Monastir, participez à notre événement de don de sang et aidez à sauver des vies.",
                contact = "93317311",
                date = "28/12/2028"
            ),

        )
        val eventAdapter=EventAdapter(listOfEvents)
        binding.viewPager.adapter=eventAdapter

        val handler = Handler()
        val Update = Runnable {
            if (currentPage == listOfEvents.size) {
                currentPage = 0

            }
            if(currentPage==0)
            {
                firstPoint.setColorFilter(requireContext().getColor(R.color.dark_green))
                secondPoint.setColorFilter(requireContext().getColor(R.color.cadet_Blue))

            }
            else
            {
                secondPoint.setColorFilter(requireContext().getColor(R.color.dark_green))
                firstPoint.setColorFilter(requireContext().getColor(R.color.cadet_Blue))

            }

            binding.viewPager.setCurrentItem(currentPage++, true)
        }
        binding.viewPager.registerOnPageChangeCallback(callback!!)



         timer = Timer() // This will create a new Thread
        timer.schedule(object : TimerTask() { // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 5000)


        lifecycleScope.launch(Main)
        {
            showDonationViewModel.StateFlowOFOtherPeopleDonation.collectLatest {

                when(it)
                {
                    is UIState.Loading -> loadingDialog.showDialog()
                    is UIState.Success ->{
                        loadingDialog.hideDialog()
                        if (it.data!!.isEmpty())
                        {
                            binding.noItems.visibility= VISIBLE

                            setUpRecyclerView(listOf())
                        }
                        else{
                            setUpRecyclerView(it.data)
                            binding.noItems.visibility= INVISIBLE
                            binding.donationRv.adapter!!.notifyDataSetChanged()


                        }
                    }
                    is UIState.Error ->{
                        loadingDialog.hideDialog()
                        it.errorMessage?.let { it1 -> Toasty.error(requireContext(), it1) }
                    }
                }
            }
        }

        lifecycleScope.launch(Main)
        {
            showDonationViewModel.mutableStateFlowOfChoice1.collectLatest {
                when (it) {
                    0 -> {
                        showDonationViewModel.getDonations()
                        withContext(Main){
                            binding.title.text="Tout les dons"

                        }
                    }
                    2 -> {
                        lifecycleScope.launch(IO)
                        {
                            showDonationViewModel.getDonationByEmail("amiinemakhlouf@gmail.com")
                            withContext(Main){
                                binding.title.text="Mes dons"

                            }

                        }

                    }
                    1 -> lifecycleScope.launch(IO)
                    {
                        showDonationViewModel.getOtherPeopleDonation("amiinemakhlouf@gmail.com")
                        withContext(Main)
                        {
                            binding.title.text="Dons partagés par d'autres utilisateurs"

                        }


                    }
                }
            }
        }


        lifecycleScope.launch(Main)
        {
            showDonationViewModel.stateflowOfDonations.collectLatest {
                when (it) {
                    is UIState.Success -> {

                        if (it.data!!.isEmpty()) {
                            binding.noItems.visibility = VISIBLE
                        } else {
                            setUpRecyclerView(it.data)
                            binding.noItems.visibility= INVISIBLE
                            binding.donationRv.adapter!!.notifyDataSetChanged()

                        }
                    }

                    is UIState.Error -> {
                        Toasty.error(requireContext(), " un erreur est survenu.")
                    }
                }
            }
        }
        lifecycleScope.launch(Main)
        {


            showDonationViewModel.stateFlowOfDonationByEmail.collectLatest {

                when (it) {
                    is UIState.Loading -> {
                        loadingDialog.showDialog()
                    }
                    is UIState.Success -> {
                        loadingDialog.hideDialog()


                        if(it.data!!.isEmpty())
                        {
                            binding.noItems.visibility= VISIBLE
                        }
                        else{
                            binding.noItems.visibility= INVISIBLE

                        }

                        setUpRecyclerView(it.data!!)
                        binding.donationRv.adapter!!.notifyDataSetChanged()

                    }

                    else -> loadingDialog.hideDialog()
                }
            }

        }

        lifecycleScope.launch(Main)
        {showDonationViewModel.stateflowOfDonations.collectLatest {

            when(it)
            {
                is UIState.Loading ->{
                    loadingDialog.showDialog()
                }
                is UIState.Success ->{
                    loadingDialog.hideDialog()
                    setUpRecyclerView(it.data!!)
                    binding.donationRv.adapter!!.notifyDataSetChanged()

                }
                is UIState.Error ->{
                    loadingDialog.hideDialog()
                    it.errorMessage?.let { it1 -> Toasty.error(requireContext(), it1) }
                }
            }
        }
        }
        showBottomBar()
        handleBottomSheet(
        )
        binding.addDonation.setOnClickListener {

            timer.cancel()
            binding.viewPager.unregisterOnPageChangeCallback(callback!!)
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action =
                ShowDonationFragmentDirections.actionShowDonationFragmentToAddDonationFragment()
            navHostFragment.navController.navigate(action)
        }

    }

    private fun handleBottomSheet() {
        binding.filter.setOnClickListener {
            val filterDonationFragment = FilterDonationFragment(
                this,
                _checkedItem = showDonationViewModel.mutableStateFlowOfChoice.value
            )
            filterDonationFragment.show(requireActivity().supportFragmentManager, "n")
        }
    }

    private fun showBottomBar() {
        (requireActivity() as MainActivity).binding.fab.visibility = VISIBLE
        (requireActivity() as MainActivity).binding.bottomNavView.visibility = VISIBLE

    }

    private fun setUpRecyclerView(list: List<Donation>) {

        binding.donationRv.apply {

            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = DonationAdapter(dataset = list, this@ShowDonationFragment)
        }
    }

    override fun passDataToFragment(checkedItem: Int) {
        lifecycleScope.launch(Main)
        {
            showDonationViewModel.mutableStateFlowOfChoice.emit(checkedItem)


        }
    }

    override fun onclick(donation: Donation) {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action= ShowDonationFragmentDirections.actionShowDonationFragmentToDonationDetailsFragment(donation)
        navHostFragment.navController.navigate(action)
    }
    inner class MyPageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            println(state)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            println(position)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position == 0) {
                firstPoint.setColorFilter(requireContext().getColor(R.color.dark_green))
                secondPoint.setColorFilter(requireContext().getColor(R.color.cadet_Blue))
            } else {
                val firstPoint = requireActivity().findViewById<ImageView>(R.id.firstPoint)
                val secondPoint = requireActivity().findViewById<ImageView>(R.id.secondPoint)
                firstPoint.setColorFilter(requireContext().getColor(R.color.cadet_Blue))
                secondPoint.setColorFilter(requireContext().getColor(R.color.dark_green))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.viewPager.unregisterOnPageChangeCallback(callback)
    }
}