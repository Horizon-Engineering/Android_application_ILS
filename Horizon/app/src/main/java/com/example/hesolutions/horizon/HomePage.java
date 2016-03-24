package com.example.hesolutions.horizon;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.allin.activity.action.SysApplication;
import com.google.common.collect.BiMap;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.mylibrary.WeekView;
import com.mylibrary.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity {
    final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH mm");
    TextView CODE1, CODE2, CODE3, CODE4;
    GridView gridView;
    Button radioButton1, radioButton2, radioButton3, radioButton4;
    boolean jump = false;
    boolean emergency = false;
    Switch switch1;
    ImageView emergencypic;
    AlertDialog.Builder builder = null;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        DatabaseManager.getInstance().addDevice(null, null);

        CODE1 = (TextView) findViewById(R.id.CODE1);
        CODE2 = (TextView) findViewById(R.id.CODE2);
        CODE3 = (TextView) findViewById(R.id.CODE3);
        CODE4 = (TextView) findViewById(R.id.CODE4);
        gridView = (GridView) findViewById(R.id.gridView);
        radioButton1 = (Button) findViewById(R.id.radioButton1);
        radioButton2 = (Button) findViewById(R.id.radioButton2);
        radioButton3 = (Button) findViewById(R.id.radioButton3);
        radioButton4 = (Button) findViewById(R.id.radioButton4);
        switch1 = (Switch)findViewById(R.id.switch1);
        emergencypic = (ImageView)findViewById(R.id.emergencypic);
    /*
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (height * 0.35), (int) (width * 0.4));
*/
        final GridView gridView = (GridView) findViewById(R.id.gridView);

        String[] numbers = new String[]{"1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "", "0", ""};

        final ArrayAdapter adapter = new CustomPinCodeAdapter(this, R.layout.arrayadapter, numbers);

        gridView.setAdapter(adapter);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Calendar today = Calendar.getInstance();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("*************running new thread");
                GetNewEvent();
            }
        }, today.getTime(), 1000 * 60 * 60 * 24);

        final Timer maintimer = new Timer();
        maintimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("*************running main thread");
                Gateway gateway = SysApplication.getInstance().getCurrGateway(HomePage.this);
                if (gateway!=null){
                    if (alertDialog!=null && alertDialog.isShowing())alertDialog.dismiss();
                    if (emergency == false) {
                        MakeAlert();
                    } else {
                        ArrayList<Device> deviceArrayList = DatabaseManager.getInstance().getDeviceList().getmDeviceList();
                        for (Device device : deviceArrayList) {
                            byte[] data;
                            data = new byte[]{(byte) 17, (byte) 100, (byte) 0, (byte) 0, (byte) 0};
                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                            device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                    device.getGatewaySSID(), HomePage.this));
                        }
                    }
                }else {
                    System.out.println("*************will?");
                    //maintimer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (alertDialog!=null && alertDialog.isShowing()){}
                            else{
                                builder = new AlertDialog.Builder(HomePage.this);
                                builder.setTitle("Error");
                                builder.setMessage("Gateway Error, please connect the wifi and press OK");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = getBaseContext().getPackageManager()
                                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        int mPendingIntentId = 3;
                                        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, i, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, mPendingIntent);
                                        System.exit(0);
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    });
                }
            }
        }, today.getTime(), 1000 * 20);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch1.isChecked() == true) {
                    emergency = true;
                    emergencypic.setVisibility(View.VISIBLE);
                    ArrayList<Device> deviceArrayList = DatabaseManager.getInstance().getDeviceList().getmDeviceList();
                    switch1.setText("Emergency ON  ");
                    gridView.setAdapter(null);
                    Gateway gateway = SysApplication.getInstance().getCurrGateway(HomePage.this);
                    if (gateway!=null) {
                        for (Device device : deviceArrayList) {
                            byte[] data;
                            data = new byte[]{(byte) 17, (byte) 100, (byte) 0, (byte) 0, (byte) 0};
                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                            device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                    device.getGatewaySSID(), HomePage.this));
                        }
                    }else{
                        if (alertDialog!=null && alertDialog.isShowing()){}
                        else{
                            builder = new AlertDialog.Builder(HomePage.this);
                            builder.setTitle("Error");
                            builder.setMessage("Gateway Error, please connect the wifi and press OK");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    int mPendingIntentId = 3;
                                    PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, i, PendingIntent.FLAG_CANCEL_CURRENT);
                                    AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, mPendingIntent);
                                    System.exit(0);
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                } else {
                    emergency = false;
                    gridView.setAdapter(adapter);
                    switch1.setText("Emergency OFF  ");
                    emergencypic.setVisibility(View.GONE);
                }
            }
        });
    }

/*
    private class MyRunnable implements Runnable
    {
        @Override
        public void run() {
            // check if it's run in main thread, or background thread
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                //in main thread
            } else {
                //in background thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }
    }
*/

    public void MakeAlert()
    {
        List<WeekViewEvent> events;
        Calendar cal = Calendar.getInstance();
        events = DataManager.getInstance().getnewevents();
        HashMap<String, HashMap<String, ArrayList<Device>>> sector = DataManager.getInstance().getsector();
        ArrayList<Device> arrayList = DatabaseManager.getInstance().LoadDeviceList("devicelist");
        Iterator<Device> iterator = arrayList.iterator();
        if (events != null) {
            Iterator<WeekViewEvent> eventIterator = events.iterator();
            while (eventIterator.hasNext())
            {
                WeekViewEvent event = eventIterator.next();
                ArrayList<String> sectorsname = event.getdeviceList();
                Calendar start = event.getStartTime();
                Calendar finish = event.getEndTime();
                String username = event.getName();
                int intensity = event.getIntensity();

                long mills = cal.getTime().getTime()- start.getTime().getTime();
                int days = (int) mills / (1000 * 60 * 60 * 24);

                if (cal.before(finish) && cal.after(start)) {
                    Iterator<String> stringIterator = sectorsname.iterator();
                    while (stringIterator.hasNext()){
                        String sectorname = stringIterator.next();
                        if (sector.get(username).containsKey(sectorname)) {
                            ArrayList<Device> deviceArrayList = sector.get(username).get(sectorname);
                            if (deviceArrayList != null) {
                                for (Device device : deviceArrayList) {
                                    while (iterator.hasNext())
                                    {
                                        Device device1 = iterator.next();
                                        if (device1.getDeviceName().equals(device.getDeviceName()))iterator.remove();
                                    }
                                    Device thedevice = DatabaseManager.getInstance().getDeviceInforName(device.getDeviceName());
                                    if (thedevice!=null) {
                                        if (thedevice.getChannelMark() != 5)
                                        // if controled by control page then dont use schedule
                                        {
                                            byte[] data;
                                            data = new byte[]{(byte) 17, (byte) intensity, (byte) 0, (byte) 0, (byte) 0};
                                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                                            thedevice.getDeviceAddress(), (short) 0, data), thedevice.getGatewayMacAddr(), thedevice.getGatewayPassword(),
                                                    thedevice.getGatewaySSID(), HomePage.this));
                                        }
                                    }
                                }
                            }else
                            {
                            }
                        }else {
                            stringIterator.remove();
                        }
                    }
                }
                if (sectorsname.size()== 0 || days > 30)
                {
                    eventIterator.remove();
                }
            }

            if (iterator != null) {
                while (iterator.hasNext()) {
                    Device device = iterator.next();
                    Device thedevice = DatabaseManager.getInstance().getDeviceInforName(device.getDeviceName());
                    if (thedevice!=null) {
                        if (thedevice.getChannelMark() != 5) {
                            byte[] data;
                            data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                            thedevice.getDeviceAddress(), (short) 0, data), thedevice.getGatewayMacAddr(), thedevice.getGatewayPassword(),
                                    thedevice.getGatewaySSID(), HomePage.this));
                            thedevice.setCurrentParams(data);
                            DatabaseManager.getInstance().updateDevice(thedevice);
                        }else {
                            byte[] data = thedevice.getCurrentParams();
                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                            thedevice.getDeviceAddress(), (short) 0, data), thedevice.getGatewayMacAddr(), thedevice.getGatewayPassword(),
                                    thedevice.getGatewaySSID(), HomePage.this));
                        }
                    }
                }
            }
        }

        DataManager.getInstance().setnewevents(events);

    }
    public void clickHandler(View v) {
        if (!((Button) v).getText().toString().equals(" ")) {
            if (CODE1.getText().length() == 0) {
                CODE1.setText(((Button) v).getText());
                radioButton1.setBackground(getResources().getDrawable(R.drawable.circledotsclicked));
            } else if (CODE2.getText().length() == 0) {
                CODE2.setText(((Button) v).getText());
                radioButton2.setBackground(getResources().getDrawable(R.drawable.circledotsclicked));
            } else if (CODE3.getText().length() == 0) {
                CODE3.setText(((Button) v).getText());
                radioButton3.setBackground(getResources().getDrawable(R.drawable.circledotsclicked));
            } else if (CODE4.getText().length() == 0) {
                CODE4.setText(((Button) v).getText());
                radioButton4.setBackground(getResources().getDrawable(R.drawable.circledotsclicked));
                jump = true;
            }
        }

        if (jump == true) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String code1 = CODE1.getText().toString();
                    String code2 = CODE2.getText().toString();
                    String code3 = CODE3.getText().toString();
                    String code4 = CODE4.getText().toString();
                    String code = code1 + code2 + code3 + code4;

                    BiMap<String, ArrayList> bimap;
                    bimap = DataManager.getInstance().getaccount();
                    ArrayList<String> nameset = new ArrayList<String>();
                    nameset = bimap.get(code);
                    //TODO: make sure this check will be removed in final version :)
                    Intent startNewActivityIntent;
                    if (code.equals("0000")) {
                        startNewActivityIntent = new Intent(HomePage.this, TabViewAdmin.class);
                        startNewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        clearPinCode();
                        code = "";
                        jump = false;
                        startActivity(startNewActivityIntent);
                    } else if (nameset != null) {
                        String Caccount = nameset.get(0);
                        String color = nameset.get(1);
                        code = "";
                        jump = false;
                        startNewActivityIntent = new Intent(HomePage.this, TabiewForUser.class);
                        startNewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        DataManager.getInstance().setUsername(Caccount);
                        DataManager.getInstance().setcolorname(color);
                        clearPinCode();
                        startActivity(startNewActivityIntent);
                    } else {
                        Animation shake = AnimationUtils.loadAnimation(HomePage.this, R.anim.shake_animation);
                        findViewById(R.id.radioButton1).startAnimation(shake);
                        findViewById(R.id.radioButton2).startAnimation(shake);
                        findViewById(R.id.radioButton3).startAnimation(shake);
                        findViewById(R.id.radioButton4).startAnimation(shake);
                        clearPinCode();
                        code = "";
                        jump = false;
                    }
                }
            }, 300);

        }

    }

    public void clearPinCode(){

        CODE1.setText("");
        CODE2.setText("");
        CODE3.setText("");
        CODE4.setText("");
        radioButton1.setBackground(getResources().getDrawable(R.drawable.circledots));
        radioButton2.setBackground(getResources().getDrawable(R.drawable.circledots));
        radioButton3.setBackground(getResources().getDrawable(R.drawable.circledots));
        radioButton4.setBackground(getResources().getDrawable(R.drawable.circledots));
    }

    public void GetNewEvent()
    {
        List<WeekViewEvent> events = DataManager.getInstance().getevents();
        List<WeekViewEvent> newevents = DataManager.getInstance().getnewevents();
        long today = Calendar.getInstance().getTimeInMillis();
        if (events!=null) {
            Iterator<WeekViewEvent> eventIterator = events.iterator();
            while (eventIterator.hasNext())
            {
                WeekViewEvent event = eventIterator.next();
                long eventday = event.getStartTime().getTimeInMillis();
                long mills = eventday - today;
                long days =  mills / (1000 * 60 * 60 * 24);
                if (days <= 30)
                {
                    newevents.add(event);
                    eventIterator.remove();
                }
            }
            DataManager.getInstance().setnewevents(newevents);
            DataManager.getInstance().setevents(events);
        }
    }
}
