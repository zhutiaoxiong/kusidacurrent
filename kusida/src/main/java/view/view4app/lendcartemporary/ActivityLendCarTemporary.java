package view.view4app.lendcartemporary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.client.proj.kusida.wxapi.WXEntryActivity;
import com.google.gson.JsonArray;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.picker.WheelNumView;
import com.tencent.tauth.TencentCommon;

import java.util.ArrayList;

import common.global.OWXShare;
import ctrl.OCtrlAuthorization;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.ManagerPublicData;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;

/**
 * 临时借车主页面
 */
public class ActivityLendCarTemporary extends Activity implements OEventObject {
    private ClipTitleMeSet title_head;
    private TextView txt_tips_success, txt_tips_cancel;
    private TextView btn_confirm;
    private WheelNumView wheel_num;
    private RelativeLayout lin_img_share,lin_wheel;
    private ImageView img_share_wechat, img_share_firend, img_share_qq;
    private int selectNum = 2;
    private ImageView code_layout;
    private ImageView iv_code;
    private TextView txt_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_car_temporary);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_tips_success = (TextView) findViewById(R.id.txt_tips_success);
        txt_tips_cancel = (TextView) findViewById(R.id.txt_tips_cancel);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        wheel_num = (WheelNumView) findViewById(R.id.wheel_num);
        lin_wheel = (RelativeLayout) findViewById(R.id.lin_wheel);
        lin_img_share = (RelativeLayout) findViewById(R.id.lin_img_share);
        img_share_wechat = (ImageView) findViewById(R.id.img_share_wechat);
        img_share_firend = (ImageView) findViewById(R.id.img_share_firend);
        img_share_qq = (ImageView) findViewById(R.id.img_share_qq);
        code_layout =  findViewById(R.id.code_layout);
        iv_code =  findViewById(R.id.iv_code);
        txt_code =  findViewById(R.id.txt_code);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            data.add("" + i);
        }
        wheel_num.setData(data);
        wheel_num.setSelected(selectNum - 2);
        wheel_num.setOnSelectListener(new WheelNumView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectNum = Integer.parseInt(text);
//                SoundHelper.playSoundPool(getContext(),R.raw.voice_move_num);
            }
        });
        selectNum = 1;//默认选1
        txt_tips_success.setVisibility(View.INVISIBLE);
        txt_tips_cancel.setVisibility(View.INVISIBLE);
        lin_img_share.setVisibility(View.INVISIBLE);
        changeDisplay();
        initEvents();
        ODispatcher.addEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK,this);
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USER_STOPED,this);
        ODispatcher.addEventListener(OEventName.APPLECODE_RESULTBACK,this);
    }

    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK,this);
        ODispatcher.removeEventListener(OEventName.AUTHORIZATION_USER_STOPED,this);
        ODispatcher.removeEventListener(OEventName.APPLECODE_RESULTBACK,this);
        super.onDestroy();
    }

    //状态变了，修改显示
    private void changeDisplay() {
        DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
        if (currentCar.authorityEndTime1<System.currentTimeMillis()) {//未授时的显示
            btn_confirm.setText("生成钥匙");
        } else {//已授时的显示
            OCtrlAuthorization.getInstance().ccmd1601_getAuthCode(currentCar.ide);
            lin_img_share.setVisibility(View.VISIBLE);
            btn_confirm.setText("取消钥匙");
            lin_wheel.setVisibility(View.INVISIBLE);

            String showTime;
            long ms = currentCar.authorityEndTime1-System.currentTimeMillis();
            double msss = Double.valueOf(""+ms);
            if(msss < 60*60*1000L){
                showTime = "<font color='#0066FF'>"+Math.round(msss/60/1000)+"</font>分钟";
            }else{
                showTime = "<font color='#0066FF'>"+(int)Math.ceil(msss/60/60/1000)+"</font>小时";
            }
            String str="当前临时借车还剩 "+showTime;
            txt_tips_cancel.setText(android.text.Html.fromHtml(str));
            txt_tips_cancel.setVisibility(View.VISIBLE);
        }
    }

    private void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                finish();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String txt = btn_confirm.getText().toString();
                if ("生成钥匙".equals(txt)) {
                    DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                    OCtrlAuthorization.getInstance().ccmd1206_giveauthor(currentCar.ide, new JsonArray(), "", System.currentTimeMillis(), System.currentTimeMillis() + selectNum * 60 * 60 * 1000);
                }else if ("取消钥匙".equals(txt)) {
                    long carId = ManagerCarList.getInstance().getCurrentCarID();
                    long userId = ManagerLoginReg.getInstance().getCurrentUser().userId;
                    OCtrlAuthorization.getInstance().ccmd1207_stopauthor(userId, carId, 1);
                }
            }
        });
        img_share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(1);
            }
        });
        img_share_firend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showShare(2);
                if(!TextUtils.isEmpty(ManagerPublicData.authCode)){
                    OCtrlAuthorization.getInstance().ccmd1602_getWXerweiCode(ManagerPublicData.authCode);
                }
            }
        });
        img_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(3);

            }
        });
        code_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code_layout.setVisibility(View.INVISIBLE);
                iv_code.setVisibility(View.INVISIBLE);
                txt_code.setVisibility(View.INVISIBLE);
            }
        });
    }


    /**
     * 收回钥匙
     * (currentCar.isActive == 0)才可执行
     */
    private void stopKey() {

//        long carId = ManagerCarList.getInstance().getCurrentCarID();
//        if (carId == 0) return;
//        OCtrlAuthorization.getInstance().ccmd1207_stopauthor(user.userId, carId, 1);
    }

    private void showShare(int pos) {
            String authCode = ManagerPublicData.authCode;//临时授权码
            String shareUrl = "http://kc.kcmoco.com/share/borrow?authCode=" + authCode;
        if (pos == 1) {
            WXEntryActivity.NEED_WXSHARE_RESULT = true;
            OWXShare.ShareWXSmallApp("有辆车已经借给您了", "请点击了解详细内容", shareUrl,authCode);
            ManagerPublicData.isNotPopGusture = true;

//            WXEntryActivity.NEED_WXSHARE_RESULT = true;
//            OWXShare.ShareFriendURL("有辆车已经借给您了", "请点击了解详细内容", shareUrl);
//            ManagerPublicData.isNotPopGusture = true;
        } else if (pos == 2) {
            OWXShare.ShareTimeLineURL("有辆车已经借给您了", "请点击了解详细内容", shareUrl);
            ManagerPublicData.isNotPopGusture = true;
        } else if (pos == 3) {
            TencentCommon.toTencent(ActivityLendCarTemporary.this, "有辆车已经借给您了", "请点击了解详细内容", shareUrl, 0, "");
            ManagerPublicData.isNotPopGusture = true;
        }
    }

    @Override
    public void receiveEvent(final String s, Object o) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                if(s.equals(OEventName.AUTHOR_CODRIVER_RESULTBACK)){//生成key OK
                    btn_confirm.setText("取消钥匙");
                    String showTime;
                    long ms = currentCar.authorityEndTime1-System.currentTimeMillis();
                    double msss = Double.valueOf(""+ms);
                    if(msss < 60*60*1000L){
                        showTime = "<font color='#0066FF'>"+Math.round(msss/60/1000)+"</font>分钟";
                    }else{
                        showTime = "<font color='#0066FF'>"+(int)Math.ceil(msss/60/60/1000)+"</font>小时";
                    }
                    String str="您的爱车 "+"<font color='#0066FF'>"+currentCar.num+"</font>"+" 现在已经产生了虚拟钥匙，"+showTime+"后过期自动失效。对方收到后只能用手机开关锁。同时也要提醒对方不要把虚拟钥匙发送给其他人使用。" ;
                    txt_tips_success.setText(android.text.Html.fromHtml(str));
                    txt_tips_success.setVisibility(View.VISIBLE);
                    lin_img_share.setVisibility(View.VISIBLE);
                }else if(s.equals(OEventName.AUTHORIZATION_USER_STOPED)){//取消key OK
                    txt_tips_success.setVisibility(View.INVISIBLE);
                    txt_tips_cancel.setVisibility(View.INVISIBLE);
                    lin_img_share.setVisibility(View.INVISIBLE);
                    lin_wheel.setVisibility(View.VISIBLE);
                    btn_confirm.setText("生成钥匙");
                }else if(s.equals(OEventName.APPLECODE_RESULTBACK)){
                    code_layout.setVisibility(View.VISIBLE);
                    iv_code.setVisibility(View.VISIBLE);
                    txt_code.setVisibility(View.VISIBLE);
                    //Base64编码地址(地址太长，省略)
                    String base64String = ManagerPublicData.appleCode;

                    //将Base64编码字符串解码成Bitmap

                    byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    //设置ImageView图片

                    iv_code.setImageBitmap(decodedByte);
                }
            }
        });
    }
}
