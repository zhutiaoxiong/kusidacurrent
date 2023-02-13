package com.kulala.staticsfunc.static_assistant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;

/**
 * Created by Administrator on 2017/4/22.
 */

public class ButtonBgStyle {
    public static StateListDrawable createDrawableSelector(Context context, Bitmap normal, Bitmap pressed, Bitmap selected) {
        StateListDrawable stateList = new StateListDrawable();
        stateList.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(pressed));
        stateList.addState(new int[]{android.R.attr.state_selected}, new BitmapDrawable(selected));
        stateList.addState(new int[]{android.R.attr.state_enabled}, new BitmapDrawable(normal));
        return stateList;
    }
    public static StateListDrawable createDrawableSelector(Context context, Drawable normal, Drawable pressed, Drawable selected) {
        if(normal == null || pressed == null || selected == null)return null;
        StateListDrawable stateList = new StateListDrawable();
        stateList.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateList.addState(new int[]{android.R.attr.state_selected}, selected);
        stateList.addState(new int[]{android.R.attr.state_enabled}, normal);
        return stateList;
    }
    public static StateListDrawable createDrawableSelector(Context context, Uri normal, Uri pressed, Uri selected) {
        StateListDrawable stateList = new StateListDrawable();
        Drawable pressD = Drawable.createFromPath(pressed.getPath());
        if(pressD == null)return null;
        stateList.addState(new int[]{android.R.attr.state_pressed}, pressD);
        Drawable selectD = Drawable.createFromPath(selected.getPath());
        if(selectD == null)return null;
        stateList.addState(new int[]{android.R.attr.state_selected}, selectD);
        Drawable normalD = Drawable.createFromPath(normal.getPath());
        if(normalD == null)return null;
        stateList.addState(new int[]{android.R.attr.state_enabled}, normalD);//要放最后
        return stateList;
    }
}
