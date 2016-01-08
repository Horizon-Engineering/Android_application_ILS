package com.example.hesolutions.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TotalDevice extends AppCompatActivity {

    EditText Inputsum;
    Button Enter;
    Button canceldevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_device);


        Inputsum = (EditText)findViewById(R.id.Inputsum);
        Enter = (Button)findViewById(R.id.Enter);
        canceldevice = (Button)findViewById(R.id.canceldevice);
        canceldevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(v.getContext(), AdminPage.class);
                startActivity(intent1);
            }
        });
        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getmessage = Inputsum.getText().toString();

                if (getmessage.isEmpty())
                {
                    Toast.makeText(TotalDevice.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }else
                {
                    int sum = Integer.parseInt(getmessage);
                    if (sum > 0)
                    {
                        //DataManager.getInstance().setGrid(sum);
                        Intent startgrid = new Intent(TotalDevice.this, AdminPage.class);
                        startActivity(startgrid);
                    }else
                    {
                        Inputsum.setText("");
                        Toast.makeText(TotalDevice.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

    }

}
