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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.util.Iterator;
import java.util.List;

public class HomePage extends AppCompatActivity {
    final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH mm");
    TextView CODE1, CODE2, CODE3, CODE4;
    GridView gridView;
    Button radioButton1, radioButton2, radioButton3, radioButton4;
    boolean jump = false;

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
    /*
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (height * 0.35), (int) (width * 0.4));
*/
        GridView gridView = (GridView) findViewById(R.id.gridView);

        String[] numbers = new String[]{"1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "", "0", ""};

        ArrayAdapter adapter = new CustomPinCodeAdapter(this, R.layout.arrayadapter, numbers);

        gridView.setAdapter(adapter);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new MyRunnable())
        {
            @Override
            public void run()
            {
                Calendar calendar = Calendar.getInstance();
                while (calendar.get(Calendar.DAY_OF_MONTH)==1 && calendar.get(Calendar.HOUR_OF_DAY)==0
                        && calendar.get(Calendar.MINUTE)==0 && calendar.get(Calendar.SECOND)==0 )
                {
                    GetNewEvent();
                }
            }
        }.start();

        new Thread(new MyRunnable() {
            @Override
            public void run() {
                try {
                    Gateway gateways = SysApplication.getInstance().getCurrGateway(HomePage.this);
                    while(gateways!=null){
                        MakeAlert();
                        Thread.sleep(5*1000);
                    }
                    if (gateways==null)
                    {

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomePage.this);
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Gateway Error, please connect the wifi and press OK");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Restart();
                            }
                        });
                        runOnUiThread(new Runnable() {
                            public void run() {
                                alertDialog.show();
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


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


    public void MakeAlert()
    {
        List<WeekViewEvent> events;
        Calendar calendar = Calendar.getInstance();
        events = DataManager.getInstance().getnewevents();
        if (events!=null)
        {
            for (int i = 0; i< events.size(); i++)
            {
                WeekViewEvent event = events.get(i);
                ArrayList<Device> devicelist = event.getdeviceList();
                Calendar starttime = event.getStartTime();
                Calendar finishtime = event.getEndTime();
                if (calendar.after(finishtime))
                {
                    events.remove(i);
                }
                if (calendar.before(finishtime)&&calendar.after(starttime))
                {
                    for (int j = 0; j <devicelist.size(); j++)
                    {
                        Device device = devicelist.get(j);
                        byte[]data;
                        data = new byte[]{(byte) 17, device.getCurrentParams()[1], (byte) 0, (byte) 0, (byte) 0};
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                        device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                device.getGatewaySSID(), HomePage.this));
                        device.setCurrentParams(data);
                        DatabaseManager.getInstance().updateDevice(device);
                    }

                }

                String currenttime = sdf.format(calendar.getTime());
                String thisendtime = sdf.format(finishtime.getTime());
                if (currenttime.equals(thisendtime))
                {

                    for (int j =0 ; j<events.size(); j++)
                    {
                        WeekViewEvent compare = events.get(j);
                        ArrayList<Device> devicelistcompare = new ArrayList<>();
                        Calendar starttimecompare = compare.getStartTime();
                        Calendar finishtimecompare = compare.getEndTime();
                        if (calendar.before(finishtimecompare)&&calendar.after(starttimecompare)|| calendar.equals(starttimecompare))
                        {
                            devicelistcompare = compare.getdeviceList();
                        }

                        ArrayList<Device> sourcelist = new ArrayList<>(devicelist);
                        //sourcelist.removeAll(devicelistcompare);

                        Iterator<Device> firstIt = sourcelist.iterator();
                        while (firstIt.hasNext()) {
                            Device origin = firstIt.next();
                            String str1 = origin.getDeviceName();
                            // recreate iterator for second list
                            Iterator<Device> secondIt = devicelistcompare.iterator();
                            while (secondIt.hasNext()) {
                                String str2 = secondIt.next().getDeviceName();
                                if (str1.equals(str2)) {
                                    if (firstIt.hasNext()) {
                                        firstIt.remove();
                                    }
                                }
                            }
                        }

                        for (int k = 0; k<sourcelist.size() ; k++)
                        {
                            Device device = devicelist.get(k);
                            byte[]data;
                            data = new byte[]{(byte) 17, (byte) 0, (byte)0, (byte) 0, (byte) 0};
                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                            device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                    device.getGatewaySSID(), HomePage.this));
                            device.setCurrentParams(data);
                            DatabaseManager.getInstance().updateDevice(device);
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
                        startActivity(startNewActivityIntent);
                    } else if (nameset != null) {
                        String Caccount = nameset.get(0);
                        String color = nameset.get(1);
                        startNewActivityIntent = new Intent(HomePage.this, TabiewForUser.class);
                        startNewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        DataManager.getInstance().setUsername(Caccount);
                        DataManager.getInstance().setcolorname(color);
                        clearPinCode();
                        startActivity(startNewActivityIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Password does not match any account", Toast.LENGTH_LONG).show();
                        clearPinCode();
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
        if (events!=null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            for (int i = 0; i < events.size(); i++) {
                WeekViewEvent event = events.get(i);
                if (year == event.getStartTime().get(Calendar.YEAR) && month == event.getStartTime().get(Calendar.MONTH)) {
                    newevents.add(event);
                    events.remove(event);
                }
            }
            DataManager.getInstance().setnewevents(newevents);
            DataManager.getInstance().setevents(events);
        }
    }

    public void Restart(){
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        int mPendingIntentId = 3;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, mPendingIntent);
        System.exit(0);
    }
}
