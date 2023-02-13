package view.home.commonview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;
import com.orhanobut.logger.Logger;

import view.home.activity.ActivityLocatorTrackList;
import view.home.activity.ActivityMore;
import view.home.activity.ActivityOrder;

public class BaiDuInfoWindow extends ConstraintLayout {
    private BatteryView battery_view;
    private TextView battery_percent;
    private TextView txt_top_left;
    private TextView txt_locating_time;
    private TextView txt_communication_time;
    private TextView txt_status;
    private TextView txt_positioning_mode;
    private TextView txt_direction;
    private TextView txt_speed;
    private TextView txt_aderess;
    private ImageView img_left;
    private ImageView img_right;
    private ImageView img_weilan;
    private ImageView img_guiji;
    private ImageView img_mingling;
    private ImageView img_more;
    private TextView txt_weilan;
    private TextView txt_guiji;
    private TextView txt_mingling;
    private TextView txt_more;
    private static String TAG = "BaiDuInfoWindow";


    public BaiDuInfoWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.baidu_info_window, this, true);
        initView();
        initEvent();
    }

    private void initView() {
        battery_view = findViewById(R.id.battery_view);
        battery_percent = findViewById(R.id.battery_percent);
        txt_top_left = findViewById(R.id.txt_top_left);
        txt_locating_time = findViewById(R.id.txt_locating_time);
        txt_communication_time = findViewById(R.id.txt_communication_time);
        txt_status = findViewById(R.id.txt_status);
        txt_positioning_mode = findViewById(R.id.txt_positioning_mode);
        txt_direction = findViewById(R.id.txt_direction);
        txt_speed = findViewById(R.id.txt_speed);
        txt_aderess = findViewById(R.id.txt_aderess);
        img_left = findViewById(R.id.img_left);
        img_right = findViewById(R.id.img_right);
        img_weilan = findViewById(R.id.img_weilan);
        img_guiji = findViewById(R.id.img_guiji);
        img_mingling = findViewById(R.id.img_mingling);
        img_more = findViewById(R.id.img_more);
        txt_weilan = findViewById(R.id.txt_weilan);
        txt_guiji = findViewById(R.id.txt_guiji);
        txt_mingling = findViewById(R.id.txt_mingling);
        txt_more = findViewById(R.id.txt_more);
    }

    private void initEvent() {
        img_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("img_left onClick");
            }
        });
        img_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("img_right onClick");
            }
        });
        img_weilan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("img_weilan onClick");
            }
        });
        txt_weilan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("txt_weilan onClick");
            }
        });
        img_mingling.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("img_guiji onClick");
                Intent intent=new Intent(getContext(), ActivityOrder.class);
                getContext().startActivity(intent);
            }
        });
        txt_mingling.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("img_mingling onClick");
                Intent intent=new Intent(getContext(), ActivityOrder.class);
                getContext().startActivity(intent);
            }
        });
        img_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("img_more onClick");
                Intent intent=new Intent(getContext(), ActivityMore.class);
                getContext().startActivity(intent);
            }
        });
        txt_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("txt_more onClick");
                Intent intent=new Intent(getContext(), ActivityMore.class);
                getContext().startActivity(intent);
            }
        });
        img_guiji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("img_guiji");
                Intent intent=new Intent(getContext(), ActivityLocatorTrackList.class);
                getContext().startActivity(intent);
            }
        });
        txt_guiji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("txt_guiji");
                Intent intent=new Intent(getContext(), ActivityLocatorTrackList.class);
                getContext().startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                setBatterUI();
            } else if (msg.what == 2) {
                setEquipmentInfoUI();
            } else if (msg.what == 3) {
                setLocationTimeUI();
            }else if (msg.what == 4) {
                setComunicatetimeTimeUI();
            }else if (msg.what == 5) {
                setStatusUI();
            }else if (msg.what == 6) {
                setLocationModeUI();
            }else if (msg.what == 7) {
                setDerectionUI();
            }else if (msg.what == 8) {
                setSpeedUI();
            }else if (msg.what == 9) {
                setAderessUI();
            }
        }
    };

    public void sendMsgSetBatterry() {
        handler.sendEmptyMessage(1);
        Logger.d("sendMsgSetBatterry");
    }
    public void sendMsgSetEquipmentInfo() {
        handler.sendEmptyMessage(2);
        Logger.d("sendMsgSetEquipmentInfo");
    }
    public void sendLocationTime() {
        handler.sendEmptyMessage(3);
        Logger.d("sendLocationTime");
    }
    public void sendComunicateTime() {
        handler.sendEmptyMessage(4);
        Logger.d("sendComunicateTime");
    }
    public void sendStatus() {
        handler.sendEmptyMessage(5);
        Logger.d("sendStatus");
    }
    public void sendLocationMode() {
        handler.sendEmptyMessage(6);
        Logger.d("sendLocationMode");
    }
    public void sendDerection() {
        handler.sendEmptyMessage(7);
        Logger.d("sendDerection");
    }
    public void sendSpeed() {
        handler.sendEmptyMessage(8);
        Logger.d("sendSpeed");
    }
    public void sendAderess() {
        handler.sendEmptyMessage(9);
        Logger.d("sendAderess");
    }



    private void setBatterUI() {
        int batter = 70;
        battery_view.setPower(70);
        battery_percent.setText(batter + "％");
        Logger.d("setBatterUI");
    }
    private void setEquipmentInfoUI() {
        String info = "888888";
        txt_top_left.setText(info);
        Logger.d("setEquipmentInfoUI");
    }
    private void setLocationTimeUI() {
        String time = "2022年6月13日 12：20";
        txt_locating_time.setText(time);
        Logger.d("setLocationTimeUI");
    }
    private void setComunicatetimeTimeUI() {
        String time = "2022年6月13日 12：20";
        txt_communication_time.setText(time);
        Logger.d("setComunicatetimeTimeUI");
    }
    private void setStatusUI() {
        String status = "在线";
        txt_status.setText(status);
        Logger.d("setStatusUI");
    }
    private void setLocationModeUI() {
        String locationMode = "WIFI定位";
        txt_positioning_mode.setText(locationMode);
        Logger.d("setLocationModeUI");
    }
    private void setDerectionUI() {
        String derection = "东北";
        txt_direction.setText(derection);
        Logger.d("setDerectionUI");
    }
    private void setSpeedUI() {
        String speed = "5km/s";
        txt_speed.setText(speed);
        Logger.d("setSpeedUI");
    }
    private void setAderessUI() {
        String speed = "广东东莞";
        txt_aderess.setText(speed);
        Logger.d("setAderessUI");
    }

}
