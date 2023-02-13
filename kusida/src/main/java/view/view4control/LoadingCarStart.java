package view.view4control;

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
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.time.CountDownTimerMy;

public class LoadingCarStart {
    private        PopupWindow     popContain;//弹出管理
    private        View            parentView;//本对象显示
    private        RelativeLayout  thisView;
    private        ImageView       img_loading;
    private        TextView        txt_loading;
    private        Animation       anmiRotate;
    private        MyHandler       handler;
    // ========================out======================
    private static LoadingCarStart _instance;

    public static LoadingCarStart getInstance() {
        if (_instance == null)
            _instance = new LoadingCarStart();
        return _instance;
    }

    //===================================================
    public void show(View parentView1) {
         if (BuildConfig.DEBUG) Log.e("modelStep","LoadingCarStart show:"+modelStep);
        if (handler == null) handler = new MyHandler();
        modelStep = 0;//开始
        openStepModel();
        this.parentView = parentView1;
        thisView = (RelativeLayout) LayoutInflater.from(parentView.getContext()).inflate(R.layout.loading_carstart, null);
        img_loading = (ImageView) thisView.findViewById(R.id.img_loading);
        txt_loading = (TextView) thisView.findViewById(R.id.txt_loading);

        anmiRotate = AnimationUtils.loadAnimation(parentView.getContext(), R.anim.animation_loading);
        LinearInterpolator lir = new LinearInterpolator();
        anmiRotate.setInterpolator(lir);//必设不然无法均速
        initViews();
    }
    private int modelStep = -1;//默认值，停止model显示
    private CountDownTimerMy timerModel;
    public void gotoStepReadySend(){
        modelStep = 1;
    }
    public void gotoStepSendOK(){
        if(modelStep>2)return;
        modelStep = 2;
    }
    public void gotoStepModelOK(){
        if(modelStep>4)return;
        modelStep = 4;
    }
    public void gotoStepRunOK(){
        if(modelStep>5)return;
        modelStep = 5;
    }
    public void gotoStop(){
        modelStep = 8;
    }
    //1.发送通讯请求
    //2.通讯成功
    //3.请求连接车辆
    //4.连接成功
    //5.启动指令下发成功
    //6.引擎启动中
    //7.状态返回中...
    /**0开始 8结束 -1主动结束*/
    private void openStepModel(){
         if (BuildConfig.DEBUG) Log.e("modelStep","openStepModel:"+modelStep);
        if(timerModel == null)timerModel = new CountDownTimerMy(15000,500) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch (modelStep){
                    case 1:
                         if (BuildConfig.DEBUG) Log.e("modelStep","发送通讯请求:"+modelStep);
                        handleChangeText("发送通讯请求");
                        break;
                    case 2:
                         if (BuildConfig.DEBUG) Log.e("modelStep","通讯成功:"+modelStep);
                        handleChangeText("通讯成功");
                        modelStep ++;
                        break;
                    case 3:
                         if (BuildConfig.DEBUG) Log.e("modelStep","请求连接车辆:"+modelStep);
                        handleChangeText("请求连接车辆");
                        break;
                    case 4:
                         if (BuildConfig.DEBUG) Log.e("modelStep","连接成功:"+modelStep);
                        handleChangeText("连接成功");
                        break;
                    case 5:
                         if (BuildConfig.DEBUG) Log.e("modelStep","启动指令下发成功:"+modelStep);
                        handleChangeText("启动指令下发成功");
                        modelStep ++;
                        break;
                    case 6:
                         if (BuildConfig.DEBUG) Log.e("modelStep","引擎启动中:"+modelStep);
                        handleChangeText("引擎启动中");
                        modelStep ++;
                        break;
                    case 7:
                         if (BuildConfig.DEBUG) Log.e("modelStep","状态返回中...:"+modelStep);
                        handleChangeText("状态返回中...");
                        modelStep ++;
                        break;
                    case 8://结束
                         if (BuildConfig.DEBUG) Log.e("modelStep","结束:"+modelStep);
                        if(timerModel!=null)timerModel.cancel();
                        modelStep = -1;
                        handleStopLoading();
                        break;
                }
            }
            @Override
            public void onFinish() {
                 if (BuildConfig.DEBUG) Log.e("modelStep","onFinish:"+modelStep);
                handleStopLoading();
            }
        };
        timerModel.start();
    }
    private void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        img_loading.startAnimation(anmiRotate);
    }
    // ===================================================
    //主动关闭
    private void handleStopLoading() {
         if (BuildConfig.DEBUG) Log.e("modelStep","handleStopLoading:"+modelStep);
        if (handler == null) return;
        Message message = new Message();
        message.what = 11003;
        handler.sendMessage(message);
    }
    //修改文字显示
    private void handleChangeText(String changeText) {
        if (handler == null) return;
        Message message = new Message();
        message.what = 11004;
        message.obj = changeText;
        handler.sendMessage(message);
    }
    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11003:
                    if (canCancelAnimation() && img_loading!=null) {
                        img_loading.clearAnimation();
                        img_loading.animate().cancel();
                    }
                    if (popContain == null) return;
                    popContain.dismiss();
                    parentView = null;
                    thisView = null;
                    break;
                case 11004:
                    String changeTxt = (String) msg.obj;
                    if (changeTxt == null) return;
                    if(txt_loading == null)return;
                    txt_loading.setText(changeTxt);
                    break;
            }
        }
    }
    public static boolean canCancelAnimation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }
    // ===================================================
}



