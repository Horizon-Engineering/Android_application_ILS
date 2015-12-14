package com.example.hesolutions.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText MSG;
    EditText CODE;
    TextView textView;
    Button SAVE;
    Button LOAD;
    Button LOGIN;

    //Button button;
    // Loginaccount key = MSG, value = CODE;
    //HashMap<String,String> Loginaccount = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        SAVE = (Button)findViewById(R.id.SAVE);
        LOAD = (Button)findViewById(R.id.LOAD);
        LOGIN = (Button)findViewById(R.id.LOGIN);
        setSupportActionBar(toolbar);
        System.out.println("Inside oncreate");
        MSG = (EditText)findViewById(R.id.MSG);
        CODE = (EditText)findViewById(R.id.CODE);
        textView = (TextView)findViewById(R.id.textView);
        textView.setVisibility(View.GONE);

        LOGIN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(MainActivity.this, ProfileAcc.class);
                startActivity(startNewActivityIntent);

            }
        });
       // button = (Button)findViewById(R.id.button);

        SAVE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Accounts = MSG.getText().toString();    //value
                String Passwords = CODE.getText().toString();  //key

                HashMap<String, String> hashmap;
                hashmap = DataManager.getInstance().getaccount();
                ArrayList<String> names;
                names = DataManager.getInstance().getaccoutname();

                if (Accounts.isEmpty()||Passwords.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Missing Accounts or Passwords", Toast.LENGTH_LONG).show();
                }else if (hashmap.get(Passwords) != null)
                {
                    String accountname = hashmap.get(Passwords);
                    Toast.makeText(getApplicationContext(), "Existant accout: " + accountname, Toast.LENGTH_LONG).show();
                    MSG.setText("");
                    CODE.setText("");
                }else if(Passwords.length()!=4)
                {
                    Toast.makeText(getApplicationContext(), "The Password must be 4 digits", Toast.LENGTH_LONG).show();
                    CODE.setText("");
                }else if (names.contains(Accounts))
                {
                    Toast.makeText(getApplicationContext(), "Accoutname already exists", Toast.LENGTH_LONG).show();
                    MSG.setText("");
                    CODE.setText("");
                }

                else{
                    hashmap.put(Passwords, Accounts);
                    DataManager.getInstance().setaccount(hashmap);
                    MSG.setText("");
                    CODE.setText("");
                    Toast.makeText(getApplicationContext(), "DATA saved", Toast.LENGTH_LONG).show();
                }
            }

        });


        LOAD.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                HashMap<String,String> hashmap;
                hashmap = DataManager.getInstance().getaccount();
                ArrayList<String> names;
                names = DataManager.getInstance().getaccoutname();
                textView.setText(hashmap.toString()+"           " + names.toString());
                textView.setVisibility(View.VISIBLE);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
