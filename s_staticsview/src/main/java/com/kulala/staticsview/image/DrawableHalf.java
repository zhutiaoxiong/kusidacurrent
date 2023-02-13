package com.kulala.staticsview.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
/**
 * Created by Administrator on 2018/1/26.
 */

public class DrawableHalf {
    private Bitmap getCoverBitmap(Bitmap bitmap, int percent) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);//画全透明背景

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        int top    = bitmap.getHeight() * (100 - percent) / 100;
        int bottom = bitmap.getHeight() * percent / 100;
        canvas.drawRect(0, top, bitmap.getWidth(), bitmap.getHeight(), paint);//先画个框
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//交查模式

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
