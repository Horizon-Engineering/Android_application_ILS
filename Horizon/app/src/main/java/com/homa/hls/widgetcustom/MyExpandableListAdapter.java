package com.homa.hls.widgetcustom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import com.example.hesolutions.horizon.R;
import com.homa.hls.widgetcustom.MyExpandableListView.QQHeaderAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends SimpleExpandableListAdapter implements QQHeaderAdapter {
    private Context context;
    private HashMap<Integer, Integer> groupStatusMap;
    private MyExpandableListView listView;
    private int mChildPosition;
    private int mGroupPosition;

    public MyExpandableListAdapter(Context context, MyExpandableListView listView, List<? extends Map<String, ?>> groupData, int expandedGroupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, expandedGroupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
        this.mGroupPosition = -1;
        this.mChildPosition = -1;
        this.groupStatusMap = new HashMap();
        this.context = context;
        this.listView = listView;
    }

    public int getQQHeaderState(int groupPosition, int childPosition) {
        if (childPosition == getChildrenCount(groupPosition) - 1) {
            return 2;
        }
        if (childPosition != -1 || this.listView.isGroupExpanded(groupPosition)) {
            return 1;
        }
        return 0;
    }

    public void configureQQHeader(View header, int groupPosition, int childPosition, int alpha) {
        String strtext = (String) ((Map) getGroup(groupPosition)).get("g");
        ((TextView) header.findViewById(R.id.groupto)).setText(strtext);
        ((ImageView) header.findViewById(R.id.openIcon)).setImageResource(R.drawable.group_open);
        if (strtext.equals("\u5168\u5f69\u706f") || strtext.equals("Full color") || strtext.equals("Alle Farben")) {
            ((ImageView) header.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_a);
        } else if (strtext.equals("\u667a\u80fd\u63d2\u5ea7") || strtext.equals("Smart socket") || strtext.equals("Intelligente Steckdose")) {
            ((ImageView) header.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_b);
        } else if (strtext.equals("\u5355\u8272\u706f") || strtext.equals("Single color") || strtext.equals("Einfarbig")) {
            ((ImageView) header.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_c);
        } else if (strtext.equals("\u8272\u6e29\u706f") || strtext.equals("Color temp") || strtext.equals("Farbtemperatur")) {
            ((ImageView) header.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_d);
        } else if (strtext.equals("\u5149\u4f20\u611f\u5668") || strtext.equals("Photot sensor") || strtext.equals("Lichtsensor")) {
            ((ImageView) header.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_e);
        } else if (strtext.equals("\u7a97\u5e18\u63a7\u5236\u5668") || strtext.equals("Curtain controller") || strtext.equals("Jalousien Steuerung")) {
            ((ImageView) header.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_f);
        } else if (strtext.equals("\u4eba\u4f53\u611f\u5e94") || strtext.equals("Motion sensor") || strtext.equals("Bewegungssensor")) {
            ((ImageView) header.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_g);
        } else if (strtext.equals("\u6e29\u5ea6\u4f20\u611f\u5668") || strtext.equals("Temperature sensor")) {
            ((ImageView) header.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_h);
        }
    }

    public void setGroupClickStatus(int groupPosition, int status) {
        this.groupStatusMap.put(Integer.valueOf(groupPosition), Integer.valueOf(status));
    }

    public int getGroupClickStatus(int groupPosition) {
        if (this.groupStatusMap.containsKey(Integer.valueOf(groupPosition))) {
            return ((Integer) this.groupStatusMap.get(Integer.valueOf(groupPosition))).intValue();
        }
        return 0;
    }

    public void setItemChecked(int groupPosition, int childPosition) {
        if (this.listView != null) {
            this.mGroupPosition = groupPosition;
            this.mChildPosition = childPosition;
            int numberOfGroupThatIsOpened = 0;
            for (int i = 0; i < groupPosition; i++) {
                if (this.listView.isGroupExpanded(i)) {
                    numberOfGroupThatIsOpened += getChildrenCount(i);
                }
            }
            int position = ((numberOfGroupThatIsOpened + groupPosition) + childPosition) + 1;
            System.out.println("groupPosition=" + groupPosition + ", childPosition=" + childPosition + ", position=" + position + ", isItemChecked=" + this.listView.isItemChecked(position));
            if (!this.listView.isItemChecked(position)) {
                this.listView.setItemChecked(position, true);
            }
        }
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.group, null);
        }
        ImageView indicator = (ImageView) convertView.findViewById(R.id.openIcon);
        if (isExpanded) {
            indicator.setImageResource(R.drawable.group_open);
            if (groupPosition == this.mGroupPosition) {
                setItemChecked(this.mGroupPosition, this.mChildPosition);
            }
        } else {
            indicator.setImageResource(R.drawable.group_close);
        }
        String strtext = (String) ((Map) getGroup(groupPosition)).get("g");
        if (strtext.equals("\u5168\u5f69\u706f") || strtext.equals("Full color") || strtext.equals("Alle Farben")) {
            ((ImageView) convertView.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_a);
        } else if (strtext.equals("\u667a\u80fd\u63d2\u5ea7") || strtext.equals("Smart socket") || strtext.equals("Intelligente Steckdose")) {
            ((ImageView) convertView.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_b);
        } else if (strtext.equals("\u5355\u8272\u706f") || strtext.equals("Single color") || strtext.equals("Einfarbig")) {
            ((ImageView) convertView.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_c);
        } else if (strtext.equals("\u8272\u6e29\u706f") || strtext.equals("Color temp") || strtext.equals("Farbtemperatur")) {
            ((ImageView) convertView.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_d);
        } else if (strtext.equals("\u5149\u4f20\u611f\u5668") || strtext.equals("Photot sensor") || strtext.equals("Lichtsensor")) {
            ((ImageView) convertView.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_e);
        } else if (strtext.equals("\u7a97\u5e18\u63a7\u5236\u5668") || strtext.equals("Curtain controller") || strtext.equals("Jalousien Steuerung")) {
            ((ImageView) convertView.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_f);
        } else if (strtext.equals("\u4eba\u4f53\u611f\u5e94") || strtext.equals("Motion sensor") || strtext.equals("Bewegungssensor")) {
            ((ImageView) convertView.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_g);
        } else if (strtext.equals("\u6e29\u5ea6\u4f20\u611f\u5668") || strtext.equals("Temperature sensor")) {
            ((ImageView) convertView.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_h);
        }
        return super.getGroupView(groupPosition, isExpanded, convertView, parent);
    }
}
