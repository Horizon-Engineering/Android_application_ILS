package com.example.hesolutions.horizon;
/*
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.mylibrary.WeekView;
import com.mylibrary.WeekViewEvent;

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
    TextView textView6;
    Button Apply;
    Button cancelTOcalendar;
    Button delete;
    CheckBox group;
    final List<WeekViewEvent> listevent = DataManager.getInstance().getevents();
    final List<List<Long>> grouplist = DataManager.getInstance().getGroupID();
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
        textView6 = (TextView)findViewById(R.id.textView6);
        group = (CheckBox)findViewById(R.id.group);

        final String start1 = getIntent().getStringExtra("startdate");
        final String start2 = getIntent().getStringExtra("starttime");
        final String end1 = getIntent().getStringExtra("finishdate");
        final String end2 = getIntent().getStringExtra("finishtime");
        final String Id = getIntent().getStringExtra("eventID");
        final ArrayList<Device> devicelist = (ArrayList<Device>)getIntent().getSerializableExtra("devicelist");
        final long ID = Long.parseLong(Id);


        final String cname = DataManager.getInstance().getUsername();
        final String colorname = DataManager.getInstance().getcolorname();
        final int colorName = Color.parseColor(colorname);
        final SimpleDateFormat ddf = new SimpleDateFormat("MMM dd, yyyy");
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
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

        ImageView homescreenBgImage = (ImageView) findViewById(R.id.imageView);
        Bitmap cachedBitmap = DataManager.getInstance().getBitmap();
        if (cachedBitmap != null) {
            Bitmap blurredBitmap = BlurBuilder.blur(this, cachedBitmap);
            homescreenBgImage.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
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

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                textView6.setVisibility(View.VISIBLE);
                        // delete a single event
                        if (group.isChecked()==false)
                        {
                            int ite = getGroup(ID);
                            if (ite!=(-1) &&ite!=(-2)) {
                                List<Long> groupid = grouplist.get(ite);
                                // if this event belongs to any group
                                if (!groupid.equals(null)) {
                                    groupid.remove(ID);
                                }
                                grouplist.set(ite, groupid);
                                DataManager.getInstance().setGroupID(grouplist);
                            }
                            CheckCurrent();
                            listevent.remove(getEvent(ID));
                            DataManager.getInstance().setevents(listevent);
                            finish();

                        }else
                        {
                            int ite = getGroup(ID);
                            if (ite!= (-1) && ite!=(-2)) {
                                List<Long> groupid = grouplist.get(ite);
                                if (!groupid.equals(null)) {
                                    for (int i = 0; i < groupid.size(); i++) {
                                        Long id = groupid.get(i);
                                        CheckCurrent();
                                        listevent.remove(getEvent(id));
                                    }
                                    grouplist.remove(groupid);
                                }
                                DataManager.getInstance().setevents(listevent);
                                DataManager.getInstance().setGroupID(grouplist);
                                finish();
                            }else if (ite == (-2)){
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(EditEvent.this, "No relative events", Toast.LENGTH_LONG).show();
                                    }
                                });
                                textView6.setVisibility(View.GONE);
                            }else if (ite == (-1)){
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(EditEvent.this, "No grouping files found", Toast.LENGTH_LONG).show();
                                    }
                                });
                                textView6.setVisibility(View.GONE);
                            }
                        }
            }
        });

        Apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                textView6.setVisibility(View.VISIBLE);
                        if (finishTime.after(startTime)) {
                            if (group.isChecked()==false) {
                                int ite = getGroup(ID);
                                if (ite != (-1) && ite != (-2)) {
                                    List<Long> groupid = grouplist.get(ite);
                                    if (!groupid.equals(null)) {
                                        groupid.remove(ID);
                                    }
                                    grouplist.set(ite, groupid);
                                    listevent.remove(getEvent(ID));
                                    DataManager.getInstance().setGroupID(grouplist);
                                    DataManager.getInstance().setevents(listevent);
                                }

                                listevent.remove(getEvent(ID));
                                WeekViewEvent event = new WeekViewEvent(ID, cname, startTime, finishTime, colorName,devicelist);
                                listevent.add(event);
                                DataManager.getInstance().setevents(listevent);
                                finish();
                            }else
                            {
                                int ite = getGroup(ID);
                                if (ite!= (-1) && ite!=(-2)) {
                                    List<Long> groupid = grouplist.get(ite);
                                    Intent intent1 = new Intent(v.getContext(), GlobalCalendar.class);
                                    if (!groupid.equals(null)) {
                                        for (int i = 0; i < groupid.size(); i++) {
                                            Long id = groupid.get(i);
                                            WeekViewEvent event = getEvent(id);
                                            listevent.remove(getEvent(id));
                                            Calendar start = Calendar.getInstance(), finish = Calendar.getInstance();
                                            start.set(Calendar.YEAR,event.getStartTime().get(Calendar.YEAR));
                                            start.set(Calendar.MONTH,event.getStartTime().get(Calendar.MONTH));
                                            start.set(Calendar.DAY_OF_MONTH, event.getStartTime().get(Calendar.DAY_OF_MONTH));
                                            start.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                                            start.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));

                                            finish.set(Calendar.YEAR, event.getEndTime().get(Calendar.YEAR));
                                            finish.set(Calendar.MONTH, event.getEndTime().get(Calendar.MONTH));
                                            finish.set(Calendar.DAY_OF_MONTH, event.getEndTime().get(Calendar.DAY_OF_MONTH));
                                            finish.set(Calendar.HOUR_OF_DAY, finishTime.get(Calendar.HOUR_OF_DAY));
                                            finish.set(Calendar.MINUTE, finishTime.get(Calendar.MINUTE));
                                            WeekViewEvent newevent = new WeekViewEvent(id, cname, start, finish, colorName,devicelist);
                                            listevent.add(newevent);
                                        }
                                    }
                                    DataManager.getInstance().setevents(listevent);
                                    startActivity(intent1);
                                }else if (ite == (-2)){
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(EditEvent.this, "No relative events", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    textView6.setVisibility(View.GONE);
                                }else if (ite == (-1)){
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(EditEvent.this, "No grouping files found", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    textView6.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(EditEvent.this, "Unvalid Time", Toast.LENGTH_LONG).show();
                                }
                            });
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

    public WeekViewEvent getEvent(Long id) {
        for (int i = 0; i < listevent.size(); i++) {
            WeekViewEvent event = listevent.get(i);
            if (id == event.getId()) return event;
        }
        return null;
    }


    public int getGroup(Long id) {
        if (!grouplist.isEmpty()) {
            for (int i = 0; i < grouplist.size(); i++) {
                List<Long> groupedlist = grouplist.get(i);
               for (int j = 0; j < groupedlist.size(); j++) {
                    if (groupedlist.get(j).equals(id)) {
                        return i;
                    }
                }
            }
            return -2;
        }

        return -1;
    }

    public void CheckCurrent()
    {
        WeekViewEvent event = DataManager.getInstance().getthisevent();
        Calendar cur = Calendar.getInstance();
        ArrayList<Device> devices = event.getdeviceList();
        if (cur.before(event.getEndTime())&&cur.after(event.getStartTime()))
        {
            for (int p=0; p <devices.size(); p++)
            {
                Device devicep = devices.get(p);
                byte[]data;
                data = new byte[]{(byte) 17, (byte) 0, devicep.getCurrentParams()[2], (byte) 0, (byte) 0};
                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                devicep.getDeviceAddress(), (short) 0, data), devicep.getGatewayMacAddr(), devicep.getGatewayPassword(),
                        devicep.getGatewaySSID(), EditEvent.this));
                devicep.setCurrentParams(data);
                DatabaseManager.getInstance().updateDevice(devicep);
            }
        }
    }
}
*/