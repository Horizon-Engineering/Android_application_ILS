package com.homa.hls.database;

public class Gateway {
    byte[] DNS;
    int GateWayId;
    short GateWayInfoIndex;
    byte[] IP;
    byte[] MacAddress;
    byte[] PassWord;
    int Port;
    byte[] SSID;

    public Gateway() {
        this.MacAddress = null;
        this.SSID = null;
        this.IP = null;
        this.DNS = null;
        this.Port = 0;
        this.PassWord = null;
        this.GateWayInfoIndex = (short) 0;
        this.Port = 0;
        this.GateWayId = 0;
        this.GateWayInfoIndex = (short) 0;
    }

    public short getGateWayInfoIndex() {
        return this.GateWayInfoIndex;
    }

    public void setGateWayInfoIndex(short gateWayInfoIndex) {
        this.GateWayInfoIndex = gateWayInfoIndex;
    }

    public byte[] getMacAddress() {
        return this.MacAddress;
    }

    public void setMacAddress(byte[] macAddress) {
        if (this.MacAddress == null) {
            this.MacAddress = new byte[macAddress.length];
        }
        this.MacAddress = macAddress;
    }

    public int getGateWayId() {
        return this.GateWayId;
    }

    public void setGateWayId(int gateWayId) {
        this.GateWayId = gateWayId;
    }

    public byte[] getSSID() {
        return this.SSID;
    }

    public void setSSID(byte[] sSID) {
        if (this.SSID == null && sSID != null) {
            this.SSID = new byte[sSID.length];
        }
        this.SSID = sSID;
    }

    public byte[] getIP() {
        return this.IP;
    }

    public void setIP(byte[] iP) {
        if (this.IP == null && iP != null) {
            this.IP = new byte[iP.length];
        }
        this.IP = iP;
    }

    public byte[] getDNS() {
        return this.DNS;
    }

    public void setDNS(byte[] dNS) {
        if (this.DNS == null && dNS != null) {
            this.DNS = new byte[dNS.length];
        }
        this.DNS = dNS;
    }

    public int getPort() {
        return this.Port;
    }

    public void setPort(int port) {
        this.Port = port;
    }

    public byte[] getPassWord() {
        return this.PassWord;
    }

    public void setPassWord(byte[] passWord) {
        if (this.PassWord == null && passWord != null) {
            this.PassWord = new byte[8];
        }
        this.PassWord = passWord;
    }
}
