package com.kulala.staticsfunc.static_view_change;

import android.content.Context;

public class ODipToPx {
	public static int dipToPx(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int PxToRealPx(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int)(var1 / var2);
    }

    public static float getDpScale(Context var0) {
        return var0.getResources().getDisplayMetrics().density;
    }
}
