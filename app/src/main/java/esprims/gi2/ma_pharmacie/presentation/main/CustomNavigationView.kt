package esprims.gi2.ma_pharmacie.presentation.main

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import esprims.gi2.ma_pharmacie.R

class CustomNavigationView : NavigationView {
     constructor(context: Context):super(context)
    constructor(context: Context,st:AttributeSet,ds:Int) :super(context,st,ds)

    constructor(context: Context,st:AttributeSet) : super(context,st) {
        // some code
    }


    override fun setNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener?) {
        super.setNavigationItemSelectedListener(listener)



    }

}