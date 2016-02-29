package com.example.hesolutions.horizon;

import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.common.collect.BiMap;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssignAccess extends Activity {
    HashMap<String, BiMap> sector = DataManager.getInstance().getsector();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_access);
        ArrayList<String> names = new ArrayList<>();
        BiMap<String, ArrayList> nameset = DataManager.getInstance().getaccount();
        for (Map.Entry<String, ArrayList> entry : nameset.entrySet()) {
            String key = entry.getKey();
            String name = (String)entry.getValue().get(0);
            names.add(name);
        }

        UserCustomListAdapter adapter = new UserCustomListAdapter(this, names);
        ListView userList = (ListView) findViewById(R.id.userList);
        userList.setAdapter(adapter);

    }

    public class UserCustomListAdapter extends ArrayAdapter<String> {

        private  Activity context;
        private  ArrayList<String> zoneList;

        public UserCustomListAdapter(Activity adminPage,ArrayList<String> zoneList) {
            super(adminPage,R.layout.row, zoneList);
            this.zoneList = zoneList;
            this.context = adminPage;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.row, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
            txtTitle.setText(zoneList.get(position));
            return rowView;
        }

    }

    public void clickEvent(View v) {
//=====================case:sector - device
        String username = ((TextView) v).getText().toString();
        BiMap<String, ArrayList>sectordetail = sector.get(username);
        if (!username.equals(" ")) {
            ArrayList<String> sectorArray = new ArrayList<>();
            if (sector.get(username)==null) {
                ListView SectorList = (ListView) findViewById(R.id.SectorList);
                SectorList.setAdapter(null);
            }
            else
            {
                for (Map.Entry<String, ArrayList> entry : sectordetail.entrySet()) {
                    sectorArray.add(entry.getKey());
                }

                if (sectorArray.isEmpty()||sectorArray.size()==0){}
                else{

                    UserCustomListAdapter adapter = new UserCustomListAdapter(this, sectorArray);
                    ListView SectorList = (ListView) findViewById(R.id.SectorList);
                    SectorList.setAdapter(adapter);

                }
            }
        }
    }
}