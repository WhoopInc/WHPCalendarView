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


class CalendarUIAdapter(var dateCellList:ArrayList<DateCell>)
    : RecyclerView.Adapter<DateCellViewHolder>(){

    var flag = 0
    var selectedInstance: Calendar? = null
    var selectedView: ArrayList<GridCellData> = ArrayList()
    var viewList: ArrayList<GridCellData> = ArrayList()
    var monthViews: ArrayList<GridCellData> = ArrayList()
    var monthWeekPair = HashMap<Int,ArrayList<GridCellData>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateCellViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_calendar_day, parent, false) as View

        return DateCellViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dateCellList.size
    }

    override fun onBindViewHolder(holder: DateCellViewHolder, position: Int) {

        val datecell : DateCell = dateCellList[position]
        var calendar : Calendar = Calendar.getInstance()
        calendar.time = datecell.date
        calendar.get(Calendar.DATE).toString()

        val day = calendar[Calendar.DATE]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]

       val textView:TextView= holder.itemView.findViewById<TextView>(R.id.datetxt)
        textView.text = calendar.get(Calendar.DATE).toString()


        val calendarToday: Calendar = selectedInstance?: Calendar.getInstance()

        if (month == calendarToday.get(Calendar.MONTH) && year == calendarToday.get(Calendar.YEAR)) {
            textView.setTextColor(Color.parseColor(datecell.textColor))
            monthViews.add(GridCellData(holder,dateCellList[position]))
            addMonthWeek(GridCellData(holder,dateCellList[position]))
        } else {
            datecell.textColor = "#E0E0E0"
            dateCellList[position] = datecell
            textView.setTextColor(Color.parseColor(datecell.textColor))
        }

        if(dateCellList[position].backgroundResource ==0 ){
            dateCellList[position].backgroundResource = R.color.transparent
        } else if(dateCellList[position].backgroundResource!=0){
            holder.itemView.setBackgroundResource(dateCellList[position].backgroundResource)
        }
        viewList.add(GridCellData(holder,dateCellList[position]))

        holder.itemView.setOnClickListener {
            if(flag == 0){
                if (selectedView.size > 0) {
                    resetColor(selectedView, R.color.transparent)
                    selectedView = ArrayList<GridCellData>()
                }
                selectedView.add(GridCellData(holder,dateCellList[position]))
                holder.itemView.setBackgroundResource(R.drawable.circle)
                (holder.itemView.findViewById(R.id.datetxt) as TextView)
                    .setTextColor(Color.parseColor("#EE0202"))

            } else if(flag == 1){

                if (selectedView.size > 0) {
                    resetColor(selectedView, R.color.transparent)
                    selectedView = ArrayList<GridCellData>()
                }
                setColorSelectedWeek(getRow(position))
            } else if(flag == 2){

                if (selectedView.size > 0) {
                    resetColor(selectedView, R.color.transparent)
                    selectedView = ArrayList<GridCellData>()
                }
                selectedMonth()
            }
        }

    }

    private fun resetColor( selectedView: ArrayList<GridCellData>, colorId:Int) {
        var gridCellData : GridCellData
        var color : String
        for (i in selectedView.indices) {
            gridCellData = selectedView[i]
            gridCellData.dataCellViewHolder.itemView.setBackgroundResource(colorId)
            gridCellData.dateCell.backgroundResource = R.color.transparent
            if(gridCellData.dateCell.textColor == "#E0E0E0"){
                color = "#E0E0E0"
                //selectedView[i].dateCell.textColor ="#E0E0E0"
            } else {
                color = "#000000"
                //selectedView[i].dateCell.textColor ="#000000"
            }
            gridCellData.dataCellViewHolder.itemView.findViewById<TextView>(R.id.datetxt).setTextColor(Color.parseColor(color))

        }
        this.selectedView = ArrayList<GridCellData>()
    }

    fun setColorSelectedWeek(week: Int) {
        if (week < 5) {
            selectedView = ArrayList<GridCellData>()
            val min = 7 * week
            val max = min + 7
            for (i in min until max) {
                selectedView.add(viewList.get(i))
                Log.d("WeekOOO", dateCellList[i].date.toString() + " weeknumber " + week)
            }
            setWeekColor(selectedView)
        }
    }


    fun getRow(position: Int): Int {
        return position / 7
    }


    fun setWeekColor(sviews: ArrayList<GridCellData>) {

        if (sviews.size == 1) {
           var d1: GridCellData =  sviews[0]
            d1.dataCellViewHolder.itemView.setBackgroundResource(R.drawable.single)
            d1.dataCellViewHolder.itemView.findViewById<TextView>(R.id.datetxt).setTextColor(Color.parseColor("#EE0202"))
        } else {
            var d2: GridCellData =  sviews[0]
            d2.dataCellViewHolder.itemView.setBackgroundResource(R.drawable.start)
            d2.dataCellViewHolder.itemView.findViewById<TextView>(R.id.datetxt).setTextColor(Color.parseColor("#EE0202"))

            for (i in 1 until sviews.size - 1) {
                var view : View = sviews[i].dataCellViewHolder.itemView
                view.setBackgroundResource(R.drawable.middle)
                view.findViewById<TextView>(R.id.datetxt).setTextColor(Color.parseColor("#EE0202"))
            }
            d2 = sviews[sviews.size - 1]
            d2.dataCellViewHolder.itemView.setBackgroundResource(R.drawable.end)
            d2.dataCellViewHolder.itemView.findViewById<TextView>(R.id.datetxt).setTextColor(Color.parseColor("#EE0202"))
        }

    }

    fun selectedMonth() {
        selectedView.addAll(monthViews)
        setWeekMonthColor()
    }

    fun setWeekMonthColor() {
        for (key in monthWeekPair.keys) {
            setWeekColor(monthWeekPair.get(key) as ArrayList<GridCellData>)
        }
    }

    fun addMonthWeek(gridCellData: GridCellData) {
        val weeknumber: Int = getWeeknumber(gridCellData.dateCell.date)

        if (monthWeekPair.containsKey(weeknumber)) {
            val viewArrayList: ArrayList<GridCellData>? = monthWeekPair[weeknumber]
            if (!viewArrayList!!.contains(gridCellData)) {
                viewArrayList.add(gridCellData)
            }
            monthWeekPair[weeknumber] = viewArrayList
        } else {
            val viewArrayList = java.util.ArrayList<GridCellData>()
            viewArrayList.add(gridCellData)
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
