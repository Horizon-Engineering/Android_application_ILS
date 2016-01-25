package com.example.hesolutions.horizon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SectorCustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] sectorList;
    private ListView mListView;


    public SectorCustomListAdapter(Activity context, String[] web) {
        super(context, R.layout.sectorlist_row, web);
        this.context = context;
        this.sectorList = web;
        ;
    }


    @Override
    public View getView(int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.sectorlist_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.sectorList);
        txtTitle.setText(sectorList[position]);
        return rowView;
    }
}
