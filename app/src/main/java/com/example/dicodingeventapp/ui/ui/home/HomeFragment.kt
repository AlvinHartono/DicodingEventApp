package com.example.dicodingeventapp.ui.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    // Binding
    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access RecyclerViews
        val layoutManagerUpcoming =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvent.layoutManager = layoutManagerUpcoming

        val layoutManagerFinished =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFinishedEvent.layoutManager = layoutManagerFinished

        // Set up DividerItemDecoration
        val itemDecoration =
            DividerItemDecoration(requireContext(), layoutManagerUpcoming.orientation)
        binding.rvUpcomingEvent.addItemDecoration(itemDecoration)
        binding.rvFinishedEvent.addItemDecoration(itemDecoration)

        // Set up NestedScrollView to modify the scrolling
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            if (!binding.nestedScrollView.canScrollVertically(1)) {
                // Enable Finished Event RecyclerView scrolling when NestedScrollView can't scroll further
                binding.rvFinishedEvent.isNestedScrollingEnabled = true
            } else {
                // Disable RecyclerView scrolling while NestedScrollView is scrolling
                binding.rvFinishedEvent.isNestedScrollingEnabled = false
            }
        })
        // Observe Loading LiveData
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        // Observe Finished Events LiveData
        homeViewModel.finishedEventsHome.observe(viewLifecycleOwner){events ->
            setFinishedEventsData(events)

        }
        // Observe Upcoming Events LiveData
        homeViewModel.upcomingEventsHome.observe(viewLifecycleOwner){events ->
            setUpcomingEventsData(events)

        }

        return root
    }


    private fun setUpcomingEventsData(events: List<ListEventsItem?>?) {
        val adapter = ListUpcomingHomeEventAdapter()
        adapter.submitList(events)
        binding.rvUpcomingEvent.adapter = adapter
    }

    private fun setFinishedEventsData(events: List<ListEventsItem?>?) {
        val adapter = ListFinishedHomeEventAdapter()
        adapter.submitList(events)
        binding.rvFinishedEvent.adapter = adapter
    }
    // Destroy Binding
    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}