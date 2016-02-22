package com.example.hesolutions.horizon;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.homa.hls.database.Device;
import com.mylibrary.WeekView;
import com.mylibrary.WeekViewEvent;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;
/**
 * Created by hesolutions on 2015-12-10.
 */
public class DataManager {

    SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss ");
    //==================Account Setting: Key = Password, Value = AccountName
    public BiMap<String,ArrayList> account = HashBiMap.create();
    public BiMap getaccount() {

        dataupdateBiAr(account, "account");
        return account;
    }

    public BiMap setaccount(BiMap accountinfo) {

        this.account = accountinfo;
        writedata(account, "account");
        return account;
    }

    //====================================Showing the user name ==============================
    public String Username;
    public String getUsername()
    {
        return Username;
    }
    public String setUsername(String name)
    {
        this.Username = name;
        return Username;
    }

    public String colorname;
    public String getcolorname()
    {
        return colorname;
    }
    public String setcolorname(String name)
    {
        this.colorname = name;
        return colorname;
    }

    //---------------------------------------------------------------------------------
    //==================Device Setting: Key = Serial ID, Value = DeviceName
    public BiMap<String, String> device = HashBiMap.create();
    public BiMap getdevice() {
        dataupdate(device, "device");
        return device;
    }

    public BiMap setdevice(BiMap deviceinfo) {

        this.device = deviceinfo;
        writedata(device, "device");
        return device;
    }



    //==================Sector Setting: Key = SectorName, Value = Hashmap of contained devices


    public BiMap<String, HashMap> sector = HashBiMap.create();

    public BiMap getsector() {
        dataupdateBi(sector, "sector");
        return sector;
    }

    public BiMap setsector(BiMap sectorinfo) {
        this.sector = sectorinfo;
        writedata(sector,"sector");
        return sector;
    }




    public static final DataManager manager = new DataManager();

    public static DataManager getInstance() {
        return manager;
    }

    //==================================================LOAD THE GRID=========================
/*
    public Integer totalsum;
    public Integer setGrid(Integer number)
    {
        this.totalsum = number;
        writeGrid();
        return totalsum;
    }

    public ArrayList<String> numberlist = new ArrayList<String>();
    public ArrayList writeGrid ()
    {
        Integer number = 0;
        numberlist.clear();
        for (int i = 0; i < totalsum; i++)
        {
            number = number + 1;
            numberlist.add(number.toString());
        }
        writedata(numberlist, "grid");
        numberlist.clear();
        return numberlist;
    }


*/


    public WeekViewEvent event;
    public WeekViewEvent getthisevent()
    {
        return event;
    }
    public WeekViewEvent setthisevent(WeekViewEvent event)
    {
        this.event = event;
        return event;
    }

    //==================================Arraylist for calendar events====================

    public List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    public List getevents()
    {
        dataupdateList(events, "calendar");
        return events;
    }

    public List setevents(List<WeekViewEvent> list)
    {
        this.events = list;
        writedata1(events, "calendar");
        return events;
    }

    public List<Long> listID = new ArrayList<Long>();
    public List getEventID()
    {
        for (int i= 0; i<events.size(); i++)
        {
            WeekViewEvent event = events.get(i);
            listID.add(event.getId());
        }
        return listID;
    }
    public List setEventID(List list)
    {
        this.listID= list;
        return listID;
    }

    public List<List<Long>> groupID = new ArrayList<List<Long>>();
    public List getGroupID()
    {
        dataupdateList2(groupID,"group");
        return groupID;
    }
    public List setGroupID(List<List<Long>> list)
    {
        this.groupID = list;
        writedata2(groupID,"group");
        return groupID;
    }

    //====================read data from arraylist===============================
/*
    public ArrayList getGrid()
    {

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, "grid");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                numberlist = (ArrayList<String>) ois.readObject();
         } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    return numberlist;

    }
*/
    //===========================================Writedata for BiMap=============================================

    public static void writedata(BiMap bimap, String filename) {
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
                oos.writeObject(bimap);
                oos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


/*
    // ============================================Writedata for Arraylist=========================
    public static void writedata(ArrayList arrayList, String filename) {
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
                oos.writeObject(arrayList);
                System.out.println(arrayList.isEmpty() + "++++++++++++++nothingwritten+++++++++++++++++++++++");
                oos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
    // ============================================Writedata for List=========================
    public static void writedata1(List<WeekViewEvent> list, String filename) {
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

    public static void writedata2(List<List<Long>> list, String filename) {
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



    //==========================read data from hashmap=======================

    public static void dataupdate(BiMap bimap, String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Map<String, String> readmap = (BiMap) ois.readObject();
                for (Map.Entry<String, String> entry : readmap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    bimap.put(key, value);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    public static void dataupdateBi(BiMap bimap, String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Map<String, HashMap> readmap = (BiMap) ois.readObject();
                for (Map.Entry<String, HashMap> entry : readmap.entrySet()) {
                    String key = entry.getKey();
                    HashMap value = entry.getValue();
                    bimap.put(key, value);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dataupdateBiAr(BiMap bimap, String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Map<String, ArrayList> readmap = (BiMap) ois.readObject();
                for (Map.Entry<String, ArrayList> entry : readmap.entrySet()) {
                    String key = entry.getKey();
                    ArrayList value = entry.getValue();
                    bimap.put(key, value);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dataupdateList(List<WeekViewEvent> list, String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                List<WeekViewEvent> listone = (List)ois.readObject();
                list.clear();
                for (int i =0; i<listone.size();i++)
                {
                    list.add(listone.get(i));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public static void dataupdateList2(List<List<Long>> list, String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                List<List<Long>> listone = (List)ois.readObject();
                list.clear();
                for (int i =0; i<listone.size();i++)
                {
                    List<Long> list2 = listone.get(i);
                    List<Long> list3 = new ArrayList<>();
                    for (int j = 0; j < list2.size();j++)
                    {
                       list3.add(list2.get(j));
                    }
                    list.add(list3);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}



