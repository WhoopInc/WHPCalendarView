package com.example.customview

import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.WhoopCalendarView.CalendarView



class MainActivity : AppCompatActivity() {

   lateinit var calendarView: CalendarView
    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView = findViewById(R.id.calendarView)
        calendarView.updateCalendar()

        radioGroup = findViewById(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.daily -> {
                    calendarView.setType(0)
                }
                R.id.weekly -> {
                    calendarView.setType(1)
                }
                R.id.monthly -> {
                    calendarView.setType(2)
                }
            }
        }
    }
}
