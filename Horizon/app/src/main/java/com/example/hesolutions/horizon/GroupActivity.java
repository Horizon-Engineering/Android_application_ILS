package com.example.hesolutions.horizon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupActivity extends Activity {

    Button backButton, newGroup;
    TextView textView12;
    String username;
    ArrayList<String>groupname, devicename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        backButton = (Button)findViewById(R.id.backbutton);
        newGroup = (Button)findViewById(R.id.newGroup);
        textView12 = (TextView)findViewById(R.id.textView12);

        HashMap<String, BiMap> sector = DataManager.getInstance().getsector();

        if (!sector.isEmpty())
        {
            /*
            username = null;
            groupname = null;
            devicename = null;
            textView12.setText("");
            for (Map.Entry<String, BiMap> entry : sector.entrySet()) {
                username = entry.getKey();
                BiMap<String,ArrayList> value = (BiMap<String,ArrayList>)entry.getValue();
                for (Map.Entry<String, ArrayList> entrys : value.entrySet()) {
                    groupname.add(entrys.getKey());
                    ArrayList<Device> devicelist = entrys.getValue();
                    for (int i = 0; i < devicelist.size(); i ++)
                    {
                        Device device = devicelist.get(i);
                        devicename.add(device.getDeviceName());
                    }
                }
            }
            if (username!=null){
            textView12.setText(username + groupname + devicename);
            }else
            {
                textView12.setText("null" + groupname + devicename);
            }
            */



            textView12.setText(sector.toString());

        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(v.getContext(), AdminPage.class);
                startActivity(intent);
            }
        });

        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(v.getContext(), AssignGroup.class);
                startActivity(intent);
            }
        });




    }

}
