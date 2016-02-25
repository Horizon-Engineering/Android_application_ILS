package com.example.hesolutions.horizon;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        DatabaseManager.getInstance().addDevice(null, null);
        /*
        //ImageView homescreenBgImage = (ImageView) findViewById(R.id.bgImage);
        Bitmap cachedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        if (cachedBitmap != null) {
            Bitmap blurredBitmap = BlurBuilder.blur(this, cachedBitmap);
            //homescreenBgImage.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        }
        */


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new MyRunnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        MakeAlert();
                        Thread.sleep(5 * 1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Intent startNewActivityIntent = new Intent(HomePage.this, UnlockScreen.class);
        startActivity(startNewActivityIntent);

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
        events = DataManager.getInstance().getevents();
        if (events.size()!=0)
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

                        Iterator<Device> firstIt = devicelist.iterator();
                        while (firstIt.hasNext()) {
                            String str1 = firstIt.next().getDeviceName();
                            // recreate iterator for second list
                            Iterator<Device> secondIt = devicelistcompare.iterator();
                            while (secondIt.hasNext()) {
                                String str2 = secondIt.next().getDeviceName();
                                if (str1.equals(str2)) {
                                    if (firstIt.hasNext()) sourcelist.remove((Device)firstIt.next());
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
        DataManager.getInstance().setevents(events);
        ///==================================make sure all the lights are off
    }

}
