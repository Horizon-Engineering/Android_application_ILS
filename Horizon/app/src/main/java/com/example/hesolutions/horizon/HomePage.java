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
import java.util.List;

public class HomePage extends AppCompatActivity {

    SimpleDateFormat time = new SimpleDateFormat("yyyy-MMM-dd HH:mm ");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ImageView homescreenBgImage = (ImageView) findViewById(R.id.bgImage);
        Bitmap cachedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        if (cachedBitmap != null) {
            Bitmap blurredBitmap = BlurBuilder.blur(this, cachedBitmap);
            homescreenBgImage.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        }


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

    public void Make()
    {
        List<WeekViewEvent> events;
        events = DataManager.getInstance().getevents();
        if (events.size()!=0)
        {
            for (int i=0;i<events.size();i++)
            {
                WeekViewEvent event = events.get(i);
                ArrayList<Device> devicelist = event.getdeviceList();
                ArrayList<Gateway> gatewaylist = DatabaseManager.mGatewayList;
                for (int j=0;j<devicelist.size();j++)
                {
                    Device device = devicelist.get(j);
                    byte[] data;

                    data = new byte[]{(byte) 17, (byte) 100, device.getCurrentParams()[2], (byte) 0, (byte) 0};
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                    device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                            device.getGatewaySSID(), HomePage.this));

                }
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
                if (calendar.before(finishtime)&&calendar.after(starttime))
                {
                    for (int j = 0; j <devicelist.size(); j++)
                    {
                        Device device = devicelist.get(j);
                        byte[]data;
                        data = new byte[]{(byte) 17, (byte) 100, device.getCurrentParams()[2], (byte) 0, (byte) 0};
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                        device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                device.getGatewaySSID(), HomePage.this));
                        device.setCurrentParams(data);
                        DatabaseManager.getInstance().updateDevice(device);
                    }

                }

            }
        }

        ///==================================make sure all the lights are off
    }

}
