package com.homa.hls.widgetcustom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.example.hesolutions.horizon.R;

public class Panel extends LinearLayout {
    private static final int ANIMATION_FRAME_DURATION = 16;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    private static final float MAXIMUM_ACCELERATION = 2000.0f;
    private static final float MAXIMUM_MAJOR_VELOCITY = 200.0f;
    private static final int MSG_ANIMATE = 1000;
    private static final int MSG_PREPARE_ANIMATE = 2000;
    public static final int RIGHT = 3;
    private static final String TAG = "Panel";
    public static final int TOP = 0;
    private float curEventTime;
    private float curRawX;
    private float curRawY;
    private float lastEventTime;
    private float lastRawX;
    private float lastRawY;
    private float mAnimatedAcceleration;
    private boolean mAnimating;
    private long mAnimationLastTime;
    private boolean mBringToFront;
    private Drawable mClosedHandle;
    private View mContent;
    private int mContentHeight;
    private int mContentId;
    private int mContentWidth;
    private long mCurrentAnimationTime;
    private int mDuration;
    private GestureDetector mGestureDetector;
    private PanelOnGestureListener mGestureListener;
    private View mHandle;
    private int mHandleId;
    private final Handler mHandler;
    private Interpolator mInterpolator;
    private boolean mIsShrinking;
    private boolean mLinearFlying;
    private final int mMaximumAcceleration;
    private final int mMaximumMajorVelocity;
    private Drawable mOpenedHandle;
    private int mOrientation;
    private int mPosition;
    private State mState;
    private float mTrackX;
    private float mTrackY;
    private float mVelocity;
    private float mWeight;
    private OnPanelListener panelListener;
    OnTouchListener touchListener;

    /* renamed from: com.homa.hls.widgetcustom.Panel.1 */
    class C04081 implements OnTouchListener {
        C04081() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (Panel.this.mAnimating) {
                return true;
            }
            int action = event.getAction();
            if (action == 0 && Panel.this.mBringToFront) {
                Panel.this.bringToFront();
            }
            if (!Panel.this.mGestureDetector.onTouchEvent(event) && action == Panel.BOTTOM) {
                long now = SystemClock.uptimeMillis();
                Panel.this.mAnimationLastTime = now;
                Panel.this.mCurrentAnimationTime = 16 + now;
                Panel.this.mAnimating = true;
                Panel.this.mHandler.removeMessages(Panel.MSG_ANIMATE);
                Panel.this.mHandler.removeMessages(Panel.MSG_PREPARE_ANIMATE);
                Panel.this.mHandler.sendMessageAtTime(Panel.this.mHandler.obtainMessage(Panel.MSG_PREPARE_ANIMATE), Panel.this.mCurrentAnimationTime);
            }
            return false;
        }
    }

    public interface OnPanelListener {
        void onPanelClosed(Panel panel);

        void onPanelOpened(Panel panel);
    }

    class PanelOnGestureListener implements OnGestureListener {
        float scrollX;
        float scrollY;

        PanelOnGestureListener() {
        }

        public boolean onDown(MotionEvent e) {
            this.scrollY = 0.0f;
            this.scrollX = 0.0f;
            Panel panel = Panel.this;
            Panel panel2 = Panel.this;
            Panel panel3 = Panel.this;
            Panel.this.curRawY = -1.0f;
            panel3.lastRawY = -1.0f;
            panel2.curRawX = -1.0f;
            panel.lastRawX = -1.0f;
            panel = Panel.this;
            Panel.this.curEventTime = -1.0f;
            panel.lastEventTime = -1.0f;
            Panel.this.initChange();
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float velocityX2;
            float velocityY2;
            Panel.this.mState = State.FLYING;
            if (Panel.this.lastRawX == -1.0f && Panel.this.lastRawY == -1.0f) {
                velocityX2 = ((Panel.this.curRawX - e1.getRawX()) / (Panel.this.curEventTime - ((float) e1.getEventTime()))) * 1000.0f;
                velocityY2 = ((Panel.this.curRawY - e1.getRawY()) / (Panel.this.curEventTime - ((float) e1.getEventTime()))) * 1000.0f;
            } else {
                velocityX2 = ((Panel.this.curRawX - Panel.this.lastRawX) / (Panel.this.curEventTime - Panel.this.lastEventTime)) * 1000.0f;
                velocityY2 = ((Panel.this.curRawY - Panel.this.lastRawY) / (Panel.this.curEventTime - Panel.this.lastEventTime)) * 1000.0f;
            }
            Panel panel = Panel.this;
            if (Panel.this.mOrientation != Panel.BOTTOM) {
                velocityY2 = velocityX2;
            }
            panel.mVelocity = velocityY2;
            if (Math.abs(Panel.this.mVelocity) <= 50.0f) {
                return false;
            }
            if (Panel.this.mVelocity > 0.0f) {
                Panel.this.mAnimatedAcceleration = (float) Panel.this.mMaximumAcceleration;
            } else {
                Panel.this.mAnimatedAcceleration = (float) (-Panel.this.mMaximumAcceleration);
            }
            long now = SystemClock.uptimeMillis();
            Panel.this.mAnimationLastTime = now;
            Panel.this.mCurrentAnimationTime = 16 + now;
            Panel.this.mAnimating = true;
            Panel.this.mHandler.removeMessages(Panel.MSG_ANIMATE);
            Panel.this.mHandler.removeMessages(Panel.MSG_PREPARE_ANIMATE);
            Panel.this.mHandler.sendMessageAtTime(Panel.this.mHandler.obtainMessage(Panel.MSG_ANIMATE), Panel.this.mCurrentAnimationTime);
            return true;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Panel.this.mState = State.TRACKING;
            float tmpY = 0.0f;
            float tmpX = 0.0f;
            if (Panel.this.mOrientation == Panel.BOTTOM) {
                this.scrollY -= distanceY;
                tmpY = Panel.this.mPosition == 0 ? Panel.this.ensureRange(this.scrollY, -Panel.this.mContentHeight, 0) : Panel.this.ensureRange(this.scrollY, 0, Panel.this.mContentHeight);
            } else {
                this.scrollX -= distanceX;
                tmpX = Panel.this.mPosition == Panel.LEFT ? Panel.this.ensureRange(this.scrollX, -Panel.this.mContentWidth, 0) : Panel.this.ensureRange(this.scrollX, 0, Panel.this.mContentWidth);
            }
            if (!(tmpX == Panel.this.mTrackX && tmpY == Panel.this.mTrackY)) {
                Panel.this.mTrackX = tmpX;
                Panel.this.mTrackY = tmpY;
            }
            Panel.this.invalidate();
            Panel.this.lastRawX = Panel.this.curRawX;
            Panel.this.lastRawY = Panel.this.curRawY;
            Panel.this.lastEventTime = Panel.this.curEventTime;
            Panel.this.curRawX = e2.getRawX();
            Panel.this.curRawY = e2.getRawY();
            Panel.this.curEventTime = (float) e2.getEventTime();
            return true;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }

    private class SlidingHandler extends Handler {
        private SlidingHandler() {
        }

        public void handleMessage(Message m) {
            switch (m.what) {
                case Panel.MSG_ANIMATE /*1000*/:
                    Panel.this.doAnimation();
                case Panel.MSG_PREPARE_ANIMATE /*2000*/:
                    Panel.this.prepareAnimation();
                    Panel.this.doAnimation();
                default:
            }
        }
    }

    private enum State {
        ABOUT_TO_ANIMATE,
        ANIMATING,
        READY,
        TRACKING,
        FLYING,
        CLICK
    }

    public Panel(Context context, AttributeSet attrs) {

        super(context, attrs);
        int i = BOTTOM;
        this.mHandler = new SlidingHandler();
        this.touchListener = new C04081();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppCompatTextView);
        this.mDuration = a.getInteger(0, 750);
        this.mPosition = a.getInteger(BOTTOM, BOTTOM);
        this.mLinearFlying = a.getBoolean(4, false);
        this.mWeight = a.getFraction(5, 0, BOTTOM, 0.0f);
        if (this.mWeight < 0.0f || this.mWeight > 1.0f) {
            this.mWeight = 0.0f;
            Log.w(TAG, a.getPositionDescription() + ": weight must be > 0 and <= 1");
        }
        this.mOpenedHandle = a.getDrawable(6);
        this.mClosedHandle = a.getDrawable(7);
        RuntimeException runtimeException = null;
        this.mHandleId = a.getResourceId(LEFT, 0);
        if (this.mHandleId == 0) {
            runtimeException = new IllegalArgumentException(a.getPositionDescription() + ": The handle attribute is required and must refer to a valid child.");
        }
        this.mContentId = a.getResourceId(RIGHT, 0);
        if (this.mContentId == 0) {
            runtimeException = new IllegalArgumentException(a.getPositionDescription() + ": The content attribute is required and must refer to a valid child.");
        }
        a.recycle();
        float density = getResources().getDisplayMetrics().density;
        this.mMaximumMajorVelocity = (int) ((MAXIMUM_MAJOR_VELOCITY * density) + 0.5f);
        this.mMaximumAcceleration = (int) ((MAXIMUM_ACCELERATION * density) + 0.5f);
        if (runtimeException != null) {
            throw runtimeException;
        }
        if (!(this.mPosition == 0 || this.mPosition == BOTTOM)) {
            i = 0;
        }
        this.mOrientation = i;
        setOrientation(HORIZONTAL);
        this.mState = State.READY;
        this.mGestureListener = new PanelOnGestureListener();
        this.mGestureDetector = new GestureDetector(this.mGestureListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
        setBaselineAligned(false);
    }

    public void setOnPanelListener(OnPanelListener onPanelListener) {
        this.panelListener = onPanelListener;
    }

    public View getHandle() {
        return this.mHandle;
    }

    public View getContent() {
        return this.mContent;
    }

    public void setInterpolator(Interpolator i) {
        this.mInterpolator = i;
    }

    public boolean setOpen(boolean open, boolean animate) {
        int i = 0;
        if (this.mState != State.READY || (isOpen() ^ open) == false) {
            return false;
        }
        this.mIsShrinking = !open;
        if (animate) {
            this.mState = State.ABOUT_TO_ANIMATE;
            if (!this.mIsShrinking) {
                this.mContent.setVisibility(INVISIBLE);
            }
            long now = SystemClock.uptimeMillis();
            this.mAnimationLastTime = now;
            this.mCurrentAnimationTime = 16 + now;
            this.mAnimating = true;
            this.mHandler.removeMessages(MSG_ANIMATE);
            this.mHandler.removeMessages(MSG_PREPARE_ANIMATE);
            this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(MSG_PREPARE_ANIMATE), this.mCurrentAnimationTime);
            return true;
        }
        View view = this.mContent;
        if (!open) {
            i = 8;
        }
        view.setVisibility(VISIBLE);
        postProcess();
        return true;
    }

    public boolean isOpen() {
        return this.mContent.getVisibility() == INVISIBLE;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHandle = findViewById(this.mHandleId);
        if (this.mHandle == null) {
            throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id." + getResources().getResourceEntryName(this.mHandleId) + "'");
        }
        this.mHandle.setClickable(true);
        this.mHandle.setOnTouchListener(this.touchListener);
        this.mContent = findViewById(this.mContentId);
        if (this.mContent == null) {
            throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id." + getResources().getResourceEntryName(this.mHandleId) + "'");
        }
        removeView(this.mHandle);
        removeView(this.mContent);
        if (this.mPosition == 0 || this.mPosition == LEFT) {
            addView(this.mContent);
            addView(this.mHandle);
        } else {
            addView(this.mHandle);
            addView(this.mContent);
        }
        if (this.mClosedHandle != null) {
            this.mHandle.setBackgroundDrawable(this.mClosedHandle);
        }
        this.mContent.setClickable(true);
        this.mContent.setVisibility(VISIBLE);
       /* if (this.mWeight > 0.0f) {
          LayoutParams params = this.mContent.getLayoutParams();
            if (this.mOrientation == BOTTOM) {
                params.height = -1;
            } else {
                params.width = -1;
            }
            this.mContent.setLayoutParams(params);
        }*/
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent != null && (parent instanceof FrameLayout)) {
            this.mBringToFront = true;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mWeight > 0.0f && this.mContent.getVisibility() == INVISIBLE) {
            View parent = (View) getParent();
            if (parent != null) {
                if (this.mOrientation == BOTTOM) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (((float) parent.getHeight()) * this.mWeight), MeasureSpec.AT_MOST);
                } else {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (((float) parent.getWidth()) * this.mWeight), MeasureSpec.AT_MOST);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mContentWidth = this.mContent.getWidth();
        this.mContentHeight = this.mContent.getHeight();
    }

    protected void dispatchDraw(Canvas canvas) {
        if (this.mState == State.ABOUT_TO_ANIMATE && !this.mIsShrinking) {
            int delta;
            if (this.mOrientation == BOTTOM) {
                delta = this.mContentHeight;
            } else {
                delta = this.mContentWidth;
            }
            if (this.mPosition == LEFT || this.mPosition == 0) {
                delta = -delta;
            }
            if (this.mOrientation == BOTTOM) {
                canvas.translate(0.0f, (float) delta);
            } else {
                canvas.translate((float) delta, 0.0f);
            }
        }
        if (this.mState == State.TRACKING || this.mState == State.FLYING || this.mState == State.CLICK) {
            canvas.translate(this.mTrackX, this.mTrackY);
        }
        super.dispatchDraw(canvas);
    }

    private float ensureRange(float v, int min, int max) {
        return Math.min(Math.max(v, (float) min), (float) max);
    }

    public boolean initChange() {
        if (this.mState != State.READY) {
            return false;
        }
        this.mState = State.ABOUT_TO_ANIMATE;
        this.mIsShrinking = this.mContent.getVisibility() == INVISIBLE;
        if (!this.mIsShrinking) {
            this.mContent.setVisibility(INVISIBLE);
        }
        return true;
    }

    private void postProcess() {
        if (this.mIsShrinking && this.mClosedHandle != null) {
            this.mHandle.setBackgroundDrawable(this.mClosedHandle);
        } else if (!(this.mIsShrinking || this.mOpenedHandle == null)) {
            this.mHandle.setBackgroundDrawable(this.mOpenedHandle);
        }
        if (this.panelListener == null) {
            return;
        }
        if (this.mIsShrinking) {
            this.panelListener.onPanelClosed(this);
        } else {
            this.panelListener.onPanelOpened(this);
        }
    }

    private void prepareAnimation() {
        boolean z = false;
        switch (this.mPosition) {
            case DialogFragment.STYLE_NORMAL /*0*/:
                if (!this.mIsShrinking) {
                    this.mVelocity = (float) this.mMaximumMajorVelocity;
                    this.mAnimatedAcceleration = (float) this.mMaximumAcceleration;
                    if (this.mTrackX == 0.0f && this.mState == State.ABOUT_TO_ANIMATE) {
                        this.mTrackY = (float) (-this.mContentHeight);
                        break;
                    }
                }
                this.mVelocity = (float) (-this.mMaximumMajorVelocity);
                this.mAnimatedAcceleration = (float) (-this.mMaximumAcceleration);
                break;
            case BOTTOM /*1*/:
                if (!this.mIsShrinking) {
                    this.mVelocity = (float) (-this.mMaximumMajorVelocity);
                    this.mAnimatedAcceleration = (float) (-this.mMaximumAcceleration);
                    if (this.mTrackX == 0.0f && this.mState == State.ABOUT_TO_ANIMATE) {
                        this.mTrackY = (float) this.mContentHeight;
                        break;
                    }
                }
                this.mVelocity = (float) this.mMaximumMajorVelocity;
                this.mAnimatedAcceleration = (float) this.mMaximumAcceleration;
                break;
            case LEFT /*2*/:
                if (!this.mIsShrinking) {
                    this.mVelocity = (float) this.mMaximumMajorVelocity;
                    this.mAnimatedAcceleration = (float) this.mMaximumAcceleration;
                    if (this.mTrackX == 0.0f && this.mState == State.ABOUT_TO_ANIMATE) {
                        this.mTrackX = (float) (-this.mContentWidth);
                        break;
                    }
                }
                this.mVelocity = (float) (-this.mMaximumMajorVelocity);
                this.mAnimatedAcceleration = (float) (-this.mMaximumAcceleration);
                break;
            case RIGHT /*3*/:
                if (!this.mIsShrinking) {
                    this.mVelocity = (float) (-this.mMaximumMajorVelocity);
                    this.mAnimatedAcceleration = (float) (-this.mMaximumAcceleration);
                    if (this.mTrackX == 0.0f && this.mState == State.ABOUT_TO_ANIMATE) {
                        this.mTrackX = (float) this.mContentWidth;
                        break;
                    }
                }
                this.mVelocity = (float) this.mMaximumMajorVelocity;
                this.mAnimatedAcceleration = (float) this.mMaximumAcceleration;
                break;
        }
        if (this.mState == State.TRACKING) {
            if (this.mIsShrinking) {
                if ((this.mOrientation == BOTTOM && Math.abs(this.mTrackY) < ((float) (this.mContentHeight / LEFT))) || (this.mOrientation == 0 && Math.abs(this.mTrackX) < ((float) (this.mContentWidth / LEFT)))) {
                    this.mVelocity = -this.mVelocity;
                    this.mAnimatedAcceleration = -this.mAnimatedAcceleration;
                    if (!this.mIsShrinking) {
                        z = true;
                    }
                    this.mIsShrinking = z;
                }
            } else if ((this.mOrientation == BOTTOM && Math.abs(this.mTrackY) > ((float) (this.mContentHeight / LEFT))) || (this.mOrientation == 0 && Math.abs(this.mTrackX) > ((float) (this.mContentWidth / LEFT)))) {
                this.mVelocity = -this.mVelocity;
                this.mAnimatedAcceleration = -this.mAnimatedAcceleration;
                if (!this.mIsShrinking) {
                    z = true;
                }
                this.mIsShrinking = z;
            }
        }
        if (this.mState != State.FLYING && this.mState != State.TRACKING) {
            this.mState = State.CLICK;
        }
    }

    private void doAnimation() {
        if (this.mAnimating) {
            long now = SystemClock.uptimeMillis();
            float t = ((float) (now - this.mAnimationLastTime)) / 1000.0f;
            float v = this.mVelocity;
            float a = this.mAnimatedAcceleration;
            this.mVelocity = (a * t) + v;
            this.mAnimationLastTime = now;
            switch (this.mPosition) {
                case DialogFragment.STYLE_NORMAL /*0*/:
                    this.mTrackY = (this.mTrackY + (v * t)) + (((0.5f * a) * t) * t);
                    if (this.mTrackY <= 0.0f) {
                        if (this.mTrackY < ((float) (-this.mContentHeight))) {
                            this.mTrackY = (float) (-this.mContentHeight);
                            this.mContent.setVisibility(VISIBLE);
                            this.mState = State.READY;
                            this.mAnimating = false;
                            break;
                        }
                    }
                    this.mTrackY = 0.0f;
                    this.mState = State.READY;
                    this.mAnimating = false;
                    break;

                case BOTTOM /*1*/:
                    this.mTrackY = (this.mTrackY + (v * t)) + (((0.5f * a) * t) * t);
                    if (this.mTrackY >= 0.0f) {
                        if (this.mTrackY > ((float) this.mContentHeight)) {
                            this.mTrackY = (float) this.mContentHeight;
                            this.mContent.setVisibility(VISIBLE);
                            this.mState = State.READY;
                            this.mAnimating = false;
                            break;
                        }
                    }
                    this.mTrackY = 0.0f;
                    this.mState = State.READY;
                    this.mAnimating = false;
                    break;

                case LEFT /*2*/:
                    this.mTrackX = (this.mTrackX + (v * t)) + (((0.5f * a) * t) * t);
                    if (this.mTrackX <= 0.0f) {
                        if (this.mTrackX < ((float) (-this.mContentWidth))) {
                            this.mTrackX = (float) (-this.mContentWidth);
                            this.mContent.setVisibility(VISIBLE);
                            this.mState = State.READY;
                            this.mAnimating = false;
                            break;
                        }
                    }
                    this.mTrackX = 0.0f;
                    this.mState = State.READY;
                    this.mAnimating = false;
                    break;

                case RIGHT /*3*/:
                    this.mTrackX = (this.mTrackX + (v * t)) + (((0.5f * a) * t) * t);
                    if (this.mTrackX >= 0.0f) {
                        if (this.mTrackX > ((float) this.mContentWidth)) {
                            this.mTrackX = (float) this.mContentWidth;
                            this.mContent.setVisibility(VISIBLE);
                            this.mState = State.READY;
                            this.mAnimating = false;
                            break;
                        }
                    }
                    this.mTrackX = 0.0f;
                    this.mState = State.READY;
                    this.mAnimating = false;
                    break;

            }
            invalidate();
            if (this.mAnimating) {
                this.mCurrentAnimationTime += 16;
                this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(MSG_ANIMATE), this.mCurrentAnimationTime);
                return;
            }
            postProcess();
        }
    }
}
