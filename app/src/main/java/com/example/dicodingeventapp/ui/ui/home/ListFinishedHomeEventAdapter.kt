package com.example.dicodingeventapp.ui.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.databinding.ItemRowEventBBinding
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity.Companion.EVENT_ITEM

class ListFinishedHomeEventAdapter: ListAdapter<ListEventsItem, ListFinishedHomeEventAdapter.EventFinishedHomeViewHolder>(DIFF_CALLBACK) {
    class EventFinishedHomeViewHolder(private val binding: ItemRowEventBBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            Glide.with(binding.imgEventMediaCover.context)
                .load(event.imageLogo)
                .into(binding.imgEventMediaCover)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventFinishedHomeViewHolder {
        val binding = ItemRowEventBBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventFinishedHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventFinishedHomeViewHolder, position: Int) {
        val event = getItem(position)

        holder.bind(event)
        holder.itemView.setOnClickListener{
            val intentDetailEvent = Intent(holder.itemView.context, DetailEventActivity::class.java).apply {
                putExtra(EVENT_ITEM, event)
            }
            holder.itemView.context.startActivity(intentDetailEvent)
        }
    }
}