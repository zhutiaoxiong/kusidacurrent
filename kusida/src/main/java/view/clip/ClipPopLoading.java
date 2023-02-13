package view.clip;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;

import common.GlobalContext;

public class ClipPopLoading{
    private PopupWindow popContain;//弹出管理
    private View        parentView;//本对象显示
    private Context     context;

    private RelativeLayout thisView;
    private ImageView      img_loading;
    private Animation      anmiRotate;

    private MyHandler handler = new MyHandler();
    private boolean isShowing = false;
    // ========================out======================
    private static ClipPopLoading _instance;

    public static ClipPopLoading getInstance() {
        if (_instance == null)
            _instance = new ClipPopLoading();
        return _instance;
    }

    public boolean getIsShowing(){
        if(popContain == null)return false;
        return popContain.isShowing();
    }
    //===================================================
    public void show(View parentView) {
        if(isShowing)return;
        this.parentView = parentView;
        context = GlobalContext.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_loading, null);
        img_loading = (ImageView) thisView.findViewById(R.id.img_loading);
        anmiRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
        LinearInterpolator lir = new LinearInterpolator();
        anmiRotate.setInterpolator(lir);//必设不然无法均速
        img_loading.startAnimation(anmiRotate);
        initViews();
        initEvents();
    }

    public static boolean canCancelAnimation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public void initViews() {
        handleShow();
    }

    public void initEvents() {
//		view_background.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				callback.callback(mark+"_cancel", null);
//				popContain.dismiss();
//			}
//		});
    }

    //need parentThread
    public void stopLoading(){
        handleStop();
//        if (popContain != null && img_loading != null) {
//            if (canCancelAnimation()) {
//                img_loading.clearAnimation();
//                Log.i("loading", "loading6");
//                img_loading.animate().cancel();
//            }
//        }
//        popContain.dismiss();

    }
    private void handleStop() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16596;
        handler.sendMessage(message);
    }
    private void handleShow() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16595;
        handler.sendMessage(message);
    }

    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16595:
                    if(thisView == null || parentView == null || !parentView.isAttachedToWindow())return;
                    popContain = new PopupWindow(thisView);
                    popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    popContain.setFocusable(false);
                    popContain.setTouchable(false);
                    popContain.setOutsideTouchable(false);
                    popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                    isShowing = true;
                    break;
                case 16596:
                    if (popContain != null && img_loading != null) {
                        if (canCancelAnimation()) {
                            parentView = null;
                            context = null;
                            thisView = null;
                            img_loading.clearAnimation();
                            Log.i("loading", "loading6");
                            img_loading.animate().cancel();
                        }
                        popContain.dismiss();
                    }
                    isShowing = false;
                    break;
            }
        }
    }
}

