package com.example.customview.WhoopCalendarView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.customview.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarView extends LinearLayout implements View.OnClickListener {
    LinearLayout header;
    ImageView btnPrev;
    ImageView btnNext;
    TextView txtDisplayDate;
    GridView gridView;
    Calendar currentDate;
    int flag=0;

    CalendarAdapter calendarAdapter;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = findViewById(R.id.calendar_header);
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtDisplayDate = findViewById(R.id.date_display_date);
        gridView = findViewById(R.id.calendar_grid);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_layout, this);
        assignUiElements();
        currentDate = Calendar.getInstance();
    }

    public void updateCalendar()
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        // determine the cell for current month's beginning



       // int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK);



        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        //Log.d("Month010",String.valueOf(monthBeginningCell));

        // move calendar backwards to the beginning of the week
       // calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);


        // fill cells
        while (cells.size() < 38)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid

        calendarAdapter = new CalendarAdapter(getContext(),
                cells, new HashSet<Date>(),currentDate);
        calendarAdapter.setFlag(flag);
        gridView.setAdapter(calendarAdapter);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM yyyy");
        String[] dateToday = sdf.format(currentDate.getTime()).split(",");
        txtDisplayDate.setText(dateToday[1]);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.calendar_prev_button){
            updateTheCalendar(-1);
            calendarAdapter.setMonthViews(new ArrayList<View>());
            calendarAdapter.notifyDataSetChanged();
        } else {
            updateTheCalendar(1);

        }
        calendarAdapter.setMonthViews(new ArrayList<View>());
        calendarAdapter.notifyDataSetChanged();
    }

    public void updateTheCalendar(int month){
        currentDate.add(Calendar.MONTH,month);
        updateCalendar();
    }


    public void setFlag(int flag) {
        this.flag = flag;
        calendarAdapter.setFlag(flag);
        calendarAdapter.notifyDataSetChanged();
    }
}
