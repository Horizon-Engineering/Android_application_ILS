package com.example.hesolutions.horizon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomPinCodeAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] zoneList;

    public CustomPinCodeAdapter(Activity unlockScreen, int arrayadapter, String[] numbers) {
        super(unlockScreen, R.layout.activity_unlock_screen, numbers);
        this.context = unlockScreen;
        this.zoneList = numbers;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.arrayadapter, null, true);
        if (zoneList[position].isEmpty()) {
            rowView.setVisibility(View.INVISIBLE);
        }
        TextView txtTitle = (TextView) rowView.findViewById(R.id.passcodeBtn);
        txtTitle.setText(zoneList[position]);
        return rowView;
    }
}
