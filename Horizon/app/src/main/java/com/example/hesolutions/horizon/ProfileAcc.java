package com.example.hesolutions.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ProfileAcc extends AppCompatActivity {
    EditText CODE1;
    EditText CODE2;
    EditText CODE3;
    EditText CODE4;
    Button CANCEL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acc);


        CODE1 = (EditText)findViewById(R.id.CODE1);
        CODE2 = (EditText)findViewById(R.id.CODE2);
        CODE3 = (EditText)findViewById(R.id.CODE3);
        CODE4 = (EditText)findViewById(R.id.CODE4);
        CANCEL = (Button)findViewById(R.id.CANCEL);


        CODE1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(CODE1.getText().length()!=0)
                    CODE2.requestFocus();
                return false;
            }
        });
        CODE2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(CODE2.getText().length()!=0)
                    CODE3.requestFocus();
                return false;
            }
        });
        CODE3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(CODE3.getText().length()!=0)
                    CODE4.requestFocus();
                return false;
            }
        });

        CODE4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (CODE4.getText().length() != 0) {
                    String code1 = CODE1.getText().toString();
                    String code2 = CODE2.getText().toString();
                    String code3 = CODE3.getText().toString();
                    String code4 = CODE4.getText().toString();
                    String code = code1 + code2 + code3 + code4;

                    HashMap<String, String> hashmap;
                    hashmap = DataManager.getInstance().getaccount();
                    if (hashmap.get(code) != null) {
                        String Caccount = hashmap.get(code);

                        Intent startNewActivityIntent = new Intent(ProfileAcc.this, DevicesCheck.class);
                        startNewActivityIntent.putExtra("CorrespondingAccounts", Caccount);
                        startActivity(startNewActivityIntent);

                    } else {
                        Toast.makeText(getApplicationContext(), "No Accounts exist", Toast.LENGTH_SHORT).show();
                        CODE1.setText("");
                        CODE2.setText("");
                        CODE3.setText("");
                        CODE4.setText("");
                        CODE1.requestFocus();
                    }
                }

                return false;
            }
        });


        CANCEL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startNewActivityIntent = new Intent(ProfileAcc.this, MainActivity.class);
                startActivity(startNewActivityIntent);


            }
        });

    }

}
