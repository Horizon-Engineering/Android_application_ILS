package com.example.hesolutions.horizon;

import android.app.Activity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SectorCustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    private final String[] data_array;
    private ListView mListView;


    public SectorCustomListAdapter(Activity context, String[] web, Integer[] imageId, String[] data_array) {
        super(context, R.layout.sectorlist_row, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.data_array = data_array;
    }

    @Override
    public View getView(int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        final View rowView = inflater.inflate(R.layout.sectorlist_row, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        imageView.setImageResource(imageId[position]);

        Button btn_sectorName = (Button) rowView.findViewById(R.id.btn_sectorName);
        btn_sectorName.setBackgroundDrawable(null);
        btn_sectorName.setText(web[position]);

        Button btn_sectorData = (Button) rowView.findViewById(R.id.btn_sectorData);
        btn_sectorData.setBackgroundDrawable(null);
        btn_sectorData.setText(data_array[position]);

        final SwitchCompat switchCompat = (SwitchCompat) rowView.findViewById(R.id.switch_compat);

        mListView = (ListView) rowView.findViewById(R.id.sector_list);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCompat.isChecked() == true) {
                    Toast.makeText(getContext(), "Checked", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_sectorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Unchecked", Toast.LENGTH_SHORT).show();
            }
        });

        btn_sectorData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Unchecked", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
}
