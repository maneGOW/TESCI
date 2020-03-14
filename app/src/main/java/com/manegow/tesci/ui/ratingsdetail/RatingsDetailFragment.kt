package com.manegow.tesci.ui.ratingsdetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manegow.tesci.R


class RatingsDetailFragment : Fragment() {

    companion object {
        fun newInstance() =
            RatingsDetailFragment()
    }

    private lateinit var viewModel: RatingsDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ratings_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RatingsDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
