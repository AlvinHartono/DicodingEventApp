package com.example.dicodingeventapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.response.EventResponse
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.data.retrofit.ApiConfig
import com.example.dicodingeventapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

//    companion object {
//        private const val TAG = "MainActivity"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvEvent.addItemDecoration(itemDecoration)

        fetchEvents()


    }

    private fun fetchEvents() {
        showLoading(true)
        val client = ApiConfig.getApiService().getEvents("0")
        client.enqueue(object: Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setEventData(responseBody.listEvents)
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setEventData(events: List<ListEventsItem?>?) {
        val adapter = ListEventAdapter()
        adapter.submitList(events)
        binding.rvEvent.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}
