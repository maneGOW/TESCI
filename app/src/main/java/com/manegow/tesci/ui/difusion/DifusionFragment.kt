package com.manegow.tesci.ui.difusion

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
import com.manegow.tesci.databinding.FragmentDifusionBinding
import com.manegow.tesci.databinding.FragmentHoraryBinding
import com.manegow.tesci.db.TesciDatabase
import com.manegow.tesci.ui.horary.HoraryFragmentDirections
import com.manegow.tesci.ui.horary.HoraryViewModel
import com.manegow.tesci.ui.horary.HoraryViewModelFactory
import com.manegow.tesci.utils.adapters.DayAdapter
import com.manegow.tesci.utils.adapters.DayTouchListener
import com.manegow.tesci.utils.adapters.DifusionNoteAdapter
import com.manegow.tesci.utils.adapters.DifusionNoteTouchListener

class DifusionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingDifusion: FragmentDifusionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_difusion, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TesciDatabase.getInstance(application).TesciDao()
        val viewModelFactory = DifusionViewModelFactory(dataSource, application)

        val difusionViewModel = ViewModelProviders.of(
            this, viewModelFactory
        ).get(DifusionViewModel::class.java)

        bindingDifusion.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 2)

        bindingDifusion.rvDifusion.layoutManager = manager

        difusionViewModel.navigateToDifusionNoteDetail.observe(viewLifecycleOwner, Observer { note ->
            note?.let {
                println("note data ------ $note")
                this.findNavController().navigate(
                    DifusionFragmentDirections.actionNavDifusionToDifusionDetailFragment(note)
                )
                difusionViewModel.onNoteNavigated()
            }
        })

        val adapter = DifusionNoteAdapter(DifusionNoteTouchListener { noteId ->
            difusionViewModel.onNoteClicked(noteId.toLong())
        })

        bindingDifusion.rvDifusion.adapter = adapter

        difusionViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addDifusionNote(it)
            }
        })
        return bindingDifusion.root
    }
}
