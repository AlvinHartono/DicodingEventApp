package com.example.dicodingeventapp.ui.ui.search

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.data.Result
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel by viewModels<SearchViewModel>{
        SearchViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind with the layout
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set appbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSearch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewSearch.addItemDecoration(itemDecoration)


        // Observe loading state
        searchViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Observe searched events
        searchViewModel.searchedEvents.observe(this) { result ->
            Log.d("SearchActivity", "Received result: $result")
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    showError(true)
                    //make recyclerview invisible
                    binding.recyclerViewSearch.visibility = View.INVISIBLE

                }
                Result.Loading -> {
                    showLoading(true)
                    showError(false)
                    binding.recyclerViewSearch.visibility = View.INVISIBLE
                }
                is Result.Success -> {
                    showLoading(false)
                    showError(false)
                    setSearchEventsData(result.data)
                    binding.recyclerViewSearch.visibility = View.VISIBLE
                }
            }
        }

        // Set up SearchView and fetch Events
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    showError(false)
                    searchViewModel.fetchSearchResult(it)
                    binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }



    private fun showError(error: Boolean) {
        if (error) {
            binding.tvNoEventFound.visibility = View.VISIBLE
        } else {
            binding.tvNoEventFound.visibility = View.GONE
        }
    }

    // Handle Up button press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Circular Progress Loading Indicator
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }
    // Give List of Events to Adapter
    private fun setSearchEventsData(events: List<Event?>?) {
        val adapter = ListSearchEventAdapter()
        adapter.submitList(events)
        binding.recyclerViewSearch.adapter = adapter
    }
}