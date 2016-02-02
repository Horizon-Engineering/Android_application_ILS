package com.example.hesolutions.horizon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.BiMap;

import java.util.ArrayList;

public class UnlockScreen extends Activity {

    TextView CODE1, CODE2, CODE3, CODE4;
    GridView gridView;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    ImageButton cancel;
    int numberOfZones = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_screen);

        CODE1 = (TextView) findViewById(R.id.CODE1);
        CODE2 = (TextView) findViewById(R.id.CODE2);
        CODE3 = (TextView) findViewById(R.id.CODE3);
        CODE4 = (TextView) findViewById(R.id.CODE4);
        gridView = (GridView) findViewById(R.id.gridView);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        cancel = (ImageButton) findViewById(R.id.cancel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (height * 0.4), (int) (width * 0.31));

        GridView gridView = (GridView) findViewById(R.id.gridView);

        String[] numbers = new String[]{"1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "0"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.arrayadapter, numbers);
        gridView.setAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Changed Itemclicklistener to clickhandler for button
    public void clickHandler(View v) {
        if (!((Button) v).getText().toString().equals(" ")) {
            if (CODE1.getText().length() == 0) {
                CODE1.setText(((Button) v).getText());
                radioButton1.setChecked(true);
            } else if (CODE2.getText().length() == 0) {
                CODE2.setText(((Button) v).getText());
                radioButton2.setChecked(true);
            } else if (CODE3.getText().length() == 0) {
                CODE3.setText(((Button) v).getText());
                radioButton3.setChecked(true);
            } else if (CODE4.getText().length() == 0) {
                CODE4.setText(((Button) v).getText());
                radioButton4.setChecked(true);
            }
        }

        if (radioButton4.isChecked() == true) {
            String code1 = CODE1.getText().toString();
            String code2 = CODE2.getText().toString();
            String code3 = CODE3.getText().toString();
            String code4 = CODE4.getText().toString();
            String code = code1 + code2 + code3 + code4;

            BiMap<String, ArrayList> bimap;
            bimap = DataManager.getInstance().getaccount();
            ArrayList<String> nameset = new ArrayList<String>();
            nameset = bimap.get(code);
            //TODO: make sure this check will be removed in final version :)
            Intent startNewActivityIntent;
            if (code.equals("0000")) {
                startNewActivityIntent = new Intent(UnlockScreen.this, AdminPage.class);
                startActivity(startNewActivityIntent);
            } else if (nameset != null) {
                String Caccount = nameset.get(0);
                String color = nameset.get(1);
                //TODO: Replace this with method call from zoneset
                //numberOfZones = ZoneSet.getNumberOfZones()
                numberOfZones = 1;
                if (numberOfZones > 1) {
                    startNewActivityIntent = new Intent(UnlockScreen.this, ZoneList.class);
                } else {
                    startNewActivityIntent = new Intent(UnlockScreen.this, TabView.class);
                }
                DataManager.getInstance().setUsername(Caccount);
                DataManager.getInstance().setcolorname(color);
                startActivity(startNewActivityIntent);
            } else {
                Toast.makeText(getApplicationContext(), "No Accounts exist", Toast.LENGTH_LONG).show();
                CODE1.setText("");
                CODE2.setText("");
                CODE3.setText("");
                CODE4.setText("");
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                radioButton4.setChecked(false);
            }
        }

    }

}
