package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import com.example.hesolutions.horizon.HomePage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
    RelativeLayout layout1, relativeblur;
    CheckBox Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
    Integer day;
    ListView sectorlistView;
    byte[] SetParams;
    MyCustomAdapter deviceAdapter = null;
    SeekBar seekBar;

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
        seekBar = (SeekBar)findViewById(R.id.seekBar);

        final SimpleDateFormat ddf = new SimpleDateFormat("MMM dd, yyyy");
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        String currentdate = ddf.format(new java.util.Date());
        startdate.setText(currentdate);
        finishdate.setText(currentdate);
        String currenttime = sdf.format(new java.util.Date());
        starttime.setText(currenttime);
        finishtime.setText(currenttime);
        final Calendar startTime = Calendar.getInstance();
        final Calendar finishTime = Calendar.getInstance();

        day =startTime.get(Calendar.DAY_OF_WEEK)-1;
        weeknumber.setFilters(new InputFilter[]{new InputFiletMinMax("1","52")});
        ImageView homescreenBgImage = (ImageView) findViewById(R.id.imageView);
        Bitmap cachedBitmap = DataManager.getInstance().getBitmap();
        if (cachedBitmap != null) {
            Bitmap blurredBitmap = BlurBuilder.blur(this, cachedBitmap);
            homescreenBgImage.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        }


//========================================Loading the sector info
        HashMap<String, HashMap> sector = DataManager.getInstance().getsector();
        String username = DataManager.getInstance().getUsername();
        ArrayList<Group> arrayList = new ArrayList<Group>();
        if (sector.get(username)==null) {}
        else {
            HashMap<String, ArrayList> sectordetails = sector.get(username);
            for (Map.Entry<String, ArrayList> entry : sectordetails.entrySet()) {
                String key = entry.getKey();
                ArrayList<Device> value = entry.getValue();
                Group group = new Group(key, value, false);
                arrayList.add(group);
            }
            Group selected = new Group("Select All", null, false);
            arrayList.add(selected);


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
                Context context = getParent();
                new DatePickerDialog(context, date, startTime
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
                    startTime.set(Calendar.SECOND,0);
                    starttime.setText(sdf.format(startTime.getTime()));

                    finishTime.set(Calendar.HOUR_OF_DAY, Hour);
                    finishTime.set(Calendar.MINUTE, Minute);
                    finishTime.set(Calendar.SECOND,0);
                    finishtime.setText(sdf.format(startTime.getTime()));
                }
            };

            @Override
            public void onClick(View v) {
                Context context = getParent();
                new TimePickerDialog(context, time, startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), true).show();

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
                Context context = getParent();
                new DatePickerDialog(context, date, finishTime
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
                    finishTime.set(Calendar.SECOND,0);
                    finishtime.setText(sdf.format(finishTime.getTime()));
                }
            };
            @Override
            public void onClick(View v) {
                Context context = getParent();
                new TimePickerDialog(context, time, finishTime.get(Calendar.HOUR_OF_DAY),finishTime.get(Calendar.MINUTE),true).show();
            }
        });
        //===================================================================================================

        cancelTOcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Apply.setOnClickListener(new View.OnClickListener() {
            String cname = DataManager.getInstance().getUsername();
            String colorname = DataManager.getInstance().getcolorname();
            int colorName = Color.parseColor(colorname);
            long oldid;
            final List<WeekViewEvent> list = DataManager.getInstance().getevents();
            final List<Long> IDlist = DataManager.getInstance().getEventID();
            final List<WeekViewEvent> newlist = DataManager.getInstance().getnewevents();
            @Override
            public void onClick(final View v) {
                final ArrayList<Device> choosedevice = new ArrayList<Device>();
                if (choosedevice.isEmpty()) {
                    ArrayList<Group> choosegrouplist = deviceAdapter.arrayList;
                    for (int i = 0; i < choosegrouplist.size(); i++) {
                        Group group = choosegrouplist.get(i);
                        if (group.getSelected() == true && group.getList()!=null) {
                            ArrayList<Device> devicelist = group.getList();
                            for (int j = 0; j < devicelist.size(); j++) {
                                Device device = devicelist.get(j);
                                device.setCurrentParams(SetParams);
                                choosedevice.add(device);
                            }
                        }
                    }
                }

                if (choosedevice.isEmpty()||choosedevice==null) {
                    Toast.makeText(CalendarTask.this, "At least one group should be selected", Toast.LENGTH_SHORT).show();
                } else {
                    // Repetition
                    if (switch1.isChecked()) {
                        if (!weeknumber.getText().toString().isEmpty()) {
                            weeks = Integer.parseInt(weeknumber.getText().toString());
                            if (weeks > 0) {
                                if ((finishTime.after(startTime))) {

                                    if (!IDlist.isEmpty()) {
                                        oldid = IDlist.get((IDlist.size() - 1));
                                    }
                                    final AtomicLong counter = new AtomicLong(oldid);
                                    Thread[] threads = new Thread[7];

                                    for (int i = 0; i < weeks; i++) {
                                        final int j = i;
                                        threads[0] = new Thread()
                                        {
                                            public void run()
                                            {
                                                if (Monday.isChecked()) {
                                                    long id = counter.incrementAndGet();
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
                                                    if (Calendar.getInstance().get(Calendar.YEAR) == event.getStartTime().get(Calendar.YEAR)
                                                            && Calendar.getInstance().get(Calendar.MONTH) == event.getStartTime().get(Calendar.MONTH)) {
                                                        System.out.println(newlist.size() + "***********MONDAY");
                                                        newlist.add(event);
                                                    }else {
                                                        list.add(event);
                                                    }
                                                    IDlist.add(id);
                                                }
                                            }
                                        };
                                        threads[0].start();
                                        threads[1] = new Thread()
                                        {
                                            public void run()
                                            {
                                                if (Tuesday.isChecked()) {
                                                    long id = counter.incrementAndGet();
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
                                                    if (Calendar.getInstance().get(Calendar.YEAR) == event.getStartTime().get(Calendar.YEAR)
                                                            && Calendar.getInstance().get(Calendar.MONTH) == event.getStartTime().get(Calendar.MONTH)) {
                                                        newlist.add(event);
                                                    }else {
                                                        list.add(event);
                                                    }
                                                    IDlist.add(id);

                                                }
                                            }
                                        };
                                        threads[1].start();


                                        threads[2] = new Thread()
                                        {
                                            public void run()
                                            {
                                                if (Wednesday.isChecked()) {
                                                    long id = counter.incrementAndGet();
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
                                                    if (Calendar.getInstance().get(Calendar.YEAR) == event.getStartTime().get(Calendar.YEAR)
                                                            && Calendar.getInstance().get(Calendar.MONTH) == event.getStartTime().get(Calendar.MONTH)) {
                                                        newlist.add(event);
                                                    }else {
                                                        list.add(event);
                                                    }
                                                    IDlist.add(id);

                                                }
                                            }
                                        };
                                        threads[2].start();

                                        threads[3] = new Thread()
                                        {
                                            public void run()
                                            {
                                                if (Thursday.isChecked()) {
                                                    long id = counter.incrementAndGet();
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
                                                    if (Calendar.getInstance().get(Calendar.YEAR) == event.getStartTime().get(Calendar.YEAR)
                                                            && Calendar.getInstance().get(Calendar.MONTH) == event.getStartTime().get(Calendar.MONTH)) {
                                                        newlist.add(event);
                                                    }else {
                                                        list.add(event);
                                                    }
                                                    IDlist.add(id);
                                                }
                                            }
                                        };
                                        threads[3].start();


                                        threads[4] = new Thread()
                                        {
                                            public void run()
                                            {
                                                if (Friday.isChecked()) {
                                                    long id = counter.incrementAndGet();
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
                                                    if (Calendar.getInstance().get(Calendar.YEAR) == event.getStartTime().get(Calendar.YEAR)
                                                            && Calendar.getInstance().get(Calendar.MONTH) == event.getStartTime().get(Calendar.MONTH)) {
                                                        newlist.add(event);
                                                    }else {
                                                        list.add(event);
                                                    }
                                                    IDlist.add(id);
                                                }
                                            }
                                        };
                                        threads[4].start();

                                        threads[5] = new Thread()
                                        {
                                            public void run()
                                            {
                                                if (Saturday.isChecked()) {
                                                    long id = counter.incrementAndGet();
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
                                                    if (Calendar.getInstance().get(Calendar.YEAR) == event.getStartTime().get(Calendar.YEAR)
                                                            && Calendar.getInstance().get(Calendar.MONTH) == event.getStartTime().get(Calendar.MONTH)) {
                                                        newlist.add(event);
                                                    }else {
                                                        list.add(event);
                                                    }
                                                    IDlist.add(id);
                                                }
                                            }
                                        };
                                        threads[5].start();


                                        threads[6] = new Thread()
                                        {
                                            public void run()
                                            {
                                                if (Sunday.isChecked()) {
                                                    long id = counter.incrementAndGet();
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
                                                    if (Calendar.getInstance().get(Calendar.YEAR) == event.getStartTime().get(Calendar.YEAR)
                                                            && Calendar.getInstance().get(Calendar.MONTH) == event.getStartTime().get(Calendar.MONTH)) {
                                                        newlist.add(event);
                                                    }else {
                                                        list.add(event);
                                                    }
                                                    IDlist.add(id);
                                                }
                                            }
                                        };
                                        threads[6].start();
                                    }

                                    for (Thread thread:threads) {
                                        try {
                                            thread.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    DataManager.getInstance().setEventID(IDlist);
                                    DataManager.getInstance().setevents(list);
                                    DataManager.getInstance().setnewevents(newlist);
                                    System.out.println("old" + list.size() + "*********************");
                                    System.out.println("new" + newlist.size() + "***********************");
                                    finish();

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
                            long id = 0;
                            if (!IDlist.isEmpty()) {
                                id = IDlist.get((IDlist.size() - 1)) + 1;
                            }
                            WeekViewEvent event = new WeekViewEvent(id, cname, startTime, finishTime, colorName, choosedevice);
                            int year = Calendar.getInstance().get(Calendar.YEAR);
                            int month = Calendar.getInstance().get(Calendar.MONTH);
                            if (year == event.getStartTime().get(Calendar.YEAR) && month == event.getStartTime().get(Calendar.MONTH)) {
                                newlist.add(event);
                            }else {
                                list.add(event);
                            }
                            IDlist.add(id);
                            DataManager.getInstance().setevents(list);
                            DataManager.getInstance().setEventID(IDlist);
                            DataManager.getInstance().setnewevents(newlist);
                            finish();
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(CalendarTask.this, "Unvalid Time", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }

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


        seekBar.setProgress(100);
        SetParams = new byte[5];
        SetParams[0] = (byte) 17;
        SetParams[1] = (byte) 100;
        SetParams[2] = (byte) 0;
        SetParams[3] = (byte) 0;
        SetParams[4] = (byte) 0;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SetParams[1] = (byte) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private class MyCustomAdapter extends ArrayAdapter<Group> {
        ArrayList<Group> arrayList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Group> arrayList) {
            super(context, textViewResourceId, arrayList);
            this.arrayList = arrayList;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.devicelist, null);

            }

            Group group = arrayList.get(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            final EnhancedSwitch checked = (EnhancedSwitch) convertView.findViewById(R.id.checked);
            name.setText(group.getName());
            checked.setCheckedProgrammatically(group.getSelected());
            checked.setTag(group);

            checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Group group = (Group) buttonView.getTag();
                    if (checked.isChecked() == true) {
                        if (group.getName().equals("Select All")) {
                            for (int k = 0; k < arrayList.size(); k++) {
                                Group every = arrayList.get(k);
                                every.setSelected(true);
                            }
                        } else {
                            group.setSelected(true);

                        }
                    } else {
                        if (group.getName().equals("Select All")) {
                            for (int k = 0; k < arrayList.size(); k++) {
                                Group every = arrayList.get(k);
                                every.setSelected(false);
                            }
                        } else {
                            group.setSelected(false);
                        }

                    }

                    boolean check = true;
                    for (int m = 0; m <arrayList.size()-1; m++)
                    {
                        if (arrayList.get(m).getSelected() == true){}
                        else{check = false;}
                    }
                    if (check == true)
                    {
                        arrayList.get(arrayList.size()-1).setSelected(true);
                    }else
                    {
                        arrayList.get(arrayList.size()-1).setSelected(false);
                    }

                    notifyDataSetChanged();
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

        public boolean getSelected()
        {
            return ischecked;
        }
        public void setSelected(boolean ischecked)
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
