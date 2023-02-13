package common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.MD5;
import com.kulala.staticsfunc.static_system.SystemMe;

import java.net.URL;

import common.linkbg.BootBroadcastReceiver;
import model.ManagerLoginReg;

public class PHeadHttp {
    //    private static String BASIC_URL_NORMAL  = "http://api.91kulala.com/kulala/";//正式
    public static  String BASIC_URL_DEBUG   = "http://120.27.137.20:8090/kulala/";
    //    private static String BASIC_URL_MASHINE = "http://192.168.1.145:8080/kulala/";//逃
//    private static String BASIC_URL_NORMAL  = "http://192.168.1.194:8081/";//正式
//    private static String BASIC_URL_NORMAL  = "http://192.168.0.37:8081/";//正式
//    private static String BASIC_URL_NORMAL  = "http://mkong.kcakl.com:8081/";//正式
//        private static String BASIC_URL_NORMAL  = "http://120.77.39.84:8086/";//正式
    private static String BASIC_URL_NORMAL  = "http://47.106.165.101:8081/";//正式

//        private static String BASIC_URL_NORMAL  = "http://192.168.0.166:8081/";//正式廖桑本地

    public static String getBasicUrl() {
//        return BASIC_URL_MASHINE;
//        return BASIC_URL_DEBUG;
        return BASIC_URL_NORMAL;
    }


    //========================================================================
    private static long   userId = 0;
    private static String watchToken  = "";
    private static String token  = "";
    public static  String rsaKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCA1XDAOhAknxaLVhbf9+JX6GfI85s3QySLHtWGKgJjsE/yB9kVfEGTU8mcOxUctpc/ozdQH3UbaeglKfMYcwzayrjy2/P00d+vZ94QqL48Ll09wDUvHNCUCt7GJUiEF8h+CTYv1aPW8tGLV+kJDdKYBpWAAA5CMPvz3SA0YNcfiQIDAQAB";

    public static void changeUserInfo(long userId1) {
        userId = userId1;
    }
    public static void changeUserToken(long userId,String toke) {
        //userId 0 退出登录了,同时改二个值才对
        token = toke;
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserToken(userId,toke);
    }
    public static void changeUserWatchToken(long userId,String watchToke) {
        watchToken = watchToke;
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"watchToken",watchToken);
    }
    public static String getUserWatchToken() {
        if(watchToken == null || watchToken.length() == 0){
            String toke =  ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId,"watchToken");
            if(toke==null)return null;
            watchToken = toke;
        }
        return watchToken;
    }
    /**用于正常进入系统后取值*/
    public static String getToken() {
        if (token == null || token.length() == 0) {
            if(userId==0){
                userId= ManagerLoginReg.getInstance().getCurrentUser().userId;
            }
            String toke =  ODBHelper.getInstance(GlobalContext.getContext()).queryUserToken(userId);
            if(toke==null)return null;
            String[] arr = toke.split(":");
            //需要保眔以前TOKEN
            if(arr == null)return null;
            long cacheUserId = 0;
            String cacheToken = "";
            if(arr.length == 1 && arr[0].length()>0){
                changeUserToken(userId,arr[0]);
                cacheUserId = userId;
                cacheToken = arr[0];
            }else if(arr.length == 2) {
                cacheUserId = Integer.parseInt(arr[0]);
                cacheToken = arr[1];
            }
            if (userId != 0 && userId == cacheUserId && cacheToken.length()>0) {
                token = cacheToken;
                BootBroadcastReceiver.initOrChangeSocket();
            } else {
                return null;
            }
        }
//         if (BuildConfig.DEBUG) Log.e("token","PheadToken:"+token);//24f189d5b3d51ad9960475612de2d2b7
        return token;
    }

    public static JsonObject getPHead(String subString) {
        JsonObject obj = new JsonObject();
        try {
            Context         context = GlobalContext.getContext();
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            obj.addProperty("pversion", 1000);
            obj.addProperty("mac", "");
            obj.addProperty("uid", userId);
            obj.addProperty("accstr", getAccStr(subString));
            obj.addProperty("cid", appInfo.metaData.getInt("cid"));
            obj.addProperty("cversion", SystemMe.getVersionCode(GlobalContext.getContext()));
            obj.addProperty("cverstr", SystemMe.getVersionName(GlobalContext.getContext()));
            String chanl = appInfo.metaData.getString("channel");//officcal,qq,baidu,huawei,360单独分开加密
            obj.addProperty("channel", chanl.equals("_360") ? "360" : chanl);
            obj.addProperty("local", "cn");
            obj.addProperty("lang", "en");
            obj.addProperty("token", getToken());
            obj.addProperty("sys", String.valueOf(android.os.Build.VERSION.SDK_INT));
            obj.addProperty("net", "wifi");
            obj.addProperty("idfa", "");
            obj.addProperty("devicetoken", SystemMe.getUUID(GlobalContext.getContext()));
            obj.addProperty("platform", 1);
            obj.addProperty("imei", SystemMe.getUUID(GlobalContext.getContext()));
            obj.addProperty("model", android.os.Build.MODEL);
            obj.addProperty("imsi", "460030912121001");
            obj.addProperty("resolution", "320*480");
        } catch (Exception e) {
        }
        return obj;
    }
    public static String getAccStr(String subString) {
        try {
//            String subscription = "common?funid=" + 1 + "&rd=" + (1 * 123 / 58);
            URL u = new URL(getBasicUrl() + subString);
            return MD5.MD5generator(u.getPath() + "?" + u.getQuery() + "kulala");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
