package view.view4me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.alipay.sdk.app.PayTask;
import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.toast.ToastResult;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.text.DecimalFormat;
import java.util.List;

import common.GlobalContext;
import common.global.OWXShare;
import ctrl.OCtrlCar;
import ctrl.OCtrlCommon;
import ctrl.OCtrlRegLogin;
import model.ManagerCarList;
import model.ManagerCommon;
import model.carlist.DataCarInfo;
import model.common.DataPayWay;
import view.clip.ClipLineBtnInptxt;
import view.clip.ClipPopChooseStr;

import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import view.clip.me.ClipPayItem;
import view.view4me.set.ClipTitleMeSet;

public class ViewPayFriend extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private ClipLineBtnInptxt txt_carname, txt_username;
    private ClipPayItem pay1, pay2, pay3, pay4, pay5, pay6;
    private Button btn_confirm;

    private DataPayWay selectWay;
    private int selectPay = -1;
    private DataCarInfo      selectCar;
    private List<DataPayWay> payWayList;
    protected MyHandler handler = new MyHandler();

    public ViewPayFriend(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_pay_firend, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_carname = (ClipLineBtnInptxt) findViewById(R.id.txt_carname);
        txt_username = (ClipLineBtnInptxt) findViewById(R.id.txt_username);
        pay1 = (ClipPayItem) findViewById(R.id.pay1);
        pay2 = (ClipPayItem) findViewById(R.id.pay2);
        pay3 = (ClipPayItem) findViewById(R.id.pay3);
        pay4 = (ClipPayItem) findViewById(R.id.pay4);
        pay5 = (ClipPayItem) findViewById(R.id.pay5);
        pay6 = (ClipPayItem) findViewById(R.id.pay6);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.PAY_CHECKPAY_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.PAY_WX_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.PAY_WX_SUCESS, this);
        ODispatcher.addEventListener(OEventName.CAR_LIST_OTHER_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.HTTP_CONN_ERROR,this);
    }

    @Override
    public void initViews() {
        txt_username.img_right.setVisibility(View.INVISIBLE);
        txt_username.openInput(InputType.TYPE_CLASS_PHONE, 11);
        handleChangeData();
    }

    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_pay);
            }
        });
        txt_username.img_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                txt_username.setText("");
            }
        });
        txt_username.txt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(txt_username.txt_input.getText().length()>0){
                    txt_username.img_right.setVisibility(View.VISIBLE);
                }else{
                    txt_username.img_right.setVisibility(View.INVISIBLE);
                }
            }
        });
        txt_carname.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String phone = txt_username.getText().toString();
                if (phone.length() < 11) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getContext().getResources().getString(R.string.please_enter_the_complete_phone_number));
                } else {
                    OCtrlCar.getInstance().ccmd1232_getCarListFromPhone(phone);
                }
            }
        });
        pay1.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
//				setPayUpDefault();
//				pay1.setIsChecked(true);
                setPayUpSelect(1);
                selectWay = payWayList.get(0);
            }
        });
        pay2.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setPayUpSelect(2);
                selectWay = payWayList.get(1);
            }
        });
        pay3.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setPayUpSelect(3);
                selectWay = payWayList.get(2);
            }
        });
        pay4.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setPayUpSelect(4);
                selectWay = payWayList.get(3);
            }
        });
        pay5.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setPayDownSelect(5);
                selectPay = 1;
            }
        });
        pay6.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setPayDownSelect(6);
                selectPay = 2;
            }
        });
        // confirm
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (selectCar == null || selectWay == null || selectPay == -1) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.no_available_vehicles));
                    return;
                }
                if (selectPay == 1) {
                    OCtrlCommon.getInstance().ccmd1001_wxpay(selectWay.fee, selectCar.ide, selectWay.time);
                } else if (selectPay == 2) {
                    OCtrlCommon.getInstance().ccmd1002_checkpay(selectWay.fee, selectCar.ide, selectWay.time);
                }
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.PAY_CHECKPAY_RESULTBACK)) {
            final String payStr = (String) paramObj;
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(GlobalContext.getCurrentActivity());
                    // 调用支付接口，获取支付结果
                    // resultStatus={9000};memo={};result={_input_charset="utf-8"&body="酷斯达数字车钥匙充值服务"&notify_url="http%3A%2F%2F120.27.137.20%3A8099%2FpayGateway%2Fpayway%2Falipay%2Fsdk%2Fnotify_url.jsp"&out_trade_no="20913"&partner="2088221499108097"&payment_type="1"&seller_id="3098057476@qq.com"&service="mobile.securitypay.pay"&subject="充值"&total_fee="0.01"&success="true"&sign_type="RSA"&sign="dPg3i9ShlEOMd3m0O1BEglUV+rZ6Ga7HbFXTBkHAkVFfuILDSCSWWzMU5yJ1UYvdgQuZWZyuq+ZJFbD62api9QatuP00BHq5qrkigAbRVi+CQbAp06Bwa2gPiph1RdO43zW+JeGarLh6+1ps4862nusgAkvLKLJdDhdyOtUI2zY="}
                    String   result    = alipay.pay(payStr, true);
                    String[] resultArr = result.split(";");
                    if (resultArr != null && resultArr.length > 1 && "resultStatus={9000}".equals(resultArr[0])) {
//                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "支付宝支付成功!");
                        handleShowResult(getContext().getResources().getString(R.string.top_up_success),true);
                    } else if ("resultStatus={4000}".equals(resultArr[0])) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getContext().getResources().getString(R.string.you_do_not_install_pay_treasure_or_cancelled_payment));
                    }else{
//                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "支付宝支付失败!");
                        handleShowResult(getContext().getResources().getString(R.string.top_up_failure),false);
                    }
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else if (eventName.equals(OEventName.PAY_WX_RESULTBACK)) {
            if (!isWeixinAvilible(getContext())) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getContext().getResources().getString(R.string.please_install_wechat_software));
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
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getContext().getResources().getString(R.string.pay_not_success));
                e.printStackTrace();
            }
        } else if (eventName.equals(OEventName.CAR_LIST_OTHER_RESULTBACK)) {
            String[] arr = ManagerCarList.getInstance().getCarNameListOther();//只有激活车可选
            handleShowCarList(arr);
        }else if (eventName.equals(OEventName.HTTP_CONN_ERROR)) {
            int protocol = (Integer) paramObj;
            if (protocol == 1101 || protocol == 1102 || protocol == 1232) {
                handleShowResult(getContext().getResources().getString(R.string.network_bad),false);
            }
        } else if (eventName.equals(OEventName.PAY_WX_SUCESS)) {
            int errCode = (Integer) paramObj;
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    handleShowResult(getContext().getResources().getString(R.string.successful_payment), true);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    handleShowResult(getContext().getResources().getString(R.string.cancel_the_payment), false);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    handleShowResult(getContext().getResources().getString(R.string.refuse_to_pay), false);
                    break;
            }
        }
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo>    pinfo          = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
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
    public void callback(String key, Object value) {
        if (key.equals("exit")) {
            OCtrlRegLogin.getInstance().ccmd1109_exitlogin();
        } else if (key.equals("pickCar")) {
            String carname = (String) value;
            selectCar = ManagerCarList.getInstance().getCarByNameOther(carname);
            handleChangeData();
        }
        super.callback(key, value);
    }

    private void setPayUpSelect(int pos) {
        if (pos == 1) pay1.setIsChecked(true);
        else pay1.setIsChecked(false);
        if (pos == 2) pay2.setIsChecked(true);
        else pay2.setIsChecked(false);
        if (pos == 3) pay3.setIsChecked(true);
        else pay3.setIsChecked(false);
        if (pos == 4) pay4.setIsChecked(true);
        else pay4.setIsChecked(false);
    }

    private void setPayDownSelect(int pos) {
        if (pos == 5) pay5.setIsChecked(true);
        else pay5.setIsChecked(false);
        if (pos == 6) pay6.setIsChecked(true);
        else pay6.setIsChecked(false);
    }

    @Override
    public void invalidateUI() {
        if (selectCar != null) {
            txt_carname.setText(selectCar.num);
        }
        pay1.setVisibility(INVISIBLE);
        pay2.setVisibility(INVISIBLE);
        pay3.setVisibility(INVISIBLE);
        pay4.setVisibility(INVISIBLE);
        payWayList = ManagerCommon.getInstance().payWayList;
        if (payWayList == null)
            return;
        DecimalFormat df = new DecimalFormat("##0.00");
        if (payWayList.size() >= 1) {
            pay1.setFee(df.format(payWayList.get(0).fee) + getContext().getResources().getString(R.string.yuan));
            pay1.setTime(payWayList.get(0).timeStr);
            pay1.setVisibility(VISIBLE);
        }
        if (payWayList.size() >= 2) {
            pay2.setFee(df.format(payWayList.get(1).fee) + getContext().getResources().getString(R.string.yuan));
            pay2.setTime(payWayList.get(1).timeStr);
            pay2.setVisibility(VISIBLE);
        }
        if (payWayList.size() >= 3) {
            pay3.setFee(df.format(payWayList.get(2).fee) + getContext().getResources().getString(R.string.yuan));
            pay3.setTime(payWayList.get(2).timeStr);
            pay3.setVisibility(VISIBLE);
        }
        if (payWayList.size() >= 4) {
            pay4.setFee(df.format(payWayList.get(3).fee) +getContext().getResources().getString(R.string.yuan));
            pay4.setTime(payWayList.get(3).timeStr);
            pay4.setVisibility(VISIBLE);
        }

        //default data
        pay1.setIsChecked(true);
        pay6.setIsChecked(true);
        selectWay = payWayList.get(0);
        selectPay = 2;
    }

    public void handleShowCarList(String[] carList) {
        Message message = new Message();
        message.what = 325;
        message.obj = carList;
        handler.sendMessage(message);
    }
    public void handleShowResult(String result,boolean resultOK) {
        Message message = new Message();
        message.what = 326;
        message.arg1 = resultOK ? 1 : 0;
        message.obj = result;
        handler.sendMessage(message);
    }

    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 325:
                    String[] carList = (String[])msg.obj;
                    ClipPopChooseStr.getInstance().show(title_head, carList, getContext().getResources().getString(R.string.please_select_a_vehicle), "pickCar", ViewPayFriend.this);
                    break;
                case 326:
                    boolean resultOK = (msg.arg1 == 1) ? true : false;
                    String result =(String) msg.obj;
                    ToastResult.getInstance().show(title_head,resultOK,result);
                    break;
            }
        }
    }
}
