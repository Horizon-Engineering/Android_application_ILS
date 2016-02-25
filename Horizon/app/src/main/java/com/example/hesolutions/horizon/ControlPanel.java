package com.example.hesolutions.horizon;

import android.content.Context;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.DeviceList;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.mylibrary.WeekView;
import com.mylibrary.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ControlPanel extends Activity {

    BiMap<String, HashMap> sector = DataManager.getInstance().getsector();
    String username =DataManager.getInstance().getUsername();
    HashMap<String, ArrayList> sectordetail = sector.get(username);
    ArrayList<String> sectorArray = new ArrayList<>();
    EnhancedSeekBar seekBar;
    String str1;
    String str2;
    byte intensity;
    ExpandListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        seekBar = (EnhancedSeekBar)findViewById(R.id.seekBar);

        if (sector.get(username)==null) {}
        else
        {

            for (Map.Entry<String, ArrayList> entry : sectordetail.entrySet()) {
                sectorArray.add(entry.getKey());
            }

            if (sectorArray.isEmpty()||sectorArray.size()==0){}
            else{
                adapter = new ExpandListAdapter(this, sectorArray);
                ExpandableListView sectorListView = (ExpandableListView) findViewById(R.id.sectorListViewId);
                sectorListView.setAdapter(adapter);

            }
        }

    }


    public class ExpandListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private ArrayList<String> groups;

        public ExpandListAdapter(Context context, ArrayList<String> groups) {
            this.context = context;
            this.groups = groups;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            ArrayList<Device> devicelist = sectordetail.get((groups.get(groupPosition)));
            ArrayList<String> devicename = new ArrayList<>();
            for (int i = 0; i < devicelist.size(); i++) {
                devicename.add(devicelist.get(i).getDeviceName());
            }

            return devicename.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String devicename = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.deviceswitch, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.textView);
            final EnhancedSwitch switchid = (EnhancedSwitch)convertView.findViewById(R.id.switchid);
            tv.setText(devicename);

            final Device device = DatabaseManager.getInstance().getDeviceInforName(devicename);

            /*
            final short address = device.getDeviceAddress();
            final ArrayList<Device> thedevice = DatabaseManager.getInstance().getSectorDeviceInforadd(address).getmDeviceList();

            for (int i = 0; i < thedevice.size(); i++) {
                if (DatabaseManager.getInstance().getlightingofDevice(thedevice.get(i))[1] != 0) {
                    System.out.println(thedevice.get(i).getDeviceName() + "**********************1");
                    switchid.setCheckedProgrammatically(true);
                    intensity = DatabaseManager.getInstance().getlightingofDevice(thedevice.get(i))[1];
                    break;
                } else {
                    System.out.println(thedevice.get(i).getDeviceName() + "**********************2");
                    switchid.setCheckedProgrammatically(false);
                }
            }
            */
            if (DatabaseManager.getInstance().getlightingofDevice(device)[1]!=0)
            {
                switchid.setCheckedProgrammatically(true);
            } else
            {
                switchid.setCheckedProgrammatically(false);
            }

            tv.setTag(device);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device deviceA = (Device) v.getTag();
                    intensity = DatabaseManager.getInstance().getlightingofDevice(deviceA)[1];
                    DataManager.getInstance().setthedevice(deviceA);
                    if (switchid.isChecked() == true) {
                        seekBar.setProgressProgrammatically(intensity);
                    } else {
                        seekBar.setProgressProgrammatically(0);
                    }
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Calendar calendar = Calendar.getInstance();
                    List<WeekViewEvent> events = DataManager.getInstance().getevents();
                    Device devicea = DataManager.getInstance().getthedevice();
                    if (events.size() != 0) {
                        for (int i = 0; i < events.size(); i++) {
                            WeekViewEvent event = events.get(i);
                            Calendar starttime = event.getStartTime();
                            Calendar finishtime = event.getEndTime();
                            if (calendar.after(starttime) && calendar.before(finishtime)) {
                                ArrayList<Device> devicelist = event.getdeviceList();
                                if (devicelist != null) {
                                    str2 = devicea.getDeviceName();
                                    Iterator<Device> firstIt = devicelist.iterator();
                                    while (firstIt.hasNext()) {
                                        str1 = firstIt.next().getDeviceName();
                                        if (str1.equals(str2)) {
                                            firstIt.remove();
                                        }
                                        if (devicelist.isEmpty()) {
                                            events.remove(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    DataManager.getInstance().setevents(events);

                    byte[] SetParams = new byte[5];
                    if (fromUser) {
                        if (progress == 0) {
                            SetParams[0] = (byte) 17;
                            SetParams[1] = (byte) 0;
                            SetParams[2] = (byte) 0;
                            SetParams[3] = (byte) 0;
                            SetParams[4] = (byte) 0;
                        } else {
                            SetParams[0] = (byte) 17;
                            SetParams[1] = (byte) progress;
                            SetParams[2] = (byte) 0;
                            SetParams[3] = (byte) 0;
                            SetParams[4] = (byte) 0;
                        }
                    }
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                        devicea.getDeviceAddress(), (short) 0, SetParams), devicea.getGatewayMacAddr(), devicea.getGatewayPassword(),
                                devicea.getGatewaySSID(), ControlPanel.this));

                    devicea.setCurrentParams(SetParams);
                    DatabaseManager.getInstance().updateDevice(devicea);
                    notifyDataSetChanged();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            switchid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Calendar calendar = Calendar.getInstance();
                    List<WeekViewEvent> events = DataManager.getInstance().getevents();
                    if (events.size() != 0) {
                        for (int i = 0; i < events.size(); i++) {
                            WeekViewEvent event = events.get(i);
                            Calendar starttime = event.getStartTime();
                            Calendar finishtime = event.getEndTime();
                            if (calendar.after(starttime) && calendar.before(finishtime)) {
                                ArrayList<Device> devicelist = event.getdeviceList();
                                if (devicelist != null) {
                                    str2 = device.getDeviceName();
                                    Iterator<Device> firstIt = devicelist.iterator();
                                    while (firstIt.hasNext()) {
                                        str1 = firstIt.next().getDeviceName();
                                        if (str1.equals(str2)) {
                                            firstIt.remove();
                                        }
                                        if (devicelist.isEmpty()) {
                                            events.remove(i);
                                        }
                                    }
                                }

                            }
                        }
                    }
                    DataManager.getInstance().setevents(events);

                    if (switchid.isChecked() == true) {
                        byte[] data;
                        data = new byte[]{(byte) 17, (byte) 100, device.getCurrentParams()[2], (byte) 0, (byte) 0};
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                        device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                device.getGatewaySSID(), ControlPanel.this));
                        device.setCurrentParams(data);
                        DatabaseManager.getInstance().updateDevice(device);
                    } else {

                        byte[] data;
                        data = new byte[]{(byte) 17, (byte) 0, device.getCurrentParams()[2], (byte) 0, (byte) 0};
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                        device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                device.getGatewaySSID(), ControlPanel.this));
                        /*
                        boolean here = true;
                        ArrayList<Device> sameDevice = DatabaseManager.getInstance().getSameAddressDevice(device.getDeviceAddress()).getmDeviceList();
                        for (int i = 0; i<sameDevice.size();i++)
                        {
                            Device devicew = sameDevice.get(i);
                            if (DatabaseManager.getInstance().getlightingofDevice(devicew)[1]!=0 &&
                                    !devicew.getDeviceName().equals(device.getDeviceName()))
                            {
                                here = false;
                            }

                        }
                        if (here==true)
                        {
                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                            device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                    device.getGatewaySSID(), ControlPanel.this));
                        }
                        */
                        device.setCurrentParams(data);
                        DatabaseManager.getInstance().updateDevice(device);
                    }
                    notifyDataSetChanged();

                }
            });

            return convertView;
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            ArrayList<Device> devicelist = sectordetail.get(groups.get(groupPosition));
            if (devicelist!=null) {return devicelist.size();}
            else{return 0;}
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition,final boolean isExpanded,
                                 View convertView, final ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.zonelist_row, null);
            }

            TextView txtTitle = (TextView) convertView.findViewById(R.id.textView);
            final EnhancedSwitch switchid = (EnhancedSwitch)convertView.findViewById(R.id.switchid);
            final String sectorname = groups.get(groupPosition);
            txtTitle.setText(sectorname);
            final ArrayList<Device> devicelist = sectordetail.get(sectorname);

            if (devicelist!=null) {
                for (int i = 0; i < devicelist.size(); i++) {
                    Device device = devicelist.get(i);
                    if (!devicelist.isEmpty()) {
                        if (DatabaseManager.getInstance().getlightingofDevice(device)[1] != 0) {
                            switchid.setCheckedProgrammatically(true);
                            break;
                        } else {
                            switchid.setCheckedProgrammatically(false);
                        }
                    }
                }
            }

            txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                    else ((ExpandableListView) parent).expandGroup(groupPosition, true);
                }
            });

            switchid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Calendar calendar = Calendar.getInstance();
                    List<WeekViewEvent> events = DataManager.getInstance().getevents();
                    if (events.size() != 0) {
                        for (int i = 0; i < events.size(); i++) {
                            WeekViewEvent event = events.get(i);
                            Calendar starttime = event.getStartTime();
                            Calendar finishtime = event.getEndTime();
                            if (calendar.after(starttime) && calendar.before(finishtime)) {
                                ArrayList<Device> devicelistevent = event.getdeviceList();

                                Iterator<Device> firstIt = devicelistevent.iterator();
                                if (devicelist != null) {
                                    while (firstIt.hasNext()) {
                                        str1 = firstIt.next().getDeviceName();
                                        // recreate iterator for second list
                                        Iterator<Device> secondIt = devicelist.iterator();
                                        while (secondIt.hasNext()) {
                                            str2 = secondIt.next().getDeviceName();
                                            if (str1.equals(str2)) {
                                                firstIt.remove();
                                            }
                                            if (devicelistevent.isEmpty()) {
                                                events.remove(i);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    DataManager.getInstance().setevents(events);

                    if (!sectorname.equals(" ")) {
                        if (devicelist != null) {
                            if (switchid.isChecked() == true) {
                                for (int i = 0; i < devicelist.size(); i++) {
                                    Device thedevice = devicelist.get(i);
                                    byte[] data;
                                    data = new byte[]{(byte) 17, (byte) 100, thedevice.getCurrentParams()[2], (byte) 0, (byte) 0};
                                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                                    thedevice.getDeviceAddress(), (short) 0, data), thedevice.getGatewayMacAddr(), thedevice.getGatewayPassword(),
                                            thedevice.getGatewaySSID(), ControlPanel.this));
                                    thedevice.setCurrentParams(data);
                                    DatabaseManager.getInstance().updateDevice(thedevice);
                                }
                            } else {
                                for (int i = 0; i < devicelist.size(); i++) {
                                    Device thedevice = devicelist.get(i);
                                    byte[] data;
                                    data = new byte[]{(byte) 17, (byte) 0, thedevice.getCurrentParams()[2], (byte) 0, (byte) 0};
                                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                                    thedevice.getDeviceAddress(), (short) 0, data), thedevice.getGatewayMacAddr(), thedevice.getGatewayPassword(),
                                            thedevice.getGatewaySSID(), ControlPanel.this));
                                    thedevice.setCurrentParams(data);
                                    DatabaseManager.getInstance().updateDevice(thedevice);
                                }
                            }
                        }
                    }
                    notifyDataSetChanged();

                }
            });

            return convertView;

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

}
