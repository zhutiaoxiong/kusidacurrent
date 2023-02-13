package com.kulala.linkscarpods.views;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/5/15.
 */
public class OnePxActivity extends Activity {
    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到广播
//             if (BuildConfig.DEBUG) Log.e("OnePxActivity","收到FinishOnePPActivity广播");
            OnePxActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         if (BuildConfig.DEBUG) Log.e("OnePxActivity","onCreate");

        Window window = getWindow();
        // 设置窗口位置在左上角
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);

        // 动态注册广播，这个广播是在屏幕亮的时候，发送广播，来关闭当前的Activity
//        registerReceiver(receiver, new IntentFilter("FinishOnePPActivity"), MytoolsGetPackageName.getBroadCastPermision(),null);
        registerReceiver(receiver, new IntentFilter("FinishOnePPActivity"));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
//         if (BuildConfig.DEBUG) Log.e("OnePxActivity","onDestroy");
        super.onDestroy();
    }

}