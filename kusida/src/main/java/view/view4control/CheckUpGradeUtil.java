package view.view4control;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import common.GlobalContext;
import ctrl.OCtrlCommon;
import model.ManagerPublicData;
import view.ActivityWeb;

public class CheckUpGradeUtil {
    public static boolean checkPackage( String packageName)
    {
        if (packageName == null || "".equals(packageName))
            return false;
        try
        {
            GlobalContext.getContext().getPackageManager().getApplicationInfo(packageName, PackageManager
                    .GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
    public static void toTaoBao( ){
        if(!TextUtils.isEmpty(ManagerPublicData.taobaoUrl)){
//            String url = "https://item.taobao.com/item.htm?spm=a2126o.success.result.1.3f2d4831Rwr4tV&id=668997705760";
            Intent intent = new Intent();
            intent.setAction("Android.intent.action.VIEW");
            Uri uri = Uri.parse(ManagerPublicData.taobaoUrl); // 商品地址
            intent.setData(uri);
            intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在非activity类中调用startactivity方法必须添加标签
            GlobalContext.getContext().startActivity(intent);
        }else{
            OCtrlCommon.getInstance().cmmd_2303_qurryTaoBaoInfo();
        }
    }
    public static void toLiuLanQi(){
        if(!TextUtils.isEmpty(ManagerPublicData.taobaoUrl)){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(ActivityWeb.TITLE_NAME, "现在预约");
//                        String address="https://item.taobao.com/item.htm?spm=a2126o.success.result.1.3f2d4831Rwr4tV&id=668997705760";
            bundle.putString(ActivityWeb.HTTP_ADDRESS, ManagerPublicData.taobaoUrl);
            intent.putExtras(bundle);
            intent.setClass(GlobalContext.getContext(), ActivityWeb.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            GlobalContext.getContext().startActivity(intent);
        }else{
            OCtrlCommon.getInstance().cmmd_2303_qurryTaoBaoInfo();
        }
    }
    public static void toTaoBaoOrLiulanQi(){
        if(checkPackage("com.taobao.taobao")){
            toTaoBao();
        }else{
            toLiuLanQi();
        }
    }
}
