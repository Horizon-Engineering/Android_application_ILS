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

public class MainScreen extends AppCompatActivity {

    EditText Inputsum;
    TextView Widths;
    TextView Heights;
    Button Enter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Inputsum = (EditText)findViewById(R.id.Inputsum);
        Widths = (TextView)findViewById(R.id.Widths);
        Heights = (TextView)findViewById(R.id.Heights);
        Enter = (Button)findViewById(R.id.Enter);

        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getmessage = Inputsum.getText().toString();
                int sum = Integer.parseInt(getmessage);

                /*
                double width;
                double height;
                width = Math.sqrt(sum / 1.8);
                height = 1.8 * width;
                int widths = (int) Math.ceil(width);
                int heights = (int) Math.ceil(height);

                if (widths * heights < sum)
                {
                    Toast.makeText(MainScreen.this, "H*W<SUM", Toast.LENGTH_SHORT).show();
                }

//                Widths.setText("Width is " + widths);
//                Heights.setText("Height is " + heights);
*/
                Intent startgrid = new Intent(MainScreen.this, CreateGrid.class);
                startgrid.putExtra("TotalDevices",(int) sum);
                startActivity(startgrid);

            }
        });




    }

}
