package model.demomode;


import com.kulala.staticsfunc.dbHelper.ODBHelper;

import common.GlobalContext;

/**
 * Created by qq522414074 on 2016/10/13.
 */
public class DemoMode {
    public static String jumpToWhere="";
//    public   String isDemoMode="";
    public static int isBeginOrFinish;

    public static void saveIsDemoMode(boolean isDemoMode){
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isDemoMode", String.valueOf(isDemoMode));
    }
    public static boolean getIsDemoMode(){
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isDemoMode");
        boolean isDemoMode = ODBHelper.queryResult2boolean(result);
        return isDemoMode;
    }
}
