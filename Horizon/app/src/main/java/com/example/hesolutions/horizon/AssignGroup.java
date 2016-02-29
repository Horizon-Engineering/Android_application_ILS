package com.example.hesolutions.horizon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.DeviceList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignGroup extends Activity {

    MyCustomAdapter dataAdapter = null;
    ListView groupnamelistview,devicelistview;
    Button cancel, apply;
    ArrayList<String> names = new ArrayList<>();
    String namechoose = null;
    EditText Groupname;
    Boolean uniquename = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_group);

        cancel = (Button)findViewById(R.id.cancel);
        apply = (Button)findViewById(R.id.apply);
        Groupname = (EditText)findViewById(R.id.Groupname);
        displayListView();
//=======================================================Name list===================================

        BiMap<String,ArrayList> account = DataManager.getInstance().getaccount();
        for (Map.Entry<String, ArrayList> entry : account.entrySet()) {
            ArrayList value = entry.getValue();
            names.add((String)value.get(0));
        }
        final ArrayAdapter accoutAdapter = (new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1, names));
        groupnamelistview = (ListView)findViewById(R.id.groupnamelistview);
        groupnamelistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        groupnamelistview.setAdapter(accoutAdapter);
        groupnamelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                namechoose = names.get(position);
            }
        });

//======================================================================================
        apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uniquename = true;
                String groupname = Groupname.getText().toString();
                ArrayList<Device> selecteddevice = new ArrayList();

                ArrayList<Device> deviceList = dataAdapter.arrayList;
                for (int i = 0; i < deviceList.size(); i++) {
                    Device device = deviceList.get(i);
                    if (device.getChecked()) selecteddevice.add(device);
                }

                //===================check the group name unique
                HashMap<String, BiMap> sector = DataManager.getInstance().getsector();
                BiMap<String, ArrayList>sectordetail = HashBiMap.create();
                for (Map.Entry<String, BiMap> entry : sector.entrySet()) {
                    BiMap<String,ArrayList> value = entry.getValue();
                    for (Map.Entry<String, ArrayList> entrys : value.entrySet()) {
                        String key = entrys.getKey();
                        if (key.equals(groupname))
                        {
                            Toast.makeText(AssignGroup.this, "Group Name alreay exsited" + key, Toast.LENGTH_SHORT).show();
                            Groupname.setText("");
                            uniquename = false;
                        }
                    }
                }

                if (uniquename==true) {
                    //===========valid groupname =====no user has been created=====
                    if (!groupname.isEmpty() && (sector.get(namechoose) == null)) {

                        if (selecteddevice.isEmpty()) {
                            Toast.makeText(AssignGroup.this, "At least one device should be selected", Toast.LENGTH_SHORT).show();
                        } else {
                            if (namechoose == null) {
                                //-----------------User name is null
                                sectordetail.put(groupname, selecteddevice);
                                sector.put(null, sectordetail);
                                DataManager.getInstance().setsector(sector);
                                Intent intent1 = new Intent(v.getContext(), GroupActivity.class);
                                startActivity(intent1);
                            } else {
                                //-----------------User name
                                sectordetail.put(groupname, selecteddevice);
                                sector.put(namechoose, sectordetail);
                                DataManager.getInstance().setsector(sector);
                                Intent intent1 = new Intent(v.getContext(), GroupActivity.class);
                                startActivity(intent1);
                            }
                        }
                        //===========valid groupname =========user has been created=====
                    } else if (!groupname.isEmpty() && (sector.get(namechoose) != null)) {
                        if (selecteddevice.isEmpty()) {
                            Toast.makeText(AssignGroup.this, "At least one device should be selected", Toast.LENGTH_SHORT).show();
                        } else {
                            sectordetail = sector.get(namechoose);
                            sectordetail.put(groupname, selecteddevice);
                            sector.remove(namechoose); // delete the old data
                            sector.put(namechoose, sectordetail);   // put back the new data
                            DataManager.getInstance().setsector(sector);
                            Intent intent1 = new Intent(v.getContext(), GroupActivity.class);
                            startActivity(intent1);
                        }

                    } else {
                        Toast.makeText(AssignGroup.this, "Group Name is unvalid", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void displayListView(){
        ArrayList<Device> arrayList;
        DeviceList deviceList = DatabaseManager.getInstance().getDeviceListExceptKnobandsenor();
        arrayList = deviceList.getmDeviceList();
        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this, R.layout.devicelist, arrayList);
        devicelistview = (ListView)findViewById(R.id.devicelistview);
        // Assign adapter to ListView
        devicelistview.setAdapter(dataAdapter);
    }

    private class MyCustomAdapter extends ArrayAdapter<Device> {

        ArrayList<Device> arrayList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Device> arrayList) {
            super(context, textViewResourceId, arrayList);
            this.arrayList = arrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.devicelist, null);

            }
            Device device = arrayList.get(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            final CheckBox checked = (CheckBox) convertView.findViewById(R.id.checked);
            checked.setTag(device);
            name.setText(device.getDeviceName());
            checked.setText("");


            checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device device = (Device)v.getTag();
                    if (checked.isChecked())
                    {
                        device.setChecked(true);
                    }else
                    {device.setChecked(false);}
                }
            });


            return convertView;

        }

    }




}
