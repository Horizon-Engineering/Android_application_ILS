package com.example.hesolutions.horizon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allin.activity.action.SysApplication;
import com.allin.activity.action.SysApplication.AdnNameLengthFilter;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Device;
import com.homa.hls.database.Scene;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AddSceneActivity extends Activity {
    private static final int CHOOSE_PICTURE = 2;
    private static final int TAKE_PHOTO = 1;
    ImageView addscene_iv_blank;
    ArrayList<Bitmap> bitmaplist;
    Bitmap bm;
    private int[] bottomGridViewMenuImg;
    String[] bottomGridViewMenuText;
    GridViewMenuAdapter bottomMenuAdapter;
    Bitmap btm;
    private GridView gridViewMenu;
    HashMap<String, Object> hashMap;
    LayoutInflater inflater;
    boolean isResume;
    ImageView mAddscene_iv;
    Button mBackButton;
    Button mChooseDeviceButton;
    ArrayList<Device> mDeviceListOfCurrAreaOrScene;
    EditText mEditText;
    Button mSaveButton;
    Dialog mdialog;
    Button mmenuButton;
    String photo;
    String picturePath;
    Scene scene;
    Toast toast;

    /* renamed from: com.allin.activity.AddSceneActivity.1 */
    class C00521 implements OnKeyListener {
        C00521() {
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

    /* renamed from: com.allin.activity.AddSceneActivity.2 */
    class C00532 implements OnItemClickListener {
        C00532() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            /*
            Intent intent = new Intent(AddSceneActivity.this, MainActivity.class);
            intent.putExtra("mainactivity", arg2);
            AddSceneActivity.this.startActivity(intent);
            AddSceneActivity.this.overridePendingTransition(0, 0);
            AddSceneActivity.this.finish();
            */
        }
    }

    /* renamed from: com.allin.activity.AddSceneActivity.3 */
    class C00543 implements OnClickListener {
        C00543() {
        }

        public void onClick(View v) {
            if (AddSceneActivity.this.mdialog != null && AddSceneActivity.this.mdialog.isShowing()) {
                AddSceneActivity.this.mdialog.cancel();
                AddSceneActivity.this.mdialog = null;
            }
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
                arg1 = LayoutInflater.from(AddSceneActivity.this).inflate(R.layout.bottom_menu_item, null);
            }
            ImageView img = (ImageView) arg1.findViewById(R.id.img_item_data);
            ((TextView) arg1.findViewById(R.id.text_item_data)).setText(AddSceneActivity.this.bottomGridViewMenuText[arg0]);
            img.setImageResource(AddSceneActivity.this.bottomGridViewMenuImg[arg0]);
            return arg1;
        }
    }

    class WeightListening implements OnClickListener {

        /* renamed from: com.allin.activity.AddSceneActivity.WeightListening.1 */
        class C00551 implements OnClickListener {
            C00551() {
            }

            public void onClick(View v) {
                if (AddSceneActivity.this.mdialog != null && AddSceneActivity.this.mdialog.isShowing()) {
                    AddSceneActivity.this.mdialog.cancel();
                    AddSceneActivity.this.mdialog = null;
                }
                AddSceneActivity.this.mEditText.setText("");
                if (!(AddSceneActivity.this.bitmaplist == null || AddSceneActivity.this.bitmaplist.size() <= 0 || AddSceneActivity.this.bitmaplist.get(0) == null || ((Bitmap) AddSceneActivity.this.bitmaplist.get(0)).isRecycled())) {
                    ((Bitmap) AddSceneActivity.this.bitmaplist.get(0)).recycle();
                    AddSceneActivity.this.bitmaplist.remove(0);
                }
                AddSceneActivity.this.mAddscene_iv.setImageResource(R.drawable.pic_broder);
            }
        }

        /* renamed from: com.allin.activity.AddSceneActivity.WeightListening.2 */
        class C00582 implements OnClickListener {

            /* renamed from: com.allin.activity.AddSceneActivity.WeightListening.2.1 */
            class C00561 implements OnClickListener {
                C00561() {
                }

                public void onClick(View v) {
                    if (AddSceneActivity.this.mdialog != null && AddSceneActivity.this.mdialog.isShowing()) {
                        AddSceneActivity.this.mdialog.cancel();
                        AddSceneActivity.this.mdialog = null;
                    }
                }
            }

            /* renamed from: com.allin.activity.AddSceneActivity.WeightListening.2.2 */
            class C00572 implements OnClickListener {
                C00572() {
                }

                public void onClick(View v) {
                    File file;
                    if (AddSceneActivity.this.mdialog != null && AddSceneActivity.this.mdialog.isShowing()) {
                        AddSceneActivity.this.mdialog.cancel();
                        AddSceneActivity.this.mdialog = null;
                    }
                    if (AddSceneActivity.this.picturePath != null) {
                        file = new File(AddSceneActivity.this.picturePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    if (AddSceneActivity.this.photo != null) {
                        file = new File(AddSceneActivity.this.photo);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    if (!(AddSceneActivity.this.bitmaplist == null || AddSceneActivity.this.bitmaplist.size() <= 0 || AddSceneActivity.this.bitmaplist.get(0) == null || ((Bitmap) AddSceneActivity.this.bitmaplist.get(0)).isRecycled())) {
                        ((Bitmap) AddSceneActivity.this.bitmaplist.get(0)).recycle();
                        AddSceneActivity.this.bitmaplist.remove(0);
                    }
                    AddSceneActivity.this.mAddscene_iv.setImageResource(R.drawable.pic_broder);
                }
            }

            C00582() {
            }

            public void onClick(View v) {
                if (AddSceneActivity.this.mdialog != null && AddSceneActivity.this.mdialog.isShowing()) {
                    AddSceneActivity.this.mdialog.cancel();
                    AddSceneActivity.this.mdialog = null;
                }
                View view = AddSceneActivity.this.inflater.inflate(R.layout.msg_dialog, null);
                Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
                Button btn_no = (Button) view.findViewById(R.id.btn_cancel);
                ((TextView) view.findViewById(R.id.textinfor)).setText(AddSceneActivity.this.getResources().getString(R.string.isdelete));
                AddSceneActivity.this.mdialog = new Dialog(AddSceneActivity.this, R.style.Theme_dialog);
                AddSceneActivity.this.mdialog.setContentView(view);
                AddSceneActivity.this.mdialog.setCancelable(true);
                AddSceneActivity.this.mdialog.setCanceledOnTouchOutside(false);
                AddSceneActivity.this.mdialog.show();
                btn_no.setOnClickListener(new C00561());
                btn_ok.setOnClickListener(new C00572());
            }
        }

        /* renamed from: com.allin.activity.AddSceneActivity.WeightListening.3 */
        class C00593 implements OnClickListener {
            C00593() {
            }

            public void onClick(View v) {
                if (AddSceneActivity.this.mdialog != null && AddSceneActivity.this.mdialog.isShowing()) {
                    AddSceneActivity.this.mdialog.cancel();
                    AddSceneActivity.this.mdialog = null;
                }
                AddSceneActivity.this.isResume = false;
                Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                openCameraIntent.putExtra("output", Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/iLightsIn/", "AreaItemimage.jpg")));
                AddSceneActivity.this.startActivityForResult(openCameraIntent, AddSceneActivity.TAKE_PHOTO);
            }
        }

        /* renamed from: com.allin.activity.AddSceneActivity.WeightListening.4 */
        class C00604 implements OnClickListener {
            C00604() {
            }

            public void onClick(View v) {
                if (AddSceneActivity.this.mdialog != null && AddSceneActivity.this.mdialog.isShowing()) {
                    AddSceneActivity.this.mdialog.cancel();
                    AddSceneActivity.this.mdialog = null;
                }
                AddSceneActivity.this.isResume = false;
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                AddSceneActivity.this.startActivityForResult(intent, AddSceneActivity.CHOOSE_PICTURE);
            }
        }

        /* renamed from: com.allin.activity.AddSceneActivity.WeightListening.5 */
        class C00615 implements OnClickListener {
            C00615() {
            }

            public void onClick(View v) {
                if (AddSceneActivity.this.mdialog != null && AddSceneActivity.this.mdialog.isShowing()) {
                    AddSceneActivity.this.mdialog.cancel();
                    AddSceneActivity.this.mdialog = null;
                }
            }
        }

        WeightListening() {
        }

        public void onClick(View v) {
            AddSceneActivity addSceneActivity = AddSceneActivity.this;
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            View view;
            Intent intent;
            switch (v.getId()) {
                case R.id.menu_areaitem:
                    view = AddSceneActivity.this.inflater.inflate(R.layout.chooseimg_method, null);
                    addSceneActivity = AddSceneActivity.this;
                    addSceneActivity.mdialog = new Dialog(AddSceneActivity.this, R.style.Theme_dialog);
                    AddSceneActivity.this.mdialog.setContentView(view);
                    AddSceneActivity.this.mdialog.setCancelable(true);
                    AddSceneActivity.this.mdialog.setCanceledOnTouchOutside(false);
                    AddSceneActivity.this.mdialog.show();
                    Button mPhotograph_btn = (Button) view.findViewById(R.id.photograph_btn);
                    Button mChoosefromphoto_btn = (Button) view.findViewById(R.id.choosefromphoto_btn);
                    Button mCancle_btn = (Button) view.findViewById(R.id.cancle_btn);
                    Button mDelimg_btn = (Button) view.findViewById(R.id.delimg_btn);
                    RelativeLayout mDelimg_layout = (RelativeLayout) view.findViewById(R.id.rllt_delimg);
                    if (AddSceneActivity.this.bitmaplist != null) {
                        if (AddSceneActivity.this.bitmaplist.size() > 0) {
                            mDelimg_layout.setVisibility(View.VISIBLE);
                            mDelimg_btn.setOnClickListener(new C00582());
                            mPhotograph_btn.setOnClickListener(new C00593());
                            mChoosefromphoto_btn.setOnClickListener(new C00604());
                            mCancle_btn.setOnClickListener(new C00615());
                        }
                    }
                    mDelimg_layout.setVisibility(View.GONE);
                    mDelimg_btn.setOnClickListener(new C00582());
                    mPhotograph_btn.setOnClickListener(new C00593());
                    mChoosefromphoto_btn.setOnClickListener(new C00604());
                    mCancle_btn.setOnClickListener(new C00615());
                case R.id.btn_back_addscene:
                    /*
                    intent = new Intent(AddSceneActivity.this, MainActivity.class);
                    intent.putExtra("mainactivity", 0);
                    AddSceneActivity.this.startActivity(intent);
                    AddSceneActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    AddSceneActivity.this.finish();
                    */
                case R.id.addscene_choose_device:
                    String SceneName = AddSceneActivity.this.mEditText.getText().toString();
                    intent = new Intent(AddSceneActivity.this, ChooseDeviceActivity.class);
                    intent.putExtra("index", "AddSceneToChooseDevice");
                    intent.putExtra("SceneName", SceneName);
                    String str = "scene";
                    intent.putExtra(str, AddSceneActivity.this.scene);
                    if (AddSceneActivity.this.picturePath != null) {
                        str = "picturePath";
                        intent.putExtra(str, AddSceneActivity.this.picturePath);
                    } else {
                        if (AddSceneActivity.this.photo != null) {
                            str = "photo";
                            intent.putExtra(str, AddSceneActivity.this.photo);
                        }
                    }
                    AddSceneActivity.this.startActivity(intent);
                    AddSceneActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    AddSceneActivity.this.finish();
                case R.id.addscene_save:
                    if (AddSceneActivity.this.mEditText.getText().toString().equals("")) {
                        AddSceneActivity.this.DialogTip(AddSceneActivity.this.getResources().getString(R.string.add_fail_name));
                        return;
                    }
                    if (AddSceneActivity.this._findSceneName(AddSceneActivity.this.mEditText.getText().toString())) {
                        AddSceneActivity.this.DialogTip(AddSceneActivity.this.getResources().getString(R.string.add_fail_ov));
                        return;
                    }
                    AddSceneActivity.this.scene.setSceneName(AddSceneActivity.this.mEditText.getText().toString());
                    if (AddSceneActivity.this.picturePath != null) {
                        String newfileName = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/iLightsIn/homeSystemSceneImage/" + AddSceneActivity.this.scene.getSceneName() + ".jpg";
                        File file = new File(AddSceneActivity.this.picturePath);
                        File newfile = new File(newfileName);
                        if (file.exists()) {
                            file.renameTo(newfile);
                            AddSceneActivity.this.scene.setPictureName(AddSceneActivity.this.scene.getSceneName());
                        }
                    }
                    if (DatabaseManager.getInstance().addScene(AddSceneActivity.this.scene)) {
                        if (AddSceneActivity.this.mDeviceListOfCurrAreaOrScene != null) {
                            int i = 0;
                            while (true) {
                                if (i < AddSceneActivity.this.mDeviceListOfCurrAreaOrScene.size()) {
                                    DatabaseManager.getInstance().AddSceneDevice(AddSceneActivity.this.scene, (Device) AddSceneActivity.this.mDeviceListOfCurrAreaOrScene.get(i));
                                    i += AddSceneActivity.TAKE_PHOTO;
                                }
                            }
                        }
                        AddSceneActivity.this.mDeviceListOfCurrAreaOrScene = null;
                        view = AddSceneActivity.this.inflater.inflate(R.layout.msg_dialog_ok, null);
                        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
                        ((TextView) view.findViewById(R.id.textinfor)).setText(AddSceneActivity.this.getResources().getString(R.string.add_success));
                        addSceneActivity = AddSceneActivity.this;
                        addSceneActivity.mdialog = new Dialog(AddSceneActivity.this, R.style.Theme_dialog);
                        AddSceneActivity.this.mdialog.setContentView(view);
                        AddSceneActivity.this.mdialog.setCancelable(true);
                        AddSceneActivity.this.mdialog.setCanceledOnTouchOutside(false);
                        AddSceneActivity.this.mdialog.show();
                        btn_ok.setOnClickListener(new C00551());
                        return;
                    }
                    AddSceneActivity.this.DialogTip(AddSceneActivity.this.getResources().getString(R.string.add_fail));
                default:
            }
        }
    }

    public AddSceneActivity() {
        this.bottomGridViewMenuImg = new int[]{R.drawable.scene, R.drawable.area, R.drawable.setting};
        this.gridViewMenu = null;
        this.mBackButton = null;
        this.mmenuButton = null;
        this.mChooseDeviceButton = null;
        this.mSaveButton = null;
        this.mEditText = null;
        this.mAddscene_iv = null;
        this.scene = null;
        this.picturePath = null;
        this.photo = null;
        this.bm = null;
        this.btm = null;
        this.toast = null;
        this.isResume = true;
        this.bottomMenuAdapter = null;
        this.bottomGridViewMenuText = null;
        this.hashMap = null;
        this.bitmaplist = new ArrayList();
        this.addscene_iv_blank = null;
        this.mdialog = null;
        this.inflater = null;
        this.mDeviceListOfCurrAreaOrScene = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addscene);
        SysApplication.getInstance().addActivity(this);
        this.inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        findViewsById();
        if (DatabaseManager.getInstance().mDBHelper == null) {
            DatabaseManager.getInstance().DatabaseInit(this);
        }
        this.scene = new Scene();
        loadBottomGridViewMenu();
        WeightListening();
        String sceneName = getIntent().getStringExtra("SceneName");
        if (sceneName != null) {
            this.mEditText.setText(sceneName);
        }
        this.photo = getIntent().getStringExtra("photo");
        this.picturePath = getIntent().getStringExtra("picturePath");
        Options options;
        Bitmap btms;
        if (this.photo != null) {
            try {
                options = new Options();
                options.inSampleSize = CHOOSE_PICTURE;
                this.bm = BitmapFactory.decodeFile(this.photo, options);
                if (this.bm != null) {
                    btms = SysApplication.getInstance().ImgRoom(this.bm, getResources().getDrawable(R.drawable.pic_broder).getIntrinsicWidth(), getResources().getDrawable(R.drawable.pic_broder).getIntrinsicHeight());
                    if (btms != null) {
                        this.mAddscene_iv.setImageBitmap(btms);
                        this.bitmaplist.add(btms);
                        if (!(this.bitmaplist == null || this.bitmaplist.size() <= TAKE_PHOTO || this.bitmaplist.get(0) == null || ((Bitmap) this.bitmaplist.get(0)).isRecycled())) {
                            ((Bitmap) this.bitmaplist.get(0)).recycle();
                            this.bitmaplist.remove(0);
                        }
                    }
                }
            } catch (Exception e) {
                if (this.toast == null) {
                    this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                } else {
                    this.toast.setText(getString(R.string.error));
                }
                this.toast.show();
            } catch (OutOfMemoryError e2) {
                if (this.toast == null) {
                    this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                } else {
                    this.toast.setText(getString(R.string.error));
                }
                this.toast.show();
            }
        } else if (this.picturePath != null) {
            try {
                options = new Options();
                options.inSampleSize = CHOOSE_PICTURE;
                this.bm = BitmapFactory.decodeFile(this.picturePath, options);
                if (this.bm != null) {
                    btms = SysApplication.getInstance().ImgRoom(this.bm, getResources().getDrawable(R.drawable.pic_broder).getIntrinsicWidth(), getResources().getDrawable(R.drawable.pic_broder).getIntrinsicHeight());
                    if (btms != null) {
                        this.mAddscene_iv.setImageBitmap(btms);
                        this.bitmaplist.add(btms);
                        if (!(this.bitmaplist == null || this.bitmaplist.size() <= TAKE_PHOTO || this.bitmaplist.get(0) == null || ((Bitmap) this.bitmaplist.get(0)).isRecycled())) {
                            ((Bitmap) this.bitmaplist.get(0)).recycle();
                            this.bitmaplist.remove(0);
                        }
                    }
                }
            } catch (Exception e3) {
                if (this.toast == null) {
                    this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                } else {
                    this.toast.setText(getString(R.string.error));
                }
                this.toast.show();
            } catch (OutOfMemoryError e4) {
                if (this.toast == null) {
                    this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                } else {
                    this.toast.setText(getString(R.string.error));
                }
                this.toast.show();
            }
        }
        this.mDeviceListOfCurrAreaOrScene = (ArrayList) getIntent().getSerializableExtra("mDeviceListOfCurrAreaOrScene");
    }

    protected void onDestory() {
        super.onDestroy();
        this.bottomGridViewMenuImg = null;
        this.mDeviceListOfCurrAreaOrScene = null;
        this.scene = null;
        this.addscene_iv_blank = null;
        if (this.mdialog != null && this.mdialog.isShowing()) {
            this.mdialog.cancel();
            this.mdialog = null;
        }
        this.bottomMenuAdapter = null;
        if (this.gridViewMenu != null) {
            this.gridViewMenu.setAdapter(null);
            this.gridViewMenu = null;
        }
        if (this.toast != null) {
            this.toast.cancel();
            this.toast = null;
        }
        if (this.hashMap != null) {
            this.hashMap.clear();
            this.hashMap = null;
        }
        if (this.bitmaplist != null && this.bitmaplist.size() > 0) {
            for (int i = 0; i < this.bitmaplist.size(); i += TAKE_PHOTO) {
                ((Bitmap) this.bitmaplist.get(i)).recycle();
                this.bitmaplist.remove(i);
            }
            this.bitmaplist = null;
        }
        if (!(this.bm == null || this.bm.isRecycled())) {
            this.bm.recycle();
            this.bm = null;
        }
        if (!(this.btm == null || this.btm.isRecycled())) {
            this.btm.recycle();
            this.btm = null;
        }
        System.gc();
    }

    private void findViewsById() {
        this.gridViewMenu = (GridView) findViewById(R.id.scene_addscene_set);
        this.mBackButton = (Button) findViewById(R.id.btn_back_addscene);
        this.mmenuButton = (Button) findViewById(R.id.menu_areaitem);
        this.mChooseDeviceButton = (Button) findViewById(R.id.addscene_choose_device);
        this.mSaveButton = (Button) findViewById(R.id.addscene_save);
        this.mEditText = (EditText) findViewById(R.id.addscene_name);
        this.mEditText.setOnKeyListener(new C00521());
        InputFilter[] filters = new InputFilter[TAKE_PHOTO];
        filters[0] = new AdnNameLengthFilter();
        this.mEditText.setFilters(filters);
        this.mAddscene_iv = (ImageView) findViewById(R.id.addscene_iv);
    }

    private void loadBottomGridViewMenu() {
        if (this.hashMap != null) {
            this.hashMap.clear();
            this.hashMap = null;
        }
        this.bottomGridViewMenuText = null;
        this.bottomMenuAdapter = null;
        this.bottomGridViewMenuText = getResources().getStringArray(R.array.bottom_menu_text_data);
        this.bottomMenuAdapter = new GridViewMenuAdapter();
        this.gridViewMenu.setAdapter(this.bottomMenuAdapter);
        this.gridViewMenu.setSelector(new ColorDrawable(0));
        this.gridViewMenu.setOnItemClickListener(new C00532());
    }

    private void WeightListening() {
        this.mBackButton.setOnClickListener(new WeightListening());
        this.mChooseDeviceButton.setOnClickListener(new WeightListening());
        this.mSaveButton.setOnClickListener(new WeightListening());
        this.mmenuButton.setOnClickListener(new WeightListening());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            /*
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("mainactivity", 0);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
            */
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            Options options;
            Bitmap btm;
            Bitmap btms;
            FileOutputStream fileOutputStream;
            switch (requestCode) {
                case TAKE_PHOTO /*1*/:
                    if (SysApplication.getInstance().checkSDcard()) {
                        String strpathString = Environment.getExternalStorageDirectory() + "/iLightsIn/AreaItemimage.jpg";
                        options = new Options();
                        options.inSampleSize = CHOOSE_PICTURE;
                        Bitmap bms = BitmapFactory.decodeFile(strpathString, options);
                        if (bms != null) {
                            SysApplication.getInstance();
                            int degree = SysApplication.readPictureDegree(strpathString);
                            SysApplication.getInstance();
                            this.bm = SysApplication.rotaingImageView(degree, bms);
                            try {
                                new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/iLightsIn/homeSystemSceneImage/").mkdirs();
                                this.picturePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/iLightsIn/homeSystemSceneImage/11111.jpg";
                                FileOutputStream fos = new FileOutputStream(this.picturePath);
                                try {
                                    btm = SysApplication.getInstance().ImgRoom(this.bm, (getResources().getDrawable(R.drawable.pic_broder).getIntrinsicWidth() / CHOOSE_PICTURE) * 3, (getResources().getDrawable(R.drawable.pic_broder).getIntrinsicHeight() / CHOOSE_PICTURE) * 3);
                                    btms = SysApplication.getInstance().ImgRoom(this.bm, getResources().getDrawable(R.drawable.pic_broder).getIntrinsicWidth(), getResources().getDrawable(R.drawable.pic_broder).getIntrinsicHeight());
                                    btm.compress(CompressFormat.JPEG, 100, fos);
                                    if (btms != null) {
                                        this.mAddscene_iv.setImageBitmap(btms);
                                        this.bitmaplist.add(btms);
                                        if (!(this.bitmaplist == null || this.bitmaplist.size() <= TAKE_PHOTO || this.bitmaplist.get(0) == null || ((Bitmap) this.bitmaplist.get(0)).isRecycled())) {
                                            ((Bitmap) this.bitmaplist.get(0)).recycle();
                                            this.bitmaplist.remove(0);
                                        }
                                    }
                                    if (fos != null) {
                                        fos.flush();
                                        fos.close();
                                    }
                                    if (this.bm != null && !this.bm.isRecycled()) {
                                        this.bm.recycle();
                                        this.bm = null;
                                        System.gc();
                                        return;
                                    }
                                    return;
                                } catch (Exception e) {
                                    fileOutputStream = fos;
                                    if (this.toast != null) {
                                        this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                                    } else {
                                        this.toast.setText(getString(R.string.error));
                                    }
                                    this.toast.show();
                                    return;
                                } catch (OutOfMemoryError e2) {
                                    fileOutputStream = fos;
                                    if (this.toast != null) {
                                        this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                                    } else {
                                        this.toast.setText(getString(R.string.error));
                                    }
                                    this.toast.show();
                                    return;
                                }
                            } catch (Exception e3) {
                                if (this.toast != null) {
                                    this.toast.setText(getString(R.string.error));
                                } else {
                                    this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                                }
                                this.toast.show();
                                return;
                            } catch (OutOfMemoryError e4) {
                                if (this.toast != null) {
                                    this.toast.setText(getString(R.string.error));
                                } else {
                                    this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                                }
                                this.toast.show();
                                return;
                            }
                        }
                        return;
                    }
                    Toast.makeText(this, getResources().getString(R.string.sdnot), Toast.LENGTH_SHORT);
                case CHOOSE_PICTURE /*2*/:
                    Uri originalUri = data.getData();
                    try {
                        options = new Options();
                        options.inSampleSize = CHOOSE_PICTURE;
                        this.bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(originalUri), null, options);
                        if (this.bm != null) {
                            new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/iLightsIn/homeSystemSceneImage/").mkdirs();
                            this.picturePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/iLightsIn/homeSystemSceneImage/11111.jpg";
                            fileOutputStream = new FileOutputStream(this.picturePath);
                            btm = SysApplication.getInstance().ImgRoom(this.bm, (getResources().getDrawable(R.drawable.pic_broder).getIntrinsicWidth() / CHOOSE_PICTURE) * 3, (getResources().getDrawable(R.drawable.pic_broder).getIntrinsicHeight() / CHOOSE_PICTURE) * 3);
                            btms = SysApplication.getInstance().ImgRoom(this.bm, getResources().getDrawable(R.drawable.pic_broder).getIntrinsicWidth(), getResources().getDrawable(R.drawable.pic_broder).getIntrinsicHeight());
                            btm.compress(CompressFormat.JPEG, 100, fileOutputStream);
                            if (btms != null) {
                                this.mAddscene_iv.setImageBitmap(btms);
                                this.bitmaplist.add(btms);
                                if (!(this.bitmaplist == null || this.bitmaplist.size() <= TAKE_PHOTO || this.bitmaplist.get(0) == null || ((Bitmap) this.bitmaplist.get(0)).isRecycled())) {
                                    ((Bitmap) this.bitmaplist.get(0)).recycle();
                                    this.bitmaplist.remove(0);
                                }
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            }
                            if (this.bm != null && !this.bm.isRecycled()) {
                                this.bm.recycle();
                                this.bm = null;
                                System.gc();
                            }
                        }
                    } catch (Exception e5) {
                        if (this.toast == null) {
                            this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                        } else {
                            this.toast.setText(getString(R.string.error));
                        }
                        this.toast.show();
                    } catch (OutOfMemoryError e6) {
                        if (this.toast == null) {
                            this.toast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                        } else {
                            this.toast.setText(getString(R.string.error));
                        }
                        this.toast.show();
                    }
                default:
            }
        }
    }

    private boolean _findSceneName(String sceneName) {
        boolean ret = false;
        ArrayList<Scene> mSceneList = DatabaseManager.getInstance().getSceneList().getSceneArrayList();
        if (mSceneList != null) {
            Iterator it = mSceneList.iterator();
            while (it.hasNext()) {
                if (((Scene) it.next()).getSceneName().equalsIgnoreCase(sceneName)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
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
        btn_ok.setOnClickListener(new C00543());
    }
}
