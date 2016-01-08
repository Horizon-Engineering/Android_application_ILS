package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hesolutions.mylibrary.WeekView;
import com.example.hesolutions.mylibrary.WeekViewEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EditEvent extends Activity {

    TextView startdate;
    TextView starttime;
    TextView finishdate;
    TextView finishtime;
    Button Apply;
    Button cancelTOcalendar;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        startdate = (TextView)findViewById(R.id.startdate);
        starttime = (TextView)findViewById(R.id.starttime);
        finishdate = (TextView)findViewById(R.id.finishdate);
        finishtime = (TextView)findViewById(R.id.finishtime);
        Apply = (Button)findViewById(R.id.Apply);
        cancelTOcalendar = (Button)findViewById(R.id.cancelTOcalendar);
        delete = (Button)findViewById(R.id.delete);

        final String start1 = getIntent().getStringExtra("startdate");
        final String start2 = getIntent().getStringExtra("starttime");
        final String end1 = getIntent().getStringExtra("finishdate");
        final String end2 = getIntent().getStringExtra("finishtime");
        final String Id = getIntent().getStringExtra("eventID");
        final long ID = Long.parseLong(Id);


        final String cname = DataManager.getInstance().getUsername();
        final String colorname = DataManager.getInstance().getcolorname();
        final int colorName = Color.parseColor(colorname);
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        starttime.setText(start2);
        finishtime.setText(end2);
        startdate.setText(start1);
        finishdate.setText(end1);


        final Calendar startTime = Calendar.getInstance();
        final Calendar finishTime = Calendar.getInstance();

        final int year1 = Integer.parseInt(start1.substring(8,12));
        final String month1 = start1.substring(0, 3);
        final int date1 = Integer.parseInt(start1.substring(4, 6));
        final int hour1 = Integer.parseInt(start2.substring(0,2));
        final int min1 = Integer.parseInt(start2.substring(3,5));

        final int year2 = Integer.parseInt(end1.substring(8,12));
        final String month2 = end1.substring(0, 3);
        final int date2 = Integer.parseInt(end1.substring(4, 6));
        final int hour2 = Integer.parseInt(end2.substring(0, 2));
        final int min2 = Integer.parseInt(end2.substring(3, 5));

        startTime.set(Calendar.YEAR, year1);
        startTime.set(Calendar.MONTH, getMonth(month1));
        startTime.set(Calendar.DAY_OF_MONTH, date1);
        startTime.set(Calendar.HOUR_OF_DAY, hour1);
        startTime.set(Calendar.MINUTE, min1);
        finishTime.set(Calendar.YEAR, year2);
        finishTime.set(Calendar.MONTH, getMonth(month2));
        finishTime.set(Calendar.DAY_OF_MONTH, date2);
        finishTime.set(Calendar.HOUR_OF_DAY, hour2);
        finishTime.set(Calendar.MINUTE, min2);
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
                new DatePickerDialog(EditEvent.this, date, year1, getMonth(month1),date1).show();



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
                new TimePickerDialog(EditEvent.this, time, hour1, min1, true).show();
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
                new DatePickerDialog(EditEvent.this, date, year2, getMonth(month2),date2).show();

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
                new TimePickerDialog(EditEvent.this, time, hour2,min2,true).show();
            }
        });
        //===================================================================================================

        cancelTOcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(),GlobalCalendar.class);

                List<WeekViewEvent> list = new ArrayList<WeekViewEvent>();
                list = DataManager.getInstance().getevents();
                WeekViewEvent event = new WeekViewEvent(ID, cname, startTime, finishTime,colorName);
                list.add(event);
                DataManager.getInstance().setevents(list);

                startActivity(intent1);
            }


        });

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(),GlobalCalendar.class);
                startActivity(intent1);
            }
        });

        Apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (finishTime.after(startTime)) {


                    Intent intent = new Intent(v.getContext(), GlobalCalendar.class);

                    List<WeekViewEvent> list = new ArrayList<WeekViewEvent>();
                    list = DataManager.getInstance().getevents();
                    WeekViewEvent event = new WeekViewEvent(ID, cname, startTime, finishTime,colorName);
                    list.add(event);
                    DataManager.getInstance().setevents(list);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditEvent.this, "Unvalid Time", Toast.LENGTH_SHORT).show();
                }
            }

        });



    }

    int getMonth(String month)
    {
        switch (month)
        {
            case "Jan": return 0;
            case "Feb": return 1;
            case "Mar": return 2;
            case "Apr": return 3;
            case "May": return 4;
            case "Jun": return 5;
            case "Jul": return 6;
            case "Aug": return 7;
            case "Sep": return 8;
            case "Oct": return 9;
            case "Nov": return 10;
            case "Dec": return 11;
        }
        return 15;
    }
}
