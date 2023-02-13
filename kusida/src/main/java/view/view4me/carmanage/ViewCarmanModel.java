package view.view4me.carmanage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastInput;

import com.zxing.activity.CaptureActivity;

import common.GlobalContext;
import ctrl.OCtrlCar;
import ctrl.OCtrlRegLogin;
import model.carlist.DataCarInfo;
import view.ActivityWeb;
import view.find.BasicParamCheckPassWord;
import view.view4me.set.ClipTitleMeSet;

/**
 * 车辆模组，进入此页，先设data
 */
public class ViewCarmanModel extends RelativeLayoutBase {
    public static DataCarInfo    data;
    private ClipTitleMeSet title_head;
    private       RelativeLayout lin_input;
    private       LinearLayout   lin_agreement;
    private       TextView       txt_model_state, txt_agreement, txt_intro;
    private EditText  input_code;
    private ImageView img_scan, img_check;
    private Button btn_confirm;
    protected MyHandler handler = new MyHandler();
    public ViewCarmanModel(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.carman_model_set, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        lin_input = (RelativeLayout) findViewById(R.id.lin_input);
        lin_agreement = (LinearLayout) findViewById(R.id.lin_agreement);
        txt_model_state = (TextView) findViewById(R.id.txt_model_state);
        txt_agreement = (TextView) findViewById(R.id.txt_agreement);
        txt_intro = (TextView) findViewById(R.id.txt_intro);
        input_code = (EditText) findViewById(R.id.input_code);
        img_scan = (ImageView) findViewById(R.id.img_scan);
        img_check = (ImageView) findViewById(R.id.img_check);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        img_check.setSelected(true);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CAR_ACTIVATE_SUCESS, this);
        ODispatcher.addEventListener(OEventName.CAR_UNACTIVATE_SUCESS, this);
        ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.SCAN_RESULT_BACK, this);
    }

    @Override
    public void initViews() {
        invalidateUI();
    }

    @Override
    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            }
        });

        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_confirm.getText().toString().equals("解绑")) {
                    OToastInput.getInstance().showInput(title_head, "解绑车辆", "请输入登录密码:", new String[]{OToastInput.PASS}, "unActive", ViewCarmanModel.this);
                }else if(btn_confirm.getText().toString().equals("激活")) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(),null,false)
                            .withTitle("激活需知")
                            .withInfo("设备需接收到GPS以及GSM信号后才能被" +
                                    "激活，点击激活前请按以下要求操作：\n" +
                                    "1.确认安全的前提下，将车辆启动一次；\n" +
                                    "2.将车辆移至能接收GPS信号的户外；\n" +
                                    "3.接收信号将耗时几分钟，请耐心等待；")
                            .withButton("先去操作","继续激活")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if(isClickConfirm){
                                        OCtrlCar.getInstance().ccmd1204_activatecar(data.ide, input_code.getText().toString());
//                                        ToastConfirmData dataa = new ToastConfirmData("激活车辆", "确定要使用激活码: "+input_code.getText()+" 激活车辆: "+data.num+" 吗?",new ToastConfirmData.OnConfirmClickListener() {
//                                            @Override
//                                            public void onConfirm(boolean isClickConfirm) {
//                                                if (isClickConfirm ) {
//                                                    OCtrlCar.getInstance().ccmd1204_activatecar(data.ide, input_code.getText().toString());
//                                                }
//                                            }
//                                        });
//                                        dataa.setOther("", "",false, R.color.red_dark);
//                                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_CONFIRM, dataa);
                                    }
                                }
                            }).show();
                }
            }
        });
        img_check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                img_check.setSelected(!img_check.isSelected());
                if(img_check.isSelected()){
                    img_check.setImageResource(R.drawable.check_ok_white);
                }else{
                    img_check.setImageResource(R.drawable.check_fail_white);
                }
                handleCheckConfirmShow();
            }
        });
        //打开用户协义
        txt_agreement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
                String address;
                try {
                    ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                } catch (PackageManager.NameNotFoundException e) {
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                }
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                intent.putExtras(bundle);
                intent.setClass(getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        input_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                handleCheckConfirmShow();
            }
        });
        //扫描
        img_scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照权限
                boolean needPermission = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionCamera = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.CAMERA);
                    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                        needPermission = true;
                        GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                    }
                }
                if(!needPermission) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), CaptureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("scantype", "oned");
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_ACTIVATE_SUCESS)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆激活成功!");
//            data = ManagerCarList.getInstance().getCarByID(data.ide);
//            handleChangeData();
        }else if (eventName.equals(OEventName.CAR_UNACTIVATE_SUCESS)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆解绑成功!");
//            data = ManagerCarList.getInstance().getCarByID(data.ide);
//            handleChangeData();
        } else if (eventName.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {
            if(BasicParamCheckPassWord.isFindMain==4){
                boolean check = (Boolean) paramObj;
                if (check) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("警告:")
                            .withInfo("解绑后手机将无法控制此车辆，确认解绑吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm ) {
                                        OCtrlCar.getInstance().ccmd1220_unactivatecar(data.ide);
                                    }
                                }
                            })
                            .show();
                }
            }
        }else if (eventName.equals(OEventName.SCAN_RESULT_BACK)) {//扫描激活码
            String tst = (String) paramObj;
            handleScanBack(tst);
        }
    }

    @Override
    public void callback(String key, Object value) {
        if(key.equals("unActive")){
            //点击验证密码确定框
            JsonObject obj  = (JsonObject) value;
            String     pass = OJsonGet.getString(obj, OToastInput.PASS);
            BasicParamCheckPassWord.isFindMain=4;
            OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
        }
    }

    @Override
    public void invalidateUI() {
        if (data == null) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无车辆数据");
            return;
        }
        if(data.isMyCar == 0){//是副车主
            txt_model_state.setText("非车主本人，无法解绑模组");
            txt_intro.setText("副车主，即被授权人无权解绑模组。");
            input_code.setText("");
            input_code.setVisibility(View.INVISIBLE);
            lin_input.setVisibility(INVISIBLE);
            lin_agreement.setVisibility(INVISIBLE);
            input_code.setFocusable(false);
            btn_confirm.setText("解绑");
            btn_confirm.setEnabled(false);
            btn_confirm.setAlpha(0.5f);
        }else if(data.isActive == 1){//已激活，解绑
            txt_model_state.setText("解绑模组，将无法使用");
            txt_intro.setText("解绑操作请在官方渠道的工作人员指导下解绑，车主自己解绑需对造成的后果负责。");
            input_code.setText("");
            input_code.setFocusable(false);
            input_code.setVisibility(View.INVISIBLE);
            lin_input.setVisibility(INVISIBLE);
            lin_agreement.setVisibility(INVISIBLE);

            btn_confirm.setText("解绑");
            btn_confirm.setEnabled(true);
            btn_confirm.setAlpha(1f);
        }else {//未激活
            txt_model_state.setText("模组未激活，请先激活");
            txt_intro.setText("激活码是产品模组上的硬件码，用户购买产品后，由安装工程师操作激活。");
            lin_input.setVisibility(VISIBLE);
            input_code.setFocusable(true);
            input_code.setVisibility(View.VISIBLE);
            lin_agreement.setVisibility(VISIBLE);
            handleCheckConfirmShow();
        }
    }
    // ===================================================
    private void handleScanBack(String result) {
        Message message = new Message();
        message.what = 325;
        message.obj = result;
        handler.sendMessage(message);
    }
    public void handleCheckConfirmShow() {
        Message message = new Message();
        message.what = 326;
        handler.sendMessage(message);
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 325 :
                    String tst = (String) msg.obj;
                    input_code.setText(tst);
                    break;
                case 326:
                    if(data.isMyCar == 0)return;
                    if(data.isActive == 1)return;
                    if(img_check.isSelected() && input_code.getText().toString().length()>0){
                        btn_confirm.setText("激活");
                        btn_confirm.setEnabled(true);
                        btn_confirm.setAlpha(1f);
                    }else {
                        btn_confirm.setText("激活");
                        btn_confirm.setEnabled(false);
                        btn_confirm.setAlpha(0.5f);
                    }
                    break;
            }
        }
    }
    // ===================================================
}
