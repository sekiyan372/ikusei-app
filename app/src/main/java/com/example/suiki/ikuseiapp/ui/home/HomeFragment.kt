package com.example.suiki.ikuseiapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.suiki.ikuseiapp.R
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.suiki.ikuseiapp.databinding.FragmentHomeBinding
import java.util.prefs.PreferenceChangeEvent

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.target.text = "目標時間"
        binding.viewWakeUpTargetLabel.text = "起床"
        binding.viewSleepTargetLabel.text = "就寝"

        PreferenceManager.getDefaultSharedPreferences(context).apply {
            val wakeUpTargetTime = getString("wakeUpTargetTime", "")
            val sleepTargetTime = getString("sleepTargetTime", "")

            binding.viewWakeUpTarget.text = wakeUpTargetTime
            binding.viewSleepTarget.text = sleepTargetTime
        }

        binding.buttonInput.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_inputFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
