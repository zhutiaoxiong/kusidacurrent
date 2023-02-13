package com.kulala.linkscarpods.service;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.kulala.linkscarpods.interfaces.OnLinkEventListener;
import com.kulala.linkscarpods.interfaces.OnSocketStateListener;
import com.kulala.staticsfunc.static_system.NotificationUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Administrator on 2018/4/26.
 */
@Deprecated
public class LinkControl {
    private Context context;
    private OnLinkEventListener onLinkEventListener;
    //=================================================================
    private static LinkControl _instance;
    public static LinkControl getInstance() {
        if (_instance == null)
            _instance = new LinkControl();
        return _instance;
    }
    public void initSoki(final Context context, final JsonObject jsonSockInitData){
        this.context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initSocket(context);
                SocketHeartThread.getInstance().startThread();
                boolean isNewChange =SocketUtil.changeData(context,jsonSockInitData);
                if(isNewChange && !TextUtils.isEmpty(SocketUtil.getUserId(context))){
                    SocketConnSer.getInstance().changeUserId();
                }
            }
        }).start();
    }
    public void needDataBackUI(JsonObject obj){//dataGet
        if(onLinkEventListener!=null)onLinkEventListener.onSokiDataBack(obj);
    }
    public void needInitNotification(){//dataGet
        if(onLinkEventListener!=null)onLinkEventListener.onNeedInitNotification();
    }
    public void initNotification(Context globalContext, int IconId, String projName, boolean soundOpen, boolean vitratorOpen){
//        SocketDataGet.IconId1 = IconId;
//        SocketDataGet.projName1 = projName;
        SocketDataGet.openSound1 = soundOpen;
        SocketDataGet.openVibrator1 = vitratorOpen;
        NotificationUtils notificationUtils = new NotificationUtils(globalContext);
//        notificationUtils.sendNotification(SocketDataGet.projName1 + "提醒您:", SocketDataGet.projName1 + "云启动已开启", SocketDataGet.IconId1);
    }
    public void setOnLinkEventListener(OnLinkEventListener eventListener){
        onLinkEventListener= eventListener;
    }
    //=================================================================
    public void sendMessage(int cmd,String jsonData){
        if(SocketUtil.getSocketPort(context) == 0){
            if(!SocketUtil.initData(context)) {
                if(onLinkEventListener!=null)onLinkEventListener.onNeedInitSoki();
            }else{
                SocketConnSer.getInstance().reConnect("SERVICE_A_INIT_SOCKET");//初始化成功后，重连接
            }
        }else{
            SocketConnSer.getInstance().sendMessage(cmd, jsonData);
        }
//        SocketConnSer.getInstance().sendMessage(2, "");//心跳
    }
    //=================================================================

    private void initSocket(final Context context1){
        SocketConnSer.getInstance().init(context1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //异常断线重连
                Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread thread, Throwable ex) {
                        Writer      result      = new StringWriter();
                        PrintWriter printWriter = new PrintWriter(result);
                        ex.printStackTrace(printWriter);
                        String stacktrace = result.toString();
                        if(SocketUtil.getSocketPort(context1)!=0)SocketConnSer.getInstance().reConnect("setDefaultUncaughtExceptionHandler"+stacktrace);
                    }
                });
            }
        }).start();//netWork in main thread
        SocketConnSer.getInstance().setOnConnStateChangeListener(new OnSocketStateListener() {
            @Override
            public void onConnFailed(String failedInfo) {
            }
            @Override
            public void onSendOK(int cmd) {
            }
            @Override
            public void onSendFailed(int cmd,String failedInfo) {
            }
        });
    }
    //=================================================================

}
