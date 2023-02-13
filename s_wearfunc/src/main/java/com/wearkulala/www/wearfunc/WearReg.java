package com.wearkulala.www.wearfunc;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
/**
 * Created by Administrator on 2018/2/5.
 */

public class WearReg {
    private OCtrlCommonListener oCtrlCommonListener;
    public interface OCtrlCommonListener {
        void ccmd1305_getSwitchInfo();
        void ccmd1306_changeSwitch(int noticeId, boolean isOpen);
    }
    public void setOCtrlCommonListener(OCtrlCommonListener listener){
        oCtrlCommonListener = listener;
    }
    // ========================out======================
    private ManagerLoginRegListener managerLoginRegListener;
    public interface ManagerLoginRegListener {
        long getUserId();
        String getWatchToken();
        void changeUserWatchToken(long userId,String watchToke);
    }
    public void setManagerLoginRegListener(ManagerLoginRegListener listener){
        managerLoginRegListener = listener;
    }
    // ========================out======================
    private ManagerSwitchsListener managerSwitchsListener;
    public interface ManagerSwitchsListener {
        int getSwitchWearsOpen();
        void setSwitchWearsOpen(int open);
        int getSwitchWearsIde();
    }
    public void setManagerSwitchsListener(ManagerSwitchsListener listener){
        managerSwitchsListener = listener;
    }
    // ========================out======================
    private static GlobalContextListener globalContextListener;
    public interface GlobalContextListener {
        Activity getCurrentActivity();
    }
    public void setGlobalContextListener(GlobalContextListener listener){
        globalContextListener = listener;
    }
    // ========================out======================
    private static WearReg _instance;
    public static WearReg getInstance() {
        if (_instance == null)
            _instance = new WearReg();
        return _instance;
    }
    // ========================out======================
    public Activity getCurrentActivity(){
        if(globalContextListener!=null)return globalContextListener.getCurrentActivity();
        return null;
    }
    /**-1无数据 0关 1开*/
    public int getSwitchWearsOpen(){
        if(managerSwitchsListener!=null) return managerSwitchsListener.getSwitchWearsOpen();
        return -1;
    }
    public int getSwitchWearsIde(){
        if(managerSwitchsListener!=null) return managerSwitchsListener.getSwitchWearsIde();
        return -1;
    }
    public void setSwitchWearsOpen(int open){
        if(managerSwitchsListener!=null) managerSwitchsListener.setSwitchWearsOpen(open);
    }
    /**-1无用户 其它，有*/
    public long getUserID(){
        if(managerLoginRegListener!=null)return managerLoginRegListener.getUserId();
        return -1;
    }
    public String getWatchToken(){
        if(managerLoginRegListener!=null)return managerLoginRegListener.getWatchToken();
        return null;
    }
    public void changeUserWatchToken(long userId,String watchToke){
        if(managerLoginRegListener!=null)managerLoginRegListener.changeUserWatchToken(userId,watchToke);
    }
    public void ccmd1305_getSwitchInfo(){
        if(oCtrlCommonListener!=null)oCtrlCommonListener.ccmd1305_getSwitchInfo();
    }
    public void ccmd1306_changeSwitch(int noticeId, boolean isOpen){
        if(oCtrlCommonListener!=null)oCtrlCommonListener.ccmd1306_changeSwitch(noticeId,isOpen);
    }
    //=========================out use this==========================
    public void wearServiceChangeUser(long uid,String token){
        WearLinkServicePhone.changeUser(uid,token);
    }
    //=============================================
    //        if(WearReg.isGooglePlayServiceAvailable(this)) {
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//    Intent intent1 = new Intent(ActivityKulalaMain.this, WearLinkServicePhone.class);
//    startService(intent1);
    public static boolean isGooglePlayServiceAvailable (Context context) {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (status == ConnectionResult.SUCCESS) {
            Log.e("YXH", "GooglePlayServicesUtil service is available.");
            return true;
        } else {
            Log.e("YXH", "GooglePlayServicesUtil service is NOT available. CODE:"+status+GoogleApiAvailability.getInstance().getErrorString(status));
//            GoogleApiAvailability.getInstance().getErrorDialog(globalContextListener.getCurrentActivity(), status, 0).show();
            return false;
        }
    }
    public static boolean isCanGetLoginState = false;
    public static void setIsCanGetLoginState(boolean canGetLogin){
        isCanGetLoginState = canGetLogin;
    }
}
