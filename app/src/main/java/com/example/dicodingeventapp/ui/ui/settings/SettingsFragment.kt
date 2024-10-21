package com.example.dicodingeventapp.ui.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingeventapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val pref = SettingsPreferences.getInstance(requireContext().dataStore)
        val settingsViewModel = ViewModelProvider(requireActivity(), SettingsViewModelFactory(pref = pref))[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        settingsViewModel.getThemeSetting().observe(requireActivity()) { isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                binding.switchTheme.isChecked = true
            } else {
                binding.switchTheme.isChecked = false
            }
        }

        // theme
        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

        return root
    }

}