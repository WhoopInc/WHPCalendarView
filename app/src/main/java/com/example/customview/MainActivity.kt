package com.example.customview

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.WhoopCalendarView.CalendarView



class MainActivity : AppCompatActivity() {

   lateinit var cal: CalendarView
    lateinit var radioGroup: RadioGroup

    val MIN_DISTANCE = 100
    var downX: Float = 0.0f
    var downY: Float = 0.0f
    var upX: Float = 0.0f
    var upY: Float = 0.0f

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

        /*  parentLayout.setOnTouchListener(object : View.OnTouchListener {
              override fun onTouch(v: View, m: MotionEvent): Boolean {
                  when (m.action) {
                      MotionEvent.ACTION_DOWN -> {
                          downX = m.getX()
                          downY = m.getY()
                          return true
                      }
                      MotionEvent.ACTION_UP -> {
                          upX = m.getX();
                          upY = m.getY();

                          var deltaX: Float = downX - upX;
                          var deltaY: Float = downY - upY;

                          if (Math.abs(deltaX) > MIN_DISTANCE) {
                              // left or right
                              if (deltaX < 0) {
                                  onLeftToRightSwipe()
                                  return true
                              }
                              if (deltaX > 0) {
                                  onRightToLeftSwipe()
                                  return true
                              }
                          }

                          if (Math.abs(deltaY) > MIN_DISTANCE) {
                              // top or down
                              if (deltaY < 0) {
                                  onTopToBottomSwipe()
                                  return true
                              }
                              if (deltaY > 0) {
                                  onBottomToTopSwipe()
                                  return true
                              }
                          }

                          return false
                      }

                  }
                  return true

              }


          }
          ) */
    }

   /* fun onRightToLeftSwipe() {
        Toast.makeText(this, "RightToLeftSwipe", Toast.LENGTH_SHORT).show()
    }

    fun onLeftToRightSwipe() {
        Toast.makeText(this, "LeftToRightSwipe", Toast.LENGTH_SHORT).show()
    }

    fun onTopToBottomSwipe() {
        Toast.makeText(this, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show()
    }

    fun onBottomToTopSwipe() {
        Toast.makeText(this, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show()
    } */
}
