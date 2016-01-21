package com.example.hesolutions.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserPage extends Activity {

    TextView Username;
    Button logout;
    Button Calendar;
    Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        Username = (TextView)findViewById(R.id.Username);
        logout = (Button) findViewById(R.id.logout);
        Calendar = (Button)findViewById(R.id.Calendar);
        testButton = (Button)findViewById(R.id.testButton);

        String cname = DataManager.getInstance().getUsername();
        Username.setText(cname);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),HomePage.class);
                startActivity(intent);
            }
        });
        /*
        GridView gridView = (GridView)findViewById(R.id.gridView);

        ArrayList<String> numberlist;
        numberlist = DataManager.getInstance().getGrid();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, numberlist);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UserPage.this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
*/
        Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),GlobalCalendar.class);
                startActivity(intent);


            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),ChooseDeviceActivity.class);
                startActivity(intent);


            }
        });

    }

}
