package com.example.hesolutions.horizon;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allin.activity.action.SysApplication;
import com.homa.hls.database.Area;
import com.homa.hls.database.DatabaseManager;
import com.homa.hls.database.Scene;
import java.util.ArrayList;

public class GuideActivity extends Activity {
    private DisplayMetrics DM;
    private int currentIndex;
    String help;
    private ImageView[] imageViews;
    private ViewGroup main;
    Dialog mdialog;
    private ArrayList<View> pageViews;
    private LinearLayout pointLLayout;
    private ViewPager viewPager;

    /* renamed from: com.allin.activity.GuideActivity.1 */
    class C02531 implements OnClickListener {
        private final /* synthetic */ LinearLayout val$llayout1;
        private final /* synthetic */ TextView val$tv;

        C02531(TextView textView, LinearLayout linearLayout) {
            this.val$tv = textView;
            this.val$llayout1 = linearLayout;
        }

        public void onClick(View v) {
            this.val$tv.setTextColor(getResources().getColor(R.color.black));
            this.val$llayout1.setBackgroundResource(R.drawable.tips_bgr_click);
            Intent intent = new Intent(GuideActivity.this, GuideMenuActivity.class);
            if (GuideActivity.this.help != null && GuideActivity.this.help.equals("help")) {
                intent.putExtra("help", "help");
            }
            GuideActivity.this.startActivity(intent);
            GuideActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            GuideActivity.this.finish();
        }
    }

    /* renamed from: com.allin.activity.GuideActivity.2 */
    class C02542 implements OnClickListener {
        C02542() {
        }

        public void onClick(View v) {
            if (GuideActivity.this.help == null || !GuideActivity.this.help.equals("help")) {

                Intent intent = new Intent(GuideActivity.this, HomePage.class);
                intent.putExtra("mainactivity", 2);
                intent.putExtra("guide", "guide");
                GuideActivity.this.startActivity(intent);
                GuideActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                GuideActivity.this.finish();

                return;
            }

            Intent intent = new Intent(GuideActivity.this, HomePage.class);
            intent.putExtra("mainactivity", 2);
            GuideActivity.this.startActivity(intent);
            GuideActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            GuideActivity.this.finish();

        }
    }

    /* renamed from: com.allin.activity.GuideActivity.3 */
    class C02553 implements OnClickListener {
        C02553() {
        }

        public void onClick(View v) {
            if (GuideActivity.this.mdialog != null && GuideActivity.this.mdialog.isShowing()) {
                GuideActivity.this.mdialog.cancel();
                GuideActivity.this.mdialog = null;
            }
        }
    }

    /* renamed from: com.allin.activity.GuideActivity.4 */
    class C02564 implements OnClickListener {
        C02564() {
        }

        public void onClick(View v) {
            if (GuideActivity.this.mdialog != null && GuideActivity.this.mdialog.isShowing()) {
                GuideActivity.this.mdialog.cancel();
                GuideActivity.this.mdialog = null;
            }
            SysApplication.getInstance().LoopThenExit();
        }
    }

    class GuidePageAdapter extends PagerAdapter {
        GuidePageAdapter() {
        }

        public int getCount() {
            return GuideActivity.this.pageViews.size();
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) GuideActivity.this.pageViews.get(arg1));
        }

        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView((View) GuideActivity.this.pageViews.get(arg1));
            return GuideActivity.this.pageViews.get(arg1);
        }

        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View arg0) {
        }

        public void finishUpdate(View arg0) {
        }
    }

    class GuidePageChangeListener implements OnPageChangeListener {
        GuidePageChangeListener() {
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            if (arg0 >= 0 && arg0 <= GuideActivity.this.imageViews.length - 1 && GuideActivity.this.currentIndex != arg0) {
                GuideActivity.this.imageViews[GuideActivity.this.currentIndex].setEnabled(true);
                GuideActivity.this.imageViews[arg0].setEnabled(false);
                GuideActivity.this.currentIndex = arg0;
            }
        }
    }

    public GuideActivity() {
        this.help = null;
        this.mdialog = null;
        this.DM = null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        if (DatabaseManager.getInstance().mDBHelper == null) {
            DatabaseManager.getInstance().DatabaseInit(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        this.main = (ViewGroup) inflater.inflate(R.layout.activity_guide, null);
        this.viewPager = (ViewPager) this.main.findViewById(R.id.guidePages);
        View itemview = inflater.inflate(R.layout.guide_item06, null);
        Button btn_oncetry = (Button) itemview.findViewById(R.id.startBtn);
        TextView tv = (TextView) this.main.findViewById(R.id.tv_guide);
        LinearLayout llayout1 = (LinearLayout) this.main.findViewById(R.id.llayout1);
        this.pointLLayout = (LinearLayout) this.main.findViewById(R.id.llayout);
        this.DM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.DM);
        this.help = getIntent().getStringExtra("help");
        if (this.help == null || !this.help.equals("help")) {
            btn_oncetry.setText(getString(R.string.oncetry));
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
        } else {
            btn_oncetry.setText(getString(R.string.exit_guide));
        }
        llayout1.setOnClickListener(new C02531(tv, llayout1));
        btn_oncetry.setOnClickListener(new C02542());
        this.pageViews = new ArrayList();
        this.pageViews.add(inflater.inflate(R.layout.guide_item01, null));
        this.pageViews.add(inflater.inflate(R.layout.guide_item02, null));
        this.pageViews.add(inflater.inflate(R.layout.guide_item03, null));
        this.pageViews.add(inflater.inflate(R.layout.guide_item04, null));
        this.pageViews.add(inflater.inflate(R.layout.guide_item05, null));
        this.pageViews.add(itemview);
        this.imageViews = new ImageView[this.pageViews.size()];
        for (int i = 0; i < this.pageViews.size(); i++) {
            this.imageViews[i] = (ImageView) this.pointLLayout.getChildAt(i);
            this.imageViews[i].setEnabled(true);
            this.imageViews[i].setTag(Integer.valueOf(i));
        }
        this.currentIndex = 0;
        this.imageViews[this.currentIndex].setEnabled(false);
        setContentView(this.main);
        this.viewPager.setAdapter(new GuidePageAdapter());
        this.viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mdialog = null;
        this.imageViews = null;
        this.pageViews.clear();
        this.pageViews = null;
        this.DM = null;
        System.gc();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.help = getIntent().getStringExtra("help");
            if (this.help == null || !this.help.equals("help")) {
                View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.msg_dialog, null);
                Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
                Button btn_no = (Button) view.findViewById(R.id.btn_cancel);
                ((TextView) view.findViewById(R.id.textinfor)).setText(getResources().getString(R.string.finishask));
                this.mdialog = new Dialog(this, R.style.Theme_dialog);
                this.mdialog.setContentView(view);
                this.mdialog.setCancelable(true);
                this.mdialog.setCanceledOnTouchOutside(false);
                this.mdialog.show();
                btn_no.setOnClickListener(new C02553());
                btn_ok.setOnClickListener(new C02564());
            } else {
                Intent intent = new Intent(this, HomePage.class);
                intent.putExtra("mainactivity", 2);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();

            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
