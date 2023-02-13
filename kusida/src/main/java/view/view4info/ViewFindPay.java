package view.view4info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.List;

import common.GlobalContext;
import common.global.OWXShare;
import ctrl.OCtrlCommon;
import model.ManagerCommon;
import com.kulala.staticsview.titlehead.ClipTitleHead;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
/**
 * 汽车皮肤支付页面
 */

/**
 * Created by qq522414074 on 2016/9/1.
 */
public class ViewFindPay extends LinearLayoutBase {
    private ClipTitleHead titleHead;       //标题栏
    private LinearLayout aliPay,wechatPay; //微信支付，支付宝支付的两个linearlayout
    private int payway;                   // 通过什么方式支付
    public static float PAY_MONEY;//其它调用此视图必设
    public static int SKIN_ID;//其它调用此视图必设
    public ViewFindPay(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_find_pay_for_skinderm,this,true);
        titleHead=(ClipTitleHead) findViewById(R.id.title_head);
        aliPay=(LinearLayout)findViewById(R.id.view_find_pay_alipay);
        wechatPay=(LinearLayout)findViewById(R.id.view_find_pay_wechat);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SKIN_PAY_RESULTBACK,this);
    }

    @Override
    protected void initViews() {

    }

    /**
     * 跳转到支付宝或者微信需要传三个参数，
     fee  float 充值的费用 必填，必须大于0，表示冲了多少钱（需要从上层拿）
     carTypeId int 车型id 必填，具体见个性装扮对象中的车型对象中的ide （需要从上层拿）
     type int 支付类型 必填，1：微信，2：支付宝
     */
    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_find_car_dressup);
            }
        });
        aliPay.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                payway=2;
                OCtrlCommon.getInstance().ccmd1004_pifupay(PAY_MONEY,SKIN_ID,payway);

            }
        });
        wechatPay.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                payway=1;
                OCtrlCommon.getInstance().ccmd1004_pifupay(PAY_MONEY,SKIN_ID,payway);
            }
        });

    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    public void receiveEvent(String s, Object o) {
       if(s.equals("SKIN_PAY_RESULTBACK")){
            if(payway==1){
                JsonObject obj=(JsonObject) o;
                JsonObject tenpayParam = OJsonGet.getJsonObject(obj, "tenpayParam");
                ManagerCommon.getInstance().saveWxPay(tenpayParam);
                if(! isWeixinAvilible(getContext())){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_install_wechat_software));
                    return;
                }
                IWXAPI api;
                // 实例化
                try {
                    api = WXAPIFactory.createWXAPI(getContext(), OWXShare.getAppIdWX());
                    api.registerApp(OWXShare.getAppIdWX());
                    PayReq request = new PayReq();
                    request.appId = OWXShare.getAppIdWX();
                    request.partnerId = ManagerCommon.wxpayInfo.partnerid;
                    request.prepayId = ManagerCommon.wxpayInfo.prepayId;
                    request.packageValue = ManagerCommon.wxpayInfo.packageValue;
                    request.nonceStr = ManagerCommon.wxpayInfo.nonceStr;
                    request.timeStamp = ManagerCommon.wxpayInfo.timestamp;
                    request.sign = ManagerCommon.wxpayInfo.sign;
                    api.sendReq(request);
                } catch (PackageManager.NameNotFoundException e) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getContext().getResources().getString(R.string.viewfindpay_no_wechatpay));
                    e.printStackTrace();
                }
            }else if(payway==2){
                JsonObject obj=(JsonObject) o;
                final String paystr = OJsonGet.getString(obj, "paystr");
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(GlobalContext.getCurrentActivity());
                        // 调用支付接口，获取支付结果
                        // resultStatus={9000};memo={};result={_input_charset="utf-8"&body="酷斯达数字车钥匙充值服务"&notify_url="http%3A%2F%2F120.27.137.20%3A8099%2FpayGateway%2Fpayway%2Falipay%2Fsdk%2Fnotify_url.jsp"&out_trade_no="20913"&partner="2088221499108097"&payment_type="1"&seller_id="3098057476@qq.com"&service="mobile.securitypay.pay"&subject="充值"&total_fee="0.01"&success="true"&sign_type="RSA"&sign="dPg3i9ShlEOMd3m0O1BEglUV+rZ6Ga7HbFXTBkHAkVFfuILDSCSWWzMU5yJ1UYvdgQuZWZyuq+ZJFbD62api9QatuP00BHq5qrkigAbRVi+CQbAp06Bwa2gPiph1RdO43zW+JeGarLh6+1ps4862nusgAkvLKLJdDhdyOtUI2zY="}
                        String result = alipay.pay(paystr, true);
                        String[] resultArr = result.split(";");
                        if (resultArr != null && resultArr.length > 1 && "resultStatus={9000}".equals(resultArr[0])) {
                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.alipay_pay_success));
                        }else if("resultStatus={4000}".equals(resultArr[0])){
                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.you_do_not_install_pay_treasure_or_cancelled_payment));
                        }
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
            }

    }
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.SKIN_PAY_RESULTBACK,this);
        super.onDetachedFromWindow();
    }
}
