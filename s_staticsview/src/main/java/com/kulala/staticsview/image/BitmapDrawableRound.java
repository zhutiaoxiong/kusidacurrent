package com.kulala.staticsview.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
/**
 * Created by Administrator on 2017/9/12.
 this.measure(0, 0);
 Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
 */

public class BitmapDrawableRound {
    /**
     * 获取裁剪后的圆形图片
     */
    public static Bitmap getCircleImage(Bitmap bitmap, int sizeMegure) {
        if(bitmap == null)return null;
        //先缩放图片
        Bitmap source = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap scaledSrcBmp = Bitmap.createScaledBitmap(source, sizeMegure,sizeMegure, true);
        Bitmap target = Bitmap.createBitmap(scaledSrcBmp.getWidth(),scaledSrcBmp.getHeight(),Bitmap.Config.ARGB_8888);
        //再切圆图
        Paint  paint  = new Paint();
        Rect  rect  = new Rect(0, 0, scaledSrcBmp.getWidth(),scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        Canvas canvas    = new Canvas(target);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,scaledSrcBmp.getHeight() / 2,scaledSrcBmp.getWidth() / 2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        source = null;
        scaledSrcBmp = null;
        return target;
    }
    //可能是错的方法
    public static Bitmap getRotateImage(Bitmap bitmap, float degree) {
        if(bitmap == null)return null;
        if(degree == 0 || degree == 360)return bitmap;
        //先缩放图片
        Bitmap source = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap target = Bitmap.createBitmap(source.getWidth(),source.getHeight(),Bitmap.Config.ARGB_8888);
        //再切圆图
        Paint  paint  = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        Canvas canvas    = new Canvas(target);
        canvas.drawARGB(0, 0, 0, 0);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree, source.getWidth() / 2, source.getHeight() / 2);
        canvas.drawBitmap(source, matrix, paint);
        return target;
    }
    /**
     * 边缘画圆
     */
    private void drawCircleBorder(Canvas canvas, int radius, int color,int mBorderThickness) {
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的　style　为STROKE：空心 */
        paint.setStyle(Paint.Style.STROKE);
        /* 设置paint的外框宽度 */
        paint.setStrokeWidth(mBorderThickness);
        canvas.drawCircle(radius, radius, radius, paint);
    }
    //=============================================
    public static Bitmap drawable2Bitmap(BitmapDrawable drawable) {
        if(drawable == null)return null;
        return drawable.getBitmap();
    }
    public static Bitmap drawable2BitmapColor(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static Drawable bitmap2Drawable(Bitmap bitmap, Context context) {
        if(bitmap == null)return null;
        Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
        return drawable;
    }
}
