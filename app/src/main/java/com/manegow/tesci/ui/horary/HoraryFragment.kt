package com.manegow.tesci.ui.horary

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.manegow.tesci.R
import com.manegow.tesci.databinding.FragmentHoraryBinding
import com.manegow.tesci.db.TesciDatabase
import com.manegow.tesci.utils.adapters.DayAdapter
import com.manegow.tesci.utils.adapters.DayTouchListener

class HoraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingHorary: FragmentHoraryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_horary, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TesciDatabase.getInstance(application).TesciDao()
        val viewModelFactory = HoraryViewModelFactory(dataSource, application)

        val horaryViewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(HoraryViewModel::class.java)

        bindingHorary.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 1)

        bindingHorary.rvDays.layoutManager = manager

        horaryViewModel.navigateToHoraryDetail.observe(viewLifecycleOwner, Observer { day ->
            day?.let {
                println("Day data ------ $day")
                this.findNavController().navigate(
                    HoraryFragmentDirections.actionNavHoraryToNavHorarydetail(day)
                )
                horaryViewModel.onDayNavigated()
            }
        })

        val adapter = DayAdapter(DayTouchListener { dayId ->
            horaryViewModel.onDayClicked(dayId)
        })

        bindingHorary.rvDays.adapter = adapter

        horaryViewModel.days.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addDaySubmitList(it)
            }
        })
        return bindingHorary.root
    }
}