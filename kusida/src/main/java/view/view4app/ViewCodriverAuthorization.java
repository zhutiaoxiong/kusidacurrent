package view.view4app;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.google.gson.JsonArray;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.timepicker.TimePickerView;

import java.util.Calendar;
import java.util.Date;

import common.GlobalContext;
import ctrl.OCtrlAuthorization;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.loginreg.DataUser;
import view.clip.ClipLineBtnInptxt;
import view.clip.ClipPopChooseStr;
import view.clip.ClipPopUserLinking;
import view.view4me.set.ClipTitleMeSet;


public class ViewCodriverAuthorization extends LinearLayoutBase {
    private ClipTitleMeSet     title_head;
    private ClipLineBtnInptxt txt_pickcar, txt_pickuser,txt_picktime;//, txt_funcchoose
    private TextView txt_timefrom, txt_timeto;
    private Button btn_confirm, btn_qrcode;
    public static DataUser selectUsaer;
    ////	private AdapterPickAuthor	authorAdapter;
//	private AdapterShowUserList	userAdapter;
//	private int					timepos;									// 是0开始时间，还是1结束时间
//	private int					currentPos		= 1;						// 1234四个位置可选择
    private long timestart, timeend;
    //	private boolean				IS_FOR_QRCODE	= true;
    private TimePickerView pvTime;

    public ViewCodriverAuthorization(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_app_codriver_authorization, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_pickcar = (ClipLineBtnInptxt) findViewById(R.id.txt_pickcar);
        txt_pickuser = (ClipLineBtnInptxt) findViewById(R.id.txt_pickuser);
//		txt_funcchoose = (ClipLineBtnInptxt) findViewById(R.id.txt_funcchoose);
        txt_timefrom = (TextView) findViewById(R.id.txt_timefrom);
        txt_timeto = (TextView) findViewById(R.id.txt_timeto);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_qrcode = (Button) findViewById(R.id.btn_qrcode);
        txt_picktime = (ClipLineBtnInptxt) findViewById(R.id.txt_picktime);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.QRCODE_CODRIVER_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.SCAN_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK, this);// 取二维码
        ODispatcher.addEventListener(OEventName.MAIN_CLICK_BACK, this);
    }

    @Override
    public void initViews() {
        // 时间下划线
        txt_timefrom.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_timeto.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //时间选择器
        pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) , calendar.get(Calendar.YEAR) +20);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
    }

    @Override
    public void initEvents() {
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date datee, String mark) {
                if (mark.equals("timefrom")) {
//                    timestart = datee.getTime();
//                    String date = ODateTime.time2StringWithHH(timestart);
//                    txt_timefrom.setText(date);
                } else if (mark.equals("timeto")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(datee);
                    calendar.add(Calendar.HOUR,23);//时
                    calendar.add(Calendar.MINUTE,59);//分
                    calendar.add(Calendar.SECOND,59);//秒
                    Date endTime = calendar.getTime();
                    String date=ODateTime.time2StringWithHH(endTime.getTime());
                    timeend = endTime.getTime();
                    timestart=System.currentTimeMillis();
                    if(timestart>timeend){
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.time_to_end_after_the_start_time));
                    }else{
                        txt_picktime.setText(date);
                    }
//                    String date = ODateTime.time2StringWithHH(timestart);
//                    if (timestart == 0) {
//                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_select_start_time));
//                    } else if (timeend < timestart) {
//                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.time_to_end_after_the_start_time));
//                    } else {
//                        String date1 = ODateTime.time2StringWithHH(timeend);
//                        txt_timeto.setText(date1);
//                    }
                }
            }
        });
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriver);
            }
        });
//        // scan
//        title_head.img_right.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    int permissionCamera = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.CAMERA);
//                    //拍照权限
//                    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
//                        GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
//                    } else {
//                        Intent intent = new Intent();
//                        intent.setClass(ViewCodriverAuthorization.this.getContext(), CaptureActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("scantype", "qrcode");
//                        intent.putExtras(bundle);
//                        ViewCodriverAuthorization.this.getContext().startActivity(intent);
//                    }
//                } else {
//                    Intent intent = new Intent();
//                    intent.setClass(ViewCodriverAuthorization.this.getContext(), CaptureActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("scantype", "qrcode");
//                    intent.putExtras(bundle);
//                    ViewCodriverAuthorization.this.getContext().startActivity(intent);
//                }
//            }
//        });
        // 点车列表
        txt_pickcar.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String[] arr = ManagerCarList.getInstance().getCarNameListActiveAndIsSelf();//只有已激活可选
                if (arr == null) return;
                ClipPopChooseStr.getInstance().show(title_head, arr,getResources().getString(R.string.please_select_authorized_vehicles), "choosecar", ViewCodriverAuthorization.this);
            }
        });
        txt_picktime.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("timeto");
            }
        });
        txt_timefrom.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("timefrom");
            }
        });
        txt_timeto.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("timeto");
            }
        });
        // 点联系人列表
        txt_pickuser.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ClipPopUserLinking.getInstance().show(true, title_head, "chooseuser", ViewCodriverAuthorization.this);
            }
        });
        // 点功能列表
//		txt_funcchoose.setOnClickListener(new OnClickListenerMy(){
//			@Override
//			public void onClickNoFast(View v) {
//				ClipPopChooseAuthor.getInstance().show(title_head, "请选择授权功能", "pickerAuthor", ViewCodriverAuthorization.this);
//			}
//		});
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                clickConfirm(true);
            }
        });
        btn_qrcode.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                clickConfirm(false);
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }

    private void clickConfirm(boolean needUser) {
        // carid
        long carid = ManagerCarList.getInstance().getCarIdByName(txt_pickcar.getText());
        if (carid == -1) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.did_not_choose_the_vehicle));
            return;
        }
        // time
        if (timestart == 0 || timeend == 0) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_select_authorized_time));
            return;
        }
        // authorlist
//		List<DataAuthorization> author = authorAdapter.getDataList();
//		JsonArray authoritys = DataAuthorization.listToIdArr(author);
        JsonArray authoritys = new JsonArray();
        // phonenum
        if (needUser) {
            if(selectUsaer!=null&&!TextUtils.isEmpty(selectUsaer.phoneNum)){
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle("授权副车主")
                        .withInfo("你确认要授权"+txt_pickcar.getText().toString()+"给"+selectUsaer.phoneNum+"吗?")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm ) {
                                    String    phone      = selectUsaer.phoneNum;
                                    long      carid      = ManagerCarList.getInstance().getCarIdByName(txt_pickcar.getText());
                                    JsonArray authoritys = new JsonArray();
                                    OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carid, authoritys, phone, timestart, timeend);
                                }
                            }
                        })
                        .show();
            }else{
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.did_not_choose_the_user));
            }
        } else {
            OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carid, authoritys, "", timestart, timeend);
        }
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.SCAN_RESULT_BACK)) {
            try {
                String str = (String) paramObj;
                OCtrlAuthorization.getInstance().ccmd1209_scan(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (eventName.equals(OEventName.AUTHOR_CODRIVER_RESULTBACK)) {
            String code = (String) paramObj;// 二维码
            if (code.equals("")) {// 回上层面板
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.deputy_owner_has_been_authorized));
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriver);
            } else {// 显示qrcode
                ViewQrcode.AUTHOR_CODE = code;
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_qrcode);
            }
        } else if (eventName.equals(OEventName.QRCODE_CODRIVER_RESULTBACK)) {
            String error = (String) paramObj;
            if (error.equals("")) {// 成功了
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.binding_deputy_owner_success));
                OCtrlCar.getInstance().ccmd1203_getcarlist();
            }
        } else if (eventName.equals(OEventName.MAIN_CLICK_BACK)) {
            if (pvTime != null) pvTime.handleHide();
        }
    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("choosecar")) {
            String carname = (String) value;
            txt_pickcar.setText(carname);
        } else if (key.equals("chooseuser")) {
            DataUser user = (DataUser) value;
            txt_pickuser.setText(user.phoneNum);
        } else if (key.equals("pickerAuthor")) {
//			this.authorAdapter = (AdapterPickAuthor) value;
//			int count = authorAdapter.getIsSelectedCount();
//			if(count == 0){
//				txt_funcchoose.setText("未选择");
//			}else{
//				txt_funcchoose.setText("已选"+count+"权限");
//			}
        } else if (key.equals("ClipPopChooseStr_Click_Right")) {
            ViewUserList.NEED_ADD_NOW = true;
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_userlist);
        }
        super.callback(key, value);
    }

}
