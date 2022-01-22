package com.example.suiki.ikuseiapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.suiki.ikuseiapp.databinding.FragmentTargetBinding

class TargetFragment : Fragment() {
    private var _binding: FragmentTargetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTargetBinding.inflate(inflater, container, false)

        PreferenceManager.getDefaultSharedPreferences(context).apply {
            val wakeUpTargetTime = getString("wakeUpTargetTime", "")
            val sleepTargetTime = getString("sleepTargetTime", "")

            binding.wakeUpTarget.setText(wakeUpTargetTime)
            binding.sleepTarget.setText(sleepTargetTime)
        }

        binding.setButton.setOnClickListener {
            val wakeUpText = binding.wakeUpTarget.text
            val sleepText = binding.sleepTarget.text
            val regex = Regex("^\\d{2}:\\d{2}$")

            if (wakeUpText.isEmpty() || sleepText.isEmpty()) {
                Toast.makeText(context, "両方の目標を入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!wakeUpText.matches(regex) || !sleepText.matches(regex)) {
                Toast.makeText(context, "正しい形式で入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveTarget()
            findNavController().navigate(R.id.action_targetFragment_to_homeFragment)
            Toast.makeText(context, "設定しました", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveTarget() {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()
        editor.putString("wakeUpTargetTime", binding.wakeUpTarget.text.toString())
            .putString("sleepTargetTime", binding.sleepTarget.text.toString())
            .apply()
    }
}
