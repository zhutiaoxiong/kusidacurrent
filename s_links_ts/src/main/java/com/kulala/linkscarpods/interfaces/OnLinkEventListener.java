package com.kulala.linkscarpods.interfaces;

import com.google.gson.JsonObject;
/**
 * Created by Administrator on 2017/2/10.
 */
public interface OnLinkEventListener {
    void onNeedInitSoki();
    void onNeedInitNotification();
    void onSokiDataBack(JsonObject data);
}
