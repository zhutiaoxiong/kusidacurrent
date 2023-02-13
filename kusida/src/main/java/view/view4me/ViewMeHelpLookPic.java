package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

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

public class ViewMeHelpLookPic extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private ImageView iv;
    public ViewMeHelpLookPic(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_help_lookpic, this, true);
        title_head = findViewById(R.id.title_head);
        iv = findViewById(R.id.iv);
        initViews();
        initEvents();
    }
    @Override
    public void initViews() {
       switch (ManagerPublicData.meHelpNumber){
           case "1":
               title_head.txt_title_show.setText("控车页面功能介绍");
               iv.setImageResource(R.drawable.me_help_01_gongnengjieshao);
               break;
           case "2":
               title_head.txt_title_show.setText("如何分享APP钥匙 (APP借车)");
               iv.setImageResource(R.drawable.me_help_03_app_jieche);
               break;
           case "3":
               title_head.txt_title_show.setText("如何分享微信钥匙 (微信借车)");
               iv.setImageResource(R.drawable.me_help_02_weixinjieche);
               break;
           case "4":
               title_head.txt_title_show.setText("如何给界面车辆换肤");
               iv.setImageResource(R.drawable.me_help_05_huanfu);
               break;
           case "5":
               title_head.txt_title_show.setText("如何关闭开关锁时车辆喇叭声音");
               iv.setImageResource(R.drawable.me_help_04_guanbilabashengyin);
               break;
           case "6":
               title_head.txt_title_show.setText("如何关闭APP提示音");
               iv.setImageResource(R.drawable.me_help_06_guanbitishiying);
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
