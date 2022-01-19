package com.example.suiki.ikuseiapp

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suiki.ikuseiapp.databinding.InputOneResultBinding

class InputViewHolder(private val binding: InputOneResultBinding):
    RecyclerView.ViewHolder(binding.root)
{
    var dateText: TextView? = null
    var wakeUpText: TextView? = null
    var wakeUpLabel: TextView? = null
    var sleepText: TextView? = null
    var sleepLabel: TextView? = null

    init {
        dateText = binding.dateText
        wakeUpText = binding.wakeUpText
        wakeUpLabel = binding.wakeUpLabel
        sleepText = binding.sleepText
        sleepLabel = binding.sleepLabel
    }
}
