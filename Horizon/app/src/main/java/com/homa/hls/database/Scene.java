package com.homa.hls.database;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class Scene implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final short SCENE_HEADER_SIZE = (short) 98;
    public static final int SCENE_NAME_BUFFER_SIZE = 50;
    private static final long serialVersionUID = 1;
    private ArrayList<Device> DeviceOfCurrenSceneList;
    String PictureName;
    short SceneInfoIndex;
    String SceneName;
    short numOfDeviceCurrenScene;
    public boolean ret;
    byte[] sceneNameBuffer;

    static {
        $assertionsDisabled = !Scene.class.desiredAssertionStatus() ? true : false;
    }

    public Scene() {
        this.DeviceOfCurrenSceneList = null;
        this.ret = true;
        this.sceneNameBuffer = new byte[SCENE_NAME_BUFFER_SIZE];
        this.DeviceOfCurrenSceneList = new ArrayList();
    }

    public void appendDevice(Device device) {
        if ($assertionsDisabled || this.DeviceOfCurrenSceneList != null) {
            this.DeviceOfCurrenSceneList.add(device);
            this.numOfDeviceCurrenScene = (short) this.DeviceOfCurrenSceneList.size();
            return;
        }
        throw new AssertionError();
    }

    public String getSceneName() {
        return this.SceneName;
    }

    public void setSceneName(String sceneName) {
        try {
            if (!sceneName.equals("")) {
                byte[] buf = sceneName.getBytes("UTF-16LE");
                int length = buf.length;
                Arrays.fill(this.sceneNameBuffer, (byte) 0);
                System.arraycopy(buf, 0, this.sceneNameBuffer, 0, length);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.SceneName = sceneName;
    }

    public byte[] getSceneNameBuffer() {
        return this.sceneNameBuffer;
    }

    public short getNumOfDeviceCurrenScene() {
        return this.numOfDeviceCurrenScene;
    }

    public void setNumOfDeviceCurrenScene(short numOfDeviceCurrenScene) {
        this.numOfDeviceCurrenScene = numOfDeviceCurrenScene;
    }

    public ArrayList<Device> getDeviceOfCurrenSceneList() {
        return this.DeviceOfCurrenSceneList;
    }

    public void setDeviceOfCurrenSceneList(ArrayList<Device> deviceOfCurrenSceneList) {
        this.DeviceOfCurrenSceneList = deviceOfCurrenSceneList;
    }

    public short getSceneInfoIndex() {
        return this.SceneInfoIndex;
    }

    public void setSceneInfoIndex(short sceneInfoIndex) {
        this.SceneInfoIndex = sceneInfoIndex;
    }

    public String getPictureName() {
        return this.PictureName;
    }

    public void setPictureName(String pictureName) {
        this.PictureName = pictureName;
    }
}
