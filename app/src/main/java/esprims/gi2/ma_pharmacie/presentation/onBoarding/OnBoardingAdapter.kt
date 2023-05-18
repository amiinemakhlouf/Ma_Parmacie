package esprims.gi2.ma_pharmacie.presentation.onBoarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class OnBoardingAdapter( fragment:FragmentManager,
 lifecycle: Lifecycle,
 val fragmentList:List<Fragment>,
) : FragmentStateAdapter(fragment,lifecycle) {
    override fun getItemCount()= fragmentList.size

    override fun createFragment(position: Int): Fragment {

        return   fragmentList[position]
    }

}