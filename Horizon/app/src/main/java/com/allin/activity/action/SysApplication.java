package com.allin.activity.action;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import com.homa.hls.database.Area;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Gateway;
import com.homa.hls.datadeal.Message;
import com.homa.hls.socketconn.CommonsNet;
import com.homa.hls.socketconn.DeviceSocket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.DatagramSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class SysApplication extends Application {
    public static byte[] CurrGateWayMacAddress;
    public static int PacketNum;
    public static List<Activity> activityList;
    public static Map<Integer, byte[]> alarmSetListMap;
    public static boolean boolFindGateway;
    public static boolean boolNewgetalarm;
    public static boolean boolNewgettime;
    public static boolean boolSendmsg;
    public static boolean boolgetalarm;
    public static boolean boolgetmode;
    public static boolean boolgetpswdtogateway;
    public static boolean boolgettime;
    public static boolean boolissend;
    public static boolean boolsetpswd;
    public static boolean boolsetpswdtogateway;
    public static String broadcastAddress;
    public static int commandsequence;
    public static CommonsNet commonsNet;
    public static ArrayList<CommonsNet> commonsNetList;
    public static ArrayList<Device> deviceList;
    public static final int[][][] deviceinfo;
    public static float fhumdity;
    public static float ftemp;
    public static int height;
    public static int iaddbeginnum;
    public static int ioutputToMaxNum;
    public static int ipackindex;
    public static boolean isclick_1;
    public static boolean isclick_2;
    public static Area mArea;
    public static DatagramSocket mDatagramSocket;
    public static Device mDevice;
    private static EventManager mEventManager;
    public static boolean mSequece;
    public static byte[] sendipadd;
    private static SysApplication sysApplication;
    public static Device time_mDevice;
    public static Map<Integer, byte[]> timerSetListMap;
    public static int width;
    byte[] buffer;
    short cmdIndex;
    static int[][][] r0;
    private MulticastLock multicastLock;

    /* renamed from: com.allin.activity.action.SysApplication.1 */
    class C03921 implements TextWatcher {
        C03921() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable edt) {
            String temp = edt.toString();
            for (int i = temp.length(); i > 0; i--) {
                int mid = temp.substring(i - 1, i).toCharArray()[0];
                if ((mid < 48 || mid > 57) && ((mid < 65 || mid > 90) && (mid < 97 || mid > 122))) {
                    edt.delete(i - 1, i);
                    return;
                }
            }
        }
    }

    /* renamed from: com.allin.activity.action.SysApplication.2 */
    class C03932 implements TextWatcher {
        C03932() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable edt) {
            String temp = edt.toString();
            if (temp.length() == 2) {
                int mid = temp.substring(0, 1).toCharArray()[0];
                int mid1 = temp.substring(1, 2).toCharArray()[0];
                if (mid > 50) {
                    edt.delete(1, 2);
                } else if (mid == 50 && mid1 > 51) {
                    edt.delete(1, 2);
                }
            }
        }
    }

    /* renamed from: com.allin.activity.action.SysApplication.3 */
    class C03943 implements TextWatcher {
        C03943() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable edt) {
            String temp = edt.toString();
            if (temp.length() == 2 && temp.substring(0, 1).toCharArray()[0] > 53) {
                edt.delete(1, 2);
            }
        }
    }

    public static class AdnNameLengthFilter implements InputFilter {
        private char ch_c;
        private char[] ch_new;
        private char[] ch_old;
        private int iallnum;

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int i;
            this.ch_old = dest.toString().toCharArray();
            this.iallnum = 0;
            for (char c : this.ch_old) {
                this.ch_c = c;
                if (SysApplication.isContainsChinese(this.ch_c)) {
                    this.iallnum += 3;
                } else {
                    this.iallnum++;
                }
            }
            if (this.iallnum >= 30) {
                return "";
            }
            this.ch_new = source.toString().toCharArray();
            int keep = 0;
            for (i = 0; i < this.ch_new.length; i++) {
                this.ch_c = this.ch_new[i];
                if (SysApplication.isContainsChinese(this.ch_c)) {
                    this.iallnum += 3;
                } else {
                    this.iallnum++;
                }
                if (this.iallnum > 30) {
                    keep = i;
                    break;
                }
            }
            if (this.iallnum <= 30) {
                return null;
            }
            if (keep == 0) {
                return "";
            }
            return source.subSequence(start, start + keep);
        }
    }

    static {
        activityList = null;
        sysApplication = null;
        commonsNet = null;
        commonsNetList = null;
        deviceList = null;
        mSequece = false;
        mArea = null;
        mDevice = null;
        time_mDevice = null;
        PacketNum = 0;
        isclick_1 = false;
        isclick_2 = false;
        broadcastAddress = null;
        mDatagramSocket = null;
        commandsequence = 0;
        CurrGateWayMacAddress = null;
        boolSendmsg = false;
        boolsetpswdtogateway = false;
        boolgetpswdtogateway = false;
        boolissend = false;
        boolgetmode = false;
        boolsetpswd = false;
        boolgetalarm = false;
        boolgettime = false;
        boolNewgetalarm = false;
        boolNewgettime = false;
        boolFindGateway = false;
        sendipadd = null;
        iaddbeginnum = 1;
        ipackindex = 0;
        ioutputToMaxNum = 525;
        alarmSetListMap = null;
        timerSetListMap = null;
        ftemp = 0.0f;
        fhumdity = 0.0f;
        r0 = new int[16][][];
        int[][] iArr = new int[5][];
        int[] iArr2 = new int[4];
        iArr2[0] = MotionEventCompat.ACTION_MASK;
        iArr2[1] = 25;
        iArr[0] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 183;
        iArr2[1] = 20;
        iArr[1] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 144;
        iArr2[1] = 15;
        iArr[2] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 104;
        iArr2[1] = 10;
        iArr[3] = iArr2;
        //iArr2 = new int[]{60, 5, iArr2, iArr};
        iArr = new int[5][];
        iArr2 = new int[4];
        iArr2[0] = 190;
        iArr2[1] = 25;
        iArr[0] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 160;
        iArr2[1] = 20;
        iArr[1] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 120;
        iArr2[1] = 15;
        iArr[2] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 80;
        iArr2[1] = 10;
        iArr[3] = iArr2;
        //iArr2 = new int[]{35, 5, iArr2, iArr};
        iArr = new int[5][];
        iArr2 = new int[4];
        iArr2[0] = 175;
        iArr2[1] = 40;
        iArr[0] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 140;
        iArr2[1] = 35;
        iArr[1] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 120;
        iArr2[1] = 30;
        iArr[2] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 105;
        iArr2[1] = 25;
        iArr[3] = iArr2;
        //iArr2 = new int[]{80, 20, iArr2, iArr};
        iArr = new int[5][];
        iArr2 = new int[4];
        iArr2[0] = MotionEventCompat.ACTION_MASK;
        iArr2[1] = 102;
        iArr[0] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 193;
        iArr2[1] = 82;
        iArr[1] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 154;
        iArr2[1] = 62;
        iArr[2] = iArr2;
        iArr2 = new int[4];
        iArr2[0] = 114;
        iArr2[1] = 42;
        iArr[3] = iArr2;
        //iArr2 = new int[]{70, 22, iArr2, iArr};
        iArr = new int[5][];
        iArr2 = new int[4];
        iArr2[1] = 25;
        iArr2[2] = MotionEventCompat.ACTION_MASK;
        iArr[0] = iArr2;
        iArr2 = new int[4];
        iArr2[1] = 20;
        iArr2[2] = 183;
        iArr[1] = iArr2;
        iArr2 = new int[4];
        iArr2[1] = 15;
        iArr2[2] = 144;
        iArr[2] = iArr2;
        iArr2 = new int[4];
        iArr2[1] = 10;
        iArr2[2] = 104;
        iArr[3] = iArr2;
        //iArr2 = new int[]{5, 40, iArr2, iArr};
        int[][] iArr3 = new int[5][];
        int[] iArr4 = new int[4];
        iArr4[0] = 25;
        iArr4[2] = MotionEventCompat.ACTION_MASK;
        iArr3[0] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 20;
        iArr4[2] = 183;
        iArr3[1] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 15;
        iArr4[2] = 144;
        iArr3[2] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 10;
        iArr4[2] = 104;
        iArr3[3] = iArr4;
        //iArr4 = new int[]{5, 40, iArr4, iArr3};
        iArr3 = new int[5][];
        iArr4 = new int[4];
        iArr4[0] = 102;
        iArr4[2] = MotionEventCompat.ACTION_MASK;
        iArr3[0] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 82;
        iArr4[2] = 193;
        iArr3[1] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 62;
        iArr4[2] = 154;
        iArr3[2] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 42;
        iArr4[2] = 114;
        iArr3[3] = iArr4;
        //iArr4 = new int[]{22, 70, iArr4, iArr3};
        iArr3 = new int[5][];
        //iArr4 = new int[]{175, 25, 100, iArr4};
        //iArr4 = new int[]{160, 20, 80, iArr4};
        //iArr4 = new int[]{146, 15, 60, iArr4};
        //iArr4 = new int[]{85, 10, 40, iArr4};
        //iArr4 = new int[]{55, 5, 20, iArr4};
        r0[7] = iArr3;
        iArr3 = new int[5][];
        iArr4 = new int[4];
        iArr4[0] = 190;
        iArr4[1] = MotionEventCompat.ACTION_MASK;
        iArr3[0] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 160;
        iArr4[1] = 183;
        iArr3[1] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 120;
        iArr4[1] = 144;
        iArr3[2] = iArr4;
        iArr4 = new int[4];
        iArr4[0] = 80;
        iArr4[1] = 104;
        iArr3[3] = iArr4;
        //iArr4 = new int[]{35, 60, iArr4, iArr3};
        //iArr3 = new int[5][];
        //iArr4 = new int[]{100, 197, MotionEventCompat.ACTION_MASK, iArr4};
        //iArr4 = new int[]{80, 120, 160, iArr4};
        //iArr4 = new int[]{60, 100, 124, iArr4};
        //iArr4 = new int[]{40, 60, 104, iArr4};
        //iArr4 = new int[]{20, 40, 60, iArr4};
        r0[9] = iArr3;
        iArr3 = new int[5][];
        iArr4 = new int[4];
        iArr4[1] = 142;
        iArr4[2] = 207;
        iArr3[0] = iArr4;
        iArr4 = new int[4];
        iArr4[1] = 120;
        iArr4[2] = 177;
        iArr3[1] = iArr4;
        iArr4 = new int[4];
        iArr4[1] = 100;
        iArr4[2] = 147;
        iArr3[2] = iArr4;
        iArr4 = new int[4];
        iArr4[1] = 80;
        iArr4[2] = 117;
        iArr3[3] = iArr4;
        //iArr4 = new int[]{60, 87, iArr4, iArr3};
        iArr3 = new int[5][];
        iArr4 = new int[4];
        iArr4[1] = MotionEventCompat.ACTION_MASK;
        iArr4[2] = MotionEventCompat.ACTION_MASK;
        iArr3[0] = iArr4;
        iArr4 = new int[4];
        iArr4[1] = 183;
        iArr4[2] = 183;
        iArr3[1] = iArr4;
        iArr4 = new int[4];
        iArr4[1] = 144;
        iArr4[2] = 144;
        iArr3[2] = iArr4;
        iArr4 = new int[4];
        iArr4[1] = 104;
        iArr4[2] = 104;
        iArr3[3] = iArr4;
        //iArr4 = new int[]{60, 60, iArr4, iArr3};
        iArr3 = new int[5][];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4[0] = 30;
        iArr3[4] = iArr4;
        r0[12] = iArr3;
        iArr3 = new int[5][];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4[1] = 30;
        iArr3[4] = iArr4;
        r0[13] = iArr3;
        iArr3 = new int[5][];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4[2] = 30;
        iArr3[4] = iArr4;
        r0[14] = iArr3;
        iArr3 = new int[5][];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4 = new int[4];
        iArr4[3] = 30;
        iArr3[4] = iArr4;
        r0[15] = iArr3;
        deviceinfo = r0;
    }

    private SysApplication() {
        this.cmdIndex = (short) 4096;
        this.buffer = null;
    }

    public short getCmdIndex() {
        this.cmdIndex = (short) (this.cmdIndex + 1);
        if (this.cmdIndex > (short) 32752) {
            this.cmdIndex = (short) 4096;
        }
        return this.cmdIndex;
    }

    public static SysApplication getInstance() {
        if (sysApplication == null) {
            synchronized (SysApplication.class) {
                if (sysApplication == null) {
                    sysApplication = new SysApplication();
                }
                activityList = new LinkedList();
                mEventManager = EventManager.getInstance();
                commonsNet = new CommonsNet();
                commonsNetList = new ArrayList();
                deviceList = new ArrayList();
                mArea = new Area();
                mDevice = new Device();
                time_mDevice = new Device();
                CurrGateWayMacAddress = new byte[12];
                alarmSetListMap = new HashMap();
                timerSetListMap = new HashMap();
            }
        }
        return sysApplication;
    }

    public byte[] EcodeFile() {
        try {
            InputStream input = new FileInputStream(DatabaseManager.getInstance().mDBHelper.mFile);
            this.buffer = new byte[((int) DatabaseManager.getInstance().mDBHelper.mFile.length())];
            input.read(this.buffer);
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return this.buffer;
    }

    public void addActivity(Activity activity) {
        for (int i = 0; i < activityList.size(); i++) {
            if (activity.getComponentName().getClassName().equals(((Activity) activityList.get(i)).getComponentName().getClassName())) {
                //((Activity) activityList.get(i)).finish();
                activityList.remove(i);
            }
        }
        activityList.add(activity);
    }

    public void addDevice(Device device) {
        deviceList.add(device);
    }

    public Activity getCurrentActivity() {
        if (activityList.size() > 0) {
            return (Activity) activityList.get(activityList.size() - 1);
        }
        return null;
    }

    public Activity getLastActivity() {
        if (activityList.size() - 2 > 0) {
            return (Activity) activityList.get(activityList.size() - 2);
        }
        return null;
    }

    public void LoopThenExit() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityList = null;
        this.multicastLock.release();
        DeviceSocket.getInstance().unInit();
        Process.killProcess(Process.myPid());
    }

    public void openwifi(Context context) {
        this.multicastLock = ((WifiManager) context.getSystemService(WIFI_SERVICE)).createMulticastLock("logoactivity.udp");
        this.multicastLock.acquire();
    }

    public boolean checkSDcard() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public Bitmap ImgRoom(Bitmap bm, int newwidth, int newheight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (width == 0 || height == 0) {
            width = bm.getWidth();
            height = bm.getHeight();
        }
        float scaleWidth = ((float) newwidth) / ((float) width);
        float scaleHeight = ((float) newheight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public static int readPictureDegree(String path) {
        try {
            switch (new ExifInterface(path).getAttributeInt("Orientation", 1)) {
                case FragmentManager.ANIM_STYLE_CLOSE_ENTER /*3*/:
                    return 180;
                case FragmentManager.ANIM_STYLE_FADE_EXIT /*6*/:
                    return 90;
                case Message.PASSWORD_BUFFER_SIZE /*8*/:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle);
        System.out.println("angle2=" + angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public File copyFile(String oldPath, String newPath) {
        File file = new File(newPath);
        try {
            if (!new File(oldPath).exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] bt = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                while (true) {
                    int c = inStream.read(bt);
                    if (c <= 0) {
                        break;
                    }
                    fs.write(bt, 0, c);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void broadcastEvent(int event, int arg1, Object data) {
        mEventManager.broadcastEvent(event, arg1, data);
    }

    public void subscibeEvent(String owner, int event, Handler receiver) {
        mEventManager.subscibeEvent(owner, event, receiver);
    }

    public void removeEvent(String owner, int event) {
        mEventManager.removeEvent(owner, event);
    }

    public boolean isNetworkConnected(Context context) {
        NetworkInfo network = ((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (network != null) {
            System.out.println("***11111111111111111111111111");
            return network.isAvailable();
        }
        System.out.println("***2222222222222222222222222222");
        return false;
    }

    public void addCommonsNet(CommonsNet commonsNet) {
        commonsNetList.add(commonsNet);
    }

    public CommonsNet getCurrNetworkConn() {
        if (commonsNetList == null || commonsNetList.size() == 0) {
            return null;
        }
        return (CommonsNet) commonsNetList.get(commonsNetList.size() - 1);
    }

    public static String getFileNameNoEx(String filename) {
        if (filename == null || filename.length() <= 0) {
            return filename;
        }
        int dot = filename.lastIndexOf(46);
        if (dot <= -1 || dot >= filename.length()) {
            return filename;
        }
        return filename.substring(0, dot);
    }

    public String FormatString(int value) {
        String strValue = "";
        byte[] ary = intToByteArray(value);
        for (int i = ary.length - 1; i >= 0; i--) {
            strValue = new StringBuilder(String.valueOf(strValue)).append(ary[i] & MotionEventCompat.ACTION_MASK).toString();
            if (i > 0) {
                strValue = new StringBuilder(String.valueOf(strValue)).append(".").toString();
            }
        }
        return strValue;
    }

    public byte[] FormatStringForByte(byte[] ary) {
        if (ary == null) {
            return null;
        }
        int i = 0;
        while (i < ary.length && ary[i] != 0) {
            i++;
        }
        if (i <= 0) {
            return null;
        }
        byte[] strValue = new byte[i];
        System.arraycopy(ary, 0, strValue, 0, i);
        return strValue;
    }

    public byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) ((value >>> (((b.length - 1) - i) * 8)) & MotionEventCompat.ACTION_MASK);
        }
        return b;
    }

    public String execCalc(String Netmask, String Netaddr) {
        int i;
        String[] tm = Netmask.split("\\.");
        StringBuffer sb = new StringBuffer();
        for (i = 0; i < tm.length; i++) {
            tm[i] = String.valueOf(Integer.parseInt(tm[i]) ^ -1);
        }
        String[] tm2 = Netaddr.split("\\.");
        for (i = 0; i < tm.length; i++) {
            tm[i] = String.valueOf(Integer.parseInt(tm2[i]) | Integer.parseInt(tm[i]));
        }
        for (String parseInt : tm) {
            sb.append(intTOstr(Integer.parseInt(parseInt)));
            sb.append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return parseIp(sb.toString());
    }

    private String parseIp(String fbg) {
        String[] tm = fbg.split("\\.");
        StringBuffer sb = new StringBuffer();
        for (String strToint : tm) {
            sb.append(strToint(strToint));
            sb.append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private String intTOstr(int num) {
        String str = "";
        str = Integer.toBinaryString(num);
        int c = 8 - str.length();
        for (int i = 0; i < c; i++) {
            str = "0" + str;
        }
        if (c < 0) {
            return str.substring(24, 32);
        }
        return str;
    }

    private int strToint(String str) {
        int total = 0;
        int top = str.length();
        for (int i = 0; i < str.length(); i++) {
            top--;
            total += ((int) Math.pow(2.0d, (double) top)) * Integer.parseInt(String.valueOf(str.charAt(i)));
        }
        return total;
    }

    public byte[] getWifiSSID(Context context) {
        byte[] bytessid = null;
        String strend = "\"";
        WifiManager wifimanage = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (wifimanage.isWifiEnabled()) {
            String strSSID = wifimanage.getConnectionInfo().getSSID();
            System.out.println("ssid " + strSSID);
            if (strSSID != null) {
                try {
                    if (strSSID.startsWith(strend) && strSSID.endsWith(strend)) {
                        strSSID = strSSID.substring(1, strSSID.length() - 1);
                    }
                    bytessid = FormatStringForByte(strSSID.getBytes("UTF-8"));
                    System.out.println("bytessid " + bytessid);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytessid;
    }

    public void SysUpdateGateWayInfo(Gateway gateway) {
        DatabaseManager.getInstance().UpdateGateWayInfo(gateway);
    }

    public void SysAddGatewayInfo(Gateway gateway) {
        DatabaseManager.getInstance().AddGatewayInfo(gateway);
    }

    public void SysdeleteGatewayOfCurrGateway(Gateway gateway) {
        DatabaseManager.getInstance().deleteGatewayOfCurrGateway(gateway.getGateWayInfoIndex());
    }

    public ArrayList<Gateway> SysSelectGatewayInfo() {
        ArrayList<Gateway> mGatewayList = DatabaseManager.getInstance().SelectGatewayInfo1();
        if (mGatewayList != null) {
            mGatewayList.size();
        }
        return mGatewayList;
    }

    public ArrayList<Gateway> SysSelectGatewayInfofirst() {
        return DatabaseManager.getInstance().SelectGatewayInfo();
    }

    public Gateway getCurrGateway(Context context) {
        byte[] SSID = getWifiSSID(context);
        if (SSID == null) {
            return null;
        }
        return getCurrGatewayCheck(SSID);
    }

    public Gateway getGatewayMac(byte[] macaddr) {
        if (macaddr == null) {
            return null;
        }
        ArrayList<Gateway> mGatewayList = DatabaseManager.getInstance().SelectGatewayInfo1();
        int i = 0;
        while (i < mGatewayList.size()) {
            if (macaddr != null && Arrays.equals(macaddr, ((Gateway) mGatewayList.get(i)).getMacAddress())) {
                return (Gateway) mGatewayList.get(i);
            }
            i++;
        }
        return null;
    }

    public Gateway getCurrGatewayCheck(byte[] SSID) {
        if (SSID == null) {
            return null;
        }
        ArrayList<Gateway> mGatewayList = DatabaseManager.getInstance().SelectGatewayInfo1();
        System.out.println("*** gateway size " + mGatewayList.size());
        int i = 0;
        while (i < mGatewayList.size()) {
            System.out.println("*** gateway index " + i + " is " + mGatewayList.get(i));
            if (SSID != null && Arrays.equals(SSID, ((Gateway) mGatewayList.get(i)).getSSID())) {
                Gateway gateway = (Gateway) mGatewayList.get(i);
                System.out.println("*** gateway dns is " + gateway.getDNS());
                System.out.println("*** gateway ip is " + gateway.getIP());
                System.out.println("*** gateway macaddress is " + gateway.getMacAddress());
                return (Gateway) mGatewayList.get(i);
            }
            i++;
        }


        return null;
    }

    public void SetEditType(EditText text) {
        text.addTextChangedListener(new C03921());
    }

    public void SetEditTypehour(EditText text) {
        text.addTextChangedListener(new C03932());
    }

    public void SetEditTypeminute(EditText text) {
        text.addTextChangedListener(new C03943());
    }

    public static byte[] _getTimeFromCurrentTo() {
        byte[] data = new byte[6];
        int time = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse("2000-01-01 00:00:00");
            Date curDate = new Date();
            Calendar baseCalendar = Calendar.getInstance();
            baseCalendar.setTime(date);
            //int baseDstOffest = baseCalendar.get(16);
            Calendar curCalendar = Calendar.getInstance();
            curCalendar.setTime(dateFormat.parse(dateFormat.format(curDate)));
            //int curDstOffest = curCalendar.get(16);
            //long j = (long) curDstOffest;
            time = (int) ((((curCalendar.getTimeInMillis() - baseCalendar.getTimeInMillis()) - ((long) 0))) / 1000);
            int cur2 = (TimeZone.getDefault().getRawOffset() + 0) / 60000;
            data[4] = (byte) (cur2 / 60);
            data[5] = (byte) (cur2 % 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        data[0] = (byte) (time >> 24);
        data[1] = (byte) (time >> 16);
        data[2] = (byte) (time >> 8);
        data[3] = (byte) (time & MotionEventCompat.ACTION_MASK);
        return data;
    }

    public String getLocalIpAddress(Context context) {
        WifiManager wifimanage = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (!wifimanage.isWifiEnabled()) {
            return null;
        }
        int ipAddress = wifimanage.getConnectionInfo().getIpAddress();
        String ipString = (ipAddress & MotionEventCompat.ACTION_MASK) + "." + ((ipAddress >> 8) & MotionEventCompat.ACTION_MASK) + "." + ((ipAddress >> 16) & MotionEventCompat.ACTION_MASK) + "." + ((ipAddress >> 24) & MotionEventCompat.ACTION_MASK);
        Log.i("..............", new StringBuilder(String.valueOf(ipString)).toString());
        return ipString;
    }

    public boolean UpdataConnWay(CommonsNet mCommonsNet) {
        if (commonsNetList != null && commonsNetList.size() > 0) {
            for (int i = 0; i < commonsNetList.size(); i++) {
                if (((CommonsNet) commonsNetList.get(i)).getmPort() == mCommonsNet.getmPort()) {
                    commonsNetList.remove(i);
                }
            }
        }
        return true;
    }

    private static boolean isContainsChinese(char c) {
        UnicodeBlock ub = UnicodeBlock.of(c);
        if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public CommonsNet JudgeConnWay(byte[] MacAddress, Gateway curGateway, boolean bolcheckwifi, boolean bolisNetworkConnected) {
        CommonsNet mCommonsNet;
        UnsupportedEncodingException e;
        CommonsNet mCommonsNet2 = null;
        if (bolcheckwifi || bolisNetworkConnected) {
            Gateway mDevGateway = getGatewayMac(MacAddress);
            if (curGateway != null) {
                try {
                    if (Arrays.equals(curGateway.getSSID(), mDevGateway.getSSID())) {
                        mCommonsNet = new CommonsNet();
                        try {
                            mCommonsNet.init(new String(FormatStringForByte(curGateway.getIP()), "UTF-8"), 50000, curGateway.getMacAddress(), curGateway.getPassWord());
                            mCommonsNet.setmConnWay((byte) 1);
                            mCommonsNet2 = mCommonsNet;
                            return mCommonsNet;
                        } catch (UnsupportedEncodingException e2) {
                            e = e2;
                            mCommonsNet2 = mCommonsNet;
                            e.printStackTrace();
                            return mCommonsNet2;
                        }
                    }
                    mCommonsNet = new CommonsNet();
                    mCommonsNet.init(new String(FormatStringForByte(mDevGateway.getIP()), "UTF-8"), 50000, mDevGateway.getMacAddress(), mDevGateway.getPassWord());
                    mCommonsNet.setmConnWay((byte) 3);
                    mCommonsNet2 = mCommonsNet;
                    return mCommonsNet;
                } catch (UnsupportedEncodingException e3) {
                    e = e3;
                    e.printStackTrace();
                    return mCommonsNet2;
                }
            } else if (bolisNetworkConnected) {
                try {
                    if (new String(mDevGateway.getSSID(), "UTF-8").indexOf("iLightsIn") == -1) {
                        mCommonsNet = new CommonsNet();
                        mCommonsNet.init(new String(FormatStringForByte(mDevGateway.getDNS()), "UTF-8"), mDevGateway.getPort(), mDevGateway.getMacAddress(), mDevGateway.getPassWord());
                        mCommonsNet.setmConnWay((byte) 3);
                        mCommonsNet2 = mCommonsNet;
                        return mCommonsNet;
                    }
                } catch (Exception e2) {}

            }
        }
        Log.i("\u6ca1\u6709\u7f51\u7edc\uff0c\u8fde\u63a5\u5931\u8d25", "\u6ca1\u6709\u7f51\u7edc\uff0c\u8fde\u63a5\u5931\u8d25");
        return mCommonsNet2;
    }

    public static void savePhotoToSDCard(String path, String photoName, Bitmap photoBitmap) {
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        if (Environment.getExternalStorageState().equals("mounted")) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName);
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    try {
                        if (photoBitmap.compress(CompressFormat.JPEG, 1, fileOutputStream2)) {
                            fileOutputStream2.flush();
                        }
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        fileOutputStream = fileOutputStream2;
                        try {
                            photoFile.delete();
                            e.printStackTrace();
                            try {
                                fileOutputStream.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                                return;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                fileOutputStream.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                            //throw th;
                        }
                    } catch (IOException e4) {
                        //e222 = e4;
                        fileOutputStream = fileOutputStream2;
                        photoFile.delete();
                        //e222.printStackTrace();
                        try {
                            fileOutputStream.close();
                        } catch (IOException e2222) {
                            e2222.printStackTrace();
                            return;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream = fileOutputStream2;
                        fileOutputStream.close();
                        //throw th;
                    }
                }
                try {
                    fileOutputStream2.close();
                } catch (IOException e22222) {
                    e22222.printStackTrace();
                }
            } catch (FileNotFoundException e5) {
                e = e5;
                photoFile.delete();
                e.printStackTrace();
                try {
                    fileOutputStream.close();
                } catch (Exception e1) {}

            } catch (IOException e6) {
                //e22222 = e6;
                photoFile.delete();
                //e22222.printStackTrace();
                try {
                    fileOutputStream.close();
                } catch (Exception e1) {}
            }
        }
    }

    class FragmentManager
    {
        public static final int ANIM_STYLE_CLOSE_ENTER = 3;
        public static final int ANIM_STYLE_FADE_EXIT = 6;
    }
}
