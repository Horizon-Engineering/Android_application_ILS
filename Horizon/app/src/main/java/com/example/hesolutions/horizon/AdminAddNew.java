package com.example.hesolutions.horizon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allin.activity.action.SysApplication;
import com.google.common.collect.BiMap;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdminAddNew extends Activity {
    EditText MSG,CODE,sectorname,devicename;
    Button SAVE, savedevice, savesector, cancel, Apply;
    RelativeLayout addNewUser, addnewsector, addnewdevice, assignuser;
    Boolean uniquesectorname = true;
    BiMap<String, HashMap> sector = DataManager.getInstance().getsector();
    HashMap<String, ArrayList<Device>>sectordetail;
    String userName = "";
    String UserName = "";
    String SectorName = "";
    String sectorName = "";
    ListView assignsector;
    MyCustomAdapter deviceAdapter =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new);
        SAVE = (Button)findViewById(R.id.SAVE);
        MSG = (EditText)findViewById(R.id.MSG);
        CODE = (EditText)findViewById(R.id.CODE);
        sectorname = (EditText)findViewById(R.id.sectorname);
        devicename = (EditText)findViewById(R.id.devicename);
        savedevice = (Button)findViewById(R.id.savedevice);
        savesector = (Button)findViewById(R.id.savesector);
        cancel = (Button)findViewById(R.id.cancel);
        addNewUser = (RelativeLayout)findViewById(R.id.addNewUser);
        addnewsector = (RelativeLayout)findViewById(R.id.addnewsector);
        addnewdevice = (RelativeLayout)findViewById(R.id.addnewdevice);
        assignuser = (RelativeLayout)findViewById(R.id.assignuser);
        assignsector = (ListView)findViewById(R.id.assignsector);
        Apply = (Button)findViewById(R.id.Apply);

        ImageView homescreenBgImage = (ImageView) findViewById(R.id.imageView);
        Bitmap cachedBitmap = DataManager.getInstance().getBitmap();
        if (cachedBitmap != null) {
            Bitmap blurredBitmap = BlurBuilder.blur(this, cachedBitmap);
            homescreenBgImage.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        }

        int usecase = getIntent().getIntExtra("Case", 0);
        if (usecase==1)
        {
            MSG.requestFocus();
            addNewUser.setVisibility(View.VISIBLE);
            addnewsector.setVisibility(View.GONE);
            addnewdevice.setVisibility(View.GONE);
            assignuser.setVisibility(View.GONE);
        }else if (usecase == 2 )
        {
            sectorname.requestFocus();
            addnewsector.setVisibility(View.VISIBLE);
            addNewUser.setVisibility(View.GONE);
            userName = getIntent().getStringExtra("userName");
            addnewdevice.setVisibility(View.GONE);
            assignuser.setVisibility(View.GONE);
        }else if (usecase ==3)
        {
            devicename.requestFocus();
            userName = getIntent().getStringExtra("userName");
            userName = getIntent().getStringExtra("sectorName");
            addnewsector.setVisibility(View.GONE);
            addNewUser.setVisibility(View.GONE);
            assignuser.setVisibility(View.GONE);
        }
        else if (usecase == 4)
        {
            assignuser.setVisibility(View.VISIBLE);
            UserName = getIntent().getStringExtra("UserName");
            SectorName = getIntent().getStringExtra("SectorName");
            addNewUser.setVisibility(View.GONE);
            addnewsector.setVisibility(View.GONE);
            addnewdevice.setVisibility(View.GONE);
        }

        SAVE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String Accounts = MSG.getText().toString();    //value
                String Passwords = CODE.getText().toString();  //key
                BiMap<String, ArrayList> bimap;
                bimap = DataManager.getInstance().getaccount();
                ArrayList<String> accout = new ArrayList<String>();

                String[] arr = {"#59dbe0", "#f57f68", "#87d288", "#f8b552", "#39add1", "#3079ab", "#c25975", "#e15258",
                        "#f9845b", "#838cc7", "#7d669e", "#53bbb4", "#51b46d", "#e0ab18", "#f092b0", "#b7c0c7"};
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
                    DataManager.getInstance().setaccount(bimap);
                    MSG.setText("");
                    CODE.setText("");
                    //addNewUser.setVisibility(View.INVISIBLE);
                    finish();
                    //userlist.add(Accounts);
                    //notifyDataSetChanged();
                    //Toast.makeText(getApplicationContext(), "DATA saved", Toast.LENGTH_LONG).show();
                }

                //addNewUser.setVisibility(View.INVISIBLE);
            }
        });


        savesector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, ArrayList> sectordetail1 = new HashMap<String, ArrayList>();
                String name = sectorname.getText().toString();
                if (!name.equals("")) {
                    for (Map.Entry<String, HashMap> entry : sector.entrySet()) {
                        HashMap<String, ArrayList> value = entry.getValue();
                        for (Map.Entry<String, ArrayList> entrys : value.entrySet()) {
                            if (entrys.getKey().equals(name)) {
                                sectorname.setText("");
                                uniquesectorname = false;
                                Toast.makeText(AdminAddNew.this, "Sector name alreay exsits", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    sectordetail= sector.get(userName);
                    if (uniquesectorname == true) {
                        if (sectordetail!=null) {
                            if (sectordetail.isEmpty()) {
                                HashMap<String, ArrayList<Device>> sectordetail2 = new HashMap<String, ArrayList<Device>>();
                                sectordetail2.put(name, null);
                                sector.put(userName, sectordetail2);
                                DataManager.getInstance().setsector(sector);
                                finish();
                                //sectolist.add(name);
                                //notifyDataSetChanged();
                            } else {
                                sectordetail1 = sector.get(userName);
                                sectordetail1.put(name, null);
                                sector.remove(userName);
                                sector.put(userName, sectordetail1);
                                DataManager.getInstance().setsector(sector);
                                finish();
                                //sectolist.add(name);
                                //notifyDataSetChanged();
                            }
                        }
                    }
                } else {
                    Toast.makeText(AdminAddNew.this, "Sector Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                sectorname.setText("");
                addnewsector.setVisibility(View.INVISIBLE);

            }
        });


        savedevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = devicename.getText().toString();
                Device mDevice = new Device();
                ArrayList<Device> mDeviceList = sectordetail.get(sectorName);
                if (!name.equals("")) {
                    if (findDeviceName(name)) {
                        Toast.makeText(AdminAddNew.this, "Devices already been added", Toast.LENGTH_SHORT).show();
                    } else{
                        Gateway gateways = SysApplication.getInstance().getCurrGateway(AdminAddNew.this);
                        if (gateways != null) {
                            if (mDeviceList==null) {
                                mDevice.setDeviceName(name);
                                ArrayList<Device> deviceArrayList = DatabaseManager.getInstance().LoadDeviceList("devicelist");
                                DatabaseManager.getInstance().addDevice(mDevice, null);
                                deviceArrayList.add(mDevice);
                                DatabaseManager.getInstance().WriteDeviceList(deviceArrayList, "devicelist");
                                mDeviceList = new ArrayList<Device>();
                                mDeviceList.add(mDevice);
                                sectordetail.put(sectorName, mDeviceList);
                                sector.put(userName, sectordetail);
                                DataManager.getInstance().setsector(sector);
                                //devicelist.add(name);
                                //notifyDataSetChanged();
                            } else {
                                mDevice.setDeviceName(name);
                                ArrayList<Device> deviceArrayList = DatabaseManager.getInstance().LoadDeviceList("devicelist");
                                DatabaseManager.getInstance().addDevice(mDevice, null);
                                deviceArrayList.add(mDevice);
                                DatabaseManager.getInstance().WriteDeviceList(deviceArrayList, "devicelist");
                                mDeviceList.add(mDevice);
                                sectordetail.put(sectorName, mDeviceList);
                                sector.put(userName, sectordetail);
                                DataManager.getInstance().setsector(sector);
                                //devicelist.add(name);
                                //notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(AdminAddNew.this, "Gateway error", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(AdminAddNew.this, "Device Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                devicename.setText("");
                addnewdevice.setVisibility(View.INVISIBLE);
            }
        });


        BiMap<String, ArrayList> nameset = DataManager.getInstance().getaccount();
        ArrayList<Group> names = new ArrayList<>();
        if (!nameset.isEmpty()) {
            names.clear();
            for (Map.Entry<String, ArrayList> entry : nameset.entrySet()) {
                String name = (String) entry.getValue().get(0);
                if (!name.equals(UserName)){
                    Group group = new Group(name, false);
                    names.add(group);
                }
            }
        }

        deviceAdapter = new MyCustomAdapter(this, R.layout.devicelist, names);
        assignsector.setAdapter(deviceAdapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Group> choosedevice = new ArrayList<Group>();
                if (choosedevice.isEmpty()) {
                    ArrayList<Group> choosegrouplist = deviceAdapter.arrayList;
                    for (int i = 0; i < choosegrouplist.size(); i++) {
                        Group group = choosegrouplist.get(i);
                        if (group.getSelected() == true ) {
                          choosedevice.add(group);
                        }
                    }
                }
                sectordetail = sector.get(UserName);
                ArrayList<Device> list = sectordetail.get(SectorName);
                HashMap<String, ArrayList<Device>> newassignsector = new HashMap<String, ArrayList<Device>>();
                newassignsector.put(SectorName,list);
                if (choosedevice.isEmpty()||choosedevice==null) {
                    Toast.makeText(AdminAddNew.this, "At least one user should be selected", Toast.LENGTH_SHORT).show();
                }else {
                    for (int k = 0; k < choosedevice.size(); k++) {
                        sectordetail = sector.get(choosedevice.get(k).getName());
                        if (sectordetail!=null && !sectordetail.isEmpty())
                        {
                            /*
                            if (sectordetail.get(SectorName)!=null||!sectordetail.get(SectorName).isEmpty())
                            {

                            }else
                            {
                            */
                                sectordetail.put(SectorName,list);
                                sector.put(choosedevice.get(k).getName(), sectordetail);
                                DataManager.getInstance().setsector(sector);
                                finish();
                            //}
                        } else {
                            sector.put(choosedevice.get(k).getName(), newassignsector);
                            DataManager.getInstance().setsector(sector);
                            finish();
                        }
                    }
                    finish();
                }
            }
        });
    }
    private boolean findDeviceName(String deviceName) {
        ArrayList<Device> check = DatabaseManager.getInstance().LoadDeviceList("devicelist");
        if (check!=null) {
            for (int i=0; i<check.size(); i++)
            {
                if (check.get(i).getDeviceName().equals(deviceName)) return true;
            }
        }
        return false;
    }


    private class MyCustomAdapter extends ArrayAdapter<Group> {
        ArrayList<Group> arrayList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Group> arrayList) {
            super(context, textViewResourceId, arrayList);
            this.arrayList = arrayList;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.devicelist, null);

            }

            Group group = arrayList.get(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            final EnhancedSwitch checked = (EnhancedSwitch) convertView.findViewById(R.id.checked);
            name.setText(group.getName());
            checked.setCheckedProgrammatically(group.getSelected());
            checked.setTag(group);

            checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Group group = (Group) buttonView.getTag();
                    if (checked.isChecked() == true) {
                        group.setSelected(true);
                    } else {
                        group.setSelected(false);
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

    }

    public class Group
    {
        String name;
        boolean ischecked;

        public Group(String name, boolean ischecked)
        {
            this.name = name;
            this.ischecked = ischecked;
        }

        public boolean getSelected()
        {
            return ischecked;
        }
        public void setSelected(boolean ischecked)
        {
            this.ischecked = ischecked;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }

}
