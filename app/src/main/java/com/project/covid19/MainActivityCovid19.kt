package com.project.covid19

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivityCovid19 : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchFragmentInjector: DispatchingAndroidInjector<Fragment>
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.navController = Navigation.findNavController(this, R.id.container)
        this.navController.setGraph(R.navigation.covid19_nav_layout)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return this.dispatchFragmentInjector
    }
}
