package com.homa.hls.widgetcustom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class CustomGallery extends Gallery {
    public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGallery(Context context) {
        super(context);
    }

    private boolean isScrollingLeft(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2) {
        return paramMotionEvent2.getX() > paramMotionEvent1.getX();
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
        int keyCode;
        if (isScrollingLeft(paramMotionEvent1, paramMotionEvent2)) {
            keyCode = 21;
        } else {
            keyCode = 22;
        }
        onKeyDown(keyCode, null);
        return true;
    }
}
