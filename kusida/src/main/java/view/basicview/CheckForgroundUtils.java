package view.basicview;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import common.GlobalContext;

public class CheckForgroundUtils {
    public static boolean isForground;
    /**
     * 判斷前后台
     */
    public static boolean isAppForeground() {
        return isForground;
//        String packageName="com.client.proj.kusida";
//        ActivityManager am = (ActivityManager) GlobalContext.getContext().getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> list=am.getRunningTasks(1);
//        if(list==null||list.size()==0){
//            return true;
//        }
//        ComponentName cn = list.get(0).topActivity;
//        String currentPackageName = cn.getPackageName();
//        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName)) {
//            return true;
//        }
//
//        return false;
    }
}
