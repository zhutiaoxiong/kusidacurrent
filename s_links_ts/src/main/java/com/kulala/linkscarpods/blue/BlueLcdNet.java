package com.kulala.linkscarpods.blue;

import android.util.Log;

import com.kulala.linkspods.BuildConfig;


public class BlueLcdNet {
    private static BlueLcdNet _instance;
    public static BlueLcdNet getInstance() {
        if (_instance == null)
            _instance = new BlueLcdNet();
        return _instance;
    }
    private OCtrl1233Listener oCtrl1233Listener;
    public interface OCtrl1233Listener {
        void ccmd1233_controlCar( DataCarInfo car,  int controlCmd,  int time);
    }
    public void setoCtrl1233Listener(OCtrl1233Listener listener){
         if (BuildConfig.DEBUG) Log.e("查看动向", "listener"+listener);
        oCtrl1233Listener = listener;
    }
    public void ccmd1233_controlCar( DataCarInfo car,  int controlCmd,  int time){
         if (BuildConfig.DEBUG) Log.e("查看动向", "oCtrl1233Listener"+oCtrl1233Listener);
        if(oCtrl1233Listener!=null){
            oCtrl1233Listener.ccmd1233_controlCar(car,controlCmd,time);
        }
    }
}
