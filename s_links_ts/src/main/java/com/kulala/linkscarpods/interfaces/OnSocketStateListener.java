package com.kulala.linkscarpods.interfaces;

/**
 * Created by Administrator on 2017/2/10.
 */ //==================================open=========================================
public interface OnSocketStateListener {
    void onConnFailed(String failedInfo);

    void onSendOK(int cmd);

    void onSendFailed(int cmd, String failedInfo);
}
