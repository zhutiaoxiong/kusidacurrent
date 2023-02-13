package view.view4control;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

import common.GlobalContext;

public class MyLongClickButton10Seconds extends androidx.appcompat.widget.AppCompatButton {
    private long downTime;
    private long upTime;
    private Timer  timer;
    private TimerTask timerTask;
    private int num;
    public MyLongClickButton10Seconds(Context context) {
        super(context);
    }

    public MyLongClickButton10Seconds(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLongClickButton10Seconds(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime=System.currentTimeMillis();
                Log.e("按钮点击事件", "按下时间" +downTime);
                if(timer==null){
                    timer = new Timer();
                }
                if(timerTask==null){
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            num++;
                            if(num==1){
                                showVibrator();
                            }
                            if(num==50){
                                if (timerTask != null) {
                                    timerTask.cancel();
                                    timerTask = null;
                                }
                                if (timer != null) {
                                    timer.cancel();
                                    timer = null;
                                }
                                num=0;
                                if(myClickListner!=null){
                                    Log.e("按钮点击事件", "停止" );
                                    myClickListner.onStop();
                                }
                                return;
                            }
                           if(myClickListner!=null){
                               Log.e("按钮点击事件", "每阁300ms发送数据" );
                               myClickListner.onLongClick();
                           }
                        }
                    };
                }
                timer.schedule(timerTask, 1000, 200);//延时1s，每隔500毫秒执行一次run方法
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                upTime=System.currentTimeMillis();
                Log.e("按钮点击事件", "抬起时间" +upTime);
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                num=0;
                if(myClickListner!=null){
                    Log.e("按钮点击事件", "停止" );
                    myClickListner.onStop();
                }
                if(upTime-downTime<800){
                    Log.e("按钮点击事件", "小于300ms抬起" );
                    if(myClickListner!=null){
                        myClickListner.onClick();
                    }
                }
                break;
        }
        return true;
    }
    public interface MyClickListner{
        void onClick();
        void onLongClick();
        void onStop();
    }
    private MyClickListner myClickListner;

    public void setMyClickListner(MyClickListner listner){
        this.myClickListner=listner;
    }
    private void showVibrator() {
        Vibrator vibrator = (Vibrator) GlobalContext.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300L);//重复两次上面的pattern 如果只想震动一次，index设为-1
    }
}
