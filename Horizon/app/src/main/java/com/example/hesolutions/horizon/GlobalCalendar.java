package com.example.hesolutions.horizon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.app.Activity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class GlobalCalendar extends Activity{
    Button addevent;
    Button backtouser;
    Button today;
    Button oneday;
    Button threedays;
    Button sevendays;
    private WeekView mWeekView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_calendar);
        addevent =(Button)findViewById(R.id.addevent);
        backtouser = (Button)findViewById(R.id.backtouser);
        today = (Button)findViewById(R.id.today);
        oneday = (Button)findViewById(R.id.oneday);
        threedays = (Button)findViewById(R.id.threedays);
        sevendays = (Button)findViewById(R.id.sevendays);
        mWeekView = (WeekView) findViewById(R.id.weekView);

        WeekView.EventClickListener mEventClickListener = new WeekView.EventClickListener()
        {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect)
            {

            }

        };
        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(mEventClickListener);

        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events;




                events = DataManager.getInstance().getevents();
                return events;
            }

        };
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(mMonthChangeListener);


        WeekView.EventLongPressListener mEventLongPressListener = new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

            }
        };

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);




        backtouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(GlobalCalendar.this, UserPage.class);
                startActivity(startNewActivityIntent);
            }
        });

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(GlobalCalendar.this, CalendarTask.class);
                startActivity(startNewActivityIntent);
            }
        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekView.goToToday();
            }
        });
        oneday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekView.setNumberOfVisibleDays(1);
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            }
        });
        threedays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekView.setNumberOfVisibleDays(3);
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            }
        });
        sevendays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekView.setNumberOfVisibleDays(7);
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

            }
        });

    }


}
