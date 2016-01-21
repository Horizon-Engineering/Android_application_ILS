package com.homa.hls.socketconn;

import android.util.Log;
import com.allin.activity.action.SysApplication;
import com.homa.hls.database.Gateway;
import com.homa.hls.datadeal.DeviceRemotePacket;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RemoteCommonsNet {
    private static DatagramSocket RemoteSocket;
    String ipstring;
    int itime;
    DatagramPacket mDatagramPacket;
    DeviceRemotePacket mDeviceRemotePacket;
    Gateway mRemoteGateway;
    RemoteUDPServerThread mRemoteUDPServerThread;

    public class RemoteUDPServerThread extends Thread {
        boolean StopRemoteUDPServerThread;
        byte[] dada;

        public RemoteUDPServerThread() {
            this.StopRemoteUDPServerThread = true;
            this.dada = new byte[200];
        }

        public void StopRemoteUDPServerThread() {
            this.StopRemoteUDPServerThread = false;
        }

        public void run() {
            super.run();
            while (this.StopRemoteUDPServerThread) {
                if (RemoteCommonsNet.RemoteSocket != null) {
                    try {
                        DatagramPacket dpt = new DatagramPacket(this.dada, this.dada.length);
                        RemoteCommonsNet.RemoteSocket.receive(dpt);
                        Log.i("Maprereceive :", String.valueOf(this.dada));
                        DeviceSocket.getInstance().UDPuserSessionCallback(dpt, RemoteCommonsNet.RemoteSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(10);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    static {
        RemoteSocket = null;
    }

    public RemoteCommonsNet() {
        this.mDatagramPacket = null;
        this.mRemoteUDPServerThread = null;
        this.mRemoteGateway = null;
        this.mDeviceRemotePacket = null;
        this.ipstring = null;
        this.itime = 0;
        this.itime = 0;
    }

    public void init(Gateway mGateway) throws SocketException, UnsupportedEncodingException {
        this.mRemoteGateway = mGateway;
        this.itime = 0;
        if (this.mRemoteUDPServerThread != null) {
            this.mRemoteUDPServerThread.StopRemoteUDPServerThread();
            this.mRemoteUDPServerThread = null;
        }
        if (RemoteSocket == null || !RemoteSocket.isConnected()) {
            if (RemoteSocket != null) {
                RemoteSocket = null;
                RemoteSocket = new DatagramSocket();
            } else {
                RemoteSocket = new DatagramSocket();
            }
        }
        this.mRemoteUDPServerThread = new RemoteUDPServerThread();
        this.mRemoteUDPServerThread.start();
        this.mDeviceRemotePacket = DeviceRemotePacket.createRemtoPacket((byte) 5, this.mRemoteGateway.getMacAddress(), this.mRemoteGateway.getPassWord(), (byte) 0, null, (byte) 0);
        this.ipstring = new String(SysApplication.getInstance().FormatStringForByte(this.mRemoteGateway.getDNS()), "UTF-8");
    }

    public void unInit() {
        if (this.mRemoteUDPServerThread != null) {
            this.mRemoteUDPServerThread.StopRemoteUDPServerThread();
            this.mRemoteUDPServerThread = null;
        }
        if (RemoteSocket != null) {
            RemoteSocket.close();
            RemoteSocket = null;
        }
        this.mDeviceRemotePacket = null;
        this.ipstring = null;
    }

    public void sendtest() throws IOException {
        if (RemoteSocket != null) {
            send(this.mDeviceRemotePacket);
        }
    }

    public void send(DeviceRemotePacket packet) throws IOException {
        if (RemoteSocket != null) {
            RemoteSocket.send(new DatagramPacket(DeviceRemotePacket.decodePacket(packet), DeviceRemotePacket.decodePacket(packet).length, InetAddress.getByName(this.ipstring), this.mRemoteGateway.getPort()));
        }
    }

    public int getmItime() {
        return this.itime;
    }

    public void setmItime(int itme) {
        this.itime = itme;
    }
}
