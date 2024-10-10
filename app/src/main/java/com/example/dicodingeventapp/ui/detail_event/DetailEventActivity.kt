package com.example.dicodingeventapp.ui.detail_event

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
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

                viewModel.isLoading.observe(this) {
                    showLoading(it)
                }
            }
        } else {
            // TODO Handle the case where dataEvent is null or id is null
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.guidelineEventDetail.visibility = View.INVISIBLE
            binding.tvEventNameDetail.visibility = View.INVISIBLE
            binding.tvEventOwnerName.visibility = View.INVISIBLE
            binding.tvEventSummary.visibility = View.INVISIBLE
            binding.tvEventSisaKuota.visibility = View.INVISIBLE
            binding.tvEventSisaKuotaNumber.visibility = View.INVISIBLE
            binding.tvBeginTimeEvent.visibility = View.INVISIBLE
            binding.tvEventDescription.visibility = View.INVISIBLE
            binding.btnEventRegister.visibility = View.INVISIBLE
            binding.imageEventMediaCover.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.guidelineEventDetail.visibility = View.VISIBLE
            binding.tvEventNameDetail.visibility = View.VISIBLE
            binding.tvEventOwnerName.visibility = View.VISIBLE
            binding.tvEventSummary.visibility = View.VISIBLE
            binding.tvEventSisaKuota.visibility = View.VISIBLE
            binding.tvEventSisaKuotaNumber.visibility = View.VISIBLE
            binding.tvBeginTimeEvent.visibility = View.VISIBLE
            binding.tvEventDescription.visibility = View.VISIBLE
            binding.btnEventRegister.visibility = View.VISIBLE
            binding.imageEventMediaCover.visibility = View.VISIBLE

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