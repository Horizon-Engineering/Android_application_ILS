package com.homa.hls.database;

import java.util.ArrayList;

public class DeviceList {
    ArrayList<Device> mDeviceList;

    public DeviceList() {
        this.mDeviceList = null;
        this.mDeviceList = new ArrayList();
    }

    public void pushDevice(Device device) {
        this.mDeviceList.add(device);
    }

    public ArrayList<Device> getmDeviceList() {
        return this.mDeviceList;
    }

    public void clearAllDevice() {
        this.mDeviceList.clear();
    }
}
