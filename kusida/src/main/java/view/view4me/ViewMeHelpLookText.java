package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;

import model.ManagerPublicData;
import view.view4me.set.ClipTitleMeSet;


/**
 * Created by qq522414074 on 2017/1/11.
 */

public class ViewMeHelpLookText extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private TextView tv;
    public ViewMeHelpLookText(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_help_looktext, this, true);
        title_head = findViewById(R.id.title_head);
        tv = findViewById(R.id.tv);
        initViews();
        initEvents();
    }
    @Override
    public void initViews() {
       switch (ManagerPublicData.meHelpNumber){
           case "7":
               title_head.txt_title_show.setText("什么是启动保护");
               tv.setText("启动保护功能打开时，熄火或开锁后且APP没连设备蓝牙的情况下，系统会在2分钟后禁止车辆启动，这时需要再操作开锁或APP连上设备蓝牙才允许车辆启动。");
               break;
           case "8":
               title_head.txt_title_show.setText("什么是洗护模式");
               tv.setText("车辆维修洗护时，请打开此开关，将阻止所有APP客户端下发指令到车直至关闭洗护模式，保障维修洗护安全。");
               break;
           case "9":
               title_head.txt_title_show.setText("如何添加NFC");
               tv.setText("点击【应用】进入【NFC智卡】栏目，按菜单内指导视频操作。");
               break;
           case "10":
               title_head.txt_title_show.setText("什么是摇一摇");
               tv.setText("这是一个让用户靠近车辆后，无需打开APP即可快速操作开关锁的功能，功能打开并设置后，可以只带手机靠近车辆，通过“摇一摇”手机实现快速开锁或关锁，如需使用，请进入【应用】-【摇一摇】菜单进行设置。");
               break;
           case "11":
               title_head.txt_title_show.setText("什么是无匙进入");
               tv.setText("无匙进入分“靠近开”和“离开锁”两个开关，打开开关并设置后，可以实现只带手机靠近车辆自动开锁，离开车辆自动关锁，系统默认关闭，如需使用，请靠近车辆且APP连接设备蓝牙的情况下进入【应用】-【无匙进入】菜单进行设置。");
               break;
           case "12":
               title_head.txt_title_show.setText("什么是行车落锁");
               tv.setText("行车落锁功能打开后，当时速超过20车锁会自动锁闭，且熄火后会自动开锁，如需打开此功能请进入【应用】-【工程设置】菜单进行设置。");
               break;
           case "13":
               title_head.txt_title_show.setText("什么是锁车升窗");
               tv.setText("锁车升窗功能打开后，每次熄火且开门后，当关好所有车门锁车时，车窗会自动升起，此功能非所有车型标配，需原车支持的情况下如需使用，请进入【应用】-【工程设置】菜单进行设置。");
               break;
           case "14":
               title_head.txt_title_show.setText("如何添加物理遥控器");
               tv.setText("添加物理遥控器需产品硬件支持，硬件支持的前提下如需添加，请进入【应用】-【遥控器】菜单进行操作。");
               break;
       }
    }

    @Override
    public void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_help);
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }

    // =====================================================
    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

}
