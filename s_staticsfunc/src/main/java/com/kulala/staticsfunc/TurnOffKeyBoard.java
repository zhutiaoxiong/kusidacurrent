package com.kulala.staticsfunc;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by qq522414074 on 2016/11/11.
 */

public class TurnOffKeyBoard {
    //关闭软件盘
    public static void closeKeyBoard(Context context) {
        Activity activity = (Activity)context;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&activity.getCurrentFocus()!=null){
            if (activity.getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    /**
     * 进入搜索页面默认弹出软键盘
     */
//    public static void openKeyBoard(final EditText edit){
//        //进入搜索页面打开软键盘
//        edit.setFocusable(true);
//        edit.setFocusableInTouchMode(true);
//        edit.requestFocus();
//        InputMethodManager inputManager =
//                (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(edit, 0);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//                           public void run() {
//                               InputMethodManager inputManager =
//                                       (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                               inputManager.showSoftInput(edit, 0);
//                           }
//                       },
//
//                998);
//    }
    //===================================================
    private static int savedMode = -1;
    //打开软件盘
    public static void openKeyBoardOpenScoll(Context contextActivity) {
        Activity activity = (Activity) contextActivity;
        savedMode = activity.getWindow().getAttributes().softInputMode;
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //如果输入法在窗口上已经显示，则隐藏，反之则显示
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//
//        imm.showSoftInput(activity.getWindow().getDecorView(), InputMethodManager.SHOW_FORCED);
         if (BuildConfig.DEBUG) Log.e("TurnKeyBoard","输入法打开");
    }
    //关闭软件盘,一般是先开的状态
    public static void closeKeyBoardCloseScoll(Context contextActivity) {
        Activity           activity = (Activity) contextActivity;
        InputMethodManager imm      = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive() && activity.getCurrentFocus() != null) {
//            if(savedMode!=-1)activity.getWindow().setSoftInputMode(savedMode);
//            savedMode = -1;
//            if (activity.getCurrentFocus().getWindowToken() != null) {
//                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
        if (savedMode != -1) activity.getWindow().setSoftInputMode(savedMode);
        savedMode = -1;
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0); //强制隐藏键盘
         if (BuildConfig.DEBUG) Log.e("TurnKeyBoard","输入法关闭");
    }
}
