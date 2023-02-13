package view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;

import common.GlobalContext;
import common.PHeadHttp;
import model.ManagerLoginReg;
import model.advertising.DataAdvertising;
import model.loginreg.DataUser;

/**
 * 广告页
 */

public class ActivityAdvertising extends ActivityBase {
    private SmartImageView image_advertising;
    private static boolean timeComplete = false;//计时完成
    public static boolean isActivityAdvertisingCome;
    private static boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        image_advertising = (SmartImageView) findViewById(R.id.image_advertising);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        String imgUrl = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("adventUrl");
        if (imgUrl != null) {
            image_advertising.setImageUrl(imgUrl);
//            Uri advent = ManagerSkinsBK.getInstance().getAdventUri();
//            if (advent != null) {//有保存过的就用
//            } else {
//            }
        }else{
            image_advertising.setImageResource(R.drawable.activity_advent);
        }
    }

    private void exitThis() {
        Intent intent = new Intent();
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        String token = PHeadHttp.getToken();
        if (user == null || user.userId == 0 || token == null || token.length() == 0) {
            intent.setClass(this, ActivityLogin.class);//未登录
        } else {
            ActivityKulalaMain.GestureNeed = true;
            intent.setClass(this, ActivityKulalaMain.class);//已登录
        }
        this.startActivity(intent);
        this.finish();
    }

    @Override
    protected void initEvents() {
        new TimeDelayTask().runTask(3000, new TimeDelayTask.OnTimeEndListener() {
            @Override
            public void onTimeEnd() {
                timeComplete = true;
                if (!isClick) {
                    exitThis();
                }
            }
        });

        image_advertising.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (!timeComplete) {
                    String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("adventInfo");
                    JsonObject obj = ODBHelper.convertJsonObject(result);
                    if (obj != null) {
                        DataAdvertising dataShakeTips = DataAdvertising.fromJsonObject(obj);
                        if (dataShakeTips != null) {
                            if (dataShakeTips.type == 1 && !TextUtils.isEmpty(dataShakeTips.jumpUrl)) {
                                isClick = true;
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString(ActivityWeb.TITLE_NAME, "");
                                bundle.putString(ActivityWeb.HTTP_ADDRESS, dataShakeTips.jumpUrl);
                                intent.putExtras(bundle);
                                intent.setClass(ActivityAdvertising.this, ActivityWeb.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                isActivityAdvertisingCome = true;
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    protected void popView(int resId) {

    }
}
