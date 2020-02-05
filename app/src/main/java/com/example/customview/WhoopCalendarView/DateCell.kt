package com.example.customview.WhoopCalendarView

import java.util.*

data class DateCell(var date: Date,var textColor:String, var backgroundResource:Int = 0,
                    var position:Int =0,
                    var currentMonth : Boolean = false,
                    var currentWeek : Boolean = false,
                    var selectedDay: Boolean = false)
