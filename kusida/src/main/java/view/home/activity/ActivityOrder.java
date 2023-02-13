package view.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.client.proj.kusida.R;
import com.orhanobut.logger.Logger;

import view.home.commonview.CommonMoreItem;
import view.home.commonview.CommonTitletem;


public class ActivityOrder extends AllActivity {
    private CommonTitletem title;
    private CommonMoreItem order_work_mode;
    private CommonMoreItem order_vibration_alarm;
    private CommonMoreItem order_speed_alarm;
    private CommonMoreItem order_tamper_alarm;
    private CommonMoreItem order_auditory_function;
    private CommonMoreItem order_center_number;
    private CommonMoreItem order_static_alarm;
    private CommonMoreItem order_sim;
    private static final String TAG="ActivityOrder";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        initEvent();
    }
    private void initView(){
        title = findViewById(R.id.titleme);
        order_work_mode = findViewById(R.id.order_work_mode);
        order_vibration_alarm = findViewById(R.id.order_vibration_alarm);
        order_speed_alarm = findViewById(R.id.order_speed_alarm);
        order_tamper_alarm = findViewById(R.id.order_tamper_alarm);
        order_auditory_function = findViewById(R.id.order_auditory_function);
        order_center_number = findViewById(R.id.order_center_number);
        order_static_alarm = findViewById(R.id.order_static_alarm);
        order_sim = findViewById(R.id.order_sim);
    }
    private void initEvent(){
        title.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick back" );
                finish();
            }
        });
        title.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick right" );
                Intent intent=new Intent(ActivityOrder.this,ActivityDistributionRecord.class);
                startActivity(intent);
            }
        });
        order_work_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick order_work_mode" );
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrder.this,R.style.dialog);
                View view = View.inflate(ActivityOrder.this, R.layout.my_diolog, null);
                Button cancle=  view.findViewById(R.id.cancle);
                Button sure=  view.findViewById(R.id.sure);
                TextView time_switch=  view.findViewById(R.id.time_switch);
                TextView delay_time=  view.findViewById(R.id.delay_time);
                TextView time=  view.findViewById(R.id.time);
                ImageView arrow_top=  view.findViewById(R.id.arrow_top);
                ImageView arrow_bottom=  view.findViewById(R.id.arrow_bottom);
                builder.setView(view);
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();

                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.d( "onClick cancle" );
                        dialog.dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.d( "onClick sure" );
                        dialog.dismiss();
                    }
                });
                arrow_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.d( "onClick arrow_top" );
                    }
                });
                arrow_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.d( "onClick arrow_bottom" );
                    }
                });
                dialog.show();
            }
        });
        order_vibration_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick order_vibration_alarm" );
            }
        });
        order_speed_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick order_speed_alarm" );
            }
        });
        order_tamper_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick order_tamper_alarm" );
            }
        });
        order_auditory_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick order_auditory_function" );
            }
        });
        order_center_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick order_center_number" );
            }
        });
        order_static_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick order_static_alarm" );
            }
        });
        order_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logger.d( "onClick order_sim" );
            }
        });
    }
}
