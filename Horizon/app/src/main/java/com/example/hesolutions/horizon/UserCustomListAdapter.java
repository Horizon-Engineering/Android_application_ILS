package com.example.hesolutions.horizon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserCustomListAdapter extends ArrayAdapter<String>  {

    private  Activity context;
    private  String[] users;
    String status = "No user Exists";

    public UserCustomListAdapter(Activity adminPage, String[] users) {
        super(adminPage,R.layout.activity_admin, users);
        this.users = users;
        this.context = adminPage;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.zonelist_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        txtTitle.setText(users[position]);
        return rowView;
    }

}
