package com.example.dicodingeventapp.ui.ui.finished

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.databinding.ItemRowEventABinding
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity.Companion.EVENT_ITEM

class ListFinishedEventAdapter :
    ListAdapter<Event, ListFinishedEventAdapter.EventFinishedViewHolder>(DIFF_CALLBACK) {
    class EventFinishedViewHolder(val binding: ItemRowEventABinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.tvEventName.text = event.name
            Glide.with(binding.imgEventImageLogo.context)
                .load(event.imageLogo)
                .into(binding.imgEventImageLogo)
        }
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventFinishedViewHolder {
        val binding = ItemRowEventABinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventFinishedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventFinishedViewHolder, position: Int) {
        val event = getItem(position)

        holder.bind(event)

        holder.itemView.setOnClickListener {

            val intentDetailEvent = Intent(holder.itemView.context, DetailEventActivity::class.java).apply {
                putExtra(EVENT_ITEM, event)
            }
            holder.itemView.context.startActivity(intentDetailEvent)
        }
    }

}
