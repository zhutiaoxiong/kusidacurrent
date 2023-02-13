package com.kulala.staticsview.autoscalinglayout;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.kulala.staticsview.R.styleable;

public class ASViewGroupUtil {
    private static final int TYPE_FIT_INSIDE = 0;
    private static final int TYPE_FIT_WIDTH = 1;
    private static final int TYPE_FIT_HEIGHT = 2;
    private int mDesignWidth;
    private int mDesignHeight;
    private float mCurrentWidth;
    private float mCurrentHeight;
    private boolean mAutoScaleEnable;
    private int mScaleType;

    public ASViewGroupUtil() {
    }
    public float getCurrentScale(){
        if(mDesignHeight == 0 || mCurrentHeight==0)return 1;
        return mCurrentHeight/mDesignHeight;
    }

    public void init(int designWidth, int designHeight) {
        this.mDesignWidth = designWidth;
        this.mDesignHeight = designHeight;
        this.mCurrentWidth = (float)this.mDesignWidth;
        this.mCurrentHeight = (float)this.mDesignHeight;
        this.mAutoScaleEnable = true;
        this.mScaleType = 0;
    }
    public void init(int designWidth, int designHeight,int mScaleType) {
        this.mDesignWidth = designWidth;
        this.mDesignHeight = designHeight;
        this.mCurrentWidth = (float)this.mDesignWidth;
        this.mCurrentHeight = (float)this.mDesignHeight;
        this.mAutoScaleEnable = true;
        this.mScaleType = mScaleType;
    }

    public void init(ViewGroup vg, AttributeSet attrs) {
        this.mScaleType = 0;
        String     scaleTypeStr = null;
        TypedArray a            = null;

        try {
            a = vg.getContext().obtainStyledAttributes(attrs, styleable.AutoScalingLayout);
            this.mDesignWidth = a.getDimensionPixelOffset(styleable.AutoScalingLayout_designWidth, 0);
            this.mDesignHeight = a.getDimensionPixelOffset(styleable.AutoScalingLayout_designHeight, 0);
            this.mAutoScaleEnable = a.getBoolean(styleable.AutoScalingLayout_autoScaleEnable, true);
            scaleTypeStr = a.getString(styleable.AutoScalingLayout_autoScaleType);
        } catch (Throwable var11) {
            this.mAutoScaleEnable = true;
            this.mDesignWidth = 0;
            this.mDesignHeight = 0;

            for(int i = 0; i < attrs.getAttributeCount(); ++i) {
                String autoScaleEnableStr;
                if("designWidth".equals(attrs.getAttributeName(i))) {
                    autoScaleEnableStr = attrs.getAttributeValue(i);
                    this.mDesignWidth = this.getDimensionPixelOffset(vg.getContext(), autoScaleEnableStr);
                } else if("designHeight".equals(attrs.getAttributeName(i))) {
                    autoScaleEnableStr = attrs.getAttributeValue(i);
                    this.mDesignHeight = this.getDimensionPixelOffset(vg.getContext(), autoScaleEnableStr);
                } else if("autoScaleEnable".equals(attrs.getAttributeName(i))) {
                    autoScaleEnableStr = attrs.getAttributeValue(i);
                    if(autoScaleEnableStr.equals("false")) {
                        this.mAutoScaleEnable = false;
                    }
                } else if("autoScaleType".equals(attrs.getAttributeName(i))) {
                    scaleTypeStr = attrs.getAttributeValue(i);
                }
            }
        } finally {
            if(null != a) {
                a.recycle();
            }

        }

        if(null != scaleTypeStr) {
            if(scaleTypeStr.equals("fitWidth")) {
                this.mScaleType = 1;
            } else if(scaleTypeStr.equals("fitHeight")) {
                this.mScaleType = 2;
            }
        }

        this.mCurrentWidth = (float)this.mDesignWidth;
        this.mCurrentHeight = (float)this.mDesignHeight;
        if(null == vg.getBackground()) {
            vg.setBackgroundColor(0);
        }

    }

    public boolean isAutoScaleEnable() {
        return this.mAutoScaleEnable;
    }

    public int[] onMeasure(ViewGroup vg, int widthMeasureSpec, int heightMeasureSpec) {
        int[] measureSpecs = new int[]{widthMeasureSpec, heightMeasureSpec};
        if(!this.mAutoScaleEnable) {
            return measureSpecs;
        } else if(0 != this.mDesignWidth && 0 != this.mDesignHeight) {
            if(0 != this.mScaleType) {
                return measureSpecs;
            } else {
                int                    widthMode  = View.MeasureSpec.getMode(widthMeasureSpec);
                int                    heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
                int                    width      = View.MeasureSpec.getSize(widthMeasureSpec);
                int                    height     = View.MeasureSpec.getSize(heightMeasureSpec);
                ViewGroup.LayoutParams params     = vg.getLayoutParams();
                //mode_shift=30,mode_mask=-1073741824,UNSPECIFIED=0,EXACTLY=1073741824,AT_MOST=-2147483648
                if(widthMode != 1073741824 && heightMode == 1073741824 && -2 == params.width) {
                    width = height * this.mDesignWidth / this.mDesignHeight;
                    measureSpecs[0] = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
                } else if(widthMode == 1073741824 && heightMode != 1073741824 && -2 == params.height) {
                    height = width * this.mDesignHeight / this.mDesignWidth;
                    measureSpecs[1] = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
                }

                return measureSpecs;
            }
        } else {
            return measureSpecs;
        }
    }

    public boolean scaleSize(ViewGroup vg) {
        if(!this.mAutoScaleEnable) {
            return false;
        } else if(0 == this.mDesignWidth && 2 != this.mScaleType) {
            return false;
        } else if(0 == this.mDesignHeight && 1 != this.mScaleType) {
            return false;
        } else {
            int width = vg.getWidth();
            int height = vg.getHeight();
            if(0 != width && 0 != height) {
                if((float)width == this.mCurrentWidth && (float)height == this.mCurrentHeight) {
                    return false;
                } else {
                    float scale;
                    if(2 == this.mScaleType) {
                        scale = (float)height / this.mCurrentHeight;
                    } else if(1 == this.mScaleType) {
                        scale = (float)width / this.mCurrentWidth;
                    } else {
                        float wScale = (float)width / this.mCurrentWidth;
                        float hScale = (float)height / this.mCurrentHeight;
                        scale = Math.min(wScale, hScale);
                    }

                    if((double)scale < 1.02D && (double)scale > 0.98D) {
                        return false;
                    } else {
                        this.mCurrentWidth = (float)width;
                        this.mCurrentHeight = (float)height;
                        ScalingUtil.scaleViewAndChildren(vg, scale, 0);
                        return true;
                    }
                }
            } else {
                return false;
            }
        }
    }

    private int getDimensionPixelOffset(Context context, String value) {
        float v;
        if(value.endsWith("px")) {
            v = Float.parseFloat(value.substring(0, value.length() - 2));
            return (int)v;
        } else {
            float density;
            if(value.endsWith("dp")) {
                v = Float.parseFloat(value.substring(0, value.length() - 2));
                density = context.getResources().getDisplayMetrics().density;
                return (int)(v * density + 0.5F);
            } else if(value.endsWith("dip")) {
                v = Float.parseFloat(value.substring(0, value.length() - 3));
                density = context.getResources().getDisplayMetrics().density;
                return (int)(v * density + 0.5F);
            } else {
                return 0;
            }
        }
    }
}

