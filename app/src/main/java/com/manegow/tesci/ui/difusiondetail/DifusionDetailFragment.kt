package com.manegow.tesci.ui.difusiondetail

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
import com.manegow.tesci.databinding.FragmentDifusionDetailBinding
import com.manegow.tesci.db.TesciDatabase
import com.manegow.tesci.ui.horarydetail.HoraryDetailFragmentArgs

class DifusionDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingDifusionDetail:  FragmentDifusionDetailBinding=
            DataBindingUtil.inflate(inflater, R.layout.fragment_difusion_detail, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TesciDatabase.getInstance(application).TesciDao()
        val arguments =  DifusionDetailFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory = DifusionDetailViewModelFactory(dataSource, arguments.noteId.toInt(), application)

        val difusionDetailViewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(DifusionDetailViewModel::class.java)

        bindingDifusionDetail.lifecycleOwner = this

        difusionDetailViewModel.noteData.observe(viewLifecycleOwner, Observer {
            it?.let {
                bindingDifusionDetail.txtNoteTitle.text = it.noteTitle
                bindingDifusionDetail.txtNoteBody.text = it.noteBody
            }
        })
        return bindingDifusionDetail.root
    }
}
