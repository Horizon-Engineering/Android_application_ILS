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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hesolutions.mylibrary.WeekViewEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarTask extends Activity {

    TextView startdate;
    TextView starttime;
    TextView finishdate;
    TextView finishtime;
    Button Apply;
    Button cancelTOcalendar;
    Button delete;
    long eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_task);
        startdate = (TextView)findViewById(R.id.startdate);
        starttime = (TextView)findViewById(R.id.starttime);
        finishdate = (TextView)findViewById(R.id.finishdate);
        finishtime = (TextView)findViewById(R.id.finishtime);
        Apply = (Button)findViewById(R.id.Apply);
        cancelTOcalendar = (Button)findViewById(R.id.cancelTOcalendar);
        delete = (Button)findViewById(R.id.delete);

        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        String currentdate = DateFormat.getDateInstance().format(new java.util.Date());
        startdate.setText(currentdate);
        finishdate.setText(currentdate);
        String currenttime = sdf.format(new java.util.Date());
        starttime.setText(currenttime);
        finishtime.setText(currenttime);
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
                    startTime.set(Calendar.HOUR_OF_DAY, Hour);
                    startTime.set(Calendar.MINUTE, Minute);
                    starttime.setText(sdf.format(startTime.getTime()));
                   }
            };

            @Override
            public void onClick(View v) {
                new TimePickerDialog(CalendarTask.this, time, startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), true).show();

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
                    finishTime.set(Calendar.HOUR_OF_DAY, Hour);
                    finishTime.set(Calendar.MINUTE, Minute);
                    finishtime.setText(sdf.format(finishTime.getTime()));
                }
            };
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CalendarTask.this, time, finishTime.get(Calendar.HOUR_OF_DAY),finishTime.get(Calendar.MINUTE),true).show();
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
            long id;
            List<Long> IDlist = DataManager.getInstance().getEventID(cname);
            @Override
            public void onClick(View v) {

                if (finishTime.after(startTime)) {


                    Intent intent = new Intent(v.getContext(), GlobalCalendar.class);

                    for (int i = 0; i<IDlist.size();i++)
                    {
                        if (id == IDlist.get(i))
                        {
                            id = id + 1;
                        }
                    }

                    WeekViewEvent event = new WeekViewEvent(id, cname, startTime, finishTime);
                    event.setColor(getResources().getColor(R.color.event_color_01));

                    DataManager.getInstance().setevents(event);
                    startActivity(intent);
                }else{
                    Toast.makeText(CalendarTask.this, "Unvalid Time", Toast.LENGTH_SHORT).show();
                }
            }

        });



    }
}
