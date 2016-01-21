package com.allin.activity.action;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.ArrayList;
import java.util.Iterator;

public class DataStorage {
    private static DataStorage mInstance;
    Context context;
    ArrayList<String> mExitReleaseList;
    SharedPreferences mSharedPreferences;

    static {
        mInstance = null;
    }

    public DataStorage(Context context) {
        this.mSharedPreferences = null;
        this.mExitReleaseList = null;
        this.mSharedPreferences = context.getSharedPreferences("lcs_config_sp", 0);
        this.mExitReleaseList = new ArrayList();
    }

    public static DataStorage getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataStorage(context);
        }
        return mInstance;
    }

    public void putInt(String key, int data) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putInt(key, data);
        editor.commit();
    }

    public void putString(String key, String data, boolean exitRelease) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putString(key, data);
        if (editor.commit() && exitRelease) {
            this.mExitReleaseList.add(key);
        }
    }

    public void putBoolean(String key, boolean data) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public int getInt(String key) {
        return this.mSharedPreferences.getInt(key, -1);
    }

    public String getString(String key) {
        return this.mSharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return this.mSharedPreferences.getBoolean(key, false);
    }

    public void release() {
        Editor editor = this.mSharedPreferences.edit();
        Iterator it = this.mExitReleaseList.iterator();
        while (it.hasNext()) {
            editor.remove((String) it.next());
        }
        editor.commit();
    }
}
