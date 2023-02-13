package com.kulala.linkscarpods.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

public class SocketUtil {
    public static int    SOCKET_HEART_SECOND = 30;
    public final static int SOCKET_SLEEP_SECOND = 3 ;//如果没有连接无服务器。读线程的sleep时间
    public static int    SOCKET_READ_TIMOUT  = 8000;
    
    public static String CODE_TYPE = "utf-8";
    private static String SOCKET_IP   = "";
    private static int    SOCKET_PORT = 0;//if 0 noConn
    private static JsonObject pHead;
    private static String userId      = "0";
    private static String deviceToken = "";

    public static String getSocketIp(Context content) {
        if(SOCKET_IP.length() == 0)initData(content);
        return SOCKET_IP;
    }
    public static int getSocketPort(Context content) {
        if(SOCKET_PORT == 0)initData(content);
        return SOCKET_PORT;
    }
    public static String getUserId(Context content) {
        if(userId.length() == 0 || userId.equals("0"))initData(content);
        return userId;
    }
    public static JsonObject getpHead(Context content) {
        if(pHead == null)initData(content);
        return pHead;
    }
    /**
     * obj.addProperty("IP", getServerIp());
     * obj.addProperty("PORT", PORT);
     * obj.addProperty("userId", userId);
     * obj.addProperty("deviceToken", SystemMe.getIMEInum(context);
     * obj.addProperty("platform", 1);
     * obj.addProperty("cversion", SystemMe.getVersionCode(context);
     * obj.addProperty("cid", PHeadHttp.cid);
     * return isNewChange
     */
    public static boolean changeData(Context content, JsonObject jsonAllData) {
        Log.i("SocketUtil", "changeData SocketUtil.usedPORT:" + SocketUtil.SOCKET_PORT);
        saveJson(content, "SocketPHead", jsonAllData.toString());
        String usedIP1   = OJsonGet.getString(jsonAllData, "IP");
        int    usedPORT1 = OJsonGet.getInteger(jsonAllData, "PORT");
        String userId1   = OJsonGet.getString(jsonAllData, "userId");
        String UUID1     = OJsonGet.getString(jsonAllData, "deviceToken");
         if (BuildConfig.DEBUG) Log.e("getUUID", "socket changeData load UUID:" + UUID1);
        boolean isNewChange = true;
        if (usedIP1.equals(SOCKET_IP)
                && SOCKET_PORT!=0 && usedPORT1 == SOCKET_PORT
                && userId1!=null && userId1.equals(userId)
                && deviceToken!=null && deviceToken.equals(UUID1)) {
            isNewChange = false;
        } else {
            SOCKET_PORT = usedPORT1;
            SOCKET_IP = usedIP1;
            userId = userId1;
            deviceToken = UUID1;
            pHead = jsonAllData;
        }
        return isNewChange;
    }
    public static boolean initData(Context content) {
        if(SOCKET_PORT!=0)return true;//已加载过
        String data = loadJson(content, "SocketPHead");
        if (data == null || data.length() == 0) return false;
        Gson       gson = new Gson();
        JsonObject obj  = gson.fromJson(data, JsonObject.class);
        if (obj != null && obj.has("PORT") && obj.has("IP") && obj.has("userId")) {
            changeData(content, obj);
             if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<SocketUtil.initData OK>>>>>> " + data);
            return true;
        } else {
            return false;
        }
    }

    public static void saveJson(Context globalContext, String key, String value) {
        try {
            SharedPreferences        settings = globalContext.getSharedPreferences("carpods_client_database", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor   = settings.edit();
            if (editor == null) return;
            editor.putString(key, new String(value.toString().getBytes(CODE_TYPE), CODE_TYPE));
            editor.apply();
        } catch (UnsupportedEncodingException e) {
             if (BuildConfig.DEBUG) Log.e("DataShare", "saveJsonArray->不支持的编码");
        }
    }
    public static String loadJson(Context globalContext, String key) {
        SharedPreferences        settings = globalContext.getSharedPreferences("carpods_client_database", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor   = settings.edit();
        if (settings == null) return "";
        String result = settings.getString(key, "");
        return result;
    }
    public static String buffer2Str(ByteBuffer buffer) throws CharacterCodingException {
        return Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
    }
    public static JsonObject str2JsonObj(String jsonStrObj){
        Gson       gson = new Gson();
        JsonObject obj  = gson.fromJson(jsonStrObj, JsonObject.class);
        return obj;
    }
}
