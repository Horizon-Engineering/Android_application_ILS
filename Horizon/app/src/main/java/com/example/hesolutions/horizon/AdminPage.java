package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.mylibrary.WeekViewEvent;
import com.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AdminPage extends Activity {

    BiMap<String, HashMap> sector;
    BiMap<String, ArrayList> nameset;
    HashMap<String, ArrayList<Device>>sectordetail;
    ArrayList<Device> mDeviceList;
    Device mDevice;
    Button adduser, addsector, adddevice;
    RelativeLayout userlistlayout, sectorlistlayout, devicelistlayout;
    String userName = "";
    String sectorName = "";
    UserCustomListAdapter useradapter;
    MyCustomListAdapter sectoradapter;
    MyCustomListAdapterfordevice deviceadapter;
    Boolean goahead = false;
    ArrayList<String> sectorArray = new ArrayList<>();
    final ArrayList<String> names = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        userlistlayout = (RelativeLayout)findViewById(R.id.userlistlayout);
        sectorlistlayout = (RelativeLayout)findViewById(R.id.sectorlistlayout);
        devicelistlayout = (RelativeLayout)findViewById(R.id.devicelistlayout);
        adduser = (Button)findViewById(R.id.adduser);
        addsector= (Button)findViewById(R.id.addsector);
        adddevice= (Button)findViewById(R.id.adddevice);
        LoadUserList();

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
                    for (int i = 0; i < names.size(); i++) {
                        if (position == i) {
                            parent.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.buttonclicked));
                        } else {
                            parent.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.buttonunclick));
                        }

                    }
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
            return rowView;
        }

    }

    public void clickEvent(View v) {
//=====================case:User - Sector
        sector = DataManager.getInstance().getsector();
        userName = ((TextView) v).getText().toString();
        sectordetail= sector.get(userName);
        sectorlistlayout.setVisibility(View.VISIBLE);
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
                    for (int i = 0; i < sectorArray.size(); i++) {
                        if (position == i) {
                            parent.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.buttonclicked));
                        } else {
                            parent.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.buttonunclick));
                        }

                    }
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
            menu.setHeaderTitle(sectorArray.get(info.position));
            menu.add(0, 0, 0, "Share");
            menu.add(0, 1, 0, "Remove");
        }
        if (v.getId() == R.id.userlist) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(names.get(info.position));
            menu.add(0, 2, 0, "Delete");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        sector = DataManager.getInstance().getsector();
        if (menuItemIndex == 1)
        {
            sector.remove(userName);
            sectordetail.remove(sectorArray.get(info.position));
            sector.put(userName, sectordetail);
            DataManager.getInstance().setsector(sector);
            sectorArray.remove(info.position);
            sectoradapter.notifyDataSetChanged();
            ListView deviceList = (ListView) findViewById(R.id.devicelist);
            deviceList.setAdapter(null);
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
            /*
            sector.remove(userName);
            DataManager.getInstance().setsector(sector);
            nameset.remove(userName);
            DataManager.getInstance().setaccount(nameset);
            names.remove(info.position);
            useradapter.notifyDataSetChanged();
            */
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



            addsector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                    Bitmap bitmap = getScreenShot(rootView);
                    DataManager.getInstance().setBitmap(bitmap);
                    Intent startNewActivityIntent = new Intent(AdminPage.this, AdminAddNew.class);
                    startNewActivityIntent.putExtra("Case",2);
                    startNewActivityIntent.putExtra("userName",userName);
                    ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                    activityadminStack.push("AdminAddNew", startNewActivityIntent);

                }
            });


            return rowView;
        }
    }

    public void showDevice(View v)
    {
        ArrayList<String> devicename = new ArrayList<>();
        sectorName = ((TextView) v).getText().toString();
        devicelistlayout.setVisibility(View.VISIBLE);
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
                deviceList.setAdapter(deviceadapter);
            }else {
                deviceList.setAdapter(null);
            }
        }
    }
    public class MyCustomListAdapterfordevice extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> devicelist;

        public MyCustomListAdapterfordevice(Activity context, ArrayList<String> zoneList) {
            super(context, R.layout.devicelistadmin, zoneList);
            this.context = context;
            this.devicelist = zoneList;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.devicelistadmin, null);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
            String devicename1 = devicelist.get(position);
            txtTitle.setText(devicename1);

            adddevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdminPage.this.startActivityForResult(new Intent(AdminPage.this, CaptureActivity.class), 0);
                    AdminPage.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });

            return rowView;
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 0) {
            Toast.makeText(this, getResources().getString(R.string.scanerfail), Toast.LENGTH_LONG).show();
        } else if (resultCode == -1) {
            String contents = data.getExtras().getString("result");
            boolean boolresu = false;
            if (contents != null && contents.length() == 7) {
                int devtype;
                int subdevtype = 0;
                if (Integer.parseInt(contents.substring(1, 2)) == 2 && Integer.parseInt(contents.substring(0, 1)) == 5) {
                    subdevtype = 2;
                    devtype = 5;
                } else if (Integer.parseInt(contents.substring(1, 2)) != 1) {
                    devtype = Integer.parseInt(contents.substring(0, 2));
                } else {
                    devtype = Integer.parseInt(contents.substring(0, 1));
                }
                String deviceaddress = contents.substring(2, contents.length());
                if (Integer.parseInt(deviceaddress) <= 65500) {

                    mDevice = new Device();
                    if (subdevtype > 0) {
                        try {
                            mDevice.setSubDeviceType((short) subdevtype);
                        } catch (Exception e) {
                            Toast.makeText(this, getResources().getString(R.string.scanerfail), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    mDevice.setChannelInfo((short) 1);
                    mDevice.setDeviceType((short) devtype);
                    mDevice.setDeviceAddress((short) Integer.parseInt(deviceaddress));
                    if (!findDeviceAddress(mDevice.getDeviceAddress()))
                    {
                        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                        Bitmap bitmap = getScreenShot(rootView);
                        DataManager.getInstance().setBitmap(bitmap);
                        Intent startNewActivityIntent = new Intent(AdminPage.this, AdminAddNew.class);
                        startNewActivityIntent.putExtra("Case",3);
                        startNewActivityIntent.putExtra("userName",userName);
                        startNewActivityIntent.putExtra("sectorName", sectorName);
                        ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                        activityadminStack.push("AdminAddNew", startNewActivityIntent);
                    }else
                    {
                        Toast.makeText(AdminPage.this, "The device has been added", Toast.LENGTH_SHORT).show();
                    }

                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    contents = null;
                    boolresu = true;
                }
            }


            if (contents != null && !boolresu) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPage.this);
                alertDialog.setTitle("Error");
                alertDialog.setMessage("QR code error");
                alertDialog.setPositiveButton("Scan Another", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                        AdminPage.this.startActivityForResult(new Intent(AdminPage.this, CaptureActivity.class), 0);
                        AdminPage.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        */
                        startActivityForResult(new Intent(AdminPage.this, CaptureActivity.class) ,0);
                        Intent startNewActivityIntent = new Intent(AdminPage.this, CaptureActivity.class);
                        ActivityStack activityStack = (ActivityStack) getParent();
                        activityStack.startActivityForResult(startNewActivityIntent, 0);

                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                alertDialog.show();

            }
        }
    }
    private boolean findDeviceAddress(short deviceAddress) {
        ArrayList<Device> check = DatabaseManager.getInstance().LoadDeviceList("devicelist");
        if (check!=null) {
            for (int i=0; i<check.size(); i++)
            {
                if (check.get(i).getDeviceAddress()== deviceAddress) return true;
            }
        }
        return false;
    }


    /*
    public void EmptySector()
    {
        final ListView sectorlist = (ListView) findViewById(R.id.sectorlist);
        sectorlist.setVisibility(View.INVISIBLE);
        final ArrayList<String> firstsector = new ArrayList<>();

        addsector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userName.equals("")) {
                    addnewdevice.setVisibility(View.INVISIBLE);
                    addNewUser.setVisibility(View.INVISIBLE);
                    uniquesectorname = true;
                    addnewsector.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(AdminPage.this, "Please select a user", Toast.LENGTH_SHORT).show();
                }
            }
        });


        savesector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = sectorname.getText().toString();
                if (!name.equals("")) {
                    for (Map.Entry<String, HashMap> entry : sector.entrySet()) {
                        HashMap<String, ArrayList> value = entry.getValue();
                        for (Map.Entry<String, ArrayList> entrys : value.entrySet()) {
                            if (entrys.getKey().equals(name)) {
                                sectorname.setText("");
                                uniquesectorname = false;
                                Toast.makeText(AdminPage.this, "Sector name alreay exsits", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (uniquesectorname == true) {
                        if (sectordetail==null) {
                            HashMap<String,ArrayList<Device>> sectordetail2 = new HashMap<String, ArrayList<Device>>();
                            sectordetail2.put(name, null);
                            sector.put(userName, sectordetail2);
                            DataManager.getInstance().setsector(sector);
                            firstsector.add(name);
                            goahead = true;
                        }
                        else
                        {
                            HashMap<String,ArrayList<Device>> sectordetail2 = sector.get(userName);
                            sectordetail2.put(name, null);
                            sector.remove(userName);
                            sector.put(userName, sectordetail2);
                            DataManager.getInstance().setsector(sector);
                            firstsector.add(name);
                            goahead = true;
                        }

                    }
                } else {
                    Toast.makeText(AdminPage.this, "Sector Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                sectorname.setText("");
                addnewsector.setVisibility(View.INVISIBLE);

            }
        });
        if (goahead = true) {
            sectoradapter = new MyCustomListAdapter(this, firstsector);
            sectorlist.setAdapter(sectoradapter);
            sectorlist.setVisibility(View.VISIBLE);
        }
    }

    public void EmptyDevice()
    {
        ListView deviceList = (ListView) findViewById(R.id.devicelist);
        deviceList.setVisibility(View.INVISIBLE);
        final ArrayList<String> firstdevice = new ArrayList<>();

        adddevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sectorname.equals("")) {
                    addnewsector.setVisibility(View.INVISIBLE);
                    addNewUser.setVisibility(View.INVISIBLE);
                    AdminPage.this.startActivityForResult(new Intent(AdminPage.this, CaptureActivity.class), 0);
                    AdminPage.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Toast.makeText(AdminPage.this, "Please select a sector", Toast.LENGTH_SHORT).show();
                }
            }
        });

        savedevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = devicename.getText().toString();
                if (!name.equals("")) {
                    if (!findDeviceName(name)) {
                        Gateway gateways = SysApplication.getInstance().getCurrGateway(AdminPage.this);
                        if (gateways != null) {
                            if (mDeviceList==null) {
                                mDevice.setDeviceName(name);
                                ArrayList<Device> deviceArrayList = DatabaseManager.getInstance().LoadDeviceList("devicelist");
                                if (!deviceArrayList.isEmpty()) {
                                } else {
                                    deviceArrayList = new ArrayList<Device>();
                                }
                                DatabaseManager.getInstance().addDevice(mDevice, null);
                                deviceArrayList.add(mDevice);
                                DatabaseManager.getInstance().WriteDeviceList(deviceArrayList, "devicelist");
                                ArrayList<Device> oldlist = new ArrayList<Device>();
                                oldlist.add(mDevice);
                                HashMap<String, ArrayList<Device>> sectordetail4 = sector.get(userName);
                                sectordetail4.put(sectorName, oldlist);
                                sector.put(userName, sectordetail4);
                                firstdevice.add(name);
                                DataManager.getInstance().setsector(sector);
                                goahead = true;
                            } else {
                                mDevice.setDeviceName(name);
                                ArrayList<Device> deviceArrayList = DatabaseManager.getInstance().LoadDeviceList("devicelist");
                                DatabaseManager.getInstance().addDevice(mDevice, null);
                                deviceArrayList.add(mDevice);
                                DatabaseManager.getInstance().WriteDeviceList(deviceArrayList, "devicelist");
                                ArrayList<Device> oldlist = new ArrayList<Device>();
                                oldlist.add(mDevice);
                                HashMap<String, ArrayList<Device>> sectordetail4 = sector.get(userName);
                                sectordetail4.put(sectorName, oldlist);
                                sector.put(userName, sectordetail4);
                                firstdevice.add(name);
                                DataManager.getInstance().setsector(sector);
                                goahead = true;
                            }
                        } else {
                            Toast.makeText(AdminPage.this, "Gateway error", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(AdminPage.this, "Device has already been added", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminPage.this, "Device Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                devicename.setText("");
                addnewdevice.setVisibility(View.INVISIBLE);

            }
        });
        if (goahead==true) {
            deviceadapter = new MyCustomListAdapterfordevice(this, firstdevice);
            deviceList.setAdapter(deviceadapter);
            deviceList.setVisibility(View.VISIBLE);
        }
    }

    public void EmptyUser()
    {
        final ArrayList<String> list = new ArrayList<>();
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                Bitmap bitmap = getScreenShot(rootView);
                DataManager.getInstance().setBitmap(bitmap);
                Intent startNewActivityIntent = new Intent(AdminPage.this, AdminAddNew.class);
                ActivityStack activityStack = (ActivityStack) getParent();
                activityStack.push("AdminAddNew", startNewActivityIntent);
            }
        });
        SAVE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String Accounts = MSG.getText().toString();    //value
                String Passwords = CODE.getText().toString();  //key
                BiMap<String, ArrayList> bimap;
                bimap = DataManager.getInstance().getaccount();
                ArrayList<String> accout = new ArrayList<String>();

                String[] arr = {"#59dbe0", "#f57f68", "#87d288", "#f8b552", "#39add1", "#3079ab", "#c25975", "#e15258",
                        "#f9845b", "#838cc7", "#7d669e", "#53bbb4", "#51b46d", "#e0ab18", "#637a91", "#f092b0", "#b7c0c7"};
                Random random = new Random();
                int select = random.nextInt(arr.length);
                String color = arr[select];

                if (Accounts.isEmpty() || Passwords.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Missing Accounts or Passwords", Toast.LENGTH_LONG).show();
                } else if (bimap.get(Passwords) != null) {
                    accout = bimap.get(Passwords);
                    String accoutname = accout.get(0);
                    Toast.makeText(getApplicationContext(), "Existant accout: " + accoutname, Toast.LENGTH_LONG).show();
                    MSG.setText("");
                    CODE.setText("");
                } else if (Passwords.length() != 4) {
                    Toast.makeText(getApplicationContext(), "The Password must be 4 digits", Toast.LENGTH_LONG).show();
                    CODE.setText("");
                } else {
                    accout.add(Accounts);
                    accout.add(color);
                    bimap.put(Passwords, accout);
                    list.add(Accounts);
                    goahead = true;
                    DataManager.getInstance().setaccount(bimap);
                    MSG.setText("");
                    CODE.setText("");
                    addNewUser.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "DATA saved", Toast.LENGTH_LONG).show();
                }

                addNewUser.setVisibility(View.INVISIBLE);
            }
        });
        if (goahead == true)
        {
            useradapter = new UserCustomListAdapter(this, list);
            ListView userlist = (ListView) findViewById(R.id.userlist);
            userlist.setAdapter(useradapter);
        }
    }
    */

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache(),0,60,1000,
                screenView.getDrawingCache().getHeight()-60);
        screenView.setDrawingCacheEnabled(false);

        return bitmap;
    }
}


