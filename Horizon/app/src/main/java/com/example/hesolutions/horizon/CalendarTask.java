package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.mylibrary.WeekView;
import com.mylibrary.WeekViewEvent;
import com.google.common.collect.BiMap;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class CalendarTask extends Activity {

    TextView startdate;
    TextView starttime;
    TextView finishdate;
    TextView finishtime;
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
    ListView sectorlistView;

    MyCustomAdapter deviceAdapter = null;

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
        sectorlistView = (ListView)findViewById(R.id.sectorlistView);

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
//========================================Loading the sector info
        BiMap<String, BiMap> sector = DataManager.getInstance().getsector();
        String username = DataManager.getInstance().getUsername();
        ArrayList<Group> arrayList = new ArrayList<Group>();
        if (sector.get(username)==null) {}
        else {
            BiMap<String, ArrayList> sectordetails = sector.get(username);
            for (Map.Entry<String, ArrayList> entry : sectordetails.entrySet()) {
                String key = entry.getKey();
                ArrayList value = entry.getValue();
                Group group = new Group(key, value, false);
                arrayList.add(group);
            }


            deviceAdapter = new MyCustomAdapter(this, R.layout.devicelist, arrayList);
            sectorlistView.setAdapter(deviceAdapter);
        }

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

                    finishTime.set(Calendar.YEAR, year);
                    finishTime.set(Calendar.MONTH, monthOfYear);
                    finishTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    finishdate.setText(ddf.format(startTime.getTime()));

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

                    finishTime.set(Calendar.HOUR_OF_DAY, Hour);
                    finishTime.set(Calendar.MINUTE, Minute);
                    finishtime.setText(sdf.format(startTime.getTime()));
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
                Intent intent1 = new Intent(v.getContext(), GlobalCalendar.class);
                startActivity(intent1);
            }
        });

        Apply.setOnClickListener(new View.OnClickListener() {
            String cname = DataManager.getInstance().getUsername();
            String colorname = DataManager.getInstance().getcolorname();
            int colorName = Color.parseColor(colorname);
            long id, oldid;
            final List<WeekViewEvent> list = DataManager.getInstance().getevents();
            final List<Long> IDlist = DataManager.getInstance().getEventID();
            final List<List<Long>> grouplist = DataManager.getInstance().getGroupID();

            @Override
            public void onClick(final View v) {

                final ArrayList<Device> choosedevice = new ArrayList<Device>();
                if (!choosedevice.isEmpty()) {
                    ArrayList<Group> choosegrouplist = deviceAdapter.arrayList;
                    for (int i = 0; i < choosegrouplist.size(); i++) {
                        Group group = choosegrouplist.get(i);
                        if (group.getChecked() == true) {
                            ArrayList<Device> devicelist = group.getList();
                            for (int j = 0; j < devicelist.size(); j++) {
                                Device device = devicelist.get(j);
                                choosedevice.add(device);
                            }
                        }
                    }
                }

                if (choosedevice.isEmpty()||choosedevice==null) {
                    Toast.makeText(CalendarTask.this, "At least one group should be selected", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final Intent intent = new Intent(v.getContext(), GlobalCalendar.class);
                            // Repetition
                            if (switch1.isChecked()) {
                                if (!weeknumber.getText().toString().isEmpty()) {
                                    weeks = Integer.parseInt(weeknumber.getText().toString());

                                    final List<Long> groupedlist = new ArrayList<Long>();

                                    if (weeks > 0) {
                                        if ((finishTime.after(startTime))) {
                                            // repeat i weeks

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    if (!IDlist.isEmpty()) {
                                                        oldid = IDlist.get((IDlist.size() - 1));
                                                    }
                                                    final AtomicLong counter = new AtomicLong(oldid);
                                                    for (int i = 0; i < weeks; i++) {
                                                        final int j = i;
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (Monday.isChecked()) {
                                                                    id = counter.incrementAndGet();
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
                                                                    event = new WeekViewEvent(id, cname, MonSt, MonFi, colorName, choosedevice);
                                                                    list.add(event);
                                                                    groupedlist.add(id);
                                                                    IDlist.add(id);

                                                                }
                                                            }
                                                        }).start();

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (Tuesday.isChecked()) {
                                                                    id = counter.incrementAndGet();
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
                                                                    event = new WeekViewEvent(id, cname, TueSt, TusFi, colorName, choosedevice);
                                                                    list.add(event);
                                                                    groupedlist.add(id);
                                                                    IDlist.add(id);

                                                                }
                                                            }
                                                        }).start();
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (Wednesday.isChecked()) {
                                                                    id = counter.incrementAndGet();
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
                                                                    event = new WeekViewEvent(id, cname, WedSt, WedFi, colorName, choosedevice);
                                                                    list.add(event);
                                                                    groupedlist.add(id);
                                                                    IDlist.add(id);

                                                                }
                                                            }
                                                        }).start();
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (Thursday.isChecked()) {
                                                                    id = counter.incrementAndGet();
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
                                                                    event = new WeekViewEvent(id, cname, ThuSt, ThuFi, colorName, choosedevice);
                                                                    list.add(event);
                                                                    groupedlist.add(id);
                                                                    IDlist.add(id);

                                                                }
                                                            }
                                                        }).start();
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (Friday.isChecked()) {
                                                                    id = counter.incrementAndGet();
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
                                                                    event = new WeekViewEvent(id, cname, FriSt, FriFi, colorName, choosedevice);
                                                                    list.add(event);
                                                                    groupedlist.add(id);
                                                                    IDlist.add(id);

                                                                }
                                                            }
                                                        }).start();
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (Saturday.isChecked()) {
                                                                    id = counter.incrementAndGet();
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
                                                                    event = new WeekViewEvent(id, cname, SatSt, SatFi, colorName, choosedevice);
                                                                    list.add(event);
                                                                    IDlist.add(id);
                                                                    groupedlist.add(id);

                                                                }
                                                            }
                                                        }).start();

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (Sunday.isChecked()) {
                                                                    id = counter.incrementAndGet();

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
                                                                    event = new WeekViewEvent(id, cname, SunSt, SunFi, colorName, choosedevice);
                                                                    list.add(event);
                                                                    IDlist.add(id);
                                                                    groupedlist.add(id);

                                                                }
                                                            }
                                                        }).start();

                                                    }
                                                    grouplist.add(groupedlist);
                                                    DataManager.getInstance().setGroupID(grouplist);
                                                    DataManager.getInstance().setEventID(IDlist);
                                                    DataManager.getInstance().setevents(list);
                                                    startActivity(intent);

                                                }
                                            }).start();

                                        } else {
                                            runOnUiThread(new Runnable() {
                                                public void run() {

                                                    Toast.makeText(CalendarTask.this, "Unvaild time", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            public void run() {

                                                Toast.makeText(CalendarTask.this, "Enter a valid week number (at least 1)", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            Toast.makeText(CalendarTask.this, "Please enter a number", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }

                            // not repetition
                            if (switch1.isChecked() == false) {
                                if ((finishTime.after(startTime))) {
                                    if (!IDlist.isEmpty()) {
                                        id = IDlist.get((IDlist.size() - 1)) + 1;
                                    }
                                    WeekViewEvent event = new WeekViewEvent(id, cname, startTime, finishTime, colorName, choosedevice);
                                    list.add(event);
                                    IDlist.add(id);
                                    DataManager.getInstance().setevents(list);
                                    DataManager.getInstance().setEventID(IDlist);
                                    startActivity(intent);
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            Toast.makeText(CalendarTask.this, "Unvalid Time", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }
                        }
                    }).start();


                }
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


    private class MyCustomAdapter extends ArrayAdapter<Group> {
        ArrayList<Group> arrayList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Group> arrayList) {
            super(context, textViewResourceId, arrayList);
            this.arrayList = new ArrayList<Group>();
            this.arrayList.addAll(arrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.devicelist, null);

            }

            Group group = arrayList.get(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            final CheckBox checked = (CheckBox) convertView.findViewById(R.id.checked);
            checked.setTag(group);
            name.setText(group.getName());
            checked.setText("");


            checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Group group = (Group) v.getTag();
                    if (checked.isChecked()) {
                        group.setChecked(true);
                    } else group.setChecked(false);
                }
            });

            return convertView;

        }
    }

    public class Group
    {
        String name;
        ArrayList<Device> devicelist;
        boolean ischecked;
        public Group(String name, ArrayList devicelist, boolean ischecked)
        {
            this.name = name;
            this.devicelist = devicelist;
            this.ischecked = ischecked;
        }

        public boolean getChecked()
        {
            return ischecked;
        }
        public void setChecked(boolean ischecked)
        {
            this.ischecked = ischecked;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public ArrayList getList() {
            return devicelist;
        }
        public void setList(ArrayList devicelist) {
            this.devicelist = devicelist;
        }
    }


}
