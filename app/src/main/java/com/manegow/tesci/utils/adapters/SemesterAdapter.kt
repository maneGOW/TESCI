package com.manegow.tesci.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manegow.tesci.databinding.SemesterItemBinding
import com.manegow.tesci.db.Semester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class SemesterAdapter(val semesterTouchListener: SemesterTouchListener) :
    ListAdapter<SemesterDataItem, RecyclerView.ViewHolder>(SemesterDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addSemesterSubmitList(list: List<Semester>?) {
        adapterScope.launch {
            val semesters = when (list) {
                null -> listOf(SemesterDataItem.Header)
                else -> list.map { SemesterDataItem.SemesterItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(semesters)
            }
        }
    }

    class ViewHolder private constructor(val binding: SemesterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: SemesterTouchListener, item: Semester) {
            binding.txtName.text = item.semesterNumber.toString()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SemesterItemBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown ViewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val semesterItem = getItem(position) as SemesterDataItem.SemesterItem
                holder.bind(semesterTouchListener, semesterItem.semester)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SemesterDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is SemesterDataItem.SemesterItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
}

class SemesterDiffCallback : DiffUtil.ItemCallback<SemesterDataItem>() {
    override fun areItemsTheSame(oldItem: SemesterDataItem, newItem: SemesterDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: SemesterDataItem, newItem: SemesterDataItem): Boolean {
        return oldItem == newItem
    }
}

class SemesterTouchListener(val clickListener: (semesterId: String) -> Unit) {
    fun onClick(semester: Semester) = clickListener(semester.semesterId.toString())
}

sealed class SemesterDataItem {
    data class SemesterItem(val semester: Semester) : SemesterDataItem() {
        override val id = semester.semesterId
    }
    object Header : SemesterDataItem() {
        override val id = Long.MIN_VALUE
    }
    abstract val id: Long
}