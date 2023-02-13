package com.kulala.linkscarpods.permission;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.kulala.linkspods.BuildConfig;


/**
 * Created by Administrator on 2017/10/30.
 */

public class SelfAutoRunPermission {
    /**
     * 跳转到权限设置界面
     */
    public static void gotoPermissionAutoStart(Activity activity) {
        String mobileType = android.os.Build.MANUFACTURER;
        if (mobileType == null) return;
        if (BuildConfig.DEBUG) Log.e("mobileType", "******************当前手机型号为：" + mobileType);
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName = null;
            if (mobileType.equals("Xiaomi")) { // 红米Note4测试通过 
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (mobileType.equals("Letv")) { // 乐视2测试通过 
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (mobileType.equals("samsung")) { // 三星Note5测试通过 
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
            } else if (mobileType.equals("HUAWEI")) { // 华为测试通过 ,com.huawei.permissionmanager.ui.MainActivity
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (mobileType.equals("vivo")) { // VIVO测试通过 
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (mobileType.equals("Meizu")) { //万恶的魅族 ,new Intent("com.meizu.safe.security.SHOW_APPSEC");
                // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，系统更新之后，完全找不到了，心里默默Fuck！ 
                // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图 
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
            } else if (mobileType.equals("OPPO")) { // OPPO R8205测试通过 
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            } else if (mobileType.equals("ulong")) { // 360手机 未测试 
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
            } else {                // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备 
                // 针对于其他设备，我们只能调整当前系统app查看详情界面 
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            }
            intent.setComponent(componentName);
            activity.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面 
            intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
        }
    }
    }
//
//
//    private static void gotoMeizuPermission(Activity activity) {
//        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
//        try {
//            activity.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            gotoHuaweiPermission(activity);
//        }
//    }
//    private static void gotoHuaweiPermission(Activity activity) {
//        try {
//            Intent intent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
//            intent.setComponent(comp);
//            activity.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            intoAutoRunPermission(activity);
//        }
//    }
//    private static void intoAutoRunPermission(Activity activity) {
//        Intent localIntent = new Intent();
//        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
//        activity.startActivity(localIntent);
//    }
//
//    /** * 跳转到权限设置界面 */
//    public static void getAppDetailSettingIntent(Context context) {
//        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");//vivo
//        if (appIntent == null)appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");//oppo
//        if (appIntent == null) appIntent = context.getPackageManager().getLaunchIntentForPackage("com.meizu.safe");//meizu
//        if (appIntent == null) appIntent = context.getPackageManager().getLaunchIntentForPackage("com.huawei.systemmanager");//huawei
//        if (appIntent != null) {
//            context.startActivity(appIntent);
////            floatingView = new SettingFloatingView(this, "SETTING", getApplication(), 0);
////            floatingView.createFloatingView();
////            return;
//        }
//    }
///**
// * 跳转到自启动页面
// *
// case "samsung":
// componentName = new ComponentName("com.samsung.android.sm",
// "com.samsung.android.sm.app.dashboard.SmartManagerDashBoardActivity");
// break;
// case "huawei":
// componentName = new ComponentName("com.huawei.systemmanager",
// "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
// break;
// case "xiaomi":
// componentName = new ComponentName("com.miui.securitycenter",
// "com.miui.permcenter.autostart.AutoStartManagementActivity");
// break;
// case "vivo":
// componentName = new ComponentName("com.iqoo.secure",
// "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity");
// break;
// case "oppo":
// componentName = new ComponentName("com.coloros.oppoguardelf",
// "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity");
// break;
// case "360":
// componentName = new ComponentName("com.yulong.android.coolsafe",
// "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity");
// break;
// case "meizu":
// componentName = new ComponentName("com.meizu.safe",
// "com.meizu.safe.permission.SmartBGActivity");
// break;
// *
// */
//
//
//
//
//跳转方法：
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ComponentName componentName = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
//        intent.setComponent(componentName);
//        try{
//        context.startActivity(intent);
//        }catch (Exception e){//抛出异常就直接打开设置页面
//        intent=new Intent(Settings.ACTION_SETTINGS);
//        context.startActivity(intent);
//        }
