package com.kulala.staticsview.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
/**
 * Created by Administrator on 2017/8/29.
 */

public class CircleImgCut extends ImageView {
    private Bitmap mBitmap,resBitmap;
    private int mWidth,mHeight;
    public CircleImgCut(Context context) {
        super(context);
    }

    public CircleImgCut(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImgCut(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if(resBitmap!=null){
            mBitmap = BitmapDrawableRound.getCircleImage(resBitmap,mWidth);
            resBitmap = null;
            if(mBitmap!=null)super.setImageBitmap(mBitmap);
        }else if(mBitmap!=null){
            mBitmap = BitmapDrawableRound.getCircleImage(mBitmap,mWidth);
            if(mBitmap!=null)super.setImageBitmap(mBitmap);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mBitmap = null;
        resBitmap = null;
        super.onDetachedFromWindow();
    }
    @Override
    public void setImageBitmap(final Bitmap bitmap) {
        boolean needLoop = false;
        if (Looper.myLooper() != Looper.getMainLooper()) needLoop = true;
        if (needLoop) Looper.prepare();

        if(mWidth>0){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mBitmap = BitmapDrawableRound.getCircleImage(bitmap,mWidth);
                    if(mBitmap!=null)CircleImgCut.super.setImageBitmap(mBitmap);
                }
            });
        }else{
            resBitmap = bitmap;
        }

        if (needLoop) Looper.loop();
    }


    @Override
    public void setImageResource(int resId) {
        boolean needLoop = false;
        if (Looper.myLooper() != Looper.getMainLooper()) needLoop = true;
        if (needLoop) Looper.prepare();

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),resId);
        if(mWidth>0){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mBitmap = BitmapDrawableRound.getCircleImage(bitmap,mWidth);
                    if(mBitmap!=null)CircleImgCut.super.setImageBitmap(mBitmap);
                }
            });
        }else{
            resBitmap = bitmap;
        }
        if (needLoop) Looper.loop();
    }
}
