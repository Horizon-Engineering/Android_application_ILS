package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allin.activity.action.DataStorage;
import com.allin.activity.action.SysApplication;
import com.allin.activity.action.SysApplication.AdnNameLengthFilter;
import com.homa.hls.database.Area;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;
import com.homa.hls.database.Scene;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Event;
import com.homa.hls.socketconn.DeviceSocket;
import com.homa.hls.widgetcustom.CustomProgressDialog;
import com.zxing.activity.CaptureActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ZxingToAddDeviceActivity extends Activity {
    TextView adddevice_savetextview;
    ArrayList<Area> areaList;
    Bitmap bm;
    int[] bottomGridViewImg;
    private int[] bottomGridViewMenuImg;
    String[] bottomGridViewMenuText;
    String[] bottomGridViewText;
    GridViewMenuAdapter bottomMenuAdapter;
    RelativeLayout choose_area;
    TextView choose_area_text;
    TextView choose_devicetype_text;
    TextView deviceAddress;
    TextView devicetype_text;
    EditText editText;
    private GridView gridViewMenu;
    HashMap<String, Object> hashMap;
    int ibackindex;
    ImageView img_devicetype;
    LayoutInflater inflater;
    boolean isClick;
    boolean isResume;
    ArrayAdapter<String> mAdapter;
    String mAreaData;
    Button mBackButton;
    Spinner mChooseButton;
    Area mCurArea;
    private Device mDevice;
    ArrayList<Device> mDeviceList;
    public Handler mHandler;
    Button mSaveButton;
    Dialog mdialog;
    String picturePath;
    private CustomProgressDialog progressDialog;
    boolean ret;
    List<String> spinnerlist;
    View[] view;

    /* renamed from: com.allin.activity.ZxingToAddDeviceActivity.1 */
    class C03841 extends Handler {
        C03841() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Event.EVENT_GET_POST_DEVTYPE /*36*/:
                    SysApplication.getInstance().removeEvent("ZxingToAddDeviceActivity", 36);
                    ZxingToAddDeviceActivity.this.stopProgressDialog();
                    if (msg.arg1 == 1) {
                        byte[] devdata = (byte[])msg.obj;
                        if (devdata == null) {
                            ZxingToAddDeviceActivity.this.DialogTip(ZxingToAddDeviceActivity.this.getResources().getString(R.string.add_fail_langdown));
                            return;
                        }
                        if (((short) ((devdata[1] & MotionEventCompat.ACTION_MASK) | ((short) ((devdata[0] & MotionEventCompat.ACTION_MASK) << 8)))) != ZxingToAddDeviceActivity.this.mDevice.getDeviceAddress()) {
                            ZxingToAddDeviceActivity.this.DialogTip(ZxingToAddDeviceActivity.this.getResources().getString(R.string.add_fail_langdown));
                            return;
                        }
                        short mdevtype = devdata[2];
                        if (mdevtype > (short) 4 || mdevtype <= (short) 0) {
                            mdevtype = (short) 1;
                        }
                        ZxingToAddDeviceActivity.this.mDevice.setSubDeviceType(mdevtype);
                        ZxingToAddDeviceActivity.this.mDevice.setChannelInfo((short) 1);
                        ZxingToAddDeviceActivity.this.mDevice.setMacAddress(new int[3]);
                        Gateway curGateway = SysApplication.getInstance().getCurrGateway(ZxingToAddDeviceActivity.this);
                        /*
                        if (curGateway != null && DatabaseManager.getInstance().addDevice(ZxingToAddDeviceActivity.this.mDevice, ZxingToAddDeviceActivity.this.mCurArea)) {
                            if (ZxingToAddDeviceActivity.this.mdialog != null && ZxingToAddDeviceActivity.this.mdialog.isShowing()) {
                                ZxingToAddDeviceActivity.this.mdialog.cancel();
                                ZxingToAddDeviceActivity.this.mdialog = null;
                            }
                            ZxingToAddDeviceActivity.this.mDevice.setGatewayMacAddr(curGateway.getMacAddress());
                            ZxingToAddDeviceActivity.this.mDevice.setGatewayPassword(curGateway.getPassWord());
                            ZxingToAddDeviceActivity.this.mDevice.setGatewaySSID(curGateway.getSSID());
                            DatabaseManager.getInstance().AddGateWayDevice(DatabaseManager.getInstance().SelectLimitDeviceIndex(), curGateway.getGateWayInfoIndex());
                            ZxingToAddDeviceActivity.this.addsuccess();
                        }
                        ZxingToAddDeviceActivity.this.mDeviceList = null;
                        */
                    } else if (msg.arg1 == 0) {
                        ZxingToAddDeviceActivity.this.DialogTip(ZxingToAddDeviceActivity.this.getResources().getString(R.string.add_fail_langdown));
                    }
                default:
            }
        }
    }

    /* renamed from: com.allin.activity.ZxingToAddDeviceActivity.2 */
    class C03852 implements OnClickListener {
        C03852() {
        }

        public void onClick(View v) {
            if (ZxingToAddDeviceActivity.this.mdialog != null && ZxingToAddDeviceActivity.this.mdialog.isShowing()) {
                ZxingToAddDeviceActivity.this.mdialog.cancel();
                ZxingToAddDeviceActivity.this.mdialog = null;
            }
            ZxingToAddDeviceActivity.this.startActivityForResult(new Intent(ZxingToAddDeviceActivity.this, CaptureActivity.class), 0);
            ZxingToAddDeviceActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    /* renamed from: com.allin.activity.ZxingToAddDeviceActivity.3 */
    class C03863 implements OnKeyListener {
        C03863() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode != 66) {
                return false;
            }
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            return true;
        }
    }

    /* renamed from: com.allin.activity.ZxingToAddDeviceActivity.4 */
    class C03874 implements OnItemClickListener {
        C03874() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(ZxingToAddDeviceActivity.this, AdminPage.class);
            intent.putExtra("mainactivity", arg2);
            ZxingToAddDeviceActivity.this.startActivity(intent);
            ZxingToAddDeviceActivity.this.overridePendingTransition(0, 0);
            ZxingToAddDeviceActivity.this.finish();
        }
    }

    /* renamed from: com.allin.activity.ZxingToAddDeviceActivity.5 */
    class C03885 implements OnClickListener {
        C03885() {
        }

        public void onClick(View v) {
            if (ZxingToAddDeviceActivity.this.mdialog != null && ZxingToAddDeviceActivity.this.mdialog.isShowing()) {
                ZxingToAddDeviceActivity.this.mdialog.cancel();
                ZxingToAddDeviceActivity.this.mdialog = null;
            }
        }
    }

    /* renamed from: com.allin.activity.ZxingToAddDeviceActivity.6 */
    class C03896 implements OnItemSelectedListener {
        C03896() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            ZxingToAddDeviceActivity.this.mCurArea = (Area) ZxingToAddDeviceActivity.this.areaList.get(arg2);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class GridViewMenuAdapter extends BaseAdapter {
        GridViewMenuAdapter() {
        }

        public int getCount() {
            return 3;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                arg1 = LayoutInflater.from(ZxingToAddDeviceActivity.this).inflate(R.layout.bottom_menu_item, null);
            }
            ImageView img = (ImageView) arg1.findViewById(R.id.img_item_data);
            ((TextView) arg1.findViewById(R.id.text_item_data)).setText(ZxingToAddDeviceActivity.this.bottomGridViewMenuText[arg0]);
//            img.setImageResource(ZxingToAddDeviceActivity.this.bottomGridViewMenuImg[arg0]);
            return arg1;
        }
    }






    class WeightListening implements OnClickListener {

        /* renamed from: com.allin.activity.AddDeviceActivity.WeightListening.1 */
        class C00391 implements OnItemClickListener {
            C00391() {
            }

            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                ZxingToAddDeviceActivity.this.mDevice.setDeviceType((short)(arg2 + 1));
                if (ZxingToAddDeviceActivity.this.mDevice.getDeviceType() < 1 || ZxingToAddDeviceActivity.this.mDevice.getDeviceType() > 5) {
                    ZxingToAddDeviceActivity.this.mDevice.setDeviceType((short)1);
                }
                DataStorage.getInstance(ZxingToAddDeviceActivity.this).putInt("add_device_type", ZxingToAddDeviceActivity.this.mDevice.getDeviceType());
                String[] deviceText = ZxingToAddDeviceActivity.this.getResources().getStringArray(R.array.choose_devicetype_text_data);
                ZxingToAddDeviceActivity.this.img_devicetype.setImageResource(ZxingToAddDeviceActivity.this.bottomGridViewImg[ZxingToAddDeviceActivity.this.mDevice.getDeviceType() - 1]);
                ZxingToAddDeviceActivity.this.devicetype_text.setText(deviceText[ZxingToAddDeviceActivity.this.mDevice.getDeviceType() - 1]);
                ZxingToAddDeviceActivity.this.mdialog.cancel();
                ZxingToAddDeviceActivity.this.mdialog = null;
            }
        }
        /* renamed from: com.allin.activity.ZxingToAddDeviceActivity.WeightListening.2 */
        class C03912 implements OnClickListener {
            C03912() {
            }

            public void onClick(View v) {
                if (ZxingToAddDeviceActivity.this.mDevice != null) {
                    boolean boolclick = true;
                    byte[] params = new byte[5];
                    if (ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == (short) 1) {
                        params[0] = (byte) -62;
                        params[2] = (byte) 0;
                        params[3] = (byte) 0;
                        params[4] = (byte) 0;
                        if (ZxingToAddDeviceActivity.this.isClick) {
                            params[1] = (byte) 0;
                        } else {
                            params[1] = (byte) 1;
                        }
                    } else if (ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == (short) 2 || ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == (short) 3 || ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == (short) 4) {
                        params[0] = (byte) 17;
                        params[3] = (byte) 0;
                        params[4] = (byte) 0;
                        if (ZxingToAddDeviceActivity.this.isClick) {
                            params[1] = (byte) 0;
                            params[2] = (byte) 0;
                        } else {
                            params[1] = (byte) 100;
                            params[2] = (byte) 50;
                        }
                    } else if (ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == (short) 8 || ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == (short) 9 || ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == (short) 13) {
                        params[0] = (byte) -31;
                        params[1] = (byte) 0;
                        params[2] = (byte) 0;
                        params[3] = (byte) 0;
                        params[4] = (byte) 0;
                        boolclick = false;
                    } else if (ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == (short) 15) {
                        params[0] = (byte) 17;
                        params[2] = (byte) 0;
                        params[3] = (byte) 0;
                        params[4] = (byte) 0;
                        if (ZxingToAddDeviceActivity.this.isClick) {
                            params[1] = (byte) 0;
                        } else {
                            params[1] = (byte) 1;
                        }
                    }
                    DeviceSocket.getInstance().send(com.homa.hls.datadeal.Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, ZxingToAddDeviceActivity.this.mDevice.getDeviceAddress(), (short) 0, params), ZxingToAddDeviceActivity.this.mDevice.getGatewayMacAddr(), ZxingToAddDeviceActivity.this.mDevice.getGatewayPassword(), ZxingToAddDeviceActivity.this.mDevice.getGatewaySSID(), ZxingToAddDeviceActivity.this));
                    if (boolclick) {
                        boolean z;
                        ZxingToAddDeviceActivity access$0 = ZxingToAddDeviceActivity.this;
                        if (ZxingToAddDeviceActivity.this.isClick) {
                            z = false;
                        } else {
                            z = true;
                        }
                        access$0.isClick = z;
                        ZxingToAddDeviceActivity.this.mDevice.setCurrentParams(params);
                        DatabaseManager.getInstance().updateDevice(ZxingToAddDeviceActivity.this.mDevice);
                    }
                }
            }
        }

        WeightListening() {
        }

        public void onClick(View paramView) {
            ((InputMethodManager) ZxingToAddDeviceActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(paramView.getWindowToken(), 0);
            View view;
            int i;
            switch (paramView.getId()) {
                case R.id.btn_back_adddevice:

                    Intent intent = new Intent(ZxingToAddDeviceActivity.this, AdminPage.class);
                    intent.putExtra("mainactivity", ZxingToAddDeviceActivity.this.ibackindex);
                    ZxingToAddDeviceActivity.this.startActivity(intent);
                    ZxingToAddDeviceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    ZxingToAddDeviceActivity.this.finish();

                case R.id.choose_devicetype_btn:
                    view = ZxingToAddDeviceActivity.this.inflater.inflate(R.layout.choose_devicetype, null);
                    ZxingToAddDeviceActivity.this.mdialog = new Dialog(ZxingToAddDeviceActivity.this, R.style.Theme_dialog);
                    ZxingToAddDeviceActivity.this.mdialog.setContentView(view);
                    ZxingToAddDeviceActivity.this.mdialog.setCancelable(true);
                    ZxingToAddDeviceActivity.this.mdialog.setCanceledOnTouchOutside(false);
                    ZxingToAddDeviceActivity.this.mdialog.show();
                    GridView mdevicetype = (GridView) view.findViewById(R.id.gridView1);
                    ArrayList<HashMap<String, Object>> arrayList = new ArrayList();
                    for (i = 0; i < ZxingToAddDeviceActivity.this.bottomGridViewText.length; i++) {
                        HashMap<String, Object> hashMap = new HashMap();
                        hashMap.put("imgItem", Integer.valueOf(ZxingToAddDeviceActivity.this.bottomGridViewImg[i]));
                        hashMap.put("textItem", ZxingToAddDeviceActivity.this.bottomGridViewText[i]);
                        arrayList.add(hashMap);
                    }
                    mdevicetype.setAdapter(new SimpleAdapter(ZxingToAddDeviceActivity.this, arrayList, R.layout.choosedevicetype_gridview_item, new String[]{"imgItem", "textItem"}, new int[]{R.id.img_item, R.id.text_item}));
                    mdevicetype.setOnItemClickListener(new C00391());
                case R.id.adddevice_savebtn:
                    String devicename = ZxingToAddDeviceActivity.this.editText.getText().toString();

                    if (devicename.equals("") || ZxingToAddDeviceActivity.this._findDeviceName(devicename)) {
                        if (devicename.equals("")) {
                            ZxingToAddDeviceActivity.this.DialogTip(ZxingToAddDeviceActivity.this.getResources().getString(R.string.add_fail_name));
                            return;
                        } else if (ZxingToAddDeviceActivity.this._findDeviceName(devicename)) {
                            ZxingToAddDeviceActivity.this.DialogTip(ZxingToAddDeviceActivity.this.getResources().getString(R.string.add_fail_ov));
                            return;
                        } else {
                            ZxingToAddDeviceActivity.this.DialogTip(ZxingToAddDeviceActivity.this.getResources().getString(R.string.add_fail));
                            return;
                        }
                    }
                    if (ZxingToAddDeviceActivity.this.mDevice.getDeviceType() == 5) {
                        ZxingToAddDeviceActivity.this.mDevice.setDeviceType((short) 15);
                    } else {
                        ZxingToAddDeviceActivity.this.mDevice.setDeviceType((short) ZxingToAddDeviceActivity.this.mDevice.getDeviceType());
                    }
                    ZxingToAddDeviceActivity.this.mDevice.setDeviceName(devicename);
                    ZxingToAddDeviceActivity.this.mDevice.setChannelMark((short) 1);
                    Gateway gateways = SysApplication.getInstance().getCurrGateway(ZxingToAddDeviceActivity.this);
                    if (ZxingToAddDeviceActivity.this._findDeviceAddress(ZxingToAddDeviceActivity.this.mDevice.getDeviceAddress()))
                    {
                        ZxingToAddDeviceActivity.this.DialogTip("Device alreay there");
                        return;
                    }else{
                        if (gateways==null)
                        {
                            ZxingToAddDeviceActivity.this.DialogTip("Gateway is not right");
                            return;
                        }else
                        {
                            ArrayList<Device> deviceArrayList = DatabaseManager.getInstance().LoadDeviceList("devicelist");
                            DatabaseManager.getInstance().addDevice(ZxingToAddDeviceActivity.this.mDevice,null);
                            deviceArrayList.add(ZxingToAddDeviceActivity.this.mDevice);
                            DatabaseManager.getInstance().WriteDeviceList(deviceArrayList,"devicelist");
                        }


                    }

                    byte[] bArr;
                    ZxingToAddDeviceActivity.this.mDevice.setGatewayMacAddr(gateways.getMacAddress());
                    ZxingToAddDeviceActivity.this.mDevice.setGatewayPassword(gateways.getPassWord());
                    ZxingToAddDeviceActivity.this.mDevice.setGatewaySSID(gateways.getSSID());
                    //.getInstance().AddGateWayDevice(DatabaseManager.getInstance().SelectLimitDeviceIndex(), gateways.getGateWayInfoIndex());
                    //DeviceSocket.getInstance().send(com.homa.hls.datadeal.Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, ZxingToAddDeviceActivity.this.mDevice.getDeviceAddress(), (short) 0, new byte[]{(byte) -96, (byte) ZxingToAddDeviceActivity.this.mDevice.getDeviceType(), (byte) 0, (byte) 0, (byte) 0}), ZxingToAddDeviceActivity.this.mDevice.getGatewayMacAddr(), ZxingToAddDeviceActivity.this.mDevice.getGatewayPassword(), ZxingToAddDeviceActivity.this.mDevice.getGatewaySSID(), ZxingToAddDeviceActivity.this));
                    //DatabaseManager.getInstance().addDevice(ZxingToAddDeviceActivity.this.mDevice, null);
                    //DeviceSocket.getInstance().send(com.homa.hls.datadeal.Message.createMessage((byte) 4, DevicePacket.createPacket((byte) 4, ZxingToAddDeviceActivity.this.mDevice.getDeviceAddress(), (short) 0, new byte[]{(byte) -96, (byte) ZxingToAddDeviceActivity.this.mDevice.getDeviceType(), (byte) 0, (byte) 0, (byte) 0}), ZxingToAddDeviceActivity.this.mDevice.getGatewayMacAddr(), ZxingToAddDeviceActivity.this.mDevice.getGatewayPassword(), ZxingToAddDeviceActivity.this.mDevice.getGatewaySSID(), ZxingToAddDeviceActivity.this));

                    /*
                    i = 0;
                    while (i < ZxingToAddDeviceActivity.this.areaList.size()) {
                        if (((Area) ZxingToAddDeviceActivity.this.areaList.get(i)).getAreaName().equals("\u6240\u6709\u8bbe\u5907") || ((Area) ZxingToAddDeviceActivity.this.areaList.get(i)).getAreaName().equals("All devices") || ((Area) ZxingToAddDeviceActivity.this.areaList.get(i)).getAreaName().equals("Alle Ger\u00e4te")) {
                            DatabaseManager.getInstance().addAreaDeviceTable(ZxingToAddDeviceActivity.this.mDevice, (Area) ZxingToAddDeviceActivity.this.areaList.get(i));
                        }
                        i++;
                    }
                    ArrayList<Scene> sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                    Device adddDevice = ZxingToAddDeviceActivity.this.mDevice;
                    i = 0;
                    while (i < sceneList.size()) {
                        if (((Scene) sceneList.get(i)).getSceneName().equals("\u5168\u5f00") || ((Scene) sceneList.get(i)).getSceneName().equals("All On") || ((Scene) sceneList.get(i)).getSceneName().equals("Alles ein")) {
                            if (adddDevice.getDeviceType() == (short) 1) {
                                bArr = new byte[5];
                                bArr[0] = (byte) -62;
                                bArr[1] = (byte) 1;
                                adddDevice.setSceneParams(bArr);
                            } else if (adddDevice.getDeviceType() == (short) 2 || adddDevice.getDeviceType() == (short) 3) {
                                bArr = new byte[5];
                                bArr[0] = (byte) 17;
                                bArr[1] = (byte) 100;
                                adddDevice.setSceneParams(bArr);
                            } else if (adddDevice.getDeviceType() == (short) 4) {
                                bArr = new byte[5];
                                bArr[0] = (byte) 17;
                                bArr[1] = (byte) 100;
                                bArr[2] = (byte) 50;
                                adddDevice.setSceneParams(bArr);
                            } else if (adddDevice.getDeviceType() == (short) 15) {
                                bArr = new byte[5];
                                bArr[0] = (byte) 17;
                                bArr[1] = (byte) 1;
                                adddDevice.setSceneParams(bArr);
                            }
                            DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                        } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u5168\u5173") || ((Scene) sceneList.get(i)).getSceneName().equals("All Off") || ((Scene) sceneList.get(i)).getSceneName().equals("Alles aus")) {
                            if (adddDevice.getDeviceType() == (short) 1) {
                                bArr = new byte[5];
                                bArr[0] = (byte) -62;
                                adddDevice.setSceneParams(bArr);
                            } else if (adddDevice.getDeviceType() == (short) 2 || adddDevice.getDeviceType() == (short) 3 || adddDevice.getDeviceType() == (short) 4 || adddDevice.getDeviceType() == (short) 15) {
                                bArr = new byte[5];
                                bArr[0] = (byte) 17;
                                adddDevice.setSceneParams(bArr);
                            }
                            DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                        }
                        i++;
                    }
                    boolean boolishavescene;
                    String[] strscenes;
                    Scene mScene1;
                    if (adddDevice.getDeviceType() == (short) 1) {
                        boolishavescene = false;
                        if (!DataStorage.getInstance(ZxingToAddDeviceActivity.this).getBoolean("SCENEA_INIT")) {
                            i = 0;
                            while (i < sceneList.size()) {
                                boolishavescene = true;
                                if (!(((Scene) sceneList.get(i)).getSceneName().equals("\u65e5\u843d") || ((Scene) sceneList.get(i)).getSceneName().equals("Sunset") || ((Scene) sceneList.get(i)).getSceneName().equals("Sonnenschein") || ((Scene) sceneList.get(i)).getSceneName().equals("\u6d77\u6d0b") || ((Scene) sceneList.get(i)).getSceneName().equals("Sea") || ((Scene) sceneList.get(i)).getSceneName().equals("See") || ((Scene) sceneList.get(i)).getSceneName().equals("\u7af9\u6797") || ((Scene) sceneList.get(i)).getSceneName().equals("Bamboo") || ((Scene) sceneList.get(i)).getSceneName().equals("Bambus") || ((Scene) sceneList.get(i)).getSceneName().equals("\u6a31\u82b1") || ((Scene) sceneList.get(i)).getSceneName().equals("Sakura") || ((Scene) sceneList.get(i)).getSceneName().equals("Kirschbl\u00fcten"))) {
                                    boolishavescene = false;
                                }
                                if (!boolishavescene) {
                                    i++;
                                } else if (!boolishavescene) {
                                    strscenes = ZxingToAddDeviceActivity.this.getResources().getStringArray(R.array.init_scene_text_data);
                                    for (String sceneName : strscenes) {
                                        mScene1 = new Scene();
                                        mScene1.setSceneName(sceneName);
                                        DatabaseManager.getInstance().addScene(mScene1);
                                    }
                                    sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                                    DataStorage.getInstance(ZxingToAddDeviceActivity.this).putBoolean("SCENEA_INIT", true);
                                }
                            }
                            if (boolishavescene) {
                                strscenes = ZxingToAddDeviceActivity.this.getResources().getStringArray(R.array.init_scene_text_data);
                                for (String sceneName : strscenes) {
                                    mScene1 = new Scene();
                                    mScene1.setSceneName(sceneName);
                                    DatabaseManager.getInstance().addScene(mScene1);
                                }
                                sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                                DataStorage.getInstance(ZxingToAddDeviceActivity.this).putBoolean("SCENEA_INIT", true);
                            }
                        }
                        if (!DataStorage.getInstance(ZxingToAddDeviceActivity.this).getBoolean("SCENEB_INIT")) {
                            boolishavescene = false;
                            i = 0;
                            while (i < sceneList.size()) {
                                boolishavescene = true;
                                if (!(((Scene) sceneList.get(i)).getSceneName().equals("\u6d3b\u529b") || ((Scene) sceneList.get(i)).getSceneName().equals("Energizer") || ((Scene) sceneList.get(i)).getSceneName().equals("Lebendigkeit") || ((Scene) sceneList.get(i)).getSceneName().equals("\u653e\u677e") || ((Scene) sceneList.get(i)).getSceneName().equals("Relax") || ((Scene) sceneList.get(i)).getSceneName().equals("Relaxen") || ((Scene) sceneList.get(i)).getSceneName().equals("\u4e13\u6ce8") || ((Scene) sceneList.get(i)).getSceneName().equals("Concentrate") || ((Scene) sceneList.get(i)).getSceneName().equals("Konzentrieren") || ((Scene) sceneList.get(i)).getSceneName().equals("\u9605\u8bfb") || ((Scene) sceneList.get(i)).getSceneName().equals("Reading") || ((Scene) sceneList.get(i)).getSceneName().equals("Lesen"))) {
                                    boolishavescene = false;
                                }
                                if (!boolishavescene) {
                                    i++;
                                } else if (!boolishavescene) {
                                    strscenes = ZxingToAddDeviceActivity.this.getResources().getStringArray(R.array.init_scene_text_data1);
                                    for (String sceneName2 : strscenes) {
                                        mScene1 = new Scene();
                                        mScene1.setSceneName(sceneName2);
                                        DatabaseManager.getInstance().addScene(mScene1);
                                    }
                                    sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                                    DataStorage.getInstance(ZxingToAddDeviceActivity.this).putBoolean("SCENEB_INIT", true);
                                }
                            }
                            if (boolishavescene) {
                                strscenes = ZxingToAddDeviceActivity.this.getResources().getStringArray(R.array.init_scene_text_data1);
                                for (String sceneName : strscenes) {
                                    mScene1 = new Scene();
                                    mScene1.setSceneName(sceneName);
                                    DatabaseManager.getInstance().addScene(mScene1);
                                }
                                sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                                DataStorage.getInstance(ZxingToAddDeviceActivity.this).putBoolean("SCENEB_INIT", true);
                            }
                        }
                        i = 0;
                        while (i < sceneList.size()) {
                            if (((Scene) sceneList.get(i)).getSceneName().equals("\u6d3b\u529b") || ((Scene) sceneList.get(i)).getSceneName().equals("Energizer") || ((Scene) sceneList.get(i)).getSceneName().equals("Lebendigkeit")) {
                                adddDevice.setSceneParams(new byte[]{(byte) -64, (byte) -1, (byte) -1, (byte) -1, (byte) 100});
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u65e5\u843d") || ((Scene) sceneList.get(i)).getSceneName().equals("Sunset") || ((Scene) sceneList.get(i)).getSceneName().equals("Sonnenschein")) {
                                bArr = new byte[5];
                                bArr[0] = (byte) -64;
                                bArr[1] = (byte) -1;
                                bArr[2] = (byte) 15;
                                bArr[4] = (byte) 100;
                                adddDevice.setSceneParams(bArr);
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u6d77\u6d0b") || ((Scene) sceneList.get(i)).getSceneName().equals("Sea") || ((Scene) sceneList.get(i)).getSceneName().equals("See")) {
                                adddDevice.setSceneParams(new byte[]{(byte) -64, (byte) 10, (byte) 15, (byte) -1, (byte) 100});
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u7af9\u6797") || ((Scene) sceneList.get(i)).getSceneName().equals("Bamboo") || ((Scene) sceneList.get(i)).getSceneName().equals("Bambus")) {
                                adddDevice.setSceneParams(new byte[]{(byte) -64, (byte) 10, (byte) -1, (byte) 10, (byte) 100});
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u6a31\u82b1") || ((Scene) sceneList.get(i)).getSceneName().equals("Sakura") || ((Scene) sceneList.get(i)).getSceneName().equals("Kirschbl\u00fcten")) {
                                adddDevice.setSceneParams(new byte[]{(byte) -64, (byte) -1, (byte) 40, (byte) -1, (byte) 100});
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u653e\u677e") || ((Scene) sceneList.get(i)).getSceneName().equals("Relax") || ((Scene) sceneList.get(i)).getSceneName().equals("Relaxen")) {
                                adddDevice.setSceneParams(new byte[]{(byte) -64, (byte) -1, (byte) 115, (byte) 15, (byte) 100});
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u4e13\u6ce8") || ((Scene) sceneList.get(i)).getSceneName().equals("Concentrate") || ((Scene) sceneList.get(i)).getSceneName().equals("Konzentrieren")) {
                                adddDevice.setSceneParams(new byte[]{(byte) -64, (byte) -56, (byte) -1, (byte) -1, (byte) 100});
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u9605\u8bfb") || ((Scene) sceneList.get(i)).getSceneName().equals("Reading") || ((Scene) sceneList.get(i)).getSceneName().equals("Lesen")) {
                                adddDevice.setSceneParams(new byte[]{(byte) -64, (byte) -1, (byte) -56, (byte) 120, (byte) 100});
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            }
                            i++;
                        }
                    } else if (adddDevice.getDeviceType() == (short) 4) {
                        if (!DataStorage.getInstance(ZxingToAddDeviceActivity.this).getBoolean("SCENEB_INIT")) {
                            boolishavescene = false;
                            i = 0;
                            while (i < sceneList.size()) {
                                boolishavescene = true;
                                if (!(((Scene) sceneList.get(i)).getSceneName().equals("\u6d3b\u529b") || ((Scene) sceneList.get(i)).getSceneName().equals("Energizer") || ((Scene) sceneList.get(i)).getSceneName().equals("Lebendigkeit") || ((Scene) sceneList.get(i)).getSceneName().equals("\u653e\u677e") || ((Scene) sceneList.get(i)).getSceneName().equals("Relax") || ((Scene) sceneList.get(i)).getSceneName().equals("Relaxen") || ((Scene) sceneList.get(i)).getSceneName().equals("\u4e13\u6ce8") || ((Scene) sceneList.get(i)).getSceneName().equals("Concentrate") || ((Scene) sceneList.get(i)).getSceneName().equals("Konzentrieren") || ((Scene) sceneList.get(i)).getSceneName().equals("\u9605\u8bfb") || ((Scene) sceneList.get(i)).getSceneName().equals("Reading") || ((Scene) sceneList.get(i)).getSceneName().equals("Lesen"))) {
                                    boolishavescene = false;
                                }
                                if (!boolishavescene) {
                                    i++;
                                } else if (!boolishavescene) {
                                    strscenes = ZxingToAddDeviceActivity.this.getResources().getStringArray(R.array.init_scene_text_data1);
                                    for (String sceneName22 : strscenes) {
                                        mScene1 = new Scene();
                                        mScene1.setSceneName(sceneName22);
                                        DatabaseManager.getInstance().addScene(mScene1);
                                    }
                                    sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                                    DataStorage.getInstance(ZxingToAddDeviceActivity.this).putBoolean("SCENEB_INIT", true);
                                }
                            }
                            if (boolishavescene) {
                                strscenes = ZxingToAddDeviceActivity.this.getResources().getStringArray(R.array.init_scene_text_data1);
                                for (String sceneName22 : strscenes) {
                                    mScene1 = new Scene();
                                    mScene1.setSceneName(sceneName22);
                                    DatabaseManager.getInstance().addScene(mScene1);
                                }
                                sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                                DataStorage.getInstance(ZxingToAddDeviceActivity.this).putBoolean("SCENEB_INIT", true);
                            }
                        }
                        i = 0;
                        while (i < sceneList.size()) {
                            if (((Scene) sceneList.get(i)).getSceneName().equals("\u6d3b\u529b") || ((Scene) sceneList.get(i)).getSceneName().equals("Energizer") || ((Scene) sceneList.get(i)).getSceneName().equals("Lebendigkeit")) {
                                bArr = new byte[5];
                                bArr[0] = (byte) 17;
                                bArr[1] = (byte) 100;
                                adddDevice.setSceneParams(bArr);
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u653e\u677e") || ((Scene) sceneList.get(i)).getSceneName().equals("Relax") || ((Scene) sceneList.get(i)).getSceneName().equals("Relaxen")) {
                                bArr = new byte[5];
                                bArr[0] = (byte) 17;
                                bArr[1] = (byte) 100;
                                bArr[2] = (byte) 100;
                                adddDevice.setSceneParams(bArr);
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u4e13\u6ce8") || ((Scene) sceneList.get(i)).getSceneName().equals("Concentrate") || ((Scene) sceneList.get(i)).getSceneName().equals("Konzentrieren")) {
                                bArr = new byte[5];
                                bArr[0] = (byte) 17;
                                bArr[1] = (byte) 100;
                                bArr[2] = (byte) 20;
                                adddDevice.setSceneParams(bArr);
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u9605\u8bfb") || ((Scene) sceneList.get(i)).getSceneName().equals("Reading") || ((Scene) sceneList.get(i)).getSceneName().equals("Lesen")) {
                                bArr = new byte[5];
                                bArr[0] = (byte) 17;
                                bArr[1] = (byte) 100;
                                bArr[2] = (byte) 60;
                                adddDevice.setSceneParams(bArr);
                                DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                            }
                            i++;
                        }
                    }
                    */
                    view = ZxingToAddDeviceActivity.this.inflater.inflate(R.layout.msg_dialog, null);
                    Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
                    Button btn_no = (Button) view.findViewById(R.id.btn_cancel);
                    btn_no.setText("Back to Admin");
                    btn_ok.setText("Scan Another");
                    ((TextView) view.findViewById(R.id.textinfor)).setText(ZxingToAddDeviceActivity.this.getResources().getString(R.string.add_success));
                    ZxingToAddDeviceActivity.this.mdialog = new Dialog(ZxingToAddDeviceActivity.this, R.style.Theme_dialog);
                    ZxingToAddDeviceActivity.this.mdialog.setContentView(view);
                    ZxingToAddDeviceActivity.this.mdialog.setCancelable(true);
                    ZxingToAddDeviceActivity.this.mdialog.setCanceledOnTouchOutside(false);
                    ZxingToAddDeviceActivity.this.mdialog.show();
                    btn_ok.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ZxingToAddDeviceActivity.this.startActivityForResult(new Intent(ZxingToAddDeviceActivity.this, CaptureActivity.class), 0);
                            ZxingToAddDeviceActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    });
                    btn_no.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(v.getContext(), AdminPage.class);
                            startActivity(intent1);
                        }
                    });

                default:
            }
        }

    }






    class sendFindposttypeThread extends Thread {
        short devaddr;
        Gateway gateways;

        public sendFindposttypeThread(short devaddr, Gateway gateways) {
            this.devaddr = (short) 0;
            this.gateways = null;
            this.devaddr = devaddr;
            this.gateways = gateways;
        }

        public void run() {
            super.run();
            SysApplication.getInstance().subscibeEvent("ZxingToAddDeviceActivity", 36, ZxingToAddDeviceActivity.this.mHandler);
            SysApplication.getInstance();
            SysApplication.boolissend = true;
            DeviceSocket.getInstance().send(com.homa.hls.datadeal.Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, Byte.MAX_VALUE, (short) 2, (short) 0, new byte[]{(byte) ((this.devaddr >> 8) & MotionEventCompat.ACTION_MASK), (byte) (this.devaddr & MotionEventCompat.ACTION_MASK)}), this.gateways.getMacAddress(), this.gateways.getPassWord(), this.gateways.getSSID(), ZxingToAddDeviceActivity.this));
            try {
                sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SysApplication.getInstance();
            if (SysApplication.boolissend) {
                SysApplication.getInstance();
                SysApplication.boolissend = false;
                SysApplication.getInstance().broadcastEvent(36, 0, null);
            }
        }
    }

    public ZxingToAddDeviceActivity() {
        this.bottomGridViewMenuImg = null;
        this.gridViewMenu = null;
        this.mBackButton = null;
        this.mSaveButton = null;
        this.mChooseButton = null;
        this.img_devicetype = null;
        this.editText = null;
        this.deviceAddress = null;
        this.choose_area_text = null;
        this.devicetype_text = null;
        this.choose_devicetype_text = null;
        this.adddevice_savetextview = null;
        this.choose_area = null;
        this.picturePath = null;
        this.mDevice = null;
        this.ibackindex = 0;
        this.ret = false;
        this.areaList = null;
        this.spinnerlist = null;
        this.mAdapter = null;
        this.mAreaData = null;
        this.mCurArea = null;
        this.view = null;
        this.isClick = true;
        this.isResume = true;
        this.bm = null;
        this.hashMap = null;
        this.bottomMenuAdapter = null;
        this.bottomGridViewMenuText = null;
        this.bottomGridViewText = null;
        this.mdialog = null;
        this.progressDialog = null;
        this.inflater = null;
        this.bottomGridViewImg = new int[]{R.drawable.device_pic_border, R.drawable.rgbw_pic, R.drawable.panel_pic, R.drawable.colortemp_pic, R.drawable.curtain_pic};
        this.mDeviceList = null;
        this.mHandler = new C03841();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddevice);
        SysApplication.getInstance().addActivity(this);
        this.inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        findViewsById();
        this.spinnerlist = new ArrayList();
        if (DatabaseManager.getInstance().mDBHelper == null) {
            DatabaseManager.getInstance().DatabaseInit(this);
        }
        this.bottomGridViewText = getResources().getStringArray(R.array.choose_devicetype_text_data);
        loadBottomGridViewMenu();
        WeightListening();
        LoadSpinnerData();
        this.mDevice = (Device) getIntent().getSerializableExtra("mDevice");
        this.ibackindex = getIntent().getIntExtra("back_to_activity", 0);
        if (this.mDevice != null) {
            _showLighting(this.mDevice);
            return;
        }
        this.mDevice = new Device();
        _showLighting(this.mDevice);
    }

    private boolean addsuccess() {
        View view = this.inflater.inflate(R.layout.msg_dialog_ok, null);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        ((TextView) view.findViewById(R.id.textinfor)).setText(getResources().getString(R.string.add_success));
        this.mdialog = new Dialog(this, R.style.Theme_dialog);
        this.mdialog.setContentView(view);
        this.mdialog.setCancelable(true);
        this.mdialog.setCanceledOnTouchOutside(false);
        this.mdialog.show();
        btn_ok.setOnClickListener(new C03852());
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        SysApplication.getInstance().removeEvent("ZxingToAddDeviceActivity", 36);
        this.mCurArea = null;
        this.mAreaData = null;
        this.mDevice = null;
        this.view = null;
        this.mdialog = null;
        this.picturePath = null;
        this.bottomGridViewText = null;
        this.bottomGridViewImg = null;
        this.bottomGridViewMenuText = null;
        if (this.gridViewMenu != null) {
            this.gridViewMenu.setAdapter(null);
            this.gridViewMenu = null;
        }
        this.areaList = null;
        if (this.bottomMenuAdapter != null) {
            this.bottomMenuAdapter = null;
        }
        if (this.hashMap != null) {
            this.hashMap.clear();
            this.hashMap = null;
        }
        if (this.bottomGridViewMenuImg != null) {
            this.bottomGridViewMenuImg = null;
        }
        if (this.spinnerlist != null) {
            this.spinnerlist.clear();
            this.spinnerlist = null;
        }
        if (!(this.bm == null || this.bm.isRecycled())) {
            this.bm.recycle();
            this.bm = null;
        }
        if (this.mAdapter != null) {
            this.mAdapter.clear();
            this.mAdapter = null;
        }
        this.mDeviceList = null;
        stopProgressDialog();
        System.gc();
    }

    private void findViewsById() {
        this.gridViewMenu = (GridView) findViewById(R.id.scene_adddevice_set);
        this.mBackButton = (Button) findViewById(R.id.btn_back_adddevice);
        this.mSaveButton = (Button) findViewById(R.id.adddevice_savebtn);
        this.editText = (EditText) findViewById(R.id.editText1);
        this.editText.setOnKeyListener(new C03863());
        this.editText.setFilters(new InputFilter[]{new AdnNameLengthFilter()});
        this.deviceAddress = (TextView) findViewById(R.id.device_addr);
        this.mChooseButton = (Spinner) findViewById(R.id.choose_area_btn);
        this.img_devicetype = (ImageView) findViewById(R.id.choose_devicetype_btn);
        this.img_devicetype.setImageResource(R.drawable.device_pic_border);
        this.choose_area_text = (TextView) findViewById(R.id.choose_area_text);
        this.devicetype_text = (TextView) findViewById(R.id.devicetype_text);
        this.choose_devicetype_text = (TextView) findViewById(R.id.choose_devicetype_text);
        this.choose_devicetype_text.setText(R.string.devicetype);
        this.adddevice_savetextview = (TextView) findViewById(R.id.adddevice_savetextview);
        this.choose_area = (RelativeLayout) findViewById(R.id.choose_area);
    }

    private void loadBottomGridViewMenu() {
        this.bottomGridViewMenuText = getResources().getStringArray(R.array.bottom_menu_text_data);
        this.bottomMenuAdapter = new GridViewMenuAdapter();
        this.gridViewMenu.setAdapter(this.bottomMenuAdapter);
        this.gridViewMenu.setSelector(new ColorDrawable(0));
        this.gridViewMenu.setOnItemClickListener(new C03874());
    }

    private void WeightListening() {
        this.mBackButton.setOnClickListener(new WeightListening());
        this.mSaveButton.setOnClickListener(new WeightListening());
    }

    private void startProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = CustomProgressDialog.createDialog(this);
            this.progressDialog.setMessage(getResources().getString(R.string.deal));
        }
        this.progressDialog.show();
    }

    private void stopProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    private void DialogTip(String message) {
        if (this.mdialog != null && this.mdialog.isShowing()) {
            this.mdialog.cancel();
            this.mdialog = null;
        }
        View view = this.inflater.inflate(R.layout.msg_dialog_ok, null);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        ((TextView) view.findViewById(R.id.textinfor)).setText(message);
        this.mdialog = new Dialog(this, R.style.Theme_dialog);
        this.mdialog.setContentView(view);
        this.mdialog.setCancelable(true);
        this.mdialog.setCanceledOnTouchOutside(false);
        this.mdialog.show();
        btn_ok.setOnClickListener(new C03885());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            Intent intent = new Intent(this, HomePage.class);
            intent.putExtra("mainactivity", this.ibackindex);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void LoadSpinnerData() {
        this.areaList = DatabaseManager.getInstance().getAreaList().getAreaArrayList();
        boolean boolcheck = false;
        int i = 0;
        while (i < this.areaList.size()) {
            if (!boolcheck && (((Area) this.areaList.get(i)).getAreaName().equals("\u6240\u6709\u8bbe\u5907") || ((Area) this.areaList.get(i)).getAreaName().equals("All devices") || ((Area) this.areaList.get(i)).getAreaName().equals("Alle Ger\u00e4te"))) {
                ((Area) this.areaList.get(i)).setAreaName(getResources().getString(R.string.alllights));
                boolcheck = true;
            }
            this.spinnerlist.add(((Area) this.areaList.get(i)).getAreaName());
            i++;
        }
        this.mAdapter = new ArrayAdapter(this, R.layout.spinnerstyle, this.spinnerlist);
        this.mChooseButton.setAdapter(this.mAdapter);
        this.mChooseButton.setOnItemSelectedListener(new C03896());
    }

    private boolean _findDeviceName(String deviceName) {
        if (this.mDeviceList == null) {
            this.mDeviceList = DatabaseManager.getInstance().getDeviceList().getmDeviceList();
        }
        if (this.mDeviceList != null) {
            Iterator it = this.mDeviceList.iterator();
            while (it.hasNext()) {
                if (((Device) it.next()).getDeviceName().equalsIgnoreCase(deviceName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean _findDeviceAddress(short deviceAddress) {
        if (this.mDeviceList == null) {
            this.mDeviceList = DatabaseManager.getInstance().getDeviceList().getmDeviceList();
        }
        if (this.mDeviceList == null) {
            return false;
        }
        Iterator it = this.mDeviceList.iterator();
        while (it.hasNext()) {
            if (((Device) it.next()).getDeviceAddress() == deviceAddress) {
                return true;
            }
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 0) {
            Toast.makeText(this, getResources().getString(R.string.scanerfail), Toast.LENGTH_LONG).show();
        } else if (resultCode == -1) {
            boolean boolresu = false;
            System.out.println("*************************" + data.getExtras().getString("result"));
            String contents = data.getExtras().getString("result");
            if (contents == null) {
                Intent intent = new Intent(this, AdminPage.class);
                intent.putExtra("mainactivity", this.ibackindex);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
                return;
            }
            if (contents != null && contents.length() == 7) {
                int devtype;
                int subdevtype = 0;
                if (Integer.parseInt(contents.substring(1, 2)) == 2 && Integer.parseInt(contents.substring(0, 1)) == 5) {
                    subdevtype = 2;
                    devtype = 5;
                } else if (Integer.parseInt(contents.substring(1, 2)) != 1) {
                    devtype = Integer.parseInt(contents.substring(0, 2));
                } else {
                    devtype = Integer.parseInt(contents.substring(0, 1));
                }
                String deviceaddress = contents.substring(2, contents.length());
                if (Integer.parseInt(deviceaddress) <= 65500) {
                    this.mDevice = new Device();
                    if (subdevtype > 0) {
                        try {
                            this.mDevice.setSubDeviceType((short) subdevtype);
                        } catch (Exception e) {
                            Toast.makeText(this, getResources().getString(R.string.scanerfail), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    this.mDevice.setChannelInfo((short) 1);
                    this.mDevice.setDeviceType((short) devtype);
                    this.mDevice.setDeviceAddress((short) Integer.parseInt(deviceaddress));
                    _showLighting(this.mDevice);
                    this.editText.setText("");
                    boolresu = true;
                }
            }
            if (contents != null && !boolresu) {
                DialogTip(getResources().getString(R.string.qrcode_err));
            }
        }
    }


    private void _showLighting(Device mDevice) {
        this.ret = false;
        this.mChooseButton.setVisibility(View.INVISIBLE);
        this.choose_area_text.setVisibility(View.INVISIBLE);
        this.adddevice_savetextview.setVisibility(View.VISIBLE);
        this.choose_area.setVisibility(View.INVISIBLE);
        if (mDevice.getDeviceType() == (short) 8) {
            this.img_devicetype.setImageResource(R.drawable.knob);
            this.devicetype_text.setText(getResources().getString(R.string.knob_device));
            this.choose_area.setVisibility(View.VISIBLE);
        } else if (mDevice.getDeviceType() == (short) 9) {
            this.img_devicetype.setImageResource(R.drawable.knob_temp);
            this.devicetype_text.setText(getResources().getString(R.string.knob_Temp));
            this.choose_area.setVisibility(View.VISIBLE);
        } else if (mDevice.getDeviceType() == (short) 13) {
            this.img_devicetype.setImageResource(R.drawable.knob_rgb);
            this.devicetype_text.setText(getResources().getString(R.string.knob_Rgb));
            this.choose_area.setVisibility(View.VISIBLE);
        } else if (mDevice.getDeviceType() == (short) 43) {
            this.img_devicetype.setImageResource(R.drawable.knob_temp_t);
            this.devicetype_text.setText(getResources().getString(R.string.knob_temp_t));
            this.choose_area.setVisibility(View.VISIBLE);
        } else if (mDevice.getDeviceType() == (short) 44) {
            this.img_devicetype.setImageResource(R.drawable.knob_t);
            this.devicetype_text.setText(getResources().getString(R.string.knob_t));
            this.choose_area.setVisibility(View.VISIBLE);
        } else if (mDevice.getDeviceType() == (short) 7) {
            this.img_devicetype.setImageResource(R.drawable.post);
            this.devicetype_text.setText(getResources().getString(R.string.post));
            this.adddevice_savetextview.setVisibility(View.INVISIBLE);
            this.adddevice_savetextview.setText(getResources().getString(R.string.post_tip));
            this.choose_area.setVisibility(View.VISIBLE);
        } else if (mDevice.getDeviceType() == (short) 6) {
            this.img_devicetype.setImageResource(R.drawable.remote);
            this.devicetype_text.setText(getResources().getString(R.string.remote));
            this.choose_area.setVisibility(View.VISIBLE);
        } else if (mDevice.getDeviceType() == (short) 5) {
            this.img_devicetype.setImageResource(R.drawable.sensor);
            this.devicetype_text.setText(getResources().getString(R.string.sensor));
        } else if (mDevice.getDeviceType() == (short) 15) {
            this.img_devicetype.setImageResource(this.bottomGridViewImg[4]);
            this.devicetype_text.setText(this.bottomGridViewText[4]);
        } else {
//            this.img_devicetype.setImageResource(this.bottomGridViewImg[mDevice.getDeviceType() - 1]);
            this.devicetype_text.setText(this.bottomGridViewText[mDevice.getDeviceType() - 1]);
        }
        this.deviceAddress.setText(String.format("0x%02x%02x", new Object[]{Integer.valueOf(((mDevice.getDeviceAddress() & 65535) >> 8) & MotionEventCompat.ACTION_MASK), Integer.valueOf(mDevice.getDeviceAddress() & MotionEventCompat.ACTION_MASK)}));
        this.mCurArea = null;
        this.mChooseButton.setSelection(0);
    }
}
