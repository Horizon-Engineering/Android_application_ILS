package com.example.hesolutions.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.app.TabActivity;
import android.widget.TabWidget;
import android.widget.TextView;

public class TabiewForUser extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabiew_for_user);



        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        /*
        TabWidget widget = tabHost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tab_selector);
        }
        */
        TextView textview14 = (TextView)findViewById(R.id.textView14);
        tabHost.setup();
        TabHost.TabSpec spec;
        Intent intent;

        textview14.setText(DataManager.getInstance().getUsername());
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ActivityStack.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("Calendar")
                .setIndicator("Calendar", getResources().getDrawable(R.drawable.ic_launcher))
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ControlPanel.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("Control")
                .setIndicator("Control", getResources().getDrawable(R.drawable.ic_launcher))
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
