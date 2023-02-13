package view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.client.proj.kusida.R;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.toast.ToastTxt;

import common.GlobalContext;
import ctrl.OCtrlCommon;
import model.DataDisPlay;
import model.ManagerDisPlay;
import view.view4me.ResizableImageView;
import view.view4me.set.ClipTitleMeSet;

/**
 * Intent intent = new Intent();
 * Bundle bundle = new Bundle();
 * bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
 * String address;
 * try {
 * ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
 * address = "http://api.91kulala.com/kulala/protocol/protocol.jsp?cid="+appInfo.metaData.getInt("cid");
 * } catch (PackageManager.NameNotFoundException e) {
 * address = "http://api.91kulala.com/kulala/protocol/protocol.jsp?cid="+0;
 * }
 * bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
 * intent.putExtras(bundle);
 * intent.setClass(getContext(), ActivityWeb.class);
 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 * getContext().startActivity(intent);
 * <p>
 * Bundle bundle = this.getIntent().getExtras();
 * String str=bundle.getString("USERNAME");
 */
public class ActivityWebDisPla extends ActivityBase {
    public static String TITLE_NAME = "TITLE_NAME";
    public static String HTTP_ADDRESS = "HTTP_ADDRESS";
    private ClipTitleMeSet title_head;
    private ResizableImageView imageview;
    private String http_address_use;
    private boolean isComFromAd;//从广告页进来
    private TextView text;

    public ActivityWebDisPla() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_display);

        title_head =  findViewById(R.id.title_head);
        imageview= findViewById(R.id.imageview);
        text= findViewById(R.id.text);
        Bundle bundle = this.getIntent().getExtras();
        String title_name = bundle.getString(TITLE_NAME);
        http_address_use = bundle.getString(HTTP_ADDRESS);
        title_head.txt_title_show.setText(title_name);

        DataDisPlay disPlay= ManagerDisPlay.getInstance().displayInfo;
        if(disPlay==null)return;
        if(!isDestroy((Activity)this)&&!TextUtils.isEmpty(disPlay.url)){
            Glide.with(GlobalContext.getContext())
                    .load(disPlay.url)
                    .into(imageview);

        }
//                    if(disPlay.id!=0){
//                OCtrlCommon.getInstance().cmmd_2302_push_Display(disPlay.id);
//            }
        initViews();
        initEvents();
    }
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity== null || mActivity.isFinishing() || mActivity.isDestroyed()) {
            return true;
        } else {
            return false;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }


    protected void initViews() {

    }
    public static void jumpAlipay(Context context,String uri) {
        try {
//            String uri = "alipayqr://platformapi/startapp?appId=2021002146620507";
//                    + "&page=pages/index/index?userId=123456"//页面参数
//                    + "&query=itemId=005007";//启动参数
            Intent intent = Intent.parseUri(uri, Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
        } catch (Exception e) {
            new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("支付宝跳转失败").quicklyShow();
            e.printStackTrace();
        }
    }

    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (ActivityAdvertising.isActivityAdvertisingCome) {
                    Intent intent = new Intent();
                    ActivityKulalaMain.GestureNeed = true;
                    intent.setClass(ActivityWebDisPla.this, ActivityKulalaMain.class);//已登录
                    startActivity(intent);
                }
                ActivityWebDisPla.this.finish();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                        .withTitle("提示")
                        .withInfo("确定要去支付宝认证吗？")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm){
                                    if (checkAliPayInstalled(ActivityWebDisPla.this)) {
                                        DataDisPlay disPlay= ManagerDisPlay.getInstance().displayInfo;
                                        if(disPlay!=null&&disPlay.id!=0){
                                            OCtrlCommon.getInstance().cmmd_2302_push_Display(disPlay.id);
                                        }
                                        if(!TextUtils.isEmpty(disPlay.content)){
                                            jumpAlipay(ActivityWebDisPla.this,disPlay.content);
                                        }
                                    } else {
                                        new ToastTxt(GlobalContext.getCurrentActivity(), null, false).withInfo("请先安装支付宝").quicklyShow();
                                    }
                                }
                            }
                        }).show();
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