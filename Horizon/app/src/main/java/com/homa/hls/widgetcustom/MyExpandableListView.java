package com.homa.hls.widgetcustom;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CursorAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hesolutions.horizon.R;

public class MyExpandableListView extends ExpandableListView implements OnScrollListener, OnGroupClickListener {
    private static final int MAX_ALPHA = 255;
    private QQHeaderAdapter mAdapter;
    private float mDownX;
    private float mDownY;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private boolean mHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mOldState;

    public interface QQHeaderAdapter {
        public static final int PINNED_HEADER_GONE = 0;
        public static final int PINNED_HEADER_PUSHED_UP = 2;
        public static final int PINNED_HEADER_VISIBLE = 1;

        void configureQQHeader(View view, int i, int i2, int i3);

        int getGroupClickStatus(int i);

        int getQQHeaderState(int i, int i2);

        void setGroupClickStatus(int i, int i2);
    }

    public MyExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOldState = -1;
        registerListener();
    }

    public MyExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mOldState = -1;
        registerListener();
    }

    public MyExpandableListView(Context context) {
        super(context);
        this.mOldState = -1;
        registerListener();
    }

    public void setHeaderView(View view) {
        this.mHeaderView = view;
        view.setLayoutParams(new LayoutParams(-1, -2));
        if (this.mHeaderView != null) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    private void registerListener() {
        setOnScrollListener(this);
        setOnGroupClickListener(this);
    }

    private void headerViewClick() {
        int groupPosition = ExpandableListView.getPackedPositionGroup(getExpandableListPosition(getFirstVisiblePosition()));
        if (this.mAdapter.getGroupClickStatus(groupPosition) == 1) {
            collapseGroup(groupPosition);
            this.mAdapter.setGroupClickStatus(groupPosition, 0);
        } else {
            expandGroup(groupPosition);
            this.mAdapter.setGroupClickStatus(groupPosition, 1);
        }
        setSelectedGroup(groupPosition);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mHeaderViewVisible) {
            switch (ev.getAction()) {
                case DialogFragment.STYLE_NORMAL /*0*/:
                    this.mDownX = ev.getX();
                    this.mDownY = ev.getY();
                    if (this.mDownX <= ((float) this.mHeaderViewWidth) && this.mDownY <= ((float) this.mHeaderViewHeight)) {
                        return true;
                    }
                case CursorAdapter.FLAG_AUTO_REQUERY /*1*/:
                    float x = ev.getX();
                    float y = ev.getY();
                    float offsetX = Math.abs(x - this.mDownX);
                    float offsetY = Math.abs(y - this.mDownY);
                    if (x <= ((float) this.mHeaderViewWidth) && y <= ((float) this.mHeaderViewHeight) && offsetX <= ((float) this.mHeaderViewWidth) && offsetY <= ((float) this.mHeaderViewHeight)) {
                        if (this.mHeaderView == null) {
                            return true;
                        }
                        headerViewClick();
                        return true;
                    }
            }
        }
        return super.onTouchEvent(ev);
    }

    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        this.mAdapter = (QQHeaderAdapter) adapter;
    }

    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (this.mAdapter.getGroupClickStatus(groupPosition) == 0) {
            this.mAdapter.setGroupClickStatus(groupPosition, 1);
            parent.expandGroup(groupPosition);
            parent.setSelectedGroup(groupPosition);
            ((ImageView) v.findViewById(R.id.openIcon)).setImageResource(R.drawable.group_open);
            String strtext = ((TextView) v.findViewById(R.id.groupto)).getText().toString();
            if (strtext.equals("\u5168\u5f69\u706f") || strtext.equals("Full color") || strtext.equals("Alle Farben")) {
                ((ImageView) v.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_a);
            } else if (strtext.equals("\u667a\u80fd\u63d2\u5ea7") || strtext.equals("Smart socket") || strtext.equals("Intelligente Steckdose")) {
                ((ImageView) v.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_b);
            } else if (strtext.equals("\u5355\u8272\u706f") || strtext.equals("Single color") || strtext.equals("Einfarbig")) {
                ((ImageView) v.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_c);
            } else if (strtext.equals("\u8272\u6e29\u706f") || strtext.equals("Color temp") || strtext.equals("Farbtemperatur")) {
                ((ImageView) v.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_d);
            } else if (strtext.equals("\u5149\u4f20\u611f\u5668") || strtext.equals("Photot sensor") || strtext.equals("Lichtsensor")) {
                ((ImageView) v.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_e);
            } else if (strtext.equals("\u7a97\u5e18\u63a7\u5236\u5668") || strtext.equals("Curtain controller") || strtext.equals("Jalousien Steuerung")) {
                ((ImageView) v.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_f);
            } else if (strtext.equals("\u4eba\u4f53\u611f\u5e94") || strtext.equals("Motion sensor") || strtext.equals("Bewegungssensor")) {
                ((ImageView) v.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_g);
            } else if (strtext.equals("\u6e29\u5ea6\u4f20\u611f\u5668") || strtext.equals("Temperature sensor")) {
                ((ImageView) v.findViewById(R.id.groupIcon)).setImageResource(R.drawable.header_h);
            }
        } else if (this.mAdapter.getGroupClickStatus(groupPosition) == 1) {
            ((ImageView) v.findViewById(R.id.openIcon)).setImageResource(R.drawable.group_close);
            this.mAdapter.setGroupClickStatus(groupPosition, 0);
            parent.collapseGroup(groupPosition);
        }
        return true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mHeaderView != null) {
            measureChild(this.mHeaderView, widthMeasureSpec, heightMeasureSpec);
            this.mHeaderViewWidth = this.mHeaderView.getMeasuredWidth();
            this.mHeaderViewHeight = this.mHeaderView.getMeasuredHeight();
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
        int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
        int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
        int state = this.mAdapter.getQQHeaderState(groupPos, childPos);
        if (!(this.mHeaderView == null || this.mAdapter == null || state == this.mOldState)) {
            this.mOldState = state;
            this.mHeaderView.layout(0, 0, this.mHeaderViewWidth, this.mHeaderViewHeight);
        }
        configureHeaderView(groupPos, childPos);
    }

    public void configureHeaderView(int groupPosition, int childPosition) {
        if (this.mHeaderView != null && this.mAdapter != null && ((ExpandableListAdapter) this.mAdapter).getGroupCount() != 0) {
            switch (this.mAdapter.getQQHeaderState(groupPosition, childPosition)) {
                case DialogFragment.STYLE_NORMAL /*0*/:
                    this.mHeaderViewVisible = false;
                case CursorAdapter.FLAG_AUTO_REQUERY /*1*/:
                    this.mAdapter.configureQQHeader(this.mHeaderView, groupPosition, childPosition, MAX_ALPHA);
                    if (this.mHeaderView.getTop() != 0) {
                        this.mHeaderView.layout(0, 0, this.mHeaderViewWidth, this.mHeaderViewHeight);
                    }
                    this.mHeaderViewVisible = true;
                case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER /*2*/:
                    int y;
                    int alpha;
                    int bottom = getChildAt(0).getBottom();
                    int headerHeight = this.mHeaderView.getHeight();
                    if (bottom < headerHeight) {
                        y = bottom - headerHeight;
                        alpha = ((headerHeight + y) * MAX_ALPHA) / headerHeight;
                    } else {
                        y = 0;
                        alpha = MAX_ALPHA;
                    }
                    this.mAdapter.configureQQHeader(this.mHeaderView, groupPosition, childPosition, alpha);
                    if (this.mHeaderView.getTop() != y) {
                        this.mHeaderView.layout(0, y, this.mHeaderViewWidth, this.mHeaderViewHeight + y);
                    }
                    this.mHeaderViewVisible = true;
                default:
            }
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mHeaderViewVisible) {
            drawChild(canvas, this.mHeaderView, getDrawingTime());
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        long flatPos = getExpandableListPosition(firstVisibleItem);
        configureHeaderView(ExpandableListView.getPackedPositionGroup(flatPos), ExpandableListView.getPackedPositionChild(flatPos));
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
