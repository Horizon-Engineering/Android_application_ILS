package com.homa.hls.widgetcustom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class ScrollViewCustom extends HorizontalScrollView {
    private int childWidth;
    private int intitPosition;
    private int newCheck;
    private OnScrollStopListner onScrollstopListner;
    private Runnable scrollerTask;

    /* renamed from: com.homa.hls.widgetcustom.ScrollViewCustom.1 */
    class C04091 implements Runnable {
        C04091() {
        }

        public void run() {
            if (ScrollViewCustom.this.intitPosition - ScrollViewCustom.this.getScrollX() != 0) {
                ScrollViewCustom.this.intitPosition = ScrollViewCustom.this.getScrollX();
                ScrollViewCustom.this.postDelayed(ScrollViewCustom.this.scrollerTask, (long) ScrollViewCustom.this.newCheck);
            } else if (ScrollViewCustom.this.onScrollstopListner != null) {
                ScrollViewCustom.this.onScrollstopListner.onScrollStoped();
                Rect outRect = new Rect();
                ScrollViewCustom.this.getDrawingRect(outRect);
                if (ScrollViewCustom.this.getScrollX() == 0) {
                    ScrollViewCustom.this.onScrollstopListner.onScrollToLeftEdge();
                } else if ((ScrollViewCustom.this.childWidth + ScrollViewCustom.this.getPaddingLeft()) + ScrollViewCustom.this.getPaddingRight() == outRect.right) {
                    ScrollViewCustom.this.onScrollstopListner.onScrollToRightEdge();
                } else {
                    ScrollViewCustom.this.onScrollstopListner.onScrollToMiddle();
                }
            }
        }
    }

    public interface OnScrollStopListner {
        void onScrollStoped();

        void onScrollToLeftEdge();

        void onScrollToMiddle();

        void onScrollToRightEdge();
    }

    public ScrollViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.newCheck = 100;
        this.childWidth = 0;
        this.scrollerTask = new C04091();
    }

    public void setOnScrollStopListner(OnScrollStopListner listner) {
        this.onScrollstopListner = listner;
    }

    public void startScrollerTask() {
        this.intitPosition = getScrollX();
        postDelayed(this.scrollerTask, (long) this.newCheck);
        checkTotalWidth();
    }

    private void checkTotalWidth() {
        if (this.childWidth <= 0) {
            for (int i = 0; i < getChildCount(); i++) {
                this.childWidth += getChildAt(i).getWidth();
            }
        }
    }
}
