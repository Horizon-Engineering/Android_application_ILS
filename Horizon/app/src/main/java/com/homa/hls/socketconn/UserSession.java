package com.homa.hls.socketconn;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import com.allin.activity.action.SysApplication;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.datadeal.Event;
import com.homa.hls.datadeal.UserMessage;
import com.homa.hls.datadeal.UserPacket;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserSession {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static UserSession mInstance = null;
    private static ConcurrentLinkedQueue<User_CommonsNet> mLocalList = null;
    private static ConcurrentLinkedQueue<UserMessage> mParseList = null;
    public static final int mPort = 45680;
    private static ConcurrentLinkedQueue<User_CommonsNet> mRemoteList;
    private static ConcurrentLinkedQueue<UserMessage> mSendList;
    String DeviceName;
    String IP;
    int PacketCount;
    byte[] buff;
    byte[] buffer;
    User_CommonsNet mCommonsNet;
    private Context mContext;
    DatagramSocket mDatagramSocket;
    HandleRecvThread mHandleRecvThread;
    RecvThread mRecvThread;
    private Scanner mScanner;
    SendThread mSendThread;
    private ServerSocket mServerSocket;
    ServerThread mServerThread;
    UDPServerThread mUDPServerThread;
    int temp;

    private class HandleRecvThread extends Thread {
        boolean stop;

        private HandleRecvThread() {
            this.stop = true;
        }

        public void StopHandleRecvThread() {
            this.stop = UserSession.$assertionsDisabled;
        }

        public void run() {
            while (this.stop) {
                if (!UserSession.mParseList.isEmpty()) {
                    UserMessage msg = (UserMessage) UserSession.mParseList.poll();
                    if (msg != null) {
                        UserSession.this.userSessionCallback(msg);
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RecvThread extends Thread {
        User_CommonsNet mConnection;
        boolean stop;

        public void stopRecvThread() {
            this.stop = UserSession.$assertionsDisabled;
        }

        public RecvThread(User_CommonsNet connection) {
            this.mConnection = null;
            this.stop = true;
            this.mConnection = connection;
        }

        public void run() {
            byte[] buffer = new byte[AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED];
            int begin = 0;
            int end = 0;
            int recvLen = 0;
            while (this.stop) {
                try {
                    recvLen = this.mConnection.recv(buffer, end, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED - end);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (recvLen > 0) {
                    end += recvLen;
                    byte range = new Integer(end - begin).byteValue();
                    while (range > (byte) 4) {
                        byte packetLen = new Integer(UserPacket.HEADERSIZE + ((buffer[begin + 2] & MotionEventCompat.ACTION_MASK) | (buffer[begin + 3] << 8))).byteValue();
                        if (packetLen < UserPacket.HEADERSIZE) {
                            begin = 0;
                            end = 0;
                            break;
                        } else if (packetLen <= range) {
                            byte[] dataPacket = new byte[packetLen];
                            System.arraycopy(buffer, begin, dataPacket, 0, packetLen);
                            begin += packetLen;
                            range = new Integer(end - begin).byteValue();
                            UserPacket packet = UserPacket.encodePacket(dataPacket);
                            if (packet != null) {
                                UserSession.mParseList.add(UserMessage.createMessage(packet, this.mConnection));
                            }
                        } else if (end >= AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) {
                            int old = begin;
                            begin = 0;
                            byte end2 = range;
                            System.arraycopy(buffer, old, buffer, 0, range);
                        }
                    }
                    if (begin >= end) {
                        begin = 0;
                        end = 0;
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    UserSession.mLocalList.remove(this.mConnection);
                    return;
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
            this.stop = UserSession.$assertionsDisabled;
        }

        public void run() {
            while (this.stop) {
                if (!UserSession.mSendList.isEmpty()) {
                    UserMessage msg = (UserMessage) UserSession.mSendList.poll();
                    if (msg != null) {
                        User_CommonsNet commonNet = (User_CommonsNet) msg.getSource();
                        if (commonNet != null) {
                            try {
                                commonNet.send(UserPacket.decodePacket((UserPacket) msg.getPacket()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public class ServerThread extends Thread {
        boolean stop;

        public ServerThread() {
            this.stop = true;
        }

        public void StopServerThread() {
            this.stop = UserSession.$assertionsDisabled;
        }

        public void run() {
            try {
                if (UserSession.this.mServerSocket != null) {
                    UserSession.this.mServerSocket.close();
                    UserSession.this.mServerSocket = null;
                }
                UserSession.this.mServerSocket = new ServerSocket(UserSession.mPort);
                while (this.stop) {
                    Socket socket = UserSession.this.mServerSocket.accept();
                    UserSession.this.mCommonsNet = new User_CommonsNet();
                    UserSession.this.mCommonsNet.init(socket);
                    UserSession.mRemoteList.clear();
                    UserSession.mRemoteList.add(UserSession.this.mCommonsNet);
                    UserSession.this.startRevcThread(UserSession.this.mCommonsNet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public enum TASK_TYPE {
        RECV_TASK,
        SEND_TASK
    }

    public class UDPServerThread extends Thread {
        boolean stop;

        public UDPServerThread() {
            this.stop = true;
        }

        public void StopUDPServerThread() {
            this.stop = UserSession.$assertionsDisabled;
        }

        public void run() {
            try {
                if (UserSession.this.mDatagramSocket != null) {
                    UserSession.this.mDatagramSocket.close();
                    UserSession.this.mDatagramSocket = null;
                }
                UserSession.this.mDatagramSocket = new DatagramSocket(UserSession.mPort);
                while (this.stop) {
                    byte[] data = new byte[200];
                    DatagramPacket packet = new DatagramPacket(data, 200);
                    UserSession.this.mDatagramSocket.receive(packet);
                    UserSession.this.UDPuserSessionCallback(data, packet, UserPacket.encodePacket(data));
                }
                UserSession.this.mDatagramSocket.close();
            } catch (SocketException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static {
        $assertionsDisabled = !UserSession.class.desiredAssertionStatus() ? true : false;
    }

    public UserSession() {
        this.mHandleRecvThread = null;
        this.mServerThread = null;
        this.mRecvThread = null;
        this.mUDPServerThread = null;
        this.mSendThread = null;
        this.mScanner = null;
        this.mCommonsNet = null;
        this.mContext = null;
        this.DeviceName = null;
        this.IP = null;
        this.PacketCount = 0;
        this.temp = 1;
        this.buffer = null;
        this.buff = null;
        this.mDatagramSocket = null;
        mParseList = new ConcurrentLinkedQueue();
        mLocalList = new ConcurrentLinkedQueue();
        mSendList = new ConcurrentLinkedQueue();
        mRemoteList = new ConcurrentLinkedQueue();
    }

    public static UserSession getInstance() {
        if (mInstance == null) {
            mInstance = new UserSession();
        }
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        if (this.mHandleRecvThread != null) {
            this.mHandleRecvThread.StopHandleRecvThread();
            this.mHandleRecvThread = null;
        }
        if (this.mServerThread != null) {
            this.mServerThread.StopServerThread();
            this.mServerThread = null;
        }
        if (this.mUDPServerThread != null) {
            this.mUDPServerThread.StopUDPServerThread();
            this.mUDPServerThread = null;
        }
        if (this.mSendThread != null) {
            this.mSendThread.StopSendThread();
            this.mSendThread = null;
        }
        this.mSendThread = new SendThread();
        this.mSendThread.start();
        this.mHandleRecvThread = new HandleRecvThread();
        this.mHandleRecvThread.start();
        this.mServerThread = new ServerThread();
        this.mServerThread.start();
        this.mUDPServerThread = new UDPServerThread();
        this.mUDPServerThread.start();
    }

    public void unInit() {
        if (this.mHandleRecvThread != null) {
            this.mHandleRecvThread.StopHandleRecvThread();
            this.mHandleRecvThread = null;
        }
        if (this.mServerThread != null) {
            this.mServerThread.StopServerThread();
            this.mServerThread = null;
        }
        if (this.mUDPServerThread != null) {
            this.mUDPServerThread.StopUDPServerThread();
            this.mUDPServerThread = null;
        }
        if (this.mSendThread != null) {
            this.mSendThread.StopSendThread();
            this.mSendThread = null;
        }
        if (this.mRecvThread != null) {
            this.mRecvThread.stopRecvThread();
            this.mRecvThread = null;
        }
    }

    public void sendToRemote(UserPacket packet) {
        Iterator<User_CommonsNet> iter = mRemoteList.iterator();
        while (iter.hasNext()) {
            send(UserMessage.createMessage(packet, (User_CommonsNet) iter.next()));
        }
    }

    public void send(UserMessage msg) {
        mSendList.add(msg);
    }

    public void stopScanning() {
        if (this.mScanner != null) {
            this.mScanner.stop(mPort);
            this.mScanner = null;
        }
    }

    public void startRevcThread(User_CommonsNet commonsNet) {
        if (this.mRecvThread != null) {
            this.mRecvThread.stopRecvThread();
            this.mRecvThread = null;
        }
        this.mRecvThread = new RecvThread(commonsNet);
        this.mRecvThread.start();
    }

    private void UDPuserSessionCallback(byte[] buff, DatagramPacket datagramPacket, UserPacket packet) {
        byte[] data;
        StringBuffer sb;
        byte[] buffer;
        switch (packet.getCommand()) {
            case (byte) 39:
                try {
                    data = packet.getData();
                    sb = new StringBuffer(datagramPacket.getAddress().toString());
                    sb.deleteCharAt(0);
                    buffer = sb.toString().getBytes("UTF-16LE");
                    if (!(data == null || buffer == null)) {
                        this.DeviceName = new String(data, "UTF-16LE").trim();
                        this.IP = new String(buffer, "UTF-16LE").trim();
                        SysApplication.getInstance().broadcastEvent(14, 1, buffer);
                        String str = this.DeviceName;
                        SysApplication.getInstance().broadcastEvent(4, 1, new StringBuilder(String.valueOf(str)).append("(").append(this.IP).append(")").toString());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            case Event.EVENT_SCENE_INFO_STAIT /*40*/:
                try {
                    String DeviceName = Build.MODEL;
                    sb = new StringBuffer(datagramPacket.getAddress().toString());
                    sb.deleteCharAt(0);
                    Context context = this.mContext;
                    if (!SysApplication.getInstance().getLocalIpAddress(context).equals(sb.toString())) {
                        data = DeviceName.getBytes("UTF-16LE");
                        DatagramSocket ds = new DatagramSocket();
                        buffer = UserPacket.decodePacket(UserPacket.createPacket((byte) 32, (byte) 39, (byte) 0, (byte) 0, data));
                        InetAddress address = InetAddress.getByName(sb.toString());
                        ds.send(new DatagramPacket(buffer, buffer.length, address, mPort));
                    }
                } catch (UnsupportedEncodingException e22) {
                    e22.printStackTrace();
                } catch (SocketException e1) {
                    e1.printStackTrace();
                } catch (UnknownHostException e3) {
                    e3.printStackTrace();
                } catch (IOException e23) {
                    e23.printStackTrace();
                }
            default:
        }
    }

    public void userSessionCallback(UserMessage msg) {
        if ($assertionsDisabled || msg != null) {
            UserPacket userPacket = (UserPacket) msg.getPacket();
            if (userPacket != null) {
                User_CommonsNet commonNet = (User_CommonsNet) msg.getSource();
                if (commonNet != null) {
                    byte[] data;
                    switch (userPacket.getCommand()) {
                        case Event.EVENT_SETDATA_TOGATEWAY_STAIT /*32*/:
                            data = userPacket.getData();
                            if (data != null) {
                                SysApplication.getInstance().broadcastEvent(6, 1, data);
                            }
                            return;
                        case Event.EVENT_GETDATA_FORGATEWAY /*33*/:
                            data = userPacket.getData();
                            if (data != null) {
                                SysApplication.getInstance().broadcastEvent(15, 0, data);
                            }
                            return;
                        case Event.EVENT_SETDATA_TOGATEWAY /*34*/:
                            data = userPacket.getData();
                            if (data != null) {
                                if (userPacket.getReserved2() <= this.PacketCount) {
                                    System.arraycopy(data, 0, this.buff, ((userPacket.getReserved2() - 1) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) * 10, data.length);
                                    if (userPacket.getReserved2() == this.PacketCount) {
                                        _recvData(this.buff);
                                        DatabaseManager.getInstance().DatabaseInit(this.mContext);
                                        this.buff = null;
                                        SysApplication.getInstance().broadcastEvent(8, 0, null);
                                        send(UserMessage.createMessage(UserPacket.createPacket((byte) 32, (byte) 37, (byte) 0, (byte) 0, null), commonNet));
                                    } else {
                                        send(UserMessage.createMessage(UserPacket.createPacket((byte) 32, (byte) 36, (byte) this.PacketCount, (byte) (userPacket.getReserved2() + 1), null), commonNet));
                                    }
                                }
                                return;
                            }
                            return;
                        case Event.EVENT_SET_GATEWAY_PSWD /*35*/:
                            data = userPacket.getData();
                            if (data != null) {
                                this.PacketCount = data[0];
                                this.buff = new byte[(this.PacketCount * 10240)];
                                send(UserMessage.createMessage(UserPacket.createPacket((byte) 32, (byte) 36, (byte) this.PacketCount, (byte) 1, null), commonNet));
                                Log.i("0x23:", "35.................." + this.PacketCount);
                            }
                            return;
                        case Event.EVENT_GET_POST_DEVTYPE /*36*/:
                            Log.i("\u6211\u662f\u4f20\u5305\u4e2a\u6570\uff1a", new StringBuilder(String.valueOf(this.temp)).toString());
                            data = new byte[10240];
                            this.buffer = SysApplication.getInstance().EcodeFile();
                            int a = this.buffer.length / data.length;
                            if (this.buffer.length % data.length != 0) {
                                a++;
                            }
                            if (this.temp == a) {
                                int i = this.temp;
                                System.arraycopy(this.buffer, (this.temp - 1) * data.length, data, 0, this.buffer.length - ((i - 1) * data.length));
                                getInstance().sendToRemote(UserPacket.createPacket((byte) 32, (byte) 34, (byte) a, (byte) this.temp, data));
                                this.temp = 1;
                            } else {
                                System.arraycopy(this.buffer, (this.temp - 1) * data.length, data, 0, data.length);
                                getInstance().sendToRemote(UserPacket.createPacket((byte) 32, (byte) 34, (byte) a, (byte) this.temp, data));
                                this.temp++;
                            }
                            return;
                        case Event.EVENT_INPUT_SCENE /*37*/:
                            SysApplication.getInstance().broadcastEvent(8, 0, null);
                            return;
                        case (byte) 39:
                            try {
                                data = userPacket.getData();
                                byte[] buffer = commonNet.getIp().toString().getBytes("UTF-16LE");
                                this.DeviceName = null;
                                this.IP = null;
                                if (!(data == null || buffer == null)) {
                                    this.DeviceName = new String(data, "UTF-16LE").trim();
                                    this.IP = new String(buffer, "UTF-16LE").trim();
                                    SysApplication.getInstance().broadcastEvent(14, 1, buffer);
                                    String str = this.DeviceName;
                                    SysApplication.getInstance().broadcastEvent(4, 1, new StringBuilder(String.valueOf(str)).append("(").append(this.IP).append(")").toString());
                                }
                                return;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                return;
                            }
                        default:
                            return;
                    }
                }
                return;
            }
            return;
        }
        throw new AssertionError();
    }

    private void _recvData(byte[] data) {
        try {
            FileOutputStream fileos = this.mContext.openFileOutput("temp.txt", 1);
            fileos.write(data);
            fileos.close();
            this.mContext.getFileStreamPath("temp.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
