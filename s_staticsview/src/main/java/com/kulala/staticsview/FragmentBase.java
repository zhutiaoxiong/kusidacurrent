package com.kulala.staticsview;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.static_interface.OCallBack;

public abstract class FragmentBase extends Fragment implements OEventObject, OCallBack {
    private MyHandlerlerler handler = new MyHandlerlerler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {}
    protected abstract void initViews();

    protected abstract void initEvents();

    protected abstract void invalidateUI();
    @Override
    public void callback(String key, Object value) {}
    //创建完成

    @Override
    public void onStart() {
        super.onStart();
    }
    //结束完成
    @Override
    public void onDestroy() {
        ODispatcher.recycle(this);
        super.onDestroy();
    }

    public void handleChangeData() {
        Message message = new Message();
        message.what = 311;
        handler.sendMessage(message);
    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandlerlerler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 311:
                    invalidateUI();
                    break;
            }
        }
    }

}
