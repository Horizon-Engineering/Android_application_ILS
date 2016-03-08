package com.example.hesolutions.horizon;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdminAddNew extends Activity {
    EditText MSG,CODE,sectorname,devicename;
    Button SAVE, savedevice, savesector, cancel, Apply;
    RelativeLayout addNewUser, addnewsector, addnewdevice, assignuser;
    Boolean uniquesectorname = true;
    HashMap<String, HashMap> sector = DataManager.getInstance().getsector();
    HashMap<String, ArrayList<Device>>sectordetail;
    String userName = "";
    String UserName = "";
    String SectorName = "";
    String sectorName = "";
    String result = "";
    Device mDevice = new Device();
    ListView assignsector;
    MyCustomAdapter deviceAdapter =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new);
        SAVE = (Button) findViewById(R.id.SAVE);
        MSG = (EditText) findViewById(R.id.MSG);
        CODE = (EditText) findViewById(R.id.CODE);
        sectorname = (EditText) findViewById(R.id.sectorname);
        devicename = (EditText) findViewById(R.id.devicename);
        savedevice = (Button) findViewById(R.id.savedevice);
        savesector = (Button) findViewById(R.id.savesector);
        cancel = (Button) findViewById(R.id.cancel);
        addNewUser = (RelativeLayout) findViewById(R.id.addNewUser);
        addnewsector = (RelativeLayout) findViewById(R.id.addnewsector);
        addnewdevice = (RelativeLayout) findViewById(R.id.addnewdevice);
        assignuser = (RelativeLayout) findViewById(R.id.assignuser);
        assignsector = (ListView) findViewById(R.id.assignsector);
        Apply = (Button) findViewById(R.id.Apply);

        ImageView homescreenBgImage = (ImageView) findViewById(R.id.imageView);
        Bitmap cachedBitmap = DataManager.getInstance().getBitmap();
        if (cachedBitmap != null) {
            Bitmap blurredBitmap = BlurBuilder.blur(this, cachedBitmap);
            homescreenBgImage.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        }

        int usecase = getIntent().getIntExtra("Case", 0);
        if (usecase == 1) {
            MSG.requestFocus();
            addNewUser.setVisibility(View.VISIBLE);
            addnewsector.setVisibility(View.GONE);
            addnewdevice.setVisibility(View.GONE);
            assignuser.setVisibility(View.GONE);
        } else if (usecase == 2) {
            sectorname.requestFocus();
            addnewsector.setVisibility(View.VISIBLE);
            addNewUser.setVisibility(View.GONE);
            userName = getIntent().getStringExtra("userName");
            addnewdevice.setVisibility(View.GONE);
            assignuser.setVisibility(View.GONE);
        }  else if (usecase == 4) {
            assignuser.setVisibility(View.VISIBLE);
            UserName = getIntent().getStringExtra("UserName");
            SectorName = getIntent().getStringExtra("SectorName");
            addNewUser.setVisibility(View.GONE);
            addnewsector.setVisibility(View.GONE);
            addnewdevice.setVisibility(View.GONE);
        } else if (usecase == 5) {
            assignuser.setVisibility(View.GONE);
            result = getIntent().getStringExtra("result");
            userName = getIntent().getStringExtra("userName");
            sectorName = getIntent().getStringExtra("sectorName");
            boolean boolresu = false;
            if (result != null && result.length() == 7) {
                int devtype;
                int subdevtype = 0;
                if (Integer.parseInt(result.substring(1, 2)) == 2 && Integer.parseInt(result.substring(0, 1)) == 5) {
                    subdevtype = 2;
                    devtype = 5;
                } else if (Integer.parseInt(result.substring(1, 2)) != 1) {
                    devtype = Integer.parseInt(result.substring(0, 2));
                } else {
                    devtype = Integer.parseInt(result.substring(0, 1));
                }
                String deviceaddress = result.substring(2, result.length());
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
                    if (!findDeviceAddress(mDevice.getDeviceAddress())) {
                        assignuser.setVisibility(View.GONE);
                        addNewUser.setVisibility(View.GONE);
                        addnewsector.setVisibility(View.GONE);
                        addnewdevice.setVisibility(View.VISIBLE);
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminAddNew.this.getParent());
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("This device has been added already");
                        alertDialog.setPositiveButton("Scan Another", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent startNewActivityIntent = new Intent(AdminAddNew.this, CaptureActivity.class);
                                ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                                startNewActivityIntent.putExtra("userName", userName);
                                startNewActivityIntent.putExtra("sectorName", sectorName);
                                activityadminStack.push("Scanner", startNewActivityIntent);
                            }
                        });
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent startNewActivityIntent = new Intent(AdminAddNew.this, AdminPage.class);
                                ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                                activityadminStack.push("Adr", startNewActivityIntent);
                            }
                        });
                        alertDialog.show();
                    }

                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    result = null;
                    boolresu = true;
                }
            }


            if (result != null && !boolresu) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminAddNew.this.getParent());
                alertDialog.setTitle("Error");
                alertDialog.setMessage("QR code error");
                alertDialog.setPositiveButton("Scan Another", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent startNewActivityIntent = new Intent(AdminAddNew.this, CaptureActivity.class);
                        ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                        startNewActivityIntent.putExtra("userName", userName);
                        startNewActivityIntent.putExtra("sectorName", sectorName);
                        activityadminStack.push("Scanner", startNewActivityIntent);

                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startNewActivityIntent = new Intent(AdminAddNew.this, AdminPage.class);
                        ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();

                        activityadminStack.push("Admin", startNewActivityIntent);
                    }
                });
                alertDialog.show();
            }
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
                boolean duplicated = true;
                for (Map.Entry<String, ArrayList> entry : bimap.entrySet()) {
                    ArrayList<String>  account = entry.getValue();
                    if (account.get(0).equals(Accounts)) duplicated = false;
                }

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
                } else if (duplicated==false)
                {
                    Toast.makeText(getApplicationContext(), "Existant accout: " + Accounts, Toast.LENGTH_LONG).show();
                    MSG.setText("");
                    CODE.setText("");
                }else if (Passwords.equals("0000"))
                {
                    Toast.makeText(AdminAddNew.this, "The password cannot be the same as for the Admin", Toast.LENGTH_SHORT).show();
                    MSG.setText("");
                    CODE.setText("");
                } else {
                    accout.add(Accounts);
                    accout.add(color);
                    bimap.put(Passwords, accout);
                    DataManager.getInstance().setaccount(bimap);
                    MSG.setText("");
                    CODE.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    finish();
                    Toast.makeText(getApplicationContext(), "Data Saved successfully!", Toast.LENGTH_LONG).show();
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
                        if (value!=null) {
                            for (Map.Entry<String, ArrayList> entrys : value.entrySet()) {
                                if (entrys.getKey().equals(name)) {
                                    sectorname.setText("");
                                    uniquesectorname = false;
                                    Toast.makeText(AdminAddNew.this, "Existant sector: " + name, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    sectordetail = sector.get(userName);
                    if (uniquesectorname == true) {
                        if (sectordetail != null) {
                            if (sectordetail.isEmpty()) {
                                HashMap<String, ArrayList<Device>> sectordetail2 = new HashMap<String, ArrayList<Device>>();
                                sectordetail2.put(name, null);
                                sector.put(userName, sectordetail2);
                                DataManager.getInstance().setsector(sector);
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                finish();
                                Toast.makeText(getApplicationContext(), "Data Saved successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                sectordetail1 = sector.get(userName);
                                sectordetail1.put(name, null);
                                sector.remove(userName);
                                sector.put(userName, sectordetail1);
                                DataManager.getInstance().setsector(sector);
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                finish();
                                Toast.makeText(getApplicationContext(), "Data Saved successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            HashMap<String, ArrayList<Device>> sectordetail2 = new HashMap<String, ArrayList<Device>>();
                            sectordetail2.put(name, null);
                            sector.put(userName, sectordetail2);
                            DataManager.getInstance().setsector(sector);
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            finish();
                            Toast.makeText(getApplicationContext(), "Data Saved successfully!", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(AdminAddNew.this, "Sector Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        savedevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = devicename.getText().toString();
                sectordetail = sector.get(userName);
                ArrayList<Device> mDeviceList = sectordetail.get(sectorName);
                if (!name.equals("")) {
                    if (findDeviceName(name)) {
                        Toast.makeText(AdminAddNew.this, "Device has already been added", Toast.LENGTH_SHORT).show();
                    } else {
                        Gateway gateways = SysApplication.getInstance().getCurrGateway(AdminAddNew.this);
                        if (gateways != null) {
                            if (mDeviceList == null) {
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
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                Toast.makeText(getApplicationContext(), "Data Saved successfully!", Toast.LENGTH_LONG).show();
                                Intent startNewActivityIntent = new Intent(AdminAddNew.this, AdminPage.class);
                                ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                                startNewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activityadminStack.push("AdminPage", startNewActivityIntent);
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
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                Toast.makeText(getApplicationContext(), "Data Saved successfully!", Toast.LENGTH_LONG).show();
                                Intent startNewActivityIntent = new Intent(AdminAddNew.this, AdminPage.class);
                                ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                                startNewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activityadminStack.push("AdminPage", startNewActivityIntent);
                            }
                        } else {
                            Restart();
                        }
                    }

                } else {
                    Toast.makeText(AdminAddNew.this, "Device Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        BiMap<String, ArrayList> nameset = DataManager.getInstance().getaccount();
        ArrayList<Group> names = new ArrayList<>();
        if (!nameset.isEmpty()) {
            names.clear();
            for (Map.Entry<String, ArrayList> entry : nameset.entrySet()) {
                String name = (String) entry.getValue().get(0);
                if (!name.equals(UserName)) {
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
                Intent startNewActivityIntent = new Intent(AdminAddNew.this, AdminPage.class);
                ActivityAdminStack activityadminStack = (ActivityAdminStack) getParent();
                startNewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activityadminStack.push("Admin", startNewActivityIntent);
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
                        if (group.getSelected() == true) {
                            choosedevice.add(group);
                        }
                    }
                }
                sectordetail = sector.get(UserName);
                final ArrayList<Device> list = sectordetail.get(SectorName);
                HashMap<String, ArrayList<Device>> newassignsector = new HashMap<String, ArrayList<Device>>();
                newassignsector.put(SectorName, list);
                if (choosedevice.isEmpty() || choosedevice == null) {
                    Toast.makeText(AdminAddNew.this, "At least one user should be selected", Toast.LENGTH_SHORT).show();
                } else {
                    for (int k = 0; k < choosedevice.size(); k++) {
                        sectordetail = sector.get(choosedevice.get(k).getName());
                        final String sectorname = choosedevice.get(k).getName();
                        if (sectordetail != null && !sectordetail.isEmpty()) {
                            if (sectordetail.get(SectorName)!=null)
                            {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminAddNew.this.getParent());
                                alertDialog.setTitle("Warning");
                                alertDialog.setMessage("Do you want to overwrite the sector?");
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        sectordetail.put(SectorName, list);
                                        sector.put(sectorname, sectordetail);
                                        DataManager.getInstance().setsector(sector);
                                    }
                                });
                                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();
                                break;
                            }else {
                                sectordetail.put(SectorName, list);
                                sector.put(sectorname, sectordetail);
                                DataManager.getInstance().setsector(sector);
                            }
                        }else
                        {
                            sector.put(choosedevice.get(k).getName(), newassignsector);
                            DataManager.getInstance().setsector(sector);
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
    public void Restart(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminAddNew.this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Gateway Error, please connect the wifi and press OK");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        runOnUiThread(new Runnable() {
            public void run() {
                alertDialog.show();
            }
        });

    }

}
