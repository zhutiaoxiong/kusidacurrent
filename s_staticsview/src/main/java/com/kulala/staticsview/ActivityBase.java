package com.kulala.staticsview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.toast.ToastLoading;
public abstract class ActivityBase extends AppCompatActivity implements OEventObject, OCallBack {
    public static final String          KEEP_SCREENON = "KEEP_SCREENON";
    private             MyHandlerlerler handler       = new MyHandlerlerler();

    private        ToastLoading loading;
    private static Toast        toast;//for cancel;

    private boolean isActive = false;//是激活的Activity
    @Override
    protected void onPause() {
        isActive = false;
        super.onPause();
//        //关闭输入法键盘，如果需要
//        if (getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
    }
    @Override
    protected void onResume() {
        isActive = true;
        super.onResume();
        SharedPreferences settings = getSharedPreferences("kusida_client_database", Activity.MODE_PRIVATE);
        if (settings != null) {
            boolean keepScreenOn = settings.getBoolean(KEEP_SCREENON, true);
            if (keepScreenOn) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持常亮
            }else{
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持常亮
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ODispatcher.addEventListener(OEventName.GLOBAL_POP_TOAST, this);
        ODispatcher.addEventListener(OEventName.GLOBAL_POP_LOADING_SHOW, this);
        ODispatcher.addEventListener(OEventName.GLOBAL_POP_LOADING_HIDE, this);
        ODispatcher.addEventListener(OEventName.ACTIVITY_FINISH_ALL_KEEP_VIEWS, this);
        //这个方法用来消毁其它不要的页面
//        if(isHttpSucess)ODispatcher.dispatchEvent(OEventName.ACTIVITY_FINISH_ALL_KEEP_VIEWS,new String[]{ActivityMain.class.getName()});
    }
    //加载完成
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
    @CallSuper
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.ACTIVITY_FINISH_ALL_KEEP_VIEWS)) {
            String[] classNames = (String[]) paramObj;
            if (classNames == null || classNames.length == 0) return;
            boolean isKeep = false;
            for(String className : classNames){
                if (getClass().getName().equals(className)) isKeep = true;
            }
            if(!isKeep)finish();
        }
        if (eventName.equals(OEventName.GLOBAL_POP_TOAST)) {
            String showStr = (String) paramObj;
            handleShowToast(showStr);
        } else if (eventName.equals(OEventName.GLOBAL_POP_LOADING_SHOW)) {
            handleShowLoading();
        } else if (eventName.equals(OEventName.GLOBAL_POP_LOADING_HIDE)) {
            handleHideLoading();
        }
    }
    protected abstract void initViews();

    protected abstract void initEvents();

    protected abstract void invalidateUI();

    protected abstract void popView(int resId);

    @Override
    public void callback(String key, Object value) {
    }

    @Override
    protected void onDestroy() {
        ODispatcher.recycle(this);
        super.onDestroy();
    }

    public void handleChangeData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 311;
                handler.sendMessage(message);
            }
        }).start();
    }

    public void handlePopView(final int resId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 312;
                message.arg1 = resId;
                handler.sendMessage(message);
            }
        }).start();
    }
    private void handleShowToast(final String info) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 313;
                message.obj = info;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void handleShowLoading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 316;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void handleHideLoading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 317;
                handler.sendMessage(message);
            }
        }).start();
    }
    // ===================================================
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;

    @SuppressLint("HandlerLeak")
    class MyHandlerlerler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 311:
                    invalidateUI();
                    break;
                case 312:
                    popView(msg.arg1);
                    break;
                case 313:
                    String showStr = (String) msg.obj;
                    if (toast == null) {
                        toast = Toast.makeText(ActivityBase.this.getApplicationContext(), showStr, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, ODipToPx.dipToPx(ActivityBase.this, 175));
                        toast.show();
                        oneTime = System.currentTimeMillis();
                    } else {
                        twoTime = System.currentTimeMillis();
                        if (showStr.equals(oldMsg)) {
                            if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                                toast.show();
                            }
                        } else {
                            oldMsg = showStr;
                            toast.setText(showStr);
                            toast.show();
                        }
                    }
                    oneTime = twoTime;
                    break;
                case 316:
                    if (loading == null) loading = new ToastLoading(ActivityBase.this, null);
                    loading.handleStartLoading();
                    break;
                case 317:
                    if (loading == null) return;
                    loading.handleStopLoading();
                    break;
            }
        }
    }

}
