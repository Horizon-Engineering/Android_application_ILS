package com.example.hesolutions.horizon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.mylibrary.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomePage extends AppCompatActivity {

    ImageButton LOGIN;
    SimpleDateFormat time = new SimpleDateFormat("yyyy-MMM-dd HH:mm ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //code to make the full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ImageView homescreenBgImage = (ImageView) findViewById(R.id.bgImage);
        Bitmap cachedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        if (cachedBitmap != null) {
            Bitmap blurredBitmap = BlurBuilder.blur(this, cachedBitmap);
            //Bitmap blurredBitmap = BlurBuilder.blur(this, image_background);
            homescreenBgImage.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
            //homescreenBgImage.setAlpha(0.5f);
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
                    while (true) {
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


    private class MyRunnable implements Runnable {
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

    public void Make() {
        List<WeekViewEvent> events;
        events = DataManager.getInstance().getevents();
        if (events.size() != 0) {
            for (int i = 0; i < events.size(); i++) {
                WeekViewEvent event = events.get(i);
                ArrayList<Device> devicelist = event.getdeviceList();
                ArrayList<Gateway> gatewaylist = DatabaseManager.mGatewayList;
                for (int j = 0; j < devicelist.size(); j++) {
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


    public void MakeAlert() {
        List<WeekViewEvent> events;
        Date current = Calendar.getInstance().getTime();
        String currenttime = time.format(current);
        events = DataManager.getInstance().getevents();

        if (events.size() != 0) {
            for (int i = 0; i < events.size(); i++) {
                WeekViewEvent event = events.get(i);
                ArrayList<Device> devicelist = event.getdeviceList();
                String starttime = time.format(event.getStartTime().getTime());
                String endtime = time.format(event.getEndTime().getTime());

                if (currenttime.equals(starttime)) {
                    for (int j = 0; j < devicelist.size(); j++) {
                        Device device = devicelist.get(j);
                        byte[] data;
                        data = new byte[]{(byte) 17, (byte) 100, device.getCurrentParams()[2], (byte) 0, (byte) 0};
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                        device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                device.getGatewaySSID(), HomePage.this));

                    }
                } else if (currenttime.equals(endtime)) {
                    for (int k = 0; k < devicelist.size(); k++) {
                        Device device = devicelist.get(k);
                        byte[] data;
                        data = new byte[]{(byte) 17, (byte) 0, device.getCurrentParams()[2], (byte) 0, (byte) 0};
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                        device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                device.getGatewaySSID(), HomePage.this));

                    }
                }
            }
        }
    }

}
