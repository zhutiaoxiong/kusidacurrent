package com.kulala.staticsview.animset;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class OAnimSet {
    private static long mDuration = 700L;
    public static final int FadeIn       = 0;//浮现

    public static final int SlideBottom  = 9;//浮现
    public static final int SlideLeft    = 10;//浮现
    public static final int SlideRight   = 11;//浮现
    public static final int SlideTop     = 12;//浮现

    public static final int Fall         = 1;//大变小
    public static final int SideFall     = 8;//斜转大变小

    public static final int D3FlipH        = 2;//3D水平
    public static final int D3FlipV        = 3;//3D直
    public static final int D3RotateBottom = 5;//3d下转
    public static final int D3RotateLeft   = 6;//3D左转
    public static final int D3Slit         = 13;//3D翻转
    public static final int NewsPaper    = 4;//新闻转动
    public static final int Shake        = 7;//振窗
    public static void startAnimation(View view,int type){
        AnimatorSet set = new AnimatorSet();
        switch (type){
            case FadeIn:
                set.playTogether(ObjectAnimator.ofFloat(view,"alpha",0,1).setDuration(mDuration));
                break;
            case SideFall:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "scaleX", 2, 1.5f, 1).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view,"scaleY",2,1.5f,1).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "rotation", 25,0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "translationX",80,0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2));
                break;
            case SlideBottom:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "translationY", 300, 0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2));
                break;
            case SlideLeft:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "translationX", -300, 0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2));
                break;
            case SlideRight:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "translationX",300,0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2));
                break;
            case SlideTop:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "translationY", -300, 0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2));
                break;
            case Fall:
                set.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 2, 1.5f, 1).setDuration(mDuration),
                    ObjectAnimator.ofFloat(view,"scaleY",2,1.5f,1).setDuration(mDuration),
                    ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2));
                break;
            case NewsPaper:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "rotation", 1080,720,360,0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2),
                        ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.5f, 1).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view,"scaleY",0.1f,0.5f,1).setDuration(mDuration));
                break;
            case Shake:
                set.playTogether(ObjectAnimator.ofFloat(view, "translationX", 0, .10f, -25, .26f, 25,.42f, -25, .58f, 25,.74f,-25,.90f,1,0).setDuration(mDuration));
                break;
            case D3FlipH:
                set.playTogether(ObjectAnimator.ofFloat(view, "rotationY", -90, 0).setDuration(mDuration));
                break;
            case D3FlipV:
                set.playTogether(ObjectAnimator.ofFloat(view, "rotationX", -90, 0).setDuration(mDuration));
                break;
            case D3RotateBottom:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "rotationX",90, 0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "translationY", 300, 0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2));
                break;
            case D3RotateLeft:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "rotationY", 90, 0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "translationX", -300, 0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2));
                break;
            case D3Slit:
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "rotationY", 90,88,88,45,0).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view, "alpha", 0,0.4f,0.8f, 1).setDuration(mDuration*3/2),
                        ObjectAnimator.ofFloat(view, "scaleX", 0,0.5f, 0.9f, 0.9f, 1).setDuration(mDuration),
                        ObjectAnimator.ofFloat(view,"scaleY",0,0.5f, 0.9f, 0.9f, 1).setDuration(mDuration));
                break;
        }
        set.start();
    }
}
