package com.example.hesolutions.horizon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public CustomListAdapter(Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.zonelist_row, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.zonelist_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        txtTitle.setText(web[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        imageView.setImageResource(imageId[position]);

        return rowView;


    }

}