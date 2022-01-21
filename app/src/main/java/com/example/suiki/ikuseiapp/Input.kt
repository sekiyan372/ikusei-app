package com.example.suiki.ikuseiapp

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.suiki.ikuseiapp.databinding.FragmentInputBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Input : Fragment() {
    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm

    @RequiresApi(Build.VERSION_CODES.O)
    private val now = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"))
    @RequiresApi(Build.VERSION_CODES.O)
    private val dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd E")
    @RequiresApi(Build.VERSION_CODES.O)
    private val idf = DateTimeFormatter.ofPattern("yyyyMMdd")
    @RequiresApi(Build.VERSION_CODES.O)
    private val instant = now.toInstant()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        binding.today.text = now.format(dtf)

        binding.buttonRecord.setOnClickListener {
            val id = now.format(idf).toInt()
            val result = realm.where<IndexWakeAndSleep>().equalTo("id", id).findFirst()

            if (result == null) {
                var wakeUp = ""
                var sleep = ""

                if (binding.wakeUpTime.text.isNullOrEmpty() && binding.sleepTime.text.isNullOrEmpty()) {
                    Toast.makeText(context, "値を入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!binding.wakeUpTime.text.isNullOrEmpty()) {
                    wakeUp = binding.wakeUpTime.text.toString()
                }

                if (!binding.sleepTime.text.isNullOrEmpty()) {
                    sleep = binding.sleepTime.text.toString()
                }
                realm.executeTransaction {
                    val indexWakeAndSleep = realm.createObject<IndexWakeAndSleep>(id)
                    indexWakeAndSleep.wakeUpTime = wakeUp
                    indexWakeAndSleep.sleepTime = sleep
                    indexWakeAndSleep.dateTime = Date.from(instant)
                }
            } else if (result.wakeUpTime != "" && result.sleepTime != "") {
                Toast.makeText(context, "本日は記録済みです", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (result.wakeUpTime != "") {
                if (binding.sleepTime.text.isNullOrEmpty()) {
                    Toast.makeText(context, "就寝時間を入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    realm.executeTransaction {
                        result.sleepTime = binding.sleepTime.text.toString()
                    }
                }
            } else if (result.sleepTime != "") {
                if (!binding.wakeUpTime.text.isNullOrEmpty()) {
                    Toast.makeText(context, "起床時間を入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    realm.executeTransaction {
                        result.wakeUpTime = binding.wakeUpTime.text.toString()
                    }
                }
            }

            findNavController().navigate(R.id.action_inputFragment_to_homeFragment)
            Toast.makeText(context, "記録しました", Toast.LENGTH_SHORT).show()
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
}
