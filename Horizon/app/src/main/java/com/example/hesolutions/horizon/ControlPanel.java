package com.example.hesolutions.horizon;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DefaultDatabaseErrorHandler;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.allin.activity.action.SysApplication;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.DeviceList;
import com.homa.hls.database.Gateway;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.mylibrary.WeekView;
import com.mylibrary.WeekViewEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ControlPanel extends Activity {

    HashMap<String, HashMap> sector = DataManager.getInstance().getsector();
    String username =DataManager.getInstance().getUsername();
    HashMap<String, ArrayList> sectordetail = sector.get(username);
    ArrayList<String> sectorArray = new ArrayList<>();
    EnhancedSeekBar seekBar;
    String str1;
    String str2;
    byte intensity;
    ImageView imageViewroomlayout;
    ExpandListAdapter adapter;
    TextView Intensity, ownertag, owner, sectortag, sectornameT, devicetag, devicenameT, Intensitynum;
    Handler myHandler;
    Runnable myRunnable;
    AlertDialog controlalertdialog;
    AlertDialog.Builder controlbuilder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        seekBar = (EnhancedSeekBar)findViewById(R.id.seekBar);
        imageViewroomlayout = (ImageView)findViewById(R.id.imageViewroomlayout);
        ownertag = (TextView)findViewById(R.id.ownertag);
        owner = (TextView)findViewById(R.id.owner);
        sectortag = (TextView)findViewById(R.id.sectortag);
        sectornameT = (TextView)findViewById(R.id.sectornameT);
        devicetag = (TextView)findViewById(R.id.devicetag);
        devicenameT = (TextView)findViewById(R.id.devicenameT);
        Intensity = (TextView)findViewById(R.id.Intensity);
        Intensitynum = (TextView)findViewById(R.id.Intensitynum);
        if (sector.get(username) == null) {
        } else {
            for (Map.Entry<String, ArrayList> entry : sectordetail.entrySet()) {
                sectorArray.add(entry.getKey());
            }

            if (sectorArray.isEmpty() || sectorArray.size() == 0) {
            } else {
                adapter = new ExpandListAdapter(this, sectorArray);
                ExpandableListView sectorListView = (ExpandableListView) findViewById(R.id.sectorListViewId);
                sectorListView.setAdapter(adapter);
            }
        }
        owner.setText(username);

        myHandler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ControlPanel.this, ScreenSaver.class);
                myHandler.removeCallbacks(myRunnable);
                startActivity(intent);
            }
        };
        myHandler.postDelayed(myRunnable, 3*60*1000);
    }

    public class ExpandListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private ArrayList<String> groups;

        public ExpandListAdapter(Context context, ArrayList<String> groups) {
            this.context = context;
            this.groups = groups;
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            ArrayList<Device> devicelist = sectordetail.get(getGroup(groupPosition));
            ArrayList<String> devicenamelist = new ArrayList<>();
            for (int i = 0; i < devicelist.size(); i++) {
                devicenamelist.add(devicelist.get(i).getDeviceName());
            }

            return devicenamelist.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String devicename = getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.deviceswitch, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.textView);
            final EnhancedSwitch switchid = (EnhancedSwitch)convertView.findViewById(R.id.switchid);
            tv.setText(devicename);

            final Device device = DatabaseManager.getInstance().getDeviceInforName(devicename);

            if (device.getCurrentParams()[1]!=0)
            {
                switchid.setCheckedProgrammatically(true);
            } else
            {
                switchid.setCheckedProgrammatically(false);
            }

            if (device.getChannelMark()==5)
            {
                switchid.setTrackDrawable(getResources().getDrawable(R.drawable.locksector));
            } else
            {
                switchid.setTrackDrawable(getResources().getDrawable(R.drawable.togglebgpress));
            }


            tv.setTag(device);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device deviceA = (Device) v.getTag();
                    Device device1 = DatabaseManager.getInstance().getDeviceInforName(deviceA.getDeviceName());
                    intensity = device1.getCurrentParams()[1];
                    seekBar.setVisibility(View.VISIBLE);
                    Intensitynum.setVisibility(View.VISIBLE);
                    Intensity.setVisibility(View.VISIBLE);
                    DataManager.getInstance().setthedevice(deviceA);
                    if (switchid.isChecked() == true) {
                        seekBar.setProgressProgrammatically(intensity);
                        Intensitynum.setText(Integer.toString(intensity) + "%");
                    } else {
                        seekBar.setProgressProgrammatically(0);
                        Intensitynum.setText("0%");
                    }
                    devicetag.setVisibility(View.VISIBLE);
                    devicenameT.setVisibility(View.VISIBLE);
                    devicenameT.setText(((TextView) v).getText().toString());
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressfinal;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Gateway gateway = SysApplication.getInstance().getCurrGateway(ControlPanel.this);
                    if (gateway != null) {
                        progressfinal = progress;
                        Intensitynum.setText(Integer.toString(progress) + "%");
                    } else {
                        Showlog();
                        seekBar.setEnabled(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Gateway gateway = SysApplication.getInstance().getCurrGateway(ControlPanel.this);
                    if (gateway != null) {
                        seekBar.setEnabled(true);
                        Intensitynum.setText(Integer.toString(progressfinal) + "%");
                        final Device devicea = DataManager.getInstance().getthedevice();
                        final byte[] SetParams = new byte[5];
                        if (progressfinal == 0) {
                            SetParams[0] = (byte) 17;
                            SetParams[1] = (byte) 0;
                            SetParams[2] = (byte) 0;
                            SetParams[3] = (byte) 0;
                            SetParams[4] = (byte) 0;
                        } else {
                            SetParams[0] = (byte) 17;
                            SetParams[1] = (byte) progressfinal;
                            SetParams[2] = (byte) 0;
                            SetParams[3] = (byte) 0;
                            SetParams[4] = (byte) 0;
                        }

                        if (progressfinal == 0) {
                            if (controlalertdialog != null && controlalertdialog.isShowing()) {
                            } else {
                                controlbuilder = new AlertDialog.Builder(ControlPanel.this.getParent());
                                controlbuilder.setTitle("Warning");
                                controlbuilder.setMessage("Do you want to remove the control of the device?");
                                controlbuilder.setCancelable(false);
                                controlbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        byte[] data;
                                        data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                                        devicea.setCurrentParams(data);
                                        devicea.setChannelMark((short) 0);
                                        DatabaseManager.getInstance().updateDevice(devicea);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                controlbuilder.setNegativeButton("No(keep control)", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        byte[] data;
                                        data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                                        devicea.setCurrentParams(data);
                                        devicea.setChannelMark((short) 5);
                                        DatabaseManager.getInstance().updateDevice(devicea);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                controlalertdialog = controlbuilder.create();
                                controlalertdialog.show();
                            }
                        }else
                        {
                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                            devicea.getDeviceAddress(), (short) 0, SetParams), devicea.getGatewayMacAddr(), devicea.getGatewayPassword(),
                                    devicea.getGatewaySSID(), ControlPanel.this));

                            devicea.setCurrentParams(SetParams);
                            devicea.setChannelMark((short) 5);
                            DatabaseManager.getInstance().updateDevice(devicea);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Showlog();
                        seekBar.setEnabled(false);
                    }
                }
            });


            switchid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Gateway gateway = SysApplication.getInstance().getCurrGateway(ControlPanel.this);
                    if (gateway != null) {
                        seekBar.setVisibility(View.INVISIBLE);
                        Intensitynum.setVisibility(View.INVISIBLE);
                        Intensity.setVisibility(View.INVISIBLE);

                        if (switchid.isChecked() == true) {
                            byte[] data;
                            data = new byte[]{(byte) 17, (byte) 100, (byte) 0, (byte) 0, (byte) 0};
                            DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                            device.getDeviceAddress(), (short) 0, data), device.getGatewayMacAddr(), device.getGatewayPassword(),
                                    device.getGatewaySSID(), ControlPanel.this));
                            device.setCurrentParams(data);
                            device.setChannelMark((short) 5);
                            DatabaseManager.getInstance().updateDevice(device);
                            adapter.notifyDataSetChanged();
                        } else {
                            if (controlalertdialog != null && controlalertdialog.isShowing()) {
                            } else {
                                controlbuilder = new AlertDialog.Builder(ControlPanel.this.getParent());
                                controlbuilder.setTitle("Warning");
                                controlbuilder.setMessage("Do you want to remove the control of the device?");
                                controlbuilder.setCancelable(false);
                                controlbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        byte[] data;
                                        data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                                        device.setCurrentParams(data);
                                        device.setChannelMark((short) 0);
                                        DatabaseManager.getInstance().updateDevice(device);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                controlbuilder.setNegativeButton("No(keep control)", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        byte[] data;
                                        data = new byte[]{(byte) 17, (byte) 0, (byte)0, (byte) 0, (byte) 0};
                                        device.setCurrentParams(data);
                                        device.setChannelMark((short) 5);
                                        DatabaseManager.getInstance().updateDevice(device);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                controlalertdialog = controlbuilder.create();
                                controlalertdialog.show();
                            }
                        }
                    } else {
                        Showlog();
                        switchid.setCheckedProgrammatically(false);
                    }
                }
            });

            return convertView;
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            ArrayList<Device> devicelist = sectordetail.get(groups.get(groupPosition));
            if (devicelist!=null) {return devicelist.size();}
            else{return 0;}
        }

        @Override
        public String getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded,
                                 View convertView, final ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.zonelist_row, null);
            }

            TextView txtTitle = (TextView) convertView.findViewById(R.id.textView);
            final EnhancedSwitch switchid = (EnhancedSwitch)convertView.findViewById(R.id.switchid);
            final String sectorname = getGroup(groupPosition);
            txtTitle.setText(sectorname);
            final ArrayList<Device> devicelist = sectordetail.get(sectorname);
            if (devicelist!=null) {
                for (Device device:devicelist) {
                    Device sampledevice = DatabaseManager.getInstance().getDeviceInforName(device.getDeviceName());
                    if (!devicelist.isEmpty()) {
                        if (sampledevice.getCurrentParams()[1]!=0) {
                            switchid.setCheckedProgrammatically(true);
                            break;
                        } else {
                            switchid.setCheckedProgrammatically(false);
                        }
                    }else
                    {
                        switchid.setCheckedProgrammatically(false);
                    }
                }
            }else
            {
                switchid.setCheckedProgrammatically(false);
            }

            txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekBar.setVisibility(View.INVISIBLE);
                    Intensitynum.setVisibility(View.INVISIBLE);
                    Intensity.setVisibility(View.INVISIBLE);
                    if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                    else ((ExpandableListView) parent).expandGroup(groupPosition, true);
                    Bitmap bitmap = null;
                    bitmap = dataupdate(sectorname + ".png");
                    if (bitmap != null) {
                        Drawable d = new BitmapDrawable(getResources(), bitmap);
                        imageViewroomlayout.setBackground(d);
                    } else {
                        imageViewroomlayout.setBackground(null);
                    }
                    sectortag.setVisibility(View.VISIBLE);
                    sectornameT.setVisibility(View.VISIBLE);
                    sectornameT.setText(((TextView) v).getText().toString());
                    devicetag.setVisibility(View.INVISIBLE);
                    devicenameT.setVisibility(View.INVISIBLE);
                }
            });
            switchid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Gateway gateway = SysApplication.getInstance().getCurrGateway(ControlPanel.this);
                    if (gateway != null) {
                        seekBar.setVisibility(View.INVISIBLE);
                        Intensitynum.setVisibility(View.INVISIBLE);
                        Intensity.setVisibility(View.INVISIBLE);

                        if (!sectorname.equals(" ")) {
                            if (devicelist != null) {
                                if (switchid.isChecked() == true) {
                                    for (Device thedevice : devicelist) {
                                        byte[] data;
                                        data = new byte[]{(byte) 17, (byte) 100, (byte)0, (byte) 0, (byte) 0};
                                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4,
                                                        thedevice.getDeviceAddress(), (short) 0, data), thedevice.getGatewayMacAddr(), thedevice.getGatewayPassword(),
                                                thedevice.getGatewaySSID(), ControlPanel.this));
                                        thedevice.setCurrentParams(data);
                                        thedevice.setChannelMark((short) 5);
                                        DatabaseManager.getInstance().updateDevice(thedevice);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    if (controlalertdialog != null && controlalertdialog.isShowing()) {
                                    } else {
                                        controlbuilder = new AlertDialog.Builder(ControlPanel.this.getParent());
                                        controlbuilder.setTitle("Warning");
                                        controlbuilder.setMessage("Do you want to remove the control of the device?");
                                        controlbuilder.setCancelable(false);
                                        controlbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                for (Device thedevice : devicelist) {
                                                    byte[] data;
                                                    data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                                                    thedevice.setCurrentParams(data);
                                                    thedevice.setChannelMark((short)0);
                                                    DatabaseManager.getInstance().updateDevice(thedevice);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                                        controlbuilder.setNegativeButton("No(keep control)", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                for (Device thedevice : devicelist) {
                                                    byte[] data;
                                                    data = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                                                    thedevice.setCurrentParams(data);
                                                    thedevice.setChannelMark((short) 5);
                                                    DatabaseManager.getInstance().updateDevice(thedevice);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                                        controlalertdialog = controlbuilder.create();
                                        controlalertdialog.show();
                                    }
                                }
                            } else {
                                switchid.setCheckedProgrammatically(false);
                            }
                        }
                    } else {
                        Showlog();
                        switchid.setCheckedProgrammatically(false);
                    }
                }
            });

            return convertView;

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
    public static Bitmap dataupdate(String filename) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Horizon/Bitmap");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream streamIn = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(streamIn);
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    public void Showlog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ControlPanel.this.getParent());
        builder.setTitle("Error");
        builder.setMessage("Gateway Error, please connect the wifi and press OK");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Gateway gateway = SysApplication.getInstance().getCurrGateway(ControlPanel.this);
                if (gateway != null) seekBar.setEnabled(true);
            }
        });
        AlertDialog myAlertDialog = builder.create();
        if (myAlertDialog != null && !myAlertDialog.isShowing()) {
            myAlertDialog.show();
        }

    }

    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable,3*60*1000);

    }

    @Override
    public void onResume() {
        super.onResume();
        myHandler.postDelayed(myRunnable, 3 * 60 * 1000);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        myHandler.removeCallbacks(myRunnable);
    }
}
