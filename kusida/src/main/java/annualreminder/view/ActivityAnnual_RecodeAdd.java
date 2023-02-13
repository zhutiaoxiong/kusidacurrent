package annualreminder.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.timepicker.TimePickerView;
import com.client.proj.kusida.R;
import annualreminder.ctrl.OCtrlAnnual;
import annualreminder.model.AnnualRecode;
import annualreminder.model.ManagerAnnual;
import annualreminder.view.clip.ClipAnnualInputAbove;
import annualreminder.view.style.StyleResult;
import annualreminder.view.style.StyleSingleLineAnnual;
import annualreminder.view.style.StyleTitleHead;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import java.util.Calendar;
import java.util.Date;

/**
 * 添加年检记录
 */
public class ActivityAnnual_RecodeAdd extends Activity implements OEventObject {
    private StyleTitleHead title_head;
    private StyleSingleLineAnnual txt_recodetime, txt_fee;
    private TextView       edit_note;
    private Button         btn_confirm;
    private RelativeLayout lin_pop_input,lin_toast;

    private       long           chooseDate;//选择的日期
    private       TimePickerView pvTime;
    public static AnnualRecode   repairRecode;//处部调用修改记录,如果是新增要清空

    private ClipAnnualInputAbove inputAbove;
    public ActivityAnnual_RecodeAdd() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_annual_recode_add);
        title_head = (StyleTitleHead) findViewById(R.id.title_head);
        txt_recodetime = (StyleSingleLineAnnual) findViewById(R.id.txt_recodetime);
        txt_fee = (StyleSingleLineAnnual) findViewById(R.id.txt_fee);
        edit_note = (TextView) findViewById(R.id.edit_note);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        lin_pop_input = (RelativeLayout) findViewById(R.id.lin_pop_input);
        lin_toast = (RelativeLayout) findViewById(R.id.lin_toast);

        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.ANNUAL_RECODE_RESULTBACK, this);
    }
    protected void initViews() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 30, calendar.get(Calendar.YEAR) + 0);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);

        invalidateUI();//初始化修改数据
        checkConfirmShow();
    }
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityAnnual_RecodeAdd.this.finish();
            }
        });
        title_head.img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ODispatcher.dispatchEvent(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW, R.layout.view_annual_auto_intro);
            }
        });
        edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lin_pop_input.getChildCount() > 0)return;//已加入过
                if (inputAbove == null)inputAbove = new ClipAnnualInputAbove(getApplicationContext(), null);
                inputAbove.show("请输入备注内容", edit_note.getText().toString(), 30);
                lin_pop_input.addView(inputAbove);
                //输入出来后禁用组件
                txt_recodetime.setClickable(false);
                txt_fee.txt_input.setFocusable(false);
                btn_confirm.setClickable(false);
                inputAbove.SetOnClickConfirmListener(new ClipAnnualInputAbove.OnClickConfirmListener() {
                    @Override
                    public void onClickConfirm(String InputTxt) {
                        if (InputTxt != null) edit_note.setText(InputTxt);
                        //隐藏输入
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputAbove.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        lin_pop_input.removeAllViews();
                        //输入关闭后开启组件
                        txt_recodetime.setClickable(true);
                        txt_fee.txt_input.setFocusableInTouchMode(true);
                        txt_fee.txt_input.setFocusable(true);
                        btn_confirm.setClickable(true);
                    }
                });
            }
        });
        lin_pop_input.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //只判断输入出来后的行为，未点备注不作处理
                if (inputAbove == null)return;
                if (lin_pop_input.getChildCount()==0)return;

                Rect r = new Rect();
                lin_pop_input.getWindowVisibleDisplayFrame(r);
                int screenHeight   = lin_pop_input.getRootView().getHeight();
                int heightKeyboard = screenHeight - (r.bottom - r.top);//键盘高度
                if (heightKeyboard > screenHeight / 3) {//是弹出的
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(getApplicationContext(), 160));
                    params.setMargins(0, (r.bottom - r.top - ODipToPx.dipToPx(getApplicationContext(), 160)), 0, 0);//边距，顶
                    inputAbove.setLayoutParams(params);
                } else {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(getApplicationContext(), 160));
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,1);
                        inputAbove.setLayoutParams(params);
                }
            }
        });
        //选择时间
        txt_recodetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
                pvTime.setMark("chooseDate");
            }
        });
        txt_fee.txt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkConfirmShow();
            }
        });
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date datee, String mark) {
                if (mark.equals("chooseDate")) {
                    chooseDate = datee.getTime();
                    String date = ODateTime.time2StringOnlyDate(chooseDate);
                    txt_recodetime.setRightText(date);
                    checkConfirmShow();
                }
            }
        });
        //确认
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_fee.getTextRight() == null || txt_fee.getTextRight().toString().equals("")) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请输入年检费用!");
                    return;
                }
                if (chooseDate <= 0) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请选择年检时间!");
                    return;
                }
                //年检记录添加 （1241）
                final AnnualRecode recode = new AnnualRecode();
                recode.inspectionTime = chooseDate;
                recode.fee = Float.valueOf(txt_fee.getTextRight());
                recode.comment = edit_note.getText().toString();
                if (repairRecode != null)
                    recode.id = repairRecode.id;
//                //是否有同年份的记录,id设为已有记录的id
//                boolean haveSame = false;
//                List<AnnualRecode> recodes = ManagerAnnual.getInstance().getCurrentRecodeList();
//                if(recodes!=null){
//                    for(AnnualRecode recode1 : recodes){
//                        String year = ODateTime.time2OnlyYear(recode1.inspectionTime);
//                        String year1 = ODateTime.time2OnlyYear(recode.inspectionTime);
//                        if(year.equals(year1)){
//                            recode.id = recode1.id;
//                            haveSame = true;
//                            break;
//                        }
//                    }
//                }
//                if(haveSame){//有同年份就提醒
//                    lin_toast.removeAllViews();
//                    StyleConfirm confirm = new StyleConfirm(getApplicationContext(),null);
//                    confirm.show("年检记录", "已添加1987年份的年检记录,覆盖?", new StyleConfirm.OnConfirmClickListener() {
//                        @Override
//                        public void onConfirm(boolean isClickConfirm) {
//                            if(isClickConfirm){
//                                OCtrlAnnual.getInstance().ccmd1240_setAnnualRecode(ManagerAnnual.currentCarId, recode);
//                            }
//                            lin_toast.removeAllViews();
//                        }
//                    });
//                    lin_toast.addView(confirm);
//                }else {
                    OCtrlAnnual.getInstance().ccmd1240_setAnnualRecode(ManagerAnnual.currentCarId, recode);
//                }
            }
        });
    }
    private void checkConfirmShow() {
        if (chooseDate <= 0 || txt_fee.getTextRight() == null || txt_fee.getTextRight().equals("")) {
            btn_confirm.setEnabled(false);
            btn_confirm.setAlpha(0.5f);
        } else {
            btn_confirm.setEnabled(true);
            btn_confirm.setAlpha(1.0f);
        }
    }
    protected void invalidateUI() {
        if (repairRecode == null) return;
        chooseDate = repairRecode.inspectionTime;
        txt_recodetime.setRightText(ODateTime.time2StringOnlyDate(repairRecode.inspectionTime));
        txt_fee.setRightText("" + repairRecode.fee);
        edit_note.setText((repairRecode.comment.equals("") ? "" : repairRecode.comment));
        title_head.setTitle("修改年检记录");
        checkConfirmShow();
    }
    //=====================================================

    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.ANNUAL_RECODE_RESULTBACK, this);
        super.onDestroy();
        ODispatcher.dispatchEvent(OEventName.ANNUAL_FROM_CAR_RESULTBACK);//首页刷新
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.ANNUAL_RECODE_RESULTBACK)) {
            boolean resultOK = (Boolean) paramObj;
            if (resultOK) {//提示在main,成功回首页,收到数据后要刷新车辆列表
                ActivityAnnual_RecodeAdd.this.finish();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StyleResult.getInstance().show(lin_toast,false,"提交失败!");
                    }
                });
            }
        }
    }

}