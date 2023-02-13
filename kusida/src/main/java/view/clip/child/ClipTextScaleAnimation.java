package view.clip.child;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.client.proj.kusida.R;

public class ClipTextScaleAnimation {
    private static long preShowTime = 0;
    //===================================================
    public static void show(final TextView text, final Activity activity) {
        long now = System.currentTimeMillis();
        if(now - preShowTime < 2000L)return;//多次显示，还没转完圈
        text.setVisibility(View.VISIBLE);
        Animation anmiScale = AnimationUtils.loadAnimation(text.getContext(), R.anim.scaletxt_animation);
//        LinearInterpolator lir = new LinearInterpolator();
//        anmiScale.setInterpolator(lir);//必设不然无法均速
        text.startAnimation(anmiScale);
        anmiScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        text.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

}

