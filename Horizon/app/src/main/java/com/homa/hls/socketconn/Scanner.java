package com.homa.hls.socketconn;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import com.allin.activity.action.SysApplication;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Scanner {
    private static final int CONNECTION_TIMEOUT = 500;
    private static final int MAX_THREAD = 15;
    private static int mScanCounter;
    private static String mSegment;
    private static long startTime;
    String DeviceName;
    private String mLocalIp;
    ScanningThread[] mSacnningthread;
    ServerSocket mServerSocket;

    private class ScanningThread extends Thread {
        private int mIndex;
        private int mPort;
        private final int mRange;
        private boolean mRunFlag;

        public ScanningThread(int index, int port) {
            this.mPort = 0;
            this.mIndex = 0;
            this.mRange = 17;
            this.mRunFlag = true;
            this.mIndex = index;
            this.mPort = port;
        }

        public void StopThread() {
            this.mRunFlag = false;
        }

        public void run() {
            int begin = this.mIndex * 17;
            int end = (this.mIndex + 1) * 17;
            while (this.mRunFlag) {
                String ip = Scanner.this._buildIp(begin);
                if (!(ip.equalsIgnoreCase(Scanner.this.mLocalIp) || ip == null)) {
                    try {
                        if (Scanner.this._checkIpValid(ip, this.mPort)) {
                            Log.i("\u6211\u5df2\u7ecf\u8fde\u63a5\u4e0a" + ip + "\u4e3b\u673a", "\u6211\u5df2\u7ecf\u8fde\u63a5\u4e0a" + ip + "\u4e3b\u673a");
                            try {
                                SysApplication.getInstance().broadcastEvent(5, 0, ip.getBytes("UTF-16LE"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (BindException e2) {
                        e2.printStackTrace();
                    }
                }
                begin++;
                Scanner.mScanCounter = Scanner.mScanCounter + 1;
                try {
                    sleep(10);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
                if (Scanner.mScanCounter >= MotionEventCompat.ACTION_MASK) {
                    Log.i("\u6211\u5df2\u7ecf\u641c\u7d22\u5b8c\u6210\u4e86", "\u6211\u5df2\u7ecf\u641c\u7d22\u5b8c\u6210\u4e86");
                    this.mRunFlag = false;
                }
            }
        }
    }

    public Scanner() {
        this.DeviceName = null;
        this.mServerSocket = null;
        this.mSacnningthread = new ScanningThread[MAX_THREAD];
    }

    private String _getIpSegment(String ip) {
        int index = 0;
        for (int i = ip.length() - 1; i >= 0; i--) {
            if (ip.charAt(i) == '.') {
                index = i;
                break;
            }
        }
        if (index != 0) {
            return ip.substring(0, index);
        }
        return new String();
    }

    public void start(int port) {
        mScanCounter = 0;
        for (int i = 0; i < MAX_THREAD; i++) {
            this.mSacnningthread[i] = new ScanningThread(i, port);
            this.mSacnningthread[i].start();
        }
    }

    public void stop(int port) {
        for (int i = 0; i < MAX_THREAD; i++) {
            if (this.mSacnningthread[i] != null) {
                this.mSacnningthread[i].StopThread();
            }
        }
    }

    public void init(Context context) {
        this.mLocalIp = SysApplication.getInstance().getLocalIpAddress(context);
        if (this.mLocalIp != null) {
            mSegment = _getIpSegment(this.mLocalIp);
            startTime = System.currentTimeMillis();
        }
    }

    private String _buildIp(int index) {
        return String.format("%s.%d", new Object[]{mSegment, Integer.valueOf(index)});
    }

    private boolean _checkIpValid(String ip, int port) throws BindException {
        InetSocketAddress socket = new InetSocketAddress(ip, port);
        int retry = 2;
        while (true) {
            retry--;
            if (retry < 0) {
                return false;
            }
            try {
                Socket s = new Socket();
                s.connect(socket, CONNECTION_TIMEOUT);
                s.close();
                return true;
            } catch (IllegalArgumentException e) {
            } catch (UnknownHostException e2) {
            } catch (ConnectException e3) {
            } catch (SocketTimeoutException e4) {
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
    }
}
