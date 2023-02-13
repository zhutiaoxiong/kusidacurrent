package com.kulala.linkscarpods.service;

import android.content.Intent;

import com.kulala.linkscarpods.LogMeLinks;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkscarpods.blue.KulalaServiceA;

import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_2_HEART_BEET;

class SocketHeartThread extends Thread {
    private static final long keepAliveSecond = 20;//10-50
    private int timeNum = 0;
    private boolean isStop                  = false;
    //=========================================================
    private static SocketHeartThread s_instance;
    public static SocketHeartThread getInstance() {
        if (s_instance == null) {
            s_instance = new SocketHeartThread();
        }
        return s_instance;
    }
    //============================================================
    public void startThread(){
        isStop = false;
        if(!isAlive())this.start();
    }
    public void run() {
        isStop = false;
        while (!isStop) {
            try {
                Thread.sleep(keepAliveSecond*1000L);
//                if(timeNum % keepAliveSecond == 0){
                    SocketConnSer.getInstance().sendMessage(2, "");//keep alive.这里有重连处理
                    LogMeLinks.e("TsControl","SocketHeartThread cmd>>>>>2");
//                }
                Intent broadcast2 = new Intent();
                broadcast2.setAction(SERVICE_2_HEART_BEET);
                broadcast2.setPackage(MytoolsGetPackageName.getPackageNameMy());
                if(KulalaServiceC.kulalaServiceCThis!=null) {
                    KulalaServiceC.kulalaServiceCThis.sendBroadcast(broadcast2,MytoolsGetPackageName.getBroadCastPermision());
//                    KulalaServiceC.kulalaServiceCThis.sendBroadcast(broadcast2);
                    if(System.currentTimeMillis() - KulalaServiceC.service1HeartTime>30*1000L){
                        KulalaServiceC.kulalaServiceCThis.startService(new Intent(KulalaServiceC.kulalaServiceCThis,KulalaServiceA.class));
                    }
                }
            } catch (Exception e) {
                SocketConnSer.getInstance().reConnect("心跳线程错" + e.toString());
            }
            timeNum++;
        }
    }

}