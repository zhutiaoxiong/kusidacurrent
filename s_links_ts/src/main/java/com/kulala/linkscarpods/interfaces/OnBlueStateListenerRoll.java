package com.kulala.linkscarpods.interfaces;

import com.kulala.linkscarpods.blue.DataReceive;

/**
 * Created by Administrator on 2017/2/10.
 */ //==================================open=========================================
public interface OnBlueStateListenerRoll {
    public static final int STATE_CLOSEING = 202;
    public static final int STATE_CLOSEED = 203;

    public static final int STATE_CONNECT_FAILED = 402;
    public static final int STATE_CONNECTING     = 403;
    public static final int STATE_CONNECT_OK     = 404;//以下表示连接成功 >=STATE_CONN_OK

    public static final int STATE_DISCOVER_FAILED = 602;
    public static final int STATE_DISCOVERING     = 603;
    public static final int STATE_DISCOVER_OK     = 604;//以下表示连接成功 >=STATE_CONN_OK

    public static final int STATE_MSG_SENDED   = 806;
    public static final int STATE_MSG_RECEIVED = 807;

    //conn
    void onConnecting();//正在连接

    void onConnectedOK();

    void onConnectedFailed(String error,boolean isNodevice);

    //discover
    void onDiscovering();//正在检测服务

    void onDiscoverOK();

    void onDiscoverFailed(String error,boolean isNoList);

    //data
    void onMessageSended(byte[] bytes);
    void onDataBack();//仅用来检测是否真的有数据返回，确定可摇
    void onDataReceived(DataReceive data);

    void onReadRemoteRssi(int rssi, int status);
    void needLog(String log);
}
