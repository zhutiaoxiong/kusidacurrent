package view.view4me.carmanage;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.line.ClipLineInfo;
import com.kulala.staticsview.OnClickListenerMy;


import common.GlobalContext;
import ctrl.OCtrlAuthorization;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;


/**
 * 车辆详情，进入此页，先设data
 */
public class ViewCarmanInfoDetail extends RelativeLayoutBase {
    public static DataCarInfo    data;
    private ClipTitleMeSet title_head;
    private       SmartImageView img_icon;
    private       ClipLineInfo   txt_carname, txt_brand, txt_series, txt_model, txt_vin, txt_engine;
    private LinearLayout lin_2;
    private TextView txt_bao_info,txt_bao_type;
    private ImageView img_bao;
    private Button btn_confirm;
    public ViewCarmanInfoDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.carman_info_detail, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        img_icon = (SmartImageView) findViewById(R.id.img_icon);
        txt_carname = (ClipLineInfo) findViewById(R.id.txt_carname);
        txt_brand = (ClipLineInfo) findViewById(R.id.txt_brand);
        txt_series = (ClipLineInfo) findViewById(R.id.txt_series);
        txt_model = (ClipLineInfo) findViewById(R.id.txt_model);
        txt_vin = (ClipLineInfo) findViewById(R.id.txt_vin);
        txt_engine = (ClipLineInfo) findViewById(R.id.txt_engine);

        lin_2 = (LinearLayout) findViewById(R.id.lin_2);
        txt_bao_info = (TextView) findViewById(R.id.txt_bao_info);
        txt_bao_type = (TextView) findViewById(R.id.txt_bao_type);
        img_bao = (ImageView) findViewById(R.id.img_bao);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USER_STOPED, this);
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
        // 修改
        title_head.txt_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ViewCarModify.data = data.copy();
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_carinfo_modify);
            }
        });
        //用户取消授权
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.isMyCar==1){
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("终止授权")
                            .withInfo("确定不再用车，取消被授权的车辆吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm ) {//成功 AUTHORIZATION_USER_STOPED
//                                    long carId= ManagerCarList.getInstance().getCurrentCarID();
                                        OCtrlAuthorization.getInstance().ccmd1207_stopauthor(data.getAuthorityInfo().authorityId, data.getAuthorityInfo().userinfo.userId,data.ide);
                                    }
                                }
                            })
                            .show();
                }else{
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"副车主无法取消授权");
                }
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.AUTHORIZATION_USER_STOPED)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
        }
    }

    @Override
    public void callback(String key, Object value) {
    }

    @Override
    public void invalidateUI() {
        if(data == null){
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"无车辆数据");
            return;
        }
        img_icon.setImageUrl(data.logo);
        txt_carname.setText(data.num);
        txt_brand.setText(data.brand);
        txt_series.setText(data.series);
        txt_model.setText(data.model);
        txt_vin.setText(data.VINNum);
        txt_engine.setText(data.engineNum);
        //保修期,isDue 0：未激活，1：保修中，2：已过保
        if(data.isMyCar == 0){//是副车主
            btn_confirm.setVisibility(VISIBLE);
            lin_2.setVisibility(INVISIBLE);
            title_head.txt_right.setVisibility(INVISIBLE);
        }else{
            btn_confirm.setVisibility(INVISIBLE);
            lin_2.setVisibility(VISIBLE);
            title_head.txt_right.setVisibility(VISIBLE);
            if(data.isDue == 0){
                txt_bao_info.setText("尚未激活");
                img_bao.setImageResource(R.drawable.carman_bao_unactive);
                txt_bao_type.setText("未激活");
                txt_bao_type.setTextColor(Color.parseColor("#6d6d6d"));
            }else if(data.isDue == 1){
                txt_bao_info.setText("于"+ ODateTime.time2StringOnlyDate(data.activeTime)+"激活");
                img_bao.setImageResource(R.drawable.carman_bao_in);
                txt_bao_type.setText("保修中");
                txt_bao_type.setTextColor(Color.parseColor("#ff0677da"));
            }else if(data.isDue == 2){
                txt_bao_info.setText("于"+ ODateTime.time2StringOnlyDate(data.activeTime)+"激活");
                img_bao.setImageResource(R.drawable.carman_bao_in);
                txt_bao_type.setText("已过保");
                txt_bao_type.setTextColor(Color.parseColor("#ff0677da"));
            }
        }
    }
}
