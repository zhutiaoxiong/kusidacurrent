package com.kulala.linkscarpods.service;

import android.util.Log;

import com.kulala.linkspods.BuildConfig;


class SocketCheckLinkThread extends Thread {
    private static final long keepAliveSecond = 2;//10-50
    private boolean isStop                  = false;
    //=========================================================
    private static SocketCheckLinkThread s_instance;
    public static SocketCheckLinkThread getInstance() {
        if (s_instance == null) {
            s_instance = new SocketCheckLinkThread();
        }
        return s_instance;
    }
    //============================================================
    public void startThread(){
        isStop = false;
        if(!isAlive())this.start();
    }
    public void run() {
        isStop = false;
        while (!isStop) {
            try {
                Thread.sleep(keepAliveSecond*1000L);
                 if (BuildConfig.DEBUG) Log.e("SocketCheckLinkThread", "发包时间"+SocketConnSer.SOCKET_SEND_TIME+"收包时间"+SocketConnSer.SOCKET_RECEIVE_TIME );
                if((SocketConnSer.SOCKET_SEND_TIME-SocketConnSer.SOCKET_RECEIVE_TIME)>2000L){
                    if (BuildConfig.DEBUG) Log.e("SocketCheckLinkThread", "重连" );
                    SocketConnSer.getInstance().reConnect("与服务器连接断开客户端主動断线重连");
                }
            } catch (Exception e) {
                 if (BuildConfig.DEBUG) Log.e("SocketCheckLinkThread", "检测心跳的线程错误" );
            }
        }
    }

}