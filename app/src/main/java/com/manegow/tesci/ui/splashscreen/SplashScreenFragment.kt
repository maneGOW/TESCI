package com.manegow.tesci.ui.splashscreen

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.manegow.tesci.R
import com.manegow.tesci.databinding.FragmentSplashScreenBinding
import com.manegow.tesci.db.TesciDatabase


class SplashScreenFragment : Fragment() {

    private lateinit var splashscreenViewModel: SplashScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as DrawerLocker?)!!.setDrawerLocked(true)

        val bindingSplashScreen: FragmentSplashScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false)

        bindingSplashScreen.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val dataSource = TesciDatabase.getInstance(application).TesciDao()

        val viewModelFactory = SplashscreenViewModelFactory(dataSource, application)
        splashscreenViewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(SplashScreenViewModel::class.java)

        bindingSplashScreen.splashScreenViewModel = splashscreenViewModel

        splashscreenViewModel.navigateToMainScreen.observe(
            viewLifecycleOwner,
            Observer { navigate ->
                if (navigate) {
                    this.findNavController().navigate(
                        SplashScreenFragmentDirections
                            .actionSplashScreenFragmentToHomeFragment()
                    )
                    splashscreenViewModel.onMainScreenNavigated()
                }
            })

        splashscreenViewModel.navigateToLogin.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                this.findNavController().navigate(
                    SplashScreenFragmentDirections
                        .actionSplashScreenFragmentToNavLogin()
                )
                splashscreenViewModel.onLoginNavigated()
            }
        })

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        return bindingSplashScreen.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            context?.let {
                splashscreenViewModel.getLocalUser()
            }
        }, 2500)
    }

}