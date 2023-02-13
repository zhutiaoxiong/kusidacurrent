package view.view4me.carmanage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.toast.ToastTxt;
import com.kulala.timepicker.TimePickerView;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.zxing.activity.CaptureActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import common.GlobalContext;
import ctrl.OCtrlCar;
import ctrl.OCtrlCommon;
import model.ManagerCommon;
import model.carlist.DataCarInfo;
import model.common.DataBrands;
import view.clip.ClipLineBtnInptxt;
import view.clip.ClipPopChooseStr;
import view.clip.carmanager.ClipPopChooseBrand;
import view.view4me.OPopSHowCityList;
import view.view4me.set.ClipTitleMeSet;

public class ViewCarNew extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private ClipLineBtnInptxt txt_carname, txt_vin, txt_carenginenum;
    private ClipLineBtnInptxt txt_carbrand, txt_carseries, txt_cartype;
    private ClipLineBtnInptxt txt_datebuy, txt_dateInsurance, txt_dateVerification, txt_datemaintain;
    private Button btn_confirm;
    private View   img_scan;

    private DataCarInfo data = new DataCarInfo();
    private List<DataBrands> brandsList;
    private DataBrands       selectBrand;
    private String           scantxt;
    private TimePickerView   pvTime;
    private TextView city;
    private EditText txt_carname_input;

    public ViewCarNew(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_newcar, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_carname = (ClipLineBtnInptxt) findViewById(R.id.txt_carname);
        txt_vin = (ClipLineBtnInptxt) findViewById(R.id.txt_vin);
        txt_carenginenum = (ClipLineBtnInptxt) findViewById(R.id.txt_carenginenum);
        img_scan = (View) findViewById(R.id.img_scan);
        txt_carbrand = (ClipLineBtnInptxt) findViewById(R.id.txt_carbrand);
        txt_carseries = (ClipLineBtnInptxt) findViewById(R.id.txt_carseries);
        txt_cartype = (ClipLineBtnInptxt) findViewById(R.id.txt_cartype);
        txt_datebuy = (ClipLineBtnInptxt) findViewById(R.id.txt_datebuy);
        txt_dateInsurance = (ClipLineBtnInptxt) findViewById(R.id.txt_dateInsurance);
        txt_dateVerification = (ClipLineBtnInptxt) findViewById(R.id.txt_dateVerification);
        txt_datemaintain = (ClipLineBtnInptxt) findViewById(R.id.txt_datemaintain);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        city = (TextView) findViewById(R.id.city);
        txt_carname_input = (EditText) findViewById(R.id.txt_carname_input);
        initViews();
        initEvents();
        txt_carname.openInput(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 0);
//        txt_vin.openInput(InputType.TYPE_CLASS_TEXT, 6);
//        txt_carenginenum.openInput(InputType.TYPE_CLASS_TEXT, 6);
        txt_carenginenum.setEditTextOnlyInputChar();
        txt_vin.setEditTextOnlyInputChar();
//        setEditTextOnlyInputChar
        ODispatcher.addEventListener(OEventName.CAR_NEW_SUCESS, this);
        ODispatcher.addEventListener(OEventName.COMMON_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.SCAN_RESULT_BACK, this);
        ODispatcher.addEventListener(OEventName.MAIN_CLICK_BACK, this);
    }

    @Override
    public void initViews() {
        if (city.getText().toString().equals("其他")) {
            txt_carname_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else {
            txt_carname_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        }
        btn_confirm.setAlpha(0.5f);
        brandsList = ManagerCommon.getInstance().getBrandsList();
        if (brandsList == null||brandsList.size()==0) {
            // 加载品牌列表
            OCtrlCommon.getInstance().ccmd1310_getBrandList(ManagerCommon.getInstance().getBrandUpdateTime());
        }
        //时间选择器
        pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 3);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
    }


    @Override
    public void callback(String key, Object value) {
        if (key.equals("carbrand")) {
            data.brand = (String) value;
            selectBrand = ManagerCommon.getInstance().getBrands(data.brand);
            data.series="";
            data.model="";
            data.brandId = selectBrand.ide;
            txt_carbrand.setText(data.brand);
            txt_carseries.setText(getResources().getString(R.string.vehicle_system_no_space));
            txt_cartype.setText(getResources().getString(R.string.vehicle_type_no_space));
//            txt_carseries.setText(getResources().getString(R.string.vehicle_system_no_space));
//            txt_cartype.setText(getResources().getString(R.string.vehicle_type_no_space));
        } else if (key.equals("carseries")) {
            data.series = (String) value;
            data.model="";
            txt_carseries.setText(data.series);
            txt_cartype.setText(getResources().getString(R.string.vehicle_type_no_space));
//            txt_cartype.setText(getResources().getString(R.string.vehicle_type_no_space));
        } else if (key.equals("cartype")) {
            data.model = (String) value;
            txt_cartype.setText(data.model);
        }else if (key.equals("cityname")) {
            String cityname= (String) value;
            city.setText(cityname);
            if (city.getText().toString().equals("其他")) {
                txt_carname_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            } else {
                txt_carname_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
            }
        }
        buttonIsEnable();
        super.callback(key, value);
    }
    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
            }
        });
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date datee, String mark) {
                if (mark.equals("datebuy")) {
                    data.buyTime = datee.getTime();
                    String date = ODateTime.time2StringOnlyDate(data.buyTime);
                    txt_datebuy.setText(date);
                } else if (mark.equals("dateinsurance")) {
                    data.insuranceTime = datee.getTime();
                    String date = ODateTime.time2StringOnlyDate(data.insuranceTime);
                    txt_dateInsurance.setText(date);
                } else if (mark.equals("dateverification")) {
                    data.annualAuditTime = datee.getTime();
                    String date = ODateTime.time2StringOnlyDate(data.annualAuditTime);
                    txt_dateVerification.setText(date);
                } else if (mark.equals("datemaintain")) {
                    data.maintenanceTime = datee.getTime();
                    String date = ODateTime.time2StringOnlyDate(data.maintenanceTime);
                    txt_datemaintain.setText(date);
                }
            }
        });
        txt_carbrand.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                txt_carname.clearFocus();
                txt_vin.clearFocus();
                txt_carenginenum.clearFocus();
                if (brandsList == null) return;
                String[] arr = DataBrands.getBrandsArr(brandsList);
                if (arr == null) {
                    // 加载品牌列表
                    OCtrlCommon.getInstance().ccmd1310_getBrandList(ManagerCommon.getInstance().getBrandUpdateTime());
                    return;
                }
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txt_carname.getWindowToken(), 0);
                ClipPopChooseBrand.getInstance().show(title_head, "carbrand", ViewCarNew.this);
                buttonIsEnable();
            }
        });
        txt_carseries.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (selectBrand == null) {
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo(getResources().getString(R.string.please_first_select_the_vehicle_brands)).quicklyShow();
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_first_select_the_vehicle_brands));
                    return;
                }
                String[] arr = selectBrand.getSeriesArr();
                ClipPopChooseStr.getInstance().show(title_head, arr, getResources().getString(R.string.please_select_a_vehicle_series), "carseries", ViewCarNew.this);
                buttonIsEnable();
            }
        });
        txt_cartype.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (selectBrand==null) {
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo(getResources().getString(R.string.please_first_select_the_vehicle_brands)).quicklyShow();
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_first_select_the_vehicle_brands));
                    return;
                }
                if (TextUtils.isEmpty(data.series)) {
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo(getResources().getString(R.string.please_first_select_a_vehicle_series)).quicklyShow();
//                    ToastUtil.showShortToastCenterAlwaysShow(getResources().getString(R.string.please_first_select_a_vehicle_series));
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_first_select_a_vehicle_series));
                    return;
                }
                String[] arr = selectBrand.getModelsArr(data.series);
                ClipPopChooseStr.getInstance().show(title_head, arr,getResources().getString(R.string.please_select_a_vehicle_model), "cartype", ViewCarNew.this);
                buttonIsEnable();
            }
        });
        txt_datebuy.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("datebuy");
            }
        });
        txt_dateInsurance.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("dateinsurance");
            }
        });
        txt_dateVerification.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("dateverification");
            }
        });
        txt_datemaintain.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("datemaintain");
            }
        });
        img_scan.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionCamera = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.CAMERA);
                    //拍照权限
                    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                        GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), CaptureActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("scantype", "oned");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }
                } else {
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
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String carName = txt_carname_input.getText().toString();
                if(carName == null || carName.length()<6)
                {
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("车牌号输入最少6位!").quicklyShow();
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "车牌号输入最少6位!");
                    return;
                }
                if(TextUtils.isEmpty(data.brand)){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("请选择车辆品牌").quicklyShow();
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请选择车系");
                    return;
                }
                if(TextUtils.isEmpty(data.series)){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("请选择车系").quicklyShow();
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请选择车系");
                    return;
                }
                if(TextUtils.isEmpty(data.model)){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("请选择车型").quicklyShow();
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请选择车型");
                    return;
                }
                if (city.getText().toString().equals("其他")) {
                    data.num = txt_carname_input.getText().toString();
                } else {
                    data.num = city.getText().toString() + txt_carname_input.getText().toString();
                }
                data.VINNum = txt_vin.getText().toString();
                data.engineNum = txt_carenginenum.getText().toString();
                String str = data.checkDataComp();
                if (str.equals("1")) {
                    OCtrlCar.getInstance().ccmd1201_newrepairCar(data);
                } else {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, str);
                }
            }
        });
        city.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String privinceName=city.getText().toString();
                OPopSHowCityList.getInstance().show(title_head,"cityname",ViewCarNew.this,privinceName);
            }
        });
        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                    buttonIsEnable();
            }
        };
        txt_carname_input.addTextChangedListener(watcher);
        txt_vin.txt_input.addTextChangedListener(watcher);
        txt_carenginenum.txt_input.addTextChangedListener(watcher);
    }
    public void buttonIsEnable(){
        String txtCarbrand=txt_carbrand.txt_input_show.getText().toString();
        String carname_input=txt_carname_input.getText().toString();
        String txt_carserie=txt_carseries.txt_input_show.getText().toString();
        String txt_cartyp=txt_cartype.txt_input_show.getText().toString();
        String txt_vi=txt_vin.txt_input.getText().toString();
        String txt_carenginenu=txt_carenginenum.txt_input.getText().toString();
        if(!txtCarbrand.equals("")&&!carname_input.equals("")&&!txt_carserie.equals("")&&!txt_cartyp.equals("")&&txt_vi.length()==6&&txt_carenginenu.length()==6){
            btn_confirm.setEnabled(true);
            btn_confirm.setAlpha(1.0f);
        }else{
            btn_confirm.setEnabled(false);
            btn_confirm.setAlpha(0.5f);
        }
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_NEW_SUCESS)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.add_a_new_vehicle) + data.num + getResources().getString(R.string.success));
            // 新车成功，回上层
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_main);
        } else if (eventName.equals(OEventName.COMMON_RESULTBACK)) {
            brandsList = ManagerCommon.getInstance().getBrandsList();
            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_carbrand.callOnClick();
                }
            });
        } else if (eventName.equals(OEventName.SCAN_RESULT_BACK)) {
            String tst = (String) paramObj;
            if (tst == null || tst.length() < 6) return;
            scantxt = tst.substring(tst.length() - 6, tst.length());
            handleChangeData();
        } else if (eventName.equals(OEventName.MAIN_CLICK_BACK)) {
            if (pvTime != null) pvTime.handleHide();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CAR_NEW_SUCESS, this);
        ODispatcher.removeEventListener(OEventName.COMMON_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.SCAN_RESULT_BACK, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        if(scantxt!=null){
            txt_vin.setText(scantxt);
            scantxt = null;
        }else{
            txt_vin.setText(data.VINNum);
        }
    }

    // =====================================================================
}
