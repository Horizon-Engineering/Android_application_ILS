package com.homa.hls.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.common.collect.BiMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String DEVICE_DATABASE_NAME = "iLightsIn.db";
    public static DeviceList DeviceToDeviceType = null;
    public static DeviceList LightToningList = null;
    private static final String SRC_DEVICE_DATABASE_NAME = "iLightsIn.db";
    private static DeviceList list;
    private static AreaList mAreaList;
    private static byte[] mCurrParamart;
    private static DeviceList mDeviceList;
    public static DeviceList mGatewayDeviceList;
    public static ArrayList<Gateway> mGatewayList;
    private static DatabaseManager mInstance;
    public static DeviceList mKnobOfPairlist;
    public static DeviceList mKnobOflist;
    private static SceneList mSceneList;
    Context context;
    public DataBaseHelper mDBHelper;
    public SQLiteDatabase mDataBase;
    int count;

    static {
        $assertionsDisabled = !DatabaseManager.class.desiredAssertionStatus() ? true : false;
        mInstance = null;
        mSceneList = null;
        mAreaList = null;
        mDeviceList = null;
        mCurrParamart = null;
        list = null;
        mKnobOflist = null;
        mKnobOfPairlist = null;
        LightToningList = null;
        DeviceToDeviceType = null;
        mGatewayList = null;
        mGatewayDeviceList = null;
    }

    public DatabaseManager() {
        this.mDBHelper = null;
        this.mDataBase = null;
    }

    public static DatabaseManager getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseManager();
            mDeviceList = new DeviceList();
            mSceneList = new SceneList();
            mAreaList = new AreaList();
            mCurrParamart = new byte[5];
            list = new DeviceList();
            mKnobOflist = new DeviceList();
            LightToningList = new DeviceList();
            mKnobOfPairlist = new DeviceList();
            DeviceToDeviceType = new DeviceList();
            mGatewayList = new ArrayList();
            mGatewayDeviceList = new DeviceList();
        }
        return mInstance;
    }

    public void DatabaseInit(Context context) {
        if (this.mDataBase != null) {
            this.mDataBase.close();
            this.mDataBase = null;
        }
        if (this.mDBHelper != null) {
            this.mDBHelper.close();
            this.mDBHelper = null;
        }
        System.out.println("***** about to call database helper");
        this.mDBHelper = new DataBaseHelper(context, SRC_DEVICE_DATABASE_NAME, SRC_DEVICE_DATABASE_NAME);
        System.out.println("***** database helper called");
        this.mDataBase = this.mDBHelper.getWritableDatabase();
        System.out.println("***** getWritableDatabase called");
    }


    public boolean addDevice(Device device, Area area) {
        Object[] objArr = null;
        if (count==0){
            count++;
            ArrayList<Device> arrayList = LoadDeviceList("devicelist");
            for (int i = 0; i<arrayList.size();i++) {
                if (area == null) {
                    try {
                        device = arrayList.get(i);
                        if (device != null) {
                            if (this.mDataBase != null) {
                                objArr = new Object[12];
                                objArr[1] = device.getDeviceName();
                                objArr[2] = Short.valueOf(device.getDeviceType());
                                objArr[3] = Short.valueOf(device.getPanId());
                                objArr[4] = Short.valueOf(device.getDeviceAddress());
                                objArr[5] = Short.valueOf(device.getFatherAddress());
                                objArr[6] = Short.valueOf(device.getChannelInfo());
                                objArr[7] = device.getCurrentParams();
                                objArr[8] = device.getPictureName();
                                objArr[9] = Short.valueOf(device.getSubDeviceType());
                                objArr[10] = Short.valueOf(device.getChannelMark());
                                objArr[11] = IntarrayToBytearray(device.getMacAddress());
                                this.mDataBase.execSQL("INSERT INTO DeviceInfoTable VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", objArr);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            if (device != null) {
                if (this.mDataBase != null) {
                    objArr = new Object[12];
                    objArr[1] = device.getDeviceName();
                    objArr[2] = Short.valueOf(device.getDeviceType());
                    objArr[3] = Short.valueOf(device.getPanId());
                    objArr[4] = Short.valueOf(device.getDeviceAddress());
                    objArr[5] = Short.valueOf(device.getFatherAddress());
                    objArr[6] = Short.valueOf(device.getChannelInfo());
                    objArr[7] = device.getCurrentParams();
                    objArr[8] = device.getPictureName();
                    objArr[9] = Short.valueOf(device.getSubDeviceType());
                    objArr[10] = Short.valueOf(device.getChannelMark());
                    objArr[11] = IntarrayToBytearray(device.getMacAddress());
                    this.mDataBase.execSQL("INSERT INTO DeviceInfoTable VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", objArr);
                }
            }
        }

        return $assertionsDisabled;
    }

    public ArrayList<String> SelectDeviceName() {
        ArrayList<String> list = new ArrayList();
        Cursor cursor = this.mDataBase.rawQuery("SELECT DeviceName FROM DeviceInfoTable", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    public ArrayList<Short> SelectDeviceAddress() {
        ArrayList<Short> list = new ArrayList();
        Cursor cursor = this.mDataBase.rawQuery("SELECT DeviceAddress FROM DeviceInfoTable", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(Short.valueOf(cursor.getShort(0)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    public boolean deleteDevice(Device mdevice) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("DELETE FROM DeviceInfoTable WHERE DeviceName = ?", new Object[]{String.valueOf(mdevice.getDeviceName())});
                /*
                deleteAreaDevice(mdevice.getDeviceIndex());
                deleteSceneDeviceToDevice(mdevice.getDeviceIndex());
                deleteDeviceOfCurrKnob(mdevice);
                deleteDeviceOfCurrGateway(mdevice.getDeviceIndex());
                DeleteGroupDeviceData(mdevice.getDeviceIndex());
                */
                return true;
            }
        } catch (Exception e) {
        }
        return $assertionsDisabled;
    }

    public DeviceList SelectDevicetoDeviceType(Area marea, int devicetype) {
        if (this.mDataBase != null) {
            if (DeviceToDeviceType != null && DeviceToDeviceType.mDeviceList.size() > 0) {
                DeviceToDeviceType.clearAllDevice();
            }
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType IN(" + devicetype + "," + devicetype + 7 + ") AND DeviceIndex IN (SELECT DeviceIndex FROM AreaDeviceTable WHERE AreaIndex = " + marea.getAreaIndex() + ")", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    DeviceToDeviceType.pushDevice(getDeviceinfo(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return DeviceToDeviceType;
    }

    public boolean deleteAreaDevice(short DeviceIndex) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("DELETE FROM AreaDeviceTable WHERE DeviceIndex = ?", new Object[]{Short.valueOf(DeviceIndex)});
            }
        } catch (Exception e) {
        }
        return $assertionsDisabled;
    }

    public boolean updateDevice(Device device) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("Update DeviceInfoTable SET CurrentParams = ?,ChannelMark = ?,DeviceType = ?,MacAddress = ?,SubDeviceType = ? WHERE DeviceName = ?", new Object[]{device.getCurrentParams(), Short.valueOf(device.getChannelMark()), Short.valueOf(device.getDeviceType()), IntarrayToBytearray(device.getMacAddress()), Short.valueOf(device.getSubDeviceType()),String.valueOf(device.getDeviceName())});
                return true;
            }
        } catch (Exception e) {
        }
        return $assertionsDisabled;
    }

    public boolean addScene(Scene scene) {
        try {
            if (this.mDataBase != null) {
                Object[] objArr = new Object[3];
                objArr[1] = scene.getSceneName();
                objArr[2] = scene.getPictureName();
                this.mDataBase.execSQL("INSERT INTO SceneInfoTable VALUES(?,?,?)", objArr);
                Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM SceneInfoTable ORDER BY SceneInfoIndex DESC LIMIT 1;", null);
                cursor.moveToFirst();
                scene.setSceneInfoIndex(cursor.getShort(0));
                cursor.close();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public boolean deleteScene(Scene scene) {
        if ($assertionsDisabled || scene != null) {
            try {
                if (this.mDataBase != null) {
                    this.mDataBase.execSQL("DELETE FROM SceneInfoTable WHERE SceneInfoIndex = ?", new Object[]{Short.valueOf(scene.getSceneInfoIndex())});
                    this.mDataBase.execSQL("DELETE FROM SceneDeviceTable WHERE SceneInfoIndex = ?", new Object[]{Short.valueOf(scene.getSceneInfoIndex())});
                    return true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return $assertionsDisabled;
        }
        throw new AssertionError();
    }

    public boolean deleteAllScene() {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("DELETE FROM SceneInfoTable");
                this.mDataBase.execSQL("DELETE FROM SceneDeviceTable");
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public boolean updateScene(Scene scene) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("Update SceneInfoTable SET SceneName = ?,PictureName = ? WHERE SceneInfoIndex = ?", new Object[]{scene.getSceneName(), scene.getPictureName(), Short.valueOf(scene.getSceneInfoIndex())});
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public boolean addArea(Area area) {
        try {
            if (this.mDataBase != null) {
                Object[] objArr = new Object[3];
                objArr[1] = area.getAreaName();
                objArr[2] = area.getPictureName();
                this.mDataBase.execSQL("INSERT INTO AreaInfoTable VALUES(?,?,?)", objArr);
                Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM AreaInfoTable ORDER BY AreaIndex DESC LIMIT 1;", null);
                cursor.moveToFirst();
                area.setAreaIndex(cursor.getShort(0));
                cursor.close();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public boolean addAreaToOne(Area area) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("INSERT INTO AreaInfoTable VALUES(?,?,?)", new Object[]{Integer.valueOf(1), area.getAreaName(), area.getPictureName()});
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public boolean addSceneToOne(Scene scene) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("INSERT INTO SceneInfoTable VALUES(?,?,?)", new Object[]{Integer.valueOf(1), scene.getSceneName(), scene.getPictureName()});
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public boolean addSceneToTwo(Scene scene) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("INSERT INTO SceneInfoTable VALUES(?,?,?)", new Object[]{Integer.valueOf(2), scene.getSceneName(), scene.getPictureName()});
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public boolean deleteArea(String areaName) {
        if ($assertionsDisabled || !(areaName == null || this.mDataBase == null)) {
            try {
                this.mDataBase.execSQL("DELETE FROM AreaInfoTable WHERE AreaName = ?", new Object[]{areaName});
                this.mDataBase.execSQL("DELETE FROM AreaDeviceTable WHERE AreaIndex = ?", new Object[]{Integer.valueOf(1)});
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return $assertionsDisabled;
            }
        }
        throw new AssertionError();
    }

    public boolean deleteScene(String sceneName) {
        if ($assertionsDisabled || !(sceneName == null || this.mDataBase == null)) {
            try {
                this.mDataBase.execSQL("DELETE FROM SceneInfoTable WHERE SceneName = ?", new Object[]{sceneName});
                this.mDataBase.execSQL("DELETE FROM SceneDeviceTable WHERE SceneInfoIndex = ?", new Object[]{Integer.valueOf(1)});
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return $assertionsDisabled;
            }
        }
        throw new AssertionError();
    }

    public boolean deleteArea(Area area) {
        if ($assertionsDisabled || !(area == null || this.mDataBase == null)) {
            try {
                this.mDataBase.execSQL("DELETE FROM AreaInfoTable WHERE AreaIndex = ?", new Object[]{Short.valueOf(area.getAreaIndex())});
                this.mDataBase.execSQL("DELETE FROM AreaDeviceTable WHERE AreaIndex = ?", new Object[]{Short.valueOf(area.getAreaIndex())});
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return $assertionsDisabled;
            }
        }
        throw new AssertionError();
    }

    public boolean deleteAllArea() {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("DELETE FROM AreaInfoTable");
                deleteAreaDevice();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public boolean updateArea(Area area) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("Update AreaInfoTable SET AreaName = ?,PictureName = ? WHERE AreaIndex = ?", new Object[]{area.getAreaName(), area.getPictureName(), Short.valueOf(area.getAreaIndex())});
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return $assertionsDisabled;
    }

    public DeviceList getLightToningList(Device mKnob) {
        if (!(LightToningList == null || this.mDataBase == null)) {
            LightToningList.clearAllDevice();
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE  DeviceType IN ('4','100')", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (Arrays.equals(mKnob.getGatewayMacAddr(), device.getGatewayMacAddr())) {
                        LightToningList.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return LightToningList;
    }

    public boolean getLightToningListYesorno(Device mKnob) {
        boolean bolret = $assertionsDisabled;
        if (this.mDataBase != null) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE  DeviceType IN ('4','100')", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (Arrays.equals(mKnob.getGatewayMacAddr(), getDeviceinfo(cursor).getGatewayMacAddr())) {
                        bolret = true;
                        break;
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return bolret;
    }

    public DeviceList getLightallToningList(Device mKnob) {
        if (!(LightToningList == null || this.mDataBase == null)) {
            LightToningList.clearAllDevice();
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType IN ('1','3','4','97','99','100')", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (Arrays.equals(mKnob.getGatewayMacAddr(), device.getGatewayMacAddr())) {
                        LightToningList.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return LightToningList;
    }

    public DeviceList getLightToningListOfSingle(Device mKnob) {
        if (!(LightToningList == null || this.mDataBase == null)) {
            LightToningList.clearAllDevice();
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType IN ('3','99')", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (Arrays.equals(mKnob.getGatewayMacAddr(), device.getGatewayMacAddr())) {
                        LightToningList.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return LightToningList;
    }

    public DeviceList getLightToningListOfSingleandCha(Device mKnob) {
        if (!(LightToningList == null || this.mDataBase == null)) {
            LightToningList.clearAllDevice();
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType IN ('2','3','98','99')", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (Arrays.equals(mKnob.getGatewayMacAddr(), device.getGatewayMacAddr())) {
                        LightToningList.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return LightToningList;
    }

    public DeviceList getLightRGBListOfSingle(Device mKnob) {
        if (!(LightToningList == null || this.mDataBase == null)) {
            LightToningList.clearAllDevice();
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType IN ('1','97')", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (Arrays.equals(mKnob.getGatewayMacAddr(), device.getGatewayMacAddr())) {
                        LightToningList.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return LightToningList;
    }

    public DeviceList getDeviceListofScene(Scene scene) {
        if (!(list == null || this.mDataBase == null)) {
            list.getmDeviceList().clear();
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex IN (SELECT DeviceIndex FROM SceneDeviceTable WHERE SceneInfoIndex = ?);", new String[]{String.valueOf(scene.getSceneInfoIndex())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (device.getDeviceType() != (short) 5 || device.getSubDeviceType() != (short) 3) {
                        list.pushDevice(device);
                    } else if (getLightToningListYesorno(device)) {
                        list.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return list;
    }

    public void addAreaDeviceTableToAll(Device device, Area area) {
        try {
            Object[] objArr = new Object[3];
            objArr[1] = Integer.valueOf(1);
            objArr[2] = Short.valueOf(device.getDeviceIndex());
            this.mDataBase.execSQL("INSERT INTO AreaDeviceTable VALUES(?,?,?)", objArr);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public short SelectLimitDeviceIndex() {
        Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable ORDER BY DeviceIndex DESC LIMIT 1;", null);
        cursor.moveToFirst();
        short DeviceIndex = cursor.getShort(0);
        cursor.close();
        return DeviceIndex;
    }

    public void addAreaDeviceTable(Device device, Area area) {
        try {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable ORDER BY DeviceIndex DESC LIMIT 1;", null);
            cursor.moveToFirst();
            device.setDeviceIndex(cursor.getShort(0));
            cursor.close();
            Object[] objArr = new Object[3];
            objArr[1] = Short.valueOf(area.getAreaIndex());
            objArr[2] = Short.valueOf(device.getDeviceIndex());
            this.mDataBase.execSQL("INSERT INTO AreaDeviceTable VALUES(?,?,?)", objArr);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAreaDevice() {
        this.mDataBase.execSQL("DELETE FROM AreaDeviceTable");
    }

    public void AddAreaDevice(Area mArea, Device device) {
        try {
            Object[] objArr = new Object[3];
            objArr[1] = Short.valueOf(mArea.getAreaIndex());
            objArr[2] = Short.valueOf(device.getDeviceIndex());
            this.mDataBase.execSQL("INSERT INTO AreaDeviceTable VALUES(?,?,?)", objArr);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Area getAreaInfoToDevice(Device device) {
        Area area = new Area();
        Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex = ?", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                area.setAreaIndex(cursor.getShort(0));
                area.setAreaName(cursor.getString(1));
                area.setPictureName(cursor.getString(2));
                cursor.close();
            }
        }
        return area;
    }

    public void deleteSceneDevice() {
        this.mDataBase.execSQL("DELETE FROM SceneDeviceTable");
    }

    public void deleteSceneDevice(Scene scene) {
        this.mDataBase.execSQL("DELETE FROM SceneDeviceTable WHERE SceneInfoIndex = ?", new Object[]{Short.valueOf(scene.getSceneInfoIndex())});
    }

    public void deleteSceneDeviceToDevice(short deviceIndex) {
        this.mDataBase.execSQL("DELETE FROM SceneDeviceTable WHERE DeviceIndex = ?", new Object[]{Short.valueOf(deviceIndex)});
    }

    public void AddSceneDevice(Scene mScene, Device device) {
        Object[] objArr = new Object[5];
        objArr[1] = Short.valueOf(mScene.getSceneInfoIndex());
        objArr[2] = Short.valueOf(device.getDeviceIndex());
        objArr[3] = device.getSceneParams();
        objArr[4] = IntarrayToBytearray(device.getSceneDeviceMac());
        this.mDataBase.execSQL("INSERT INTO SceneDeviceTable VALUES(?,?,?,?,?)", objArr);
}

    public void updateSceneDevice(Scene mScene, Device device) {
        deleteSceneDevice();
    }

    public DeviceList getDeviceListExceptKnobandsenor() {

        if (mDeviceList.getmDeviceList().size() > 0) {
            mDeviceList.clearAllDevice();
        }

        if (!(mDeviceList == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType IN ('1','2','3','4','5','15','97','98','99','100','111')", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    mDeviceList.pushDevice(device);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }else{}
        return mDeviceList;
    }
    public DeviceList getSectorDeviceInfor(String devicename) {
        if (mDeviceList.getmDeviceList().size() > 0) {
            mDeviceList.clearAllDevice();
        }
        if (!(mDeviceList == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceName = ?", new String[]{(devicename)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    mDeviceList.pushDevice(device);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }else{}
        return mDeviceList;
    }
    public DeviceList getSameAddressDevice(short address) {
        if (mDeviceList.getmDeviceList().size() > 0) {
            mDeviceList.clearAllDevice();
        }
        if (!(mDeviceList == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceAddress = ?", new String[]{String.valueOf(address)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    mDeviceList.pushDevice(device);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }else{}
        return mDeviceList;
    }


    public Device getDeviceInforName(String devicename) {
        if (this.mDataBase == null) {
            return null;
        }
        Device device = null;
        Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceName = ?", new String[]{String.valueOf(devicename)});
        if (cursor != null) {
            cursor.moveToFirst();
            device = getDeviceinfo(cursor);
            cursor.close();
        }else
        {
            return null;
        }
        return device;
    }

    public Device getDeviceinfo(Cursor cursor) {
        Device device = new Device();
        device.setDeviceIndex(cursor.getShort(0));
        device.setDeviceName(cursor.getString(1));
        device.setDeviceType(cursor.getShort(2));
        device.setPanId(cursor.getShort(3));
        device.setDeviceAddress(cursor.getShort(4));
        device.setFatherAddress(cursor.getShort(5));
        device.setChannelInfo(cursor.getShort(6));
        device.setCurrentParams(cursor.getBlob(7));
        device.setPictureName(cursor.getString(8));
        device.setSubDeviceType(cursor.getShort(9));
        device.setChannelMark(cursor.getShort(10));
        device.setMacAddress(BytearrayToIntarray(cursor.getBlob(11)));
        Cursor cursor1 = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex = ?", new String[]{String.valueOf(device.getDeviceIndex())});
        if (cursor1 != null) {
            cursor1.moveToFirst();
            if (!cursor1.isAfterLast()) {
                int igatewayindex = cursor1.getShort(2);
                Cursor cursor2 = this.mDataBase.rawQuery("SELECT * FROM GateWayInfoTable WHERE GateWayInfoIndex = ?", new String[]{String.valueOf(igatewayindex)});
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    if (!cursor2.isAfterLast()) {
                        device.setGatewayMacAddr(cursor2.getBlob(1));
                        device.setGatewaySSID(cursor2.getBlob(3));
                        device.setGatewayPassword(cursor2.getBlob(7));
                    }
                    cursor2.close();
                }
            }
            cursor1.close();
        }
        return device;
    }

    public DeviceList getDeviceList() {
        if (mDeviceList.getmDeviceList().size() > 0) {
            mDeviceList.clearAllDevice();
        }
        if (!(mDeviceList == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    mDeviceList.pushDevice(getDeviceinfo(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return mDeviceList;
    }

    public DeviceList getDeviceListofexceptGroup_output() {
        if (mDeviceList.getmDeviceList().size() > 0) {
            mDeviceList.clearAllDevice();
        }
        if (!(mDeviceList == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType <= 96", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    mDeviceList.pushDevice(getDeviceinfo(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return mDeviceList;
    }

    public DeviceList getDeviceListofexceptGroup_outputDevList() {
        if (mDeviceList.getmDeviceList().size() > 0) {
            mDeviceList.clearAllDevice();
        }
        if (!(mDeviceList == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType <= 96", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (device.getDeviceType() != (short) 5 || device.getSubDeviceType() != (short) 3) {
                        mDeviceList.pushDevice(device);
                    } else if (getLightToningListYesorno(device)) {
                        mDeviceList.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return mDeviceList;
    }

    public DeviceList getDeviceListOfToGroup() {
        if (mDeviceList.getmDeviceList().size() > 0) {
            mDeviceList.clearAllDevice();
        }
        if (!(mDeviceList == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType IN ('97','98','99','100','111')", null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    mDeviceList.pushDevice(getDeviceinfo(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return mDeviceList;
    }

    public DeviceList getAllDeviceofAsGroup(Device GroupDevice) {
        if (mKnobOfPairlist.getmDeviceList().size() > 0) {
            mKnobOfPairlist.clearAllDevice();
        }
        if (!(mKnobOfPairlist == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceType = ?", new String[]{String.valueOf(GroupDevice.getDeviceType() - 96)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (GroupDevice.getGatewayMacAddr() != null && Arrays.equals(GroupDevice.getGatewayMacAddr(), device.getGatewayMacAddr())) {
                        mKnobOfPairlist.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return mKnobOfPairlist;
    }


    public byte[] getlightingofDevice(Device device) {
        if (device == null || this.mDataBase == null) {
            return null;
        }
        Cursor cursor = this.mDataBase.rawQuery("SELECT CurrentParams FROM DeviceInfoTable WHERE DeviceName = ?", new String[]{String.valueOf(device.getDeviceName())});
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mCurrParamart = cursor.getBlob(0);
            cursor.moveToNext();
        }
        cursor.close();
        return mCurrParamart;
    }

    public int[] getScenePosofDevice(Device device, Scene scene) {
        int[] mSceneDeviceMac = new int[5];
        if (!(scene == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT SceneDeviceMac FROM SceneDeviceTable WHERE DeviceIndex = ? AND SceneInfoIndex = ?", new String[]{String.valueOf(device.getDeviceIndex()), String.valueOf(scene.getSceneInfoIndex())});
            if (cursor != null) {
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    mSceneDeviceMac = BytearrayToIntarray(cursor.getBlob(0));
                }
                cursor.close();
            }
        }
        return mSceneDeviceMac;
    }

    public byte[] getScenelightingofDevice(Device device, Scene scene) {
        byte[] mSceneParams = new byte[5];
        if (!(scene == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT SceneParams FROM SceneDeviceTable WHERE DeviceIndex = ? AND SceneInfoIndex = ?", new String[]{String.valueOf(device.getDeviceIndex()), String.valueOf(scene.getSceneInfoIndex())});
            if (cursor != null) {
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    mSceneParams = cursor.getBlob(0);
                }
                cursor.close();
            }
        }
        return mSceneParams;
    }

    public void UpdateGateWayInfo(Gateway mGateway) {
        if (this.mDataBase != null) {
            this.mDataBase.execSQL("Update GateWayInfoTable SET MacAddress = ?,GateWayId = ?,SSID = ?,IP = ?,IDN = ?,Port = ?,PassWord = ? WHERE GateWayInfoIndex = ?", new Object[]{mGateway.getMacAddress(), Integer.valueOf(mGateway.getGateWayId()), mGateway.getSSID(), mGateway.getIP(), mGateway.getDNS(), Integer.valueOf(mGateway.getPort()), mGateway.getPassWord(), Short.valueOf(mGateway.getGateWayInfoIndex())});
        }
        SelectGatewayInfo();
    }

    public boolean AddGatewayInfo(Gateway gateway) {
        Object[] objArr = new Object[8];
        objArr[1] = gateway.getMacAddress();
        objArr[2] = Integer.valueOf(gateway.getGateWayId());
        objArr[3] = gateway.getSSID();
        objArr[4] = gateway.getIP();
        objArr[5] = gateway.getDNS();
        objArr[6] = Integer.valueOf(gateway.getPort());
        objArr[7] = gateway.getPassWord();
        this.mDataBase.execSQL("INSERT INTO GateWayInfoTable VALUES(?,?,?,?,?,?,?,?)", objArr);
        SelectGatewayInfo();
        return true;
    }

    public ArrayList<Gateway> SelectGatewayInfo1() {
        return mGatewayList;
    }

    public ArrayList<Gateway> SelectGatewayInfo() {
        System.out.println("*** mDataBase is " + mDataBase);
        System.out.println("*** mGatewayList is " + mGatewayList);
        if (!(mGatewayList == null || this.mDataBase == null)) {
            mGatewayList.clear();
            System.out.println("***  mGatewayList.clear() is called");
        }
        Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM GateWayInfoTable ", null);
        System.out.println("***  mDataBase.rawQuery is called");
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Gateway mGateway = new Gateway();
                mGateway.setGateWayInfoIndex(cursor.getShort(0));
                mGateway.setMacAddress(cursor.getBlob(1));
                mGateway.setGateWayId(cursor.getInt(2));
                mGateway.setSSID(cursor.getBlob(3));
                mGateway.setIP(cursor.getBlob(4));
                mGateway.setDNS(cursor.getBlob(5));
                mGateway.setPort(cursor.getInt(6));
                mGateway.setPassWord(cursor.getBlob(7));
                mGatewayList.add(mGateway);
                cursor.moveToNext();
            }
            cursor.close();
        }

        System.out.println("***  mGatewayList " + mGatewayList);
        return mGatewayList;
    }



    public static void WriteDeviceList(ArrayList<Device> list, String filename) {
        String state;
        state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/Horizon");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, filename);

            try {

                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(list);

                oos.flush();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static ArrayList LoadDeviceList(String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        ArrayList<Device> newArraylist = new ArrayList<>();
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList<Device> arrayList = (ArrayList) ois.readObject();
                newArraylist.addAll(arrayList);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return newArraylist;
    }



    public boolean IFExit(Device mDevice, Device device) {
        boolean boolIshave = $assertionsDisabled;
        Cursor cursor = this.mDataBase.rawQuery("SELECT GroupDeviceIndex FROM GroupDeviceTable WHERE GroupInfoIndex = ? AND DeviceIndex = ?", new String[]{String.valueOf(mDevice.getDeviceIndex()), String.valueOf(device.getDeviceIndex())});
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                boolIshave = true;
            }
            cursor.close();
        }
        return boolIshave;
    }

    private byte[] IntarrayToBytearray(int[] intarray) {
        byte[] bytearray = new byte[(intarray.length * 4)];
        for (int i = 0; i < intarray.length; i++) {
            bytearray[i * 4] = (byte) (intarray[i] >> 24);
            bytearray[(i * 4) + 1] = (byte) ((intarray[i] >> 16) & MotionEventCompat.ACTION_MASK);
            bytearray[(i * 4) + 2] = (byte) ((intarray[i] >> 8) & MotionEventCompat.ACTION_MASK);
            bytearray[(i * 4) + 3] = (byte) (intarray[i] & MotionEventCompat.ACTION_MASK);
        }
        return bytearray;
    }

    private int[] BytearrayToIntarray(byte[] bytearray) {
        int[] intarray = new int[(bytearray.length / 4)];
        for (int i = 0; i < intarray.length; i++) {
            byte[] a = new byte[4];
            System.arraycopy(bytearray, i * 4, a, 0, 4);
            intarray[i] = ((((a[0] & MotionEventCompat.ACTION_MASK) << 24) | ((a[1] & MotionEventCompat.ACTION_MASK) << 16)) | ((a[2] & MotionEventCompat.ACTION_MASK) << 8)) | (a[3] & MotionEventCompat.ACTION_MASK);
        }
        return intarray;
    }


    public boolean AddGateWayDevice(short DeviceIndex, short gateWayInfoIndex) {
        Object[] objArr = new Object[3];
        objArr[1] = Short.valueOf(DeviceIndex);
        objArr[2] = Short.valueOf(gateWayInfoIndex);
        this.mDataBase.execSQL("INSERT INTO GateWayDeviceTable VALUES(?,?,?)", objArr);
        return true;
    }

    public void deleteDeviceOfCurrGateway(int deviceIndex) {
        if (this.mDataBase != null) {
            this.mDataBase.execSQL("DELETE FROM GateWayDeviceTable WHERE DeviceIndex = ?", new Object[]{Integer.valueOf(deviceIndex)});
        }
    }

    public void UpdateGateWayInfoToNewGatewayinfo(short moldindex, short mnewindex) {
        if (this.mDataBase != null) {
            this.mDataBase.execSQL("Update GateWayDeviceTable SET GateWayInfoIndex = ? WHERE GateWayInfoIndex = ?", new Object[]{Short.valueOf(mnewindex), Short.valueOf(moldindex)});
        }
    }

    public DeviceList SelectDeviceToGateWayIndex(short gateWayInfoIndex) {
        if (this.mDataBase != null) {
            if (mGatewayDeviceList != null) {
                mGatewayDeviceList.clearAllDevice();
            }
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex IN (SELECT DeviceIndex FROM GateWayDeviceTable WHERE GateWayInfoIndex = ?)", new String[]{new StringBuilder(String.valueOf(gateWayInfoIndex)).toString()});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    mGatewayDeviceList.pushDevice(getDeviceinfo(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return mGatewayDeviceList;
    }

    public byte[] SelectGateWayPassword(short deviceindex) {
        Cursor cursor = this.mDataBase.rawQuery("SELECT PassWord FROM GateWayInfoTable WHERE GateWayInfoIndex IN (SELECT GateWayInfoIndex FROM GateWayDeviceTable WHERE DeviceIndex = ?)", new String[]{new StringBuilder(String.valueOf(deviceindex)).toString()});
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            return cursor.getBlob(0);
        }
        return null;
    }

    public void deleteGatewayOfCurrGateway(short gatewayIndex) {
        if (this.mDataBase != null) {
            this.mDataBase.execSQL("DELETE FROM GateWayInfoTable WHERE GateWayInfoIndex = ?", new Object[]{Short.valueOf(gatewayIndex)});
        }
        SelectGatewayInfo();
    }

    public void updateDeviceInfo(Device device) {
        if (this.mDataBase != null) {
            try {
                this.mDataBase.execSQL("Update DeviceInfoTable SET CurrentParams = ? WHERE DeviceAddress = ? AND ChannelMark = ?", new Object[]{device.getCurrentParams(), Short.valueOf(device.getDeviceAddress()), Short.valueOf(device.getChannelMark())});
            } catch (Exception e) {
            }
        }
    }

    public short SelectDeviceInfo(short address) {
        Cursor cursor = this.mDataBase.rawQuery("SELECT DeviceIndex FROM DeviceInfoTable WHERE DeviceAddress = ?", new String[]{String.valueOf(address)});
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getShort(0);
            }
            cursor.close();
        }
        return (short) -1;
    }

    public int GetPostIndex(int postindex) {
        return 16777215 & postindex;
    }

    public int GetPostInchannel(int postindex) {
        return postindex >> 24;
    }

    public int SetPostIndex(int postindex, byte channinfo) {
        return ((channinfo & MotionEventCompat.ACTION_MASK) << 24) | (16777215 & postindex);
    }

    public boolean DelKnobDevchann(Device mknob, short deviceindex, int channum) {
        for (int i = 1; i <= channum; i++) {
            int mknobindex = SetPostIndex(mknob.getDeviceIndex(), (byte) i);
            this.mDataBase.execSQL("DELETE FROM KnobDeviceTable WHERE KnobIndex = ? AND DeviceIndex = ?", new Object[]{Integer.valueOf(mknobindex), Short.valueOf(deviceindex)});
        }
        return true;
    }

    public boolean AddKnobDeviceTable(Device mknob, byte channinfo, short deviceindex) {
        if (this.mDataBase == null) {
            return $assertionsDisabled;
        }
        int mknobindex;
        if (mknob.getDeviceType() == (short) 6) {
            mknobindex = SetPostIndex(mknob.getDeviceIndex(), channinfo);
            DelKnobDevchann(mknob, deviceindex, 6);
        } else if (mknob.getDeviceType() == (short) 43) {
            mknobindex = SetPostIndex(mknob.getDeviceIndex(), channinfo);
            DelKnobDevchann(mknob, deviceindex, 3);
        } else if (mknob.getDeviceType() == (short) 44) {
            mknobindex = SetPostIndex(mknob.getDeviceIndex(), channinfo);
            DelKnobDevchann(mknob, deviceindex, 4);
        } else if (mknob.getDeviceType() == (short) 7 && mknob.getSubDeviceType() == (short) 3) {
            mknobindex = SetPostIndex(mknob.getDeviceIndex(), channinfo);
            DelKnobDevchann(mknob, deviceindex, 3);
        } else {
            mknobindex = mknob.getDeviceIndex();
        }
        Object[] objArr = new Object[3];
        objArr[1] = Integer.valueOf(mknobindex);
        objArr[2] = Short.valueOf(deviceindex);
        this.mDataBase.execSQL("INSERT INTO KnobDeviceTable VALUES(?,?,?)", objArr);
        return true;
    }

    public boolean DelKnobDeviceTable(Device mknob, short deviceindex) {
        if ((mknob.getDeviceType() == (short) 7 && mknob.getSubDeviceType() == (short) 3) || mknob.getDeviceType() == (short) 6 || mknob.getDeviceType() == (short) 43 || mknob.getDeviceType() == (short) 44) {
            if (this.mDataBase == null) {
                return $assertionsDisabled;
            }
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM KnobDeviceTable WHERE DeviceIndex = ?", new String[]{new StringBuilder(String.valueOf(deviceindex)).toString()});
            if (cursor == null) {
                return true;
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (GetPostIndex(cursor.getInt(1)) == mknob.getDeviceIndex()) {
                    this.mDataBase.execSQL("DELETE FROM KnobDeviceTable WHERE KnobIndex = ? AND DeviceIndex = ?", new Object[]{Integer.valueOf(cursor.getInt(1)), Short.valueOf(deviceindex)});
                }
                cursor.moveToNext();
            }
            cursor.close();
            return true;
        } else if (this.mDataBase == null) {
            return $assertionsDisabled;
        } else {
            this.mDataBase.execSQL("DELETE FROM KnobDeviceTable WHERE KnobIndex = ? AND DeviceIndex = ?", new Object[]{Short.valueOf(mknob.getDeviceIndex()), Short.valueOf(deviceindex)});
            return true;
        }
    }

    public boolean SeleteKnobDeviceTable(Device mknob, byte channinfo, short deviceindex) {
        int mknobindex;
        boolean boolIshave = $assertionsDisabled;
        if ((mknob.getDeviceType() == (short) 7 && mknob.getSubDeviceType() == (short) 3) || mknob.getDeviceType() == (short) 6 || mknob.getDeviceType() == (short) 43 || mknob.getDeviceType() == (short) 44) {
            mknobindex = SetPostIndex(mknob.getDeviceIndex(), channinfo);
        } else {
            mknobindex = mknob.getDeviceIndex();
        }
        Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM KnobDeviceTable WHERE KnobIndex = ? AND DeviceIndex = ?", new String[]{String.valueOf(mknobindex), String.valueOf(deviceindex)});
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                boolIshave = true;
            }
            cursor.close();
        }
        return boolIshave;
    }

    public boolean deleteDeviceOfCurrKnob(Device mknob) {
        if (this.mDataBase == null) {
            return $assertionsDisabled;
        }
        if ((mknob.getDeviceType() != (short) 7 || mknob.getSubDeviceType() != (short) 3) && mknob.getDeviceType() != (short) 6 && mknob.getDeviceType() != (short) 43 && mknob.getDeviceType() != (short) 44) {
            this.mDataBase.execSQL("DELETE FROM KnobDeviceTable WHERE KnobIndex = ? OR DeviceIndex = ?", new Object[]{Short.valueOf(mknob.getDeviceIndex()), Short.valueOf(mknob.getDeviceIndex())});
            return true;
        } else if (this.mDataBase == null) {
            return $assertionsDisabled;
        } else {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM KnobDeviceTable", null);
            if (cursor == null) {
                return true;
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (GetPostIndex(cursor.getInt(1)) == mknob.getDeviceIndex()) {
                    this.mDataBase.execSQL("DELETE FROM KnobDeviceTable WHERE KnobIndex = ?", new Object[]{Integer.valueOf(cursor.getInt(1))});
                }
                cursor.moveToNext();
            }
            cursor.close();
            return true;
        }
    }

    public DeviceList getDeviceOfKnob(Device mKnob) {
        if (!(list == null || this.mDataBase == null)) {
            list.getmDeviceList().clear();
            Cursor cursor;
            Device device;
            if ((mKnob.getDeviceType() == (short) 7 && mKnob.getSubDeviceType() == (short) 3) || mKnob.getDeviceType() == (short) 6 || mKnob.getDeviceType() == (short) 43 || mKnob.getDeviceType() == (short) 44) {
                cursor = this.mDataBase.rawQuery("SELECT * FROM KnobDeviceTable", null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        short knobin = new Integer(GetPostIndex(cursor.getInt(1))).shortValue();
                        int devindex = cursor.getShort(2);
                        if (knobin == mKnob.getDeviceIndex()) {
                            Cursor cursor1 = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex = ?", new String[]{new StringBuilder(String.valueOf(devindex)).toString()});
                            if (cursor1 != null) {
                                cursor1.moveToFirst();
                                if (!cursor1.isAfterLast()) {
                                    device = getDeviceinfo(cursor1);
                                    device.setChannelInfo((short) GetPostInchannel(cursor.getInt(1)));
                                    if (Arrays.equals(mKnob.getGatewayMacAddr(), device.getGatewayMacAddr())) {
                                        list.pushDevice(device);
                                    }
                                    cursor1.close();
                                }
                            }
                        }
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } else {
                cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex IN (SELECT DeviceIndex FROM KnobDeviceTable WHERE KnobIndex = ?);", new String[]{String.valueOf(mKnob.getDeviceIndex())});
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        device = getDeviceinfo(cursor);
                        if (Arrays.equals(mKnob.getGatewayMacAddr(), device.getGatewayMacAddr())) {
                            list.pushDevice(device);
                        }
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            }
        }
        return list;
    }


    public DeviceList getDeviceOfGroup(Device GroupDevice) {
        try {
            if (DeviceToDeviceType.getmDeviceList().size() > 0) {
                DeviceToDeviceType.clearAllDevice();
            }
            if (!(DeviceToDeviceType == null || this.mDataBase == null)) {
                Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex IN (SELECT DeviceIndex FROM GroupDeviceTable WHERE GroupInfoIndex = ?);", new String[]{String.valueOf(GroupDevice.getDeviceIndex())});
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        DeviceToDeviceType.pushDevice(getDeviceinfo(cursor));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DeviceToDeviceType;
    }

    public void AddGroupDeviceData_AddGroup(Device GroupDevice, Device mDevice) {
        try {
            if (this.mDataBase != null) {
                Cursor cursor = this.mDataBase.rawQuery("SELECT DeviceIndex FROM DeviceInfoTable ORDER BY DeviceIndex DESC LIMIT 1;", null);
                cursor.moveToFirst();
                GroupDevice.setDeviceIndex(cursor.getShort(0));
                Object[] objArr = new Object[3];
                objArr[1] = Short.valueOf(GroupDevice.getDeviceIndex());
                objArr[2] = Short.valueOf(mDevice.getDeviceIndex());
                this.mDataBase.execSQL("INSERT INTO GroupDeviceTable VALUES(?,?,?)", objArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddGroupDeviceData_EditGroup(Device GroupDevice, Device mDevice, boolean boolDel) {
        try {
            if (this.mDataBase == null) {
                return;
            }
            if (boolDel) {
                this.mDataBase.execSQL("DELETE FROM GroupDeviceTable WHERE GroupInfoIndex = ? AND DeviceIndex = ?", new Object[]{Short.valueOf(GroupDevice.getDeviceIndex()), Short.valueOf(mDevice.getDeviceIndex())});
                return;
            }
            Object[] objArr = new Object[3];
            objArr[1] = Short.valueOf(GroupDevice.getDeviceIndex());
            objArr[2] = Short.valueOf(mDevice.getDeviceIndex());
            this.mDataBase.execSQL("INSERT INTO GroupDeviceTable VALUES(?,?,?)", objArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteGroupDeviceData(short devindex) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("DELETE FROM GroupDeviceTable WHERE GroupInfoIndex = ? OR DeviceIndex = ?", new Object[]{Short.valueOf(devindex), Short.valueOf(devindex)});
            }
        } catch (Exception e) {
        }
    }

    public void DelGroupOfmDevice(Device mDevice) {
        try {
            if (this.mDataBase != null) {
                this.mDataBase.execSQL("DELETE FROM GroupDeviceTable WHERE DeviceIndex = ?", new Object[]{Short.valueOf(mDevice.getDeviceIndex())});
            }
        } catch (Exception e) {
        }
        deleteDeviceOfCurrKnob(mDevice);
    }

    public DeviceList getAllDeviceList() {
        return mDeviceList;
    }

    public DeviceList addDevToAllDeviceList(Device mDevice) {
        mDeviceList.pushDevice(mDevice);
        return mDeviceList;
    }

    public SceneList getSceneList() {
        if (mSceneList.getSceneArrayList().size() > 0) {
            mSceneList.getSceneArrayList().clear();
        }
        if (this.mDataBase != null) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM SceneInfoTable", null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Scene scene = new Scene();
                scene.setSceneInfoIndex(cursor.getShort(0));
                scene.setSceneName(cursor.getString(1));
                scene.setPictureName(cursor.getString(2));
                mSceneList.pushScene(scene);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return mSceneList;
    }

    public SceneList getAllSceneList() {
        return mSceneList;
    }

    public AreaList getAllAreaList() {
        return mAreaList;
    }

    public AreaList getAreaList() {
        if (mAreaList.getAreaArrayList().size() > 0) {
            mAreaList.getAreaArrayList().clear();
        }
        if (!(mAreaList == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM AreaInfoTable", null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Area area = new Area();
                area.setAreaIndex(cursor.getShort(0));
                area.setAreaName(cursor.getString(1));
                mAreaList.pushArea(area);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return mAreaList;
    }

    public DeviceList getDeviceListOfArea(Area area) {
        list.getmDeviceList().clear();
        if (!(list == null || this.mDataBase == null || area == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex IN (SELECT DeviceIndex FROM AreaDeviceTable WHERE AreaIndex = ?)", new String[]{String.valueOf(area.getAreaIndex())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Device device = getDeviceinfo(cursor);
                    if (device.getDeviceType() != (short) 5 || device.getSubDeviceType() != (short) 3) {
                        list.pushDevice(device);
                    } else if (getLightToningListYesorno(device)) {
                        list.pushDevice(device);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return list;
    }

    public DeviceList getDeviceListOfAreaToZIGBEE(Area area) {
        list.getmDeviceList().clear();
        if (!(list == null || this.mDataBase == null || area == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex IN (SELECT DeviceIndex FROM AreaDeviceTable WHERE AreaIndex = ?) AND DeviceType IN ('1','3','4','97','99','100')", new String[]{String.valueOf(area.getAreaIndex())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    list.pushDevice(getDeviceinfo(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return list;
    }

    public DeviceList getDeviceListOfScene(Scene scene) {
        list.getmDeviceList().clear();
        if (!(list == null || this.mDataBase == null)) {
            Cursor cursor = this.mDataBase.rawQuery("SELECT * FROM DeviceInfoTable WHERE DeviceIndex IN (SELECT DeviceIndex FROM SceneDeviceTable WHERE SceneInfoIndex = ?)", new String[]{String.valueOf(scene.getSceneInfoIndex())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    list.pushDevice(getDeviceinfo(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return list;
    }


}
