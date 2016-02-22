package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.CursorAdapter;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allin.activity.action.DataStorage;
import com.allin.activity.action.SysApplication;
import com.example.hesolutions.horizon.GuideActivity;
import com.example.hesolutions.horizon.HomePage;
import com.example.hesolutions.horizon.R;
import com.homa.hls.database.Area;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;
import com.homa.hls.database.Scene;
import com.homa.hls.datadeal.DevicePacket;
import com.homa.hls.datadeal.Event;
import com.homa.hls.datadeal.UserPacket;
import com.homa.hls.socketconn.DeviceSocket;
import com.homa.hls.socketconn.UserSession;
import com.homa.hls.widgetcustom.CustomProgressDialog;
import com.mylibrary.WeekViewEvent;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class LogoActivity extends Activity {
    File file1;
    File file2;
    File file3;
    LayoutInflater inflater;
    public Handler mHandler;
    Dialog mdialog;
    Dialog mdialog1;
    private CustomProgressDialog progressDialog;
    String sdpath1;
    String sdpath2;
    String sdpath3;

    /* renamed from: com.allin.activity.LogoActivity.1 */
    class C02631 extends Handler {
        C02631() {
        }
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Gateway gateway;
            switch (msg.what) {
                case CursorAdapter.FLAG_AUTO_REQUERY /*1*/:
                    LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.password_error));
                case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER /*2*/:
                    SysApplication.getInstance();
                    if (SysApplication.boolgetpswdtogateway) {
                        if (msg.arg1 == 1) {
                            new updatedatabaseThread((Gateway) msg.obj).start();
                        } else {
                            SysApplication.getInstance();
                            SysApplication.deviceList.clear();
                            Log.i("deviceList", "clear");
                            LogoActivity.this.stopProgressDialog();
                            LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.getdata_forgateway_err));
                        }
                        SysApplication.getInstance();
                        SysApplication.boolgetpswdtogateway = false;
                    }
                case 6 /*6*/:
                    LogoActivity.this.requestDialog();
                case com.homa.hls.datadeal.Message.PASSWORD_BUFFER_SIZE /*8*/:
                    LogoActivity.this.stopProgressDialog();
                    LogoActivity.this.SysnDatasuccess();
                case Event.EVENT_REMOTE_MACADDRESS_ERROR /*9*/:
                    LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.macaddress_error));
                case Event.EVENT_SYSDATA_FAIL /*15*/:
                    LogoActivity.this.stopProgressDialog();
                    LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.responerequest_no));
                case Event.EVENT_SOCKET_NULL /*18*/:
                    LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.send_socket_null));
                case Event.EVENT_SEND_ERROR /*24*/:
                    LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.send_error));
                case Event.EVENT_SEND_DATA /*25*/:
                    if (SysApplication.getInstance().getWifiSSID(SysApplication.getInstance().getCurrentActivity()) != null) {
                        LogoActivity logoActivity = LogoActivity.this;
                        LogoActivity.this.getApplicationContext();
                        String nameString = SysApplication.getInstance().execCalc(SysApplication.getInstance().FormatString(Integer.parseInt(String.valueOf(((WifiManager) logoActivity.getSystemService(WIFI_SERVICE)).getDhcpInfo().netmask))), SysApplication.getInstance().getLocalIpAddress(SysApplication.getInstance().getCurrentActivity()));
                        try {
                            SysApplication.getInstance();
                            SysApplication.sendipadd = nameString.getBytes("UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                case Event.EVENT_SETDATA_TOGATEWAY_STAIT /*32*/:
                    SysApplication.getInstance();
                    if (SysApplication.boolsetpswdtogateway) {
                        int msgarg = msg.arg1;
                        LogoActivity.this.stopProgressDialog();
                        if (msgarg == 1) {
                            LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.outputsuccess));
                        } else {
                            LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.senddata_togateway_err));
                        }
                        SysApplication.getInstance();
                        SysApplication.boolsetpswdtogateway = false;
                    }
                case Event.EVENT_GETDATA_FORGATEWAY /*33*/:
                    if (LogoActivity.this.progressDialog == null && LogoActivity.this.mdialog1 == null) {
                        LogoActivity.this.GetDataForGateway(false, 0);
                    }
                case Event.EVENT_SETDATA_TOGATEWAY /*34*/:
                    byte[] pwd = new byte[8];
                    gateway = SysApplication.getInstance().getCurrGateway(SysApplication.getInstance().getCurrentActivity());
                    if (gateway != null && Arrays.equals(gateway.getPassWord(), pwd)) {
                        LogoActivity.this.startProgressDialog(LogoActivity.this.getResources().getString(R.string.deal));
                        new GetDataForGatewayThread(gateway, true).start();
                    } else if (gateway != null) {
                        LogoActivity.this.GetGatewayPswd(gateway, true);
                    }
                case Event.EVENT_SET_GATEWAY_PSWD /*35*/:
                    if (msg.arg1 == 1) {
                        byte[] dataPassword = (byte[]) msg.obj;
                        if (dataPassword != null) {
                            if (dataPassword[0] == 0) {
                                for (int i = 0; i < dataPassword.length; i++) {
                                    dataPassword[i] = (byte) 0;
                                }
                            }
                            gateway = SysApplication.getInstance().getCurrGateway(SysApplication.getInstance().getCurrentActivity());
                            if (!(gateway == null || gateway.getPassWord() == null || dataPassword == null || Arrays.equals(gateway.getPassWord(), dataPassword))) {
                                gateway.setPassWord(dataPassword);
                                SysApplication.getInstance().SysUpdateGateWayInfo(gateway);
                            }
                            LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.wifi_connt_success));
                        } else {
                            return;
                        }
                    }
                    LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.password_seterr));
                    SysApplication.getInstance();
                    SysApplication.boolsetpswd = false;
                default:
            }
        }
    }

    /* renamed from: com.allin.activity.LogoActivity.2 */
    class C02642 implements OnClickListener {
        C02642() {
        }

        public void onClick(View v) {
            if (LogoActivity.this.mdialog != null && LogoActivity.this.mdialog.isShowing()) {
                LogoActivity.this.mdialog.cancel();
                LogoActivity.this.mdialog = null;
            }
        }
    }

    /* renamed from: com.allin.activity.LogoActivity.3 */
    class C02653 implements OnClickListener {
        C02653() {
        }

        public void onClick(View v) {
            if (LogoActivity.this.mdialog != null && LogoActivity.this.mdialog.isShowing()) {
                LogoActivity.this.mdialog.cancel();
                LogoActivity.this.mdialog = null;
            }
            SysApplication.getInstance().LoopThenExit();
        }
    }

    /* renamed from: com.allin.activity.LogoActivity.4 */
    class C02664 implements OnClickListener {
        C02664() {
        }

        public void onClick(View v) {
            LogoActivity.this.stopmdialog1();
        }
    }

    /* renamed from: com.allin.activity.LogoActivity.5 */
    class C02675 implements OnClickListener {
        private final /* synthetic */ boolean val$boolisset;
        private final /* synthetic */ Gateway val$gateway;

        C02675(Gateway gateway, boolean z) {
            this.val$gateway = gateway;
            this.val$boolisset = z;
        }

        public void onClick(View v) {
            LogoActivity.this.stopmdialog1();
            byte[] pwd = new byte[8];
            if (this.val$gateway != null && Arrays.equals(this.val$gateway.getPassWord(), pwd)) {
                LogoActivity.this.startProgressDialog(LogoActivity.this.getResources().getString(R.string.deal));
                new GetDataForGatewayThread(this.val$gateway, this.val$boolisset).start();
            } else if (this.val$gateway != null) {
                LogoActivity.this.GetGatewayPswd(this.val$gateway, this.val$boolisset);
            }
        }
    }

    /* renamed from: com.allin.activity.LogoActivity.6 */
    class C02686 implements OnCheckedChangeListener {
        private final /* synthetic */ EditText val$et;

        C02686(EditText editText) {
            this.val$et = editText;
        }

        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            if (arg1) {
                this.val$et.setInputType(144);
                this.val$et.setSelection(this.val$et.length());
                return;
            }
            this.val$et.setInputType(129);
            this.val$et.setSelection(this.val$et.length());
        }
    }

    /* renamed from: com.allin.activity.LogoActivity.7 */
    class C02697 implements OnKeyListener {
        C02697() {
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

    /* renamed from: com.allin.activity.LogoActivity.8 */
    class C02708 implements OnClickListener {
        C02708() {
        }

        public void onClick(View v) {
            LogoActivity.this.stopmdialog1();
        }
    }

    /* renamed from: com.allin.activity.LogoActivity.9 */
    class C02719 implements OnClickListener {
        private final /* synthetic */ boolean val$boolisset;
        private final /* synthetic */ EditText val$et;
        private final /* synthetic */ Gateway val$gateway;

        C02719(EditText editText, Gateway gateway, boolean z) {
            this.val$et = editText;
            this.val$gateway = gateway;
            this.val$boolisset = z;
        }

        public void onClick(View v) {
            try {
                String str1 = this.val$et.getText().toString();
                if (str1 == null || str1.length() < 6) {
                    LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.password_err));
                    return;
                }
                byte[] datas = new byte[8];
                datas = str1.getBytes("UTF-8");
                byte[] data1s = new byte[8];
                System.arraycopy(datas, 0, data1s, 0, datas.length);
                if (this.val$gateway == null || !Arrays.equals(this.val$gateway.getPassWord(), data1s)) {
                    LogoActivity.this.stopmdialog1();
                    LogoActivity.this.DialogShow(LogoActivity.this.getResources().getString(R.string.pswd_err));
                } else {
                    LogoActivity.this.stopmdialog1();
                    LogoActivity.this.startProgressDialog(LogoActivity.this.getResources().getString(R.string.deal));
                    new GetDataForGatewayThread(this.val$gateway, this.val$boolisset).start();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    class GetDataForGatewayThread extends Thread {
        boolean boolisset;
        Gateway gateway;

        public GetDataForGatewayThread(Gateway gateway, boolean boolisset) {
            this.gateway = null;
            this.boolisset = false;
            this.gateway = gateway;
            this.boolisset = boolisset;
        }

        public void run() {
            super.run();
            if (this.boolisset) {
                SysApplication.getInstance();
                SysApplication.boolsetpswdtogateway = true;
                SysApplication.getInstance();
                SysApplication.ipackindex = 0;
                ArrayList<Device> mDeviceList = DatabaseManager.getInstance().getDeviceListofexceptGroup_output().getmDeviceList();
                if (mDeviceList.size() <= 0) {
                    SysApplication.getInstance().broadcastEvent(32, 0, null);
                    return;
                }
                int idevallnum = mDeviceList.size();
                if (this.gateway.getGateWayId() == 4 && idevallnum > 500) {
                    idevallnum = 500;
                } else if (this.gateway.getGateWayId() == 3 && idevallnum > 525) {
                    idevallnum = 525;
                }
                short devallnum = (short) (65535 & idevallnum);
                byte hight = (byte) (devallnum >> 8);
                byte low = (byte) (devallnum & MotionEventCompat.ACTION_MASK);
                int sendDevnum = 0;
                short sendDevnum2;
                int senddatalen;
                int i;
                short devtype;
                short devaddr;
                byte devsubtype;
                if (this.gateway.getGateWayId() == 4) {
                    if (devallnum >= (short) 5) {
                        sendDevnum = 5;
                    } else {
                        sendDevnum2 = devallnum;
                    }
                    senddatalen = (sendDevnum * 36) + 4;
                    byte[] sendData = new byte[senddatalen];
                    sendData[0] = hight;
                    sendData[1] = low;
                    sendData[2] = (byte) 0;
                    sendData[3] = (byte) sendDevnum;
                    for (i = 0; i < sendDevnum; i++) {
                        devtype = ((Device) mDeviceList.get(i)).getDeviceType();
                        devaddr = ((Device) mDeviceList.get(i)).getDeviceAddress();
                        devsubtype = (byte) ((Device) mDeviceList.get(i)).getSubDeviceType();
                        sendData[(i * 36) + 4] = (byte) (devaddr >> 8);
                        sendData[(i * 36) + 5] = (byte) (devaddr & MotionEventCompat.ACTION_MASK);
                        sendData[(i * 36) + 6] = (byte) devtype;
                        sendData[(i * 36) + 7] = devsubtype;
                        sendData[(i * 36) + 8] = (byte) ((Device) mDeviceList.get(i)).getChannelInfo();
                        byte[] Devstrname = null;
                        try {
                            Devstrname = SysApplication.getInstance().FormatStringForByte(((Device) mDeviceList.get(i)).getDeviceName().getBytes("UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (Devstrname.length > 30) {
                            sendData[(i * 36) + 9] = (byte) 30;
                        } else {
                            sendData[(i * 36) + 9] = (byte) Devstrname.length;
                        }
                        byte[] devname = new byte[30];
                        System.arraycopy(Devstrname, 0, devname, 0, sendData[(i * 36) + 9]);
                        System.arraycopy(devname, 0, sendData, (i * 36) + 10, 30);
                    }
                    DeviceSocket.getInstance().send(com.homa.hls.datadeal.Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 123, (short) senddatalen, (short) 0, sendData), this.gateway.getMacAddress(), this.gateway.getPassWord(), this.gateway.getSSID(), SysApplication.getInstance().getCurrentActivity()));
                    return;
                } else if (this.gateway.getGateWayId() == 3) {
                    if (devallnum >= (short) 15) {
                        sendDevnum = 15;
                    } else {
                        sendDevnum2 = devallnum;
                    }
                    senddatalen = (sendDevnum * 4) + 4;
                    byte[] sendData2 = new byte[senddatalen];
                    sendData2[0] = hight;
                    sendData2[1] = low;
                    sendData2[2] = (byte) 0;
                    sendData2[3] = (byte) sendDevnum;
                    for (i = 0; i < sendDevnum; i++) {
                        devtype = ((Device) mDeviceList.get(i)).getDeviceType();
                        devaddr = ((Device) mDeviceList.get(i)).getDeviceAddress();
                        devsubtype = (byte) ((Device) mDeviceList.get(i)).getSubDeviceType();
                        if (devtype > (short) 96) {
                            sendData2[(i * 4) + 4] = (byte) 1;
                            devtype = (short) (devtype - 96);
                        } else {
                            sendData2[(i * 4) + 4] = (byte) 0;
                        }
                        sendData2[(i * 4) + 5] = (byte) ((((byte) devtype) << 4) | devsubtype);
                        sendData2[(i * 4) + 6] = (byte) (devaddr >> 8);
                        sendData2[(i * 4) + 7] = (byte) (devaddr & MotionEventCompat.ACTION_MASK);
                    }
                    DeviceSocket.getInstance().send(com.homa.hls.datadeal.Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 124, (short) senddatalen, (short) 0, sendData2), this.gateway.getMacAddress(), this.gateway.getPassWord(), this.gateway.getSSID(), SysApplication.getInstance().getCurrentActivity()));
                    return;
                } else {
                    return;
                }
            }
            SysApplication.getInstance();
            SysApplication.deviceList.clear();
            Log.i("deviceList", "clear");
            SysApplication.getInstance();
            SysApplication.iaddbeginnum = 1;
            SysApplication.getInstance();
            SysApplication.ipackindex = 0;
            SysApplication.getInstance();
            SysApplication.boolgetpswdtogateway = true;
            DeviceSocket.getInstance().send(com.homa.hls.datadeal.Message.createMessage((byte) 1, DevicePacket.createGatewayPacket((byte) 1, (byte) 125, (short) 1, (short) 0, new byte[1]), this.gateway.getMacAddress(), this.gateway.getPassWord(), this.gateway.getSSID(), SysApplication.getInstance().getCurrentActivity()));
        }
    }

    class SleepThread extends Thread {
        SleepThread() {
        }

        public void run() {
            super.run();
            try {
                sleep(1000);
                if (DataStorage.getInstance(LogoActivity.this).getBoolean("GUIDE_INIT")) {
                    System.out.println("*** call main activity");

                    Intent intent = new Intent(LogoActivity.this, HomePage.class);
                    intent.putExtra("mainactivity", 0);
                    LogoActivity.this.startActivity(intent);

                } else {
                    LogoActivity.this.startActivity(new Intent(LogoActivity.this, HomePage.class));
                }
                LogoActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                LogoActivity.this.finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class sendFindZigbeeThread extends Thread {
        sendFindZigbeeThread() {
        }

        public void run() {
            super.run();
            LogoActivity logoActivity = LogoActivity.this;
            LogoActivity.this.getApplicationContext();
            String s_netmask = String.valueOf(((WifiManager) logoActivity.getSystemService(WIFI_SERVICE)).getDhcpInfo().netmask);
            int a = 3;
            SysApplication.getInstance();
            SysApplication.boolFindGateway = true;
            while (a > 0) {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SysApplication.getInstance();
                System.out.println("*** SysApplication.boolFindGateway is " + SysApplication.boolFindGateway);
                if (SysApplication.boolFindGateway) {
                    LogoActivity.UDPScannerIp(SysApplication.getInstance().execCalc(SysApplication.getInstance().FormatString(Integer.parseInt(s_netmask)), SysApplication.getInstance().getLocalIpAddress(SysApplication.getInstance().getCurrentActivity())), 50000, (byte) 0);
                    a--;
                } else {
                    return;
                }
            }
        }
    }

    class updatedatabaseThread extends Thread {
        Gateway mcgateway;

        public updatedatabaseThread(Gateway gateway) {
            this.mcgateway = null;
            this.mcgateway = gateway;
        }

        public void run() {
            super.run();
            ArrayList<Device> mDeviceList = DatabaseManager.getInstance().getDeviceList().getmDeviceList();
            ArrayList<Area> areaList = DatabaseManager.getInstance().getAreaList().getAreaArrayList();
            ArrayList<Scene> sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
            SysApplication.getInstance();
            if (SysApplication.deviceList != null) {
                SysApplication.getInstance();
                if (SysApplication.deviceList.size() > 0) {
                    int i = 0;
                    while (true) {
                        SysApplication.getInstance();
                        if (i >= SysApplication.deviceList.size()) {
                            break;
                        }
                        LogoActivity logoActivity = LogoActivity.this;
                        SysApplication.getInstance();
                        if (logoActivity.GetDevDataSaveDatabase((Device) SysApplication.deviceList.get(i), sceneList, areaList, mDeviceList, this.mcgateway)) {
                            mDeviceList = DatabaseManager.getInstance().getAllDeviceList().getmDeviceList();
                        }
                        i++;
                    }
                }
            }
            SysApplication.getInstance();
            SysApplication.deviceList.clear();
            Log.i("deviceList", "clear");
            LogoActivity.this.stopProgressDialog();
            /*
            if (SysApplication.getInstance().getCurrentActivity().getComponentName().getClassName().equals("com.allin.activity.MainActivity")) {
                SysApplication.getInstance().broadcastEvent(40, 1, null);
                return;
            }
            Intent intent = new Intent(SysApplication.getInstance().getCurrentActivity(), MainActivity.class);
            intent.putExtra("mainactivity", 0);
            intent.putExtra("inputsuccess", "inputsuccess");
            SysApplication.getInstance().getCurrentActivity().startActivity(intent);
            LogoActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            SysApplication.getInstance().getCurrentActivity().finish();
            */
        }
    }

    public LogoActivity() {
        this.sdpath1 = null;
        this.sdpath2 = null;
        this.sdpath3 = null;
        this.file1 = null;
        this.file2 = null;
        this.file3 = null;
        this.mdialog = null;
        this.mdialog1 = null;
        this.progressDialog = null;
        this.inflater = null;
        this.mHandler = new C02631();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onCreate(android.os.Bundle r20) {
        super.onCreate(r20);

        setContentView(R.layout.activity_logo);
        SysApplication.getInstance().addActivity(this);
        getWindow().setFlags(1024, 1024);
        this.inflater = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE));
        DataStorage.getInstance(this).putInt("deviceid", -1);
        DataStorage.getInstance(this).putInt("scene_gridview_item", -1);

        try {

            SysApplication.getInstance().openwifi(this);
            SysApplication.getInstance();
            if (SysApplication.mDatagramSocket != null)
            {
                SysApplication.getInstance();
                if (SysApplication.mDatagramSocket.isConnected()) {
                    System.out.println("**** mDatagramSocket is connected");
                }
            }
            else
            {
                SysApplication.getInstance();
                if (SysApplication.mDatagramSocket == null) {
                    SysApplication.mDatagramSocket = new DatagramSocket();
                }

                createSDCardDir();
                DatabaseManager.getInstance().DatabaseInit(this);
                List<Gateway> gateways = SysApplication.getInstance().SysSelectGatewayInfofirst();
                UserSession.getInstance().init(this);
                DeviceSocket.getInstance().init(this);
                /*
                SysApplication.getInstance().subscibeEvent("LogoActivity", 6, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 8, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 18, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 24, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 1, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 9, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 25, this.mHandler);

                SysApplication.getInstance().subscibeEvent("LogoActivity", 33, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 34, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 2, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 32, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 35, this.mHandler);
                SysApplication.getInstance().subscibeEvent("LogoActivity", 41, this.mHandler);
                SysApplication.getInstance();
                */
                byte[] i = SysApplication._getTimeFromCurrentTo();
                byte[] arrayOfByte1 = new byte[6];
                arrayOfByte1[0] = ((byte)(i[0] >> 24));
                arrayOfByte1[1] = ((byte)(i[1] >> 16));
                arrayOfByte1[2] = ((byte)(i[2] >> 8));
                arrayOfByte1[3] = ((byte)(i[3] & 0xFF));
                int rowOffset = TimeZone.getDefault().getRawOffset() / 60000;
                arrayOfByte1[4] = ((byte)(rowOffset / 60));
                arrayOfByte1[5] = ((byte)(rowOffset % 60));
                byte[] arrayOfByte2 = SysApplication.getInstance().getWifiSSID(this);
                if ((arrayOfByte2 != null) || (SysApplication.getInstance().isNetworkConnected(this)))
                {
                    if (arrayOfByte2 != null) {
                        System.out.println("*** about to start sendFindZigbeeThread");
                        new sendFindZigbeeThread().start();
                    }
                }
                System.out.println("*** about to start SleepThread");
                new SleepThread().start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startProgressDialog(String message) {
        if (this.progressDialog == null) {
            this.progressDialog = CustomProgressDialog.createDialog(SysApplication.getInstance().getCurrentActivity());
            this.progressDialog.setMessage(message);
        }
        this.progressDialog.show();
    }

    private void stopProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            View view = this.inflater.inflate(R.layout.msg_dialog, null);
            Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
            Button btn_no = (Button) view.findViewById(R.id.btn_cancel);
            ((TextView) view.findViewById(R.id.textinfor)).setText(getResources().getString(R.string.finishask));
            this.mdialog = new Dialog(this, R.style.Theme_dialog);
            this.mdialog.setContentView(view);
            this.mdialog.setCancelable(true);
            this.mdialog.setCanceledOnTouchOutside(false);
            this.mdialog.show();
            btn_no.setOnClickListener(new C02642());
            btn_ok.setOnClickListener(new C02653());
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.sdpath1 = null;
        this.sdpath2 = null;
        this.sdpath3 = null;
        this.file1 = null;
        this.file2 = null;
        this.file3 = null;
        if (this.mdialog != null && this.mdialog.isShowing()) {
            this.mdialog.cancel();
            this.mdialog = null;
        }
        stopmdialog1();
        stopProgressDialog();
        System.gc();
    }

    private boolean _findDeviceName(String deviceName, ArrayList<Device> mDeviceList) {
        if (mDeviceList != null) {
            Iterator it = mDeviceList.iterator();
            while (it.hasNext()) {
                if (((Device) it.next()).getDeviceName().equalsIgnoreCase(deviceName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean _findDeviceaddr(short devaddr, ArrayList<Device> mDeviceList) {
        if (mDeviceList != null) {
            Iterator it = mDeviceList.iterator();
            while (it.hasNext()) {
                if (((Device) it.next()).getDeviceAddress() == devaddr) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean GetDevDataSaveDatabase(Device mDevice, ArrayList<Scene> sceneList, ArrayList<Area> areaList, ArrayList<Device> mDeviceList, Gateway mGateway) {
        if (_findDeviceaddr(mDevice.getDeviceAddress(), mDeviceList)) {
            return false;
        }
        int i;
        if (mGateway.getGateWayId() != 4) {
            SysApplication.getInstance();
            i = SysApplication.iaddbeginnum;
            while (_findDeviceName(new StringBuilder(String.valueOf(i)).toString(), mDeviceList)) {
                i++;
            }
            SysApplication.getInstance();
            SysApplication.iaddbeginnum = i;
            mDevice.setDeviceName(new StringBuilder(String.valueOf(i)).toString());
        } else if (_findDeviceName(mDevice.getDeviceName(), mDeviceList)) {
            SysApplication.getInstance();
            i = SysApplication.iaddbeginnum;
            while (_findDeviceName(mDevice.getDeviceName() + i, mDeviceList)) {
                i++;
            }
            SysApplication.getInstance();
            SysApplication.iaddbeginnum = i;
            mDevice.setDeviceName(mDevice.getDeviceName() + i);
        }
        //AddDeviceToDatabase(mDevice, sceneList, areaList, mGateway);
        DatabaseManager.getInstance().addDevToAllDeviceList(mDevice);
        return true;
    }
/*
    private boolean AddDeviceToDatabase(Device mDevice, ArrayList<Scene> sceneList, ArrayList<Area> areaList, Gateway mGateway) {
        if (DatabaseManager.getInstance().addDevice(mDevice, null)) {
            mDevice.setGatewayMacAddr(mGateway.getMacAddress());
            mDevice.setGatewayPassword(mGateway.getPassWord());
            mDevice.setGatewaySSID(mGateway.getSSID());
            DatabaseManager.getInstance().AddGateWayDevice(DatabaseManager.getInstance().SelectLimitDeviceIndex(), mGateway.getGateWayInfoIndex());
            if (mDevice.getDeviceType() == (short) 6 || mDevice.getDeviceType() == (short) 7 || mDevice.getDeviceType() == (short) 8 || mDevice.getDeviceType() == (short) 9 || mDevice.getDeviceType() == (short) 13 || mDevice.getDeviceType() == (short) 43 || mDevice.getDeviceType() == (short) 44) {
                return true;
            }
            byte[] bArr;
            int i = 0;
            while (i < areaList.size()) {
                if (((Area) areaList.get(i)).getAreaName().equals("\u6240\u6709\u8bbe\u5907") || ((Area) areaList.get(i)).getAreaName().equals("All devices") || ((Area) areaList.get(i)).getAreaName().equals("Alle Ger\u00e4te")) {
                    DatabaseManager.getInstance().addAreaDeviceTable(mDevice, (Area) areaList.get(i));
                    break;
                }
                i++;
            }
            Device adddDevice = mDevice;
            int ibreak = 0;
            i = 0;
            while (i < sceneList.size()) {
                if (((Scene) sceneList.get(i)).getSceneName().equals("\u5168\u5f00") || ((Scene) sceneList.get(i)).getSceneName().equals("All On") || ((Scene) sceneList.get(i)).getSceneName().equals("Alles ein")) {
                    if (adddDevice.getDeviceType() == (short) 1 || adddDevice.getDeviceType() == (short) 97) {
                        bArr = new byte[5];
                        bArr[0] = (byte) -62;
                        bArr[1] = (byte) 1;
                        adddDevice.setSceneParams(bArr);
                    } else if (adddDevice.getDeviceType() == (short) 2 || adddDevice.getDeviceType() == (short) 3 || adddDevice.getDeviceType() == (short) 98 || adddDevice.getDeviceType() == (short) 99) {
                        bArr = new byte[5];
                        bArr[0] = (byte) 17;
                        bArr[1] = (byte) 100;
                        adddDevice.setSceneParams(bArr);
                    } else if (adddDevice.getDeviceType() == (short) 4 || adddDevice.getDeviceType() == (short) 100) {
                        bArr = new byte[5];
                        bArr[0] = (byte) 17;
                        bArr[1] = (byte) 100;
                        bArr[2] = (byte) 50;
                        adddDevice.setSceneParams(bArr);
                    } else if (adddDevice.getDeviceType() == (short) 15 || adddDevice.getDeviceType() == (short) 111) {
                        bArr = new byte[5];
                        bArr[0] = (byte) 17;
                        bArr[1] = (byte) 1;
                        adddDevice.setSceneParams(bArr);
                    } else if (adddDevice.getDeviceType() == (short) 5) {
                        if (adddDevice.getSubDeviceType() == (short) 1) {
                            bArr = new byte[5];
                            bArr[0] = (byte) 48;
                            bArr[1] = (byte) 1;
                            adddDevice.setSceneParams(bArr);
                        } else if (adddDevice.getSubDeviceType() == (short) 3) {
                            bArr = new byte[5];
                            bArr[0] = (byte) 52;
                            bArr[1] = (byte) 1;
                            adddDevice.setSceneParams(bArr);
                        } else {
                            bArr = new byte[5];
                            bArr[0] = (byte) 58;
                            bArr[1] = (byte) 1;
                            adddDevice.setSceneParams(bArr);
                        }
                    }
                    DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                    ibreak++;
                } else if (((Scene) sceneList.get(i)).getSceneName().equals("\u5168\u5173") || ((Scene) sceneList.get(i)).getSceneName().equals("All Off") || ((Scene) sceneList.get(i)).getSceneName().equals("Alles aus")) {
                    if (adddDevice.getDeviceType() == (short) 1 || adddDevice.getDeviceType() == (short) 97) {
                        bArr = new byte[5];
                        bArr[0] = (byte) -62;
                        adddDevice.setSceneParams(bArr);
                    } else if (adddDevice.getDeviceType() == (short) 2 || adddDevice.getDeviceType() == (short) 3 || adddDevice.getDeviceType() == (short) 4 || adddDevice.getDeviceType() == (short) 98 || adddDevice.getDeviceType() == (short) 99 || adddDevice.getDeviceType() == (short) 100 || adddDevice.getDeviceType() == (short) 15 || adddDevice.getDeviceType() == (short) 111) {
                        bArr = new byte[5];
                        bArr[0] = (byte) 17;
                        adddDevice.setSceneParams(bArr);
                    } else if (adddDevice.getDeviceType() == (short) 5) {
                        if (adddDevice.getSubDeviceType() == (short) 1) {
                            bArr = new byte[5];
                            bArr[0] = (byte) 48;
                            adddDevice.setSceneParams(bArr);
                        } else if (adddDevice.getSubDeviceType() == (short) 3) {
                            bArr = new byte[5];
                            bArr[0] = (byte) 52;
                            adddDevice.setSceneParams(bArr);
                        } else {
                            bArr = new byte[5];
                            bArr[0] = (byte) 58;
                            adddDevice.setSceneParams(bArr);
                        }
                    }
                    DatabaseManager.getInstance().AddSceneDevice((Scene) sceneList.get(i), adddDevice);
                    ibreak++;
                }
                if (ibreak == 2) {
                    break;
                }
                i++;
            }
            boolean boolishavescene;
            String[] strscenes;
            Scene mScene1;
            if (adddDevice.getDeviceType() == (short) 1 || adddDevice.getDeviceType() == (short) 97) {
                boolishavescene = false;
                if (!DataStorage.getInstance(SysApplication.getInstance().getCurrentActivity()).getBoolean("SCENEA_INIT")) {
                    i = 0;
                    while (i < sceneList.size()) {
                        boolishavescene = true;
                        if (!(((Scene) sceneList.get(i)).getSceneName().equals("\u65e5\u843d") || ((Scene) sceneList.get(i)).getSceneName().equals("Sunset") || ((Scene) sceneList.get(i)).getSceneName().equals("Sonnenschein") || ((Scene) sceneList.get(i)).getSceneName().equals("\u6d77\u6d0b") || ((Scene) sceneList.get(i)).getSceneName().equals("Sea") || ((Scene) sceneList.get(i)).getSceneName().equals("See") || ((Scene) sceneList.get(i)).getSceneName().equals("\u7af9\u6797") || ((Scene) sceneList.get(i)).getSceneName().equals("Bamboo") || ((Scene) sceneList.get(i)).getSceneName().equals("Bambus") || ((Scene) sceneList.get(i)).getSceneName().equals("\u6a31\u82b1") || ((Scene) sceneList.get(i)).getSceneName().equals("Sakura") || ((Scene) sceneList.get(i)).getSceneName().equals("Kirschbl\u00fcten"))) {
                            boolishavescene = false;
                        }
                        if (boolishavescene) {
                            break;
                        }
                        i++;
                    }
                    if (!boolishavescene) {
                        strscenes = SysApplication.getInstance().getCurrentActivity().getResources().getStringArray(R.array.init_scene_text_data);
                        for (String sceneName : strscenes) {
                            mScene1 = new Scene();
                            mScene1.setSceneName(sceneName);
                            DatabaseManager.getInstance().addScene(mScene1);
                        }
                        sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                        DataStorage.getInstance(SysApplication.getInstance().getCurrentActivity()).putBoolean("SCENEA_INIT", true);
                    }
                }
                if (!DataStorage.getInstance(SysApplication.getInstance().getCurrentActivity()).getBoolean("SCENEB_INIT")) {
                    boolishavescene = false;
                    i = 0;
                    while (i < sceneList.size()) {
                        boolishavescene = true;
                        if (!(((Scene) sceneList.get(i)).getSceneName().equals("\u6d3b\u529b") || ((Scene) sceneList.get(i)).getSceneName().equals("Energizer") || ((Scene) sceneList.get(i)).getSceneName().equals("Lebendigkeit") || ((Scene) sceneList.get(i)).getSceneName().equals("\u653e\u677e") || ((Scene) sceneList.get(i)).getSceneName().equals("Relax") || ((Scene) sceneList.get(i)).getSceneName().equals("Relaxen") || ((Scene) sceneList.get(i)).getSceneName().equals("\u4e13\u6ce8") || ((Scene) sceneList.get(i)).getSceneName().equals("Concentrate") || ((Scene) sceneList.get(i)).getSceneName().equals("Konzentrieren") || ((Scene) sceneList.get(i)).getSceneName().equals("\u9605\u8bfb") || ((Scene) sceneList.get(i)).getSceneName().equals("Reading") || ((Scene) sceneList.get(i)).getSceneName().equals("Lesen"))) {
                            boolishavescene = false;
                        }
                        if (boolishavescene) {
                            break;
                        }
                        i++;
                    }
                    if (!boolishavescene) {
                        strscenes = SysApplication.getInstance().getCurrentActivity().getResources().getStringArray(R.array.init_scene_text_data1);
                        for (String sceneName2 : strscenes) {
                            mScene1 = new Scene();
                            mScene1.setSceneName(sceneName2);
                            DatabaseManager.getInstance().addScene(mScene1);
                        }
                        sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                        DataStorage.getInstance(SysApplication.getInstance().getCurrentActivity()).putBoolean("SCENEB_INIT", true);
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
            } else if (adddDevice.getDeviceType() == (short) 4 || adddDevice.getDeviceType() == (short) 100) {
                if (!DataStorage.getInstance(SysApplication.getInstance().getCurrentActivity()).getBoolean("SCENEB_INIT")) {
                    boolishavescene = false;
                    i = 0;
                    while (i < sceneList.size()) {
                        boolishavescene = true;
                        if (!(((Scene) sceneList.get(i)).getSceneName().equals("\u6d3b\u529b") || ((Scene) sceneList.get(i)).getSceneName().equals("Energizer") || ((Scene) sceneList.get(i)).getSceneName().equals("Lebendigkeit") || ((Scene) sceneList.get(i)).getSceneName().equals("\u653e\u677e") || ((Scene) sceneList.get(i)).getSceneName().equals("Relax") || ((Scene) sceneList.get(i)).getSceneName().equals("Relaxen") || ((Scene) sceneList.get(i)).getSceneName().equals("\u4e13\u6ce8") || ((Scene) sceneList.get(i)).getSceneName().equals("Concentrate") || ((Scene) sceneList.get(i)).getSceneName().equals("Konzentrieren") || ((Scene) sceneList.get(i)).getSceneName().equals("\u9605\u8bfb") || ((Scene) sceneList.get(i)).getSceneName().equals("Reading") || ((Scene) sceneList.get(i)).getSceneName().equals("Lesen"))) {
                            boolishavescene = false;
                        }
                        if (boolishavescene) {
                            break;
                        }
                        i++;
                    }
                    if (!boolishavescene) {
                        strscenes = getResources().getStringArray(R.array.init_scene_text_data1);
                        for (String sceneName22 : strscenes) {
                            mScene1 = new Scene();
                            mScene1.setSceneName(sceneName22);
                            DatabaseManager.getInstance().addScene(mScene1);
                        }
                        sceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
                        DataStorage.getInstance(SysApplication.getInstance().getCurrentActivity()).putBoolean("SCENEB_INIT", true);
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
        }
        return true;
    }
    */

    private void GetDataForGateway(boolean boolisset, int iismax) {
        boolean bolgetdata = false;
        Gateway gateway = SysApplication.getInstance().getCurrGateway(SysApplication.getInstance().getCurrentActivity());
        if (gateway != null && (gateway.getGateWayId() == 3 || gateway.getGateWayId() == 4)) {
            bolgetdata = true;
        }
        if (bolgetdata) {
            View view = this.inflater.inflate(R.layout.msg_dialog, null);
            Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
            TextView textinfor = (TextView) view.findViewById(R.id.textinfor);
            if (!boolisset) {
                textinfor.setText(getResources().getString(R.string.putintfor_getway));
            } else if (iismax == 1) {
                textinfor.setText(getResources().getString(R.string.putoutto_getwayismax));
            } else {
                textinfor.setText(getResources().getString(R.string.putoutto_getway));
            }
            this.mdialog1 = new Dialog(SysApplication.getInstance().getCurrentActivity(), R.style.Theme_dialog);
            this.mdialog1.setContentView(view);
            this.mdialog1.setCancelable(true);
            this.mdialog1.setCanceledOnTouchOutside(false);
            this.mdialog1.show();
            btn_cancel.setOnClickListener(new C02664());
            btn_ok.setOnClickListener(new C02675(gateway, boolisset));
        }
    }

    private void stopmdialog1() {
        if (this.mdialog1 != null && this.mdialog1.isShowing()) {
            this.mdialog1.cancel();
            this.mdialog1 = null;
        }
    }

    private void GetGatewayPswd(Gateway gateway, boolean boolisset) {
        View view = this.inflater.inflate(R.layout.verify_password, null);
        ((TextView) view.findViewById(R.id.tv_name)).setText(getResources().getString(R.string.gateway_password));
        EditText et = (EditText) view.findViewById(R.id.editText1);
        et.setFilters(new InputFilter[]{new LengthFilter(8)});
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_no = (Button) view.findViewById(R.id.btn_cancel);
        ((CheckBox) view.findViewById(R.id.chkbox_select)).setOnCheckedChangeListener(new C02686(et));
        et.setOnKeyListener(new C02697());
        SysApplication.getInstance().SetEditType(et);
        this.mdialog1 = new Dialog(SysApplication.getInstance().getCurrentActivity(), R.style.Theme_dialog);
        this.mdialog1.setCancelable(true);
        this.mdialog1.setCanceledOnTouchOutside(false);
        this.mdialog1.setContentView(view);
        this.mdialog1.show();
        btn_no.setOnClickListener(new C02708());
        btn_ok.setOnClickListener(new C02719(et, gateway, boolisset));
    }

    private void requestDialog() {
        View view = this.inflater.inflate(R.layout.msg_dialog, null);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_no = (Button) view.findViewById(R.id.btn_cancel);
        ((TextView) view.findViewById(R.id.textinfor)).setText(getResources().getString(R.string.requestDialog));
        this.mdialog = new Dialog(SysApplication.getInstance().getCurrentActivity(), R.style.Theme_dialog);
        this.mdialog.setContentView(view);
        this.mdialog.setCancelable(true);
        this.mdialog.setCanceledOnTouchOutside(false);
        btn_no.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LogoActivity.this.mdialog != null && LogoActivity.this.mdialog.isShowing()) {
                    LogoActivity.this.mdialog.cancel();
                    LogoActivity.this.mdialog = null;
                }
                UserSession.getInstance().sendToRemote(UserPacket.createPacket((byte) 32, (byte) 33, (byte) 0, (byte) 0, null));
            }
        });
        btn_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LogoActivity.this.mdialog != null && LogoActivity.this.mdialog.isShowing()) {
                    LogoActivity.this.mdialog.cancel();
                    LogoActivity.this.mdialog = null;
                }
                LogoActivity.this._sendFile();
                LogoActivity.this.startProgressDialog(LogoActivity.this.getResources().getString(R.string.transmission));
            }
        });
        if (!SysApplication.getInstance().getCurrentActivity().isFinishing()) {
            this.mdialog.show();
        }
    }

    private void _sendFile() {
        subpackage(SysApplication.getInstance().EcodeFile());
    }

    private void subpackage(byte[] data) {
        byte[] buff = new byte[10240];
        int a = data.length / buff.length;
        byte[] b = new byte[1];
        if (data.length % buff.length == 0) {
            b[0] = (byte) a;
        } else {
            b[0] = (byte) (a + 1);
        }
        UserSession.getInstance().sendToRemote(UserPacket.createPacket((byte) 32, (byte) 35, (byte) 0, (byte) 0, b));
    }

    private void SysnDatasuccess() {
        if (!SysApplication.getInstance().getCurrentActivity().isFinishing()) {
            DialogShow(getResources().getString(R.string.syssuccess));
        }
    }

    public void createSDCardDir() {
        File path1 = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/iLightsIn").toString());
        if (!path1.exists()) {
            path1.mkdirs();
        }
    }

    private void DialogShow(String message) {
        if (this.mdialog != null && this.mdialog.isShowing()) {
            this.mdialog.cancel();
            this.mdialog = null;
        }
        View view = this.inflater.inflate(R.layout.msg_dialog_ok, null);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        ((TextView) view.findViewById(R.id.textinfor)).setText(message);
        this.mdialog = new Dialog(SysApplication.getInstance().getCurrentActivity(), R.style.Theme_dialog);
        this.mdialog.setContentView(view);
        this.mdialog.setCancelable(true);
        this.mdialog.setCanceledOnTouchOutside(false);
        this.mdialog.show();
        btn_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LogoActivity.this.mdialog != null && LogoActivity.this.mdialog.isShowing()) {
                    LogoActivity.this.mdialog.cancel();
                    LogoActivity.this.mdialog = null;
                }
            }
        });
    }

    public static void UDPScannerIp(String BroadcastAddress, int port, byte msgid) {
        try {
            SysApplication.getInstance();
            System.out.println("*** BroadcastAddress is " + BroadcastAddress);
            SysApplication.sendipadd = BroadcastAddress.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        try {
            DevicePacket packet = DevicePacket.createGatewayPacket((byte) 1, (byte) 116, (short) 0, (short) msgid, null);
            System.out.println("*** DevicePacket data is " + packet.getData());
            System.out.println("*** DevicePacket address is " + packet.getAddress());
            DatagramPacket dp = new DatagramPacket(DevicePacket.decodePacket(packet), DevicePacket.decodePacket(packet).length, InetAddress.getByName(BroadcastAddress), port);
            System.out.println("*** DatagramPacket is " + dp);
            System.out.println("*** DatagramPacket dp data " + dp.getData());
            System.out.println("*** DatagramPacket dp address " + dp.getAddress());
            System.out.println("*** DatagramPacket dp socket address " + dp.getSocketAddress());
            SysApplication.getInstance();
            System.out.println("*** SysApplication.getInstance CALLED");
            SysApplication.mDatagramSocket.send(dp);
            System.out.println("*** SysApplication.mDatagramSocket.send CALLED");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }
}
