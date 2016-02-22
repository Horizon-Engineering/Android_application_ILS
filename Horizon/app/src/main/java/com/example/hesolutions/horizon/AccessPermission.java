package com.example.hesolutions.horizon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.common.collect.BiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AccessPermission extends AppCompatActivity {

    EditText MSG;
    EditText CODE;
    TextView textView;
    Button SAVE;
    Button LOAD;
    Button cancelback;
    Button DELETE;

    //Button button;
    // Loginaccount key = MSG, value = CODE;
    //HashMap<String,String> Loginaccount = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_permission);
        SAVE = (Button)findViewById(R.id.SAVE);
        LOAD = (Button)findViewById(R.id.LOAD);
        MSG = (EditText)findViewById(R.id.MSG);
        CODE = (EditText)findViewById(R.id.CODE);
        DELETE = (Button)findViewById(R.id.DELETE);
        textView = (TextView)findViewById(R.id.textView);
        textView.setVisibility(View.GONE);
        cancelback = (Button)findViewById(R.id.cancelback);


        // button = (Button)findViewById(R.id.button);
        cancelback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(AccessPermission.this, AdminPage.class);
                startActivity(startNewActivityIntent);


            }
        });
        SAVE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String Accounts = MSG.getText().toString();    //value
                String Passwords = CODE.getText().toString();  //key
                BiMap<String, ArrayList> bimap;
                bimap = DataManager.getInstance().getaccount();
                ArrayList<String> accout = new ArrayList<String>();

                String [] arr = {"#59dbe0", "#f57f68", "#87d288", "#f8b552","#39add1","#3079ab","#c25975","#e15258",
                        "#f9845b","#838cc7","#7d669e","#53bbb4","#51b46d","#e0ab18","#637a91","#f092b0","#b7c0c7"};
                Random random = new Random();
                int select = random.nextInt(arr.length);
                String color = arr[select];

                if (Accounts.isEmpty()||Passwords.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Missing Accounts or Passwords", Toast.LENGTH_LONG).show();
                }else if (bimap.get(Passwords) != null)
                {
                    accout =  bimap.get(Passwords);
                    String accoutname = accout.get(0);
                    Toast.makeText(getApplicationContext(), "Existant accout: " + accoutname, Toast.LENGTH_LONG).show();
                    MSG.setText("");
                    CODE.setText("");
                }
                else if(Passwords.length()!=4)
                {
                    Toast.makeText(getApplicationContext(), "The Password must be 4 digits", Toast.LENGTH_LONG).show();
                    CODE.setText("");
                }
                else{
                    accout.add(Accounts);
                    accout.add(color);
                    bimap.put(Passwords, accout);
                    DataManager.getInstance().setaccount(bimap);

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
                BiMap<String,ArrayList> bimap;
                bimap = DataManager.getInstance().getaccount();
                textView.setText(bimap.toString());
                textView.setVisibility(View.VISIBLE);

            }
        });

        DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Accounts = MSG.getText().toString();    //value
                String Passwords = CODE.getText().toString();  //key
                BiMap<String,ArrayList> bimap;
                ArrayList<String> nameset = new ArrayList<String>();
                bimap = DataManager.getInstance().getaccount();
                nameset = bimap.get(Passwords);
                if (Accounts.equals(nameset.get(0)))
                {
                    bimap.remove(Passwords);
                    MSG.setText("");
                    CODE.setText("");
                    DataManager.getInstance().setaccount(bimap);
                    Toast.makeText(getApplicationContext(), "Account deleted successfully", Toast.LENGTH_LONG).show();
                }else
                {
                    MSG.setText("");
                    CODE.setText("");
                    Toast.makeText(getApplicationContext(), "Enter a valid account", Toast.LENGTH_LONG).show();
                }

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