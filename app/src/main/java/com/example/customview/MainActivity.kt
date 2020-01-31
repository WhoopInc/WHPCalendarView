package com.example.customview

import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.WhoopCalendarView.CalendarView



class MainActivity : AppCompatActivity() {

   lateinit var cal: CalendarView
    lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cal = findViewById(R.id.calendarView)
        cal.updateCalendar()

        radioGroup = findViewById(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.daily) {
                cal.setFlag(0)
            } else if(checkedId == R.id.weekly) {
                cal.setFlag(1)
            } else if(checkedId == R.id.monthly) {
                cal.setFlag(2)
            }
        }  )
    }
}
