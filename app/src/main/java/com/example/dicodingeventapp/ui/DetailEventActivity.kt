package com.example.dicodingeventapp.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    companion object {
        const val EVENT_ITEM = "event_item"
    }

    private lateinit var binding: ActivityDetailEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val dataEvent = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EVENT_ITEM, ListEventsItem::class.java)

        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EVENT_ITEM)
        }

        dataEvent?.let { event ->
            binding.tvEventNameDetail.text = event.name
            binding.tvEventSummary.text = event.summary
            binding.tvEventOwnerName.text = event.ownerName
            binding.tvEventSisaKuotaNumber.text = (event.registrants?.let { event.quota?.minus(it) }).toString()
            binding.tvEventDescription.text = HtmlCompat.fromHtml(
                event.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.tvBeginTimeEvent.text = event.beginTime
            Glide.with(this)
                .load(event.mediaCover)  // Replace with your image URL
                .into(binding.imageEventMediaCover)
        }

    }
}