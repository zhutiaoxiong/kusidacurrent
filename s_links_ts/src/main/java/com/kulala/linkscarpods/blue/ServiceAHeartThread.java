package com.kulala.linkscarpods.blue;

import android.content.Intent;
import android.util.Log;

import com.kulala.linkscarpods.LogMeLinks;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkscarpods.service.KulalaServiceC;

import static com.kulala.linkscarpods.blue.BlueStaticValue.ONBLUECONNCHANGE;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_1_HEART_BEET;

class ServiceAHeartThread extends Thread {
    private boolean isStop                  = false;
    private long timeNum = 0;
    private long sleepTime = 1000L;
    private boolean isThreadStart;
    //=========================================================
    private static ServiceAHeartThread s_instance;
    public static ServiceAHeartThread getInstance() {
        if (s_instance == null) {
            s_instance = new ServiceAHeartThread();
        }
        return s_instance;
    }
    //============================================================
    public void startThread(){
        isStop = false;
        if(!isThreadStart){
            if(!isAlive()){
                this.start();
                isThreadStart=true;
            }
        }

    }
    public void stopThread(){
        isStop = true;
    }
//    private long waitPlayTime = 0;
//    public void waitPlayMusic(){
//        if(waitPlayTime == 0)waitPlayTime = System.currentTimeMillis();
//    }
//    public void stopPlayMusic(){
//        waitPlayTime = 0;
//    }
    public void run() {
        isStop = false;
        while (!isStop) {
            try {
                Thread.sleep(sleepTime);//1000L
                timeNum++;
                long now = System.currentTimeMillis();
                DataCarBlue carBlue = BlueLinkControl.getInstance().getDataCar();
                //soundKeep
//                if(OShakeBlue.getIsScreenOff()){
//                    if(carBlue!=null && carBlue.carId!=0)//要求,只有熄屏蓝牙没连上时才播声音,功耗,但实际不行不解是  && !BlueLinkControl.getInstance().getIsBlueConnOK()
//                        SoundPlay.getInstance().playMediaSoundKeep(KulalaServiceA.KulalaServiceAThis);
//                }else{
//                    SoundPlay.getInstance().stopMediaSoundKeep(KulalaServiceA.KulalaServiceAThis);
//                }

//                if(!OShakeBlue.getIsScreenOff()){
//                    SoundPlay.getInstance().stopMediaSoundKeep(KulalaServiceA.KulalaServiceAThis);
//                }if(OShakeBlue.getIsScreenOff() && BlueLinkControl.getInstance().getIsBlueConnOK()){
//                    SoundPlay.getInstance().stopMediaSoundKeep(KulalaServiceA.KulalaServiceAThis);
//                }else if((carBlue.isShakeOpen || carBlue.isUseBlueModel) && OShakeBlue.getIsScreenOff()  &&!BlueLinkControl.getInstance().getIsBlueConnOK()){
//                    SoundPlay.getInstance().playMediaSoundKeep(KulalaServiceA.KulalaServiceAThis);
//                }
                //blueState
                if(timeNum%2==0) {
                    Intent broadcast1 = new Intent();
                    broadcast1.setAction(ONBLUECONNCHANGE);
                    boolean isConnect=BlueLinkControl.getInstance().getIsBlueConnOK();
                    Log.e("bluestate", "1:-------- "+isConnect);
//                    broadcast1.putExtra("blueConnOK", BlueLinkControl.getInstance().getIsBlueConnOK());
                    broadcast1.putExtra("blueConnOK", BlueLinkControl.getInstance().getIsBlueConnOKk());
                    broadcast1.setPackage(MytoolsGetPackageName.getPackageNameMy());
                    if(KulalaServiceA.KulalaServiceAThis!=null)
                        KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast1,MytoolsGetPackageName.getBroadCastPermision());
//                    KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast1);
                }
                //serviceAheart
                if(timeNum%10==0){//60Sec/1.5
                    //心跳包
                    Intent broadcast2 = new Intent();
                    broadcast2.setAction(SERVICE_1_HEART_BEET);
                    broadcast2.setPackage(MytoolsGetPackageName.getPackageNameMy());
                    if(KulalaServiceA.KulalaServiceAThis!=null)
                        KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast2,MytoolsGetPackageName.getBroadCastPermision());
//                    KulalaServiceA.KulalaServiceAThis.sendBroadcast(broadcast2);
                    //
                    if( now - KulalaServiceA.service2HeartTime>30*1000L&&KulalaServiceA.KulalaServiceAThis!=null){
                        KulalaServiceA.KulalaServiceAThis.startService(new Intent(KulalaServiceA.KulalaServiceAThis, KulalaServiceC.class));
                    }
                }
                if(timeNum % 30 == 0){
                    LogMeLinks.e("AHeart", "timeNum:" + timeNum);
                }
            } catch (Exception e) {
            }
        }
    }

}