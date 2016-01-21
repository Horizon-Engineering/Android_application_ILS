package com.example.hesolutions.horizon;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.homa.hls.database.Device;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    ImageButton LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        LOGIN = (ImageButton) findViewById(R.id.LOGIN);

        LOGIN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(HomePage.this, UnlockScreen.class);
                startActivity(startNewActivityIntent);

            }
        });
    }

}
