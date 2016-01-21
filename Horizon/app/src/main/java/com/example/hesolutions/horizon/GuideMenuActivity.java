package com.example.hesolutions.horizon;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allin.activity.action.SysApplication;
import com.homa.hls.database.Area;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Gateway;
import com.homa.hls.database.Scene;
import com.homa.hls.widgetcustom.CustomGallery;

public class GuideMenuActivity extends Activity {
    private DisplayMetrics DM;
    private ImageView Imageview_gone;
    private Bitmap bitmap;
    private Button btn_back_adddevice;
    CustomGallery gallery;
    RelativeLayout guide_menu_head;
    private String help;
    private Animation hiddenRightMenuAnimation;
    private Animation hiddenTopMenuAnimation;
    private Animation hiddenbotMenuAnimation;
    private Animation hiddenleftMenuAnimation;
    private TextView img_num;
    private ImageView imgview_left;
    private ImageView imgview_right;
    private int[] item_img;
    private ListView listView1;
    AreaAdapter mAreaAdapter;
    RightMenuAdapter mRightMenuAdapter;
    String[] mstrMenuitemText;
    private TextView scene_tv;
    private Animation showRightMenuAnimation;
    private Animation showTopMenuAnimation;
    private Animation showbotMenuAnimation;
    private Animation showleftMenuAnimation;

    /* renamed from: com.allin.activity.GuideMenuActivity.1 */
    class C02571 implements OnItemClickListener {
        C02571() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            GuideMenuActivity.this.guide_menu_head.startAnimation(GuideMenuActivity.this.hiddenTopMenuAnimation);
            GuideMenuActivity.this.guide_menu_head.setVisibility(View.VISIBLE);
            GuideMenuActivity.this.listView1.startAnimation(GuideMenuActivity.this.hiddenRightMenuAnimation);
            GuideMenuActivity.this.listView1.setVisibility(View.VISIBLE);
            GuideMenuActivity.this.Imageview_gone.startAnimation(GuideMenuActivity.this.hiddenleftMenuAnimation);
            GuideMenuActivity.this.Imageview_gone.setVisibility(View.VISIBLE);
            GuideMenuActivity.this.img_num.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
            GuideMenuActivity.this.imgview_left.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
            GuideMenuActivity.this.imgview_right.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
            GuideMenuActivity.this.img_num.setVisibility(View.INVISIBLE);
            GuideMenuActivity.this.imgview_left.setVisibility(View.INVISIBLE);
            GuideMenuActivity.this.imgview_right.setVisibility(View.INVISIBLE);
            GuideMenuActivity.this.gallery.setSelection(arg2);
            GuideMenuActivity.this.ChangeNum(arg2);
        }
    }

    /* renamed from: com.allin.activity.GuideMenuActivity.2 */
    class C02582 implements OnItemClickListener {
        C02582() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (GuideMenuActivity.this.guide_menu_head.getVisibility() == View.VISIBLE) {
                GuideMenuActivity.this.scene_tv.setText(GuideMenuActivity.this.mstrMenuitemText[arg2]);
                GuideMenuActivity.this.mRightMenuAdapter.setImgIndex(arg2);
                GuideMenuActivity.this.mRightMenuAdapter.notifyDataSetInvalidated();
                GuideMenuActivity.this.listView1.setSelection(arg2);
                GuideMenuActivity.this.guide_menu_head.startAnimation(GuideMenuActivity.this.showTopMenuAnimation);
                GuideMenuActivity.this.guide_menu_head.setVisibility(View.INVISIBLE);
                GuideMenuActivity.this.listView1.startAnimation(GuideMenuActivity.this.showRightMenuAnimation);
                GuideMenuActivity.this.listView1.setVisibility(View.INVISIBLE);
                GuideMenuActivity.this.Imageview_gone.startAnimation(GuideMenuActivity.this.showleftMenuAnimation);
                GuideMenuActivity.this.Imageview_gone.setVisibility(View.INVISIBLE);
                GuideMenuActivity.this.img_num.startAnimation(GuideMenuActivity.this.hiddenbotMenuAnimation);
                GuideMenuActivity.this.imgview_left.startAnimation(GuideMenuActivity.this.hiddenbotMenuAnimation);
                GuideMenuActivity.this.imgview_right.startAnimation(GuideMenuActivity.this.hiddenbotMenuAnimation);
                GuideMenuActivity.this.img_num.setVisibility(View.VISIBLE);
                GuideMenuActivity.this.imgview_left.setVisibility(View.VISIBLE);
                GuideMenuActivity.this.imgview_right.setVisibility(View.VISIBLE);
                return;
            }
            GuideMenuActivity.this.guide_menu_head.startAnimation(GuideMenuActivity.this.hiddenTopMenuAnimation);
            GuideMenuActivity.this.guide_menu_head.setVisibility(View.VISIBLE);
            GuideMenuActivity.this.listView1.startAnimation(GuideMenuActivity.this.hiddenRightMenuAnimation);
            GuideMenuActivity.this.listView1.setVisibility(View.VISIBLE);
            GuideMenuActivity.this.Imageview_gone.startAnimation(GuideMenuActivity.this.hiddenleftMenuAnimation);
            GuideMenuActivity.this.Imageview_gone.setVisibility(View.VISIBLE);
            GuideMenuActivity.this.img_num.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
            GuideMenuActivity.this.imgview_left.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
            GuideMenuActivity.this.imgview_right.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
            GuideMenuActivity.this.img_num.setVisibility(View.INVISIBLE);
            GuideMenuActivity.this.imgview_left.setVisibility(View.INVISIBLE);
            GuideMenuActivity.this.imgview_right.setVisibility(View.INVISIBLE);
        }
    }

    /* renamed from: com.allin.activity.GuideMenuActivity.3 */
    class C02593 implements OnItemSelectedListener {
        C02593() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            GuideMenuActivity.this.ChangeNum(arg2);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class AreaAdapter extends BaseAdapter {
        AreaAdapter() {
        }

        public int getCount() {
            return GuideMenuActivity.this.item_img.length;
        }

        public Object getItem(int arg0) {
            return Integer.valueOf(0);
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                arg1 = LayoutInflater.from(GuideMenuActivity.this).inflate(R.layout.guide_item_gridview, null);
            }
            ((ImageView) arg1.findViewById(R.id.guide_image_item)).setBackgroundResource(GuideMenuActivity.this.item_img[arg0]);
            return arg1;
        }
    }

    class RightMenuAdapter extends BaseAdapter {
        int img_index;

        RightMenuAdapter() {
            this.img_index = 0;
        }

        public int setImgIndex(int index) {
            this.img_index = index;
            return 0;
        }

        public int getCount() {
            return GuideMenuActivity.this.mstrMenuitemText.length;
        }

        public Object getItem(int arg0) {
            return Integer.valueOf(0);
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                arg1 = LayoutInflater.from(GuideMenuActivity.this).inflate(R.layout.guide_right_itemview, null);
            }
            TextView TextView1 = (TextView) arg1.findViewById(R.id.img_item_data);
            TextView1.setText(GuideMenuActivity.this.mstrMenuitemText[arg0]);
            if (this.img_index == arg0) {
                TextView1.setTextColor(getResources().getColor(R.color.black));
                TextView1.setBackgroundResource(R.drawable.guide_item_bgr_click);
            } else {
                TextView1.setTextColor(getResources().getColor(R.color.black));
                TextView1.setBackgroundResource(R.drawable.guide_item_bgr);
            }
            return arg1;
        }
    }

    class WeightListening implements OnClickListener {
        WeightListening() {
        }

        public void onClick(View v) {
            ((InputMethodManager) GuideMenuActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            switch (v.getId()) {
                case R.id.btn_back_adddevice:
                    GuideMenuActivity.this.helpclick();
                case R.id.Imageview_gone:
                    GuideMenuActivity.this.Imageview_gone.startAnimation(GuideMenuActivity.this.hiddenleftMenuAnimation);
                    GuideMenuActivity.this.Imageview_gone.setVisibility(View.VISIBLE);
                    GuideMenuActivity.this.guide_menu_head.startAnimation(GuideMenuActivity.this.hiddenTopMenuAnimation);
                    GuideMenuActivity.this.guide_menu_head.setVisibility(View.VISIBLE);
                    GuideMenuActivity.this.listView1.startAnimation(GuideMenuActivity.this.hiddenRightMenuAnimation);
                    GuideMenuActivity.this.listView1.setVisibility(View.VISIBLE);
                    GuideMenuActivity.this.img_num.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
                    GuideMenuActivity.this.imgview_left.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
                    GuideMenuActivity.this.imgview_right.startAnimation(GuideMenuActivity.this.showbotMenuAnimation);
                    GuideMenuActivity.this.img_num.setVisibility(View.INVISIBLE);
                    GuideMenuActivity.this.imgview_left.setVisibility(View.INVISIBLE);
                    GuideMenuActivity.this.imgview_right.setVisibility(View.INVISIBLE);
                default:
            }
        }
    }

    public GuideMenuActivity() {
        this.mAreaAdapter = null;
        this.mRightMenuAdapter = null;
        this.mstrMenuitemText = null;
        this.item_img = new int[]{R.drawable.guide_item1, R.drawable.guide_item2, R.drawable.guide_item3, R.drawable.guide_item4, R.drawable.guide_item5, R.drawable.guide_item6, R.drawable.guide_item7, R.drawable.guide_item8, R.drawable.guide_item9, R.drawable.guide_item10, R.drawable.guide_item11, R.drawable.guide_item12, R.drawable.guide_item19, R.drawable.guide_item20, R.drawable.guide_item15, R.drawable.guide_item21, R.drawable.guide_item17, R.drawable.guide_item18};
        this.gallery = null;
        this.guide_menu_head = null;
        this.showTopMenuAnimation = null;
        this.hiddenTopMenuAnimation = null;
        this.showRightMenuAnimation = null;
        this.hiddenRightMenuAnimation = null;
        this.showleftMenuAnimation = null;
        this.hiddenleftMenuAnimation = null;
        this.showbotMenuAnimation = null;
        this.hiddenbotMenuAnimation = null;
        this.DM = null;
        this.bitmap = null;
        this.scene_tv = null;
        this.help = null;
        this.btn_back_adddevice = null;
        this.listView1 = null;
        this.Imageview_gone = null;
        this.img_num = null;
        this.imgview_left = null;
        this.imgview_right = null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_menu);
        SysApplication.getInstance().addActivity(this);
        this.DM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.DM);
        if (DatabaseManager.getInstance().mDBHelper == null) {
            DatabaseManager.getInstance().DatabaseInit(this);
        }
        Gateway curGateway = SysApplication.getInstance().getCurrGateway(this);
        if (curGateway == null || !(curGateway.getGateWayId() == 1 || curGateway.getGateWayId() == 3)) {
            this.item_img[12] = R.drawable.guide_item19;
            this.item_img[13] = R.drawable.guide_item20;
            this.item_img[15] = R.drawable.guide_item21;
        } else {
            this.item_img[12] = R.drawable.guide_item13;
            this.item_img[13] = R.drawable.guide_item14;
            this.item_img[15] = R.drawable.guide_item16;
        }
        this.help = getIntent().getStringExtra("help");
        findViewsById();
        WeightListening();
        LoadAreaView();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.DM = null;
        this.item_img = null;
        this.mstrMenuitemText = null;
        this.mAreaAdapter = null;
        this.mRightMenuAdapter = null;
        if (!(this.bitmap == null || this.bitmap.isRecycled())) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        this.gallery = null;
        System.gc();
    }

    private void findViewsById() {
        this.showTopMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        this.hiddenTopMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_out);
        this.showRightMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
        this.hiddenRightMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.push_right_out);
        this.showleftMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
        this.hiddenleftMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
        this.showbotMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        this.hiddenbotMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
        this.gallery = (CustomGallery) findViewById(R.id.gallery3);
        this.guide_menu_head = (RelativeLayout) findViewById(R.id.guide_menu_head);
        this.scene_tv = (TextView) findViewById(R.id.scene_tv);
        this.btn_back_adddevice = (Button) findViewById(R.id.btn_back_adddevice);
        this.listView1 = (ListView) findViewById(R.id.listView1);
        this.Imageview_gone = (ImageView) findViewById(R.id.Imageview_gone);
        this.img_num = (TextView) findViewById(R.id.textView_num);
        this.imgview_left = (ImageView) findViewById(R.id.img_left);
        this.imgview_right = (ImageView) findViewById(R.id.img_right);
    }

    private void ChangeNum(int iarg) {
        iarg++;
        this.img_num.setVisibility(View.INVISIBLE);
        if (iarg == 1) {
            this.imgview_left.setVisibility(View.VISIBLE);
            this.imgview_right.setVisibility(View.INVISIBLE);
        } else if (iarg == this.mstrMenuitemText.length) {
            this.imgview_left.setVisibility(View.INVISIBLE);
            this.imgview_right.setVisibility(View.VISIBLE);
        } else {
            this.imgview_left.setVisibility(View.INVISIBLE);
            this.imgview_right.setVisibility(View.INVISIBLE);
        }
        this.img_num.setText(new StringBuilder(String.valueOf(iarg)).append("/").append(this.mstrMenuitemText.length).toString());
    }

    private void WeightListening() {
        this.btn_back_adddevice.setOnClickListener(new WeightListening());
        this.Imageview_gone.setOnClickListener(new WeightListening());
    }

    private void LoadAreaView() {
        this.mstrMenuitemText = getResources().getStringArray(R.array.guide_menu);
        this.mRightMenuAdapter = new RightMenuAdapter();
        this.listView1.setAdapter(this.mRightMenuAdapter);
        this.listView1.setOnItemClickListener(new C02571());
        this.mAreaAdapter = new AreaAdapter();
        this.gallery.setAdapter(this.mAreaAdapter);
        this.gallery.setOnItemClickListener(new C02582());
        this.gallery.setOnItemSelectedListener(new C02593());
    }

    private void helpclick() {
        if (this.help == null || !this.help.equals("help")) {
            Area mArea = new Area();
            mArea.setAreaName(getResources().getString(R.string.alllights));
            DatabaseManager.getInstance().addAreaToOne(mArea);
            Scene mScene = new Scene();
            Scene mScene1 = new Scene();
            mScene.setSceneName(getResources().getString(R.string.all_open));
            mScene.setSceneInfoIndex((short) 1);
            mScene1.setSceneName(getResources().getString(R.string.all_close));
            mScene1.setSceneInfoIndex((short) 2);
            DatabaseManager.getInstance().addSceneToOne(mScene);
            DatabaseManager.getInstance().addSceneToTwo(mScene1);
            /*
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("mainactivity", 2);
            intent.putExtra("guide", "guide");
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();
            */
            return;
        }
        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mainactivity", 2);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        finish();
        */
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.guide_menu_head.getVisibility() == View.VISIBLE) {
                helpclick();
            } else {
                this.listView1.startAnimation(this.hiddenRightMenuAnimation);
                this.listView1.setVisibility(View.VISIBLE);
                this.guide_menu_head.startAnimation(this.hiddenTopMenuAnimation);
                this.guide_menu_head.setVisibility(View.VISIBLE);
                this.Imageview_gone.startAnimation(this.hiddenleftMenuAnimation);
                this.Imageview_gone.setVisibility(View.VISIBLE);
                this.img_num.startAnimation(this.showbotMenuAnimation);
                this.imgview_left.startAnimation(this.showbotMenuAnimation);
                this.imgview_right.startAnimation(this.showbotMenuAnimation);
                this.img_num.setVisibility(View.INVISIBLE);
                this.imgview_left.setVisibility(View.INVISIBLE);
                this.imgview_right.setVisibility(View.INVISIBLE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
