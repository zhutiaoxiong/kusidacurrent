package com.kulala.staticsview.autoscalinglayout;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ASFrameLayout extends FrameLayout {
    private ASViewGroupUtil mASViewGroupUtil;

    public ASFrameLayout(Context context, int designWidth, int designHeight) {
        super(context);
        this.mASViewGroupUtil = new ASViewGroupUtil();
        this.mASViewGroupUtil.init(designWidth, designHeight);
    }
    //初始化必须加背景，不然不能缩放
    /**designWidth:pxW designHeight:pxH*/
    public ASFrameLayout(Context context, int designWidth, int designHeight,int mScaleType) {
        super(context);
        this.mASViewGroupUtil = new ASViewGroupUtil();
        this.mASViewGroupUtil.init(designWidth, designHeight,mScaleType);
    }

    public ASFrameLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.init(attributes);
    }

    public ASFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    @TargetApi(21)
    public ASFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(attrs);
    }

    public float getCurrentScale(){
        return mASViewGroupUtil.getCurrentScale();
    }
    private void init(AttributeSet attrs) {
        if(null == this.mASViewGroupUtil) {
            this.mASViewGroupUtil = new ASViewGroupUtil();
            this.mASViewGroupUtil.init(this, attrs);
        }

    }

    public boolean isAutoScaleEnable() {
        return this.mASViewGroupUtil.isAutoScaleEnable();
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
