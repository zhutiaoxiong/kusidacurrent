package common.http;

/**
 step1: init:
 HttpConn.getInstance().setGetDataListener(new HttpConn.GetDataListener() {
@Override
public JsonObject getPHeadJsonObj(String subscription) {
return PHeadHttp.getPHead(subscription);
}
@Override
public String getBasicUrl() {
return HttpUtil.getBasicUrl();
}
});
 HttpConn.getInstance().setOnHttpStateListener(...

 step2: sendMessage:
 HttpConn.getInstance().sendMessage(...
 */

import android.os.Process;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.GZIP;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpConn {
    public static String  CODE_TYPE    = "utf-8";
    private       int     TIMEOUT_CONN = 2000;
    private       int     TIMEOUT_SO   = 6000;
    public static boolean isHttpConn   = false;//用来判断是否可开百度地图
    private        OnHttpStateListener onHttpStateListener;
    private        GetDataListener     getDataListener;
    // ======================================================
    private static HttpConn            _instance;
//    OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder().build();

    private HttpConn() {
        init();
    }

    public static HttpConn getInstance() {
        if (_instance == null)
            _instance = new HttpConn();
        return _instance;
    }

    private void init() {

    }

    // ======================================================
    public void sendMessage(JsonObject params, int protocol) {
        if (params == null) params = new JsonObject();
        params.addProperty("protocol", protocol);
        if(protocol!=1219)Log.i("<<<Http>>>", "发包>>>> cmd:" + protocol);
        new HttpThread(params, protocol).start();
    }

    // ==========================out============================

    private class HttpThread extends Thread {
        private JsonObject params;// +common?funid=1101&rd=24324
        private int        protocol;

        HttpThread(JsonObject params, int protocol) {
            this.params = params;
            this.protocol = protocol;
            if (params == null) this.params = new JsonObject();
        }

        public void run() {
            if (getDataListener == null) return;//用此侦听器接收外部数据
            if (onHttpStateListener != null) onHttpStateListener.onSendStart(protocol);
            try {
                //init
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//                 if (BuildConfig.DEBUG) Log.e("<<<Http>>>", "发包>>>>1");
                String subscription = "common?funid=" + protocol + "&rd=" + (protocol * 123 / 58);
                String useUrl       = getDataListener.getBasicUrl().concat(subscription);
                int    handle       = 0;//发包gzip
                int    shandle      = 1;//收包gzip
//                 if (BuildConfig.DEBUG) Log.e("<<<Http>>>", "发包>>>>2");
                params.add("phead", getDataListener.getPHeadJsonObj(subscription));
                 if (BuildConfig.DEBUG) Log.e("<<<Http>>>", getDataListener.getPHeadJsonObj(subscription).toString());
                String zip;
                if(handle == 1){
                    zip = GZIP.toGZIPString(params.toString());
                }else{
//                    byte[] byts = params.toString().getBytes("UTF-8");
//                    zip = new String(byts, "UTF-8");
                    zip = params.toString();
                }
//                 if (BuildConfig.DEBUG) Log.e("<<<Http>>>", "发包>>>>4");

                //put data
                //加入日志
//                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//                if(BuildConfig.DEBUG)httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//                else httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                //开始client
                OkHttpClient mOkHttpClient = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("handle", handle + "")
                        .add("data", zip)
                        .add("shandle", shandle + "")
                        .build();
                Request request = new Request.Builder()
                        .url(useUrl)
                        .post(formBody)
                        .build();


//                Log.e("<<<Http>>>",request.url().toString()+request.body().toString());
//                 if (BuildConfig.DEBUG) Log.e("<<<Http>>>", "发包>>>>5");
                Call call = mOkHttpClient.newCall(request);
//                 if (BuildConfig.DEBUG) Log.e("<<<Http>>>", "发包>>>>6");
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        isHttpConn = false;
                         if (BuildConfig.DEBUG) Log.e("<<<Http>>>", "网络连接异常>>>> Send:cmd:" + protocol + " exception:" + e.toString());
                        if (onHttpStateListener != null)onHttpStateListener.onSendFailed(protocol, " exception:" + e.toString());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException{
                        String json = response.body().string();
                        if (json == null || json.equals("")) {
                             if (BuildConfig.DEBUG) Log.e("<<<Http>>>", ">>>Http 收包服务器返回null字串");
                            return;
                        }
                        if(protocol!=1219)Log.i("<<<Http>>>", ">>>Http成功收包:" + protocol + " data: " + json);
                        if (onHttpStateListener != null)onHttpStateListener.onReceiveData(protocol, json);
                        isHttpConn = true;
                    }

                });
            } catch (Exception e) {
                isHttpConn = false;
                 if (BuildConfig.DEBUG) Log.e("<<<Http>>>", "网络连接异常>>>> Send:cmd:" + protocol + " exception:" +  e.toString());
                if (onHttpStateListener != null)
                    onHttpStateListener.onSendFailed(protocol, " exception:" + e.toString());
            }
        }
    }
    // ======================================================

    public void setGetDataListener(GetDataListener listener) {
        this.getDataListener = listener;
    }

    /**
     * 连接装态改变
     */
    public void setOnHttpStateListener(OnHttpStateListener listener) {
        this.onHttpStateListener = listener;
    }

}
