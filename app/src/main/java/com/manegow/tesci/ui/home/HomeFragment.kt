package com.manegow.tesci.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.manegow.tesci.R
import com.manegow.tesci.ui.splashscreen.DrawerLocker

class HomeFragment : Fragment() {

    private lateinit var galleryViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as DrawerLocker?)!!.setDrawerLocked(false)

        galleryViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }
}
