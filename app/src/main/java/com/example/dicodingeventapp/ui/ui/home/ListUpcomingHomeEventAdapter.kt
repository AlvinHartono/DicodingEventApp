package com.example.dicodingeventapp.ui.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.databinding.ItemRowEventABinding
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity.Companion.EVENT_ITEM

class ListUpcomingHomeEventAdapter :
    ListAdapter<ListEventsItem, ListUpcomingHomeEventAdapter.EventUpcomingHomeViewHolder>(
        DIFF_CALLBACK
    ) {
    class EventUpcomingHomeViewHolder(private val binding: ItemRowEventABinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            Glide.with(binding.imgEventImageLogo.context)
                .load(event.imageLogo)
                .into(binding.imgEventImageLogo)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventUpcomingHomeViewHolder {
        val binding = ItemRowEventABinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventUpcomingHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventUpcomingHomeViewHolder, position: Int) {
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