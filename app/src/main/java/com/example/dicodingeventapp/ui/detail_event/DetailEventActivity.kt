package com.example.dicodingeventapp.ui.detail_event

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    companion object {
        const val EVENT_ITEM = "event_item"
        const val TAG = "DetailEventActivity"
    }

    private lateinit var viewModel: DetailEventActivityViewModel
    private lateinit var binding: ActivityDetailEventBinding

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.detailEventToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Extract event data from the Intent
        val dataEvent = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EVENT_ITEM, ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EVENT_ITEM)
        }

        if (dataEvent?.id != null) {
            val eventId = dataEvent.id

            // Use Factory to pass parameter to ViewModel
            val factory = DetailEventActivityViewModelFactory(eventId)
            viewModel =
                ViewModelProvider(this, factory)[DetailEventActivityViewModel::class.java]


            viewModel.event.observe(this) { event ->
                binding.detailEventToolbar.title = event.name
                binding.tvEventNameDetail.text = event.name
                binding.tvEventSummary.text = event.summary
                binding.tvEventOwnerName.text = event.ownerName
                binding.tvEventSisaKuotaNumber.text =
                    (event.registrants?.let { event.quota?.minus(it) }).toString()
                binding.tvEventDescription.text = HtmlCompat.fromHtml(
                    event.description.toString(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                binding.tvBeginTimeEvent.text = event.beginTime
                Glide.with(this)
                    .load(event.mediaCover)
                    .into(binding.imageEventMediaCover)

                binding.btnEventRegister.setOnClickListener {
                    if (event.link.isNullOrEmpty()) {
                        Log.e(TAG, "Event link is null or empty")
                    } else {
                        val webpage: Uri = Uri.parse(event.link)
                        Log.d(TAG, webpage.toString())
                        val browserIntent = Intent(Intent.ACTION_VIEW, webpage)
//                        startActivity(browserIntent)
                        if (browserIntent.resolveActivity(packageManager) != null) {
                            startActivity(browserIntent)
                        } else {
                            Log.e(TAG, "No app found to open this URL")
                        }
                    }
                }

            }
        } else {
            // TODO Handle the case where dataEvent is null or id is null
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}