package com.example.dicodingeventapp.ui.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory.getInstance(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root : View = binding.root

        // Access RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFavorite.addItemDecoration(itemDecoration)

        favoriteViewModel.getFavoriteEvents().observe(viewLifecycleOwner) { event ->
                setFavoriteEventsData(event)
        }


        return root
    }

    private fun setFavoriteEventsData(events: List<Event>?) {
        if (events.isNullOrEmpty()) {
            binding.rvFavorite.visibility = View.INVISIBLE
            binding.tvEmptyFavorite.visibility = View.VISIBLE
        } else {
            binding.rvFavorite.visibility = View.VISIBLE
            binding.tvEmptyFavorite.visibility = View.INVISIBLE

            val adapter = ListFavoriteEventAdapter { event ->
                CoroutineScope(Dispatchers.Main).launch {
                    if (event.isFavorite) {
                        favoriteViewModel.deleteEvent(event) // Call the suspend function
                    } else {
                        favoriteViewModel.saveEvent(event) // Call the suspend function
                    }
                }
            }

            adapter.submitList(events)
            binding.rvFavorite.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}