package com.kulala.staticsview.image;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.kulala.staticsfunc.time.CountDownTimerMy;
/**
 * animation.getAnimatedFraction()获得当前进度占整个动画过程的比例，浮点型，0-1之间
 * valueAnimator.getAnimatedValue()//获得当前动画的进度值  ，整型，1-100之间
 * 在有多个转圈动画时，开一个必要停掉另一个，不然卡死( onPause中clearAnim)
 */
public class RoundImageViewRotate extends ImageView  implements ValueAnimator.AnimatorUpdateListener{
    private int    mWidth;
    private Bitmap mBitmap, resBitmap;
    //anim
    private ObjectAnimator anim;
    private boolean isPause,isPaused;
    //==============================================
    public RoundImageViewRotate(Context context) {
        super(context);
    }
    public RoundImageViewRotate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RoundImageViewRotate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (resBitmap != null && mWidth>0) {
            mBitmap = BitmapDrawableRound.getCircleImage(resBitmap, mWidth);
            resBitmap = null;
            if (mBitmap != null) super.setImageBitmap(mBitmap);
        } else if (mBitmap != null && mWidth>0) {
            mBitmap = BitmapDrawableRound.getCircleImage(mBitmap, mWidth);
            if (mBitmap != null) super.setImageBitmap(mBitmap);
        }
    }
    //==============================================
    @Override
    public void setImageBitmap(Bitmap bitmap) {
        if (mWidth > 0) {
            mBitmap = BitmapDrawableRound.getCircleImage(bitmap, mWidth);
            if (mBitmap != null) super.setImageBitmap(mBitmap);
        } else {
            resBitmap = bitmap;
        }
    }

    @Override
    public void setImageResource(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        if (mWidth > 0) {
            mBitmap = BitmapDrawableRound.getCircleImage(bitmap, mWidth);
            if (mBitmap != null) super.setImageBitmap(mBitmap);
        } else {
            resBitmap = bitmap;
        }
    }
    //==============================================
    @Override
    protected void onDetachedFromWindow() {
        clearAnim();
        mBitmap = null;
        resBitmap = null;
        super.onDetachedFromWindow();
    }
    public void startRotate() {
        isPause = false;
        initAnim();
        if (anim != null && !anim.isRunning()) anim.start();
    }

    public void stopRotate() {
        isPause = true;
        isPaused = false;
    }
    public void clearAnim(){
        if (anim != null){
            anim.end();
            anim.cancel();
            anim = null;
        }
        this.clearAnimation();
    }
    //==============================================
    private void initAnim() {
        if (anim != null)return;
        LinearInterpolator lin = new LinearInterpolator();//声明为线性变化
        anim = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);//设置动画为旋转动画，角度是0-360
        anim.setDuration(60000);//时间15秒，这个可以自己酌情修改,三圈的总时间
        anim.setInterpolator(lin);
        anim.setRepeatMode(ValueAnimator.RESTART);//设置重复模式为重新开始
        anim.setRepeatCount(ObjectAnimator.INFINITE);//重复次数为-1，就是无限循环
        anim.addUpdateListener(this);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            Drawable drawable = getDrawable();
            if (getDrawable() != null) {
                resBitmap = BitmapDrawableRound.drawable2Bitmap((BitmapDrawable) getDrawable());
                this.measure(0, 0);
                return;
            }
        } else {//已经生成过图片
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    private long mCurrentPlayTime;
    private float fraction;
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if(isPause){
            if(!isPaused){
                mCurrentPlayTime = animation.getCurrentPlayTime();
                fraction = animation.getAnimatedFraction();
                animation.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        return fraction;
                    }
                });
                isPaused =  true;
            }else{
                animation.setInterpolator(null);
                if(anim!=null)anim.setInterpolator(null);
            }
            //每隔动画播放的时间，我们都会将播放时间往回调整，以便重新播放的时候接着使用这个时间,同时也为了让整个动画不结束
            new CountDownTimerMy(ValueAnimator.getFrameDelay(), ValueAnimator.getFrameDelay()){

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    if(anim!=null)anim.setCurrentPlayTime(mCurrentPlayTime);
                }
            }.start();
        }else{
            animation.setInterpolator(null);
            if(anim!=null)anim.setInterpolator(null);
        }
    }
}