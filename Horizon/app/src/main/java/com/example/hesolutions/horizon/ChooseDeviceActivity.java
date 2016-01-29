package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.CursorAdapter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.allin.activity.action.DataStorage;
import com.allin.activity.action.SysApplication;
import com.homa.hls.database.Area;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.DeviceList;
import com.homa.hls.database.Scene;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Event;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.DeviceSocket;
import com.homa.hls.widgetcustom.CustomImageView;
import com.mylibrary.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChooseDeviceActivity extends Activity {
    private static ArrayList<Device> mDeviceListOfCurrAreaOrScene;
    int B_1;
    DisplayMetrics DM;
    int G_1;
    int R_1;
    private boolean ThreadRunning;
    int f23a;
    MyAdapter adapter;
    DeviceList allDeviceList;
    Bitmap bit;
    ArrayList<Bitmap> bitMaplist;
    Bitmap bitmap;
    int bitmapheight;
    int bitmapsmlheight;
    int bitmapsmlwidth;
    int bitmapwidth;
    int delay_hue;
    byte[] deviceParmats;
    private ArrayList<Device> devicelist;
    int effect_hue;
    byte[] effectpar;
    int f24i;
    int[] image;
    int[] image_ib3;
    int[] image_ib4;
    private String index;
    LayoutInflater inflater;
    private Area mArea;
    private BackUpParams mBackUpParams;
    private Button mChoosedeviceBackButton;
    private Button mChoosedeviceSaveButton;
    private Device mDevice;
    private ListView mList;
    private Scene mScene;
    ThreadGC mThreadGC;
    int mWea;
    Dialog mdialog;
    int newbs;
    int newls;
    int newrs;
    int newts;
    private Paint paint;
    private String photo;
    private String picturePath;
    ChangeBrightthread thread;
    Button time_sequence;

    /* renamed from: com.allin.activity.ChooseDeviceActivity.1 */
    class C01751 implements RecyclerListener {
        C01751() {
        }

        public void onMovedToScrapHeap(View view) {
        }
    }

    class BackUpParams {
        short address;
        short channelMask;
        byte deviceType;
        byte[] paramsData;
        byte subdevicetype;

        BackUpParams() {
            this.address = (short) -1;
            this.deviceType = (byte) 0;
            this.subdevicetype = (byte) 0;
            this.channelMask = (short) 0;
            this.paramsData = new byte[5];
        }

        public void ResetBackupParmas(Device device) {
            this.address = device.getDeviceAddress();
            this.deviceType = (byte) device.getDeviceType();
            this.subdevicetype = (byte) device.getSubDeviceType();
            this.channelMask = device.getChannelMark();
            this.paramsData[0] = device.getCurrentParams()[0];
            this.paramsData[1] = device.getCurrentParams()[1];
            this.paramsData[2] = device.getCurrentParams()[2];
            this.paramsData[3] = device.getCurrentParams()[3];
            this.paramsData[4] = device.getCurrentParams()[4];
        }
    }

    class ChangeBrightthread extends Thread {
        ChangeBrightthread() {
        }

        void StopChangeBrightthread() {
            ChooseDeviceActivity.this.ThreadRunning = false;
        }

        public void run() {
            while (ChooseDeviceActivity.this.ThreadRunning) {
                if (!(ChooseDeviceActivity.this.mDevice == null || ChooseDeviceActivity.this.mBackUpParams == null)) {
                    if (!(ChooseDeviceActivity.this.mBackUpParams.deviceType == ((byte) ChooseDeviceActivity.this.mDevice.getDeviceType()) && ChooseDeviceActivity.this.mBackUpParams.address == ChooseDeviceActivity.this.mDevice.getDeviceAddress() && ChooseDeviceActivity.this.mBackUpParams.subdevicetype == ((byte) ChooseDeviceActivity.this.mDevice.getSubDeviceType()) && ChooseDeviceActivity.this.mBackUpParams.channelMask == ChooseDeviceActivity.this.mDevice.getChannelMark() && ChooseDeviceActivity.this.mBackUpParams.paramsData[0] == ChooseDeviceActivity.this.mDevice.getCurrentParams()[0] && ChooseDeviceActivity.this.mBackUpParams.paramsData[1] == ChooseDeviceActivity.this.mDevice.getCurrentParams()[1] && ChooseDeviceActivity.this.mBackUpParams.paramsData[2] == ChooseDeviceActivity.this.mDevice.getCurrentParams()[2] && ChooseDeviceActivity.this.mBackUpParams.paramsData[3] == ChooseDeviceActivity.this.mDevice.getCurrentParams()[3] && ChooseDeviceActivity.this.mBackUpParams.paramsData[4] == ChooseDeviceActivity.this.mDevice.getCurrentParams()[4])) {
                        ChooseDeviceActivity.this.mBackUpParams.address = ChooseDeviceActivity.this.mDevice.getDeviceAddress();
                        ChooseDeviceActivity.this.mBackUpParams.deviceType = (byte) ChooseDeviceActivity.this.mDevice.getDeviceType();
                        ChooseDeviceActivity.this.mBackUpParams.subdevicetype = (byte) ChooseDeviceActivity.this.mDevice.getSubDeviceType();
                        ChooseDeviceActivity.this.mBackUpParams.channelMask = ChooseDeviceActivity.this.mDevice.getChannelMark();
                        ChooseDeviceActivity.this.mBackUpParams.paramsData[0] = ChooseDeviceActivity.this.mDevice.getCurrentParams()[0];
                        ChooseDeviceActivity.this.mBackUpParams.paramsData[1] = ChooseDeviceActivity.this.mDevice.getCurrentParams()[1];
                        ChooseDeviceActivity.this.mBackUpParams.paramsData[2] = ChooseDeviceActivity.this.mDevice.getCurrentParams()[2];
                        ChooseDeviceActivity.this.mBackUpParams.paramsData[3] = ChooseDeviceActivity.this.mDevice.getCurrentParams()[3];
                        ChooseDeviceActivity.this.mBackUpParams.paramsData[4] = ChooseDeviceActivity.this.mDevice.getCurrentParams()[4];
                        if (ChooseDeviceActivity.this.mDevice.getDeviceType() == (short) 1 || ChooseDeviceActivity.this.mDevice.getDeviceType() == (short) 97) {
                            if (ChooseDeviceActivity.this.mDevice.getCurrentParams()[0] == -64) {
                                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, ChooseDeviceActivity.this.mDevice.getDeviceAddress(), (short) 0, new byte[]{(byte) -64, ChooseDeviceActivity.this.mDevice.getCurrentParams()[1], ChooseDeviceActivity.this.mDevice.getCurrentParams()[2], ChooseDeviceActivity.this.mDevice.getCurrentParams()[3], ChooseDeviceActivity.this.mDevice.getCurrentParams()[4]}), ChooseDeviceActivity.this.mDevice.getGatewayMacAddr(), ChooseDeviceActivity.this.mDevice.getGatewayPassword(), ChooseDeviceActivity.this.mDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                            }
                        } else if (ChooseDeviceActivity.this.mDevice.getDeviceType() == (short) 4 || ChooseDeviceActivity.this.mDevice.getDeviceType() == (short) 100) {
                            if (ChooseDeviceActivity.this.mDevice.getCurrentParams()[0] == 17) {
                                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, ChooseDeviceActivity.this.mDevice.getDeviceAddress(), (short) 0, new byte[]{(byte) 17, ChooseDeviceActivity.this.mDevice.getCurrentParams()[1], ChooseDeviceActivity.this.mDevice.getCurrentParams()[2], (byte) 0, (byte) ChooseDeviceActivity.this.mDevice.getChannelMark()}), ChooseDeviceActivity.this.mDevice.getGatewayMacAddr(), ChooseDeviceActivity.this.mDevice.getGatewayPassword(), ChooseDeviceActivity.this.mDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                                DatabaseManager.getInstance().updateDevice(ChooseDeviceActivity.this.mDevice);
                            }
                        } else if (ChooseDeviceActivity.this.mDevice.getDeviceType() == (short) 3 || ChooseDeviceActivity.this.mDevice.getDeviceType() == (short) 99) {
                            if (ChooseDeviceActivity.this.mDevice.getCurrentParams()[0] == 17) {
                                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, ChooseDeviceActivity.this.mDevice.getDeviceAddress(), (short) 0, new byte[]{(byte) 17, ChooseDeviceActivity.this.mDevice.getCurrentParams()[1], (byte) 0, (byte) 0, (byte) 0}), ChooseDeviceActivity.this.mDevice.getGatewayMacAddr(), ChooseDeviceActivity.this.mDevice.getGatewayPassword(), ChooseDeviceActivity.this.mDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                                DatabaseManager.getInstance().updateDevice(ChooseDeviceActivity.this.mDevice);
                            } else if (ChooseDeviceActivity.this.mDevice.getCurrentParams()[0] == -63 && ChooseDeviceActivity.this.mDevice.getCurrentParams()[1] == (byte) 3 && ChooseDeviceActivity.this.mDevice.getCurrentParams()[4] > (byte) 1) {
                                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, ChooseDeviceActivity.this.mDevice.getDeviceAddress(), (short) 0, new byte[]{(byte) 17, ChooseDeviceActivity.this.mDevice.getCurrentParams()[4], (byte) 0, (byte) 0, (byte) 0}), ChooseDeviceActivity.this.mDevice.getGatewayMacAddr(), ChooseDeviceActivity.this.mDevice.getGatewayPassword(), ChooseDeviceActivity.this.mDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                                DatabaseManager.getInstance().updateDevice(ChooseDeviceActivity.this.mDevice);
                            }
                        }
                    }
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class MyAdapter extends BaseAdapter {

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.10 */
        class AnonymousClass10 implements OnClickListener {
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$mDevice_pic_tick;

            AnonymousClass10(int i, ImageView imageView) {
                this.val$arg0 = i;
                this.val$mDevice_pic_tick = imageView;
            }

            public void onClick(View v) {
                Device IsChooseDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                boolean ischoose = IsChooseDevice.isIschoose();
                String ss = IsChooseDevice.getDeviceName();
                if (ischoose) {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (int i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(ss)) {
                                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.remove(i);
                            }
                        }
                    }
                    IsChooseDevice.setIschoose(false);
                    return;
                }
                this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.add(IsChooseDevice);
                IsChooseDevice.setIschoose(true);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.11 */
        class AnonymousClass11 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageView val$imageView4;

            AnonymousClass11(Device device, ImageView imageView) {
                this.val$LoadDevice = device;
                this.val$imageView4 = imageView;
            }

            public void onClick(View v) {
                byte[] CurrParmart;
                byte[] switchparams = new byte[5];
                if (this.val$LoadDevice.isIsopen()) {
                    if ((ChooseDeviceActivity.this.index != null && ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) || ChooseDeviceActivity.this.mArea != null) {
                        this.val$LoadDevice.setCurrentParams(new byte[]{switchparams[0], switchparams[1], switchparams[2], switchparams[3], new byte[]{(byte) 17, (byte) 100, (byte) 0, (byte) 0, (byte) this.val$LoadDevice.getChannelMark()}[4]});
                        this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, switchparams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                        this.val$LoadDevice.setIsopen(true);
                    } else if (ChooseDeviceActivity.this.mScene != null) {
                        CurrParmart = new byte[]{switchparams[0], switchparams[1], switchparams[2], switchparams[3], new byte[]{(byte) 17, (byte) 100, (byte) 0, (byte) 0, (byte) this.val$LoadDevice.getChannelMark()}[4]};
                        this.val$LoadDevice.setCurrentParams(CurrParmart);
                        this.val$LoadDevice.setSceneParams(CurrParmart);
                        this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, switchparams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                        this.val$LoadDevice.setIsopen(true);
                    }
                    this.val$LoadDevice.setIsopen(false);
                } else {
                    if ((ChooseDeviceActivity.this.index != null && ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) || ChooseDeviceActivity.this.mArea != null) {
                        this.val$LoadDevice.setCurrentParams(new byte[]{switchparams[0], switchparams[1], switchparams[2], switchparams[3], new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) this.val$LoadDevice.getChannelMark()}[4]});
                        this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, switchparams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                        this.val$LoadDevice.setIsopen(true);
                    } else if (ChooseDeviceActivity.this.mScene != null) {
                        CurrParmart = new byte[]{switchparams[0], switchparams[1], switchparams[2], switchparams[3], new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) this.val$LoadDevice.getChannelMark()}[4]};
                        this.val$LoadDevice.setCurrentParams(CurrParmart);
                        this.val$LoadDevice.setSceneParams(CurrParmart);
                        this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, switchparams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                        this.val$LoadDevice.setIsopen(true);
                    }
                    this.val$LoadDevice.setIsopen(true);
                }
                DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.12 */
        class AnonymousClass12 implements OnClickListener {
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$mDevice_pic_tick;

            AnonymousClass12(int i, ImageView imageView) {
                this.val$arg0 = i;
                this.val$mDevice_pic_tick = imageView;
            }

            public void onClick(View v) {
                boolean ischoose = ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).isIschoose();
                String ss = ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).getDeviceName();
                if (ischoose) {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    for (int i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                        if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(ss)) {
                            ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.remove(i);
                        }
                    }
                    ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).setIschoose(false);
                    return;
                }
                this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.add((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0));
                ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).setIschoose(true);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.13 */
        class AnonymousClass13 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            /*
            private final /* synthetic  ImageButton val$ib3;
            private final /* synthetic  ImageButton val$ib4;
            private final /* synthetic  ImageButton val$ib5;
            */
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;

            AnonymousClass13(Device device, ImageView imageView, SeekBar seekBar/*, ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3*/) {
                this.val$LoadDevice = device;
                this.val$image_switch = imageView;
                this.val$seekBar = seekBar;


                byte nn = DatabaseManager.getInstance().getlightingofDevice((Device) this.val$LoadDevice)[1];

                if (DatabaseManager.getInstance().getlightingofDevice((Device) this.val$LoadDevice)[1] == 0)
                {
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                }else
                {
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$seekBar.setProgress(nn);
                }

                /*
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
                */
            }

            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                List<WeekViewEvent> events = DataManager.getInstance().getevents();
                if (events.size()!=0)
                {
                    for (int i = 0; i< events.size(); i++)
                    {
                        WeekViewEvent event = events.get(i);
                        Calendar starttime = event.getStartTime();
                        Calendar finishtime = event.getEndTime();
                        if (calendar.after(starttime)&&calendar.before(finishtime))
                        {
                            ArrayList<Device> devicelist = event.getdeviceList();
                            for (int j =0 ; j < devicelist.size(); j++)
                            {
                                if (devicelist.get(j).getDeviceIndex()== this.val$LoadDevice.getDeviceIndex())
                                {
                                    devicelist.remove(j);
                                }
                            }
                            if (devicelist.isEmpty())events.remove(i);
                        }
                    }
                }
                DataManager.getInstance().setevents(events);

                byte[] data;
                if (this.val$LoadDevice.isClick()) {
                    /*
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                    this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[0]));
                    */
                    this.val$LoadDevice.setIsstart(true);
                    this.val$LoadDevice.setClick(false);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                    data = new byte[]{(byte) 17, (byte) 0, this.val$LoadDevice.getCurrentParams()[2], (byte) 0, (byte) 0};
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, data), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setCurrentParams(data);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        this.val$LoadDevice.setSceneParams(data);
                    }
                    this.val$seekBar.setProgress(0);
                } else {
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    data = new byte[]{(byte) 17, (byte) 100, this.val$LoadDevice.getCurrentParams()[2], (byte) 0, (byte) 0};
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, data), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setCurrentParams(data);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        this.val$LoadDevice.setSceneParams(data);
                    }
                    this.val$seekBar.setProgress(100);
                    //Toast.makeText(getApplicationContext(), "11111111111" + val$LoadDevice.getGatewayMacAddr().toString(), Toast.LENGTH_SHORT).show();
                }
                DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);

            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.14 */
        class AnonymousClass14 implements OnSeekBarChangeListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ int val$arg0;
            /*
            private final /* synthetic ImageButton val$ib3;
            private final /* synthetic ImageButton val$ib4;
            private final /* synthetic ImageButton val$ib5;
            */
            private final /* synthetic */ ImageView val$image_switch;

            AnonymousClass14(Device device, ImageView imageView /*, ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3*/, int i) {
                this.val$LoadDevice = device;
                this.val$image_switch = imageView;
                /*
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
                */
                this.val$arg0 = i;

            }

            public void onStopTrackingTouch(SeekBar arg0) {
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onProgressChanged(SeekBar i, int arg1, boolean arg2) {
                if (arg2) {
                    byte[] SetParams = new byte[5];
                    if (arg1 == 0) {
                        this.val$LoadDevice.setClick(false);
                        this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        SetParams[0] = (byte) 17;
                        SetParams[1] = (byte) arg1;
                        SetParams[2] = (byte) 0;
                        SetParams[3] = (byte) 0;
                        SetParams[4] = (byte) 0;
                    } else {
                        this.val$LoadDevice.setClick(true);
                        this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        if (this.val$LoadDevice.getCurrentParams()[0] == -63 && this.val$LoadDevice.getCurrentParams()[1] == (byte) 3) {
                            SetParams[0] = (byte) -63;
                            SetParams[1] = (byte) 3;
                            SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                            SetParams[3] = this.val$LoadDevice.getCurrentParams()[3];
                            SetParams[4] = (byte) arg1;
                        } else {
                            /*
                            this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                            this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                            this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[0]));
                            */
                            this.val$LoadDevice.setIsstart(true);
                            SetParams[0] = (byte) 17;
                            SetParams[1] = (byte) arg1;
                            SetParams[2] = (byte) 0;
                            SetParams[3] = (byte) 0;
                            SetParams[4] = (byte) 0;
                        }
                    }

                    ChooseDeviceActivity.this.mDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                    ChooseDeviceActivity.this.mDevice.setCurrentParams(SetParams);
                    this.val$LoadDevice.setCurrentParams(SetParams);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        ChooseDeviceActivity.this.mDevice.setSceneParams(SetParams);
                        this.val$LoadDevice.setSceneParams(SetParams);
                    }
                    DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);

                }
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.15 */
        class AnonymousClass15 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;

            /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.15.1 */
            class C01761 implements OnItemClickListener {
                private final /* synthetic */ Device val$LoadDevice;
                private final /* synthetic */ ImageButton val$ib3;
                private final /* synthetic */ ImageButton val$ib5;
                private final /* synthetic */ ImageView val$image_switch;
                private final /* synthetic */ SeekBar val$seekBar;

                C01761(ImageButton imageButton, Device device, SeekBar seekBar, ImageView imageView, ImageButton imageButton2) {
                    this.val$ib3 = imageButton;
                    this.val$LoadDevice = device;
                    this.val$seekBar = seekBar;
                    this.val$image_switch = imageView;
                    this.val$ib5 = imageButton2;
                }

                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    this.val$ib3.setImageResource(ChooseDeviceActivity.this.image_ib4[arg2]);
                    byte[] SetParams = new byte[5];
                    SetParams[0] = (byte) -63;
                    if (arg2 == 0) {
                        SetParams[1] = (byte) 1;
                    } else if (arg2 == 1) {
                        SetParams[1] = (byte) 4;
                    } else if (arg2 == 2) {
                        SetParams[1] = (byte) 5;
                    }
                    SetParams[3] = (byte) 1;
                    SetParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    if (this.val$LoadDevice.getCurrentParams()[0] == -63) {
                        SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                    } else {
                        SetParams[2] = (byte) 1;
                    }
                    byte parb = SetParams[2];
                    Device device = this.val$LoadDevice;
                    byte[] bArr = new byte[5];
                    bArr[0] = (byte) -63;
                    bArr[1] = (byte) (arg2 + 1);
                    bArr[2] = parb;
                    bArr[3] = (byte) 1;
                    device.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = (byte) (arg2 + 1);
                        bArr[2] = parb;
                        bArr[3] = (byte) 1;
                        device.setSceneParams(bArr);
                    }
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, SetParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$seekBar.setProgress(100);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    if (this.val$LoadDevice.isIsstart()) {
                        this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                        this.val$LoadDevice.setIsstart(false);
                    }
                    DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
                    if (ChooseDeviceActivity.this.mdialog != null && ChooseDeviceActivity.this.mdialog.isShowing()) {
                        ChooseDeviceActivity.this.mdialog.cancel();
                    }
                }
            }

            AnonymousClass15(ImageButton imageButton, Device device, SeekBar seekBar, ImageView imageView, ImageButton imageButton2) {
                this.val$ib3 = imageButton;
                this.val$LoadDevice = device;
                this.val$seekBar = seekBar;
                this.val$image_switch = imageView;
                this.val$ib5 = imageButton2;
            }

            public void onClick(View v) {
                ArrayList<HashMap<String, Object>> arraylist = new ArrayList();
                String[] effect = ChooseDeviceActivity.this.getResources().getStringArray(R.array.hue_effect_color);
                for (int i = 0; i < effect.length; i++) {
                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap.put("imgItem", Integer.valueOf(ChooseDeviceActivity.this.image_ib4[i]));
                    hashMap.put("textItem", effect[i]);
                    arraylist.add(hashMap);
                }
                View view = ChooseDeviceActivity.this.inflater.inflate(R.layout.spinnerdialog, null);
                ChooseDeviceActivity.this.mdialog = new Dialog(ChooseDeviceActivity.this, R.style.Theme_dialog);
                ChooseDeviceActivity.this.mdialog.setContentView(view);
                ChooseDeviceActivity.this.mdialog.setCancelable(true);
                ChooseDeviceActivity.this.mdialog.setCanceledOnTouchOutside(false);
                ChooseDeviceActivity.this.mdialog.show();
                ListView list = (ListView) view.findViewById(R.id.listView1);
                list.setAdapter(new SimpleAdapter(ChooseDeviceActivity.this, arraylist, R.layout.spinner_item, new String[]{"imgItem", "textItem"}, new int[]{R.id.device_img, R.id.skjnjg}));
                list.setOnItemClickListener(new C01761(this.val$ib3, this.val$LoadDevice, this.val$seekBar, this.val$image_switch, this.val$ib5));
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.16 */
        class AnonymousClass16 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;

            /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.16.1 */
            class C01771 implements OnItemClickListener {
                private final /* synthetic */ Device val$LoadDevice;
                private final /* synthetic */ ImageButton val$ib4;
                private final /* synthetic */ ImageButton val$ib5;
                private final /* synthetic */ ImageView val$image_switch;
                private final /* synthetic */ SeekBar val$seekBar;

                C01771(ImageButton imageButton, Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton2) {
                    this.val$ib4 = imageButton;
                    this.val$LoadDevice = device;
                    this.val$image_switch = imageView;
                    this.val$seekBar = seekBar;
                    this.val$ib5 = imageButton2;
                }

                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    this.val$ib4.setImageResource(ChooseDeviceActivity.this.image[arg2]);
                    byte[] SetParams = new byte[5];
                    SetParams[0] = (byte) -63;
                    SetParams[2] = (byte) (arg2 + 1);
                    SetParams[3] = (byte) 1;
                    SetParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    if (this.val$LoadDevice.getCurrentParams()[0] == -63) {
                        SetParams[1] = this.val$LoadDevice.getCurrentParams()[1];
                    } else {
                        SetParams[1] = (byte) 1;
                    }
                    byte parb = SetParams[1];
                    Device device = this.val$LoadDevice;
                    byte[] bArr = new byte[5];
                    bArr[0] = (byte) -63;
                    bArr[1] = parb;
                    bArr[2] = (byte) (arg2 + 1);
                    bArr[3] = (byte) 1;
                    device.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = parb;
                        bArr[2] = (byte) (arg2 + 1);
                        bArr[3] = (byte) 1;
                        device.setSceneParams(bArr);
                    }
                    if (SetParams[1] == 0 || SetParams[1] == 1) {
                        SetParams[1] = (byte) 1;
                    } else if (SetParams[1] == 2) {
                        SetParams[1] = (byte) 4;
                    } else if (SetParams[1] == 3) {
                        SetParams[1] = (byte) 5;
                    }
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, SetParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageResource(R.drawable.open);
                    this.val$seekBar.setProgress(100);
                    if (this.val$LoadDevice.isIsstart()) {
                        this.val$ib5.setImageResource(R.drawable.hue_stop);
                        this.val$LoadDevice.setIsstart(false);
                    }
                    DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
                    if (ChooseDeviceActivity.this.mdialog != null && ChooseDeviceActivity.this.mdialog.isShowing()) {
                        ChooseDeviceActivity.this.mdialog.cancel();
                    }
                }
            }

            AnonymousClass16(ImageButton imageButton, Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton2) {
                this.val$ib4 = imageButton;
                this.val$LoadDevice = device;
                this.val$image_switch = imageView;
                this.val$seekBar = seekBar;
                this.val$ib5 = imageButton2;
            }

            public void onClick(View v) {
                ArrayList<HashMap<String, Object>> arraylist = new ArrayList();
                String[] interval = ChooseDeviceActivity.this.getResources().getStringArray(R.array.time_interval);
                for (int i = 0; i < interval.length; i++) {
                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap.put("imgItem", Integer.valueOf(ChooseDeviceActivity.this.image[i]));
                    hashMap.put("textItem", interval[i]);
                    arraylist.add(hashMap);
                }
                View view = ChooseDeviceActivity.this.inflater.inflate(R.layout.spinnerdialog, null);
                ChooseDeviceActivity.this.mdialog = new Dialog(ChooseDeviceActivity.this, R.style.Theme_dialog);
                ChooseDeviceActivity.this.mdialog.setContentView(view);
                ChooseDeviceActivity.this.mdialog.setCancelable(true);
                ChooseDeviceActivity.this.mdialog.setCanceledOnTouchOutside(false);
                ChooseDeviceActivity.this.mdialog.show();
                ListView list = (ListView) view.findViewById(R.id.listView1);
                list.setAdapter(new SimpleAdapter(ChooseDeviceActivity.this, arraylist, R.layout.spinner_item, new String[]{"imgItem", "textItem"}, new int[]{R.id.device_img, R.id.skjnjg}));
                list.setOnItemClickListener(new C01771(this.val$ib4, this.val$LoadDevice, this.val$image_switch, this.val$seekBar, this.val$ib5));
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.17 */
        class AnonymousClass17 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;

            AnonymousClass17(Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton) {
                this.val$LoadDevice = device;
                this.val$image_switch = imageView;
                this.val$seekBar = seekBar;
                this.val$ib5 = imageButton;
            }

            public void onClick(View v) {
                byte[] SetParams = new byte[5];
                SetParams[0] = (byte) -63;
                SetParams[4] = (byte) 0;
                Device device;
                byte[] bArr;
                if (this.val$LoadDevice.isIsstart()) {
                    if (this.val$LoadDevice.getCurrentParams()[0] == -63) {
                        SetParams[1] = this.val$LoadDevice.getCurrentParams()[1];
                        SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                        SetParams[3] = (byte) 1;
                    } else {
                        SetParams[1] = (byte) 1;
                        SetParams[2] = (byte) 1;
                        SetParams[3] = (byte) 1;
                    }
                    device = this.val$LoadDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) -63;
                    bArr[1] = SetParams[1];
                    bArr[2] = SetParams[2];
                    bArr[3] = (byte) 1;
                    device.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = SetParams[1];
                        bArr[2] = SetParams[2];
                        bArr[3] = (byte) 1;
                        device.setSceneParams(bArr);
                    }
                    if (SetParams[1] == 0 || SetParams[1] == (byte) 1) {
                        SetParams[1] = (byte) 1;
                    } else if (SetParams[1] == (byte) 2) {
                        SetParams[1] = (byte) 4;
                    } else if (SetParams[1] == (byte) 3) {
                        SetParams[1] = (byte) 5;
                    }
                    SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                    SetParams[3] = (byte) 1;
                    SetParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, SetParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$seekBar.setProgress(100);
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                    this.val$LoadDevice.setIsstart(false);
                } else {
                    if (this.val$LoadDevice.getCurrentParams()[0] == -63) {
                        SetParams[1] = this.val$LoadDevice.getCurrentParams()[1];
                        SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                        SetParams[3] = (byte) 0;
                    } else {
                        SetParams[1] = (byte) 1;
                        SetParams[2] = (byte) 1;
                        SetParams[3] = (byte) 0;
                    }
                    device = this.val$LoadDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) -63;
                    bArr[1] = SetParams[1];
                    bArr[2] = SetParams[2];
                    device.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = SetParams[1];
                        bArr[2] = SetParams[2];
                        device.setSceneParams(bArr);
                    }
                    if (SetParams[1] == 0 || SetParams[1] == (byte) 1) {
                        SetParams[1] = (byte) 1;
                    } else if (SetParams[1] == (byte) 2) {
                        SetParams[1] = (byte) 4;
                    } else if (SetParams[1] == (byte) 3) {
                        SetParams[1] = (byte) 5;
                    }
                    SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                    SetParams[3] = (byte) 0;
                    SetParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, SetParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$seekBar.setProgress(100);
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$LoadDevice.setIsstart(true);
                }
                DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.18 */
        class AnonymousClass18 implements OnClickListener {
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$mDevice_pic_tick;

            AnonymousClass18(int i, ImageView imageView) {
                this.val$arg0 = i;
                this.val$mDevice_pic_tick = imageView;
            }

            public void onClick(View v) {
                Device LoadDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                boolean ischoose = LoadDevice.isIschoose();
                String ss = LoadDevice.getDeviceName();
                if (ischoose) {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    for (int i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                        if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(ss)) {
                            ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.remove(i);
                        }
                    }
                    LoadDevice.setIschoose(false);
                    return;
                }
                this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.add(LoadDevice);
                LoadDevice.setIschoose(true);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.19 */
        class AnonymousClass19 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;
            private final /* synthetic */ SeekBar val$seekBar2;

            AnonymousClass19(Device device, ImageView imageView, SeekBar seekBar, SeekBar seekBar2, ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3) {
                this.val$LoadDevice = device;
                this.val$image_switch = imageView;
                this.val$seekBar = seekBar;
                this.val$seekBar2 = seekBar2;
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
            }

            public void onClick(View v) {
                byte[] data;
                if (this.val$LoadDevice.isClick()) {
                    this.val$LoadDevice.setClick(false);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                    data = new byte[]{(byte) 17, (byte) 0, this.val$LoadDevice.getCurrentParams()[2], (byte) 0, (byte) 0};
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, data), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setCurrentParams(data);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        this.val$LoadDevice.setSceneParams(data);
                    }
                    this.val$seekBar.setProgress(0);
                    this.val$seekBar2.setProgress(0);
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$LoadDevice.setIsstart(true);
                    this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                    this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[0]));
                } else {
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    data = new byte[]{(byte) 17, (byte) 100, this.val$LoadDevice.getCurrentParams()[2], (byte) 0, (byte) 0};
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, data), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setCurrentParams(data);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        this.val$LoadDevice.setSceneParams(data);
                    }
                    this.val$seekBar.setProgress(100);
                }
                DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.1 */
        class C01781 implements OnSeekBarChangeListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$button4;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;

            C01781(int i, ImageButton imageButton, Device device, ImageButton imageButton2, ImageButton imageButton3, ImageView imageView) {
                this.val$arg0 = i;
                this.val$ib5 = imageButton;
                this.val$LoadDevice = device;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
                this.val$button4 = imageView;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    ChooseDeviceActivity.this.mDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                    byte[] params = new byte[5];
                    if (ChooseDeviceActivity.this.mDevice.getCurrentParams()[0] == -64) {
                        params[0] = (byte) -64;
                        params[1] = ChooseDeviceActivity.this.mDevice.getCurrentParams()[1];
                        params[2] = ChooseDeviceActivity.this.mDevice.getCurrentParams()[2];
                        params[3] = ChooseDeviceActivity.this.mDevice.getCurrentParams()[3];
                        params[4] = (byte) progress;
                    } else {
                        this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                        this.val$LoadDevice.setIsstart(true);
                        this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                        this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[0]));
                        if (ChooseDeviceActivity.this.newls > 0 && ChooseDeviceActivity.this.newls <= (ChooseDeviceActivity.this.bitmapwidth - ChooseDeviceActivity.this.bitmapsmlwidth) - 1 && ChooseDeviceActivity.this.newts > 0 && ChooseDeviceActivity.this.newts <= (ChooseDeviceActivity.this.bitmapheight - ChooseDeviceActivity.this.bitmapsmlheight) - 1) {
                            ChooseDeviceActivity access$0;
                            int pixel = ChooseDeviceActivity.this.bitmap.getPixel(ChooseDeviceActivity.this.newls, ChooseDeviceActivity.this.newts);
                            int RR = Color.red(pixel);
                            int G = Color.green(pixel);
                            int B = Color.blue(pixel);
                            ChooseDeviceActivity.this.R_1 = RR;
                            ChooseDeviceActivity.this.G_1 = G;
                            ChooseDeviceActivity.this.B_1 = B;
                            if (ChooseDeviceActivity.this.R_1 < 0) {
                                access$0 = ChooseDeviceActivity.this;
                                access$0.R_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                            }
                            if (ChooseDeviceActivity.this.G_1 < 0) {
                                access$0 = ChooseDeviceActivity.this;
                                access$0.G_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                            }
                            if (ChooseDeviceActivity.this.B_1 < 0) {
                                access$0 = ChooseDeviceActivity.this;
                                access$0.B_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                            }
                        }
                        params[0] = (byte) -64;
                        params[1] = (byte) ChooseDeviceActivity.this.R_1;
                        params[2] = (byte) ChooseDeviceActivity.this.G_1;
                        params[3] = (byte) ChooseDeviceActivity.this.B_1;
                        params[4] = (byte) progress;
                    }
                    ChooseDeviceActivity.this.mDevice.setCurrentParams(params);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        ChooseDeviceActivity.this.mDevice.setSceneParams(params);
                    }
                    DatabaseManager.getInstance().updateDevice(ChooseDeviceActivity.this.mDevice);
                    if (progress == 0) {
                        this.val$LoadDevice.setClick(false);
                        this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        return;
                    }
                    this.val$LoadDevice.setClick(true);
                    this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                }
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.20 */
        class AnonymousClass20 implements OnSeekBarChangeListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar1;

            AnonymousClass20(ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3, Device device, int i, SeekBar seekBar, ImageView imageView) {
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
                this.val$LoadDevice = device;
                this.val$arg0 = i;
                this.val$seekBar1 = seekBar;
                this.val$image_switch = imageView;
            }

            public void onStopTrackingTouch(SeekBar arg0) {
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onProgressChanged(SeekBar i, int arg1, boolean arg2) {
                if (arg2) {
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                    this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[0]));
                    this.val$LoadDevice.setIsstart(true);
                    ChooseDeviceActivity.this.mDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                    Device access$5 = ChooseDeviceActivity.this.mDevice;
                    byte[] bArr = new byte[5];
                    bArr[0] = (byte) 17;
                    bArr[1] = (byte) arg1;
                    bArr[2] = (byte) (100 - this.val$seekBar1.getProgress());
                    access$5.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        access$5 = ChooseDeviceActivity.this.mDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) 17;
                        bArr[1] = (byte) arg1;
                        bArr[2] = (byte) (100 - this.val$seekBar1.getProgress());
                        access$5.setSceneParams(bArr);
                    }
                    if (arg1 == 0) {
                        this.val$LoadDevice.setClick(false);
                        this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        return;
                    }
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                }
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.21 */
        class AnonymousClass21 implements OnSeekBarChangeListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ SeekBar val$seekBar;

            AnonymousClass21(ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3, Device device, int i, SeekBar seekBar) {
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
                this.val$LoadDevice = device;
                this.val$arg0 = i;
                this.val$seekBar = seekBar;
            }

            public void onStopTrackingTouch(SeekBar arg0) {
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onProgressChanged(SeekBar i, int arg1, boolean arg2) {
                if (arg2) {
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                    this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[0]));
                    this.val$LoadDevice.setIsstart(true);
                    ChooseDeviceActivity.this.mDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                    Device access$5 = ChooseDeviceActivity.this.mDevice;
                    byte[] bArr = new byte[5];
                    bArr[0] = (byte) 17;
                    bArr[1] = (byte) this.val$seekBar.getProgress();
                    bArr[2] = (byte) (100 - arg1);
                    access$5.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        access$5 = ChooseDeviceActivity.this.mDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) 17;
                        bArr[1] = (byte) this.val$seekBar.getProgress();
                        bArr[2] = (byte) (100 - arg1);
                        access$5.setSceneParams(bArr);
                    }
                }
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.22 */
        class AnonymousClass22 implements OnSeekBarChangeListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;
            private final /* synthetic */ SeekBar val$seekBar1;

            AnonymousClass22(ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3, Device device, int i, SeekBar seekBar, SeekBar seekBar2, ImageView imageView) {
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
                this.val$LoadDevice = device;
                this.val$arg0 = i;
                this.val$seekBar = seekBar;
                this.val$seekBar1 = seekBar2;
                this.val$image_switch = imageView;
            }

            public void onStopTrackingTouch(SeekBar arg0) {
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onProgressChanged(SeekBar i, int arg1, boolean arg2) {
                if (arg2) {
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                    this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[0]));
                    this.val$LoadDevice.setIsstart(true);
                    ChooseDeviceActivity.this.mDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                    Device access$5 = ChooseDeviceActivity.this.mDevice;
                    byte[] bArr = new byte[5];
                    bArr[0] = (byte) 17;
                    bArr[1] = (byte) arg1;
                    bArr[2] = (byte) (100 - arg1);
                    access$5.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        access$5 = ChooseDeviceActivity.this.mDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) 17;
                        bArr[1] = (byte) arg1;
                        bArr[2] = (byte) (100 - arg1);
                        access$5.setSceneParams(bArr);
                    }
                    this.val$seekBar.setProgress(arg1);
                    this.val$seekBar1.setProgress(arg1);
                    if (arg1 == 0) {
                        this.val$LoadDevice.setClick(false);
                        this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        return;
                    }
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                }
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.23 */
        class AnonymousClass23 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;
            private final /* synthetic */ SeekBar val$seekBar1;
            private final /* synthetic */ SeekBar val$seekBar2;

            /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.23.1 */
            class C01791 implements OnItemClickListener {
                private final /* synthetic */ Device val$LoadDevice;
                private final /* synthetic */ ImageButton val$ib3;
                private final /* synthetic */ ImageButton val$ib5;
                private final /* synthetic */ ImageView val$image_switch;
                private final /* synthetic */ SeekBar val$seekBar;
                private final /* synthetic */ SeekBar val$seekBar1;
                private final /* synthetic */ SeekBar val$seekBar2;

                C01791(ImageButton imageButton, Device device, SeekBar seekBar, ImageView imageView, ImageButton imageButton2, SeekBar seekBar2, SeekBar seekBar3) {
                    this.val$ib3 = imageButton;
                    this.val$LoadDevice = device;
                    this.val$seekBar = seekBar;
                    this.val$image_switch = imageView;
                    this.val$ib5 = imageButton2;
                    this.val$seekBar1 = seekBar2;
                    this.val$seekBar2 = seekBar3;
                }

                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    this.val$ib3.setImageResource(ChooseDeviceActivity.this.image_ib4[arg2]);
                    byte[] SetParams = new byte[5];
                    SetParams[0] = (byte) -63;
                    if (arg2 == 0) {
                        SetParams[1] = (byte) 1;
                    } else if (arg2 == 1) {
                        SetParams[1] = (byte) 4;
                    } else if (arg2 == 2) {
                        SetParams[1] = (byte) 5;
                    }
                    SetParams[3] = (byte) 1;
                    SetParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    if (this.val$LoadDevice.getCurrentParams()[0] == -63) {
                        SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                    } else {
                        SetParams[2] = (byte) 1;
                    }
                    byte parb = SetParams[2];
                    Device device = this.val$LoadDevice;
                    byte[] bArr = new byte[5];
                    bArr[0] = (byte) -63;
                    bArr[1] = (byte) (arg2 + 1);
                    bArr[2] = parb;
                    bArr[3] = (byte) 1;
                    device.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = (byte) (arg2 + 1);
                        bArr[2] = parb;
                        bArr[3] = (byte) 1;
                        device.setSceneParams(bArr);
                    }
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, SetParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$seekBar.setProgress(100);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    if (this.val$LoadDevice.isIsstart()) {
                        this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                        this.val$LoadDevice.setIsstart(false);
                    }
                    DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
                    this.val$seekBar1.setProgress(0);
                    this.val$seekBar2.setProgress(0);
                    if (ChooseDeviceActivity.this.mdialog != null && ChooseDeviceActivity.this.mdialog.isShowing()) {
                        ChooseDeviceActivity.this.mdialog.cancel();
                    }
                }
            }

            AnonymousClass23(ImageButton imageButton, Device device, SeekBar seekBar, ImageView imageView, ImageButton imageButton2, SeekBar seekBar2, SeekBar seekBar3) {
                this.val$ib3 = imageButton;
                this.val$LoadDevice = device;
                this.val$seekBar = seekBar;
                this.val$image_switch = imageView;
                this.val$ib5 = imageButton2;
                this.val$seekBar1 = seekBar2;
                this.val$seekBar2 = seekBar3;
            }

            public void onClick(View v) {
                ArrayList<HashMap<String, Object>> arraylist = new ArrayList();
                String[] effect = ChooseDeviceActivity.this.getResources().getStringArray(R.array.hue_effect_color);
                for (int i = 0; i < effect.length; i++) {
                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap.put("imgItem", Integer.valueOf(ChooseDeviceActivity.this.image_ib4[i]));
                    hashMap.put("textItem", effect[i]);
                    arraylist.add(hashMap);
                }
                View view = ChooseDeviceActivity.this.inflater.inflate(R.layout.spinnerdialog, null);
                ChooseDeviceActivity.this.mdialog = new Dialog(ChooseDeviceActivity.this, R.style.Theme_dialog);
                ChooseDeviceActivity.this.mdialog.setContentView(view);
                ChooseDeviceActivity.this.mdialog.setCancelable(true);
                ChooseDeviceActivity.this.mdialog.setCanceledOnTouchOutside(false);
                ChooseDeviceActivity.this.mdialog.show();
                ListView list = (ListView) view.findViewById(R.id.listView1);
                ListView listView = list;
                listView.setAdapter(new SimpleAdapter(ChooseDeviceActivity.this, arraylist, R.layout.spinner_item, new String[]{"imgItem", "textItem"}, new int[]{R.id.device_img, R.id.skjnjg}));
                list.setOnItemClickListener(new C01791(this.val$ib3, this.val$LoadDevice, this.val$seekBar, this.val$image_switch, this.val$ib5, this.val$seekBar1, this.val$seekBar2));
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.24 */
        class AnonymousClass24 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;
            private final /* synthetic */ SeekBar val$seekBar1;
            private final /* synthetic */ SeekBar val$seekBar2;

            /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.24.1 */
            class C01801 implements OnItemClickListener {
                private final /* synthetic */ Device val$LoadDevice;
                private final /* synthetic */ ImageButton val$ib4;
                private final /* synthetic */ ImageButton val$ib5;
                private final /* synthetic */ ImageView val$image_switch;
                private final /* synthetic */ SeekBar val$seekBar;
                private final /* synthetic */ SeekBar val$seekBar1;
                private final /* synthetic */ SeekBar val$seekBar2;

                C01801(ImageButton imageButton, Device device, ImageView imageView, SeekBar seekBar, SeekBar seekBar2, SeekBar seekBar3, ImageButton imageButton2) {
                    this.val$ib4 = imageButton;
                    this.val$LoadDevice = device;
                    this.val$image_switch = imageView;
                    this.val$seekBar = seekBar;
                    this.val$seekBar1 = seekBar2;
                    this.val$seekBar2 = seekBar3;
                    this.val$ib5 = imageButton2;
                }

                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    this.val$ib4.setImageResource(ChooseDeviceActivity.this.image[arg2]);
                    byte[] SetParams = new byte[5];
                    SetParams[0] = (byte) -63;
                    SetParams[2] = (byte) (arg2 + 1);
                    SetParams[3] = (byte) 1;
                    SetParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    if (this.val$LoadDevice.getCurrentParams()[0] == -63) {
                        SetParams[1] = this.val$LoadDevice.getCurrentParams()[1];
                    } else {
                        SetParams[1] = (byte) 1;
                    }
                    byte parb = SetParams[1];
                    Device device = this.val$LoadDevice;
                    byte[] bArr = new byte[5];
                    bArr[0] = (byte) -63;
                    bArr[1] = parb;
                    bArr[2] = (byte) (arg2 + 1);
                    bArr[3] = (byte) 1;
                    device.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = parb;
                        bArr[2] = (byte) (arg2 + 1);
                        bArr[3] = (byte) 1;
                        device.setSceneParams(bArr);
                    }
                    if (SetParams[1] == 0 || SetParams[1] == 1) {
                        SetParams[1] = (byte) 1;
                    } else if (SetParams[1] == 2) {
                        SetParams[1] = (byte) 4;
                    } else if (SetParams[1] == 3) {
                        SetParams[1] = (byte) 5;
                    }
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, SetParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageResource(R.drawable.open);
                    this.val$seekBar.setProgress(100);
                    this.val$seekBar1.setProgress(0);
                    this.val$seekBar2.setProgress(0);
                    if (this.val$LoadDevice.isIsstart()) {
                        this.val$ib5.setImageResource(R.drawable.hue_stop);
                        this.val$LoadDevice.setIsstart(false);
                    }
                    DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
                    if (ChooseDeviceActivity.this.mdialog != null && ChooseDeviceActivity.this.mdialog.isShowing()) {
                        ChooseDeviceActivity.this.mdialog.cancel();
                    }
                }
            }

            AnonymousClass24(ImageButton imageButton, Device device, ImageView imageView, SeekBar seekBar, SeekBar seekBar2, SeekBar seekBar3, ImageButton imageButton2) {
                this.val$ib4 = imageButton;
                this.val$LoadDevice = device;
                this.val$image_switch = imageView;
                this.val$seekBar = seekBar;
                this.val$seekBar1 = seekBar2;
                this.val$seekBar2 = seekBar3;
                this.val$ib5 = imageButton2;
            }

            public void onClick(View v) {
                ArrayList<HashMap<String, Object>> arraylist = new ArrayList();
                String[] interval = ChooseDeviceActivity.this.getResources().getStringArray(R.array.time_interval);
                for (int i = 0; i < interval.length; i++) {
                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap.put("imgItem", Integer.valueOf(ChooseDeviceActivity.this.image[i]));
                    hashMap.put("textItem", interval[i]);
                    arraylist.add(hashMap);
                }
                View view = ChooseDeviceActivity.this.inflater.inflate(R.layout.spinnerdialog, null);
                ChooseDeviceActivity.this.mdialog = new Dialog(ChooseDeviceActivity.this, R.style.Theme_dialog);
                ChooseDeviceActivity.this.mdialog.setContentView(view);
                ChooseDeviceActivity.this.mdialog.setCancelable(true);
                ChooseDeviceActivity.this.mdialog.setCanceledOnTouchOutside(false);
                ChooseDeviceActivity.this.mdialog.show();
                ListView list = (ListView) view.findViewById(R.id.listView1);
                ListView listView = list;
                listView.setAdapter(new SimpleAdapter(ChooseDeviceActivity.this, arraylist, R.layout.spinner_item, new String[]{"imgItem", "textItem"}, new int[]{R.id.device_img, R.id.skjnjg}));
                list.setOnItemClickListener(new C01801(this.val$ib4, this.val$LoadDevice, this.val$image_switch, this.val$seekBar, this.val$seekBar1, this.val$seekBar2, this.val$ib5));
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.25 */
        class AnonymousClass25 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$image_switch;
            private final /* synthetic */ SeekBar val$seekBar;
            private final /* synthetic */ SeekBar val$seekBar1;
            private final /* synthetic */ SeekBar val$seekBar2;

            AnonymousClass25(Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton, SeekBar seekBar2, SeekBar seekBar3) {
                this.val$LoadDevice = device;
                this.val$image_switch = imageView;
                this.val$seekBar = seekBar;
                this.val$ib5 = imageButton;
                this.val$seekBar1 = seekBar2;
                this.val$seekBar2 = seekBar3;
            }

            public void onClick(View v) {
                byte[] SetParams = new byte[5];
                SetParams[0] = (byte) -63;
                SetParams[4] = (byte) 0;
                Device device;
                byte[] bArr;
                if (this.val$LoadDevice.isIsstart()) {
                    if (this.val$LoadDevice.getCurrentParams()[0] == -63) {
                        SetParams[1] = this.val$LoadDevice.getCurrentParams()[1];
                        SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                        SetParams[3] = (byte) 1;
                    } else {
                        SetParams[1] = (byte) 1;
                        SetParams[2] = (byte) 1;
                        SetParams[3] = (byte) 1;
                    }
                    device = this.val$LoadDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) -63;
                    bArr[1] = SetParams[1];
                    bArr[2] = SetParams[2];
                    bArr[3] = (byte) 1;
                    device.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = SetParams[1];
                        bArr[2] = SetParams[2];
                        bArr[3] = (byte) 1;
                        device.setSceneParams(bArr);
                    }
                    if (SetParams[1] == 0 || SetParams[1] == (byte) 1) {
                        SetParams[1] = (byte) 1;
                    } else if (SetParams[1] == (byte) 2) {
                        SetParams[1] = (byte) 4;
                    } else if (SetParams[1] == (byte) 3) {
                        SetParams[1] = (byte) 5;
                    }
                    SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                    SetParams[3] = (byte) 1;
                    SetParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, SetParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$seekBar.setProgress(100);
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                    this.val$LoadDevice.setIsstart(false);
                    this.val$seekBar1.setProgress(0);
                    this.val$seekBar2.setProgress(0);
                } else {
                    if (this.val$LoadDevice.getCurrentParams()[0] == -63) {
                        SetParams[1] = this.val$LoadDevice.getCurrentParams()[1];
                        SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                        SetParams[3] = (byte) 0;
                    } else {
                        SetParams[1] = (byte) 1;
                        SetParams[2] = (byte) 1;
                        SetParams[3] = (byte) 0;
                    }
                    device = this.val$LoadDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) -63;
                    bArr[1] = SetParams[1];
                    bArr[2] = SetParams[2];
                    device.setCurrentParams(bArr);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = SetParams[1];
                        bArr[2] = SetParams[2];
                        device.setSceneParams(bArr);
                    }
                    if (SetParams[1] == 0 || SetParams[1] == (byte) 1) {
                        SetParams[1] = (byte) 1;
                    } else if (SetParams[1] == (byte) 2) {
                        SetParams[1] = (byte) 4;
                    } else if (SetParams[1] == (byte) 3) {
                        SetParams[1] = (byte) 5;
                    }
                    SetParams[2] = this.val$LoadDevice.getCurrentParams()[2];
                    SetParams[3] = (byte) 0;
                    SetParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, SetParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$seekBar.setProgress(100);
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$LoadDevice.setIsstart(true);
                    this.val$seekBar1.setProgress(0);
                    this.val$seekBar2.setProgress(0);
                }
                DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.26 */
        class AnonymousClass26 implements OnClickListener {
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$mDevice_pic_tick;

            AnonymousClass26(int i, ImageView imageView) {
                this.val$arg0 = i;
                this.val$mDevice_pic_tick = imageView;
            }

            public void onClick(View v) {
                Device IsChooseDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                boolean ischoose = IsChooseDevice.isIschoose();
                String ss = IsChooseDevice.getDeviceName();
                if (ischoose) {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (int i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(ss)) {
                                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.remove(i);
                            }
                        }
                    }
                    IsChooseDevice.setIschoose(false);
                    return;
                }
                this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.add(IsChooseDevice);
                IsChooseDevice.setIschoose(true);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.27 */
        class AnonymousClass27 implements OnClickListener {
            private final /* synthetic */ Device val$NorDevice;
            private final /* synthetic */ ImageView val$imageView4;

            AnonymousClass27(Device device, ImageView imageView) {
                this.val$NorDevice = device;
                this.val$imageView4 = imageView;
            }

            public void onClick(View v) {
                Device device;
                byte[] bArr;
                if (this.val$NorDevice.isIsopen()) {
                    this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 58;
                    device.setCurrentParams(bArr);
                    this.val$NorDevice.setIsopen(false);
                } else {
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 58;
                    bArr[1] = (byte) 1;
                    device.setCurrentParams(bArr);
                    this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$NorDevice.setIsopen(true);
                }
                byte[] params = new byte[]{(byte) ((this.val$NorDevice.getDeviceAddress() >> 8) & MotionEventCompat.ACTION_MASK), (byte) (this.val$NorDevice.getDeviceAddress() & MotionEventCompat.ACTION_MASK), (byte) 0};
                if (this.val$NorDevice.isIsopen()) {
                    params[2] = (byte) 1;
                } else {
                    params[2] = (byte) 0;
                }
                if (ChooseDeviceActivity.this.mScene != null) {
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 58;
                    bArr[1] = params[2];
                    device.setSceneParams(bArr);
                }
                DeviceSocket.getInstance().send(Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 58, (short) params.length, (short) 0, params), this.val$NorDevice.getGatewayMacAddr(), this.val$NorDevice.getGatewayPassword(), this.val$NorDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                DatabaseManager.getInstance().updateDevice(this.val$NorDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.28 */
        class AnonymousClass28 implements OnClickListener {
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$mDevice_pic_tick;

            AnonymousClass28(int i, ImageView imageView) {
                this.val$arg0 = i;
                this.val$mDevice_pic_tick = imageView;
            }

            public void onClick(View v) {
                Device IsChooseDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                boolean ischoose = IsChooseDevice.isIschoose();
                String ss = IsChooseDevice.getDeviceName();
                if (ischoose) {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (int i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(ss)) {
                                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.remove(i);
                            }
                        }
                    }
                    IsChooseDevice.setIschoose(false);
                    return;
                }
                this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.add(IsChooseDevice);
                IsChooseDevice.setIschoose(true);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.29 */
        class AnonymousClass29 implements OnClickListener {
            private final /* synthetic */ Device val$NorDevice;
            private final /* synthetic */ Button val$button1;
            private final /* synthetic */ Button val$button2;
            private final /* synthetic */ Button val$button3;

            AnonymousClass29(Device device, Button button, Button button2, Button button3) {
                this.val$NorDevice = device;
                this.val$button1 = button;
                this.val$button2 = button2;
                this.val$button3 = button3;
            }

            public void onClick(View v) {
                byte[] switchparams = new byte[]{(byte) 17, (byte) 1, (byte) 0, (byte) 0, (byte) this.val$NorDevice.getChannelMark()};
                this.val$button1.setTextColor(-10104851);
                this.val$button2.setTextColor(-9605779);
                this.val$button3.setTextColor(-9605779);
                this.val$NorDevice.setCurrentParams(switchparams);
                if (ChooseDeviceActivity.this.mScene != null) {
                    this.val$NorDevice.setSceneParams(switchparams);
                }
                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$NorDevice.getDeviceAddress(), (short) 0, switchparams), this.val$NorDevice.getGatewayMacAddr(), this.val$NorDevice.getGatewayPassword(), this.val$NorDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                DatabaseManager.getInstance().updateDevice(this.val$NorDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.2 */
        class C01812 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$mDevice_pic_tick;

            C01812(int i, ImageView imageView, Device device) {
                this.val$arg0 = i;
                this.val$mDevice_pic_tick = imageView;
                this.val$LoadDevice = device;
            }

            public void onClick(View v) {
                boolean ischoose = ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).isIschoose();
                String ss = ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).getDeviceName();
                if (ischoose) {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    for (int i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                        if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(ss)) {
                            ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.remove(i);
                        }
                    }
                    ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).setIschoose(false);
                } else {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                    ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.add((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0));
                    ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).setIschoose(true);
                }
                DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.30 */
        class AnonymousClass30 implements OnClickListener {
            private final /* synthetic */ Device val$NorDevice;
            private final /* synthetic */ Button val$button1;
            private final /* synthetic */ Button val$button2;
            private final /* synthetic */ Button val$button3;

            AnonymousClass30(Device device, Button button, Button button2, Button button3) {
                this.val$NorDevice = device;
                this.val$button1 = button;
                this.val$button2 = button2;
                this.val$button3 = button3;
            }

            public void onClick(View v) {
                byte[] switchparams = new byte[]{(byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) this.val$NorDevice.getChannelMark()};
                this.val$button1.setTextColor(-9605779);
                this.val$button2.setTextColor(-10104851);
                this.val$button3.setTextColor(-9605779);
                this.val$NorDevice.setCurrentParams(switchparams);
                if (ChooseDeviceActivity.this.mScene != null) {
                    this.val$NorDevice.setSceneParams(switchparams);
                }
                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$NorDevice.getDeviceAddress(), (short) 0, switchparams), this.val$NorDevice.getGatewayMacAddr(), this.val$NorDevice.getGatewayPassword(), this.val$NorDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                DatabaseManager.getInstance().updateDevice(this.val$NorDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.31 */
        class AnonymousClass31 implements OnClickListener {
            private final /* synthetic */ Device val$NorDevice;
            private final /* synthetic */ Button val$button1;
            private final /* synthetic */ Button val$button2;
            private final /* synthetic */ Button val$button3;

            AnonymousClass31(Device device, Button button, Button button2, Button button3) {
                this.val$NorDevice = device;
                this.val$button1 = button;
                this.val$button2 = button2;
                this.val$button3 = button3;
            }

            public void onClick(View v) {
                byte[] switchparams = new byte[]{(byte) 17, (byte) 2, (byte) 0, (byte) 0, (byte) this.val$NorDevice.getChannelMark()};
                this.val$button1.setTextColor(-9605779);
                this.val$button2.setTextColor(-9605779);
                this.val$button3.setTextColor(-10104851);
                this.val$NorDevice.setCurrentParams(switchparams);
                if (ChooseDeviceActivity.this.mScene != null) {
                    this.val$NorDevice.setSceneParams(switchparams);
                }
                DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$NorDevice.getDeviceAddress(), (short) 0, switchparams), this.val$NorDevice.getGatewayMacAddr(), this.val$NorDevice.getGatewayPassword(), this.val$NorDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                DatabaseManager.getInstance().updateDevice(this.val$NorDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.32 */
        class AnonymousClass32 implements OnClickListener {
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$mDevice_pic_tick;

            AnonymousClass32(int i, ImageView imageView) {
                this.val$arg0 = i;
                this.val$mDevice_pic_tick = imageView;
            }

            public void onClick(View v) {
                Device IsChooseDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                boolean ischoose = IsChooseDevice.isIschoose();
                String ss = IsChooseDevice.getDeviceName();
                if (ischoose) {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (int i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(ss)) {
                                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.remove(i);
                            }
                        }
                    }
                    IsChooseDevice.setIschoose(false);
                    return;
                }
                this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.add(IsChooseDevice);
                IsChooseDevice.setIschoose(true);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.33 */
        class AnonymousClass33 implements OnClickListener {
            private final /* synthetic */ Device val$NorDevice;
            private final /* synthetic */ ImageView val$imageView4;

            AnonymousClass33(Device device, ImageView imageView) {
                this.val$NorDevice = device;
                this.val$imageView4 = imageView;
            }

            public void onClick(View v) {
                Device device;
                byte[] bArr;
                if (this.val$NorDevice.isIsopen()) {
                    this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 48;
                    device.setCurrentParams(bArr);
                    this.val$NorDevice.setIsopen(false);
                } else {
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 48;
                    bArr[1] = (byte) 1;
                    device.setCurrentParams(bArr);
                    this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$NorDevice.setIsopen(true);
                }
                byte[] params = new byte[]{(byte) ((this.val$NorDevice.getDeviceAddress() >> 8) & MotionEventCompat.ACTION_MASK), (byte) (this.val$NorDevice.getDeviceAddress() & MotionEventCompat.ACTION_MASK), (byte) 0};
                if (this.val$NorDevice.isIsopen()) {
                    params[2] = (byte) 1;
                } else {
                    params[2] = (byte) 0;
                }
                if (ChooseDeviceActivity.this.mScene != null) {
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 48;
                    bArr[1] = params[2];
                    device.setSceneParams(bArr);
                }
                DeviceSocket.getInstance().send(Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 48, (short) params.length, (short) 0, params), this.val$NorDevice.getGatewayMacAddr(), this.val$NorDevice.getGatewayPassword(), this.val$NorDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                DatabaseManager.getInstance().updateDevice(this.val$NorDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.34 */
        class AnonymousClass34 implements OnClickListener {
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$mDevice_pic_tick;

            AnonymousClass34(int i, ImageView imageView) {
                this.val$arg0 = i;
                this.val$mDevice_pic_tick = imageView;
            }

            public void onClick(View v) {
                Device IsChooseDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                boolean ischoose = IsChooseDevice.isIschoose();
                String ss = IsChooseDevice.getDeviceName();
                if (ischoose) {
                    this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (int i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(ss)) {
                                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.remove(i);
                            }
                        }
                    }
                    IsChooseDevice.setIschoose(false);
                    return;
                }
                this.val$mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.add(IsChooseDevice);
                IsChooseDevice.setIschoose(true);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.35 */
        class AnonymousClass35 implements OnClickListener {
            private final /* synthetic */ Device val$NorDevice;
            private final /* synthetic */ ImageView val$imageView4;

            AnonymousClass35(Device device, ImageView imageView) {
                this.val$NorDevice = device;
                this.val$imageView4 = imageView;
            }

            public void onClick(View v) {
                Device device;
                byte[] bArr;
                if (this.val$NorDevice.isIsopen()) {
                    this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 52;
                    device.setCurrentParams(bArr);
                    this.val$NorDevice.setIsopen(false);
                } else {
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 52;
                    bArr[1] = (byte) 1;
                    device.setCurrentParams(bArr);
                    this.val$imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$NorDevice.setIsopen(true);
                }
                byte[] params = new byte[]{(byte) ((this.val$NorDevice.getDeviceAddress() >> 8) & MotionEventCompat.ACTION_MASK), (byte) (this.val$NorDevice.getDeviceAddress() & MotionEventCompat.ACTION_MASK), (byte) 0};
                if (this.val$NorDevice.isIsopen()) {
                    params[2] = (byte) 1;
                } else {
                    params[2] = (byte) 0;
                }
                if (ChooseDeviceActivity.this.mScene != null) {
                    device = this.val$NorDevice;
                    bArr = new byte[5];
                    bArr[0] = (byte) 52;
                    bArr[1] = params[2];
                    device.setSceneParams(bArr);
                }
                DeviceSocket.getInstance().send(Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 52, (short) params.length, (short) 0, params), this.val$NorDevice.getGatewayMacAddr(), this.val$NorDevice.getGatewayPassword(), this.val$NorDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                DatabaseManager.getInstance().updateDevice(this.val$NorDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.3 */
        class C01823 implements OnTouchListener {
            int f19b;
            int f20l;
            int f21r;
            int f22t;

            C01823() {
                this.f21r = 0;
                this.f19b = 0;
                this.f20l = 0;
                this.f22t = 0;
            }

            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case DialogFragment.STYLE_NORMAL /*0*/:
                        this.f20l = (int) event.getX();
                        this.f22t = (int) event.getY();
                        this.f21r = this.f20l + ChooseDeviceActivity.this.bitmapsmlwidth;
                        this.f19b = this.f22t + ChooseDeviceActivity.this.bitmapsmlheight;
                        if (this.f20l <= 0) {
                            this.f20l = 0;
                        } else if (this.f20l > (ChooseDeviceActivity.this.bitmapwidth - ChooseDeviceActivity.this.bitmapsmlwidth) - 1) {
                            this.f20l = (ChooseDeviceActivity.this.bitmapwidth - ChooseDeviceActivity.this.bitmapsmlwidth) - 1;
                        }
                        this.f21r = this.f20l + ChooseDeviceActivity.this.bitmapsmlwidth;
                        if (this.f22t <= 0) {
                            this.f22t = 0;
                        } else if (this.f22t > (ChooseDeviceActivity.this.bitmapheight - ChooseDeviceActivity.this.bitmapsmlheight) - 1) {
                            this.f22t = (ChooseDeviceActivity.this.bitmapheight - ChooseDeviceActivity.this.bitmapsmlheight) - 1;
                        }
                        this.f19b = this.f22t + ChooseDeviceActivity.this.bitmapsmlheight;
                        ChooseDeviceActivity.this.newbs = this.f19b;
                        ChooseDeviceActivity.this.newls = this.f20l;
                        ChooseDeviceActivity.this.newrs = this.f21r;
                        ChooseDeviceActivity.this.newts = this.f22t;
                        break;
                }
                return false;
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.4 */
        class C01834 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$button4;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$imageView1;
            private final /* synthetic */ SeekBar val$seekBar1;

            C01834(ImageView imageView, int i, SeekBar seekBar, Device device, ImageView imageView2, ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3) {
                this.val$imageView1 = imageView;
                this.val$arg0 = i;
                this.val$seekBar1 = seekBar;
                this.val$LoadDevice = device;
                this.val$button4 = imageView2;
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
            }

            public void onClick(View v) {
                this.val$imageView1.layout(ChooseDeviceActivity.this.newls, ChooseDeviceActivity.this.newts, ChooseDeviceActivity.this.newrs, ChooseDeviceActivity.this.newbs);
                ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).setMacAddress(new int[]{ChooseDeviceActivity.this.newls, ChooseDeviceActivity.this.newts, ChooseDeviceActivity.this.newrs, ChooseDeviceActivity.this.newbs});
                if (ChooseDeviceActivity.this.mScene != null) {
                    ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).setSceneDeviceMac(new int[]{ChooseDeviceActivity.this.newls, ChooseDeviceActivity.this.newts, ChooseDeviceActivity.this.newrs, ChooseDeviceActivity.this.newbs});
                }
                if (this.val$seekBar1.getProgress() == 0) {
                    this.val$LoadDevice.setClick(true);
                    this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$seekBar1.setProgress(100);
                }
                int pixel = ChooseDeviceActivity.this.bitmap.getPixel(ChooseDeviceActivity.this.newls, ChooseDeviceActivity.this.newts);
                int RR = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                ChooseDeviceActivity.this.mDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                ChooseDeviceActivity.this.R_1 = RR;
                ChooseDeviceActivity.this.G_1 = G;
                ChooseDeviceActivity.this.B_1 = B;
                ChooseDeviceActivity access$0 = null;
                if (ChooseDeviceActivity.this.R_1 < 0) {
                    access$0 = ChooseDeviceActivity.this;
                    access$0.R_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                }
                if (ChooseDeviceActivity.this.G_1 < 0) {
                    access$0 = ChooseDeviceActivity.this;
                    access$0.G_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                }
                if (ChooseDeviceActivity.this.B_1 < 0) {
                    access$0 = ChooseDeviceActivity.this;
                    access$0.B_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                }
                if (this.val$LoadDevice.getCurrentParams()[0] != -64) {
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$LoadDevice.setIsstart(true);
                    this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                    this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[0]));
                }
                ChooseDeviceActivity.this.mDevice.setCurrentParams(new byte[]{(byte) -64, (byte) ChooseDeviceActivity.this.R_1, (byte) ChooseDeviceActivity.this.G_1, (byte) ChooseDeviceActivity.this.B_1, (byte) this.val$seekBar1.getProgress()});
                if (ChooseDeviceActivity.this.mScene != null) {
                    ChooseDeviceActivity.this.mDevice.setSceneParams(new byte[]{(byte) -64, (byte) ChooseDeviceActivity.this.R_1, (byte) ChooseDeviceActivity.this.G_1, (byte) ChooseDeviceActivity.this.B_1, (byte) this.val$seekBar1.getProgress()});
                }
                DatabaseManager.getInstance().updateDevice(ChooseDeviceActivity.this.mDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.5 */
        class C01845 implements OnTouchListener {
            int newb;
            int newl;
            int newr;
            int newt;
            int startX;
            int startY;
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ int val$arg0;
            private final /* synthetic */ ImageView val$button4;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ ImageView val$imageView1;
            private final /* synthetic */ SeekBar val$seekBar1;

            C01845(ImageView imageView, int i, SeekBar seekBar, Device device, ImageView imageView2, ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3) {
                this.val$imageView1 = imageView;
                this.val$arg0 = i;
                this.val$seekBar1 = seekBar;
                this.val$LoadDevice = device;
                this.val$button4 = imageView2;
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
                this.startX = 0;
                this.startY = 0;
                this.newr = 0;
                this.newb = 0;
                this.newl = 0;
                this.newt = 0;
            }

            public boolean onTouch(View view, MotionEvent event) {
                ChooseDeviceActivity.this.mList.requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {
                    case DialogFragment.STYLE_NORMAL /*0*/:
                        this.startX = (int) event.getRawX();
                        this.startY = (int) event.getRawY();
                        break;
                    case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER /*2*/:
                        ChooseDeviceActivity access$0;
                        int dx = ((int) event.getRawX()) - this.startX;
                        int dy = ((int) event.getRawY()) - this.startY;
                        int r = this.val$imageView1.getRight();
                        int b = this.val$imageView1.getBottom();
                        this.newr = r + dx;
                        this.newb = b + dy;
                        this.newl = this.newr - this.val$imageView1.getWidth();
                        this.newt = this.newb - this.val$imageView1.getHeight();
                        if (this.newl <= 0) {
                            this.newl = 0;
                        } else if (this.newl > (ChooseDeviceActivity.this.bitmapwidth - ChooseDeviceActivity.this.bitmapsmlwidth) - 1) {
                            this.newl = (ChooseDeviceActivity.this.bitmapwidth - ChooseDeviceActivity.this.bitmapsmlwidth) - 1;
                        }
                        this.newr = this.newl + ChooseDeviceActivity.this.bitmapsmlwidth;
                        if (this.newt <= 0) {
                            this.newt = 0;
                        } else if (this.newt > (ChooseDeviceActivity.this.bitmapheight - ChooseDeviceActivity.this.bitmapsmlheight) - 1) {
                            this.newt = (ChooseDeviceActivity.this.bitmapheight - ChooseDeviceActivity.this.bitmapsmlheight) - 1;
                        }
                        this.newb = this.newt + ChooseDeviceActivity.this.bitmapsmlheight;
                        this.val$imageView1.layout(this.newl, this.newt, this.newr, this.newb);
                        ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).setMacAddress(new int[]{this.newl, this.newt, this.newr, this.newb});
                        if (ChooseDeviceActivity.this.mScene != null) {
                            ((Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0)).setSceneDeviceMac(new int[]{this.newl, this.newt, this.newr, this.newb});
                        }
                        this.startX = (int) event.getRawX();
                        this.startY = (int) event.getRawY();
                        int pixel = ChooseDeviceActivity.this.bitmap.getPixel(this.newl, this.newt);
                        int RR = Color.red(pixel);
                        int G = Color.green(pixel);
                        int B = Color.blue(pixel);
                        ChooseDeviceActivity.this.mDevice = (Device) ChooseDeviceActivity.this.devicelist.get(this.val$arg0);
                        ChooseDeviceActivity.this.R_1 = RR;
                        ChooseDeviceActivity.this.G_1 = G;
                        ChooseDeviceActivity.this.B_1 = B;
                        if (ChooseDeviceActivity.this.R_1 < 0) {
                            access$0 = ChooseDeviceActivity.this;
                            access$0.R_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                        }
                        if (ChooseDeviceActivity.this.G_1 < 0) {
                            access$0 = ChooseDeviceActivity.this;
                            access$0.G_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                        }
                        if (ChooseDeviceActivity.this.B_1 < 0) {
                            access$0 = ChooseDeviceActivity.this;
                            access$0.B_1 += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
                        }
                        if (this.val$seekBar1.getProgress() == 0) {
                            this.val$LoadDevice.setClick(true);
                            this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                            this.val$seekBar1.setProgress(100);
                        }
                        if (this.val$LoadDevice.getCurrentParams()[0] != -64) {
                            this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                            this.val$LoadDevice.setIsstart(true);
                            this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                            this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[0]));
                        }
                        ChooseDeviceActivity.this.mDevice.setCurrentParams(new byte[]{(byte) -64, (byte) ChooseDeviceActivity.this.R_1, (byte) ChooseDeviceActivity.this.G_1, (byte) ChooseDeviceActivity.this.B_1, (byte) this.val$seekBar1.getProgress()});
                        if (ChooseDeviceActivity.this.mScene != null) {
                            ChooseDeviceActivity.this.mDevice.setSceneParams(new byte[]{(byte) -64, (byte) ChooseDeviceActivity.this.R_1, (byte) ChooseDeviceActivity.this.G_1, (byte) ChooseDeviceActivity.this.B_1, (byte) this.val$seekBar1.getProgress()});
                        }
                        DatabaseManager.getInstance().updateDevice(ChooseDeviceActivity.this.mDevice);
                        break;
                }
                return true;
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.6 */
        class C01866 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageView val$button4;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ SeekBar val$seekBar1;

            /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.6.1 */
            class C01851 implements OnItemClickListener {
                private final /* synthetic */ Device val$LoadDevice;
                private final /* synthetic */ ImageView val$button4;
                private final /* synthetic */ ImageButton val$ib3;
                private final /* synthetic */ ImageButton val$ib5;
                private final /* synthetic */ SeekBar val$seekBar1;

                C01851(ImageButton imageButton, Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton2) {
                    this.val$ib3 = imageButton;
                    this.val$LoadDevice = device;
                    this.val$button4 = imageView;
                    this.val$seekBar1 = seekBar;
                    this.val$ib5 = imageButton2;
                }

                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    ChooseDeviceActivity.this.effect_hue = arg2 + 1;
                    this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[arg2]));
                    Device device;
                    byte[] bArr;
                    if (this.val$LoadDevice.getCurrentParams()[0] == (byte) -63) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = (byte) ChooseDeviceActivity.this.effect_hue;
                        bArr[2] = this.val$LoadDevice.getCurrentParams()[2];
                        bArr[3] = (byte) 1;
                        device.setCurrentParams(bArr);
                    } else {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = (byte) ChooseDeviceActivity.this.effect_hue;
                        bArr[2] = (byte) 1;
                        bArr[3] = (byte) 1;
                        device.setCurrentParams(bArr);
                    }
                    short deviceAddress = this.val$LoadDevice.getDeviceAddress();
                    byte[] bArr2 = new byte[5];
                    bArr2[0] = (byte) -63;
                    bArr2[1] = (byte) ChooseDeviceActivity.this.effect_hue;
                    bArr2[2] = this.val$LoadDevice.getCurrentParams()[2];
                    bArr2[3] = (byte) 1;
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, deviceAddress, (short) 0, bArr2), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$seekBar1.setProgress(100);
                    if (this.val$LoadDevice.isIsstart()) {
                        this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                        this.val$LoadDevice.setIsstart(false);
                    }
                    if (ChooseDeviceActivity.this.mScene != null) {
                        this.val$LoadDevice.setSceneParams(this.val$LoadDevice.getCurrentParams());
                    }
                    DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
                    if (ChooseDeviceActivity.this.mdialog != null && ChooseDeviceActivity.this.mdialog.isShowing()) {
                        ChooseDeviceActivity.this.mdialog.cancel();
                        ChooseDeviceActivity.this.mdialog = null;
                    }
                }
            }

            C01866(ImageButton imageButton, Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton2) {
                this.val$ib3 = imageButton;
                this.val$LoadDevice = device;
                this.val$button4 = imageView;
                this.val$seekBar1 = seekBar;
                this.val$ib5 = imageButton2;
            }

            public void onClick(View v) {
                ArrayList<HashMap<String, Object>> arraylist = new ArrayList();
                String[] effect = ChooseDeviceActivity.this.getResources().getStringArray(R.array.hue_effect);
                for (int i = 0; i < effect.length; i++) {
                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap.put("imgItem", Integer.valueOf(ChooseDeviceActivity.this.image_ib3[i]));
                    hashMap.put("textItem", effect[i]);
                    arraylist.add(hashMap);
                }
                View view = ChooseDeviceActivity.this.inflater.inflate(R.layout.spinnerdialog, null);
                ChooseDeviceActivity.this.mdialog = new Dialog(ChooseDeviceActivity.this, R.style.Theme_dialog);
                ChooseDeviceActivity.this.mdialog.setContentView(view);
                ChooseDeviceActivity.this.mdialog.setCancelable(true);
                ChooseDeviceActivity.this.mdialog.setCanceledOnTouchOutside(false);
                ChooseDeviceActivity.this.mdialog.show();
                ListView list = (ListView) view.findViewById(R.id.listView1);
                list.setAdapter(new SimpleAdapter(ChooseDeviceActivity.this, arraylist, R.layout.spinner_item, new String[]{"imgItem", "textItem"}, new int[]{R.id.device_img, R.id.skjnjg}));
                list.setOnItemClickListener(new C01851(this.val$ib3, this.val$LoadDevice, this.val$button4, this.val$seekBar1, this.val$ib5));
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.7 */
        class C01887 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageView val$button4;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ SeekBar val$seekBar1;

            /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.7.1 */
            class C01871 implements OnItemClickListener {
                private final /* synthetic */ Device val$LoadDevice;
                private final /* synthetic */ ImageView val$button4;
                private final /* synthetic */ ImageButton val$ib4;
                private final /* synthetic */ ImageButton val$ib5;
                private final /* synthetic */ SeekBar val$seekBar1;

                C01871(ImageButton imageButton, Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton2) {
                    this.val$ib4 = imageButton;
                    this.val$LoadDevice = device;
                    this.val$button4 = imageView;
                    this.val$seekBar1 = seekBar;
                    this.val$ib5 = imageButton2;
                }

                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    ChooseDeviceActivity.this.delay_hue = arg2 + 1;
                    this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[arg2]));
                    Device device;
                    byte[] bArr;
                    if (this.val$LoadDevice.getCurrentParams()[0] == (byte) -63) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = this.val$LoadDevice.getCurrentParams()[1];
                        bArr[2] = (byte) ChooseDeviceActivity.this.delay_hue;
                        bArr[3] = (byte) 1;
                        device.setCurrentParams(bArr);
                    } else {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = (byte) 1;
                        bArr[2] = (byte) ChooseDeviceActivity.this.delay_hue;
                        bArr[3] = (byte) 1;
                        device.setCurrentParams(bArr);
                    }
                    short deviceAddress = this.val$LoadDevice.getDeviceAddress();
                    byte[] bArr2 = new byte[5];
                    bArr2[0] = (byte) -63;
                    bArr2[1] = this.val$LoadDevice.getCurrentParams()[1];
                    bArr2[2] = (byte) ChooseDeviceActivity.this.delay_hue;
                    bArr2[3] = (byte) 1;
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, deviceAddress, (short) 0, bArr2), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$seekBar1.setProgress(100);
                    if (this.val$LoadDevice.isIsstart()) {
                        this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                        this.val$LoadDevice.setIsstart(false);
                    }
                    if (ChooseDeviceActivity.this.mScene != null) {
                        this.val$LoadDevice.setSceneParams(this.val$LoadDevice.getCurrentParams());
                    }
                    DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
                    if (ChooseDeviceActivity.this.mdialog != null && ChooseDeviceActivity.this.mdialog.isShowing()) {
                        ChooseDeviceActivity.this.mdialog.cancel();
                        ChooseDeviceActivity.this.mdialog = null;
                    }
                }
            }

            C01887(ImageButton imageButton, Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton2) {
                this.val$ib4 = imageButton;
                this.val$LoadDevice = device;
                this.val$button4 = imageView;
                this.val$seekBar1 = seekBar;
                this.val$ib5 = imageButton2;
            }

            public void onClick(View v) {
                ArrayList<HashMap<String, Object>> arraylist = new ArrayList();
                String[] interval = ChooseDeviceActivity.this.getResources().getStringArray(R.array.time_interval);
                for (int i = 0; i < interval.length; i++) {
                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap.put("imgItem", Integer.valueOf(ChooseDeviceActivity.this.image[i]));
                    hashMap.put("textItem", interval[i]);
                    arraylist.add(hashMap);
                }
                View view = ChooseDeviceActivity.this.inflater.inflate(R.layout.spinnerdialog, null);
                ChooseDeviceActivity.this.mdialog = new Dialog(ChooseDeviceActivity.this, R.style.Theme_dialog);
                ChooseDeviceActivity.this.mdialog.setContentView(view);
                ChooseDeviceActivity.this.mdialog.setCancelable(true);
                ChooseDeviceActivity.this.mdialog.setCanceledOnTouchOutside(false);
                ChooseDeviceActivity.this.mdialog.show();
                ListView list = (ListView) view.findViewById(R.id.listView1);
                list.setAdapter(new SimpleAdapter(ChooseDeviceActivity.this, arraylist, R.layout.spinner_item, new String[]{"imgItem", "textItem"}, new int[]{R.id.device_img, R.id.skjnjg}));
                list.setOnItemClickListener(new C01871(this.val$ib4, this.val$LoadDevice, this.val$button4, this.val$seekBar1, this.val$ib5));
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.8 */
        class C01898 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageView val$button4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ SeekBar val$seekBar1;

            C01898(Device device, ImageView imageView, ImageButton imageButton, SeekBar seekBar) {
                this.val$LoadDevice = device;
                this.val$button4 = imageView;
                this.val$ib5 = imageButton;
                this.val$seekBar1 = seekBar;
            }

            public void onClick(View v) {
                Device device;
                byte[] bArr;
                short deviceAddress;
                byte[] bArr2;
                if (this.val$LoadDevice.isIsstart()) {
                    if (this.val$LoadDevice.getCurrentParams()[0] == (byte) -63) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = this.val$LoadDevice.getCurrentParams()[1];
                        bArr[2] = this.val$LoadDevice.getCurrentParams()[2];
                        bArr[3] = (byte) 1;
                        device.setCurrentParams(bArr);
                    } else {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = (byte) 1;
                        bArr[2] = (byte) 1;
                        bArr[3] = (byte) 1;
                        device.setCurrentParams(bArr);
                    }
                    deviceAddress = this.val$LoadDevice.getDeviceAddress();
                    bArr2 = new byte[5];
                    bArr2[0] = (byte) -63;
                    bArr2[1] = (byte) ChooseDeviceActivity.this.effect_hue;
                    bArr2[2] = (byte) ChooseDeviceActivity.this.delay_hue;
                    bArr2[3] = (byte) 1;
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, deviceAddress, (short) 0, bArr2), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                    this.val$LoadDevice.setIsstart(false);
                    this.val$seekBar1.setProgress(100);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        this.val$LoadDevice.setSceneParams(this.val$LoadDevice.getCurrentParams());
                    }
                } else {
                    if (this.val$LoadDevice.getCurrentParams()[0] == (byte) -63) {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = this.val$LoadDevice.getCurrentParams()[1];
                        bArr[2] = this.val$LoadDevice.getCurrentParams()[2];
                        device.setCurrentParams(bArr);
                    } else {
                        device = this.val$LoadDevice;
                        bArr = new byte[5];
                        bArr[0] = (byte) -63;
                        bArr[1] = (byte) 1;
                        bArr[2] = (byte) 1;
                        device.setCurrentParams(bArr);
                    }
                    deviceAddress = this.val$LoadDevice.getDeviceAddress();
                    bArr2 = new byte[5];
                    bArr2[0] = (byte) -63;
                    bArr2[1] = (byte) ChooseDeviceActivity.this.effect_hue;
                    bArr2[2] = (byte) ChooseDeviceActivity.this.delay_hue;
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, deviceAddress, (short) 0, bArr2), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$LoadDevice.setIsstart(true);
                    this.val$seekBar1.setProgress(100);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        this.val$LoadDevice.setSceneParams(this.val$LoadDevice.getCurrentParams());
                    }
                }
                DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
            }
        }

        /* renamed from: com.allin.activity.ChooseDeviceActivity.MyAdapter.9 */
        class C01909 implements OnClickListener {
            private final /* synthetic */ Device val$LoadDevice;
            private final /* synthetic */ ImageView val$button4;
            private final /* synthetic */ ImageButton val$ib3;
            private final /* synthetic */ ImageButton val$ib4;
            private final /* synthetic */ ImageButton val$ib5;
            private final /* synthetic */ SeekBar val$seekBar1;

            C01909(Device device, ImageView imageView, SeekBar seekBar, ImageButton imageButton, ImageButton imageButton2, ImageButton imageButton3) {
                this.val$LoadDevice = device;
                this.val$button4 = imageView;
                this.val$seekBar1 = seekBar;
                this.val$ib5 = imageButton;
                this.val$ib4 = imageButton2;
                this.val$ib3 = imageButton3;
            }

            public void onClick(View v) {
                byte[] rgb_switchParams = new byte[5];
                byte[] s = new byte[5];
                if (this.val$LoadDevice.isClick()) {
                    this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                    rgb_switchParams[0] = (byte) -62;
                    rgb_switchParams[1] = (byte) 0;
                    rgb_switchParams[2] = (byte) 0;
                    rgb_switchParams[3] = (byte) 0;
                    rgb_switchParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, rgb_switchParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(false);
                    s[0] = (byte) -62;
                    s[1] = (byte) 0;
                    s[2] = (byte) 0;
                    s[3] = (byte) 0;
                    s[4] = (byte) 0;
                    this.val$LoadDevice.setCurrentParams(s);
                    this.val$seekBar1.setProgress(0);
                    this.val$ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    this.val$LoadDevice.setIsstart(true);
                    this.val$ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                    this.val$ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[0]));
                } else {
                    this.val$button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    rgb_switchParams[0] = (byte) -62;
                    rgb_switchParams[1] = (byte) 1;
                    rgb_switchParams[2] = (byte) 0;
                    rgb_switchParams[3] = (byte) 0;
                    rgb_switchParams[4] = (byte) this.val$LoadDevice.getChannelMark();
                    DeviceSocket.getInstance().send(Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, this.val$LoadDevice.getDeviceAddress(), (short) 0, rgb_switchParams), this.val$LoadDevice.getGatewayMacAddr(), this.val$LoadDevice.getGatewayPassword(), this.val$LoadDevice.getGatewaySSID(), ChooseDeviceActivity.this));
                    this.val$LoadDevice.setClick(true);
                    s[0] = (byte) -62;
                    s[1] = (byte) 1;
                    s[2] = (byte) 0;
                    s[3] = (byte) 0;
                    s[4] = (byte) 0;
                    this.val$LoadDevice.setCurrentParams(s);
                    this.val$seekBar1.setProgress(100);
                }
                DatabaseManager.getInstance().updateDevice(this.val$LoadDevice);
                if (ChooseDeviceActivity.this.mScene != null) {
                    this.val$LoadDevice.setSceneParams(s);
                }
            }
        }

        MyAdapter() {
        }

        public int getCount() {
            return ChooseDeviceActivity.this.devicelist.size();
        }

        public Object getItem(int arg0) {
            int a;
            if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceType() == (short) 5 && ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getSubDeviceType() == (short) 3) {
                a = 7;
            } else if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceType() == (short) 5 && ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getSubDeviceType() == (short) 1) {
                a = 6;
            } else if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceType() == (short) 111 || ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceType() == (short) 15) {
                a = 5;
            } else if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceType() >= (short) 97) {
                a = ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceType() - 97;
            } else {
                a = ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceType() - 1;
            }
            return Integer.valueOf(a);
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public int getViewTypeCount() {
            return 8;
        }

        public int getItemViewType(int position) {
            if (((Device) ChooseDeviceActivity.this.devicelist.get(position)).getDeviceType() == (short) 5 && ((Device) ChooseDeviceActivity.this.devicelist.get(position)).getSubDeviceType() == (short) 3) {
                return 7;
            }
            if (((Device) ChooseDeviceActivity.this.devicelist.get(position)).getDeviceType() == (short) 5 && ((Device) ChooseDeviceActivity.this.devicelist.get(position)).getSubDeviceType() == (short) 1) {
                return 6;
            }
            if (((Device) ChooseDeviceActivity.this.devicelist.get(position)).getDeviceType() == (short) 111 || ((Device) ChooseDeviceActivity.this.devicelist.get(position)).getDeviceType() == (short) 15) {
                return 5;
            }
            if (((Device) ChooseDeviceActivity.this.devicelist.get(position)).getDeviceType() >= (short) 97) {
                return ((Device) ChooseDeviceActivity.this.devicelist.get(position)).getDeviceType() - 97;
            }
            return ((Device) ChooseDeviceActivity.this.devicelist.get(position)).getDeviceType() - 1;
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (ChooseDeviceActivity.this.mList.getChildCount() > 20) {
                ChooseDeviceActivity.this.mList.removeViewAt(0);
            }
            Device LoadDevice;
            ImageButton ib3;
            ImageButton ib4;
            ImageButton ib5;
            ImageView mDevice_pic_tick;
            SeekBar seekBar1;
            int j;
            ImageView imgview;
            TextView deviceName_tv;
            Rect r;
            ImageView imageView4;
            ImageView image_switch;
            SeekBar seekBar;
            ImageView image_2;
            Device NorDevice;
            Paint paint;
            switch (getItemViewType(arg0)) {
                case DialogFragment.STYLE_NORMAL /*0*/:
                    int l;
                    int t;
                    LoadDevice = (Device) ChooseDeviceActivity.this.devicelist.get(arg0);
                    if (ChooseDeviceActivity.this.mBackUpParams != null) {
                        ChooseDeviceActivity.this.mBackUpParams.ResetBackupParmas(LoadDevice);
                    }
                    ChooseDeviceActivity.this.mDevice = LoadDevice;
                    if (arg1 == null) {
                        arg1 = LayoutInflater.from(ChooseDeviceActivity.this).inflate(R.layout.hue_bulb_1, null);
                    }
                    RelativeLayout relativeLayout = (RelativeLayout) arg1.findViewById(R.id.relativeLayout3);
                    ImageView imageView1 = (ImageView) arg1.findViewById(R.id.imageView1);
                    CustomImageView imageView0 = (CustomImageView) arg1.findViewById(R.id.imageView0);
                    ib3 = (ImageButton) arg1.findViewById(R.id.imageView13);
                    ib4 = (ImageButton) arg1.findViewById(R.id.imageView15);
                    ImageView rgb_pic_border = (ImageView) arg1.findViewById(R.id.rgb_pic_border);
                    ib5 = (ImageButton) arg1.findViewById(R.id.imageView16);
                    mDevice_pic_tick = (ImageView) arg1.findViewById(R.id.device_pic_tick);
                    ImageView imageView = (ImageView) arg1.findViewById(R.id.rgb_pic_border);
                    ImageView button4 = (ImageView) arg1.findViewById(R.id.button4);
                    seekBar1 = (SeekBar) arg1.findViewById(R.id.seekBar1);
                    TextView textView = (TextView) arg1.findViewById(R.id.textView11);
                    textView.setText(LoadDevice.getDeviceName());
                    seekBar1.setProgress(100);
                    if (ChooseDeviceActivity.this.bitmapsmlwidth == 0 || ChooseDeviceActivity.this.bitmapsmlheight == 0) {
                        ChooseDeviceActivity.this.bitmapsmlwidth = imageView1.getWidth();
                        ChooseDeviceActivity.this.bitmapsmlheight = imageView1.getHeight();
                        if (ChooseDeviceActivity.this.bitmapsmlwidth == 0 || ChooseDeviceActivity.this.bitmapsmlheight == 0) {
                            imageView1.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
                            ChooseDeviceActivity.this.bitmapsmlwidth = imageView1.getMeasuredWidth();
                            ChooseDeviceActivity.this.bitmapsmlheight = imageView1.getMeasuredHeight();
                        }
                    }
                    if (ChooseDeviceActivity.this.bitmapwidth == ChooseDeviceActivity.this.DM.widthPixels) {
                        ChooseDeviceActivity chooseDeviceActivity = ChooseDeviceActivity.this;
                        chooseDeviceActivity.bitmapwidth -= ChooseDeviceActivity.this.bitmapsmlwidth / 2;
                    }
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ChooseDeviceActivity.this.bitmapwidth, ChooseDeviceActivity.this.bitmapheight);
                    layoutParams.addRule(3, R.id.blank21);
                    relativeLayout.setLayoutParams(layoutParams);
                    if (ChooseDeviceActivity.this.mScene != null) {
                        l = LoadDevice.getSceneDeviceMac()[0];
                        t = LoadDevice.getSceneDeviceMac()[1];
                    } else {
                        l = LoadDevice.getMacAddress()[0];
                        t = LoadDevice.getMacAddress()[1];
                    }
                    if (l <= 0) {
                        ChooseDeviceActivity.this.newls = 0;
                    } else if (l > (ChooseDeviceActivity.this.bitmapwidth - ChooseDeviceActivity.this.bitmapsmlwidth) - 1) {
                        ChooseDeviceActivity.this.newls = (ChooseDeviceActivity.this.bitmapwidth - ChooseDeviceActivity.this.bitmapsmlwidth) - 1;
                    } else {
                        ChooseDeviceActivity.this.newls = l;
                    }
                    if (t <= 0) {
                        ChooseDeviceActivity.this.newts = 0;
                    } else if (t > (ChooseDeviceActivity.this.bitmapheight - ChooseDeviceActivity.this.bitmapsmlheight) - 1) {
                        ChooseDeviceActivity.this.newts = (ChooseDeviceActivity.this.bitmapheight - ChooseDeviceActivity.this.bitmapsmlheight) - 1;
                    } else {
                        ChooseDeviceActivity.this.newts = t;
                    }
                    layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                    layoutParams.setMargins(ChooseDeviceActivity.this.newls, ChooseDeviceActivity.this.newts, 0, 0);
                    imageView1.setLayoutParams(layoutParams);
                    layoutParams = new RelativeLayout.LayoutParams(ChooseDeviceActivity.this.bitmapwidth, ChooseDeviceActivity.this.bitmapheight);
                    layoutParams.leftMargin = ChooseDeviceActivity.this.bitmapsmlwidth / 2;
                    layoutParams.rightMargin = ChooseDeviceActivity.this.bitmapsmlwidth / 2;
                    layoutParams.topMargin = ChooseDeviceActivity.this.bitmapsmlheight;
                    imageView0.setLayoutParams(layoutParams);
                    ChooseDeviceActivity.this.bitmap = Bitmap.createScaledBitmap(ChooseDeviceActivity.this.bit, ChooseDeviceActivity.this.bitmapwidth - ChooseDeviceActivity.this.bitmapsmlwidth, ChooseDeviceActivity.this.bitmapheight - ChooseDeviceActivity.this.bitmapsmlheight, true);
                    imageView0.setBackgroundDrawable(new BitmapDrawable(ChooseDeviceActivity.this.bitmap));
                    ChooseDeviceActivity.this.bitMaplist.add(ChooseDeviceActivity.this.bitmap);
                    seekBar1.setOnSeekBarChangeListener(new C01781(arg0, ib5, LoadDevice, ib4, ib3, button4));
                    if (LoadDevice.getDeviceType() >= (short) 61) {
                        rgb_pic_border.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_border_group));
                    } else {
                        rgb_pic_border.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_border));
                    }
                    imageView.setOnClickListener(new C01812(arg0, mDevice_pic_tick, LoadDevice));
                    imageView0.setOnTouchListener(new C01823());
                    imageView0.setOnClickListener(new C01834(imageView1, arg0, seekBar1, LoadDevice, button4, ib5, ib4, ib3));
                    imageView1.setOnTouchListener(new C01845(imageView1, arg0, seekBar1, LoadDevice, button4, ib5, ib4, ib3));
                    ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[0]));
                    ib3.setOnClickListener(new C01866(ib3, LoadDevice, button4, seekBar1, ib5));
                    ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[0]));
                    ib4.setOnClickListener(new C01887(ib4, LoadDevice, button4, seekBar1, ib5));
                    ib5.setOnClickListener(new C01898(LoadDevice, button4, ib5, seekBar1));
                    button4.setOnClickListener(new C01909(LoadDevice, button4, seekBar1, ib5, ib4, ib3));
                    button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                    LoadDevice.setClick(true);
                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    LoadDevice.setIsstart(true);
                    seekBar1.setProgress(100);
                    if ((ChooseDeviceActivity.this.index == null || !ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) && ChooseDeviceActivity.this.mArea == null) {
                        if (ChooseDeviceActivity.this.mScene != null) {
                            if (LoadDevice.getSceneParams()[0] == -64) {
                                seekBar1.setProgress(LoadDevice.getSceneParams()[4]);
                                if (LoadDevice.getSceneParams()[4] == 0) {
                                    button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                    LoadDevice.setClick(false);
                                }
                            } else if (LoadDevice.getSceneParams()[0] == 0) {
                                seekBar1.setProgress(0);
                                button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                LoadDevice.setClick(false);
                            } else if (LoadDevice.getSceneParams()[0] == -63) {
                                ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[LoadDevice.getSceneParams()[1] - 1]));
                                ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[LoadDevice.getSceneParams()[2] - 1]));
                                if (LoadDevice.getSceneParams()[3] == 1) {
                                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                                    LoadDevice.setIsstart(false);
                                } else {
                                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                                    LoadDevice.setIsstart(true);
                                }
                            } else if (LoadDevice.getSceneParams()[0] == -62 && LoadDevice.getSceneParams()[1] == 0) {
                                seekBar1.setProgress(0);
                                button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                LoadDevice.setClick(false);
                            }
                        }
                    } else if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[0] == -64) {
                        seekBar1.setProgress(((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[4]);
                        if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[4] == 0) {
                            seekBar1.setProgress(0);
                            button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                            LoadDevice.setClick(false);
                        }
                    } else if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[0] == 0) {
                        seekBar1.setProgress(0);
                        button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        LoadDevice.setClick(false);
                    } else if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[0] == -63) {
                        ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib3[((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[1] - 1]));
                        ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[2] - 1]));
                        if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[3] == 1) {
                            ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                            LoadDevice.setIsstart(false);
                        } else {
                            ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                            LoadDevice.setIsstart(true);
                        }
                    } else if (((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[0] == -62 && ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getCurrentParams()[1] == 0) {
                        seekBar1.setProgress(0);
                        button4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        LoadDevice.setClick(false);
                    }
                    mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (j = 0; j < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); j++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).getDeviceName().equals(LoadDevice.getDeviceName())) {
                                ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setCurrentParams(LoadDevice.getCurrentParams());
                                if (ChooseDeviceActivity.this.mScene != null) {
                                    ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setSceneParams(LoadDevice.getCurrentParams());
                                }
                                mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                                ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).setIschoose(true);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                case CursorAdapter.FLAG_AUTO_REQUERY /*1*/:
                    LoadDevice = (Device) ChooseDeviceActivity.this.devicelist.get(arg0);
                    LoadDevice.setCurrentParams(DatabaseManager.getInstance().getlightingofDevice((Device) ChooseDeviceActivity.this.devicelist.get(ChooseDeviceActivity.this.f24i)));
                    if (arg1 == null) {
                        arg1 = LayoutInflater.from(ChooseDeviceActivity.this).inflate(R.layout.switchlight_item_1, null);
                    }
                    imgview = (ImageView) arg1.findViewById(R.id.imageView2);
                    mDevice_pic_tick = (ImageView) arg1.findViewById(R.id.device_pic_tick);
                    if (LoadDevice.getDeviceType() >= (short) 98) {
                        imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.rgbw_group_pic));
                    } else {
                        imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.rgbw_pic));
                    }
                    imgview.setOnClickListener(new AnonymousClass10(arg0, mDevice_pic_tick));
                    deviceName_tv = (TextView) arg1.findViewById(R.id.textView1);
                    r = new Rect();
                    ChooseDeviceActivity.this.paint = new Paint();
                    ChooseDeviceActivity.this.paint.getTextBounds(deviceName_tv.getText().toString(), 0, deviceName_tv.getText().toString().length(), r);
                    if (r.width() > deviceName_tv.getWidth()) {
                        deviceName_tv.setMaxWidth(((int) deviceName_tv.getTextSize()) - (r.width() - deviceName_tv.getWidth()));
                    }
                    deviceName_tv.setText(((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceName());
                    ChooseDeviceActivity.this.paint = null;
                    imageView4 = (ImageView) arg1.findViewById(R.id.imageView4);
                    if ((ChooseDeviceActivity.this.index == null || !ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) && ChooseDeviceActivity.this.mArea == null) {
                        if (ChooseDeviceActivity.this.mScene != null) {
                            if (DatabaseManager.getInstance().getScenelightingofDevice((Device) ChooseDeviceActivity.this.devicelist.get(arg0), ChooseDeviceActivity.this.mScene)[1] == 100) {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                                LoadDevice.setIsopen(false);
                            } else {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                LoadDevice.setIsopen(true);
                            }
                        }
                    } else if (DatabaseManager.getInstance().getlightingofDevice((Device) ChooseDeviceActivity.this.devicelist.get(arg0))[1] == 100) {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        LoadDevice.setIsopen(false);
                    } else {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        LoadDevice.setIsopen(true);
                    }
                    imageView4.setOnClickListener(new AnonymousClass11(LoadDevice, imageView4));
                    mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (j = 0; j < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); j++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).getDeviceName().equals(LoadDevice.getDeviceName())) {
                                ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setCurrentParams(LoadDevice.getCurrentParams());
                                if (ChooseDeviceActivity.this.mScene != null) {
                                    ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setSceneParams(LoadDevice.getSceneParams());
                                }
                                mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                                ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).setIschoose(true);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER /*2*/:
                    LoadDevice = (Device) ChooseDeviceActivity.this.devicelist.get(arg0);
                    if (ChooseDeviceActivity.this.mBackUpParams != null) {
                        ChooseDeviceActivity.this.mBackUpParams.ResetBackupParmas(LoadDevice);
                    }
                    ChooseDeviceActivity.this.mDevice = LoadDevice;
                    if (arg1 == null) {
                        arg1 = LayoutInflater.from(ChooseDeviceActivity.this).inflate(R.layout.dimmerlight, null);
                    }
                    imgview = (ImageView) arg1.findViewById(R.id.dimmerlight_border);
                    mDevice_pic_tick = (ImageView) arg1.findViewById(R.id.device_pic_tick);
                    image_switch = (ImageView) arg1.findViewById(R.id.image_switch);
                    seekBar = (SeekBar) arg1.findViewById(R.id.seekBar1);
                    /*
                    image_2 = (ImageView) arg1.findViewById(R.id.imageView11);
                    ((ImageView) arg1.findViewById(R.id.imageView9)).setVisibility(8);
                    image_2.setVisibility(8);
                    ib3 = (ImageButton) arg1.findViewById(R.id.imageView13);
                    ib4 = (ImageButton) arg1.findViewById(R.id.imageView15);
                    ib5 = (ImageButton) arg1.findViewById(R.id.imageView16);
                    ib3.setBackgroundResource(R.drawable.left_selector);
                    */
                    if (LoadDevice.getDeviceType() >= (short) 97) {
                        imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.panel_group_pic));
                    } else {
                        imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.panel_pic));
                    }
                    //imgview.setOnClickListener(new AnonymousClass12(arg0, mDevice_pic_tick));
                    deviceName_tv = (TextView) arg1.findViewById(R.id.devicename_tv);
                    r = new Rect();
                    ChooseDeviceActivity.this.paint = new Paint();
                    ChooseDeviceActivity.this.paint.getTextBounds(deviceName_tv.getText().toString(), 0, deviceName_tv.getText().toString().length(), r);
                    if (r.width() > deviceName_tv.getWidth()) {
                        deviceName_tv.setMaxWidth(((int) deviceName_tv.getTextSize()) - (r.width() - deviceName_tv.getWidth()));
                    }
                    deviceName_tv.setText(((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceName());
                    ChooseDeviceActivity.this.paint = null;
                    image_switch.setOnClickListener(new AnonymousClass13(LoadDevice, image_switch, seekBar /*, ib5, ib4, ib3*/));
                    seekBar.setOnSeekBarChangeListener(new AnonymousClass14(LoadDevice, image_switch,/* ib5, ib4, ib3, */arg0));
                    /*
                    ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[0]));
                    ib3.setOnClickListener(new AnonymousClass15(ib3, LoadDevice, seekBar, image_switch, ib5));
                    ib4.setImageResource(ChooseDeviceActivity.this.image[0]);
                    ib4.setOnClickListener(new AnonymousClass16(ib4, LoadDevice, image_switch, seekBar, ib5));
                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    ib5.setOnClickListener(new AnonymousClass17(LoadDevice, image_switch, seekBar, ib5));
                    */
                    LoadDevice.setIsstart(true);
                    /*
                    if ((ChooseDeviceActivity.this.index == null || !ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) && ChooseDeviceActivity.this.mArea == null) {
                        if (ChooseDeviceActivity.this.mScene != null) {
                            if (LoadDevice.getSceneParams()[0] == -63) {
                                ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[LoadDevice.getSceneParams()[1] - 1]));
                                ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[LoadDevice.getSceneParams()[2] - 1]));
                                if (LoadDevice.getSceneParams()[3] == 1) {
                                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                                    LoadDevice.setIsstart(false);
                                } else {
                                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                                    LoadDevice.setIsstart(true);
                                }
                                LoadDevice.setClick(true);
                                image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                                if (LoadDevice.getSceneParams()[1] != 3 || LoadDevice.getSceneParams()[4] <= 1) {
                                    seekBar.setProgress(100);
                                } else {
                                    seekBar.setProgress(LoadDevice.getSceneParams()[4]);
                                }
                            } else if (LoadDevice.getSceneParams()[0] == 17 || LoadDevice.getSceneParams()[0] == 0) {
                                seekBar.setProgress(LoadDevice.getSceneParams()[1]);
                                if (seekBar.getProgress() == 0) {
                                    LoadDevice.setClick(false);
                                    image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                } else {
                                    LoadDevice.setClick(true);
                                    image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                                }
                            }
                        }
                    } else
                    */
                    if (LoadDevice.getCurrentParams()[0] == -63) {
                        //ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[LoadDevice.getCurrentParams()[1] - 1]));
                        //ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[LoadDevice.getCurrentParams()[2] - 1]));
                        if (LoadDevice.getCurrentParams()[3] == 1) {
                            //ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                            LoadDevice.setIsstart(false);
                        } else {
                            //ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                            LoadDevice.setIsstart(true);
                        }
                        LoadDevice.setClick(true);
                        image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        if (LoadDevice.getCurrentParams()[1] != 3 || LoadDevice.getCurrentParams()[4] <= 1) {
                            seekBar.setProgress(100);
                        } else {
                            seekBar.setProgress(LoadDevice.getCurrentParams()[4]);
                        }
                    } else if (LoadDevice.getCurrentParams()[0] == 17 || LoadDevice.getCurrentParams()[0] == 0) {
                        seekBar.setProgress(LoadDevice.getCurrentParams()[1]);
                        if (seekBar.getProgress() == 0) {
                            LoadDevice.setClick(false);
                            image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        } else {
                            LoadDevice.setClick(true);
                            image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        }
                    }
                    mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    /*
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (j = 0; j < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); j++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).getDeviceName().equals(LoadDevice.getDeviceName())) {
                                ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setCurrentParams(LoadDevice.getCurrentParams());
                                if (ChooseDeviceActivity.this.mScene != null) {
                                    ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setSceneParams(LoadDevice.getSceneParams());
                                }
                                mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                                ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).setIschoose(true);
                                break;
                            }
                        }
                        break;
                    }
                    */
                    break;
                case 3 /*3*/:
                    LoadDevice = (Device) ChooseDeviceActivity.this.devicelist.get(arg0);
                    if (ChooseDeviceActivity.this.mBackUpParams != null) {
                        ChooseDeviceActivity.this.mBackUpParams.ResetBackupParmas(LoadDevice);
                    }
                    ChooseDeviceActivity.this.mDevice = LoadDevice;
                    if (arg1 == null) {
                        arg1 = LayoutInflater.from(ChooseDeviceActivity.this).inflate(R.layout.colortemp_delay, null);
                    }
                    imgview = (ImageView) arg1.findViewById(R.id.lighttoning_border);
                    mDevice_pic_tick = (ImageView) arg1.findViewById(R.id.device_pic_tick);
                    image_switch = (ImageView) arg1.findViewById(R.id.image_switch);
                    image_2 = (ImageView) arg1.findViewById(R.id.imageView11);
                    ((ImageView) arg1.findViewById(R.id.imageView9)).setVisibility(8);
                    image_2.setVisibility(8);
                    ib3 = (ImageButton) arg1.findViewById(R.id.imageView13);
                    ib4 = (ImageButton) arg1.findViewById(R.id.imageView15);
                    ib5 = (ImageButton) arg1.findViewById(R.id.imageView16);
                    ib3.setBackgroundResource(R.drawable.left_selector);
                    if (LoadDevice.getDeviceType() >= (short) 97) {
                        imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.colortemp_group_pic));
                    } else {
                        imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.colortemp_pic));
                    }
                    imgview.setOnClickListener(new AnonymousClass18(arg0, mDevice_pic_tick));
                    deviceName_tv = (TextView) arg1.findViewById(R.id.devicename_tv);
                    r = new Rect();
                    ChooseDeviceActivity.this.paint = new Paint();
                    ChooseDeviceActivity.this.paint.getTextBounds(deviceName_tv.getText().toString(), 0, deviceName_tv.getText().toString().length(), r);
                    if (r.width() > deviceName_tv.getWidth()) {
                        deviceName_tv.setMaxWidth(((int) deviceName_tv.getTextSize()) - (r.width() - deviceName_tv.getWidth()));
                    }
                    deviceName_tv.setText(((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceName());
                    ChooseDeviceActivity.this.paint = null;
                    seekBar = (SeekBar) arg1.findViewById(R.id.seekBar1);
                    seekBar1 = (SeekBar) arg1.findViewById(R.id.seekBar2);
                    SeekBar seekBar2 = (SeekBar) arg1.findViewById(R.id.seekBar3);
                    image_switch.setOnClickListener(new AnonymousClass19(LoadDevice, image_switch, seekBar, seekBar2, ib5, ib4, ib3));
                    seekBar.setOnSeekBarChangeListener(new AnonymousClass20(ib5, ib4, ib3, LoadDevice, arg0, seekBar1, image_switch));
                    seekBar1.setOnSeekBarChangeListener(new AnonymousClass21(ib5, ib4, ib3, LoadDevice, arg0, seekBar));
                    seekBar2.setOnSeekBarChangeListener(new AnonymousClass22(ib5, ib4, ib3, LoadDevice, arg0, seekBar, seekBar1, image_switch));
                    ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[0]));
                    ib3.setOnClickListener(new AnonymousClass23(ib3, LoadDevice, seekBar, image_switch, ib5, seekBar1, seekBar2));
                    ib4.setImageResource(ChooseDeviceActivity.this.image[0]);
                    ib4.setOnClickListener(new AnonymousClass24(ib4, LoadDevice, image_switch, seekBar, seekBar1, seekBar2, ib5));
                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                    ib5.setOnClickListener(new AnonymousClass25(LoadDevice, image_switch, seekBar, ib5, seekBar1, seekBar2));
                    LoadDevice.setIsstart(true);
                    if ((ChooseDeviceActivity.this.index == null || !ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) && ChooseDeviceActivity.this.mArea == null) {
                        if (ChooseDeviceActivity.this.mScene != null) {
                            if (LoadDevice.getSceneParams()[0] == -63) {
                                ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[LoadDevice.getSceneParams()[1] - 1]));
                                ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[LoadDevice.getSceneParams()[2] - 1]));
                                if (LoadDevice.getSceneParams()[3] == 1) {
                                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                                    LoadDevice.setIsstart(false);
                                } else {
                                    ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                                    LoadDevice.setIsstart(true);
                                }
                                LoadDevice.setClick(true);
                                image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                                seekBar.setProgress(100);
                            } else if (LoadDevice.getSceneParams()[0] == 17 || LoadDevice.getSceneParams()[0] == 0) {
                                seekBar.setProgress(LoadDevice.getSceneParams()[1]);
                                seekBar1.setProgress(100 - LoadDevice.getSceneParams()[2]);
                                if (LoadDevice.getSceneParams()[1] + LoadDevice.getSceneParams()[2] == 100) {
                                    seekBar2.setProgress(LoadDevice.getSceneParams()[1]);
                                }
                                if (seekBar.getProgress() == 0) {
                                    LoadDevice.setClick(false);
                                    image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                } else {
                                    LoadDevice.setClick(true);
                                    image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                                }
                            }
                        }
                    } else if (LoadDevice.getCurrentParams()[0] == -63) {
                        ib3.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image_ib4[LoadDevice.getCurrentParams()[1] - 1]));
                        ib4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(ChooseDeviceActivity.this.image[LoadDevice.getCurrentParams()[2] - 1]));
                        if (LoadDevice.getCurrentParams()[3] == 1) {
                            ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.hue_stop));
                            LoadDevice.setIsstart(false);
                        } else {
                            ib5.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.initiate));
                            LoadDevice.setIsstart(true);
                        }
                        LoadDevice.setClick(true);
                        image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        seekBar.setProgress(100);
                    } else if (LoadDevice.getCurrentParams()[0] == 17 || LoadDevice.getCurrentParams()[0] == 0) {
                        seekBar.setProgress(LoadDevice.getCurrentParams()[1]);
                        seekBar1.setProgress(100 - LoadDevice.getCurrentParams()[2]);
                        if (LoadDevice.getCurrentParams()[1] + LoadDevice.getCurrentParams()[2] == 100) {
                            seekBar2.setProgress(LoadDevice.getCurrentParams()[1]);
                        }
                        if (seekBar.getProgress() == 0) {
                            LoadDevice.setClick(false);
                            image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        } else {
                            LoadDevice.setClick(true);
                            image_switch.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        }
                    }
                    mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (j = 0; j < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); j++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).getDeviceName().equals(LoadDevice.getDeviceName())) {
                                ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setCurrentParams(LoadDevice.getCurrentParams());
                                if (ChooseDeviceActivity.this.mScene != null) {
                                    ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setSceneParams(LoadDevice.getSceneParams());
                                }
                                mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                                ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).setIschoose(true);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                case 4 /*4*/:
                    NorDevice = (Device) ChooseDeviceActivity.this.devicelist.get(arg0);
                    if (arg1 == null) {
                        arg1 = LayoutInflater.from(ChooseDeviceActivity.this).inflate(R.layout.sensorlight_item, null);
                    }
                    imgview = (ImageView) arg1.findViewById(R.id.imageView2);
                    mDevice_pic_tick = (ImageView) arg1.findViewById(R.id.device_pic_tick);
                    imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.sensor));
                    deviceName_tv = (TextView) arg1.findViewById(R.id.textView1);
                    imgview.setOnClickListener(new AnonymousClass26(arg0, mDevice_pic_tick));
                    r = new Rect();
                    paint = new Paint();
                    paint.getTextBounds(deviceName_tv.getText().toString(), 0, deviceName_tv.getText().toString().length(), r);
                    if (r.width() > deviceName_tv.getWidth()) {
                        deviceName_tv.setMaxWidth(((int) deviceName_tv.getTextSize()) - (r.width() - deviceName_tv.getWidth()));
                    }
                    deviceName_tv.setText(((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceName());
                    imageView4 = (ImageView) arg1.findViewById(R.id.imageView4);
                    imageView4.setOnClickListener(new AnonymousClass27(NorDevice, imageView4));
                    if ((ChooseDeviceActivity.this.index == null || !ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) && ChooseDeviceActivity.this.mArea == null) {
                        if (ChooseDeviceActivity.this.mScene != null) {
                            if (NorDevice.getSceneParams()[0] != 58) {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                NorDevice.setIsopen(false);
                            } else if (NorDevice.getSceneParams()[1] == 1) {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                                NorDevice.setIsopen(true);
                            } else {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                NorDevice.setIsopen(false);
                            }
                        }
                    } else if (NorDevice.getCurrentParams()[0] != 58) {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        NorDevice.setIsopen(false);
                    } else if (NorDevice.getCurrentParams()[1] == 1) {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        NorDevice.setIsopen(true);
                    } else {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        NorDevice.setIsopen(false);
                    }
                    mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (j = 0; j < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); j++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).getDeviceName().equals(NorDevice.getDeviceName())) {
                                ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setCurrentParams(NorDevice.getCurrentParams());
                                if (ChooseDeviceActivity.this.mScene != null) {
                                    ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setSceneParams(NorDevice.getSceneParams());
                                }
                                mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                                ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).setIschoose(true);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                case 5 /*5*/:
                    NorDevice = (Device) ChooseDeviceActivity.this.devicelist.get(arg0);
                    NorDevice.setCurrentParams(DatabaseManager.getInstance().getlightingofDevice((Device) ChooseDeviceActivity.this.devicelist.get(ChooseDeviceActivity.this.f24i)));
                    if (arg1 == null) {
                        arg1 = LayoutInflater.from(ChooseDeviceActivity.this).inflate(R.layout.curtain_item1, null);
                    }
                    imgview = (ImageView) arg1.findViewById(R.id.imageView2);
                    mDevice_pic_tick = (ImageView) arg1.findViewById(R.id.device_pic_tick);
                    if (NorDevice.getDeviceType() >= (short) 111) {
                        imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.curtain_group_pic));
                    } else {
                        imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.curtain_pic));
                    }
                    imgview.setOnClickListener(new AnonymousClass28(arg0, mDevice_pic_tick));
                    deviceName_tv = (TextView) arg1.findViewById(R.id.textView1);
                    r = new Rect();
                    ChooseDeviceActivity.this.paint = new Paint();
                    ChooseDeviceActivity.this.paint.getTextBounds(deviceName_tv.getText().toString(), 0, deviceName_tv.getText().toString().length(), r);
                    if (r.width() > deviceName_tv.getWidth()) {
                        deviceName_tv.setMaxWidth(((int) deviceName_tv.getTextSize()) - (r.width() - deviceName_tv.getWidth()));
                    }
                    deviceName_tv.setText(((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceName());
                    ChooseDeviceActivity.this.paint = null;
                    Button button1 = (Button) arg1.findViewById(R.id.button1);
                    Button button2 = (Button) arg1.findViewById(R.id.button2);
                    Button button3 = (Button) arg1.findViewById(R.id.button3);
                    button2.setTextColor(-9605779);
                    button1.setTextColor(-9605779);
                    button3.setTextColor(-9605779);
                    if ((ChooseDeviceActivity.this.index == null || !ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) && ChooseDeviceActivity.this.mArea == null) {
                        if (ChooseDeviceActivity.this.mScene != null) {
                            if (NorDevice.getSceneParams()[1] == 0) {
                                button2.setTextColor(-10104851);
                            } else if (NorDevice.getSceneParams()[1] == 1) {
                                button1.setTextColor(-10104851);
                            } else if (NorDevice.getSceneParams()[1] == 2) {
                                button3.setTextColor(-10104851);
                            }
                        }
                    } else if (NorDevice.getCurrentParams()[1] == 0) {
                        button2.setTextColor(-10104851);
                    } else if (NorDevice.getCurrentParams()[1] == 1) {
                        button1.setTextColor(-10104851);
                    } else if (NorDevice.getCurrentParams()[1] == 2) {
                        button3.setTextColor(-10104851);
                    }
                    button1.setOnClickListener(new AnonymousClass29(NorDevice, button1, button2, button3));
                    button2.setOnClickListener(new AnonymousClass30(NorDevice, button1, button2, button3));
                    button3.setOnClickListener(new AnonymousClass31(NorDevice, button1, button2, button3));
                    mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (j = 0; j < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); j++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).getDeviceName().equals(NorDevice.getDeviceName())) {
                                ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setCurrentParams(NorDevice.getCurrentParams());
                                if (ChooseDeviceActivity.this.mScene != null) {
                                    ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setSceneParams(NorDevice.getSceneParams());
                                }
                                mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                                ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).setIschoose(true);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                case 6 /*6*/:
                    NorDevice = (Device) ChooseDeviceActivity.this.devicelist.get(arg0);
                    if (arg1 == null) {
                        arg1 = LayoutInflater.from(ChooseDeviceActivity.this).inflate(R.layout.sensorlight_item, null);
                    }
                    imgview = (ImageView) arg1.findViewById(R.id.imageView2);
                    mDevice_pic_tick = (ImageView) arg1.findViewById(R.id.device_pic_tick);
                    imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.induction));
                    deviceName_tv = (TextView) arg1.findViewById(R.id.textView1);
                    imgview.setOnClickListener(new AnonymousClass32(arg0, mDevice_pic_tick));
                    r = new Rect();
                    paint = new Paint();
                    paint.getTextBounds(deviceName_tv.getText().toString(), 0, deviceName_tv.getText().toString().length(), r);
                    if (r.width() > deviceName_tv.getWidth()) {
                        deviceName_tv.setMaxWidth(((int) deviceName_tv.getTextSize()) - (r.width() - deviceName_tv.getWidth()));
                    }
                    deviceName_tv.setText(((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceName());
                    imageView4 = (ImageView) arg1.findViewById(R.id.imageView4);
                    imageView4.setOnClickListener(new AnonymousClass33(NorDevice, imageView4));
                    if ((ChooseDeviceActivity.this.index == null || !ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) && ChooseDeviceActivity.this.mArea == null) {
                        if (ChooseDeviceActivity.this.mScene != null) {
                            if (NorDevice.getSceneParams()[0] != 48) {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                NorDevice.setIsopen(false);
                            } else if (NorDevice.getSceneParams()[1] == 1) {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                                NorDevice.setIsopen(true);
                            } else {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                NorDevice.setIsopen(false);
                            }
                        }
                    } else if (NorDevice.getCurrentParams()[0] != 48) {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        NorDevice.setIsopen(false);
                    } else if (NorDevice.getCurrentParams()[1] == 1) {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        NorDevice.setIsopen(true);
                    } else {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        NorDevice.setIsopen(false);
                    }
                    mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (j = 0; j < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); j++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).getDeviceName().equals(NorDevice.getDeviceName())) {
                                ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setCurrentParams(NorDevice.getCurrentParams());
                                if (ChooseDeviceActivity.this.mScene != null) {
                                    ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setSceneParams(NorDevice.getSceneParams());
                                }
                                mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                                ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).setIschoose(true);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                case Event.EVENT_REQUEST_SYNC_DATA_RESULT /*7*/:
                    NorDevice = (Device) ChooseDeviceActivity.this.devicelist.get(arg0);
                    if (arg1 == null) {
                        arg1 = LayoutInflater.from(ChooseDeviceActivity.this).inflate(R.layout.sensorlight_item, null);
                    }
                    imgview = (ImageView) arg1.findViewById(R.id.imageView2);
                    mDevice_pic_tick = (ImageView) arg1.findViewById(R.id.device_pic_tick);
                    imgview.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.temperature));
                    deviceName_tv = (TextView) arg1.findViewById(R.id.textView1);
                    imgview.setOnClickListener(new AnonymousClass34(arg0, mDevice_pic_tick));
                    r = new Rect();
                    paint = new Paint();
                    paint.getTextBounds(deviceName_tv.getText().toString(), 0, deviceName_tv.getText().toString().length(), r);
                    if (r.width() > deviceName_tv.getWidth()) {
                        deviceName_tv.setMaxWidth(((int) deviceName_tv.getTextSize()) - (r.width() - deviceName_tv.getWidth()));
                    }
                    deviceName_tv.setText(((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).getDeviceName());
                    imageView4 = (ImageView) arg1.findViewById(R.id.imageView4);
                    imageView4.setOnClickListener(new AnonymousClass35(NorDevice, imageView4));
                    if ((ChooseDeviceActivity.this.index == null || !ChooseDeviceActivity.this.index.equals("AddAreaToChooseDevice")) && ChooseDeviceActivity.this.mArea == null) {
                        if (ChooseDeviceActivity.this.mScene != null) {
                            if (NorDevice.getSceneParams()[0] != 52) {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                NorDevice.setIsopen(false);
                            } else if (NorDevice.getSceneParams()[1] == 1) {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                                NorDevice.setIsopen(true);
                            } else {
                                imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                                NorDevice.setIsopen(false);
                            }
                        }
                    } else if (NorDevice.getCurrentParams()[0] != 52) {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        NorDevice.setIsopen(false);
                    } else if (NorDevice.getCurrentParams()[1] == 1) {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.open));
                        NorDevice.setIsopen(true);
                    } else {
                        imageView4.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.close));
                        NorDevice.setIsopen(false);
                    }
                    mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick));
                    if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                        for (j = 0; j < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); j++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).getDeviceName().equals(NorDevice.getDeviceName())) {
                                ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setCurrentParams(NorDevice.getCurrentParams());
                                if (ChooseDeviceActivity.this.mScene != null) {
                                    ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(j)).setSceneParams(NorDevice.getSceneParams());
                                }
                                mDevice_pic_tick.setImageDrawable(ChooseDeviceActivity.this.getResources().getDrawable(R.drawable.device_pic_tick_choose));
                                ((Device) ChooseDeviceActivity.this.devicelist.get(arg0)).setIschoose(true);
                                break;
                            }
                        }
                        break;
                    }
                    break;
            }
            return arg1;
        }
    }

    class ThreadGC extends Thread {
        boolean StopThread;

        ThreadGC() {
            this.StopThread = true;
        }

        void StopThreadGC() {
            this.StopThread = false;
        }

        public void run() {
            super.run();
            while (this.StopThread) {
                if (!(ChooseDeviceActivity.this.bitMaplist.size() <= 2 || ChooseDeviceActivity.this.bitMaplist.get(0) == null || ((Bitmap) ChooseDeviceActivity.this.bitMaplist.get(0)).isRecycled())) {
                    ((Bitmap) ChooseDeviceActivity.this.bitMaplist.get(0)).recycle();
                    ChooseDeviceActivity.this.bitMaplist.remove(0);
                }
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*  Valeria changed
        class WeightListen implements OnClickListener {
            WeightListen() {
            }

            public void onClick(View v) {
                String AreaName;
                Intent intent;
                String SceneName;
                switch (v.getId()) {
                    case R.id.btn_back_choosedevice:
                        ChooseDeviceActivity.this.ThreadRunning = false;
                        if ((ChooseDeviceActivity.this.index != null && ChooseDeviceActivity.this.index.toString().equals("AddAreaToChooseDevice")) || DataStorage.getInstance(ChooseDeviceActivity.this).getInt("PageIndex") == 0) {
                            AreaName = ChooseDeviceActivity.this.getIntent().getStringExtra("AreaName");
                            intent = new Intent(ChooseDeviceActivity.this, UserPage.class);
                            intent.putExtra("AreaName", AreaName);
                            if (ChooseDeviceActivity.this.picturePath != null) {
                                intent.putExtra("picturePath", ChooseDeviceActivity.this.picturePath);
                            } else if (ChooseDeviceActivity.this.photo != null) {
                                intent.putExtra("photo", ChooseDeviceActivity.this.photo);
                            }
                            ChooseDeviceActivity.this.startActivity(intent);
                            ChooseDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            ChooseDeviceActivity.this.finish();
                        } else if (ChooseDeviceActivity.this.mArea != null || DataStorage.getInstance(ChooseDeviceActivity.this).getInt("PageIndex") == 1) {
                            AreaName = ChooseDeviceActivity.this.getIntent().getStringExtra("AreaName");
                            intent = new Intent(ChooseDeviceActivity.this, UserPage.class);
                            intent.putExtra("AreaName", AreaName);
                            intent.putExtra("area", ChooseDeviceActivity.this.mArea);
                            if (ChooseDeviceActivity.this.picturePath != null) {
                                intent.putExtra("picturePath", ChooseDeviceActivity.this.picturePath);
                            } else if (ChooseDeviceActivity.this.photo != null) {
                                intent.putExtra("photo", ChooseDeviceActivity.this.photo);
                            }
                            ChooseDeviceActivity.this.startActivity(intent);
                            ChooseDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            ChooseDeviceActivity.this.finish();
                        } else if ((ChooseDeviceActivity.this.index != null && ChooseDeviceActivity.this.index.toString().equals("AddSceneToChooseDevice")) || DataStorage.getInstance(ChooseDeviceActivity.this).getInt("PageIndex") == 2) {
                            SceneName = ChooseDeviceActivity.this.getIntent().getStringExtra("SceneName");
                            intent = new Intent(ChooseDeviceActivity.this, UserPage.class);
                            intent.putExtra("SceneName", SceneName);
                            if (ChooseDeviceActivity.this.picturePath != null) {
                                intent.putExtra("picturePath", ChooseDeviceActivity.this.picturePath);
                            } else if (ChooseDeviceActivity.this.photo != null) {
                                intent.putExtra("photo", ChooseDeviceActivity.this.photo);
                            }
                            ChooseDeviceActivity.this.startActivity(intent);
                            ChooseDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            ChooseDeviceActivity.this.finish();
                        } else if (ChooseDeviceActivity.this.mScene != null || DataStorage.getInstance(ChooseDeviceActivity.this).getInt("PageIndex") == 3) {
                            SceneName = ChooseDeviceActivity.this.getIntent().getStringExtra("SceneName");
                            intent = new Intent(ChooseDeviceActivity.this, UserPage.class);
                            intent.putExtra("scene", ChooseDeviceActivity.this.mScene);
                            intent.putExtra("SceneName", SceneName);
                            if (ChooseDeviceActivity.this.picturePath != null) {
                                intent.putExtra("picturePath", ChooseDeviceActivity.this.picturePath);
                            } else if (ChooseDeviceActivity.this.photo != null) {
                                intent.putExtra("photo", ChooseDeviceActivity.this.photo);
                            }
                            ChooseDeviceActivity.this.startActivity(intent);
                            ChooseDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            ChooseDeviceActivity.this.finish();
                        }
                    case R.id.sequence:
                        ChooseDeviceActivity.this.sequence.setBackgroundResource(R.drawable.seque_click);
                        ChooseDeviceActivity.this.time_sequence.setBackgroundResource(R.drawable.time_seque);
                        ChooseDeviceActivity.this.sequence.setTextColor(-1);
                        ChooseDeviceActivity.this.time_sequence.setTextColor(-14701071);
                        SysApplication.getInstance();
                        SysApplication.mSequece = true;
                        if (SysApplication.mSequece) {
                            Collections.sort(ChooseDeviceActivity.this.devicelist);
                        }
                        ChooseDeviceActivity.this.adapter.notifyDataSetChanged();
                    case R.id.time_sequence:
                        ChooseDeviceActivity.this.sequence.setBackgroundResource(R.drawable.seque);
                        ChooseDeviceActivity.this.time_sequence.setBackgroundResource(R.drawable.time_seque_click);
                        ChooseDeviceActivity.this.time_sequence.setTextColor(-1);
                        ChooseDeviceActivity.this.sequence.setTextColor(-14701071);
                        SysApplication.getInstance();
                        SysApplication.mSequece = false;
                        if (ChooseDeviceActivity.this.mScene != null) {
                            ChooseDeviceActivity.this.allDeviceList = DatabaseManager.getInstance().getDeviceListExceptKnobandsenor();
                        } else if (ChooseDeviceActivity.this.mArea != null) {
                            ChooseDeviceActivity.this.allDeviceList = DatabaseManager.getInstance().getDeviceListExceptKnobandsenor();
                        }
                        if (ChooseDeviceActivity.this.allDeviceList != null && ChooseDeviceActivity.this.allDeviceList.getmDeviceList() != null) {
                            ChooseDeviceActivity.this.devicelist = ChooseDeviceActivity.this.allDeviceList.getmDeviceList();
                            ChooseDeviceActivity.this.adapter.notifyDataSetChanged();
                        }
                    case R.id.btn_save_choosedevice:
                        int i;
                        for (i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                            if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getSceneParams()[0] == 0) {
                                short devicetype = ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceType();
                                Device device;
                                byte[] bArr;
                                if (devicetype == (short) 1 || devicetype == (short) 97) {
                                    device = (Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i);
                                    bArr = new byte[5];
                                    bArr[0] = (byte) -64;
                                    device.setSceneParams(bArr);
                                } else if (devicetype == (short) 2 || devicetype == (short) 98 || devicetype == (short) 3 || devicetype == (short) 99 || devicetype == (short) 4 || devicetype == (short) 100 || devicetype == (short) 15 || devicetype == (short) 111) {
                                    device = (Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i);
                                    bArr = new byte[5];
                                    bArr[0] = (byte) 17;
                                    device.setSceneParams(bArr);
                                } else if (devicetype == (short) 5) {
                                    if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getSubDeviceType() == (short) 1) {
                                        device = (Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i);
                                        bArr = new byte[5];
                                        bArr[0] = (byte) 48;
                                        device.setSceneParams(bArr);
                                    } else if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getSubDeviceType() == (short) 3) {
                                        device = (Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i);
                                        bArr = new byte[5];
                                        bArr[0] = (byte) 52;
                                        device.setSceneParams(bArr);
                                    } else {
                                        device = (Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i);
                                        bArr = new byte[5];
                                        bArr[0] = (byte) 58;
                                        device.setSceneParams(bArr);
                                    }
                                }
                            }
                        }
                        if ((ChooseDeviceActivity.this.index != null && ChooseDeviceActivity.this.index.toString().equals("AddAreaToChooseDevice")) || DataStorage.getInstance(ChooseDeviceActivity.this).getInt("PageIndex") == 0) {
                            AreaName = ChooseDeviceActivity.this.getIntent().getStringExtra("AreaName");
                            intent = new Intent(ChooseDeviceActivity.this, UserPage.class);
                            intent.putExtra("mDeviceListOfCurrAreaOrScene", ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene);
                            for (i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                            }
                            intent.putExtra("AreaName", AreaName);
                            if (ChooseDeviceActivity.this.picturePath != null) {
                                intent.putExtra("picturePath", ChooseDeviceActivity.this.picturePath);
                            } else if (ChooseDeviceActivity.this.photo != null) {
                                intent.putExtra("photo", ChooseDeviceActivity.this.photo);
                            }
                            ChooseDeviceActivity.this.startActivity(intent);
                            ChooseDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            ChooseDeviceActivity.this.finish();
                        } else if (ChooseDeviceActivity.this.mArea != null || DataStorage.getInstance(ChooseDeviceActivity.this).getInt("PageIndex") == 1) {
                            AreaName = ChooseDeviceActivity.this.getIntent().getStringExtra("AreaName");
                            intent = new Intent(ChooseDeviceActivity.this, UserPage.class);
                            intent.putExtra("AreaName", AreaName);
                            intent.putExtra("area", ChooseDeviceActivity.this.mArea);
                            if (ChooseDeviceActivity.this.picturePath != null) {
                                intent.putExtra("picturePath", ChooseDeviceActivity.this.picturePath);
                            } else if (ChooseDeviceActivity.this.photo != null) {
                                intent.putExtra("photo", ChooseDeviceActivity.this.photo);
                            }
                            intent.putExtra("mDeviceListOfCurrAreaOrScene", ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene);
                            ChooseDeviceActivity.this.startActivity(intent);
                            ChooseDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            ChooseDeviceActivity.this.finish();
                        } else if ((ChooseDeviceActivity.this.index != null && ChooseDeviceActivity.this.index.toString().equals("AddSceneToChooseDevice")) || DataStorage.getInstance(ChooseDeviceActivity.this).getInt("PageIndex") == 2) {
                            SceneName = ChooseDeviceActivity.this.getIntent().getStringExtra("SceneName");
                            intent = new Intent(ChooseDeviceActivity.this, UserPage.class);
                            intent.putExtra("mDeviceListOfCurrAreaOrScene", ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene);
                            intent.putExtra("SceneName", SceneName);
                            if (ChooseDeviceActivity.this.picturePath != null) {
                                intent.putExtra("picturePath", ChooseDeviceActivity.this.picturePath);
                            } else if (ChooseDeviceActivity.this.photo != null) {
                                intent.putExtra("photo", ChooseDeviceActivity.this.photo);
                            }
                            ChooseDeviceActivity.this.startActivity(intent);
                            ChooseDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            ChooseDeviceActivity.this.finish();
                        } else if (ChooseDeviceActivity.this.mScene != null || DataStorage.getInstance(ChooseDeviceActivity.this).getInt("PageIndex") == 3) {
                            SceneName = ChooseDeviceActivity.this.getIntent().getStringExtra("SceneName");
                            intent = new Intent(ChooseDeviceActivity.this, UserPage.class);
                            intent.putExtra("scene", ChooseDeviceActivity.this.mScene);
                            if (ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene != null && ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size() > 0) {
                                for (int j = 0; j < ChooseDeviceActivity.this.devicelist.size(); j++) {
                                    for (i = 0; i < ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.size(); i++) {
                                        if (((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).getDeviceName().equals(((Device) ChooseDeviceActivity.this.devicelist.get(j)).getDeviceName())) {
                                            ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).setSceneParams(((Device) ChooseDeviceActivity.this.devicelist.get(j)).getSceneParams());
                                            ((Device) ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene.get(i)).setSceneDeviceMac(((Device) ChooseDeviceActivity.this.devicelist.get(j)).getMacAddress());
                                        }
                                    }
                                }
                            }
                            intent.putExtra("mDeviceListOfCurrAreaOrScene", ChooseDeviceActivity.mDeviceListOfCurrAreaOrScene);
                            intent.putExtra("SceneName", SceneName);
                            if (ChooseDeviceActivity.this.picturePath != null) {
                                intent.putExtra("picturePath", ChooseDeviceActivity.this.picturePath);
                            } else if (ChooseDeviceActivity.this.photo != null) {
                                intent.putExtra("photo", ChooseDeviceActivity.this.photo);
                            }
                            ChooseDeviceActivity.this.startActivity(intent);
                            ChooseDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            ChooseDeviceActivity.this.finish();
                        }
                    default:
                }
            }
        }
    */
    public ChooseDeviceActivity() {
        this.mList = null;
        this.devicelist = null;
        this.mChoosedeviceBackButton = null;
        this.mChoosedeviceSaveButton = null;
        this.mArea = null;
        this.mScene = null;
        this.index = null;
        this.mDevice = null;
        this.mBackUpParams = null;
        this.ThreadRunning = true;
        this.photo = null;
        this.picturePath = null;
        this.paint = null;
        this.f24i = 0;
        this.adapter = null;
        this.bitMaplist = new ArrayList();
        this.f23a = 0;
        this.effectpar = new byte[5];
        this.deviceParmats = new byte[5];

        //Valeria
        //this.sequence = null;
        // this.time_sequence = null;
        this.thread = null;
        this.R_1 = MotionEventCompat.ACTION_MASK;
        this.G_1 = 245;
        this.B_1 = 210;
        this.effect_hue = 1;
        this.delay_hue = 1;
        this.mThreadGC = null;
        this.allDeviceList = null;

        this.image = new int[]{R.drawable.time_interval_1, R.drawable.time_interval_2, R.drawable.time_interval_3, R.drawable.time_interval_4, R.drawable.time_interval_5, R.drawable.time_interval_6, R.drawable.time_interval_7, R.drawable.time_interval_8, R.drawable.time_interval_9, R.drawable.time_interval_10};
        this.image_ib3 = new int[]{R.drawable.effect_saltus, R.drawable.call_police, R.drawable.effect_glint, R.drawable.effect_shade, R.drawable.candela};
        this.image_ib4 = new int[]{R.drawable.effect_saltus, R.drawable.effect_shade, R.drawable.candela};

        this.DM = null;
        this.bitmap = null;
        this.bitmapwidth = 0;
        this.bitmapheight = 0;
        this.bitmapsmlwidth = 0;
        this.bitmapsmlheight = 0;
        this.bit = null;
        this.newrs = 0;
        this.newbs = 0;
        this.newls = 0;
        this.newts = 0;
        this.mdialog = null;
        this.inflater = null;
    }

    static {
        mDeviceListOfCurrAreaOrScene = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosedevice);
        SysApplication.getInstance().addActivity(this);
        this.inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        findViewsById();
        if (DatabaseManager.getInstance().mDBHelper == null) {
            DatabaseManager.getInstance().DatabaseInit(this);
        }
        this.bit = BitmapFactory.decodeResource(getResources(), R.drawable.bulb);
        this.DM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.DM);
        this.bitmapwidth = this.DM.widthPixels;
        this.bitmapheight = this.DM.heightPixels / 2;

        //Valeria
        /*
        if (SysApplication.mSequece) {
            SysApplication.getInstance();
            if (SysApplication.mSequece) {
                this.sequence.setBackgroundResource(R.drawable.seque_click);
                this.time_sequence.setBackgroundResource(R.drawable.time_seque);
                this.sequence.setTextColor(-1);
                this.time_sequence.setTextColor(-14701071);
            }
        } else {
            this.sequence.setBackgroundResource(R.drawable.seque);
            this.time_sequence.setBackgroundResource(R.drawable.time_seque_click);
            this.time_sequence.setTextColor(-1);
            this.sequence.setTextColor(-14701071);
        }
        */
        this.mArea = (Area) getIntent().getSerializableExtra("area");
        this.mScene = (Scene) getIntent().getSerializableExtra("scene");
        this.index = getIntent().getStringExtra("index");
        this.photo = getIntent().getStringExtra("photo");
        this.picturePath = getIntent().getStringExtra("picturePath");
        /* Valeria
        this.mChoosedeviceBackButton.setOnClickListener(new WeightListen());
        this.mChoosedeviceSaveButton.setOnClickListener(new WeightListen());
        this.time_sequence.setOnClickListener(new WeightListen());
        this.sequence.setOnClickListener(new WeightListen());
        */
        this.mThreadGC = new ThreadGC();
        this.mThreadGC.start();
        this.thread = new ChangeBrightthread();
        this.thread.start();
        this.mBackUpParams = new BackUpParams();
        LoadDeviceList();
    }

    protected void onDestroy() {

        super.onDestroy();
        this.mArea = null;
        this.image = null;
        this.mScene = null;
        this.index = null;
        this.paint = null;
        this.mDevice = null;
        this.image_ib3 = null;
        this.image_ib4 = null;
        this.mBackUpParams = null;
        this.adapter = null;
        this.deviceParmats = null;
        this.effectpar = null;
        this.photo = null;
        this.picturePath = null;
        this.DM = null;
        this.mdialog = null;
        if (this.mList != null) {
            this.mList.setAdapter(null);
            this.mList = null;
        }
        if (!(this.bit == null || this.bit.isRecycled())) {
            this.bit.recycle();
            this.bit = null;
        }
        if (!(this.bitmap == null || this.bitmap.isRecycled())) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        this.allDeviceList = null;
        mDeviceListOfCurrAreaOrScene = null;
        this.devicelist = null;
        if (this.bitMaplist != null) {
            int i = 0;
            while (i < this.bitMaplist.size()) {
                if (!(this.bitMaplist.get(i) == null || ((Bitmap) this.bitMaplist.get(i)).isRecycled())) {
                    ((Bitmap) this.bitMaplist.get(i)).recycle();
                }
                i++;
            }
            this.bitMaplist.clear();
            this.bitMaplist = null;
        }
        if (this.mThreadGC != null) {
            this.mThreadGC.StopThreadGC();
            this.mThreadGC = null;
        }
        if (this.thread != null) {
            this.thread.StopChangeBrightthread();
            this.thread = null;
        }
        System.gc();
    }

    private void findViewsById() {
        this.mList = (ListView) findViewById(R.id.listView1);
        this.mChoosedeviceBackButton = (Button) findViewById(R.id.btn_back_choosedevice);
        this.mChoosedeviceSaveButton = (Button) findViewById(R.id.btn_save_choosedevice);

        //Valeria
        //this.time_sequence = (Button) findViewById(R.id.time_sequence);
        //this.sequence = (Button) findViewById(R.id.sequence);
    }

    private void LoadDeviceList() {

        this.allDeviceList = DatabaseManager.getInstance().getDeviceListExceptKnobandsenor();
        if (this.allDeviceList != null && this.allDeviceList.getmDeviceList() != null) {
            this.devicelist = this.allDeviceList.getmDeviceList();
            if (SysApplication.mSequece) {
                Collections.sort(this.devicelist);
            }
            if (this.devicelist != null) {
                for (int i = 0; i < this.devicelist.size(); i++) {

                    short devicetype = ((Device) this.devicelist.get(i)).getDeviceType();
                    Device device;
                    byte[] bArr;
                    if (devicetype == (short) 1 || devicetype == (short) 97) {
                        device = (Device) this.devicelist.get(i);
                        bArr = new byte[5];
                        bArr[0] = (byte) -64;
                        device.setSceneParams(bArr);
                    } else if (devicetype == (short) 2 || devicetype == (short) 98 || devicetype == (short) 3 || devicetype == (short) 99 || devicetype == (short) 4 || devicetype == (short) 100 || devicetype == (short) 15 || devicetype == (short) 111) {
                        device = (Device) this.devicelist.get(i);
                        bArr = new byte[5];
                        bArr[0] = (byte) 17;
                        device.setSceneParams(bArr);
                    } else if (devicetype == (short) 5) {
                        if (((Device) this.devicelist.get(i)).getSubDeviceType() == (short) 1) {
                            device = (Device) this.devicelist.get(i);
                            bArr = new byte[5];
                            bArr[0] = (byte) 48;
                            device.setSceneParams(bArr);
                        } else if (((Device) this.devicelist.get(i)).getSubDeviceType() == (short) 3) {
                            device = (Device) this.devicelist.get(i);
                            bArr = new byte[5];
                            bArr[0] = (byte) 52;
                            device.setSceneParams(bArr);
                        } else {
                            device = (Device) this.devicelist.get(i);
                            bArr = new byte[5];
                            bArr[0] = (byte) 58;
                            device.setSceneParams(bArr);
                        }
                    }

                }

                this.adapter = new MyAdapter();
                this.mList.setAdapter(this.adapter);
                this.mList.setRecyclerListener(new C01751());
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.ThreadRunning = false;
            String AreaName;
            Intent intent;
            String SceneName = null;
            if ((this.index != null && this.index.toString().equals("AddAreaToChooseDevice")) || DataStorage.getInstance(this).getInt("PageIndex") == 0) {
                AreaName = getIntent().getStringExtra("AreaName");
                intent = new Intent(this, UserPage.class);
                intent.putExtra("AreaName", AreaName);
                if (this.picturePath != null) {
                    intent.putExtra("picturePath", this.picturePath);
                } else if (this.photo != null) {
                    intent.putExtra("photo", this.photo);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
                return true;
            } else if (this.mArea != null || DataStorage.getInstance(this).getInt("PageIndex") == 1) {
                AreaName = getIntent().getStringExtra("AreaName");
                intent = new Intent(this, UserPage.class);
                intent.putExtra("AreaName", AreaName);
                intent.putExtra("area", this.mArea);
                if (this.picturePath != null) {
                    intent.putExtra("picturePath", this.picturePath);
                } else if (this.photo != null) {
                    intent.putExtra("photo", this.photo);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
                return true;
            } else if ((this.index != null && this.index.toString().equals("AddSceneToChooseDevice")) || DataStorage.getInstance(this).getInt("PageIndex") == 2) {
                SceneName = getIntent().getStringExtra("SceneName");
                intent = new Intent(this, UserPage.class);
                intent.putExtra("SceneName", SceneName);
                if (this.picturePath != null) {
                    intent.putExtra("picturePath", this.picturePath);
                } else if (this.photo != null) {
                    intent.putExtra("photo", this.photo);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
                return true;
            } else if (this.mScene != null || DataStorage.getInstance(this).getInt("PageIndex") == 3) {
                SceneName = getIntent().getStringExtra("SceneName");
                intent = new Intent(this, UserPage.class);
                intent.putExtra("scene", this.mScene);
                intent.putExtra("SceneName", SceneName);
                if (this.picturePath != null) {
                    intent.putExtra("picturePath", this.picturePath);
                } else if (this.photo != null) {
                    intent.putExtra("photo", this.photo);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
