package com.example.hesolutions.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity {

    TextView accountname;
    Button scannerButton;
    Button AccessPermit;
    Button Logout;
    Button SetDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);


        accountname = (TextView)findViewById(R.id.accountname);
        String account = DataManager.getInstance().getUsername();
        accountname.setText(account);


        scannerButton = (Button) findViewById(R.id.scannerButton);
        AccessPermit = (Button)findViewById(R.id.AccessPermit);
        Logout = (Button)findViewById(R.id.Logout);
        SetDevice = (Button)findViewById(R.id.SetDevice);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), BarcodeScanner.class);
                startActivity(intent);
            }
        });
        AccessPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(),AccessPermission.class);
                startActivity(intent1);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(),HomePage.class);
                startActivity(intent1);
            }
        });
        SetDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(), TotalDevice.class);
                startActivity(intent1);
            }
        });
        GridView gridView = (GridView)findViewById(R.id.gridView);

        ArrayList<String> numberlist;
        numberlist = DataManager.getInstance().getGrid();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, numberlist);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridView.invalidateViews();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AdminPage.this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
