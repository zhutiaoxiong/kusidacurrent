package common.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WindowStartStopTime extends RelativeLayout {
    private SmartImageView img_car_logo;
    private TextView txt_car_name, txt_time_l, txt_time_r;
    public TextView txt_collect,txt_share,txt_navigate;

    public static DataCarTime data;
    private MyHandler handler = new MyHandler();
    private CountDownTimer     countControlTimer;
    public WindowStartStopTime(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.map_window_start_stop_time_forcar, this, true);
        img_car_logo = (SmartImageView) findViewById(R.id.img_car_logo);
        txt_car_name = (TextView) findViewById(R.id.txt_car_name);
        txt_time_l = (TextView) findViewById(R.id.txt_time_l);
        txt_time_r = (TextView) findViewById(R.id.txt_time_r);
        txt_collect = (TextView) findViewById(R.id.txt_collect);
        txt_share = (TextView) findViewById(R.id.txt_share);
        txt_navigate = (TextView) findViewById(R.id.txt_navigate);
    }
    @Override
    protected void onDetachedFromWindow() {
        if(countControlTimer!=null)countControlTimer.cancel();
        super.onDetachedFromWindow();
    }
    @Override
    protected void onAttachedToWindow() {
        if(countControlTimer!=null)countControlTimer.start();
        super.onAttachedToWindow();
    }
    public void setData(DataCarTime dataCarTime) {
        this.data = dataCarTime;
//        if(BuildConfig.DEBUG) if (BuildConfig.DEBUG) Log.e("TimeWindow","DataCarTime:"+dataCarTime.carName+":"+dataCarTime.time);
        handlerChangeView();
    }
    public void handlerChangeView() {
        Message message = new Message();
        message.what = 101;
        handler.sendMessage(message);
    }
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    if (data == null) return;
                    if (data.logo != null) img_car_logo.setImageUrl(data.logo);
                    if (data.carName != null) txt_car_name.setText(data.carName);
                    if (data.isStart == 1) {
                        txt_time_l.setText("启动时长:");
                    } else {
                        txt_time_l.setText("熄火时长:");
                    }
                    if (countControlTimer == null) countControlTimer = new CountDownTimer(200000, 1000) {
                        @Override
                        public void onTick(long l) {
                            long now = System.currentTimeMillis();
                            long pastTime = now - data.time;//经过多长时间
                            String showTime = "";
                            if(pastTime>1000L*60*60*24){//大于一天
                                showTime = ((int)(Math.floor(pastTime/(1000L*60*60*24))))+"天";
                            }else{
                                SimpleDateFormat sdf        = new SimpleDateFormat("HH:mm:ss");
                                showTime = sdf.format(new Date(pastTime-8*60*60*1000L));//= ODateTime.time2StringHHmmss(pastTime);
                            }
                            txt_time_r.setText(showTime);
                        }
                        @Override
                        public void onFinish() {
                        }
                    };
                    countControlTimer.cancel();
                    countControlTimer.start();
                    break;
            }
        }
    }

}
