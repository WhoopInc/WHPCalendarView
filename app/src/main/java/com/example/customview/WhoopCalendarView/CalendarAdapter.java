package com.example.customview.WhoopCalendarView;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customview.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

class CalendarAdapter extends ArrayAdapter<Date> {

    private LayoutInflater inflater;
    HashSet<Date> eventDays = new HashSet<>();
    Calendar curentInstance;

    Calendar selectedInstance;
    int flag;

    List<View> selectedView = new ArrayList<>();
    HashMap<Integer,View> views = new HashMap<>();
    List<View> monthViews = new ArrayList<>();
    HashMap<Integer,ArrayList<View>> monthWeekPair = new HashMap<>();

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays, Calendar calendar)
    {
        super(context, R.layout.custom_calendar_day, days);
        this.eventDays = eventDays;
        inflater = LayoutInflater.from(context);
        this.curentInstance=calendar;
        selectedInstance = Calendar.getInstance();
        selectedInstance.setTime(new Date());
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {

        // day in question
        Calendar calendar = Calendar.getInstance();
        Date date = getItem(position);
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);



        Calendar calendarToday = curentInstance;

        if (view == null) {
            view = inflater.inflate(R.layout.custom_calendar_day, parent, false);
        }



        TextView customTextView = view.findViewById(R.id.datetxt);

        customTextView.setTypeface(null, Typeface.NORMAL);


        if (month == calendarToday.get(Calendar.MONTH) && year == calendarToday.get(Calendar.YEAR)) {
            customTextView.setTextColor(Color.BLACK);
            addMonthWeek(date,view);
            monthViews.add(view);
            views.put(position,view);
        } else {
            customTextView.setTextColor(Color.parseColor("#E0E0E0"));
            views.put(position,view);
        }

        customTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag==0) {
                    if(selectedView.size()>0) {
                        resetColor(selectedView,R.color.transparent);
                        ((TextView)selectedView.get(0).findViewById(R.id.datetxt))
                                .setTextColor(Color.BLACK);
                        selectedView = new ArrayList<>();
                    }
                    selectedView.add(v);
                    v.setBackgroundResource(R.drawable.circle);
                    ((TextView)v.findViewById(R.id.datetxt))
                            .setTextColor(Color.parseColor("#EE0202"));
                   // resetColor(selectedView,R.color.colorBackground);
                    Toast.makeText(v.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                } else if(flag==1){

                    if(selectedView.size()>0) {
                        resetColor(selectedView,R.color.transparent);
                        selectedView = new ArrayList<>();
                    }
                    setColorSelectedWeek(getRow(position));
                } else {

                    if(selectedView.size()>0) {

                        resetColor(selectedView,R.color.transparent);
                        selectedView = new ArrayList<>();
                    }
                  selectedMonth();

                }
            }
        });
        return view;
    }

    public void setMonthViews(List<View> monthViews) {
        this.monthViews = monthViews;
        monthWeekPair=new HashMap<>();
    }

    public void resetColor(List<View> selectedView,int color){
        for(int i=0;i<selectedView.size();i++){
            selectedView.get(i).setBackgroundResource(color);

           /* ((TextView)selectedView.get(i).findViewById(R.id.datetxt))
                    .setTextColor(Color.BLACK);*/

        }
    }


    public void setWeekColor(List<View> sviews) {


        if(sviews.size()==1){
            sviews.get(0).setBackgroundResource(R.drawable.single);
        } else {
            sviews.get(0).setBackgroundResource(R.drawable.start);
            for (int i = 1; i < sviews.size() - 1; i++) {
                sviews.get(i).setBackgroundResource(R.drawable.middle);
            }
            sviews.get(sviews.size() -1).setBackgroundResource(R.drawable.end);
        }

    }


    public int getRow(int position){
        return position/7;
    }

    public void setColorSelectedWeek(int week){

        if(week<5) {
            selectedView = new ArrayList<>();

            int min = 7 * week;
            int max = min + 7;


            for (int i = min; i < max; i++) {
                Log.d("week0120", String.valueOf(i));
                selectedView.add(views.get(i));
            }
            setWeekColor(selectedView);
        }
    }

    public void selectedMonth(){

        selectedView.addAll(monthViews);

        setWeekMonthColor();

    }

    public void setWeekMonthColor() {

        for(Integer key :monthWeekPair.keySet()){
            setWeekColor(monthWeekPair.get(key));
        }
    }


    public int getWeeknumber(Date d){
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(d);
        return c.get(Calendar.WEEK_OF_MONTH);
    }



    public void addMonthWeek(Date date,View v){

        int weeknumber = getWeeknumber(date);
        Log.d("WeekOOO",String.valueOf(date)+" weeknumber "+weeknumber);

        if(monthWeekPair.containsKey(weeknumber)){
            ArrayList<View> viewArrayList = monthWeekPair.get(weeknumber);
            if(!viewArrayList.contains(v)) {
                viewArrayList.add(v);
            }
            monthWeekPair.put(weeknumber,viewArrayList);

        } else {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            monthWeekPair.put(weeknumber,viewArrayList);
        }
    }



}
