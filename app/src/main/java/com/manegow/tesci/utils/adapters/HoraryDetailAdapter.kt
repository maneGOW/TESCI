package com.manegow.tesci.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manegow.tesci.databinding.HoraryDetailItemBinding
import com.manegow.tesci.db.Days
import com.manegow.tesci.db.HoraryDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class HoraryDetailAdapter(val horaryDetailTouchListener: HoraryDetailTouchListener) :
    ListAdapter<HoraryDetailDataItem, RecyclerView.ViewHolder>(
        HoraryDetailDiffCallback()
    ) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHoraryDetailSubmitList(list: List<HoraryDetail>?) {
        adapterScope.launch {
            val horaryDetail = when (list) {
                null -> listOf(HoraryDetailDataItem.Header)
                else -> list.map { HoraryDetailDataItem.HoraryDetailItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(horaryDetail)
            }
        }
    }

    class ViewHolder private constructor(val binding: HoraryDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: HoraryDetailTouchListener, item: HoraryDetail) {
            binding.txtHorario.text = "De ${item.horaryTimeStart} a ${item.horaryTimeEnd}"
            binding.txtMateria.text = item.horaryNameSignature
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HoraryDetailItemBinding.inflate(layoutInflater)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown ViewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                val horaryDetailItem = getItem(position) as HoraryDetailDataItem.HoraryDetailItem
                holder.bind(horaryDetailTouchListener, horaryDetailItem.horaryDetail)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HoraryDetailDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is HoraryDetailDataItem.HoraryDetailItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
}

class HoraryDetailDiffCallback : DiffUtil.ItemCallback<HoraryDetailDataItem>() {
    override fun areItemsTheSame(oldItem: HoraryDetailDataItem, newItem: HoraryDetailDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: HoraryDetailDataItem, newItem: HoraryDetailDataItem): Boolean {
        return oldItem == newItem
    }
}

class HoraryDetailTouchListener(val clickListener: (horaryDetailId: String) -> Unit) {
    fun onClick(horaryDetail: HoraryDetail) = clickListener(horaryDetail.horaryDetailId.toString())
}

sealed class HoraryDetailDataItem {
    data class HoraryDetailItem(val horaryDetail: HoraryDetail) : HoraryDetailDataItem() {
        override val id = horaryDetail.horaryDetailId
    }
    object Header : HoraryDetailDataItem() {
        override val id = Long.MIN_VALUE
    }
    abstract val id: Long
}