package com.example.hesolutions.horizon;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TabHost;

public class TabView extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host);

        final TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;


        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, AdminPage.class);
        spec = tabHost.newTabSpec("Access")
                .setIndicator("Access", getResources().getDrawable(R.drawable.ic_launcher))
                        .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ZoneSet.class);
        spec = tabHost.newTabSpec("ZoneSet")
                .setIndicator("ZoneSet", getResources().getDrawable(R.drawable.ic_launcher))
                        .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SectorList.class);
        spec = tabHost.newTabSpec("Sectors")
                .setIndicator("Sectors", getResources().getDrawable(R.drawable.ic_launcher))
                .setContent(intent);

       tabHost.addTab(spec);

        //set tab which one you want open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);


    }


}
