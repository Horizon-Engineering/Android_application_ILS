package com.example.hesolutions.horizon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;

import android.widget.Toast;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


public class ZoneSet extends Activity {
    static class ViewHolder {
        TextView text;
    }
    public class HashBiMapAdapter extends BaseAdapter {

        private BiMap<String, BiMap> sectorlist = HashBiMap.create();
        private String[]sectorID;          //keys
        public HashBiMapAdapter(BiMap<String, BiMap> sector){
            sectorlist = sector;
            sectorID = sectorlist.keySet().toArray(new String[sector.size()]);
        }



        public String getKey(int position) {       //key
            return sectorID[position] ;
        }

        @Override
        public int getCount() {
            return sectorlist.size();
        }

        @Override
        public BiMap getItem(int position) {       //values
            return sectorlist.get(sectorID[position]);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (rowView == null) {
                rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.text.setText(sectorID[position]);

            return rowView;

        }
        public BiMap<String, BiMap> getList(){
            return sectorlist;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_set);
    }

}
