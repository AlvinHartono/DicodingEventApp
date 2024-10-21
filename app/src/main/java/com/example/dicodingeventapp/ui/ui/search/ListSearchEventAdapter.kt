package com.example.dicodingeventapp.ui.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.databinding.ItemRowEventSearchBinding
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity.Companion.EVENT_ITEM

class ListSearchEventAdapter :
    ListAdapter<Event, ListSearchEventAdapter.SearchEventViewHolder>(DIFF_CALLBACK) {

    // ViewHolder
    class SearchEventViewHolder(private val binding: ItemRowEventSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventNameSearch.text = event.name

            Glide.with(binding.imgEventImageLogo.context)
                .load(event.imageLogo)
                .into(binding.imgEventImageLogo)
        }

    }
    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchEventViewHolder {
        val binding = ItemRowEventSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchEventViewHolder(binding)
    }

    // Bind ViewHolder
    override fun onBindViewHolder(holder: SearchEventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {

            val intentDetailEvent = Intent(holder.itemView.context, DetailEventActivity::class.java).apply {
                putExtra(EVENT_ITEM, event)
            }
            holder.itemView.context.startActivity(intentDetailEvent)
        }
    }

    // Get Item Count
    override fun getItemCount(): Int {
        return currentList.size
    }

    // DiffUtil
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(
                oldItem: Event,
                newItem: Event
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Event,
                newItem: Event
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}