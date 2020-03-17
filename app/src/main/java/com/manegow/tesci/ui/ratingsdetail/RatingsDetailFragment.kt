package com.manegow.tesci.ui.ratingsdetail

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
import com.manegow.tesci.databinding.FragmentRatingsDetailBinding
import com.manegow.tesci.db.TesciDatabase
import com.manegow.tesci.utils.adapters.RatingAdapter
import com.manegow.tesci.utils.adapters.RatingTouchListener


class RatingsDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingRatingDetail: FragmentRatingsDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_ratings_detail, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TesciDatabase.getInstance(application).TesciDao()
        val arguments =  RatingsDetailFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = RatingsViewModelFactory(dataSource, arguments.semester.toLong() , application)

        val ratingsViewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(RatingsDetailViewModel::class.java)

        bindingRatingDetail.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 1)

        bindingRatingDetail.rvRatingsDetail.layoutManager = manager

       /* ratingsViewModel.navigateToRatingsDetail.observe(viewLifecycleOwner, Observer { semester ->
            semester?.let {
                println("Semester $semester")
                this.findNavController().navigate(
                    RatingsFragmentDirections.actionRatingsFragmentToRatingsDetailFragment(semester)
                )
                ratingsViewModel.onMovementNavigated()
            }
        })*/

        val adapter = RatingAdapter(RatingTouchListener { ratingId ->
            ratingsViewModel.onRatingClicked(ratingId.toInt())
        })

        bindingRatingDetail.rvRatingsDetail.adapter = adapter

        ratingsViewModel.ratings.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addSemesterSubmitList(it)
            }
        })
        return bindingRatingDetail.root
    }
}
