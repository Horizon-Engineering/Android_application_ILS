package com.homa.hls.database;

import com.allin.activity.action.SysApplication;
import java.util.ArrayList;

public class SceneList {
    private static ArrayList<Scene> mSceneList;
    private static SceneList sysApplication;

    static {
        mSceneList = null;
        sysApplication = null;
    }

    public SceneList() {
        mSceneList = new ArrayList();
    }

    public static SceneList getInstance() {
        if (sysApplication == null) {
            synchronized (SysApplication.class) {
                if (sysApplication == null) {
                    sysApplication = new SceneList();
                }
            }
        }
        return sysApplication;
    }

    public ArrayList<Scene> getSceneArrayList() {
        return mSceneList;
    }

    public void setSceneArrayList(ArrayList<Scene> sceneArrayList) {
        mSceneList = sceneArrayList;
    }

    public void pushScene(Scene scene) {
        if (mSceneList != null && scene != null) {
            mSceneList.add(scene);
        }
    }
}
