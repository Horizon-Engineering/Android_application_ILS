package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
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


public class CalendarTask extends Activity {

    TextView startdate;
    TextView starttime;
    TextView finishdate;
    TextView finishtime;
    TextView textView6;
    Button Apply;
    Button cancelTOcalendar;
    Button delete;
    Switch switch1;
    EditText weeknumber;
    TextView textView4,textView5;
    Integer weeks;
    RelativeLayout layout1;
    CheckBox Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
    Integer day;


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
        switch1 =(Switch)findViewById(R.id.switch1);
        weeknumber = (EditText)findViewById(R.id.weeknumber);
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        Sunday = (CheckBox)findViewById(R.id.Sunday);
        Monday = (CheckBox)findViewById(R.id.Monday);
        Tuesday = (CheckBox)findViewById(R.id.Tuesday);
        Wednesday = (CheckBox)findViewById(R.id.Wednesday);
        Thursday = (CheckBox)findViewById(R.id.Thursday);
        Friday = (CheckBox)findViewById(R.id.Friday);
        Saturday = (CheckBox)findViewById(R.id.Saturday);
        textView6 = (TextView)findViewById(R.id.textView6);

        final SimpleDateFormat ddf = new SimpleDateFormat("MMM dd, yyyy");
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        String currentdate = ddf.format(new java.util.Date());
        startdate.setText(currentdate);
        finishdate.setText(currentdate);
        String currenttime = sdf.format(new java.util.Date());
        starttime.setText(currenttime);
        finishtime.setText(currenttime);
        final Calendar startTime = Calendar.getInstance();
        final Calendar finishTime = Calendar.getInstance();

        day =startTime.get(Calendar.DAY_OF_WEEK)-1;
//=======================================start date and time===============================================
        startdate.setOnClickListener(new View.OnClickListener() {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    startTime.set(Calendar.YEAR, year);
                    startTime.set(Calendar.MONTH, monthOfYear);
                    startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    startdate.setText(ddf.format(startTime.getTime()));
                    day =startTime.get(Calendar.DAY_OF_WEEK)-1;
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
                    finishdate.setText(ddf.format(finishTime.getTime()));
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
            String colorname = DataManager.getInstance().getcolorname();
            int colorName = Color.parseColor(colorname);
            long id;

            @Override
            public void onClick(final View v) {
                textView6.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                Intent intent = new Intent(v.getContext(), GlobalCalendar.class);
                // Repetition
                if (switch1.isChecked()) {
                    if (!weeknumber.getText().toString().isEmpty()) {
                        weeks = Integer.parseInt(weeknumber.getText().toString());
                        if (weeks > 0) {
                            if ((finishTime.after(startTime))) {
                                final List<WeekViewEvent> list = DataManager.getInstance().getevents();
                                final List<Long> IDlist = DataManager.getInstance().getEventID();
                                // repeat i weeks
                                for (int i = 0; i < weeks; i++) {
                                    final int j = i;

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Monday.isChecked()) {
                                                if (!IDlist.isEmpty()) {
                                                    id = IDlist.get((IDlist.size() - 1)) + 1;
                                                }
                                                Calendar MonSt = Calendar.getInstance(), MonFi = Calendar.getInstance();
                                                MonSt.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
                                                MonSt.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
                                                MonSt.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                MonSt.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                                                MonSt.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
                                                MonFi.set(Calendar.YEAR, finishTime.get(Calendar.YEAR));
                                                MonFi.set(Calendar.MONTH, finishTime.get(Calendar.MONTH));
                                                MonFi.set(Calendar.DAY_OF_MONTH, finishTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                MonFi.set(Calendar.HOUR_OF_DAY, finishTime.get(Calendar.HOUR_OF_DAY));
                                                MonFi.set(Calendar.MINUTE, finishTime.get(Calendar.MINUTE));
                                                Integer date = 1 - day;
                                                MonSt.add(Calendar.DAY_OF_MONTH, date);
                                                MonFi.add(Calendar.DAY_OF_MONTH, date);
                                                WeekViewEvent event;
                                                event = new WeekViewEvent(id, cname, MonSt, MonFi, colorName);
                                                list.add(event);
                                                IDlist.add(id);

                                            }
                                        }
                                    }).start();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Tuesday.isChecked()) {
                                                if (!IDlist.isEmpty()) {
                                                    id = IDlist.get((IDlist.size() - 1)) + 1;
                                                }
                                                Calendar TueSt = Calendar.getInstance(), TusFi = Calendar.getInstance();
                                                TueSt.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
                                                TueSt.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
                                                TueSt.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                TueSt.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                                                TueSt.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
                                                TusFi.set(Calendar.YEAR, finishTime.get(Calendar.YEAR));
                                                TusFi.set(Calendar.MONTH, finishTime.get(Calendar.MONTH));
                                                TusFi.set(Calendar.DAY_OF_MONTH, finishTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                TusFi.set(Calendar.HOUR_OF_DAY, finishTime.get(Calendar.HOUR_OF_DAY));
                                                TusFi.set(Calendar.MINUTE, finishTime.get(Calendar.MINUTE));
                                                Integer date = 2 - day;
                                                TueSt.add(Calendar.DAY_OF_MONTH, date);
                                                TusFi.add(Calendar.DAY_OF_MONTH, date);
                                                WeekViewEvent event;
                                                event = new WeekViewEvent(id, cname, TueSt, TusFi, colorName);
                                                list.add(event);
                                                IDlist.add(id);

                                            }
                                        }
                                    }).start();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Wednesday.isChecked()) {
                                                if (!IDlist.isEmpty()) {
                                                    id = IDlist.get((IDlist.size() - 1)) + 1;
                                                }
                                                Calendar WedSt = Calendar.getInstance(), WedFi = Calendar.getInstance();
                                                WedSt.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
                                                WedSt.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
                                                WedSt.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                WedSt.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                                                WedSt.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
                                                WedFi.set(Calendar.YEAR, finishTime.get(Calendar.YEAR));
                                                WedFi.set(Calendar.MONTH, finishTime.get(Calendar.MONTH));
                                                WedFi.set(Calendar.DAY_OF_MONTH, finishTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                WedFi.set(Calendar.HOUR_OF_DAY, finishTime.get(Calendar.HOUR_OF_DAY));
                                                WedFi.set(Calendar.MINUTE, finishTime.get(Calendar.MINUTE));
                                                Integer date = 3 - day;
                                                WedSt.add(Calendar.DAY_OF_MONTH, date);
                                                WedFi.add(Calendar.DAY_OF_MONTH, date);
                                                WeekViewEvent event;
                                                event = new WeekViewEvent(id, cname, WedSt, WedFi, colorName);
                                                list.add(event);
                                                IDlist.add(id);

                                            }
                                        }
                                    }).start();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Thursday.isChecked()) {
                                                if (!IDlist.isEmpty()) {
                                                    id = IDlist.get((IDlist.size() - 1)) + 1;
                                                }
                                                Calendar ThuSt = Calendar.getInstance(), ThuFi = Calendar.getInstance();
                                                ThuSt.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
                                                ThuSt.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
                                                ThuSt.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                ThuSt.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                                                ThuSt.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
                                                ThuFi.set(Calendar.YEAR, finishTime.get(Calendar.YEAR));
                                                ThuFi.set(Calendar.MONTH, finishTime.get(Calendar.MONTH));
                                                ThuFi.set(Calendar.DAY_OF_MONTH, finishTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                ThuFi.set(Calendar.HOUR_OF_DAY, finishTime.get(Calendar.HOUR_OF_DAY));
                                                ThuFi.set(Calendar.MINUTE, finishTime.get(Calendar.MINUTE));
                                                Integer date = 4 - day;
                                                ThuSt.add(Calendar.DAY_OF_MONTH, date);
                                                ThuFi.add(Calendar.DAY_OF_MONTH, date);
                                                WeekViewEvent event;
                                                event = new WeekViewEvent(id, cname, ThuSt, ThuFi, colorName);
                                                list.add(event);
                                                IDlist.add(id);

                                            }
                                        }
                                    }).start();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Friday.isChecked()) {
                                                if (!IDlist.isEmpty()) {
                                                    id = IDlist.get((IDlist.size() - 1)) + 1;
                                                }
                                                Calendar FriSt = Calendar.getInstance(), FriFi = Calendar.getInstance();
                                                FriSt.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
                                                FriSt.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
                                                FriSt.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                FriSt.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                                                FriSt.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
                                                FriFi.set(Calendar.YEAR, finishTime.get(Calendar.YEAR));
                                                FriFi.set(Calendar.MONTH, finishTime.get(Calendar.MONTH));
                                                FriFi.set(Calendar.DAY_OF_MONTH, finishTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                FriFi.set(Calendar.HOUR_OF_DAY, finishTime.get(Calendar.HOUR_OF_DAY));
                                                FriFi.set(Calendar.MINUTE, finishTime.get(Calendar.MINUTE));
                                                Integer date = 5 - day;
                                                FriSt.add(Calendar.DAY_OF_MONTH, date);
                                                FriFi.add(Calendar.DAY_OF_MONTH, date);
                                                WeekViewEvent event;
                                                event = new WeekViewEvent(id, cname, FriSt, FriFi, colorName);
                                                list.add(event);
                                                IDlist.add(id);

                                            }
                                        }
                                    }).start();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Saturday.isChecked()) {
                                                if (!IDlist.isEmpty()) {
                                                    id = IDlist.get((IDlist.size() - 1)) + 1;
                                                }
                                                Calendar SatSt = Calendar.getInstance(), SatFi = Calendar.getInstance();
                                                SatSt.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
                                                SatSt.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
                                                SatSt.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                SatSt.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                                                SatSt.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
                                                SatFi.set(Calendar.YEAR, finishTime.get(Calendar.YEAR));
                                                SatFi.set(Calendar.MONTH, finishTime.get(Calendar.MONTH));
                                                SatFi.set(Calendar.DAY_OF_MONTH, finishTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                SatFi.set(Calendar.HOUR_OF_DAY, finishTime.get(Calendar.HOUR_OF_DAY));
                                                SatFi.set(Calendar.MINUTE, finishTime.get(Calendar.MINUTE));

                                                Integer date = 6 - day;
                                                SatSt.add(Calendar.DAY_OF_MONTH, date);
                                                SatFi.add(Calendar.DAY_OF_MONTH, date);
                                                WeekViewEvent event;
                                                event = new WeekViewEvent(id, cname, SatSt, SatFi, colorName);
                                                list.add(event);
                                                IDlist.add(id);

                                            }
                                        }
                                    }).start();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Sunday.isChecked()) {
                                                if (!IDlist.isEmpty()) {
                                                    id = IDlist.get((IDlist.size() - 1)) + 1;
                                                }
                                                Calendar SunSt = Calendar.getInstance(), SunFi = Calendar.getInstance();
                                                SunSt.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
                                                SunSt.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
                                                SunSt.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                SunSt.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                                                SunSt.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
                                                SunFi.set(Calendar.YEAR, finishTime.get(Calendar.YEAR));
                                                SunFi.set(Calendar.MONTH, finishTime.get(Calendar.MONTH));
                                                SunFi.set(Calendar.DAY_OF_MONTH, finishTime.get(Calendar.DAY_OF_MONTH) + 7 * j);
                                                SunFi.set(Calendar.HOUR_OF_DAY, finishTime.get(Calendar.HOUR_OF_DAY));
                                                SunFi.set(Calendar.MINUTE, finishTime.get(Calendar.MINUTE));

                                                Integer date = 0 - day;
                                                SunSt.add(Calendar.DAY_OF_MONTH, date);
                                                SunFi.add(Calendar.DAY_OF_MONTH, date);
                                                WeekViewEvent event;
                                                event = new WeekViewEvent(id, cname, SunSt, SunFi, colorName);
                                                list.add(event);
                                                IDlist.add(id);

                                            }
                                        }
                                    }).start();

                                }


                                DataManager.getInstance().setevents(list);
                                DataManager.getInstance().setEventID(IDlist);
                                startActivity(intent);
                            } else {
                                Toast.makeText(CalendarTask.this, "Unvalid Time", Toast.LENGTH_SHORT).show();
                                textView6.setVisibility(View.GONE);
                            }
                        } else
                        {Toast.makeText(CalendarTask.this, "Enter a valid week number (at least one)", Toast.LENGTH_SHORT).show();
                            textView6.setVisibility(View.GONE);}

                    }else {Toast.makeText(CalendarTask.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                        textView6.setVisibility(View.GONE);}
                }

                // not repetition
                if (switch1.isChecked()==false) {
                    textView6.setVisibility(View.VISIBLE);
                    final List<WeekViewEvent> list = DataManager.getInstance().getevents();
                    final List<Long> IDlist = DataManager.getInstance().getEventID();
                    if ((finishTime.after(startTime))) {
                        if (!IDlist.isEmpty()) {
                            id = IDlist.get((IDlist.size() - 1)) + 1;
                        }
                        WeekViewEvent event = new WeekViewEvent(id, cname, startTime, finishTime, colorName);
                        list.add(event);
                        IDlist.add(id);
                        DataManager.getInstance().setevents(list);
                        DataManager.getInstance().setEventID(IDlist);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CalendarTask.this, "Unvalid Time", Toast.LENGTH_SHORT).show();
                        textView6.setVisibility(View.GONE);
                    }

                }
                    }
                }).start();
            }

        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                    startdate.setEnabled(false);
                    finishdate.setEnabled(false);
                    starttime.setEnabled(false);
                    finishtime.setEnabled(false);

                    for (int i = 0; i < layout1.getChildCount(); i++) {
                        View child = layout1.getChildAt(i);
                        child.setEnabled(true);
                    }


                    if (day.equals(1)) {
                        Monday.setChecked(true);
                        Monday.setEnabled(false);
                    } else if (day.equals(2)) {
                        Tuesday.setChecked(true);
                        Tuesday.setEnabled(false);
                    } else if (day.equals(3)) {
                        Wednesday.setChecked(true);
                        Wednesday.setEnabled(false);
                    } else if (day.equals(4)) {
                        Thursday.setChecked(true);
                        Thursday.setEnabled(false);
                    } else if (day.equals(5)) {
                        Friday.setChecked(true);
                        Friday.setEnabled(false);
                    } else if (day.equals(6)) {
                        Saturday.setChecked(true);
                        Saturday.setEnabled(false);
                    } else {
                        Sunday.setChecked(true);
                        Sunday.setEnabled(false);
                    }


                } else {
                    for (int i = 0; i < layout1.getChildCount(); i++) {
                        View child = layout1.getChildAt(i);
                        child.setEnabled(false);
                    }

                    startdate.setEnabled(true);
                    finishdate.setEnabled(true);
                    starttime.setEnabled(true);
                    finishtime.setEnabled(true);

                    Monday.setChecked(false);
                    Tuesday.setChecked(false);
                    Wednesday.setChecked(false);
                    Thursday.setChecked(false);
                    Friday.setChecked(false);
                    Saturday.setChecked(false);
                    Sunday.setChecked(false);


                }
            }
        });
    }


}
