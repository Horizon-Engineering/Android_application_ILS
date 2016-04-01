package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allin.activity.action.SysApplication;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.homa.hls.database.Area;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.mylibrary.WeekView;
import com.mylibrary.WeekViewEvent;
import com.zxing.activity.CaptureActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class AdminPage extends Activity {

    HashMap<String, HashMap> sector;
    BiMap<String, ArrayList> nameset;
    HashMap<String, ArrayList<Device>>sectordetail;
    ArrayList<Device> mDeviceList;
    TextView inforsumuser,inforsumsector,inforsumdevice;
    Button adduser, addsector, adddevice;
    RelativeLayout userlistlayout, sectorlistlayout, devicelistlayout, infor;
    String userName = "";
    String sectorName = "";
    String deviceName = "";
    UserCustomListAdapter useradapter;
    MyCustomListAdapter sectoradapter;
    MyCustomListAdapterfordevice deviceadapter;
    ArrayList<String> sectorArray = new ArrayList<>();
    ArrayList<String> devicename = new ArrayList<>();
    final ArrayList<String> names = new ArrayList<>();
    int Selected_User = -1;
    int Selected_Sector = -1;
    int Selected_Device = -1;
    Handler myHandler;
    Runnable myRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        userlistlayout = (RelativeLayout)findViewById(R.id.userlistlayout);
        sectorlistlayout = (RelativeLayout)findViewById(R.id.sectorlistlayout);
        devicelistlayout = (RelativeLayout)findViewById(R.id.devicelistlayout);
        adduser = (Button)findViewById(R.id.adduser);
        addsector= (Button)findViewById(R.id.addsector);
        adddevice= (Button)findViewById(R.id.adddevice);
        infor = (RelativeLayout)findViewById(R.id.infor);
        inforsumuser = (TextView)findViewById(R.id.inforsumuser);
        inforsumsector = (TextView)findViewById(R.id.inforsumsector);
        inforsumdevice = (TextView)findViewById(R.id.inforsumdevice);
        myHandler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AdminPage.this, ScreenSaver.class);
                myHandler.removeCallbacks(myRunnable);
                startActivity(intent);
            }
        };
        myHandler.postDelayed(myRunnable, 3*60*1000);
        LoadUserList();
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                Bitmap bitmap = getScreenShot(rootView);
                DataManager.getInstance().setBitmap(bitmap);
                Intent startNewActivityIntent = new Intent(AdminPage.this, AdminAddNew.class);
                startNewActivityIntent.putExtra("Case", 1);
                ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                activityadminStack.push("AdminAddNew", startNewActivityIntent);

            }
        });
        addsector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                Bitmap bitmap = getScreenShot(rootView);
                DataManager.getInstance().setBitmap(bitmap);
                Intent startNewActivityIntent = new Intent(AdminPage.this, AdminAddNew.class);
                startNewActivityIntent.putExtra("Case", 2);
                startNewActivityIntent.putExtra("userName", userName);
                ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                activityadminStack.push("AdminAddNew", startNewActivityIntent);

            }
        });
        adddevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                Bitmap bitmap = getScreenShot(rootView);
                DataManager.getInstance().setBitmap(bitmap);
                Intent startNewActivityIntent = new Intent(AdminPage.this, CaptureActivity.class);
                ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                startNewActivityIntent.putExtra("userName", userName);
                startNewActivityIntent.putExtra("sectorName", sectorName);
                activityadminStack.push("Scanner", startNewActivityIntent);
            }
        });

        GetSummary();
    }

    public void LoadUserList()
    {
        sectorlistlayout.setVisibility(View.INVISIBLE);
        devicelistlayout.setVisibility(View.INVISIBLE);
        ListView userlist = (ListView) findViewById(R.id.userlist);
        nameset = DataManager.getInstance().getaccount();
        if (!nameset.isEmpty()) {
            names.clear();
            for (Map.Entry<String, ArrayList> entry : nameset.entrySet()) {
                String name = (String) entry.getValue().get(0);
                names.add(name);
            }
            useradapter = new UserCustomListAdapter(this, names);
            userlist.setAdapter(useradapter);
            registerForContextMenu(userlist);
            userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    clickEvent(view);
                    Selected_User = position;
                    Selected_Sector = -1;
                    Selected_Device = -1;
                    useradapter.notifyDataSetChanged();
                }
            });

        }else
        {
            userlist.setAdapter(null);

        }

    }
    public class UserCustomListAdapter extends ArrayAdapter<String> {

        private Activity context;
        private ArrayList<String> userlist;

        public UserCustomListAdapter(Activity adminPage,ArrayList<String> nameslist) {
            super(adminPage, R.layout.row, nameslist);
            this.userlist = nameslist;
            this.context = adminPage;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.row, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
            txtTitle.setText(userlist.get(position));

            if (position==Selected_User)
            {
                txtTitle.setBackground(getResources().getDrawable(R.drawable.buttonclicked));
            }else
            {
                txtTitle.setBackground(getResources().getDrawable(R.drawable.buttonunclick));
            }
            return rowView;
        }

    }

    public void clickEvent(View v) {
//=====================case:User - Sector
        sector = DataManager.getInstance().getsector();
        userName = ((TextView) v).getText().toString();
        sectordetail= sector.get(userName);
        sectorlistlayout.setVisibility(View.VISIBLE);
        addsector.setVisibility(View.VISIBLE);
        devicelistlayout.setVisibility(View.INVISIBLE);
        ListView sectorlist = (ListView) findViewById(R.id.sectorlist);
        if (sectordetail!=null)
        {
            sectorArray.clear();
            for (Map.Entry<String, ArrayList<Device>> entry : sectordetail.entrySet()) {
                sectorArray.add(entry.getKey());
            }
            sectoradapter = new MyCustomListAdapter(this, sectorArray);
            sectorlist.setVisibility(View.VISIBLE);
            sectorlist.setAdapter(sectoradapter);
            registerForContextMenu(sectorlist);
            sectorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showDevice(view);
                    Selected_Sector = position;
                    Selected_Device = -1;
                    sectoradapter.notifyDataSetChanged();
                }
            });
        }else{
            sectorlist.setAdapter(null);
        }


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.sectorlist) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            sectorName = sectorArray.get(info.position);
            menu.setHeaderTitle(sectorName);
            menu.add(0, 0, 0, "Share");
            menu.add(0, 1, 0, "Remove");
        }
        if (v.getId() == R.id.userlist) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            userName = names.get(info.position);
            menu.setHeaderTitle(userName);
            menu.add(0, 2, 0, "Delete");
            menu.add(0, 4, 0, "Change Passwords");
        }
        if (v.getId() == R.id.devicelist)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            deviceName = devicename.get(info.position);
            menu.setHeaderTitle(userName);
            menu.add(0, 3, 0, "Delete");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        sector = DataManager.getInstance().getsector();
        if (menuItemIndex == 1)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPage.this.getParent());
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Do you want to remove the sector?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ArrayList<Device> array = (ArrayList<Device>) sector.get(userName).get(sectorName);
                    //// delete the lights if this sector is the last one contains device information
                    RemoveEvents(userName, sectorName);
                    boolean deletedevice = true;
                    HashMap<String, HashMap> sector = DataManager.getInstance().getsector();
                    if (!sector.isEmpty()) {
                        for (Map.Entry<String, HashMap> entry : sector.entrySet()) {
                            String name = (String) entry.getKey();
                            if (!name.equals(userName)) {
                                //check if any other user contains the sector
                                HashMap<String, ArrayList<Device>> sectorinfo = entry.getValue();
                                for (Map.Entry<String, ArrayList<Device>> entrys : sectorinfo.entrySet()) {
                                    if (entrys.getKey().equals(sectorName)) {
                                        deletedevice = false;
                                    }
                                }
                            }
                        }
                    }
                    if (deletedevice == true) {
                        int devicenum = DataManager.getInstance().getDevicenum();
                        if (array!=null) {
                            devicenum = devicenum - array.size();
                        }
                        DataManager.getInstance().setDevicenum(devicenum);
                        int sectornum = DataManager.getInstance().getSectornum();
                        sectornum--;
                        DataManager.getInstance().setSectornum(sectornum);
                        inforsumsector.setText("Total number of sectors is: " + sectornum);
                        inforsumdevice.setText("Total number of devices is: " + devicenum);
                        ArrayList<Device> check = DatabaseManager.getInstance().LoadDeviceList("devicelist");
                        if (array!=null) {
                            Iterator<Device> deviceIterator = array.iterator();
                            Iterator<Device> checkdevice = check.iterator();
                            while (deviceIterator.hasNext()) {
                                // remove from the devicelist file
                                Device device = deviceIterator.next();
                                if (checkdevice != null) {
                                    while (checkdevice.hasNext()) {
                                        if (checkdevice.next().getDeviceName().equals(device.getDeviceName())) {
                                            checkdevice.remove();
                                        }
                                    }
                                }
                                // remove from the SQL table
                                byte[] data;
                                data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                                device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                        device.getGatewaySSID(), AdminPage.this));
                                device.setCurrentParams(data);
                                DatabaseManager.getInstance().updateDevice(device);
                                DatabaseManager.getInstance().deleteDevice(device);
                                deviceIterator.remove();
                            }

                            DatabaseManager.getInstance().WriteDeviceList(check, "devicelist");

                        }

                        Bitmap bitmap = dataupdate(sectorName+".png");
                        if (bitmap!=null)
                        {
                            File root = Environment.getExternalStorageDirectory();
                            File dir = new File(root.getAbsolutePath() + "/Horizon/Bitmap");
                            File file = new File(dir, sectorName+".png");
                            file.delete();
                        }
                    }else
                    {
                        if (array!=null) {
                            array.clear();
                        }
                    }
                    ListView deviceList = (ListView) findViewById(R.id.devicelist);
                    deviceList.setAdapter(null);
                    sectordetail.remove(sectorArray.get(info.position));
                    sector.put(userName, sectordetail);
                    DataManager.getInstance().setsector(sector);
                    sectorArray.remove(info.position);
                    sectoradapter.notifyDataSetChanged();
                    adddevice.setVisibility(View.INVISIBLE);
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }else if (menuItemIndex == 0)
        {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            Bitmap bitmap = getScreenShot(rootView);
            DataManager.getInstance().setBitmap(bitmap);
            Intent startNewActivityIntent = new Intent(AdminPage.this, AdminAddNew.class);
            startNewActivityIntent.putExtra("Case", 4);
            startNewActivityIntent.putExtra("UserName",userName);
            startNewActivityIntent.putExtra("SectorName",sectorArray.get(info.position));
            ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
            activityadminStack.push("AdminAddNew", startNewActivityIntent);
        }else if (menuItemIndex == 2)
        {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPage.this.getParent());
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Do you want to delete the user?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    boolean deletedevice = true;
                    //// delete the lights if this sector is the last one contains device information
                    HashMap<String, ArrayList<Device>> sectorinformation = sector.get(userName);
                    int sectornumber = DataManager.getInstance().getSectornum();
                    if (sectorinformation!=null) {
                        for (Map.Entry<String, ArrayList<Device>> selfloop : sectorinformation.entrySet()) {
                            String deleteselfsectorname = selfloop.getKey();
                            Bitmap bitmap = dataupdate(deleteselfsectorname+".png");
                            if (bitmap!=null)
                            {
                                File root = Environment.getExternalStorageDirectory();
                                File dir = new File(root.getAbsolutePath() + "/Horizon/Bitmap");
                                File file = new File(dir, deleteselfsectorname+".png");
                                file.delete();
                            }


                            for (Map.Entry<String, HashMap> entry : sector.entrySet()) {
                                String name = (String) entry.getKey();
                                if (!name.equals(userName)) {
                                    //check if any other user contains the sector
                                    HashMap<String, ArrayList<Device>> sectorinfo = entry.getValue();
                                    for (Map.Entry<String, ArrayList<Device>> entrys : sectorinfo.entrySet()) {
                                        if (entrys.getKey().equals(deleteselfsectorname)) {
                                            deletedevice = false;
                                        }
                                    }
                                }
                            }

                            if (deletedevice == true) {
                                sectornumber--;
                                ArrayList<Device> array = (ArrayList<Device>) sector.get(userName).get(deleteselfsectorname);
                                int devicenum = DataManager.getInstance().getDevicenum();
                                if (array != null) {
                                    devicenum = devicenum - array.size();
                                }
                                DataManager.getInstance().setDevicenum(devicenum);
                                inforsumdevice.setText("Total number of devices is: " + devicenum);
                                ArrayList<Device> check = DatabaseManager.getInstance().LoadDeviceList("devicelist");
                                if (array != null) {
                                    Iterator<Device> deviceIterator = array.iterator();
                                    Iterator<Device> checkdevice = check.iterator();
                                    while (deviceIterator.hasNext()) {
                                        // remove from the devicelist file
                                        Device device = deviceIterator.next();
                                        if (checkdevice != null) {
                                            while (checkdevice.hasNext()) {
                                                if (checkdevice.next().getDeviceName().equals(device.getDeviceName())) {
                                                    checkdevice.remove();
                                                }
                                            }
                                        }
                                        // remove from the SQL table
                                        byte[] data;
                                        data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                                        device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                                device.getGatewaySSID(), AdminPage.this));
                                        device.setCurrentParams(data);
                                        DatabaseManager.getInstance().updateDevice(device);
                                        DatabaseManager.getInstance().deleteDevice(device);
                                        deviceIterator.remove();
                                    }

                                    DatabaseManager.getInstance().WriteDeviceList(check, "devicelist");

                                }
                            }
                        }
                    }

                    for (Map.Entry<String, ArrayList> entry : nameset.entrySet()) {
                        ArrayList<String>  account = entry.getValue();
                        String passwords = entry.getKey();
                        if (account.get(0).equals(userName))
                        {
                            nameset.remove(passwords);
                            DataManager.getInstance().setaccount(nameset);
                            break;
                        }
                    }

                    RemoveEventsUser();

                    sector.remove(userName);
                    int usernum = DataManager.getInstance().getUsernum();
                    usernum --;
                    DataManager.getInstance().setUsernum(usernum);
                    DataManager.getInstance().setSectornum(sectornumber);
                    DataManager.getInstance().setsector(sector);
                    names.remove(userName);
                    useradapter.notifyDataSetChanged();
                    inforsumuser.setText("Total number of users is: " + usernum);
                    inforsumsector.setText("Total number of sectors is: " + sectornumber);
                    ListView deviceList = (ListView) findViewById(R.id.devicelist);
                    deviceList.setAdapter(null);
                    ListView sectorlist = (ListView) findViewById(R.id.sectorlist);
                    sectorlist.setAdapter(null);
                    adddevice.setVisibility(View.INVISIBLE);
                    addsector.setVisibility(View.INVISIBLE);
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();


        }else if (menuItemIndex == 3)
        {
            // delete single device
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPage.this.getParent());
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Do you want to delete the device?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    HashMap<String, HashMap<String, ArrayList<Device>>> sector = DataManager.getInstance().getsector();
                    ArrayList<Device> check = DatabaseManager.getInstance().LoadDeviceList("devicelist");
                    HashMap<String, ArrayList<Device>> sectioninformation = sector.get(userName);
                    ArrayList<Device> devicelist = sectioninformation.get(sectorName);
                    if (!sector.isEmpty()) {
                        for (Device device : devicelist) {
                            // turn off the light and remove from the table
                            if (device.getDeviceName().equals(deviceName)) {
                                byte[] data;
                                data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                                device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                        device.getGatewaySSID(), AdminPage.this));
                                device.setCurrentParams(data);
                                DatabaseManager.getInstance().updateDevice(device);
                                DatabaseManager.getInstance().deleteDevice(device);
                                devicelist.remove(device);
                            }
                        }
                        Iterator<Device> checkdevice = check.iterator();
                        // remove from the devicelist file
                        if (checkdevice != null) {
                            while (checkdevice.hasNext()) {
                                if (checkdevice.next().getDeviceName().equals(deviceName)) {
                                    checkdevice.remove();
                                }
                            }
                        }
                        DatabaseManager.getInstance().WriteDeviceList(check, "devicelist");
                        sectioninformation.put(sectorName, devicelist);
                        sector.put(userName, sectioninformation);
                        DataManager.getInstance().setsector(sector);
                        ListView deviceList = (ListView) findViewById(R.id.devicelist);
                        deviceList.setAdapter(null);
                        ListView sectorlist = (ListView) findViewById(R.id.sectorlist);
                        sectorlist.setAdapter(null);
                        int devicenum = DataManager.getInstance().getDevicenum();
                        devicenum--;
                        DataManager.getInstance().setDevicenum(devicenum);
                        inforsumdevice.setText("Total number of devices is: " + devicenum);
                    }
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();


        }else if (menuItemIndex == 4)
        {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            Bitmap bitmap = getScreenShot(rootView);
            DataManager.getInstance().setBitmap(bitmap);
            Intent startNewActivityIntent = new Intent(AdminPage.this, AdminAddNew.class);
            startNewActivityIntent.putExtra("Case", 6);
            startNewActivityIntent.putExtra("UserName",userName);
            ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
            activityadminStack.push("AdminAddNew", startNewActivityIntent);
        }

        return true;
    }

    public class MyCustomListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> sectolist;

        public MyCustomListAdapter(Activity context, ArrayList<String> sectolist) {
            super(context, R.layout.sectorlist, sectolist);
            this.context = context;
            this.sectolist = sectolist;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.sectorlist, null);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
            final String sectorname1 = sectolist.get(position);
            txtTitle.setText(sectorname1);

            if (position==Selected_Sector)
            {
                txtTitle.setBackground(getResources().getDrawable(R.drawable.buttonclicked));
            }else
            {
                txtTitle.setBackground(getResources().getDrawable(R.drawable.buttonunclick));
            }
            return rowView;
        }
    }

    public void showDevice(View v)
    {
        devicename = new ArrayList<>();
        sectorName = ((TextView) v).getText().toString();
        devicelistlayout.setVisibility(View.VISIBLE);
        adddevice.setVisibility(View.VISIBLE);
        ListView deviceList = (ListView) findViewById(R.id.devicelist);
        if  (sectordetail!=null)
        {
            mDeviceList = sectordetail.get(sectorName);
            if (mDeviceList!=null){
                for (int i = 0; i < mDeviceList.size(); i++) {
                    devicename.add(mDeviceList.get(i).getDeviceName());
                }
                deviceadapter = new MyCustomListAdapterfordevice(this, devicename);
                deviceList.setVisibility(View.VISIBLE);
                registerForContextMenu(deviceList);
                deviceList.setAdapter(deviceadapter);
            }else {
                deviceList.setAdapter(null);
            }

            deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Selected_Device = position;
                    deviceadapter.notifyDataSetChanged();
                }
            });
        }
    }
    public class MyCustomListAdapterfordevice extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> devicenamelist;

        public MyCustomListAdapterfordevice(Activity context, ArrayList<String> zoneList) {
            super(context, R.layout.devicelistadmin, zoneList);
            this.context = context;
            this.devicenamelist = zoneList;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.devicelistadmin, null);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
            String devicename1 = devicenamelist.get(position);
            txtTitle.setText(devicename1);
            if (position==Selected_Device)
            {
                txtTitle.setBackground(getResources().getDrawable(R.drawable.buttonclicked));
            }else
            {
                txtTitle.setBackground(getResources().getDrawable(R.drawable.buttonunclick));
            }
            return rowView;
        }

    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache(),0,100,750,
                screenView.getDrawingCache().getHeight()-100);
        screenView.setDrawingCacheEnabled(false);

        return bitmap;
    }
    public void GetSummary(){
        int usernumber = DataManager.getInstance().getUsernum();
        int sectornumber = DataManager.getInstance().getSectornum();
        int devicenumber = DataManager.getInstance().getDevicenum();
        inforsumuser.setText("Total number of users is: " + usernumber);
        inforsumsector.setText("Total number of sectors is: " + sectornumber);
        inforsumdevice.setText("Total number of devices is: " + devicenumber);
    }


    public void RemoveEvents(String belongeduser, String deletethissector){
        List<WeekViewEvent> events = DataManager.getInstance().getnewevents();
        if (events.size() != 0) {
            Iterator<WeekViewEvent> eventIterator = events.iterator();
            while (eventIterator.hasNext()) {
                WeekViewEvent event = eventIterator.next();
                ArrayList<String> sectorsname = event.getdeviceList();
                if (event.getName().equals(belongeduser)&&sectorsname.contains(deletethissector)){
                    sectorsname.remove(deletethissector);
                }
                if (sectorsname.size()==0)eventIterator.remove();
            }
        }
        DataManager.getInstance().setnewevents(events);
        List<WeekViewEvent> futureevents = DataManager.getInstance().getevents();
        if (events.size() != 0) {
            Iterator<WeekViewEvent> eventIterator = futureevents.iterator();
            while (eventIterator.hasNext()) {
                WeekViewEvent event = eventIterator.next();
                ArrayList<String> sectorsname = event.getdeviceList();
                if (event.getName().equals(belongeduser) && sectorsname.contains(deletethissector)){
                    sectorsname.remove(deletethissector);
                }
                if (sectorsname.size()==0)eventIterator.remove();
            }
        }
        DataManager.getInstance().setevents(futureevents);


    }

    public void RemoveEventsUser() {
        Gateway gateway = SysApplication.getInstance().getCurrGateway(AdminPage.this);
        if (gateway!=null) {
            List<WeekViewEvent> events = DataManager.getInstance().getnewevents();
            if (events.size() != 0) {
                Iterator<WeekViewEvent> eventIterator = events.iterator();
                while (eventIterator.hasNext()) {
                    WeekViewEvent event = eventIterator.next();
                    if (event.getName().equals(userName))eventIterator.remove();
                }
            }
            DataManager.getInstance().setnewevents(events);

            List<WeekViewEvent> comingevents = DataManager.getInstance().getevents();
            if (comingevents.size() != 0) {
                Iterator<WeekViewEvent> eventIterator = comingevents.iterator();
                while (eventIterator.hasNext()) {
                    WeekViewEvent event = eventIterator.next();
                    if (event.getName().equals(userName))eventIterator.remove();
                }
            }
            DataManager.getInstance().setevents(comingevents);
        }
    }
    public static Bitmap dataupdate(String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon/Bitmap");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream streamIn = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(streamIn);
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable,3*60*1000);

    }

    @Override
    public void onResume() {
        super.onResume();
        myHandler.postDelayed(myRunnable, 6*30 * 1000);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        myHandler.removeCallbacks(myRunnable);
    }
}


