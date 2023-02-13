package com.kulala.staticsview.autoscalinglayout;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ASLinearLayout extends LinearLayout {
    private ASViewGroupUtil mASViewGroupUtil;

    public ASLinearLayout(Context context, int designWidth, int designHeight) {
        super(context);
        this.mASViewGroupUtil = new ASViewGroupUtil();
        this.mASViewGroupUtil.init(designWidth, designHeight);
    }

    public ASLinearLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.init(attributes);
    }

    @TargetApi(11)
    public ASLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    @TargetApi(21)
    public ASLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        if(null == this.mASViewGroupUtil) {
            this.mASViewGroupUtil = new ASViewGroupUtil();
            this.mASViewGroupUtil.init(this, attrs);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measureSpecs = this.mASViewGroupUtil.onMeasure(this, widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measureSpecs[0], measureSpecs[1]);
    }

    public void draw(Canvas canvas) {
        if(this.isInEditMode()) {
            super.draw(canvas);
        } else {
            if(!this.mASViewGroupUtil.scaleSize(this)) {
                super.draw(canvas);
            } else {
                this.invalidate();
            }

        }
    }
}
