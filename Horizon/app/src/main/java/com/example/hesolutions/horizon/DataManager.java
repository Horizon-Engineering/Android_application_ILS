package com.example.hesolutions.horizon;

import android.content.Context;
import android.os.Environment;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hesolutions on 2015-12-10.
 */
public class DataManager {


    //==================Account Setting: Key = Password, Value = AccountName
    public HashMap<String, String> account = new HashMap<String, String>();
    public ArrayList<String> accountname = new ArrayList<String>();
    public HashMap getaccount() {

        dataupdate(account, "account.txt");
        return account;
    }

    public HashMap setaccount(HashMap accountinfo) {
        dataupdate(account,"account.txt");
        this.account = accountinfo;
        writedata(account, "account.txt");
        return account;
    }
    public ArrayList getaccoutname ()
    {
        datagetvalue(accountname, "account.txt");
        return accountname;
    }

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
    public HashMap<String, String> device = new HashMap<String, String>();
    public ArrayList<String> devicename = new ArrayList<String>();
    public HashMap getdevice() {
        dataupdate(device,"device.txt");
        return device;
    }

    public HashMap setdevice(HashMap deviceinfo) {

        dataupdate(device,"device.txt");
        this.device = deviceinfo;
        writedata(device, "device.txt");
        return device;
    }
    public ArrayList getdevicename ()
    {
        datagetvalue(devicename, "device.txt");
        return devicename;
    }
    //==================Sector Setting: Key = SectorName, Value = Hashmap of contained devices
    public HashMap<String, HashMap> sector = new HashMap<String, HashMap>();

    public HashMap getsector() {
        return sector;
    }

    public HashMap setsector(HashMap sectorinfo) {
        this.sector = sectorinfo;
        writedata(sector, "sector.txt");
        return sector;
    }


    //==================Zone Setting: Key = ZoneName, Value = Hashmap of contained sectors
    public HashMap<String, HashMap> zone = new HashMap<String, HashMap>();

    public HashMap getzone() {
        return zone;
    }

    public HashMap setzone(HashMap zoneinfo) {
        this.zone = zoneinfo;
        writedata(zone, "zone.txt");
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

    public ArrayList getGrid()
    {

        //====================read data from arraylist===============================

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

    //===========================================Writedata for Hashmap=============================================

    public static void writedata(HashMap hashmap, String filename) {
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
                oos.writeObject(hashmap);
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



    //==========================read data from hashmap=======================
    public static void readdata(String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Map myNewlyReadInMap = (HashMap) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void dataupdate(HashMap hashmap, String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Map<String, String> readmap = (HashMap) ois.readObject();
                for (Map.Entry<String, String> entry : readmap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    hashmap.put(key, value);
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

    public static void datagetvalue(ArrayList values, String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Map<String, String> readmap = (HashMap) ois.readObject();
                for (Map.Entry<String, String> entry : readmap.entrySet()) {
                    String value = entry.getValue();
                    if (!values.contains(value))
                    {
                        values.add(value);
                    }
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



