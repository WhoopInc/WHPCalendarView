package com.example.customview.WhoopCalendarView

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R
import java.util.*
import kotlin.collections.ArrayList


class CalendarUIAdapter(var dataCellList : ArrayList<DateCell>)
    : RecyclerView.Adapter<DateCellViewHolder>(){

    var flag = 0
    var selectedInstance: Calendar? = null
    var monthWeekPair = HashMap<Int,ArrayList<Int>>()
    var selectedView = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateCellViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_calendar_day, parent, false) as View
        return DateCellViewHolder(view)
    }



    override fun onBindViewHolder(holder: DateCellViewHolder, position: Int) {

        val dateCell :DateCell = dataCellList[position]
        var calendar : Calendar = Calendar.getInstance()
        calendar.time = dateCell.date

        holder.textView.text = calendar.get(Calendar.DATE).toString()
        if(dateCell.currentMonth || dateCell.selectedDay || dateCell.currentWeek ){
            holder.textView.setTextColor(Color.parseColor(dateCell.textColor))
        } else {
            holder.textView.setTextColor(Color.parseColor("#E0E0E0"))
        }
        holder.itemView.setBackgroundResource(dateCell.backgroundResource)


        holder.itemView.setOnClickListener {
            when(flag){
                0 -> {
                    if(selectedView != null && selectedView.size >0){
                        resetColor(selectedView)
                    }
                    selectedView.add(position)
                    updateSingleDate()
                }
                1 -> {
                    if(selectedView != null && selectedView.size >0){
                        resetColor(selectedView)
                    }

                    setColorSelectedWeek(getRow(position))
                }
                2 -> {
                    if(selectedView != null && selectedView.size >0){
                        resetColor(selectedView)
                    }

                    selectedMonth()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return dataCellList.size
    }

    private fun updateSingleDate(){
        for(position in selectedView.indices){
            var dateCell = dataCellList[selectedView[position]]
            dateCell.selectedDay = true
            dateCell.textColor = "#EE0202"
            dateCell.backgroundResource = R.drawable.circle

            dataCellList[selectedView[position]] = dateCell
        }
        notifyDataSetChanged()

    }

    private fun resetColor( sview: ArrayList<Int>) {

        for(position in sview.indices){
            var dateCell = dataCellList[sview[position]]
            if(dateCell.currentMonth) {
                dateCell.textColor = "#000000"
            } else {
                dateCell.textColor = "#E0E0E0"
            }
            dateCell.backgroundResource = R.color.transparent
            dateCell.selectedDay = false

            dataCellList[sview[position]] = dateCell

        }
        selectedView = ArrayList()

        notifyDataSetChanged()

    }

    fun setColorSelectedWeek(week: Int) {
        var min:Int
        var max :Int
        selectedView = ArrayList()
        if (week < 5) {
             min = 7 * week
             max = min + 7

        } else {
             min = 7 * 5
             max = dataCellList.size - min
        }
        for (i in min until max) {
            selectedView.add(i)
        }
        setWeekColor(selectedView)
    }



    fun getRow(position: Int): Int {
        return position / 7
    }


    fun setWeekColor(sviews: ArrayList<Int>) {
        if (sviews.size == 1) {
            var dateCell = dataCellList[sviews[0]]
            dateCell.currentWeek = true
            dateCell.textColor = "#EE0202"
            dateCell.backgroundResource = R.drawable.circle

            dataCellList[sviews[0]] = dateCell

        } else {
            var dateCell = dataCellList[sviews[0]]
            dateCell.currentWeek = true
            dateCell.textColor = "#EE0202"
            dateCell.backgroundResource = R.drawable.start

            dataCellList[sviews[0]] = dateCell

            for (i in 1 until sviews.size - 1) {
                var dateCell = dataCellList[sviews[i]]
                dateCell.currentWeek = true
                dateCell.textColor = "#EE0202"
                dateCell.backgroundResource = R.drawable.middle

                dataCellList[sviews[i]] = dateCell
            }
            dateCell = dataCellList[sviews[sviews.size - 1]]
            dateCell.currentWeek = true
            dateCell.textColor = "#EE0202"
            dateCell.backgroundResource = R.drawable.end

            dataCellList[sviews[sviews.size - 1]] = dateCell
        }
        notifyDataSetChanged()
    }



   fun setWeekMonthColor() {
        for (key in monthWeekPair.keys) {
            setWeekColor(monthWeekPair[key] as ArrayList<Int>)
        }
    }

   fun selectedMonth() {
       for(position in dataCellList.indices){
           if(dataCellList[position].currentMonth){
               selectedView.add(position)
               addMonthWeek(position)
           }
       }
        setWeekMonthColor()
    }

    fun addMonthWeek(position: Int) {
        val weeknumber: Int = getWeeknumber(dataCellList[position].date)

        if (monthWeekPair.containsKey(weeknumber)) {
            val viewArrayList: ArrayList<Int>? = monthWeekPair[weeknumber]
            if (!viewArrayList!!.contains(position)) {
                viewArrayList.add(position)
            }
            monthWeekPair[weeknumber] = viewArrayList
        } else {
            val viewArrayList = java.util.ArrayList<Int>()
            viewArrayList.add(position)
            monthWeekPair[weeknumber] = viewArrayList
        }
    }

    fun getWeeknumber(d: Date?): Int {
        val c = Calendar.getInstance()
        c.firstDayOfWeek = Calendar.MONDAY
        c.time = d
        return c[Calendar.WEEK_OF_MONTH]
    }


}
