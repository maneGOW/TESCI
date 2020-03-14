package com.manegow.tesci.ui.ratings

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
import com.manegow.tesci.databinding.FragmentRatingsBinding
import com.manegow.tesci.db.TesciDatabase
import com.manegow.tesci.utils.adapters.SemesterAdapter
import com.manegow.tesci.utils.adapters.SemesterTouchListener


class RatingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val bindingMovements: FragmentRatingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_ratings, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TesciDatabase.getInstance(application).TesciDao()
        val viewModelFactory = RatingsViewModelFactory(dataSource, application)

        val ratingsViewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(RatingsViewModel::class.java)

        bindingMovements.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 1)

        bindingMovements.recyclerSemester.layoutManager = manager

        /* ratingsViewModel..observe(viewLifecycleOwner, Observer{
                 movement ->
             movement?.let{
                 this.findNavController().navigate(
                     MovementsFragmentDirections.actionMovementsFragmentToPaymentDetailFragment(movement))
                 movementsViewModel.onMovementNavigated()
             }
         })
         */

        val adapter = SemesterAdapter(SemesterTouchListener { semesterId ->
            ratingsViewModel.onSemesterClicked(semesterId)
        })

        bindingMovements.recyclerSemester.adapter = adapter

        ratingsViewModel.semesters.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addSemesterSubmitList(it)
            }
        })
        return bindingMovements.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}