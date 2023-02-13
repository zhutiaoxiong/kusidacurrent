package com.kulala.staticsfunc.time;

import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by Administrator on 2017/6/16.
 */

public class TimeDelayTask {
    private OnTimeEndListener onTimeEndListener;
    private Timer timer;

    public interface OnTimeEndListener {
        void onTimeEnd();
    }
    public void runTask(long timeMs, OnTimeEndListener listener) {
        onTimeEndListener = listener;
        TimerTask task = new TimerTask() {
            public void run() {
                if (onTimeEndListener != null) onTimeEndListener.onTimeEnd();
            }
        };
        timer = new Timer();
        timer.schedule(task, timeMs);
    }
    public void stop() {timer.cancel();}
}
