package com.example.dicodingeventapp.ui.ui.finished
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.databinding.FragmentFinishedBinding
import com.example.dicodingeventapp.ui.ui.search.SearchActivity


class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val finishedViewModel by viewModels<FinishedViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access RecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 2)
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

        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            setUpcomingEventsData(events)
        }
        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
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

    private fun setUpcomingEventsData(events: List<ListEventsItem?>?) {
        val adapter = ListFinishedEventAdapter()
        adapter.submitList(events)
        binding.rvEvent.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}