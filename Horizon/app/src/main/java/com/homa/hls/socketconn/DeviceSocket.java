package com.homa.hls.socketconn;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;

import com.example.hesolutions.horizon.R;
import com.allin.activity.action.SysApplication;
import com.google.zxing.pdf417.PDF417Common;
import com.homa.hls.database.Area;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;
import com.homa.hls.database.Scene;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.DeviceRemotePacket;
import com.homa.hls.datadeal.Event;
import com.homa.hls.datadeal.Message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DeviceSocket {
    static DeviceSocket mDeviceSession;
    private static ConcurrentLinkedQueue<Message> mSendList;
    byte[] GateWayIp;
    int TimerCount;
    int Timercount_1;
    CommonsNet commonNet;
    Context context;
    private Object lock;
    private Object lock1;
    DatagramSocket mDatagramSocketLocal;
    GetTempThread mGetTempThread;
    PacketTestThread mPacketTestThread;
    RemoteUDPServerThread mRemoteUDPServerThread;
    SendThread mSendThread;
    UDPServerThread mUDPServerThread;
    Map<String, RemoteCommonsNet> sendtestMap;
    short tempaddr;

    public class GetTempThread extends Thread {
        boolean StopGetTempThread;

        public GetTempThread() {
            this.StopGetTempThread = true;
        }

        public void StopGetTempThread() {
            this.StopGetTempThread = false;
        }

        public void run() {
            super.run();
            while (this.StopGetTempThread) {
                if (!SysApplication.getInstance().getCurrentActivity().getComponentName().getClassName().equals("com.allin.activity.MainActivity")) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (DeviceSocket.this.tempaddr == (short) 0) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    DevicePacket packet = DevicePacket.createGatewayPacket((byte) 1, (byte) 54, (short) 2, (short) 0, new byte[]{(byte) ((DeviceSocket.this.tempaddr >> 8) & MotionEventCompat.ACTION_MASK), (byte) (DeviceSocket.this.tempaddr & MotionEventCompat.ACTION_MASK)});
                    try {
                        DatagramPacket dp = new DatagramPacket(DevicePacket.decodePacket(packet), DevicePacket.decodePacket(packet).length, InetAddress.getByName(new String(DeviceSocket.this.GateWayIp, "UTF-8")), 50000);
                        SysApplication.getInstance();
                        SysApplication.mDatagramSocket.send(dp);
                    } catch (UnknownHostException e22) {
                        e22.printStackTrace();
                    } catch (UnsupportedEncodingException e23) {
                        e23.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        sleep(5000);
                    } catch (InterruptedException e24) {
                        e24.printStackTrace();
                    }
                }
            }
        }
    }

    class PacketTestThread extends Thread {
        boolean PacketTestThreadStop;
        boolean bolisremote;

        PacketTestThread() {
            this.PacketTestThreadStop = true;
            this.bolisremote = true;
        }

        public void StopPacketTestThread() {
            this.PacketTestThreadStop = false;
        }

        public void run() {
            super.run();
            while (this.PacketTestThreadStop) {
                byte[] SSID = SysApplication.getInstance().getWifiSSID(DeviceSocket.this.context);
                if (SSID != null || SysApplication.getInstance().isNetworkConnected(DeviceSocket.this.context)) {
                    RemoteCommonsNet remoteCommonsNet;
                    ArrayList<Gateway> mGatewayList = SysApplication.getInstance().SysSelectGatewayInfo();
                    for (int i = 0; i < mGatewayList.size(); i++) {
                        Gateway gateway = (Gateway) mGatewayList.get(i);
                        this.bolisremote = false;
                        if (gateway != null) {
                            try {
                                if ((gateway.getGateWayId() == 1 || gateway.getGateWayId() == 3 || gateway.getGateWayId() == 4) && new String(gateway.getSSID(), "UTF-8").indexOf("iLightsIn") == -1 && (SSID == null || !Arrays.equals(gateway.getSSID(), SSID))) {
                                    this.bolisremote = true;
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        String macaddr = null;
                        try {
                            macaddr = new String(gateway.getMacAddress(), "UTF-8");
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                        synchronized (DeviceSocket.this.lock1) {
                            if (DeviceSocket.this.sendtestMap.containsKey(macaddr)) {
                                remoteCommonsNet = (RemoteCommonsNet) DeviceSocket.this.sendtestMap.get(macaddr);
                                if (!this.bolisremote) {
                                    remoteCommonsNet.unInit();
                                    DeviceSocket.this.sendtestMap.remove(macaddr);
                                    Log.i("Mapremove :", macaddr);
                                } else if (remoteCommonsNet.getmItime() <= 0) {
                                    try {
                                        remoteCommonsNet.sendtest();
                                        Log.i("Mapresendtest :", macaddr);
                                    } catch (IOException e2) {
                                        e2.printStackTrace();
                                    }
                                    remoteCommonsNet.setmItime(24);
                                }
                            } else if (this.bolisremote) {
                                remoteCommonsNet = new RemoteCommonsNet();
                                try {
                                    remoteCommonsNet.init(gateway);
                                } catch (SocketException e3) {
                                    e3.printStackTrace();
                                } catch (UnsupportedEncodingException e4) {
                                    e4.printStackTrace();
                                }
                                DeviceSocket.this.sendtestMap.put(macaddr, remoteCommonsNet);
                                Log.i("Mapreput :", macaddr);
                                try {
                                    remoteCommonsNet.sendtest();
                                    Log.i("Mapresendtest :", macaddr);
                                } catch (IOException e22) {
                                    e22.printStackTrace();
                                }
                                remoteCommonsNet.setmItime(24);
                            }
                        }
                    }
                    synchronized (DeviceSocket.this.lock1) {
                        for (Entry<String, RemoteCommonsNet> entry : DeviceSocket.this.sendtestMap.entrySet()) {
                            remoteCommonsNet = (RemoteCommonsNet) entry.getValue();
                            if (remoteCommonsNet.getmItime() > 0) {
                                remoteCommonsNet.setmItime(remoteCommonsNet.getmItime() - 1);
                            }
                        }
                    }
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e5) {
                    e5.printStackTrace();
                }
            }
        }
    }

    public class RemoteUDPServerThread extends Thread {
        boolean StopRemoteUDPServerThread;
        byte[] dada;
        int port;

        public RemoteUDPServerThread() {
            this.StopRemoteUDPServerThread = true;
            this.dada = new byte[200];
            this.port = 0;
        }

        public void StopRemoteUDPServerThread() {
            this.StopRemoteUDPServerThread = false;
        }

        public void run() {
            super.run();
            while (this.StopRemoteUDPServerThread) {
                try {
                    DatagramPacket dpt = new DatagramPacket(this.dada, this.dada.length);
                    SysApplication.getInstance();
                    SysApplication.mDatagramSocket.receive(dpt);
                    DeviceSocket deviceSocket = DeviceSocket.this;
                    SysApplication.getInstance();
                    deviceSocket.UDPuserSessionCallback(dpt, SysApplication.mDatagramSocket);
                    System.out.println("*** deviceSocket.UDPuserSessionCallback called");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    sleep(10);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private class SendThread extends Thread {
        boolean stop;

        private SendThread() {
            this.stop = true;
        }

        public void StopSendThread() {
            this.stop = false;
        }

        public void run() {
            Map<String, Integer> sendMap = new HashMap();
            Gateway curGateway = null;
            boolean bolcheckwifi = false;
            boolean bolisNetworkConnected = false;
            byte[] curSSID = null;
            int igetssid = 20;
            while (this.stop) {
                if (!DeviceSocket.mSendList.isEmpty()) {
                    Iterator it;
                    if (igetssid >= 20) {
                        igetssid = 0;
                        curSSID = SysApplication.getInstance().getWifiSSID(DeviceSocket.this.context);
                        if (curSSID == null) {
                            bolcheckwifi = false;
                        } else {
                            bolcheckwifi = true;
                        }
                        if (SysApplication.getInstance().isNetworkConnected(DeviceSocket.this.context)) {
                            bolisNetworkConnected = true;
                        } else {
                            bolisNetworkConnected = false;
                        }
                        curGateway = SysApplication.getInstance().getCurrGatewayCheck(curSSID);
                    }
                    synchronized (DeviceSocket.this.lock) {
                        Message msg;
                        Iterator it2 = DeviceSocket.mSendList.iterator();
                        while (it2.hasNext()) {
                            msg = (Message) it2.next();
                            if (msg != null && msg.getTimerCount() <= 0) {
                                if (msg.getMAX_SEND() <= 0 && DeviceSocket.mSendList.contains(msg)) {
                                    if (msg.getDataType() == 1 && msg.getCommandId() == 17) {
                                        SysApplication.getInstance().broadcastEvent(20, 0, null);
                                    } else if (msg.getDataType() == 1 && msg.getCommandId() == 33) {
                                        SysApplication.getInstance().broadcastEvent(21, 0, null);
                                    } else if (msg.getDataType() == 1 && msg.getCommandId() == 48) {
                                        SysApplication.getInstance().broadcastEvent(24, 0, null);
                                    } else if (!(msg.getDataType() == 1 && msg.getCommandId() == 80)) {
                                        if (msg.getDataType() == 1 && msg.getCommandId() == 58) {
                                            SysApplication.getInstance().broadcastEvent(24, 0, null);
                                        } else if (msg.getCommandId() == 126) {
                                            SysApplication.getInstance().broadcastEvent(37, 0, null);
                                        } else if (msg.getCommandId() == 125) {
                                            SysApplication.getInstance().broadcastEvent(2, 0, null);
                                        } else if (msg.getDataType() == 1 && (msg.getCommandId() == 51 || msg.getCommandId() == 61 || msg.getCommandId() == 63)) {
                                            SysApplication.getInstance().broadcastEvent(38, 0, null);
                                        } else if (msg.getCommandId() == 124 || msg.getCommandId() == 123) {
                                            SysApplication.getInstance().broadcastEvent(32, 0, null);
                                        } else {
                                            SysApplication.getInstance();
                                            if (SysApplication.boolsetpswd && msg.getCommandId() == 101) {
                                                SysApplication.getInstance().broadcastEvent(35, 0, null);
                                            } else if (!(msg.getCommandId() == 112 || msg.getCommandId() == 127)) {
                                                if (msg.getIsGetGatewayinfo() || msg.getIsRemote()) {
                                                    SysApplication.getInstance().broadcastEvent(24, 0, null);
                                                } else {
                                                    if (curSSID != null) {
                                                        try {
                                                            if (Arrays.equals(msg.getSSID(), curSSID) && new String(msg.getSSID(), "UTF-8").indexOf("iLightsIn") == -1) {
                                                                Message msg1 = Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 116, (short) 0, (short) 0, null), msg.getMacaddr(), msg.getPassword(), msg.getSSID(), DeviceSocket.this.context);
                                                                msg1.setIsGetGatewayinfo(true);
                                                                DeviceSocket.getInstance().send(msg1);
                                                            }
                                                        } catch (UnsupportedEncodingException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    SysApplication.getInstance().broadcastEvent(24, 0, null);
                                                }
                                            }
                                        }
                                    }
                                    Log.i("deletemsgid = ", String.valueOf(msg.getId()));
                                    DeviceSocket.mSendList.remove(msg);
                                } else if (msg.getMAX_SEND() > 0 && msg.getMAX_SEND() <= 3) {
                                    boolean boolCanSend = false;
                                    String macaddr = null;
                                    try {
                                        macaddr = new String(msg.getMacaddr(), "UTF-8");
                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }
                                    boolean boolIshave = sendMap.containsKey(macaddr);
                                    if (sendMap.isEmpty() || !boolIshave) {
                                        boolCanSend = true;
                                        sendMap.put(macaddr, Integer.valueOf(400));
                                    } else if (((Integer) sendMap.get(macaddr)).intValue() <= 0) {
                                        boolCanSend = true;
                                        sendMap.put(macaddr, Integer.valueOf(400));
                                    }
                                    if (boolCanSend) {
                                        if (msg.getCommandId() == 124 || msg.getCommandId() == 125 || msg.getCommandId() == 63 || msg.getCommandId() == 61 || msg.getCommandId() == 48 || msg.getCommandId() == 58 || msg.getCommandId() == 62 || msg.getCommandId() == 60 || msg.getCommandId() == 126 || msg.getCommandId() == 127 || msg.getCommandId() == 51) {
                                            if (msg.getIsRemote()) {
                                                msg.setTimerCount(4000);
                                            } else {
                                                msg.setTimerCount(2000);
                                            }
                                        } else if (msg.getDataType() == 1 && (msg.getCommandId() == 17 || msg.getCommandId() == 33 || msg.getCommandId() == 18 || msg.getCommandId() == 34)) {
                                            if (msg.getIsRemote()) {
                                                msg.setTimerCount(4000);
                                            } else {
                                                msg.setTimerCount(2000);
                                            }
                                        } else if (msg.getIsRemote()) {
                                            msg.setTimerCount(3000);
                                        } else {
                                            msg.setTimerCount(1000);
                                        }
                                        msg.setMAX_SEND(msg.getMAX_SEND() - 1);
                                        Log.i("msgid = ", String.valueOf(msg.getId()));
                                        Log.i("msgsend = ", (3 - msg.getMAX_SEND()) + "\u6b21");
                                        byte[] data;
                                        if (msg.getCommandId() == 116) {
                                            SysApplication.getInstance().broadcastEvent(25, 0, null);
                                            SysApplication.getInstance();
                                            if (SysApplication.sendipadd != null) {
                                                DeviceSocket.this.commonNet = new CommonsNet();
                                                try {
                                                    CommonsNet commonsNet = DeviceSocket.this.commonNet;
                                                    SysApplication.getInstance();
                                                    commonsNet.init(new String(SysApplication.sendipadd, "UTF-8"), 50000, msg.getMacaddr(), msg.getPassword());
                                                } catch (UnsupportedEncodingException e2) {
                                                    e2.printStackTrace();
                                                }
                                                DeviceSocket.this.commonNet.setmConnWay((byte) 2);
                                                data = DevicePacket.decodePacket(msg.getPacket());
                                                try {
                                                    Log.i("sendcommid = ", String.valueOf(msg.getCommandId()));
                                                    DeviceSocket.this.commonNet.send(DeviceSocket.this.commonNet.CreateDatagramPacket(data));
                                                } catch (IOException e3) {
                                                    e3.printStackTrace();
                                                }
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            DeviceSocket.this.commonNet = SysApplication.getInstance().JudgeConnWay(msg.getMacaddr(), curGateway, bolcheckwifi, bolisNetworkConnected);
                                            data = DevicePacket.decodePacket(msg.getPacket());
                                            SysApplication.getInstance();
                                            if (SysApplication.PacketNum <= 1 || data == null || data[4] != -93 || msg.getMAX_SEND() != 2) {
                                                SysApplication.getInstance();
                                                if (SysApplication.PacketNum <= 1 && data != null && data[4] == -93 && msg.getMAX_SEND() == 2) {
                                                    SysApplication.getInstance().broadcastEvent(3, 0, null);
                                                }
                                            } else {
                                                SysApplication.getInstance();
                                                SysApplication.PacketNum--;
                                            }
                                            SysApplication.getInstance();
                                            if (SysApplication.PacketNum <= 1 || data == null || data[4] != -95 || msg.getMAX_SEND() != 2) {
                                                SysApplication.getInstance();
                                                if (SysApplication.PacketNum <= 1 && data != null && data[4] == -95 && msg.getMAX_SEND() == 2) {
                                                    SysApplication.getInstance().broadcastEvent(3, 0, null);
                                                }
                                            } else {
                                                SysApplication.getInstance();
                                                SysApplication.PacketNum--;
                                            }
                                            StringBuilder stringBuilder = new StringBuilder();
                                            SysApplication.getInstance();
                                            Log.i("send...PacketNum = ", stringBuilder.append(SysApplication.PacketNum).toString());
                                            if (data[4] == -64) {
                                                Log.i("param", String.valueOf(data[8]));
                                            } else if (data[4] == 17) {
                                                Log.i("param", String.valueOf(data[5]));
                                            }
                                            if (DeviceSocket.this.commonNet != null) {
                                                if (DeviceSocket.this.commonNet.getmConnWay() == 3) {
                                                    DeviceRemotePacket drp = DeviceRemotePacket.createRemtoPacket((byte) 5, msg.getMacaddr(), msg.getPassword(), (byte) data.length, data, msg.getId());
                                                    synchronized (DeviceSocket.this.lock1) {
                                                        if (DeviceSocket.this.sendtestMap.containsKey(macaddr) || DeviceSocket.this.sendtestMap.containsKey(macaddr)) {
                                                            try {
                                                                ((RemoteCommonsNet) DeviceSocket.this.sendtestMap.get(macaddr)).send(drp);
                                                                Log.i("remotesendcommid = ", String.valueOf(msg.getCommandId()));
                                                            } catch (IOException e32) {
                                                                e32.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    try {
                                                        DeviceSocket.this.commonNet.send(DeviceSocket.this.commonNet.CreateDatagramPacket(data));
                                                        Log.i("sendcommid = ", String.valueOf(msg.getCommandId()));
                                                    } catch (IOException e322) {
                                                        e322.printStackTrace();
                                                    }
                                                }
                                            } else if (DeviceSocket.mSendList.contains(msg) && msg.getCommandId() != 112) {
                                                SysApplication.getInstance();
                                                if (!(SysApplication.boolsetpswd && msg.getCommandId() == 101)) {
                                                    if (msg.getDataType() == 1 && msg.getCommandId() == 17) {
                                                        SysApplication.getInstance().broadcastEvent(20, 0, null);
                                                    } else if (msg.getDataType() == 1 && msg.getCommandId() == 33) {
                                                        SysApplication.getInstance().broadcastEvent(21, 0, null);
                                                    } else if (msg.getCommandId() != 80) {
                                                        if (msg.getCommandId() == 126) {
                                                            SysApplication.getInstance().broadcastEvent(37, 0, null);
                                                        } else if (msg.getCommandId() == 125) {
                                                            SysApplication.getInstance().broadcastEvent(2, 0, null);
                                                        } else if (msg.getCommandId() == 51 || msg.getCommandId() == 61 || msg.getCommandId() == 63) {
                                                            SysApplication.getInstance().broadcastEvent(38, 0, null);
                                                        } else if (msg.getCommandId() == 124 || msg.getCommandId() == 123) {
                                                            SysApplication.getInstance().broadcastEvent(32, 0, null);
                                                        } else {
                                                            SysApplication.getInstance();
                                                            if (SysApplication.boolsetpswd && msg.getCommandId() == 101) {
                                                                SysApplication.getInstance().broadcastEvent(35, 0, null);
                                                            } else {
                                                                SysApplication.getInstance().broadcastEvent(18, 0, null);
                                                            }
                                                        }
                                                    }
                                                }
                                                DeviceSocket.mSendList.remove(msg);
                                            }
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                        }
                        it = DeviceSocket.mSendList.iterator();
                        while (it.hasNext()) {
                            msg = (Message) it.next();
                            msg.setTimerCount(msg.getTimerCount() - 50);
                        }
                    }
                    for (Entry<String, Integer> entry : sendMap.entrySet()) {
                        Integer mapvalue = (Integer) entry.getValue();
                        if (mapvalue.intValue() > 0) {
                            entry.setValue(Integer.valueOf(mapvalue.intValue() - 50));
                        }
                    }
                }
                igetssid++;
                try {
                    sleep(50);
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                }
            }
            if (sendMap != null) {
                sendMap.clear();
            }
        }
    }

    public class UDPServerThread extends Thread {
        boolean StopUDPServerThread;
        byte[] data;
        int port;

        public UDPServerThread() {
            this.StopUDPServerThread = true;
            this.data = new byte[300];
            this.port = 50000;
        }

        public void StopUDPServerThread() {
            this.StopUDPServerThread = false;
        }

        public void run() {
            try {
                if (DeviceSocket.this.mDatagramSocketLocal != null) {
                    DeviceSocket.this.mDatagramSocketLocal.close();
                    DeviceSocket.this.mDatagramSocketLocal = null;
                }
                DeviceSocket.this.mDatagramSocketLocal = new DatagramSocket(this.port);
                while (this.StopUDPServerThread) {
                    Log.i("-----receive----", "----before-----");
                    DatagramPacket dpt = new DatagramPacket(this.data, this.data.length);
                    DeviceSocket.this.mDatagramSocketLocal.receive(dpt);
                    Log.i("-----receive----", "----end-----");
                    DeviceSocket.this.UDPuserSessionCallback(dpt, DeviceSocket.this.mDatagramSocketLocal);
                }
            } catch (SocketException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(10);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    static {
        mDeviceSession = null;
    }

    public DeviceSocket() {
        this.context = null;
        this.lock = new Object();
        this.lock1 = new Object();
        this.mUDPServerThread = null;
        this.mSendThread = null;
        this.commonNet = null;
        this.TimerCount = 0;
        this.Timercount_1 = 0;
        this.mDatagramSocketLocal = null;
        this.mPacketTestThread = null;
        this.mRemoteUDPServerThread = null;
        this.mGetTempThread = null;
        this.tempaddr = (short) 0;
        this.GateWayIp = null;
        this.sendtestMap = null;
        mSendList = new ConcurrentLinkedQueue();
    }

    public static DeviceSocket getInstance() {
        if (mDeviceSession == null) {
            mDeviceSession = new DeviceSocket();
        }
        return mDeviceSession;
    }

    public boolean init(Context context) {
        this.context = context;
        this.sendtestMap = new HashMap();
        if (this.mUDPServerThread != null) {
            this.mUDPServerThread.StopUDPServerThread();
            this.mUDPServerThread = null;
        }
        this.mUDPServerThread = new UDPServerThread();
        this.mUDPServerThread.start();
        if (this.mSendThread != null) {
            this.mSendThread.StopSendThread();
            this.mSendThread = null;
        }
        this.mSendThread = new SendThread();
        this.mSendThread.start();
        if (this.mPacketTestThread != null) {
            this.mPacketTestThread.StopPacketTestThread();
            this.mPacketTestThread = null;
        }
        this.mPacketTestThread = new PacketTestThread();
        this.mPacketTestThread.start();
        if (this.mRemoteUDPServerThread != null) {
            this.mRemoteUDPServerThread.StopRemoteUDPServerThread();
            this.mRemoteUDPServerThread = null;
        }
        this.mRemoteUDPServerThread = new RemoteUDPServerThread();
        this.mRemoteUDPServerThread.start();
        if (this.mGetTempThread != null) {
            this.mGetTempThread.StopGetTempThread();
            this.mGetTempThread = null;
        }
        return false;
    }

    public void unInit() {
        if (this.mSendThread != null) {
            this.mSendThread.StopSendThread();
            this.mSendThread = null;
        }
        if (this.mUDPServerThread != null) {
            this.mUDPServerThread.StopUDPServerThread();
            this.mUDPServerThread = null;
        }
        if (this.mRemoteUDPServerThread != null) {
            this.mRemoteUDPServerThread.StopRemoteUDPServerThread();
            this.mRemoteUDPServerThread = null;
        }
        if (this.mPacketTestThread != null) {
            this.mPacketTestThread.StopPacketTestThread();
            this.mPacketTestThread = null;
        }
        if (this.mGetTempThread != null) {
            this.mGetTempThread.StopGetTempThread();
            this.mGetTempThread = null;
        }
        for (Entry<String, RemoteCommonsNet> entry : this.sendtestMap.entrySet()) {
            ((RemoteCommonsNet) entry.getValue()).unInit();
        }
        this.sendtestMap.clear();
        this.sendtestMap = null;
        SysApplication.getInstance();
        if (SysApplication.mDatagramSocket != null) {
            SysApplication.getInstance();
            SysApplication.mDatagramSocket.close();
            SysApplication.getInstance();
            SysApplication.mDatagramSocket = null;
        }
        if (this.mDatagramSocketLocal != null) {
            this.mDatagramSocketLocal.close();
            this.mDatagramSocketLocal = null;
        }
    }

    public void send(Message msg1) {
        if (msg1.getCommandId() != 80) {
            Gateway curGateway = SysApplication.getInstance().getCurrGateway(this.context);
            if (curGateway != null) {
                try {
                    if (new String(curGateway.getSSID(), "UTF-8").indexOf("iLightsIn") != -1) {
                        msg1.setIsRemote(false);
                        msg1.setMacAddress(curGateway.getMacAddress());
                        msg1.setPassWord(curGateway.getPassWord());
                        msg1.setSSID(curGateway.getSSID());
                    } else if (Arrays.equals(curGateway.getSSID(), msg1.getSSID()) || new String(msg1.getSSID(), "UTF-8").indexOf("iLightsIn") != -1) {
                        msg1.setIsRemote(false);
                        msg1.setMacAddress(curGateway.getMacAddress());
                        msg1.setPassWord(curGateway.getPassWord());
                        msg1.setSSID(curGateway.getSSID());
                    } else {
                        msg1.setIsRemote(true);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                msg1.setIsRemote(true);
            }
        }
        if (mSendList.isEmpty()) {
            mSendList.add(msg1);
            Log.i("addmsgid = ", String.valueOf(msg1.getId()));
            return;
        }
        synchronized (this.lock) {
            Iterator it = mSendList.iterator();
            while (it.hasNext()) {
                Message msg = (Message) it.next();
                if (msg != null && msg1.getDataType() == msg.getDataType() && msg1.getCommandId() == msg.getCommandId() && Arrays.equals(msg1.getMacaddr(), msg.getMacaddr())) {
                    if (msg.getDataType() == (byte) 1) {
                        Log.i("deletemsgid = ", String.valueOf(msg.getId()));
                        mSendList.remove(msg);
                    } else if (msg.getDataType() == 4 && msg1.getDeviceAddress() != (short) 0 && msg1.getDeviceAddress() == msg.getDeviceAddress()) {
                        Log.i("deletemsgid = ", String.valueOf(msg.getId()));
                        mSendList.remove(msg);
                    }
                }
            }
        }
        mSendList.add(msg1);
        Log.i("addmsgid = ", String.valueOf(msg1.getId()));
    }

    private boolean SendTimeForIP(String mIp) {
        SysApplication.getInstance();
        byte[] data = SysApplication._getTimeFromCurrentTo();
        DevicePacket packet = DevicePacket.createGatewayPacket((byte) 1, (byte) 80, (short) data.length, (short) 0, data);
        try {
            DatagramPacket dp = new DatagramPacket(DevicePacket.decodePacket(packet), DevicePacket.decodePacket(packet).length, InetAddress.getByName(mIp), 50000);
            SysApplication.getInstance();
            SysApplication.mDatagramSocket.send(dp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return true;
    }

    private boolean SendTimeToGateway(Gateway gateway) {
        SysApplication.getInstance();
        byte[] data = SysApplication._getTimeFromCurrentTo();
        getInstance().send(Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 80, (short) data.length, (short) 0, data), gateway.getMacAddress(), gateway.getPassWord(), gateway.getSSID(), this.context));
        Log.i("SendTimeToGateway", "SendTimeToGateway");
        return true;
    }

    private boolean DataCheck(byte[] data, int datalength) {
        if (data == null || data[0] == (byte) 1 || data[0] == 4 || data[0] == (byte) 5) {
            return false;
        }
        if (data[0] == (byte) 6 && data[1] == -103) {
            return false;
        }
        short checkSum = (short) 0;
        int i;
        if (data[0] != 9 || datalength <= 24) {
            if (data[0] == 8 && datalength == 10) {
                i = 0;
                while (i < datalength - 1) {
                    checkSum = (short) (data[i] + checkSum);
                    i++;
                }
                if (((byte) (checkSum & MotionEventCompat.ACTION_MASK)) == data[i]) {
                    return true;
                }
                return false;
            } else if (data[0] != (byte) 6 || datalength < 5 || datalength != (data[2] & MotionEventCompat.ACTION_MASK) + 5) {
                return false;
            } else {
                i = 0;
                while (i < datalength - 1) {
                    checkSum = (short) (data[i] + checkSum);
                    i++;
                }
                if (((byte) (checkSum & MotionEventCompat.ACTION_MASK)) == data[i]) {
                    return true;
                }
                return false;
            }
        } else if (datalength != (data[21] & MotionEventCompat.ACTION_MASK) + 24) {
            return false;
        } else {
            i = 0;
            while (i < datalength - 1) {
                checkSum = (short) (data[i] + checkSum);
                i++;
            }
            if (((byte) (checkSum & MotionEventCompat.ACTION_MASK)) == data[i]) {
                return true;
            }
            return false;
        }
    }

    private boolean updataGatewayInfor(boolean boltype, Gateway newGateway) {
        ArrayList<Gateway> mGatewayList;
        int i;
        Gateway mGateway1;
        boolean boolishave = false;
        short gatewayinfex = (short) 0;
        Gateway mGateways1 = SysApplication.getInstance().getGatewayMac(newGateway.getMacAddress());
        if (mGateways1 != null) {
            gatewayinfex = mGateways1.getGateWayInfoIndex();
            newGateway.setGateWayInfoIndex(gatewayinfex);
            SysApplication.getInstance().SysUpdateGateWayInfo(newGateway);
            SendTimeToGateway(newGateway);
            boolishave = true;
        }
        if (boltype) {
            short inum = (short) 0;
            short iindex = (short) 0;
            mGatewayList = SysApplication.getInstance().SysSelectGatewayInfo();
            if (mGatewayList != null) {
                for (i = 0; i < mGatewayList.size(); i++) {
                    Gateway mGateway1s = (Gateway) mGatewayList.get(i);
                    if (mGateway1s != null) {
                        try {
                            if (new String(mGateway1s.getSSID(), "UTF-8").indexOf("iLightsIn") != -1) {
                                inum = (short) (inum + 1);
                                iindex = mGateway1s.getGateWayInfoIndex();
                                if (inum > (short) 1) {
                                    break;
                                }
                            } else {
                                continue;
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (inum == (short) 0) {
                SysApplication.getInstance().SysAddGatewayInfo(newGateway);
                SendTimeToGateway(newGateway);
                return true;
            } else if (inum == (short) 1) {
                newGateway.setGateWayInfoIndex(iindex);
                SysApplication.getInstance().SysUpdateGateWayInfo(newGateway);
                if (!boolishave) {
                    SendTimeToGateway(newGateway);
                }
                return true;
            }
        }
        mGatewayList = SysApplication.getInstance().SysSelectGatewayInfo();
        if (!boolishave) {
            SysApplication.getInstance().SysAddGatewayInfo(newGateway);
            SendTimeToGateway(newGateway);
            if (mGatewayList != null) {
                for (i = 0; i < mGatewayList.size(); i++) {
                    mGateway1 = (Gateway) mGatewayList.get(i);
                    if (mGateway1 != null && Arrays.equals(newGateway.getMacAddress(), mGateway1.getMacAddress())) {
                        gatewayinfex = mGateway1.getGateWayInfoIndex();
                        break;
                    }
                }
            }
        }
        boolean bolistwo = false;
        if (mGatewayList != null) {
            for (i = 0; i < mGatewayList.size(); i++) {
                mGateway1 = (Gateway) mGatewayList.get(i);
                if (gatewayinfex == (short) 0) {
                    break;
                }
                if (!(mGateway1 == null || mGateway1.getGateWayInfoIndex() == (short) 0)) {
                    if (boltype) {
                        try {
                            if (new String(mGateway1.getSSID(), "UTF-8").indexOf("iLightsIn") != -1) {
                                if (!Arrays.equals(newGateway.getMacAddress(), mGateway1.getMacAddress())) {
                                    DatabaseManager.getInstance().UpdateGateWayInfoToNewGatewayinfo(mGateway1.getGateWayInfoIndex(), gatewayinfex);
                                    SysApplication.getInstance().SysdeleteGatewayOfCurrGateway(mGateway1);
                                } else if (bolistwo) {
                                    DatabaseManager.getInstance().UpdateGateWayInfoToNewGatewayinfo(mGateway1.getGateWayInfoIndex(), gatewayinfex);
                                    SysApplication.getInstance().SysdeleteGatewayOfCurrGateway(mGateway1);
                                } else {
                                    bolistwo = true;
                                }
                            }
                        } catch (UnsupportedEncodingException e2) {
                            e2.printStackTrace();
                        }
                    } else if (Arrays.equals(newGateway.getSSID(), mGateway1.getSSID())) {
                        if (!Arrays.equals(newGateway.getMacAddress(), mGateway1.getMacAddress())) {
                            DatabaseManager.getInstance().UpdateGateWayInfoToNewGatewayinfo(mGateway1.getGateWayInfoIndex(), gatewayinfex);
                            SysApplication.getInstance().SysdeleteGatewayOfCurrGateway(mGateway1);
                        } else if (bolistwo) {
                            DatabaseManager.getInstance().UpdateGateWayInfoToNewGatewayinfo(mGateway1.getGateWayInfoIndex(), gatewayinfex);
                            SysApplication.getInstance().SysdeleteGatewayOfCurrGateway(mGateway1);
                        } else {
                            bolistwo = true;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean GetDevData1(byte[] devdata, Gateway mGateway) {
        int devorgroup = devdata[0];
        byte devtype = (byte) ((devdata[1] & 240) >> 4);
        byte devsubtype = (byte) (devdata[1] & 15);
        if (devtype != 0) {
            if (devorgroup == 1) {
                devtype = (byte) (devtype + 96);
            } else {
                short devaddress = (short) ((devdata[3] & MotionEventCompat.ACTION_MASK) | ((short) ((devdata[2] & MotionEventCompat.ACTION_MASK) << 8)));
                Device mDevice = new Device();
                mDevice.setDeviceType((short) devtype);
                mDevice.setDeviceAddress(devaddress);
                mDevice.setSubDeviceType((short) devsubtype);
                mDevice.setChannelInfo((short) 1);
                SysApplication.getInstance().addDevice(mDevice);
            }
        }
        return true;
    }

    private boolean GetDevData(byte[] devdata, Gateway mGateway) {
        short devaddress = (short) ((devdata[1] & MotionEventCompat.ACTION_MASK) | ((short) ((devdata[0] & MotionEventCompat.ACTION_MASK) << 8)));
        byte devtype = devdata[2];
        byte devsubtype = devdata[3];
        byte devchannel = devdata[4];
        if (devtype > 0) {
            Device mDevice = new Device();
            mDevice.setDeviceType((short) devtype);
            mDevice.setDeviceAddress(devaddress);
            mDevice.setSubDeviceType((short) devsubtype);
            mDevice.setChannelInfo(devchannel);
            byte[] devname = new byte[devdata[5]];
            System.arraycopy(devdata, 6, devname, 0, devdata[5]);
            try {
                mDevice.setDeviceName(new String(devname, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SysApplication.getInstance().addDevice(mDevice);
        }
        return true;
    }

    void UDPuserSessionCallback(DatagramPacket packet, DatagramSocket mDatagramSocket) {
        byte[] data = packet.getData();
        Log.i("+++", "+++");
        if (DataCheck(data, packet.getLength())) {
            Iterator it;
            byte command = (byte) 0;
            DevicePacket DPacket = null;
            Message msg;
            if (data[0] == 9) {
                System.out.println("date[0] == 9");
                DeviceRemotePacket RemotePacket = DeviceRemotePacket.encodePacket(data);
                synchronized (this.lock) {
                    if (!mSendList.isEmpty()) {
                        it = mSendList.iterator();
                        while (it.hasNext()) {
                            msg = (Message) it.next();
                            if (msg.getId() == RemotePacket.getCommandsequence()) {
                                if (RemotePacket.getData().length == 2 && RemotePacket.getData()[0] == 1) {
                                    if (msg.getDataType() == 1 && msg.getCommandId() == 17) {
                                        SysApplication.getInstance().broadcastEvent(20, 0, null);
                                    } else if (msg.getDataType() == 1 && msg.getCommandId() == 33) {
                                        SysApplication.getInstance().broadcastEvent(21, 0, null);
                                    } else if (msg.getDataType() == 1 && (msg.getCommandId() == 51 || msg.getCommandId() == 61 || msg.getCommandId() == 63)) {
                                        SysApplication.getInstance().broadcastEvent(38, 0, null);
                                    } else if (RemotePacket.getData()[1] == 1) {
                                        SysApplication.getInstance().broadcastEvent(9, RemotePacket.getData()[1], null);
                                        mSendList.remove(msg);
                                    } else if (RemotePacket.getData()[1] == 2) {
                                        SysApplication.getInstance().broadcastEvent(1, RemotePacket.getData()[1], null);
                                        mSendList.remove(msg);
                                    }
                                } else if (RemotePacket.getData().length > 2) {
                                    DPacket = DevicePacket.encodePacket(RemotePacket.getData());
                                    command = DPacket.getCommand();
                                    mSendList.remove(msg);
                                }
                            }
                        }
                    }
                }
            } else {
                DPacket = DevicePacket.encodePacket(data);
                System.out.println("DPacket == " + DPacket);
                command = DPacket.getCommand();
                synchronized (this.lock) {
                    if (!mSendList.isEmpty()) {
                        it = mSendList.iterator();
                        while (it.hasNext()) {
                            msg = (Message) it.next();
                            if (msg.getId() == DPacket.getCommandsequence()) {
                                mSendList.remove(msg);
                                break;
                            }
                        }
                    }
                }
            }
            Log.i("command = ", String.valueOf(command));
            byte height;
            byte low;
            short devs;
            ArrayList<Device> mDeviceList;
            int i;
            byte currpck;
            StringBuilder stringBuilder;
            Gateway curGateway;
            int idevallnum;
            short devallnum;
            byte hight;
            byte allpck;
            int sendDevnum;
            int senddatalen;
            int j;
            short devtype;
            short devaddr;
            byte devsubtype;
            byte pcknum;
            int allnum;
            byte[] mdevdata;
            byte i2;
            switch (command) {
                case PDF417Common.MODULES_IN_CODEWORD /*17*/:
                    SysApplication.getInstance();
                    if (SysApplication.boolgetalarm) {
                        SysApplication.getInstance();
                        SysApplication.boolgetalarm = false;
                        SysApplication.getInstance().broadcastEvent(20, 1, DPacket.getData());
                    }
                case Event.EVENT_SOCKET_NULL /*18*/:
                    SysApplication.getInstance();
                    if (SysApplication.boolNewgetalarm) {
                        SysApplication.getInstance();
                        SysApplication.boolNewgetalarm = false;
                        SysApplication.getInstance().broadcastEvent(20, 1, DPacket.getData());
                    }
                case Event.EVENT_GETDATA_FORGATEWAY /*33*/:
                    SysApplication.getInstance();
                    if (SysApplication.boolgettime) {
                        SysApplication.getInstance();
                        SysApplication.boolgettime = false;
                        SysApplication.getInstance().broadcastEvent(21, 1, DPacket.getData());
                    }
                case Event.EVENT_SETDATA_TOGATEWAY /*34*/:
                    SysApplication.getInstance();
                    if (SysApplication.boolNewgettime) {
                        SysApplication.getInstance();
                        SysApplication.boolNewgettime = false;
                        SysApplication.getInstance().broadcastEvent(21, 1, DPacket.getData());
                    }
                case Scene.SCENE_NAME_BUFFER_SIZE /*50*/:
                    SysApplication.getInstance();
                    if (SysApplication.boolgetmode) {
                        SysApplication.getInstance();
                        SysApplication.boolgetmode = false;
                        SysApplication.getInstance().broadcastEvent(38, 1, DPacket.getData());
                    }
                case (byte) 54:
                    SysApplication.getInstance().broadcastEvent(48, 1, DPacket.getData());
                case (byte) 60:
                    SysApplication.getInstance();
                    if (SysApplication.boolgetmode) {
                        SysApplication.getInstance();
                        SysApplication.boolgetmode = false;
                        SysApplication.getInstance().broadcastEvent(38, 1, DPacket.getData());
                    }
                case (byte) 62:
                    SysApplication.getInstance();
                    if (SysApplication.boolgetmode) {
                        SysApplication.getInstance();
                        SysApplication.boolgetmode = false;
                        SysApplication.getInstance().broadcastEvent(38, 1, DPacket.getData());
                    }
                case (byte) 81:
                    if (DPacket.getData() != null && DPacket.getDatalenght() == (short) 8) {
                        height = DPacket.getData()[6];
                        low = DPacket.getData()[7];
                        devs = (short) ((low & MotionEventCompat.ACTION_MASK) | ((short) ((height & MotionEventCompat.ACTION_MASK) << 8)));
                        if (devs != (short) 0) {
                            String devicename_sub;
                            short devsw = (short) ((low & MotionEventCompat.ACTION_MASK) | ((short) (((height + 32) & MotionEventCompat.ACTION_MASK) << 8)));
                            this.tempaddr = devsw;
                            boolean ret = true;
                            boolean retw = true;
                            mDeviceList = DatabaseManager.getInstance().getDeviceList().getmDeviceList();
                            if (mDeviceList != null) {
                                it = mDeviceList.iterator();
                                while (it.hasNext()) {
                                    if (((Device) it.next()).getDeviceAddress() == devs) {
                                        ret = false;
                                    }
                                }
                            }
                            if (mDeviceList != null) {
                                it = mDeviceList.iterator();
                                while (it.hasNext()) {
                                    if (((Device) it.next()).getDeviceAddress() == devsw) {
                                        retw = false;
                                    }
                                }
                            }
                            Device mDevice = null;
                            int stri = 1;
                            if (ret) {
                                mDevice = new Device();
                                mDevice.setDeviceType((short) 5);
                                mDevice.setSubDeviceType((short) 2);
                                mDevice.setDeviceAddress(devs);
                                mDevice.setChannelMark((short) 1);
                                String devicename = this.context.getResources().getString(R.string.sensor);
                                while (true) {
                                    devicename_sub = new StringBuilder(String.valueOf(devicename)).append("_").append(stri).toString();
                                    ret = false;
                                    if (mDeviceList != null) {
                                        it = mDeviceList.iterator();
                                        while (it.hasNext()) {
                                            if (((Device) it.next()).getDeviceName().equalsIgnoreCase(devicename_sub)) {
                                                ret = true;
                                            }
                                        }
                                    }
                                    if (ret) {
                                        stri++;
                                    } else {
                                        mDevice.setDeviceName(devicename_sub);
                                    }
                                }
                            }
                            Device mDevicew = null;
                            if (retw) {
                                mDevicew = new Device();
                                mDevicew.setDeviceType((short) 5);
                                mDevicew.setSubDeviceType((short) 3);
                                mDevicew.setDeviceAddress(devsw);
                                mDevicew.setChannelMark((short) 1);
                                String devicenamew = this.context.getResources().getString(R.string.temperature_sensor);
                                while (true) {
                                    devicename_sub = new StringBuilder(String.valueOf(devicenamew)).append("_").append(stri).toString();
                                    retw = false;
                                    if (mDeviceList != null) {
                                        it = mDeviceList.iterator();
                                        while (it.hasNext()) {
                                            if (((Device) it.next()).getDeviceName().equalsIgnoreCase(devicename_sub)) {
                                                retw = true;
                                            }
                                        }
                                    }
                                    if (retw) {
                                        stri++;
                                    } else {
                                        mDevicew.setDeviceName(devicename_sub);
                                    }
                                }
                            }
                            if (mDevice != null || mDevicew != null) {
                                Gateway gateways = SysApplication.getInstance().getCurrGateway(this.context);
                                if (gateways != null) {
                                    if (mDevice != null) {
                                        mDevice.setGatewayMacAddr(gateways.getMacAddress());
                                        mDevice.setGatewayPassword(gateways.getPassWord());
                                        mDevice.setGatewaySSID(gateways.getSSID());
                                    }
                                    if (mDevicew != null) {
                                        mDevicew.setGatewayMacAddr(gateways.getMacAddress());
                                        mDevicew.setGatewayPassword(gateways.getPassWord());
                                        mDevicew.setGatewaySSID(gateways.getSSID());
                                    }
                                }
                                if (gateways != null) {
                                    ArrayList<Scene> sceneList;
                                    byte[] bArr;
                                    ArrayList<Area> areaList = DatabaseManager.getInstance().getAreaList().getAreaArrayList();
                                    i = 0;
                                    while (i < areaList.size()) {
                                        if (((Area) areaList.get(i)).getAreaName().equals("\u6240\u6709\u8bbe\u5907") || ((Area) areaList.get(i)).getAreaName().equals("All devices") || ((Area) areaList.get(i)).getAreaName().equals("Alle Ger\u00e4te")) {
                                            if (mDevice != null) {
                                                //DatabaseManager.getInstance().addDevice(mDevice, null);
                                                DatabaseManager.getInstance().AddGateWayDevice(DatabaseManager.getInstance().SelectLimitDeviceIndex(), gateways.getGateWayInfoIndex());
                                                DatabaseManager.getInstance().addAreaDeviceTable(mDevice, (Area) areaList.get(i));
                                            }
                                            if (mDevicew != null) {
                                                //DatabaseManager.getInstance().addDevice(mDevicew, null);
                                                DatabaseManager.getInstance().AddGateWayDevice(DatabaseManager.getInstance().SelectLimitDeviceIndex(), gateways.getGateWayInfoIndex());
                                                DatabaseManager.getInstance().addAreaDeviceTable(mDevicew, (Area) areaList.get(i));
                                            }
                                            sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                                            i = 0;
                                            while (i < sceneList.size()) {
                                                if (!((Scene) sceneList.get(i)).getSceneName().equals("\u5168\u5f00") || ((Scene) sceneList.get(i)).getSceneName().equals("All On") || ((Scene) sceneList.get(i)).getSceneName().equals("Alles ein")) {
                                                    if (mDevice != null) {
                                                        bArr = new byte[5];
                                                        bArr[0] = (byte) 58;
                                                        bArr[1] = (byte) 1;
                                                        mDevice.setSceneParams(bArr);
                                                        DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), mDevice);
                                                    }
                                                    if (mDevicew != null) {
                                                        bArr = new byte[5];
                                                        bArr[0] = (byte) 52;
                                                        bArr[1] = (byte) 1;
                                                        mDevicew.setSceneParams(bArr);
                                                        DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), mDevicew);
                                                    }
                                                } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u5168\u5173") || ((Scene) sceneList.get(i)).getSceneName().equals("All Off") || ((Scene) sceneList.get(i)).getSceneName().equals("Alles aus")) {
                                                    if (mDevice != null) {
                                                        bArr = new byte[5];
                                                        bArr[0] = (byte) 58;
                                                        mDevice.setSceneParams(bArr);
                                                        DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), mDevice);
                                                    }
                                                    if (mDevicew != null) {
                                                        bArr = new byte[5];
                                                        bArr[0] = (byte) 52;
                                                        mDevicew.setSceneParams(bArr);
                                                        DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), mDevicew);
                                                    }
                                                }
                                                i++;
                                            }
                                        }
                                        i++;
                                    }
                                    sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                                    i = 0;
                                    while (i < sceneList.size()) {
                                        if (((Scene) sceneList.get(i)).getSceneName().equals("\u5168\u5f00")) {
                                            break;
                                        }
                                        if (mDevice != null) {
                                            bArr = new byte[5];
                                            bArr[0] = (byte) 58;
                                            bArr[1] = (byte) 1;
                                            mDevice.setSceneParams(bArr);
                                            DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), mDevice);
                                        }
                                        if (mDevicew != null) {
                                            bArr = new byte[5];
                                            bArr[0] = (byte) 52;
                                            bArr[1] = (byte) 1;
                                            mDevicew.setSceneParams(bArr);
                                            DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), mDevicew);
                                        }
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                case (byte) 82:
                    if (data[0] != 9) {
                        String mIp = packet.getAddress().getHostAddress();
                        if (mIp != null) {
                            SendTimeForIP(mIp);
                        }
                    }
                case (byte) 101:
                    SysApplication.getInstance();
                    if (SysApplication.boolsetpswd) {
                        SysApplication.getInstance().broadcastEvent(35, 1, DPacket.getData());
                    } else {
                        SysApplication.getInstance().broadcastEvent(22, 0, DPacket.getData());
                    }
                case (byte) 114:
                    if (DPacket.getData() != null && DPacket.getData().length>12) {
                        SysApplication.getInstance();
                        SysApplication.boolFindGateway = false;
                        Gateway mGateway = new Gateway();
                        System.out.println("*** DPacket.getData()[0] " + DPacket.getData()[0]);
                        mGateway.setGateWayId(DPacket.getData()[0]);
                        byte[] macaddress = new byte[12];
                        System.out.println("Before mac address copy"+DPacket.getData().length + "****");


                            System.arraycopy(DPacket.getData(), 1, macaddress, 0, 12);
                        System.out.println("After mac address copy" + DPacket.getData().length + "****");

                        System.out.println("*** System.arraycopy called ");
                        mGateway.setMacAddress(macaddress);
                        byte[] password = new byte[8];


                        System.out.println("Before password copy"+DPacket.getData().length + "****");


                        System.arraycopy(DPacket.getData(), 13, password, 0, 8);

                        System.out.println("After password copy" + password.toString() + "****");



                        mGateway.setPassWord(password);
                        System.out.println("*** mGateway 1 " + mGateway);
                        Gateway gateway = mGateway;
                        height = DPacket.getData()[21];
                        gateway.setPort((height << 8) | DPacket.getData()[22]);
                        byte[] DNS = new byte[DPacket.getData()[23]];
                        System.arraycopy(DPacket.getData(), 24, DNS, 0, DPacket.getData()[23]);
                        mGateway.setDNS(DNS);
                        System.out.println("*** mGateway 2 " + mGateway);
                        if (mGateway.getPort() != 0) {
                            byte[] SSID = SysApplication.getInstance().FormatStringForByte(SysApplication.getInstance().getWifiSSID(this.context));
                            if (SSID != null) {
                                try {
                                    this.GateWayIp = SysApplication.getInstance().FormatStringForByte(packet.getAddress().getHostAddress().getBytes("UTF-8"));
                                    mGateway.setIP(this.GateWayIp);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                if (SSID.length <= 64) {
                                    mGateway.setSSID(SSID);
                                }
                                if (mGateway.getGateWayId() == 2) {
                                    Gateway mGateway1 = SysApplication.getInstance().getGatewayMac(mGateway.getMacAddress());
                                    if (mGateway1 != null) {
                                        mGateway.setGateWayInfoIndex(mGateway1.getGateWayInfoIndex());
                                        SysApplication.getInstance().SysUpdateGateWayInfo(mGateway);
                                        SendTimeToGateway(mGateway);
                                        return;
                                    }
                                    SysApplication.getInstance().SysAddGatewayInfo(mGateway);
                                    SendTimeToGateway(mGateway);
                                } else if (mGateway.getGateWayId() == 1 || mGateway.getGateWayId() == 3 || mGateway.getGateWayId() == 4) {
                                    try {
                                        if (mGateway.getSSID() != null && new String(mGateway.getSSID(), "UTF-8").indexOf("iLightsIn") != -1) {
                                            updataGatewayInfor(true, mGateway);
                                        } else if (mGateway.getSSID() != null && new String(mGateway.getSSID(), "UTF-8").indexOf("iLightsIn") == -1) {
                                            updataGatewayInfor(false, mGateway);
                                        }
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                                SysApplication.getInstance();
                                if (SysApplication.boolSendmsg) {
                                    SysApplication.getInstance();
                                    SysApplication.boolSendmsg = false;
                                    SysApplication.getInstance().broadcastEvent(23, 1, null);
                                }
                            }
                        }
                        System.out.println("*** mGateway final " + mGateway);
                    }
                case (byte) 121:
                    SysApplication.getInstance();
                    if (SysApplication.boolsetpswdtogateway) {
                        currpck = DPacket.getData()[0];
                        SysApplication.getInstance();
                        if (SysApplication.ipackindex == currpck) {
                            SysApplication.getInstance();
                            SysApplication.ipackindex++;
                            stringBuilder = new StringBuilder();
                            SysApplication.getInstance();
                       //     Log.i("setDataForGateway_ipackindex = ", stringBuilder.append(SysApplication.ipackindex).toString());
                            if (currpck < 0 || currpck > 99) {
                                SysApplication.getInstance().broadcastEvent(32, 0, null);
                                return;
                            }
                            mDeviceList = DatabaseManager.getInstance().getAllDeviceList().getmDeviceList();
                            if (mDeviceList == null || mDeviceList.size() <= 0) {
                                SysApplication.getInstance().broadcastEvent(32, 0, null);
                                return;
                            }
                            curGateway = SysApplication.getInstance().getCurrGateway(this.context);
                            if (curGateway == null) {
                                SysApplication.getInstance().broadcastEvent(32, 0, null);
                                return;
                            }
                            idevallnum = mDeviceList.size();
                            if (idevallnum > 500) {
                                idevallnum = 500;
                            }
                            devallnum = (short) (65535 & idevallnum);
                            hight = (byte) (devallnum >> 8);
                            low = (byte) (devallnum & MotionEventCompat.ACTION_MASK);
                            allpck = (byte) 0;
                            if (devallnum > (short) 5) {
                                allpck = new Integer((devallnum / 5) - 1).byteValue();
                                if (devallnum % 5 != 0) {
                                    allpck++;
                                }
                            }
                            if (currpck < allpck) {
                                if (currpck == 99) {
                                    SysApplication.getInstance().broadcastEvent(32, 1, null);
                                    return;
                                }
                                currpck = (byte) (currpck + 1);
                            } else if (currpck == allpck) {
                                SysApplication.getInstance().broadcastEvent(32, 1, null);
                                return;
                            }
                           // Log.i("setDatatoGateway_currpck = ", String.valueOf(currpck));
                            if (currpck != allpck) {
                                sendDevnum = 5;
                            } else if (devallnum % 5 == 0) {
                                sendDevnum = 5;
                            } else {
                                sendDevnum = devallnum % 5;
                            }
                            senddatalen = (sendDevnum * 36) + 4;
                            byte[] sendData = new byte[senddatalen];
                            sendData[0] = hight;
                            sendData[1] = low;
                            sendData[2] = currpck;
                            sendData[3] = (byte) sendDevnum;
                            i = currpck * 5;
                            j = 0;
                            while (i < (currpck * 5) + sendDevnum) {
                                devtype = ((Device) mDeviceList.get(i)).getDeviceType();
                                devaddr = ((Device) mDeviceList.get(i)).getDeviceAddress();
                                devsubtype = (byte) ((Device) mDeviceList.get(i)).getSubDeviceType();
                                if (devtype == (short) 7) {
                                    if (devsubtype < 1) {
                                        devsubtype = (byte) 1;
                                    } else if (devsubtype > 3) {
                                        devsubtype = (byte) 3;
                                    }
                                }
                                sendData[(j * 36) + 4] = (byte) (devaddr >> 8);
                                sendData[(j * 36) + 5] = (byte) (devaddr & MotionEventCompat.ACTION_MASK);
                                sendData[(j * 36) + 6] = (byte) devtype;
                                sendData[(j * 36) + 7] = devsubtype;
                                sendData[(j * 36) + 8] = (byte) ((Device) mDeviceList.get(i)).getChannelInfo();
                                byte[] Devstrname = null;
                                try {
                                    Devstrname = SysApplication.getInstance().FormatStringForByte(((Device) mDeviceList.get(i)).getDeviceName().getBytes("UTF-8"));
                                } catch (UnsupportedEncodingException e22) {
                                    e22.printStackTrace();
                                }
                                if (Devstrname.length > 30) {
                                    sendData[(j * 36) + 9] = (byte) 30;
                                } else {
                                    sendData[(j * 36) + 9] = (byte) Devstrname.length;
                                }
                                Object devname = new byte[30];
                                System.arraycopy(Devstrname, 0, devname, 0, sendData[(j * 36) + 9]);
                                System.arraycopy(devname, 0, sendData, (j * 36) + 10, 30);
                                i++;
                                j++;
                            }
                            getInstance().send(Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 123, (short) senddatalen, (short) 0, sendData), curGateway.getMacAddress(), curGateway.getPassWord(), curGateway.getSSID(), this.context));
                            return;
                        }
                        Log.i("break:currpck", String.valueOf(currpck));
                    }
                case (byte) 123:
                    SysApplication.getInstance();
                    if (SysApplication.boolgetpswdtogateway) {
                        currpck = DPacket.getData()[2];
                        SysApplication.getInstance();
                        if (SysApplication.ipackindex == currpck) {
                            SysApplication.getInstance();
                            SysApplication.ipackindex++;
                            stringBuilder = new StringBuilder();
                            SysApplication.getInstance();
                            //Log.i("getDataForGateway_ipackindex = ", stringBuilder.append(SysApplication.ipackindex).toString());
                            if (currpck < 0 || currpck > 99) {
                                SysApplication.getInstance().broadcastEvent(2, 0, null);
                                return;
                            }
                            pcknum = DPacket.getData()[3];
                            if (pcknum < 0 || pcknum > 5) {
                                SysApplication.getInstance().broadcastEvent(2, 0, null);
                                return;
                            }
                            height = DPacket.getData()[0];
                            devs = (short) ((height & MotionEventCompat.ACTION_MASK) << 8);
                            allnum = ((short) ((DPacket.getData()[1] & MotionEventCompat.ACTION_MASK) | devs)) & 65535;
                            mdevdata = new byte[36];
                            curGateway = SysApplication.getInstance().getCurrGateway(this.context);
                            if (curGateway == null) {
                                SysApplication.getInstance().broadcastEvent(2, 0, null);
                                return;
                            }
                            for (i2 = (byte) 0; i2 < pcknum; i2++) {
                                System.arraycopy(DPacket.getData(), (i2 * 36) + 4, mdevdata, 0, 36);
                                GetDevData(mdevdata, curGateway);
                            }
                            allpck = (byte) 0;
                            if (allnum > 5) {
                                allpck = new Integer((allnum / 5) - 1).byteValue();
                                if (allnum % 5 != 0) {
                                    allpck++;
                                }
                            }
                            if (currpck < allpck) {
                                if (currpck == 99) {
                                    SysApplication.getInstance().broadcastEvent(2, 1, curGateway);
                                } else {
                                    currpck = (byte) (currpck + 1);
                                    getInstance().send(Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 125, (short) 1, (short) 0, new byte[]{currpck}), curGateway.getMacAddress(), curGateway.getPassWord(), curGateway.getSSID(), this.context));
                                }
                            } else if (currpck == allpck) {
                                SysApplication.getInstance().broadcastEvent(2, 1, curGateway);
                            }
                          //  Log.i("getDataForGateway_currpck = ", String.valueOf(currpck));
                            return;
                        }
                        Log.i("break:currpck", String.valueOf(currpck));
                    }
                case (byte) 124:
                    SysApplication.getInstance();
                    if (SysApplication.boolsetpswdtogateway) {
                        currpck = DPacket.getData()[0];
                        SysApplication.getInstance();
                        if (SysApplication.ipackindex == currpck) {
                            SysApplication.getInstance();
                            SysApplication.ipackindex++;
                            stringBuilder = new StringBuilder();
                            SysApplication.getInstance();
                         //   Log.i("setDataForGateway_ipackindex = ", stringBuilder.append(SysApplication.ipackindex).toString());
                            if (currpck < 0 || currpck > 34) {
                                SysApplication.getInstance().broadcastEvent(32, 0, null);
                                return;
                            }
                            mDeviceList = DatabaseManager.getInstance().getAllDeviceList().getmDeviceList();
                            if (mDeviceList == null || mDeviceList.size() <= 0) {
                                SysApplication.getInstance().broadcastEvent(32, 0, null);
                                return;
                            }
                            curGateway = SysApplication.getInstance().getCurrGateway(this.context);
                            if (curGateway == null) {
                                SysApplication.getInstance().broadcastEvent(32, 0, null);
                                return;
                            }
                            idevallnum = mDeviceList.size();
                            if (idevallnum > 525) {
                                idevallnum = 525;
                            }
                            devallnum = (short) (65535 & idevallnum);
                            hight = (byte) (devallnum >> 8);
                            low = (byte) (devallnum & MotionEventCompat.ACTION_MASK);
                            allpck = (byte) 0;
                            if (devallnum > (short) 15) {
                                allpck = new Integer((devallnum / 15) - 1).byteValue();
                                if (devallnum % 15 != 0) {
                                    allpck++;
                                }
                            }
                            if (currpck < allpck) {
                                if (currpck == 34) {
                                    SysApplication.getInstance().broadcastEvent(32, 1, null);
                                    return;
                                }
                                currpck = (byte) (currpck + 1);
                            } else if (currpck == allpck) {
                                SysApplication.getInstance().broadcastEvent(32, 1, null);
                                return;
                            }
                          //  Log.i("setDatatoGateway_currpck = ", String.valueOf(currpck));
                            if (currpck != allpck) {
                                sendDevnum = 15;
                            } else if (devallnum % 15 == 0) {
                                sendDevnum = 15;
                            } else {
                                sendDevnum = devallnum % 15;
                            }
                            senddatalen = (sendDevnum * 4) + 4;
                            byte[] sendData2 = new byte[senddatalen];
                            sendData2[0] = hight;
                            sendData2[1] = low;
                            sendData2[2] = currpck;
                            sendData2[3] = (byte) sendDevnum;
                            i = currpck * 15;
                            j = 0;
                            while (i < (currpck * 15) + sendDevnum) {
                                devtype = ((Device) mDeviceList.get(i)).getDeviceType();
                                devaddr = ((Device) mDeviceList.get(i)).getDeviceAddress();
                                devsubtype = (byte) ((Device) mDeviceList.get(i)).getSubDeviceType();
                                if (devtype > (short) 96) {
                                    sendData2[(j * 4) + 4] = (byte) 1;
                                    devtype = (short) (devtype - 96);
                                } else {
                                    sendData2[(j * 4) + 4] = (byte) 0;
                                }
                                if (devtype == (short) 7) {
                                    if (devsubtype < 1) {
                                        devsubtype = (byte) 1;
                                    } else if (devsubtype > 3) {
                                        devsubtype = (byte) 3;
                                    }
                                }
                                sendData2[(j * 4) + 5] = (byte) ((((byte) devtype) << 4) | devsubtype);
                                sendData2[(j * 4) + 6] = (byte) (devaddr >> 8);
                                sendData2[(j * 4) + 7] = (byte) (devaddr & MotionEventCompat.ACTION_MASK);
                                i++;
                                j++;
                            }
                            getInstance().send(Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 124, (short) senddatalen, (short) 0, sendData2), curGateway.getMacAddress(), curGateway.getPassWord(), curGateway.getSSID(), this.context));
                            return;
                        }
                        Log.i("break:currpck", String.valueOf(currpck));
                    }
                case (byte) 125:
                    SysApplication.getInstance();
                    if (SysApplication.boolgetpswdtogateway) {
                        currpck = DPacket.getData()[2];
                        SysApplication.getInstance();
                        if (SysApplication.ipackindex == currpck) {
                            SysApplication.getInstance();
                            SysApplication.ipackindex++;
                            stringBuilder = new StringBuilder();
                            SysApplication.getInstance();
                          //  Log.i("getDataForGateway_ipackindex = ", stringBuilder.append(SysApplication.ipackindex).toString());
                            if (currpck < 0 || currpck > 34) {
                                SysApplication.getInstance().broadcastEvent(2, 0, null);
                                return;
                            }
                            pcknum = DPacket.getData()[3];
                            if (pcknum < 0 || pcknum > 15) {
                                SysApplication.getInstance().broadcastEvent(2, 0, null);
                                return;
                            }
                            height = DPacket.getData()[0];
                            devs = (short) ((height & MotionEventCompat.ACTION_MASK) << 8);
                            allnum = ((short) ((DPacket.getData()[1] & MotionEventCompat.ACTION_MASK) | devs)) & 65535;
                            mdevdata = new byte[4];
                            curGateway = SysApplication.getInstance().getCurrGateway(this.context);
                            if (curGateway == null) {
                                SysApplication.getInstance().broadcastEvent(2, 0, null);
                                return;
                            }
                            for (i2 = (byte) 0; i2 < pcknum; i2++) {
                                System.arraycopy(DPacket.getData(), (i2 * 4) + 4, mdevdata, 0, 4);
                                GetDevData1(mdevdata, curGateway);
                            }
                            allpck = (byte) 0;
                            if (allnum > 15) {
                                allpck = new Integer((allnum / 15) - 1).byteValue();
                                if (allnum % 15 != 0) {
                                    allpck++;
                                }
                            }
                            if (currpck < allpck) {
                                if (currpck == 34) {
                                    SysApplication.getInstance().broadcastEvent(2, 1, curGateway);
                                } else {
                                    currpck = (byte) (currpck + 1);
                                    getInstance().send(Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 125, (short) 1, (short) 0, new byte[]{currpck}), curGateway.getMacAddress(), curGateway.getPassWord(), curGateway.getSSID(), this.context));
                                }
                            } else if (currpck == allpck) {
                                SysApplication.getInstance().broadcastEvent(2, 1, curGateway);
                            }
                       //    Log.i("getDataForGateway_currpck = ", String.valueOf(currpck));
                            return;
                        }
                        Log.i("break:currpck", String.valueOf(currpck));
                    }
                case (byte) 126:
                    SysApplication.getInstance().broadcastEvent(37, 1, DPacket.getData());
                case Byte.MAX_VALUE:
                    SysApplication.getInstance();
                    if (SysApplication.boolissend) {
                        SysApplication.getInstance();
                        SysApplication.boolissend = false;
                        SysApplication.getInstance().broadcastEvent(36, 1, DPacket.getData());
                    }
                default:
            }
        }
    }
}
