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
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    Button LOGIN;
    Button Quit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        LOGIN = (Button)findViewById(R.id.LOGIN);
        Quit = (Button)findViewById(R.id.Quit);
        LOGIN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(HomePage.this, UnlockLogin.class);
                startActivity(startNewActivityIntent);

            }
        });
        Quit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        GridView gridView = (GridView)findViewById(R.id.gridView);

        ArrayList<String> numberlist;
        numberlist = DataManager.getInstance().getGrid();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,numberlist);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomePage.this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
