package view.view4me.carmanage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.ToastConfirmNormal;

import common.GlobalContext;
import common.blue.BlueLinkNetSwitch;
import ctrl.OCtrlBlueTooth;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;

/**
 * 车辆蓝牙，进入此页，先设data
 * 请注意data 不是当前车
 * 只有已激活车，才进的了此页
 */
public class ViewCarmanBlue extends RelativeLayoutBase {
    private static DataCarInfo data;

    private final ClipTitleMeSet title_head;
    private final TextView txt_blue_state;
    private final TextView txt_intro;
    private final TextView input_code;
    private final Button btn_confirm;


    public ViewCarmanBlue(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.carman_blue_set, this, true);
        title_head = findViewById(R.id.title_head);
        txt_blue_state = findViewById(R.id.txt_blue_state);
        txt_intro = findViewById(R.id.txt_intro);
        input_code = findViewById(R.id.input_code);
        btn_confirm = findViewById(R.id.btn_confirm);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.MINI_UNBIND_RESULT, this);
    }

    public static void setData(DataCarInfo datacar) {
        data = datacar;
        ManagerCarList.getInstance().setCurrentCar(datacar.ide);
    }

    @Override
    public void initViews() {
        data = ManagerCarList.getInstance().getCarByID(data.ide);
        invalidateUI();
    }

    @Override
    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                data = ManagerCarList.getInstance().getCarByID(data.ide);
                BlueLinkNetSwitch.setSwitchBlueModel(false, data.ide, "CarmanBlue back net");//切回网络模式
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unBindConfirm();
            }
        });

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
    }

    @Override
    public void invalidateUI() {
        if (data == null) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无车辆数据");
            return;
        }
        data = ManagerCarList.getInstance().getCarByID(data.ide);
        if (data == null) return;
        txt_blue_state.setText("已绑定蓝牙");
        txt_intro.setText("解绑蓝牙操作需在安装工程师的指导下进行，用户切勿擅自操作。");
        input_code.setVisibility(INVISIBLE);
        input_code.setText("");
        btn_confirm.setText("解绑");
        btn_confirm.setEnabled(true);
        btn_confirm.setAlpha(1f);
        btn_confirm.setBackground(getContext().getResources().getDrawable(R.drawable.bgst_bk_fore_round8));
    }

    @Override
    public void receiveEvent(String eventName, Object paramObject) {
        if (eventName.equals(OEventName.MINI_UNBIND_RESULT)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车辆解绑成功!");
        }
    }
}
