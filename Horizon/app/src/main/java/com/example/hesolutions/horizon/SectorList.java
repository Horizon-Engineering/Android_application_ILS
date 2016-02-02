package com.example.hesolutions.horizon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class SectorList extends Activity {

    ListView sector;
    //TODO get all the sectors from the userData
    String[] sectorArray = {"Sector1", "Sector2", "Sector3", "Sector4", "Sector5", "Sector6", "Sector7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector_list);

        SectorCustomListAdapter adapter = new SectorCustomListAdapter(this, sectorArray);
        sector = (ListView) findViewById(R.id.sector_list);
        sector.setAdapter(adapter);
    }
}
