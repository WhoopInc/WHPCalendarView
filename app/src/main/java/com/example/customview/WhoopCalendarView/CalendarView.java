package com.example.customview.WhoopCalendarView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    RecyclerView gridView;
    Calendar currentDate;
    int flag=0;
    Context context;

    CalendarUIAdapter calendarUIAdapter;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
        this.context=context;
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
        ArrayList<DateCell> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        Calendar nextMonth = (Calendar) calendar.clone();
        nextMonth.add(Calendar.MONTH,1);

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        for(int i=0;i<38;i++){

            cells.add(new DateCell(calendar.getTime(),
                    "#000000",
                    R.color.transparent, i,
                    isCurrentMonth(calendar),
                    false,
                    false));

            calendar.add(Calendar.DAY_OF_MONTH, 1);

            if (calendar.compareTo(nextMonth) >= 0) {
                break;
            }
        }

    calendarUIAdapter = new CalendarUIAdapter(cells);
    calendarUIAdapter.setSelectedInstance(currentDate);
    calendarUIAdapter.setFlag(flag);
    gridView.setLayoutManager(new GridLayoutManager(context, 7, RecyclerView.VERTICAL, false));
    gridView.setAdapter(calendarUIAdapter);
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM yyyy");
    String[] dateToday = sdf.format(currentDate.getTime()).split(",");
    txtDisplayDate.setText(dateToday[1]);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.calendar_prev_button){

            updateTheCalendar(-1);

        } else {
            updateTheCalendar(1);

        }
        calendarUIAdapter.notifyDataSetChanged();
    }

    public void updateTheCalendar(int month){
        currentDate.add(Calendar.MONTH,month);
        updateCalendar();
    }

    public boolean isCurrentMonth(Calendar calendar){

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        if (month == currentDate.get(Calendar.MONTH) && year == currentDate.get(Calendar.YEAR)) {
            return true;
        } else {
            return false;
        }
    }


    public void setFlag(int flag) {
        this.flag = flag;
        calendarUIAdapter.setFlag(flag);
        calendarUIAdapter.notifyDataSetChanged();
    }
}
