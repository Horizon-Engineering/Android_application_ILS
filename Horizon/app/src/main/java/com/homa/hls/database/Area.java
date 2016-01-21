package com.homa.hls.database;

import java.io.Serializable;
import java.util.ArrayList;

public class Area implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final short AREA_HEADER_SIZE = (short) 98;
    public static final int AREA_NAME_BUFFER_SIZE = 50;
    private static final long serialVersionUID = 1;
    short AreaIndex;
    String AreaName;
    private ArrayList<Device> DeviceOfCurrenAreaList;
    String PictureName;
    byte[] areaNameBuffer;
    short numOfDeviceCurrenArea;

    static {
        $assertionsDisabled = !Area.class.desiredAssertionStatus() ? true : false;
    }

    public Area() {
        this.DeviceOfCurrenAreaList = null;
        this.areaNameBuffer = new byte[AREA_NAME_BUFFER_SIZE];
        this.DeviceOfCurrenAreaList = new ArrayList();
    }

    public void appendDevice(Device device) {
        if ($assertionsDisabled || this.DeviceOfCurrenAreaList != null) {
            this.DeviceOfCurrenAreaList.add(device);
            this.numOfDeviceCurrenArea = (short) this.DeviceOfCurrenAreaList.size();
            return;
        }
        throw new AssertionError();
    }

    public short getAreaIndex() {
        return this.AreaIndex;
    }

    public void setAreaIndex(short areaIndex) {
        this.AreaIndex = areaIndex;
    }

    public String getAreaName() {
        return this.AreaName;
    }

    public void setAreaName(String areaName) {
        this.AreaName = areaName;
    }

    public String getPictureName() {
        return this.PictureName;
    }

    public void setPictureName(String pictureName) {
        this.PictureName = pictureName;
    }

    public byte[] getAreaNameBuffer() {
        return this.areaNameBuffer;
    }

    public void setAreaNameBuffer(byte[] areaNameBuffer) {
        this.areaNameBuffer = areaNameBuffer;
    }

    public short getNumOfDeviceCurrenArea() {
        return this.numOfDeviceCurrenArea;
    }

    public void setNumOfDeviceCurrenArea(short numOfDeviceCurrenArea) {
        this.numOfDeviceCurrenArea = numOfDeviceCurrenArea;
    }

    public ArrayList<Device> getDeviceOfCurrenAreaList() {
        return this.DeviceOfCurrenAreaList;
    }

    public void setDeviceOfCurrenAreaList(ArrayList<Device> deviceOfCurrenAreaList) {
        this.DeviceOfCurrenAreaList = deviceOfCurrenAreaList;
    }
}
