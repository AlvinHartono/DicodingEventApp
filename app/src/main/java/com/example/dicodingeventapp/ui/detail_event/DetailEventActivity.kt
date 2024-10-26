package com.example.dicodingeventapp.ui.detail_event

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.databinding.ActivityDetailEventBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailEventActivity : AppCompatActivity() {

    companion object {
        const val EVENT_ITEM = "event_item"
//        private const val TAG = "DetailEventActivity"
    }

    private lateinit var binding: ActivityDetailEventBinding
    private val viewModel by viewModels<DetailEventActivityViewModel> {
        DetailEventActivityViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.detailEventToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dataEvent = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EVENT_ITEM, Event::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EVENT_ITEM)
        }

        //from the intent, get the data and set it to the view
        binding.tvEventNameDetail.text = dataEvent?.name
        binding.tvEventOwnerName.text = dataEvent?.ownerName
        binding.tvEventSummary.text = dataEvent?.summary
        binding.tvEventSisaKuotaNumber.text = (dataEvent?.registrants.let {
            it?.let { it1 ->
                dataEvent?.quota?.minus(
                    it1
                )
            }
        }).toString()
        binding.tvBeginTimeEvent.text = dataEvent?.beginTime
        binding.tvEventDescription.text = HtmlCompat.fromHtml(
            dataEvent?.description.toString(),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        if (dataEvent!!.isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_outline)
        }
        Glide.with(this)
            .load(dataEvent.mediaCover)
            .into(binding.imageEventMediaCover)

        binding.btnEventRegister.setOnClickListener {
            when {
                dataEvent.link.isEmpty() -> {
                    Log.e("DetailEventActivity", "Event link is null or empty")
                }

                else -> {
                    val webpage: Uri = Uri.parse(dataEvent.link)
                    Log.d("DetailEventActivity", webpage.toString())
                    val browserIntent = Intent(Intent.ACTION_VIEW, webpage)
                    if (browserIntent.resolveActivity(packageManager) != null) {
                        startActivity(browserIntent)
                    } else {
                        Log.e("DetailEventActivity", "No app found to open this URL")
                    }
                }
            }
        }

        binding.fabFavorite.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch { // Launch a coroutine on the Main dispatcher
                val event = dataEvent
                if (event.isFavorite) {
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_outline)
                    viewModel.deleteEvent(event) // Call the suspend function
                } else {
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                    viewModel.saveEvent(event) // Call the suspend function
                }
            }

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