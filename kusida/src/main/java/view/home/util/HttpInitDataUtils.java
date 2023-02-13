package view.home.util;

import android.util.Log;

import common.PHeadHttp;
import ctrl.OCtrlBaseHttp;

public class HttpInitDataUtils {
    public static void initData(){
        PHeadHttp.getToken();
        OCtrlBaseHttp.getInstance();
        Log.e("myhome", "init ");
    }
}
