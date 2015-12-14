package com.example.hesolutions.horizon;

import android.content.Context;
import android.os.Environment;
import android.view.View;
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

    public static class AppendingObjectOutputStream extends ObjectOutputStream {

        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        public void writeStreamHeader() throws IOException {
            // do not write a header, but reset:
            // this line added after another question
            // showed a problem with the original
            reset();
        }

    }

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

    public static void readdata(String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon");
        File file = new File(dir, filename);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Map myNewlyReadInMap = (HashMap) ois.readObject();
            ois.close();
            System.out.println("Reading From " + file + " and the data readed from file is " + myNewlyReadInMap.toString());
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



