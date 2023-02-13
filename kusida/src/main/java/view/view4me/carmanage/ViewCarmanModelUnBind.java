package view.view4me.carmanage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.TurnOffKeyBoard;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.ToastConfirmNormal;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlBlueTooth;
import ctrl.OCtrlCar;
import ctrl.OCtrlRegLogin;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.find.BasicParamCheckPassWord;
import view.view4me.set.ClipTitleMeSet;

/**
 * 车辆模组，进入此页，先设data
 */
public class ViewCarmanModelUnBind extends RelativeLayoutBase {
    public static DataCarInfo data;
    private ClipTitleMeSet title_head;
    private TextView txt_model_state, txt_intro;
    private Button btn_confirm;
    private EditText txt_pass;
    private TextView btn_cancel, btn_confirm_pop;
    private LinearLayout tiplayout;

    public ViewCarmanModelUnBind(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.carman_model_set_unbind, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_model_state = (TextView) findViewById(R.id.txt_model_state);
        txt_intro = (TextView) findViewById(R.id.txt_intro);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tiplayout = (LinearLayout) findViewById(R.id.tiplayout);
        txt_pass = (EditText) findViewById(R.id.txt_pass);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_confirm_pop = (TextView) findViewById(R.id.btn_confirm_pop);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CAR_UNACTIVATE_SUCESS, this);
        ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.MINI_UNBIND_RESULT, this);
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
//                if(btn_confirm.getText().toString().equals("解绑")) {
                tiplayout.setVisibility(View.VISIBLE);
//                    eded.setVisibility(View.VISIBLE);
//                    OToastInput.getInstance().showInput(title_head, "解绑车辆", "请输入登录密码:", new String[]{OToastInput.PASS}, "unActive", ViewCarmanModelUnBind.this);
//                }
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnOffKeyBoard.closeKeyBoard(GlobalContext.getCurrentActivity());
                tiplayout.setVisibility(View.INVISIBLE);
            }
        });
        btn_confirm_pop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = txt_pass.getText().toString();
                tiplayout.setVisibility(View.INVISIBLE);
//                TurnOffKeyBoard.closeKeyBoard(GlobalContext.getCurrentActivity());
                BasicParamCheckPassWord.isFindMain = 5;
                OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_UNACTIVATE_SUCESS)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆解绑成功!");
            //解绑的时当前车辆就把当前车辆蓝牙清零
            if (data.ide == ManagerCarList.getInstance().getCurrentCarID()) {
                BlueLinkReceiver.needChangeCar(data.ide, "", "", data.isBindBluetooth, data.carsig, data.isMyCar);
            }
//            data = ManagerCarList.getInstance().getCarByID(data.ide);
//            handleChangeData();
        } else if (eventName.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {
            if (BasicParamCheckPassWord.isFindMain == 5) {
                boolean check = (Boolean) paramObj;
                if (check) {
                    if (data.terminalNum.startsWith("NFC") || data.terminalNum.startsWith("MIN")) {
                        unBindConfirm();
                    } else {
                        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                                .withTitle("警告:")
                                .withInfo("解绑后手机将无法控制此车辆，确认解绑吗?")
                                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                    @Override
                                    public void onClickConfirm(boolean isClickConfirm) {
                                        if (isClickConfirm) {
                                            OCtrlCar.getInstance().ccmd1220_unactivatecar(data.ide);
                                        }
                                    }
                                })
                                .show();
                    }
                }
            }

        }else if (eventName.equals(OEventName.MINI_UNBIND_RESULT)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆解绑成功!");
        }
    }
    private void unBindConfirm() {
        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                .withTitle("解绑")
                .withInfo("解绑后，请到手机蓝牙列表中查找，如有NFC开头的蓝牙项，则需要取消该蓝牙的配对。")
                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                    @Override
                    public void onClickConfirm(boolean isClickConfirm) {
                        if (isClickConfirm) {//发送解绑协议
                            OCtrlBlueTooth.getInstance().ccmd_10003_unbind_mini(data.ide);
                        }
                    }
                }).show();
    }

    @Override
    public void callback(String key, Object value) {
//        if(key.equals("unActive")){
//            //点击验证密码确定框
//            JsonObject obj  = (JsonObject) value;
//            String     pass = OJsonGet.getString(obj, OToastInput.PASS);
//            OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
//        }
    }

    @Override
    public void invalidateUI() {
        if (data == null) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无车辆数据");
            return;
        }
        if (data.isMyCar == 0) {//是副车主
            txt_model_state.setText("非车主本人，无法解绑模组");
            txt_intro.setText("副车主，即被授权人无权解绑模组。");
            btn_confirm.setText("解绑");
            btn_confirm.setEnabled(false);
            btn_confirm.setAlpha(0.5f);
        } else if (data.isActive == 1) {//已激活，解绑
            txt_model_state.setText("解绑模组，将无法使用");
            txt_intro.setText("解绑操作请在官方渠道的工作人员指导下解绑，车主自己解绑需对造成的后果负责。");
            btn_confirm.setText("解绑");
            btn_confirm.setEnabled(true);
            btn_confirm.setAlpha(1f);
        }
    }
    // ===================================================

    // ===================================================
}
