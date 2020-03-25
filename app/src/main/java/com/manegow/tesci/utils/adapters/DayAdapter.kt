package com.manegow.tesci.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manegow.tesci.databinding.DayItemBinding
import com.manegow.tesci.db.Days
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class DayAdapter(val dayTouchListener: DayTouchListener) :
    ListAdapter<DaysDataItem, RecyclerView.ViewHolder>(DayDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addDaySubmitList(list: List<Days>?) {
        adapterScope.launch {
            val days = when (list) {
                null -> listOf(DaysDataItem.Header)
                else -> list.map { DaysDataItem.DayItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(days)
            }
        }
    }

    class ViewHolder private constructor(val binding: DayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: DayTouchListener, item: Days) {
            binding.textView7.text = item.dayName
            binding.days = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DayItemBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> DayAdapter.ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown ViewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is DayAdapter.ViewHolder -> {
                val dayItem = getItem(position) as DaysDataItem.DayItem
                holder.bind(dayTouchListener, dayItem.days)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DaysDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DaysDataItem.DayItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
}

class DayDiffCallback : DiffUtil.ItemCallback<DaysDataItem>() {
    override fun areItemsTheSame(oldItem: DaysDataItem, newItem: DaysDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: DaysDataItem, newItem: DaysDataItem): Boolean {
        return oldItem == newItem
    }
}

class DayTouchListener(val clickListener: (dayId: String) -> Unit) {
    fun onClick(days: Days) = clickListener(days.dayName.toString())
}

sealed class DaysDataItem {
    data class DayItem(val days: Days) : DaysDataItem() {
        override val id = days.dayName
    }
    object Header : DaysDataItem() {
        override val id = ""
    }
    abstract val id: String
}