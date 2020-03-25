package com.manegow.tesci.ui.horarydetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.manegow.tesci.R
import com.manegow.tesci.databinding.FragmentHoraryDetailBinding
import com.manegow.tesci.db.TesciDatabase
import com.manegow.tesci.ui.horary.HoraryViewModelFactory
import com.manegow.tesci.utils.adapters.HoraryDetailAdapter
import com.manegow.tesci.utils.adapters.HoraryDetailTouchListener


class HoraryDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingHoraryDetail: FragmentHoraryDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_horary_detail, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TesciDatabase.getInstance(application).TesciDao()
        val arguments =  HoraryDetailFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = HoraryDetailViewModelFactory(dataSource, arguments.day, application)

        val horaryDetailViewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(HoraryDetailViewModel::class.java)

        bindingHoraryDetail.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 1)

        bindingHoraryDetail.rvHoraryDetail.layoutManager = manager

        val adapter = HoraryDetailAdapter(HoraryDetailTouchListener { horaryTouch ->
            horaryDetailViewModel.onHoraryClicked(horaryTouch.toInt())
        })

        bindingHoraryDetail.rvHoraryDetail.adapter = adapter

        horaryDetailViewModel.details.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHoraryDetailSubmitList(it)
            }
        })
        return bindingHoraryDetail.root
    }

}
