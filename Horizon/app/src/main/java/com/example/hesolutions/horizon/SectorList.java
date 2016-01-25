package com.example.hesolutions.horizon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SectorList extends Activity {

    ListView sector;
    SwitchCompat switch_compact;
    //TODO get all the sectors from the userData
    String[] sectorArray = {"Sector1", "Sector2", "Sector3", "Sector4", "Sector5", "Sector6", "Sector7"};

    Integer[] sector_imageId = {
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon
    };

    String[] dataArray = {"Active device 3", "Active device 5", "Active device 8", "Active device 9", "Active device 10", "Active device 10", "Inactive device 7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector_list);

        SectorCustomListAdapter adapter = new SectorCustomListAdapter(this, sectorArray);
        sector = (ListView) findViewById(R.id.sector_list);
        sector.setAdapter(adapter);


        sector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
               /* AlertDialog.Builder adb = new AlertDialog.Builder(ZoneList.this);
                adb.setTitle("LVSelectedItemExample");
                adb.setMessage("Selected Item is = " + list.getItemAtPosition(position));
                adb.setPositiveButton("Ok", null);
                adb.show();*/
                Intent intent = new Intent(SectorList.this, UserPage.class);
                startActivity(intent);
            }
        });
    }
}
