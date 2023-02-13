package view.view4me.shake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.titlehead.ClipTitleHead;

import view.ActivityWeb;


/**
 * Created by qq522414074 on 2017/10/25.
 */

public class ViewSwitchShakeSet extends RelativeLayoutBase {
    private ClipTitleHead title_head;
    private RelativeLayout layout_lock_app, layout_power, layout_app_permision;
    private String jumpUrl;

    public ViewSwitchShakeSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_me_switch_set, this, true);
        title_head = (ClipTitleHead) findViewById(R.id.title_head);
        layout_lock_app = (RelativeLayout) findViewById(R.id.layout_lock_app);
        layout_power = (RelativeLayout) findViewById(R.id.layout_power);
        layout_app_permision = (RelativeLayout) findViewById(R.id.layout_app_permision);
        initEvents();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_shake);
            }
        });
        layout_lock_app.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                jumpUrl="http://120.27.137.20:8090/kulala/manageSetting/lockApp.jsp";
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "更多锁定APP方式");
                bundle.putString(ActivityWeb.HTTP_ADDRESS, jumpUrl);
                intent.putExtras(bundle);
                intent.setClass(getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        layout_power.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                jumpUrl="http://120.27.137.20:8090/kulala/manageSetting/batteryManage.jsp";
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "更多电池/电量管理设置方式");
                bundle.putString(ActivityWeb.HTTP_ADDRESS, jumpUrl);
                intent.putExtras(bundle);
                intent.setClass(getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        layout_app_permision.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
               jumpUrl="http://120.27.137.20:8090/kulala/manageSetting/applicationManage.jsp";
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "更多应用/权限管理设置方式");
                bundle.putString(ActivityWeb.HTTP_ADDRESS, jumpUrl);
                intent.putExtras(bundle);
                intent.setClass(getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

    }

    @Override
    protected void invalidateUI() {

    }
}
