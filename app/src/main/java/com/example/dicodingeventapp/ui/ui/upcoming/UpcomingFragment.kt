package com.example.dicodingeventapp.ui.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.data.Result
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.databinding.FragmentUpcomingBinding
import com.example.dicodingeventapp.ui.ui.search.SearchActivity
import kotlinx.coroutines.launch

class UpcomingFragment : Fragment() {


    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val upcomingViewModel by viewModels<UpcomingViewModel> {
        UpcomingViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvEvent.addItemDecoration(itemDecoration)


        // Set up NestedScrollView to modify the scrolling
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            if (!binding.nestedScrollView.canScrollVertically(1)) {
                // Enable RecyclerView scrolling when NestedScrollView can't scroll further
                binding.rvEvent.isNestedScrollingEnabled = true
            } else {
                // Disable RecyclerView scrolling while NestedScrollView is scrolling
                binding.rvEvent.isNestedScrollingEnabled = false
            }
        })
        // Searchbar goes to SearchFragment when clicked
        binding.searchBar.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        //observe ViewModel's LiveData
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->

            Log.d("UpcomingFragment", "Result: $result")
            when (result) {
                is Result.Error -> showError(result.error)
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    setUpcomingEventsData(result.data)
                }
            }
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // check which thread is used
            Log.d("UpcomingFragment Thread", "Thread: ${Thread.currentThread().name}")
            upcomingViewModel.fetchUpcomingEvents()
        }

        return root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
        Log.e("UpcomingFragment", "Error: $message")
    }

    private fun setUpcomingEventsData(events: List<Event?>?) {
        if (events.isNullOrEmpty()) {
            binding.tvNoEventFound.visibility = View.VISIBLE
            binding.rvEvent.visibility = View.INVISIBLE
        } else {
            val adapter = ListUpcomingEventAdapter()
            binding.tvNoEventFound.visibility = View.INVISIBLE
            binding.rvEvent.visibility = View.VISIBLE
            adapter.submitList(events)
            binding.rvEvent.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}