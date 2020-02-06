package com.example.customview.WhoopCalendarView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.R

class DateCellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val textView : TextView = itemView.findViewById(R.id.dateTxt)
}
