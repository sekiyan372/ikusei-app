package com.example.suiki.ikuseiapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.suiki.ikuseiapp.databinding.InputOneResultBinding
import io.realm.RealmResults
import java.text.SimpleDateFormat

class CustomInputViewAdapter(realmResults: RealmResults<IndexWakeAndSleep>)
    : RecyclerView.Adapter<InputViewHolder>()
{
    private val rResults: RealmResults<IndexWakeAndSleep> = realmResults
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private val sdf = SimpleDateFormat("yyyy/MM/dd E")

    override fun getItemCount(): Int {
        return rResults.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputViewHolder {
        val view = LayoutInflater.from(parent.context)
        return InputViewHolder(InputOneResultBinding.inflate(view, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: InputViewHolder, position: Int) {
        val input = rResults[position]
        if (input != null) {
            holder.dateText?.text = sdf.format(input.dateTime)
        }
        holder.wakeUpText?.text = input?.wakeUpTime
        holder.wakeUpLabel?.text = "起床"
        holder.sleepText?.text = input?.sleepTime
        holder.sleepLabel?.text = "就寝"
        holder.itemView.setBackgroundColor(if (position % 2 == 0) Color.LTGRAY else Color.WHITE)
    }
}
