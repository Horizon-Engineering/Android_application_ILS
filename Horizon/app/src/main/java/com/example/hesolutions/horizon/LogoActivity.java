package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import com.allin.activity.action.DataStorage;
import com.allin.activity.action.SysApplication;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.socketconn.DeviceSocket;
import com.homa.hls.socketconn.UserSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimeZone;

public class LogoActivity extends Activity {
    protected void onCreate(android.os.Bundle r20) {
        super.onCreate(r20);

        setContentView(R.layout.activity_logo);
        SysApplication.getInstance().addActivity(this);
        getWindow().setFlags(1024, 1024);
        DataStorage.getInstance(this).putInt("deviceid", -1);
        DataStorage.getInstance(this).putInt("scene_gridview_item", -1);

        try {
            SysApplication.getInstance().openwifi(this);
            SysApplication.getInstance();

            if (SysApplication.mDatagramSocket != null)
            {
                SysApplication.getInstance();
                if (SysApplication.mDatagramSocket.isConnected()) {
                    System.out.println("**** mDatagramSocket is connected");
                }
            }
            else {
                SysApplication.getInstance();
                if (SysApplication.mDatagramSocket == null) {
                    SysApplication.mDatagramSocket = new DatagramSocket();
                }
                DatabaseManager.getInstance().DatabaseInit(this);
                UserSession.getInstance().init(this);
                DeviceSocket.getInstance().init(this);

                byte[] i = SysApplication._getTimeFromCurrentTo();
                byte[] arrayOfByte1 = new byte[6];
                arrayOfByte1[0] = ((byte) (i[0] >> 24));
                arrayOfByte1[1] = ((byte) (i[1] >> 16));
                arrayOfByte1[2] = ((byte) (i[2] >> 8));
                arrayOfByte1[3] = ((byte) (i[3] & 0xFF));
                int rowOffset = TimeZone.getDefault().getRawOffset() / 60000;
                arrayOfByte1[4] = ((byte) (rowOffset / 60));
                arrayOfByte1[5] = ((byte) (rowOffset % 60));
                byte[] arrayOfByte2 = SysApplication.getInstance().getWifiSSID(this);
                if ((arrayOfByte2 != null) || (SysApplication.getInstance().isNetworkConnected(this))) {
                    if (arrayOfByte2 != null) {
                        new sendFindZigbeeThread().start();
                    }
                }
                System.out.println("*** about to start SleepThread");
                new SleepThread().start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
    class sendFindZigbeeThread extends Thread {
        public void run() {
            super.run();
            LogoActivity logoActivity = LogoActivity.this;
            LogoActivity.this.getApplicationContext();
            String s_netmask = String.valueOf(((WifiManager) logoActivity.getSystemService(WIFI_SERVICE)).getDhcpInfo().netmask);
            int a = 3;
            SysApplication.getInstance();
            SysApplication.boolFindGateway = true;
            while (a > 0) {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SysApplication.getInstance();
                System.out.println("*** SysApplication.boolFindGateway is " + SysApplication.boolFindGateway);
                if (SysApplication.boolFindGateway) {
                    LogoActivity.UDPScannerIp(SysApplication.getInstance().execCalc(SysApplication.getInstance().FormatString(Integer.parseInt(s_netmask)), SysApplication.getInstance().getLocalIpAddress(SysApplication.getInstance().getCurrentActivity())), 50000, (byte) 0);
                    a--;
                } else {
                    return;
                }
            }
        }
    }
    class SleepThread extends Thread {
        public void run() {
            super.run();
            try {
                sleep(1000);
                Intent intent = new Intent(LogoActivity.this, HomePage.class);
                intent.putExtra("mainactivity", 0);
                LogoActivity.this.startActivity(intent);
                LogoActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                LogoActivity.this.finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void UDPScannerIp(String BroadcastAddress, int port, byte msgid) {
        try {
            SysApplication.getInstance();
            System.out.println("*** BroadcastAddress is " + BroadcastAddress);
            SysApplication.sendipadd = BroadcastAddress.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        try {
            DevicePacket packet = DevicePacket.createGatewayPacket((byte) 1, (byte) 116, (short) 0, (short) msgid, null);
            System.out.println("*** DevicePacket data is " + packet.getData());
            System.out.println("*** DevicePacket address is " + packet.getAddress());
            DatagramPacket dp = new DatagramPacket(DevicePacket.decodePacket(packet), DevicePacket.decodePacket(packet).length, InetAddress.getByName(BroadcastAddress), port);
            System.out.println("*** DatagramPacket is " + dp);
            System.out.println("*** DatagramPacket dp data " + dp.getData());
            System.out.println("*** DatagramPacket dp address " + dp.getAddress());
            System.out.println("*** DatagramPacket dp socket address " + dp.getSocketAddress());
            SysApplication.getInstance();
            System.out.println("*** SysApplication.getInstance CALLED");
            SysApplication.mDatagramSocket.send(dp);
            System.out.println("*** SysApplication.mDatagramSocket.send CALLED");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }
}
