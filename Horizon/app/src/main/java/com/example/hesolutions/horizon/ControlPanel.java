package com.example.hesolutions.horizon;

import android.content.Context;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.mylibrary.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ControlPanel extends Activity {

    BiMap<String, BiMap> sector = DataManager.getInstance().getsector();
    String username =DataManager.getInstance().getUsername();
    BiMap<String, ArrayList>sectordetail = sector.get(username);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        CallSector();

    }
    public void clickEvent(View v)
    {
//=====================case:sector - device
        final Button gobacktosector = (Button)findViewById(R.id.gobacktosector);
        final Switch Onoffswitch = (Switch)findViewById(R.id.Onoffswitch);
        String sectorname = ((TextView)v).getText().toString();
        if  (!sectorname.equals(" "))
        {
            ArrayList<Device> devicelist = sectordetail.get((sectorname));
            ArrayList<String> devicename = new ArrayList<>();
            for (int i = 0; i<devicelist.size(); i++)
            {
                devicename.add(devicelist.get(i).getDeviceName());
            }

            MyCustomListAdapterfordevice adapter = new MyCustomListAdapterfordevice(this, devicename);
            ListView sectorListView = (ListView) findViewById(R.id.sectorListViewId);
            gobacktosector.setVisibility(View.VISIBLE);
            sectorListView.setAdapter(adapter);

        }
        gobacktosector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallSector();
                gobacktosector.setVisibility(View.GONE);
                Onoffswitch.setVisibility(View.INVISIBLE);
            }
        });
//===================case:device - switch
    }
    public class MyCustomListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> zoneList;

        public MyCustomListAdapter(Activity context, ArrayList<String> zoneList) {
            super(context, R.layout.zonelist_row, zoneList);
            this.context = context;
            this.zoneList = zoneList;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.zonelist_row, null);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
            txtTitle.setText(zoneList.get(position));
            return rowView;
        }

    }
    public class MyCustomListAdapterfordevice extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> zoneList;

        public MyCustomListAdapterfordevice(Activity context, ArrayList<String> zoneList) {
            super(context, R.layout.zonelist_row, zoneList);
            this.context = context;
            this.zoneList = zoneList;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.deviceswitch, null);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
            txtTitle.setText(zoneList.get(position));
            return rowView;
        }

    }

    public void openSwitch(View v)
    {
        String devicename = ((TextView)v).getText().toString();
        ArrayList<Device> loading = DatabaseManager.getInstance().getSectorDeviceInfor(devicename).getmDeviceList();

        final Device thedevice = loading.get(0);
        final Switch Onoffswitch = (Switch)findViewById(R.id.Onoffswitch);

        Onoffswitch.setVisibility(View.VISIBLE);
        if (DatabaseManager.getInstance().getlightingofDevice(thedevice)[1] == 0)
        {
            Onoffswitch.setChecked(false);
        }else
        {
            Onoffswitch.setChecked(true);
        }

        Onoffswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                            for (int j = 0; j < devicelist.size(); j++) {
                                if (devicelist.get(j).getDeviceIndex() == thedevice.getDeviceIndex()) {
                                    devicelist.remove(j);
                                }
                            }
                            if (devicelist.isEmpty()) events.remove(i);
                        }
                    }
                }
                DataManager.getInstance().setevents(events);
                if (Onoffswitch.isChecked() == true) {
                    byte[] data;
                    data = new byte[]{(byte) 17, (byte) 100, thedevice.getCurrentParams()[2], (byte) 0, (byte) 0};
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                    thedevice.getDeviceAddress(), (short) 0, data), thedevice.getGatewayMacAddr(), thedevice.getGatewayPassword(),
                            thedevice.getGatewaySSID(), ControlPanel.this));
                    thedevice.setCurrentParams(data);
                    DatabaseManager.getInstance().updateDevice(thedevice);
                } else {
                    byte[] data;
                    data = new byte[]{(byte) 17, (byte) 0, thedevice.getCurrentParams()[2], (byte) 0, (byte) 0};
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                    thedevice.getDeviceAddress(), (short) 0, data), thedevice.getGatewayMacAddr(), thedevice.getGatewayPassword(),
                            thedevice.getGatewaySSID(), ControlPanel.this));
                    thedevice.setCurrentParams(data);
                    DatabaseManager.getInstance().updateDevice(thedevice);
                }

            }
        });

    }

    public void CallSector()
    {

        ArrayList<String> sectorArray = new ArrayList<>();
        if (sector.get(username)==null) {}
        else
        {

            for (Map.Entry<String, ArrayList> entry : sectordetail.entrySet()) {
                sectorArray.add(entry.getKey());
            }

            if (sectorArray.isEmpty()||sectorArray.size()==0){}
            else{

                MyCustomListAdapter adapter = new MyCustomListAdapter(this, sectorArray);
                ListView sectorListView = (ListView) findViewById(R.id.sectorListViewId);
                sectorListView.setAdapter(adapter);

            }
        }
    }

}
