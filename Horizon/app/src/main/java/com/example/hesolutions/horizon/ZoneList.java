package com.example.hesolutions.horizon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/*
public class ZoneList extends Activity implements View.OnClickListener {
    ListView list;
    View selectedZoneRow;
    View selectedSectorRow;

    //TODO: Get all zones from user data
    String[] zoneArray = {"Zone1", "Zone2", "Zone3", "Zone4", "Zone5", "Zone6", "Zone7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_list);

        CustomListAdapter adapter = new CustomListAdapter(this, zoneArray);
        list = (ListView) findViewById(R.id.zoneListViewId);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (selectedZoneRow != null) {
                    selectedZoneRow.setBackgroundResource(R.color.app_background_color);
                }
                selectedZoneRow = view;
                view.setBackgroundResource(R.color.selected_row);
                updateSectorList(0);
            }
        });
    }

    private void updateSectorList(int zoneId) {
        //TODO get sector for the particular zone Id
        String[] sectorArray = {"Sector1", "Sector2", "Sector3", "Sector4", "Sector5", "Sector6", "Sector7"};
        SectorCustomListAdapter adapter = new SectorCustomListAdapter(this, sectorArray);
        ListView sector = (ListView) findViewById(R.id.sectorListViewId);
        sector.setAdapter(adapter);

        sector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (selectedSectorRow != null) {
                    selectedSectorRow.setBackgroundResource(R.color.app_background_color);

                    TextView active_device = (TextView) findViewById(R.id.activeDevicesId);
                    active_device.setText("Active Device: 10");
                    TextView inactive_device = (TextView) findViewById(R.id.inactiveDevice);
                    inactive_device.setText("Inactive Device: 3");
                    TextView defective_device = (TextView) findViewById(R.id.defectDevice);
                    defective_device.setText("Defect Device: 1");
                    ImageView sector_Image = (ImageView) findViewById(R.id.sectorImage);
                    sector_Image.setImageResource(R.drawable.horizon);
                }
                selectedSectorRow = view;
                view.setBackgroundResource(R.color.selected_row);

            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.i("clicks", "You Clicked B1");
        Intent i = new Intent(ZoneList.this, UserPage.class);
        startActivity(i);
    }
}



*/


