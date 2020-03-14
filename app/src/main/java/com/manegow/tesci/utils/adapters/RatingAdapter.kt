package com.manegow.tesci.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manegow.tesci.databinding.RatingsDetailItemBinding
import com.manegow.tesci.databinding.SemesterItemBinding
import com.manegow.tesci.db.Rating
import com.manegow.tesci.db.Semester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class RatingAdapter(val ratingTouchListener: RatingTouchListener) :
    ListAdapter<RatingDataItem, RecyclerView.ViewHolder>(RatingDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addSemesterSubmitList(list: List<Rating>?) {
        adapterScope.launch {
            val ratings = when (list) {
                null -> listOf(RatingDataItem.Header)
                else -> list.map { RatingDataItem.RatingItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(ratings)
            }
        }
    }

    class ViewHolder private constructor(val binding: RatingsDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: RatingTouchListener, item: Rating) {
            binding.txtName.text = item.ratingName
            binding.txtProm.text = item.ratingScore.toString()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RatingsDetailItemBinding.inflate(layoutInflater)
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
                val ratingItem = getItem(position) as RatingDataItem.RatingItem
                holder.bind(ratingTouchListener, ratingItem.rating)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RatingDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is RatingDataItem.RatingItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
}

class RatingDiffCallback : DiffUtil.ItemCallback<RatingDataItem>() {
    override fun areItemsTheSame(oldItem: RatingDataItem, newItem: RatingDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: RatingDataItem, newItem: RatingDataItem): Boolean {
        return oldItem == newItem
    }
}

class RatingTouchListener(val clickListener: (ratingId: String) -> Unit) {
    fun onClick(rating: Rating) = clickListener(rating.ratingId.toString())
}

sealed class RatingDataItem {
    data class RatingItem(val rating: Rating) : RatingDataItem() {
        override val id = rating.ratingId
    }
    object Header : RatingDataItem() {
        override val id = Long.MIN_VALUE
    }
    abstract val id: Long
}