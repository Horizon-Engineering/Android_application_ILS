package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alamkanak.weekview.WeekViewEvent;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;

public class CalendarTask extends Activity {

    TextView startdate;
    TextClock starttime;
    TextView finishdate;
    TextClock finishtime;
    Button Apply;
    Button cancelTOcalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_task);
        startdate = (TextView)findViewById(R.id.startdate);
        starttime = (TextClock)findViewById(R.id.starttime);
        finishdate = (TextView)findViewById(R.id.finishdate);
        finishtime = (TextClock)findViewById(R.id.finishtime);
        Apply = (Button)findViewById(R.id.Apply);
        cancelTOcalendar = (Button)findViewById(R.id.cancelTOcalendar);

        String currentdate = DateFormat.getDateInstance().format(new java.util.Date());
        startdate.setText(currentdate);
        finishdate.setText(currentdate);
        final Calendar startTime = Calendar.getInstance();
        final Calendar finishTime = Calendar.getInstance();



//=======================================start date and time===============================================
        startdate.setOnClickListener(new View.OnClickListener() {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    startTime.set(Calendar.YEAR, year);
                    startTime.set(Calendar.MONTH, monthOfYear);
                    startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    startdate.setText(DateFormat.getDateInstance().format(startTime.getTime()));
                }

            };


            @Override
            public void onClick(View v) {
                new DatePickerDialog(CalendarTask.this, date, startTime
                        .get(Calendar.YEAR), startTime.get(Calendar.MONTH),
                        startTime.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        starttime.setOnClickListener(new View.OnClickListener() {

            TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int Hour, int Minute) {
                    startTime.set(Calendar.HOUR, Hour);
                    startTime.set(Calendar.MINUTE, Minute);
                    starttime.setText(DateFormat.getTimeInstance().format(startTime.getTime()));
                }
            };

            @Override
            public void onClick(View v) {
                new TimePickerDialog(CalendarTask.this, time, startTime.get(Calendar.HOUR), startTime.get(Calendar.MINUTE), true).show();
            }
        });



//=================================finish date time==============================

        finishdate.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    finishTime.set(Calendar.YEAR, year);
                    finishTime.set(Calendar.MONTH, monthOfYear);
                    finishTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    finishdate.setText(DateFormat.getDateInstance().format(finishTime.getTime()));
                }

            };


            @Override
            public void onClick(View v) {
                new DatePickerDialog(CalendarTask.this, date, finishTime
                        .get(Calendar.YEAR), finishTime.get(Calendar.MONTH),
                        finishTime.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        finishtime.setOnClickListener(new View.OnClickListener() {

            TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker view, int Hour, int Minute)
                {
                    finishTime.set(Calendar.HOUR, Hour);
                    finishTime.set(Calendar.MINUTE, Minute);
                    finishtime.setText(DateFormat.getTimeInstance().format(finishTime.getTime()));
                }
            };
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CalendarTask.this, time, finishTime.get(Calendar.HOUR),finishTime.get(Calendar.MINUTE),true).show();
            }
        });
    //===================================================================================================

        cancelTOcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(),GlobalCalendar.class);
                startActivity(intent1);
            }
        });

        Apply.setOnClickListener(new View.OnClickListener() {
            String cname = DataManager.getInstance().getUsername();
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),GlobalCalendar.class);

                WeekViewEvent event = new WeekViewEvent(1, cname, startTime, finishTime);
                event.setColor(R.color.event_color_03);
                DataManager.getInstance().setevents(event);
                startActivity(intent);
            }

        });

    }
}
