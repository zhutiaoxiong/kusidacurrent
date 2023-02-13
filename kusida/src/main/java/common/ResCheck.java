package common;


import android.os.Process;

import common.timetick.OTimeSchedule;
import ctrl.OCtrlCommon;
import ctrl.OCtrlSocketMsg;
import model.ManagerCarList;
import model.ManagerCommon;
import model.ManagerGesture;
import model.ManagerLoginReg;
import model.ManagerSwitchs;
import model.ManagerWarnings;

//加载预设和通用信息
public class ResCheck {
    public static boolean alreadyRunning = false;

    public static void runningCheck() {
        if (alreadyRunning) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                try {
                    alreadyRunning = true;
                    Thread.sleep(500L);
                    // 加载本地信息
                    ManagerGesture.getInstance();
                    ManagerLoginReg.getInstance();
                    ManagerCarList.getInstance();
                    ManagerSwitchs.getInstance();
                    ManagerWarnings.getInstance();
                    ManagerCommon.getInstance();
                    Thread.sleep(300L);
                    // 加载通用信息
                    OCtrlCommon.getInstance().ccmd1303_getCommonInfo();
                    Thread.sleep(100L);
                    // 加载品牌列表
//                    OCtrlCommon.getInstance().ccmd1310_getBrandList(ManagerCommon.getInstance().getBrandUpdateTime());
                    // 加载数据连接
                    OCtrlSocketMsg.getInstance();
                    //加载声音
                    OTimeSchedule.getInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void runningLost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                try {
                    // 加载登录过期
                    OCtrlCommon.getInstance().ccmd1115_getLoginState();
                    Thread.sleep(100L);
                    //取消失的信息
                    OCtrlCommon.getInstance().ccmd1223_getLostNotifation();
//                    Thread.sleep(100L);
//                    //加载结束后就查没取到的警告,协议已变要指定每辆车
//                    OCtrlCar.getInstance().ccmd1221_getWarninglist(ManagerWarnings.getInstance().getLastWarrningTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
