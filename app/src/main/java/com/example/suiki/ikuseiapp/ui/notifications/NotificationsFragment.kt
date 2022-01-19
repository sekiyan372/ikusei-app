package com.example.suiki.ikuseiapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.suiki.ikuseiapp.R
import com.example.suiki.ikuseiapp.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        PreferenceManager.getDefaultSharedPreferences(context).apply {
            val wakeUpTargetTime = getString("wakeUpTargetTime", "")
            val sleepTargetTime = getString("sleepTargetTime", "")

            binding.viewWakeUpTarget.text = wakeUpTargetTime
            binding.viewSleepTarget.text = sleepTargetTime
        }

        binding.setTargetButton.setOnClickListener {
            findNavController().navigate(R.id.action_notificationsFragment_to_targetFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
