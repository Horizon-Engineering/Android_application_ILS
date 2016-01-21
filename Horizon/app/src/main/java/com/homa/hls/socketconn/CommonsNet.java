package com.homa.hls.socketconn;

import com.allin.activity.action.SysApplication;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommonsNet {
    byte[] MacAddress;
    byte mConnWay;
    DatagramPacket mDatagramPacket;
    private String mIP;
    private int mPort;
    byte[] password;

    public CommonsNet() {
        this.mPort = 0;
        this.mDatagramPacket = null;
        this.mConnWay = (byte) 0;
        this.MacAddress = null;
        this.password = null;
        this.mIP = "";
        this.mPort = 0;
    }

    public DatagramSocket getmDatagramSocket() {
        SysApplication.getInstance();
        return SysApplication.mDatagramSocket;
    }

    public void init(String ip, int port, byte[] macaddress, byte[] password) {
        this.mIP = ip;
        this.mPort = port;
        this.MacAddress = macaddress;
        this.password = password;
    }

    public void send(DatagramPacket packet) throws IOException {
        SysApplication.getInstance();
        if (SysApplication.mDatagramSocket != null) {
            SysApplication.getInstance();
            SysApplication.mDatagramSocket.send(packet);
        }
    }

    public void receive(DatagramPacket packet) throws IOException {
        if (packet != null) {
            SysApplication.getInstance();
            if (SysApplication.mDatagramSocket != null) {
                SysApplication.getInstance();
                SysApplication.mDatagramSocket.receive(packet);
            }
        }
    }

    public int getmPort() {
        return this.mPort;
    }

    public String getmIP() {
        return this.mIP;
    }

    public void setmIP(String mIP) {
        this.mIP = mIP;
    }

    public void setmPort(int mPort) {
        this.mPort = mPort;
    }

    public DatagramPacket getmDatagramPacket() {
        return this.mDatagramPacket;
    }

    public void setmDatagramPacket(DatagramPacket mDatagramPacket) {
        this.mDatagramPacket = mDatagramPacket;
    }

    public byte getmConnWay() {
        return this.mConnWay;
    }

    public void setmConnWay(byte mConnWay) {
        this.mConnWay = mConnWay;
    }

    public byte[] getMacAddress() {
        return this.MacAddress;
    }

    public void setMacAddress(byte[] macAddress) {
        this.MacAddress = macAddress;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public DatagramPacket CreateDatagramPacket(byte[] data) throws UnknownHostException {
        this.mDatagramPacket = new DatagramPacket(data, data.length, InetAddress.getByName(this.mIP), getmPort());
        return this.mDatagramPacket;
    }
}
