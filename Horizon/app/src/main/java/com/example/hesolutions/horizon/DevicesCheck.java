package com.example.hesolutions.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DevicesCheck extends AppCompatActivity {

    TextView accountname;
    Button scannerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_check);


        accountname = (TextView)findViewById(R.id.accountname);
        String account = (String)getIntent().getStringExtra("CorrespondingAccounts");
        accountname.setText(account);


        scannerButton = (Button) findViewById(R.id.scannerButton);

        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), BarcodeScanner.class);
                startActivity(intent);
            }
        });

    }

}
