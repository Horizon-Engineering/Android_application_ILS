package com.example.hesolutions.horizon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] zoneList;

    public CustomListAdapter(Activity context, String[] zoneList) {
        super(context, R.layout.zonelist_row, zoneList);
        this.context = context;
        this.zoneList = zoneList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.zonelist_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        txtTitle.setText(zoneList[position]);
        return rowView;
    }

}
*/