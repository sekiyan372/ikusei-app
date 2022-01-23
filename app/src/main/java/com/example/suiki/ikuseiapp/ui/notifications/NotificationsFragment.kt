package com.example.suiki.ikuseiapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.suiki.ikuseiapp.Chara
import com.example.suiki.ikuseiapp.R
import com.example.suiki.ikuseiapp.databinding.FragmentNotificationsBinding
import io.realm.Realm
import io.realm.kotlin.where

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

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

        binding.resetCharaButton.setOnClickListener {
            val character: Chara? = getCharacter(0)
            if (character != null) resetCharacter(character)

            findNavController().navigate(R.id.action_notificationsFragment_to_homeFragment)
            Toast.makeText(context, "キャラクターをリセットしました", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun getCharacter(id: Int): Chara? {
        return realm.where<Chara>()
            .equalTo("id", id)
            ?.findFirst()
    }

    private fun resetCharacter(character: Chara) {
        realm.executeTransaction {
            character.level = 1
            character.exp = 0
            character.nextExp = 8
        }
    }
}
