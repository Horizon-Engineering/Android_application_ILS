package com.homa.hls.datadeal;

import android.content.Context;

public class Message {
    public static final int MACADDR_BUFFER_SIZE = 12;
    public static final int PASSWORD_BUFFER_SIZE = 8;
    public static final int SSID_BUFFER_SIZE = 64;
    private short DeviceAddress;
    private boolean IsGetGatewayinfo;
    private boolean IsRemote;
    private int MAX_SEND;
    private byte[] MacAddress;
    private byte[] Password;
    private byte[] SSID;
    private byte commandId;
    private byte datatype;
    private byte id;
    private DevicePacket packet;
    private int timercount;

    private Message() {
        this.MAX_SEND = 3;
        this.timercount = 0;
        this.datatype = (byte) 0;
        this.commandId = (byte) 0;
        this.DeviceAddress = (short) 0;
        this.IsRemote = false;
        this.IsGetGatewayinfo = false;
        this.id = (byte) 0;
        this.packet = null;
        this.timercount = 0;
        this.datatype = (byte) 0;
        this.commandId = (byte) 0;
        this.DeviceAddress = (short) 0;
        this.IsRemote = false;
        this.MacAddress = null;
        this.SSID = null;
        this.Password = null;
        this.IsGetGatewayinfo = false;
    }

    public static Message createMessage(byte dataType, DevicePacket packet, byte[] macaddr, byte[] password, byte[] ssid, Context context) {
        Message message = new Message();
        DevicePacket packettype = packet;
        if (macaddr == null || password == null || ssid == null) {
            macaddr = null;
        }
        message.MacAddress = macaddr;
        message.Password = password;
        message.SSID = ssid;
        if (dataType == 4) {
            message.DeviceAddress = packettype.getAddress();
            message.commandId = packettype.getData()[0];
        } else if (dataType == 1) {
            message.DeviceAddress = (short) 0;
            message.commandId = packettype.getCommand();
        }
        message.IsRemote = false;
        message.datatype = dataType;
        message.timercount = 0;
        message.MAX_SEND = 3;
        message.id = packettype.getCommandsequence();
        message.packet = packettype;

        return message;
    }

    public void setMacAddress(byte[] macAddress) {
        if (this.MacAddress == null) {
            this.MacAddress = new byte[macAddress.length];
        }
        this.MacAddress = macAddress;
    }

    public void setSSID(byte[] sSID) {
        if (this.SSID == null && sSID != null) {
            this.SSID = new byte[sSID.length];
        }
        this.SSID = sSID;
    }

    public void setPassWord(byte[] passWord) {
        if (this.Password == null && this.Password != null) {
            this.Password = new byte[PASSWORD_BUFFER_SIZE];
        }
        this.Password = passWord;
    }

    public int getMAX_SEND() {
        return this.MAX_SEND;
    }

    public void setMAX_SEND(int mAX_SEND) {
        this.MAX_SEND = mAX_SEND;
    }

    public byte getId() {
        return this.id;
    }

    public int getTimerCount() {
        return this.timercount;
    }

    public void setTimerCount(int timerCount) {
        this.timercount = timerCount;
    }

    public DevicePacket getPacket() {
        return this.packet;
    }

    public void setPacket(DevicePacket Packet) {
        this.packet = Packet;
    }

    public byte getDataType() {
        return this.datatype;
    }

    public byte getCommandId() {
        return this.commandId;
    }

    public byte[] getMacaddr() {
        return this.MacAddress;
    }

    public short getDeviceAddress() {
        return this.DeviceAddress;
    }

    public byte[] getSSID() {
        return this.SSID;
    }

    public byte[] getPassword() {
        return this.Password;
    }

    public boolean getIsRemote() {
        return this.IsRemote;
    }

    public void setIsRemote(boolean isr) {
        this.IsRemote = isr;
    }

    public boolean getIsGetGatewayinfo() {
        return this.IsGetGatewayinfo;
    }

    public void setIsGetGatewayinfo(boolean isget) {
        this.IsGetGatewayinfo = isget;
    }
}
