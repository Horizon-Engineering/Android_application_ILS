package com.homa.hls.widgets.interpolator;

import android.view.animation.Interpolator;
import com.homa.hls.widgets.interpolator.EasingType.Type;

public class BackInterpolator implements Interpolator {
    private float overshot;
    private Type type;

    public BackInterpolator(Type type, float overshot) {
        this.type = type;
        this.overshot = overshot;
    }

    public float getInterpolation(float t) {
        if (this.type == Type.IN) {
            return in(t, this.overshot);
        }
        if (this.type == Type.OUT) {
            return out(t, this.overshot);
        }
        if (this.type == Type.INOUT) {
            return inout(t, this.overshot);
        }
        return 0.0f;
    }

    private float in(float t, float o) {
        if (o == 0.0f) {
            o = 1.70158f;
        }
        return (t * t) * (((1.0f + o) * t) - o);
    }

    private float out(float t, float o) {
        if (o == 0.0f) {
            o = 1.70158f;
        }
        t -= 1.0f;
        return ((t * t) * (((o + 1.0f) * t) + o)) + 1.0f;
    }

    private float inout(float t, float o) {
        if (o == 0.0f) {
            o = 1.70158f;
        }
        t *= 2.0f;
        if (t < 1.0f) {
            o = (float) (((double) o) * 1.525d);
            return ((t * t) * (((o + 1.0f) * t) - o)) * 0.5f;
        }
        t -= 2.0f;
        o = (float) (((double) o) * 1.525d);
        return (((t * t) * (((o + 1.0f) * t) + o)) + 2.0f) * 0.5f;
    }
}
