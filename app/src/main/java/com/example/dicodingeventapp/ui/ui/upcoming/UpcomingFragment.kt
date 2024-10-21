package com.example.dicodingeventapp.ui.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.data.Result
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.databinding.FragmentUpcomingBinding
import com.example.dicodingeventapp.ui.ui.search.SearchActivity

class UpcomingFragment : Fragment() {


    private var _binding: FragmentUpcomingBinding? = null
    private val upcomingViewModel by viewModels<UpcomingViewModel>{
        UpcomingViewModelFactory.getInstance(requireActivity())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
//            TODO: make an intent to SearchActivity
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        //observe ViewModel's LiveDatas
        upcomingViewModel.fetchUpcomingEvents().observe(viewLifecycleOwner) { result ->
            if (result != null){
                when (result) {
                    is Result.Error -> TODO()
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success ->{
                        showLoading(false)
                        setUpcomingEventsData(result.data)
                    }
                }
            }
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
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

        private fun setUpcomingEventsData(events: List<Event?>?) {
        val adapter = ListUpcomingEventAdapter{
            if (it.isFavorite){
                upcomingViewModel.deleteEvent(it)
            }else{
                upcomingViewModel.saveEvent(it)
            }
        }
        adapter.submitList(events)
        binding.rvEvent.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}