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
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateGrid extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        GridView gridView = (GridView)findViewById(R.id.gridView);

        Intent thisintent = getIntent();
        int totalsum = thisintent.getIntExtra("TotalDevices", 0);
        //gridView.setNumColumns(getheight);

        ArrayList<String> numberlist = new ArrayList<>();
        Integer number = 0;

        for (int i = 0; i < totalsum; i++)
        {
            number = number + 1;
            numberlist.add(number.toString());
        }
        //Toast.makeText(CreateGrid.this, numberlist.toString(), Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,numberlist);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreateGrid.this, ((TextView) view).getText() , Toast.LENGTH_SHORT).show();
            }
        });

    }

}
