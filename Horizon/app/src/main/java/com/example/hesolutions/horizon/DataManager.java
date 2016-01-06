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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.hesolutions.mylibrary.WeekViewEvent;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;
/**
 * Created by hesolutions on 2015-12-10.
 */
public class DataManager {


    //==================Account Setting: Key = Password, Value = AccountName
    public BiMap<String,String> account = HashBiMap.create();
    public BiMap getaccount() {

        dataupdate(account, "account");
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
    //---------------------------------------------------------------------------------
    //==================Device Setting: Key = Serial ID, Value = DeviceName
    public BiMap<String, String> device = HashBiMap.create();
    public BiMap getdevice() {
        dataupdate(device,"device");
        return device;
    }

    public BiMap setdevice(BiMap deviceinfo) {

        this.device = deviceinfo;
        writedata(device, "device");
        return device;
    }



    //==================Sector Setting: Key = SectorName, Value = Hashmap of contained devices
    public BiMap<String, BiMap> sector = HashBiMap.create();

    public BiMap getsector() {
        dataupdateBi(sector, "sector");
        return sector;
    }

    public BiMap setsector(BiMap sectorinfo) {
        this.sector = sectorinfo;
        writedata(sector, "sector");
        return sector;
    }


    //==================Zone Setting: Key = ZoneName, Value = Hashmap of contained sectors
    public BiMap<String, BiMap> zone = HashBiMap.create();

    public BiMap getzone() {

        dataupdateBi(zone, "zone");
        return zone;
    }

    public BiMap setzone(BiMap zoneinfo) {
        this.zone = zoneinfo;
        writedata(zone, "zone");
        return zone;
    }

    public static final DataManager manager = new DataManager();

    public static DataManager getInstance() {
        return manager;
    }

    //==================================================LOAD THE GRID=========================

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
        writedata(numberlist, "grid.txt");
        numberlist.clear();
        return numberlist;
    }




    //==================================Arraylist for calendar events====================

    public List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    public List getevents()
    {
        dataupdateList(events,"calendar");
        return events;
    }
    public List setevents(WeekViewEvent event)
    {
        events.add(event);
        writedata(events,"calendar");
        return events;
    }
    public List deleteevent(WeekViewEvent event)
    {
        events.remove(event);
        writedata(events, "calendar");
        return events;
    }



    //====================read data from arraylist===============================

    public ArrayList getGrid()
    {

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, "grid.txt");
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
                oos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ============================================Writedata for List=========================
    public static void writedata(List<WeekViewEvent> list, String filename) {
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
                Map<String, BiMap> readmap = (BiMap) ois.readObject();
                for (Map.Entry<String, BiMap> entry : readmap.entrySet()) {
                    String key = entry.getKey();
                    BiMap value = entry.getValue();
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
                for (int i = 0; i < listone.size(); i++)
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

}



