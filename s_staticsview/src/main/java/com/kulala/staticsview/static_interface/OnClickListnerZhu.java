package com.kulala.staticsview.static_interface;

import android.view.View;

public class OnClickListnerZhu implements View.OnClickListener {
    private static long beforeTime = 0;
    @Override
    public void onClick(View v) {
        long time = System.currentTimeMillis();
        if(Math.abs(time-beforeTime)<1500L){
//			OToastOMG.getInstance().showText("请不要频繁点击！");
            return;
        }
        beforeTime = time;
        onClickNoFast(v);
    }

    public void onClickNoFast(View v){

    }
}
