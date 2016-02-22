package com.example.hesolutions.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.WindowManager;
import android.widget.TabHost;
import android.app.TabActivity;
import android.widget.TextView;

public class TabViewAdmin extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabiew_for_user);



        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec spec;
        Intent intent;

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ActivityAdminStack.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("Access")
                .setIndicator("Access", getResources().getDrawable(R.drawable.ic_launcher))
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, HomePage.class);
        spec = tabHost.newTabSpec("Logout")
                .setIndicator("Logout", getResources().getDrawable(R.drawable.ic_launcher))
                .setContent(intent);
        tabHost.addTab(spec);

        //set tab which one you want open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);
        getTabHost().setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if (tabId == "Logout") finish();
            }
        });
    }

}
