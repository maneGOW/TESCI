package com.manegow.tesci.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manegow.tesci.databinding.DifusionItemBinding
import com.manegow.tesci.db.DifusionNotes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class DifusionNoteAdapter(val difusionNoteTouchListener: DifusionNoteTouchListener) :
    ListAdapter<DifusionNotesDataItem, RecyclerView.ViewHolder>(DifusionNotesDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addDifusionNote(list: List<DifusionNotes>?) {
        adapterScope.launch {
            val notes = when (list) {
                null -> listOf(DifusionNotesDataItem.Header)
                else -> list.map { DifusionNotesDataItem.DifusionNotesItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(notes)
            }
        }
    }

    class ViewHolder private constructor(val binding: DifusionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: DifusionNoteTouchListener, item: DifusionNotes) {
            //binding.imageView3 = item.dayName
            binding.notes = item
            binding.txtDifusionTitle.text = item.noteTitle
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DifusionItemBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> DifusionNoteAdapter.ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown ViewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is DifusionNoteAdapter.ViewHolder -> {
                val noteItem = getItem(position) as DifusionNotesDataItem.DifusionNotesItem
                holder.bind(difusionNoteTouchListener, noteItem.difusionNotes)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DifusionNotesDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DifusionNotesDataItem.DifusionNotesItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
}

class DifusionNotesDiffCallback : DiffUtil.ItemCallback<DifusionNotesDataItem>() {
    override fun areItemsTheSame(oldItem: DifusionNotesDataItem, newItem: DifusionNotesDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: DifusionNotesDataItem, newItem: DifusionNotesDataItem): Boolean {
        return oldItem == newItem
    }
}

class DifusionNoteTouchListener(val clickListener: (noteId: Int) -> Unit) {
    fun onClick(difusionNotes: DifusionNotes) = clickListener(difusionNotes.noteId.toInt())
}

sealed class DifusionNotesDataItem {
    data class DifusionNotesItem(val difusionNotes: DifusionNotes) : DifusionNotesDataItem() {
        override val id = difusionNotes.noteId
    }
    object Header : DifusionNotesDataItem() {
        override val id = Long.MIN_VALUE
    }
    abstract val id: Long
}