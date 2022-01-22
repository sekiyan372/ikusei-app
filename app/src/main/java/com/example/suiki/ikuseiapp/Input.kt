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
import androidx.preference.PreferenceManager
import com.example.suiki.ikuseiapp.databinding.FragmentInputBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs
import kotlin.math.round

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
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val wakeUpTarget = pref.getString("wakeUpTargetTime", "")
            val sleepTarget = pref.getString("sleepTargetTime", "")
            val regex = Regex("^\\d{2}:\\d{2}$")
            val id = now.format(idf).toInt()
            val getData = realm.where<IndexWakeAndSleep>().equalTo("id", id).findFirst()
            var wakeUp = ""
            var sleep = ""
            val character: Chara? = getCharacter(0)
            var getExp = 0

            if (wakeUpTarget.isNullOrEmpty() || sleepTarget.isNullOrEmpty()) {
                Toast.makeText(context, "目標値が設定されていません", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (getData == null) {
                var getWakeUpExp = 0
                var getSleepExp = 0

                if (binding.wakeUpTime.text.isNullOrEmpty() && binding.sleepTime.text.isNullOrEmpty()) {
                    Toast.makeText(context, "値を入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (
                    !binding.wakeUpTime.text.isNullOrEmpty() && !binding.wakeUpTime.text.matches(regex)
                    || !binding.sleepTime.text.isNullOrEmpty() && !binding.sleepTime.text.matches(regex)
                ) {
                    Toast.makeText(context, "正しい形式で入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!binding.wakeUpTime.text.isNullOrEmpty()) {
                    wakeUp = binding.wakeUpTime.text.toString()
                    getWakeUpExp = calculateExp(wakeUpTarget, wakeUp)
                }

                if (!binding.sleepTime.text.isNullOrEmpty()) {
                    sleep = binding.sleepTime.text.toString()
                    getSleepExp = calculateExp(sleepTarget, sleep)
                }

                realm.executeTransaction {
                    val indexWakeAndSleep = realm.createObject<IndexWakeAndSleep>(id)
                    indexWakeAndSleep.wakeUpTime = wakeUp
                    indexWakeAndSleep.sleepTime = sleep
                    indexWakeAndSleep.dateTime = Date.from(instant)
                }

                getExp = getWakeUpExp + getSleepExp
            }

            else if (getData.wakeUpTime.isNotEmpty() && getData.sleepTime.isNotEmpty()) {
                Toast.makeText(context, "本日は記録済みです", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else if (getData.wakeUpTime.isNotEmpty()) {
                if (binding.sleepTime.text.isNullOrEmpty()) {
                    Toast.makeText(context, "就寝時間を入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!binding.sleepTime.text.matches(regex)) {
                    Toast.makeText(context, "正しい形式で入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                sleep = binding.sleepTime.text.toString()
                realm.executeTransaction {
                    getData.sleepTime = sleep
                }
                getExp = calculateExp(sleepTarget, sleep)
            }

            else if (getData.sleepTime.isNotEmpty()) {
                if (!binding.wakeUpTime.text.isNullOrEmpty()) {
                    Toast.makeText(context, "起床時間を入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!binding.wakeUpTime.text.matches(regex)) {
                    Toast.makeText(context, "正しい形式で入力してください", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                wakeUp = binding.wakeUpTime.text.toString()
                realm.executeTransaction {
                    getData.wakeUpTime = wakeUp
                }
                getExp = calculateExp(wakeUpTarget, wakeUp)
            }

            if (character != null && character.level != 100) {
                realm.executeTransaction {
                    if (isLevelUp(character, getExp) && character.level == 99) {
                        character.level  = 100
                        character.exp = character.nextExp
                    }

                    else if (isLevelUp(character, getExp)) {
                        character.level += 1
                        character.exp += getExp
                        character.nextExp = calculateNextExp(character.nextExp, character.level)
                        //経験値がカンストした時の処理
                        if (character.exp >= character.nextExp) {
                            character.level += 1
                            character.nextExp = calculateNextExp(character.nextExp, character.level)
                        }
                    }

                    else {
                        character.exp += getExp
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

    private fun getCharacter(id: Int): Chara? {
        return realm.where<Chara>()
            .equalTo("id", id)
            ?.findFirst()
    }

    private fun calculateExp(target: String, input: String): Int {
        val cutTargetHour = target.substring(0, 2).toInt()
        val cutTargetMin = target.substring(3, 5).toInt()
        val sumTargetMin = cutTargetHour * 60 + cutTargetMin

        val cutInputHour = input.substring(0, 2).toInt()
        val cutInputMin = input.substring(3, 5).toInt()
        val sumInputMin = cutInputHour * 60 + cutInputMin

        val difference = abs(sumTargetMin - sumInputMin)
        val culDifference = if (difference >= 1290) 1440 - difference else difference

        val result = when (culDifference) {
            in 0..30 -> 10
            in 31..60 -> 8
            in 61..90 -> 6
            in 91..120 -> 4
            in 121..150 -> 2
            else -> 1
        }

        return result
    }

    private fun isLevelUp(chara: Chara, gotExp: Int): Boolean {
        return chara.exp + gotExp >= chara.nextExp
    }

    private fun calculateNextExp(exp: Int, level: Int): Int {
        return round(((exp * 1.1) + (level * 15)) / 2).toInt()
    }
}
