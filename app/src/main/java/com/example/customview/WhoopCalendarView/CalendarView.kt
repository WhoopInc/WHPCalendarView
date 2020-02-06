package com.example.customview.WhoopCalendarView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class CalendarView(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs), View.OnClickListener {

    private lateinit var header: LinearLayout
    private lateinit var btnPrev: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var txtDisplayDate: TextView
    private lateinit var gridView: RecyclerView
    private lateinit var calendarUIAdapter: CalendarUIAdapter
    private lateinit var currentDate: Calendar
    var flag : Int = 0

    private var MIN_DISTANCE = 100
    private var downX = 0.0f
    private var downY = 0.0f
    private var upX = 0.0f
    private var upY = 0.0f

    private fun assignUiElements() {
        header = findViewById(R.id.calendar_header)
        btnPrev = findViewById(R.id.calendar_prev_button)
        btnNext = findViewById(R.id.calendar_next_button)
        txtDisplayDate = findViewById(R.id.date_display_date)
        gridView = findViewById(R.id.calendar_grid)
        btnPrev.setOnClickListener(this)
        btnNext.setOnClickListener(this)
    }

    /**
     * Load control xml layout
     */

    init {
        initControl(context)
    }
    private fun initControl(
        context: Context
    ) {

        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.calendar_layout, this)
        assignUiElements()
        currentDate = Calendar.getInstance()
    }

    fun updateCalendar() {
        val cells = ArrayList<DateCell>()
        val calendar = currentDate.clone() as Calendar
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.firstDayOfWeek = Calendar.MONDAY
        val nextMonth = calendar.clone() as Calendar
        nextMonth.add(Calendar.MONTH, 1)
        while (calendar[Calendar.DAY_OF_WEEK] != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }
        for (i in 0..37) {
            cells.add(
                DateCell(
                    calendar.time,
                    "#000000",
                    R.color.transparent,i,
                    isCurrentMonth(calendar)
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            if (calendar >= nextMonth) {
                break
            }
        }
        while (calendar[Calendar.DAY_OF_WEEK] != Calendar.MONDAY) {
            cells.add(
                DateCell(
                    calendar.time,
                    "#000000",
                    R.color.transparent, cells.size + 1,
                    isCurrentMonth(calendar)
                )
            )
            calendar.add(Calendar.DATE, 1)
        }
        calendarUIAdapter = CalendarUIAdapter(cells)
        calendarUIAdapter.selectedInstance = currentDate
        calendarUIAdapter.flag = flag
        gridView.layoutManager = GridLayoutManager(context, 7, RecyclerView.VERTICAL, false)
        gridView.adapter = calendarUIAdapter
        gridView.setOnTouchListener(OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    upX = event.x
                    upY = event.y
                    val deltaX = downX - upX
                    val deltaY = downY - upY
                    if (abs(deltaX) > MIN_DISTANCE) { // left or right
                        if (deltaX < 0) {
                            onLeftToRightSwipe()
                            return@OnTouchListener true
                        }
                        if (deltaX > 0) {
                            onRightToLeftSwipe()
                            return@OnTouchListener true
                        }
                    }
                    if (abs(deltaY) > MIN_DISTANCE) { // top or down
                        if (deltaY < 0) {
                            onTopToBottomSwipe()
                            return@OnTouchListener true
                        }
                        if (deltaY > 0) {
                            onBottomToTopSwipe()
                            return@OnTouchListener true
                        }
                    }
                    return@OnTouchListener false // no swipe horizontally and no swipe vertically
                }
            }
            false
        })
        val sdf = SimpleDateFormat("EEEE,MMMM yyyy")
        val dateToday =
            sdf.format(currentDate.time).split(",").toTypedArray()
        txtDisplayDate.text = dateToday[1]
    }

    override fun onClick(v: View) {
        if (v.id == R.id.calendar_prev_button) {
            updateTheCalendar(-1)
        } else {
            updateTheCalendar(1)
        }
        calendarUIAdapter.notifyDataSetChanged()
    }

    private fun updateTheCalendar(month: Int) {
        currentDate.add(Calendar.MONTH, month)
        updateCalendar()
    }

    private fun isCurrentMonth(calendar: Calendar): Boolean {
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        return month == currentDate[Calendar.MONTH] && year == currentDate[Calendar.YEAR]
    }



    fun setType( f:Int){
        this.flag = f
        calendarUIAdapter.flag = f
        calendarUIAdapter.notifyDataSetChanged()
    }


    private fun onRightToLeftSwipe() {
        updateTheCalendar(1)
    }

    private fun onLeftToRightSwipe() {
        updateTheCalendar(-1)
    }

    private fun onTopToBottomSwipe() {
        updateTheCalendar(-1)
    }

    private fun onBottomToTopSwipe() {
        updateTheCalendar(1)
    }

}
