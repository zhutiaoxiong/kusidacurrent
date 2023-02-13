package com.kulala.linkscarpods.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kulala.linkscarpods.LogMeLinks;
import com.kulala.linkscarpods.blue.OCtrlSocketMsgBackground;
import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.static_system.NotificationUtils;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.kulala.linkscarpods.service.KulalaServiceC.kulalaServiceCThis;

/**
 * Created by Administrator on 2018/5/2.
 */

public class SocketDataGet {
//    public static int     IconId1       = -1;
//    public static String  projName1     = "";
    public static boolean isInitedNoti   = false;
    public static boolean openSound1    = true;
    public static boolean openVibrator1 = true;
    private DataInputStream reader;
    private Context         context;
    SocketDataGet(Context context) {
        this.context = context;
    }
    public void readData(byte[] enData) throws Exception {
        InputStream re = byteArr2InputStream(enData);
        reader = new DataInputStream(re);
        while (reader.available() > 0) {
            short cmd    = reader.readByte();
            int   length = reader.readInt();
//            OLogCt.getInstance().LogControlCmd("TsControl","        6. Socket收到数据:cmd:"+cmd+" length:"+length);
            //大于10Ｋ的数据基本是错包
            if (length > 10000) {
                int    avlia = reader.available();
                byte[] drop  = new byte[avlia];
                reader.read(drop, 0, avlia);
                break;
            }
            //判定错包完成
            byte[] data = new byte[length];// 准备要读多少,max 4096
            if (length > 0) {
                reader.read(data, 0, length);
            }
            byte       drop1 = reader.readByte();// \r\n
            byte       drop2 = reader.readByte();// \r\n
            JsonObject obj   = new JsonObject();
            if (data.length > 0) {
                String json = new String(data, SocketUtil.CODE_TYPE);
                Gson   gson = new Gson();
                obj = gson.fromJson(json, JsonObject.class);
                obj.addProperty("cmd", cmd);
            } else {
                obj.addProperty("cmd", cmd);
            }
//             if (BuildConfig.DEBUG) Log.e("SocketConn", "Receive--->cmd:" + cmd + " json:" + obj.toString());

            //重连完后看有没缓存的消息
//            if (cmd == 101) {
//                if (cacheMessage != null) {
//                     if (BuildConfig.DEBUG) Log.e("|||||SocketConn", "reSend cacheMessage>>>:");
//                    sendMessage(4, cacheMessage);//need one thread
//                    cacheMessage = null;
//                }
//            }else if(cmd == 104){
//                cacheMessage = null;//控制成功，清除
//            }
            onReceiveData(cmd, obj);
        }
    }
    // =========================心跳=============================
    public void onDataRead(byte[] bytes) throws Exception {
//        DataInputStream reader;
//        InputStream     re = byteArr2InputStream(bytes);
//        reader = new DataInputStream(re);
//        while (reader.available() > 0) {
//            short cmd    = reader.readByte();
//            int   length = reader.readInt();
//            //大于10Ｋ的数据基本是错包
//            if (length > 10000) {
//                int    avlia = reader.available();
//                byte[] drop  = new byte[avlia];
//                reader.read(drop, 0, avlia);
//                break;
//            }
//            //判定错包完成
//            byte[] data = new byte[length];// 准备要读多少,max 4096
//            if (length > 0) {
//                reader.read(data, 0, length);
//            }
//            byte       drop1 = reader.readByte();// \r\n
//            byte       drop2 = reader.readByte();// \r\n
//            JsonObject obj   = new JsonObject();
//            if (data.length > 0) {
//                String json = new String(data, SocketUtil.CODE_TYPE);
//                Gson   gson = new Gson();
//                obj = gson.fromJson(json, JsonObject.class);
//                obj.addProperty("cmd", cmd);
//            } else {
//                obj.addProperty("cmd", cmd);
//            }
//            onReceiveData(cmd, obj);
//        }
    }

    private  HashMap<Integer, JsonObject> mtypeCache;
    private void onReceiveData(final int cmd, final JsonObject obj) {//
        if (cmd == 101) return;// 第一个消息还未初始化
        new Thread(new Runnable() {
            @Override
            public void run() {
                int  mType      = OJsonGet.getInteger(obj, "mType");
                long createTime = OJsonGet.getInteger(obj, "createTime");
//                 if (BuildConfig.DEBUG) Log.e("Socket", "收包cmd:" + cmd);
                if (cmd == 3) {//服务端推送的消息
                    //一秒内不重复收消息,防止缓存不停响
                    if (mtypeCache == null) mtypeCache = new HashMap<Integer, JsonObject>();
                    //存入最新的消息
                    if (mtypeCache.containsKey(mType)) {
                        JsonObject ojj           = mtypeCache.get(mType);
                        long       createTimeOjj = OJsonGet.getInteger(ojj, "createTime");
                        if (createTimeOjj < createTime) {
                            mtypeCache.put(mType, obj);
                        }
                    } else mtypeCache.put(mType, obj);
                    if (!checkThreadRunning) new ReceiveCheckThread().start();
                } else {
                    doReceive(obj);
                }
            }
        }).start();
    }
    private boolean checkThreadRunning = false;

    class ReceiveCheckThread extends Thread {
        public ReceiveCheckThread() {
            checkThreadRunning = true;
        }
        public void run() {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mtypeCache == null) return;
//            Iterator<Map.Entry<Integer, JsonObject>> iter = mtypeCache.entrySet().iterator();
//            while (iter.hasNext()) {
//                Map.Entry<Integer, JsonObject> entry =  (Map.Entry) iter.next();
//                int                            mType = entry.getKey();
//                JsonObject                     data  = entry.getValue();
//                doReceive(data);
//            }
            for (Map.Entry<Integer, JsonObject> entry : mtypeCache.entrySet()) {
//                System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
                JsonObject                     data  = entry.getValue();
                doReceive(data);
            }
            mtypeCache.clear();
            checkThreadRunning = false;
        }
    }
    private void doReceive(JsonObject obj) {
        int cmd   = OJsonGet.getInteger(obj, "cmd");
        int mType = OJsonGet.getInteger(obj, "mType");
        if (BuildConfig.DEBUG) Log.e("------------","Socket收包----cmd>>>"+cmd+"mType"+mType);
        int        messageAlertType = OJsonGet.getInteger(obj, "messageAlertType");
        LogMeLinks.e("TsControl","Socket收包----cmd>>>"+cmd);
         if (BuildConfig.DEBUG) Log.e("------------","Socket收包----cmd>>>"+obj.toString());
        // 判断是否提示消息
        if (cmd == 3 && mType == 4) {// 消息推送
            if (!isInitedNoti) {
                if(kulalaServiceCThis!=null) kulalaServiceCThis.needInitNotification();
//                LinkControl.getInstance().needInitNotification();
            }
            int isNotice = OJsonGet.getInteger(obj, "isNotice");
            if (isNotice == 1) {
                JsonObject data       = OJsonGet.getJsonObject(obj, "data");
                String     content    = OJsonGet.getString(data, "content");
                long       createTime = OJsonGet.getLong(data, "createTime");
                String     title      = "酷斯达数字车钥匙消息提醒:";
                String     info       = content + "  " + time2StringHHmm(createTime);
//                LogMeLinks.e("TsControl", "openSound:"+openSound1+"  "+title + info);
                int alertType = OJsonGet.getInteger(data, "alertType");// 1：消息，2：警报，3：安全
                int alertId   = OJsonGet.getInteger(data, "alertId");
                if (alertType == 3 && openSound1) {//警告声音
                    SoundPlay.getInstance().play_warrning(context);
                }
                if (alertId != 0 && openSound1) {//语音
                     if (BuildConfig.DEBUG) Log.e("看警告声音", "博警告声音 " );
                    SoundPlay.getInstance().playSoundById(context, alertId);
                }
                 if (BuildConfig.DEBUG) Log.e("openVibrator", "alertId:"+alertId+"  "+"alertType:"+alertType+"  "+"openVibrator1:"+openVibrator1+"  ");
                if (alertId == 0 && alertType == 0) {
                    //no vibrator
                } else if (openVibrator1) {
                    SoundPlay.getInstance().playVibrator(context);//振动
                }
                if(context!=null){
                     if (BuildConfig.DEBUG) Log.e("------------", "通知从这里弹出来 " );
                    NotificationUtils notificationUtils = new NotificationUtils(context);
                    notificationUtils.sendNotification(title, info);
                }
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//        if(kulalaServiceCThis!=null){
//            boolean isForground=  kulalaServiceCThis.isAppForeground("com.client.proj.kusida");
//            if (BuildConfig.DEBUG) Log.e("前后台", "在不在前台"+isForground);
//        }
//         if (BuildConfig.DEBUG) Log.e("前后台", "KulalaServiceC.isInForground" + KulalaServiceC.isInForground);
//       boolean isInForground= KulalaServiceC.isInForground;
        if(KulalaServiceC.kulalaServiceCThis!=null) KulalaServiceC.kulalaServiceCThis.needDataBackUI(obj);
//        if(messageAlertType==1){
//            if(kulalaServiceCThis!=null) kulalaServiceCThis.needDataBackUI(obj);
//        }else{
//            if(isForground){
//                if(kulalaServiceCThis!=null) kulalaServiceCThis.needDataBackUI(obj);
//            }else{
//                if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡收");
//                OCtrlSocketMsgBackground.getInstance().processResult(cmd,obj);
//            }
//        }
        // 返回数据给UI
//
//        LinkControl.getInstance().needDataBackUI(obj);
    }

    private static String time2StringHHmm(long time) {
        SimpleDateFormat sdf        = new SimpleDateFormat("HH:mm");
        String           re_StrTime = sdf.format(new Date(time));
        return re_StrTime;
    }
    public static InputStream byteArr2InputStream(byte[] in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }
}
