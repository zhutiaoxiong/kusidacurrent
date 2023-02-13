package com.kulala.staticsview.autoscalinglayout;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Method;
public class ScalingUtil {
    public ScalingUtil() {
    }

    public static void scaleViewAndChildren(View view, float factor, int level) {
        try {
            Method layoutParams = view.getClass().getMethod("isAutoScaleEnable", new Class[0]);
            if(!((Boolean)layoutParams.invoke(view, new Object[0])).booleanValue()) {
                return;
            }

            if(level > 0) {
                return;
            }
        } catch (Exception var6) {
            ;
        }

        ViewGroup.LayoutParams var7 = view.getLayoutParams();
        if(var7.width > 0) {
            var7.width = (int)((float)var7.width * factor);
        }

        if(var7.height > 0) {
            var7.height = (int)((float)var7.height * factor);
        }

        if(var7 instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams vg = (ViewGroup.MarginLayoutParams)var7;
            vg.leftMargin = (int)((float)vg.leftMargin * factor);
            vg.topMargin = (int)((float)vg.topMargin * factor);
            vg.rightMargin = (int)((float)vg.rightMargin * factor);
            vg.bottomMargin = (int)((float)vg.bottomMargin * factor);
        }

        view.setLayoutParams(var7);
        if(!(view instanceof EditText)) {
            view.setPadding((int)((float)view.getPaddingLeft() * factor), (int)((float)view.getPaddingTop() * factor), (int)((float)view.getPaddingRight() * factor), (int)((float)view.getPaddingBottom() * factor));
        }

        if(view instanceof TextView) {
            scaleTextSize((TextView)view, factor, var7);
        }

        if(view instanceof ViewGroup) {
            ViewGroup var8 = (ViewGroup)view;

            for(int i = 0; i < var8.getChildCount(); ++i) {
                scaleViewAndChildren(var8.getChildAt(i), factor, level + 1);
            }
        }

    }

    public static void scaleTextSize(TextView tv, float factor, ViewGroup.LayoutParams layoutParams) {
        tv.setTextSize(0, tv.getTextSize() * factor);
    }
}
