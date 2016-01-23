package com.example.hesolutions.horizon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class ZoneList extends Activity implements View.OnClickListener {
    ListView list;

    //TODO: Get all zones from user data
    String[] mobileArray = {"Zone1", "Zone2", "Zone3", "Zone4", "Zone5", "Zone6", "Zone7"};

    Integer[] imageId = {
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon,
            R.drawable.horizon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_list);

        CustomListAdapter adapter = new CustomListAdapter(this, mobileArray, imageId);
        list = (ListView) findViewById(R.id.zoneList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent intent = new Intent(ZoneList.this, SectorList.class);
                startActivity(intent);
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






