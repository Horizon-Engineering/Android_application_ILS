package com.homa.hls.database;

import android.support.v4.view.MotionEventCompat;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Device implements Serializable, Comparable {
    public static final short DEVICE_SIZE = (short) 99;
    public static final int MACADDR_BUFFER_SIZE = 12;
    public static final int NAME_BUFFER_SIZE = 50;
    public static final int PASSWORD_BUFFER_SIZE = 8;
    public static final int SSID_BUFFER_SIZE = 64;
    private static final long serialVersionUID = 1;
    private short ChannelInfo;
    private short ChannelMark;
    private byte[] CurrentParams;
    private short DeviceAddress;
    private short DeviceIndex;
    private String DeviceName;
    private byte[] DeviceNameBuffer;
    private short DeviceType;
    private short FatherAddress;
    private byte[] GatewayMacAddress;
    private byte[] GatewayPassword;
    private byte[] GatewaySSID;
    private byte[] LastCurrentParams;
    private int[] MacAddress;
    private short PanId;
    private String PictureName;
    private byte[] PictureNameBuffer;
    private int[] SceneDeviceMac;
    private short SceneId;
    private byte[] SceneParams;
    private short SubDeviceType;
    boolean isClick;
    boolean ischoose;
    boolean isopen;
    boolean isstart;
    boolean ischecked;

    public Device() {
        byte[] bArr = new byte[5];
        bArr[4] = (byte) 100;
        this.CurrentParams = bArr;
        bArr = new byte[5];
        bArr[4] = (byte) 100;
        this.LastCurrentParams = bArr;
        bArr = new byte[5];
        bArr[4] = (byte) 100;
        this.SceneParams = bArr;
        this.DeviceIndex = (short) 0;
        this.DeviceName = null;
        this.DeviceType = (short) 0;
        this.SubDeviceType = (short) 0;
        this.PanId = (short) 0;
        this.DeviceAddress = (short) 0;
        this.FatherAddress = (short) 0;
        this.ChannelInfo = (short) 0;
        this.ChannelMark = (short) 0;
        this.PictureName = null;
        this.DeviceNameBuffer = new byte[NAME_BUFFER_SIZE];
        this.PictureNameBuffer = new byte[NAME_BUFFER_SIZE];
        this.GatewayMacAddress = null;
        this.GatewaySSID = null;
        this.GatewayPassword = null;
        this.MacAddress = new int[4];
        this.SceneDeviceMac = new int[4];
        this.ischoose = false;
        this.isopen = false;
        this.isClick = false;
        this.isstart = false;
        this.ischecked = false;
    }

    public boolean getChecked()
    {
        return ischecked;
    }
    public void setChecked(boolean ischecked)
    {
        this.ischecked = ischecked;
    }


    public static byte[] decodeDevice(Device device) throws UnsupportedEncodingException {
        byte[] data = new byte[99];
        int i = 0 + 1;
        data[0] = (byte) 0;
        int i2 = i + 1;
        data[i] = (byte) ((device.getPanId() >> PASSWORD_BUFFER_SIZE) & MotionEventCompat.ACTION_MASK);
        i = i2 + 1;
        data[i2] = (byte) (device.getPanId() & MotionEventCompat.ACTION_MASK);
        i2 = i + 1;
        data[i] = (byte) ((device.getDeviceAddress() >> PASSWORD_BUFFER_SIZE) & MotionEventCompat.ACTION_MASK);
        i = i2 + 1;
        data[i2] = (byte) (device.getDeviceAddress() & MotionEventCompat.ACTION_MASK);
        i2 = i + 1;
        data[i] = (byte) ((device.getFatherAddress() >> PASSWORD_BUFFER_SIZE) & MotionEventCompat.ACTION_MASK);
        i = i2 + 1;
        data[i2] = (byte) (device.getFatherAddress() & MotionEventCompat.ACTION_MASK);
        i2 = i + PASSWORD_BUFFER_SIZE;
        i = i2 + 1;
        data[i2] = (byte) (device.getChannelInfo() & MotionEventCompat.ACTION_MASK);
        i2 = i + 1;
        data[i] = (byte) (device.getDeviceType() & MotionEventCompat.ACTION_MASK);
        i = i2 + 1;
        data[i2] = (byte) (device.getSubDeviceType() & MotionEventCompat.ACTION_MASK);
        return data;
    }

    public static Device createDeviceByDevice(byte[] device) {
        if (device == null) {
            return null;
        }
        int index = 0 + 1;
        byte dataType = device[0];
        int index2 = index + 1;
        short panId = (short) (device[index] << PASSWORD_BUFFER_SIZE);
        index = index2 + 1;
        panId = (short) ((device[index2] & MotionEventCompat.ACTION_MASK) | panId);
        index2 = index + 1;
        short netAddress = (short) ((device[index] & MotionEventCompat.ACTION_MASK) << PASSWORD_BUFFER_SIZE);
        index = index2 + 1;
        netAddress = (short) ((device[index2] & MotionEventCompat.ACTION_MASK) | netAddress);
        index2 = index + 1;
        short parentAddress = (short) (device[index] << PASSWORD_BUFFER_SIZE);
        index = index2 + 1;
        parentAddress = (short) ((device[index2] & MotionEventCompat.ACTION_MASK) | parentAddress);
        System.arraycopy(device, index, new byte[PASSWORD_BUFFER_SIZE], 0, PASSWORD_BUFFER_SIZE);
        index2 = index + PASSWORD_BUFFER_SIZE;
        index = index2 + 1;
        short channelId = (short) (device[index2] & MotionEventCompat.ACTION_MASK);
        index2 = index + 1;
        byte deviceType = device[index];
        index = index2 + 1;
        byte subdeviceType = device[index2];
        Device mDevice = new Device();
        mDevice.setDeviceType(deviceType);
        mDevice.setDeviceAddress(netAddress);
        mDevice.setFatherAddress(parentAddress);
        mDevice.setPanId(panId);
        mDevice.setChannelInfo(channelId);
        mDevice.setSubDeviceType(subdeviceType);
        return mDevice;
    }

    public short getDeviceIndex() {
        return this.DeviceIndex;
    }

    public void setDeviceIndex(short deviceIndex) {
        this.DeviceIndex = deviceIndex;
    }

    public short getDeviceType() {
        return this.DeviceType;
    }

    public void setDeviceType(short deviceType) {
        this.DeviceType = deviceType;
    }

    public short getSubDeviceType() {
        return this.SubDeviceType;
    }

    public void setSubDeviceType(short subDeviceType) {
        this.SubDeviceType = subDeviceType;
    }

    public short getPanId() {
        return this.PanId;
    }

    public void setPanId(short panId) {
        this.PanId = panId;
    }

    public short getDeviceAddress() {
        return this.DeviceAddress;
    }

    public void setDeviceAddress(short deviceAddress) {
        this.DeviceAddress = deviceAddress;
    }

    public short getFatherAddress() {
        return this.FatherAddress;
    }

    public void setFatherAddress(short fatherAddress) {
        this.FatherAddress = fatherAddress;
    }

    public short getChannelInfo() {
        return this.ChannelInfo;
    }

    public void setChannelInfo(short channelInfo) {
        this.ChannelInfo = channelInfo;
    }

    public String getPictureName() {
        return this.PictureName;
    }

    public void setPictureName(String pictureName) {
        this.PictureName = pictureName;
    }

    public byte[] getCurrentParams() {
        return this.CurrentParams;
    }

    public void setCurrentParams(byte[] currentParams) {
        this.CurrentParams = currentParams;
    }

    public byte[] getDeviceNameBuffer() {
        return this.DeviceNameBuffer;
    }

    public void setDeviceNameBuffer(byte[] deviceNameBuffer) {
        this.DeviceNameBuffer = deviceNameBuffer;
    }

    public byte[] getPictureNameBuffer() {
        return this.PictureNameBuffer;
    }

    public void setPictureNameBuffer(byte[] pictureNameBuffer) {
        this.PictureNameBuffer = pictureNameBuffer;
    }

    public String getDeviceName() {
        return this.DeviceName;
    }

    public short getChannelMark() {
        return this.ChannelMark;
    }

    public void setChannelMark(short channelMark) {
        this.ChannelMark = channelMark;
    }

    public short getSceneId() {
        return this.SceneId;
    }

    public void setSceneId(short sceneId) {
        this.SceneId = sceneId;
    }

    public byte[] getSceneParams() {
        return this.SceneParams;
    }

    public void setSceneParams(byte[] sceneParams) {
        this.SceneParams = sceneParams;
    }

    public boolean isIschoose() {
        return this.ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    public boolean isIsopen() {
        return this.isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }

    public boolean isClick() {
        return this.isClick;
    }

    public void setClick(boolean isClick) {
        this.isClick = isClick;
    }

    public boolean isIsstart() {
        return this.isstart;
    }

    public void setIsstart(boolean isstart) {
        this.isstart = isstart;
    }

    public void setDeviceName(String deviceName) {
        try {
            byte[] buf = deviceName.getBytes("UTF-16LE");
            int length = buf.length;
            Arrays.fill(this.DeviceNameBuffer, (byte) 0);
            System.arraycopy(buf, 0, this.DeviceNameBuffer, 0, length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.DeviceName = deviceName;
    }

    public int compareTo(Object arg0) {
        return getDeviceName().compareTo(((Device) arg0).getDeviceName());
    }

    public int[] getMacAddress() {
        return this.MacAddress;
    }

    public void setMacAddress(int[] macAddress) {
        this.MacAddress = macAddress;
    }

    public int[] getSceneDeviceMac() {
        return this.SceneDeviceMac;
    }

    public void setSceneDeviceMac(int[] sceneDeviceMac) {
        this.SceneDeviceMac = sceneDeviceMac;
    }

    public byte[] getLastCurrentParams() {
        return this.LastCurrentParams;
    }

    public void setLastCurrentParams(byte[] lastCurrentParams) {
        this.LastCurrentParams = lastCurrentParams;
    }

    public byte[] getGatewayMacAddr() {
        return this.GatewayMacAddress;
    }

    public void setGatewayMacAddr(byte[] gatewayMacAddress) {
        if (this.GatewayMacAddress == null && gatewayMacAddress != null) {
            this.GatewayMacAddress = new byte[MACADDR_BUFFER_SIZE];
        }
        this.GatewayMacAddress = gatewayMacAddress;
    }

    public byte[] getGatewaySSID() {
        return this.GatewaySSID;
    }

    public void setGatewaySSID(byte[] gatewaySSID) {
        if (this.GatewaySSID == null && gatewaySSID != null) {
            this.GatewaySSID = new byte[SSID_BUFFER_SIZE];
        }
        this.GatewaySSID = gatewaySSID;
    }

    public byte[] getGatewayPassword() {
        return this.GatewayPassword;
    }

    public void setGatewayPassword(byte[] gatewayPassword) {
        if (this.GatewayPassword == null && gatewayPassword != null) {
            this.GatewayPassword = new byte[PASSWORD_BUFFER_SIZE];
        }
        this.GatewayPassword = gatewayPassword;
    }
}
