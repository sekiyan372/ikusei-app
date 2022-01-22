package com.example.suiki.ikuseiapp

import android.app.Dialog
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment

//class TimePick : DialogFragment(), TimePickerDialog.OnTimeSetListener {
//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val c = Calendar.getInstance()
//
//        val hour = c.get(Calendar.HOUR_OF_DAY)
//        val minute = c.get(Calendar.MINUTE)
//
//        return TimePickerDialog(
//            activity,
//            activity as MainActivity?,
//            hour,
//            minute,
//            true)
//    }
//
//    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
//        //
//    }
//}
