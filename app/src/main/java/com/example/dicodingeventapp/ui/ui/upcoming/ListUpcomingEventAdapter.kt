package com.example.dicodingeventapp.ui.ui.upcoming

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.databinding.ItemRowEventBBinding
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity
import com.example.dicodingeventapp.ui.detail_event.DetailEventActivity.Companion.EVENT_ITEM

class ListUpcomingEventAdapter(private val onFavoriteClick: (Event) -> Unit) :
    ListAdapter<Event, ListUpcomingEventAdapter.EventViewHolder>(DIFF_CALLBACK) {
    class EventViewHolder(val binding: ItemRowEventBBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.tvEventName.text = event.name
            Glide.with(binding.imgEventMediaCover.context)
                .load(event.mediaCover)
                .into(binding.imgEventMediaCover)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            ItemRowEventBBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)

        holder.bind(event)
        val isFavorite = holder.binding.imgFavoriteB
        if (event.isFavorite) {
            isFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            isFavorite.setImageResource(R.drawable.ic_favorite_outline)
        }

        isFavorite.setOnClickListener {
            onFavoriteClick(event)
        }

        holder.itemView.setOnClickListener {

            val intentDetailEvent = Intent(holder.itemView.context, DetailEventActivity::class.java).apply {
                putExtra(EVENT_ITEM, event)
            }
            holder.itemView.context.startActivity(intentDetailEvent)
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


}