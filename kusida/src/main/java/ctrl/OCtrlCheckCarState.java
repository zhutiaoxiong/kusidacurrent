package ctrl;

import android.os.CountDownTimer;
import android.util.Log;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import common.blue.BlueLinkReceiver;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.demomode.DemoMode;

/**
 * 100-299
 */
public class OCtrlCheckCarState {
    private static int     CHANGE_TIME = 3;//3,10
    private int countNum = 0;//记时器
    private        CountDownTimer     countDownTimer;
    // ========================out======================
    private static OCtrlCheckCarState _instance;

    protected OCtrlCheckCarState() {
    }

    public static OCtrlCheckCarState getInstance() {
        if (_instance == null)
            _instance = new OCtrlCheckCarState();
        return _instance;
    }

    public void setNeedCheck(boolean needCheck, int timeSecond) {
        if(timeSecond>0)CHANGE_TIME = timeSecond;
        if (countDownTimer != null) countDownTimer.cancel();
        if (needCheck) {
            countNum = 0;
            countDownTimer = new CountDownTimer(Long.MAX_VALUE, 500L) {//每秒记一次
                @Override
                public void onTick(long millisUntilFinished) {
                    countNum ++;
                    DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                    if(countNum % (CHANGE_TIME*2) == 0 &&
                        !BlueLinkReceiver.getInstance().getIsBlueConnOK()){
                        if (car == null) return;
                        if (car.ide == 0) return;//未激活车，不请求
                        if (car.isActive == 1) {
//                             if (BuildConfig.DEBUG) Log.e("Online","ccmd1219_getCarState:"+car.ide);
                            OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 0);
                        }else{
                            if (DemoMode.getIsDemoMode()) {
                                OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 1);
                            }
                        }
                    }
                    if(countNum % 4 == 0) Log.e("查询车辆状态", "8" );
                        ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);//每秒刷新车状态
                }
                @Override
                public void onFinish() {
                }
            };
            countDownTimer.start();
        }else{
            ODispatcher.dispatchEvent(OEventName.STOP_ANIM_ROTATE);
        }
    }

}
