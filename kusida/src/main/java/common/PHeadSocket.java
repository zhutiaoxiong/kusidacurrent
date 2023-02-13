package common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.SystemMe;

import model.ManagerLoginReg;
import model.loginreg.DataUser;

public class PHeadSocket {
    //    private static String SERVERIP_NORMAL = "114.55.88.7";//正式
//    private static String SERVERIP_NORMAL = "free.shenzhuo.vip";//正式
//    private static String SERVERIP_NORMAL = "personal2.shenzhuo.vip";//正式廖桑本地
//    private static String SERVERIP_NORMAL = "mkong.kcakl.com";//正式
//        private static String SERVERIP_NORMAL = "120.77.39.84";//正式

    private static String SERVERIP_NORMAL = "47.106.165.101";//正式
//    private static String SERVERIP_DEBUG  = "120.27.137.94";//
//    public static  int    PORT            = 10086;
//    public static  int    PORT            = 16382;
//    public static  int    PORT            = 17080;

//        public static  int    PORT            = 16382;//廖桑本地
    public static  int    PORT            = 9899;//廖桑服務器

    public static String getServerIp() {
//        return SERVERIP_DEBUG;
        return SERVERIP_NORMAL;
    }
    //=========================================================

    /**Already add AllSocketInfo*/
    public static JsonObject
    getPHeadSocketAllInit(Context context) {
        JsonObject      obj     = new JsonObject();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            obj.addProperty("IP", getServerIp());
            obj.addProperty("PORT", PORT);
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            String  userId = (user==null) ?  "0" : String.valueOf(user.userId);
            obj.addProperty("userId", userId);
            obj.addProperty("deviceToken", SystemMe.getUUID(context));
            obj.addProperty("platform", 1);
            obj.addProperty("cversion", SystemMe.getVersionCode(context));
            obj.addProperty("cid", appInfo.metaData.getInt("cid"));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
