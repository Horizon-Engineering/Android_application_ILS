package com.homa.hls.database;

import java.util.ArrayList;

public class AreaList {
    public static ArrayList<Area> mAreaList;

    static {
        mAreaList = null;
    }

    public AreaList() {
        mAreaList = new ArrayList();
    }

    public ArrayList<Area> getAreaArrayList() {
        return mAreaList;
    }

    public void setAreaArrayList(ArrayList<Area> areaArrayList) {
        mAreaList = areaArrayList;
    }

    public void pushArea(Area area) {
        mAreaList.add(area);
    }
}
