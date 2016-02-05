package com.example.hesolutions.horizon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.homa.hls.database.Device;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.mylibrary.WeekViewEvent;
import com.mylibrary.WeekView;
import com.mylibrary.MonthLoader;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GlobalCalendar extends Activity{
    Button addevent;
    Button today;
    Button oneday;
    Button threedays;
    Button sevendays;
    private WeekView mWeekView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_calendar);
        addevent =(Button)findViewById(R.id.addevent);
        today = (Button)findViewById(R.id.today);
        oneday = (Button)findViewById(R.id.oneday);
        threedays = (Button)findViewById(R.id.threedays);
        sevendays = (Button)findViewById(R.id.sevendays);
        mWeekView = (WeekView) findViewById(R.id.weekView);


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

                if (event.getName().equals(DataManager.getInstance().getUsername()))
                {

                    Intent editevent = new Intent(GlobalCalendar.this, EditEvent.class);

                    Date starttime = event.getStartTime().getTime();
                    Date endtime = event.getEndTime().getTime();
                    ArrayList<Device> devicelist = event.getdeviceList();
                    SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
                    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss ");
                    System.out.println("--------------------------------------------------------------------");
                    editevent.putExtra("eventID", Long.toString(event.getId()));
                    editevent.putExtra("startdate", date.format(starttime));
                    editevent.putExtra("starttime",time.format(starttime));
                    editevent.putExtra("finishdate",date.format(endtime));
                    editevent.putExtra("finishtime", time.format(endtime));
                    editevent.putExtra("devicelist", devicelist);

                    DataManager.getInstance().setthisevent(event);
                    ActivityStack activityStack = (ActivityStack) getParent();
                    activityStack.push("ThirdActivity", editevent);
                }else
                {
                    Toast.makeText(GlobalCalendar.this, "Don not have permission", Toast.LENGTH_SHORT).show();
                }

            }
        };

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);

        WeekView.EventClickListener mEventClickListener = new WeekView.EventClickListener()
        {
            @Override
            public void onEventClick(final WeekViewEvent event, RectF eventRect)
            {
                Toast.makeText(GlobalCalendar.this, "Created by " + event.getName() + "\nStarting at "
                        + event.getStartTime().getTime()+
                        "\nFinishing at " + event.getEndTime().getTime()
                        +"\nName is: " + event.getName() + " ID is: "+event.getId(), Toast.LENGTH_SHORT).show();
            }

        };
        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(mEventClickListener);


        // not necessary
        /*
        backtouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(GlobalCalendar.this, UserPage.class);
                startActivity(startNewActivityIntent);
            }
        });
*/
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(GlobalCalendar.this, CalendarTask.class);
                ActivityStack activityStack = (ActivityStack) getParent();
                activityStack.push("SecondActivity", startNewActivityIntent);
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
