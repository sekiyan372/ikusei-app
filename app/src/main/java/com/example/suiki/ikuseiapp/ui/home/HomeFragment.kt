package com.example.suiki.ikuseiapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.suiki.ikuseiapp.R
import androidx.navigation.fragment.findNavController
import com.example.suiki.ikuseiapp.Chara
import com.example.suiki.ikuseiapp.databinding.FragmentHomeBinding
import io.realm.Realm

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        var character: Chara? = getCharacter(0)

        if (character == null) {
            createCharacterData()
            character = getCharacter(0)
        }

        binding.name.text = character?.name
        binding.level.text = "Lv." + character?.level.toString()
        binding.currentExp.text = character?.exp.toString()
        binding.fullExp.text = character?.nextExp.toString()

        binding.buttonInput.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_inputFragment)
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

    private fun createCharacterData() {
        realm.executeTransaction {
            val id = realm.where(Chara::class.java).max("id")
            val nextId = if (id != null) {
                (id.toLong() + 1)
            } else 0
            val chara = it.createObject(Chara::class.java, nextId)
            chara.name = "モンキー・D・ルフィー"
            chara.level = 1
            chara.exp = 0
            chara.nextExp = 7
        }
    }

    private fun getCharacter(id: Int): Chara? {
        return realm.where(Chara::class.java)
            .equalTo("id", id)
            ?.findFirst()
    }
}
