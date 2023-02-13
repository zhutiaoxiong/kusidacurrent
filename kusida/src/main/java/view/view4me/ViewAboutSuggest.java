package view.view4me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.toast.ToastResult;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import common.GlobalContext;

import view.clip.child.CheckBoxOMG;
import view.view4me.set.ClipTitleMeSet;

public class ViewAboutSuggest extends LinearLayoutBase{
    private ClipTitleMeSet title_head;
    private CheckBoxOMG   select_1, select_2, select_3, select_4, select_5, select_6;
    private EditText   txt_input;
    private Button     btn_confirm;
    private ScrollView laye_scroll;

    private int selectPos = 0;
    protected MyHandler handler = new MyHandler();

    public ViewAboutSuggest(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_about_suggest, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        select_1 = (CheckBoxOMG) findViewById(R.id.select_1);
        select_2 = (CheckBoxOMG) findViewById(R.id.select_2);
        select_3 = (CheckBoxOMG) findViewById(R.id.select_3);
        select_4 = (CheckBoxOMG) findViewById(R.id.select_4);
        select_5 = (CheckBoxOMG) findViewById(R.id.select_5);
        select_6 = (CheckBoxOMG) findViewById(R.id.select_6);
        txt_input = (EditText) findViewById(R.id.txt_input);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        laye_scroll = (ScrollView) findViewById(R.id.laye_scroll);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CLIP_CHECKBOX_CHANGESELECT, this);
        ODispatcher.addEventListener(OEventName.SUGGEST_HTTP_RESULTBACK, this);
    }

    public void initViews() {
        btn_confirm.setEnabled(false);//
        select_1.setSelected(true);selectPos = 1;
    }
    public void initEvents() {
        laye_scroll.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                laye_scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                String txt = txt_input.getText().toString();
                    if (selectPos != 0 && !txt.equals("")) {
//                        OCtrlCommon.getInstance().ccmd1313_sendSuggest(selectPos, txt);
                        ToastResult.getInstance().show(title_head,true,null);
                    } else {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.information_not_filled));
                    }
                super.onClickNoFast(v);
            }
        });
        txt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(selectPos >0 && editable.toString().length()>0){
                    btn_confirm.setEnabled(true);
                }else{
                    btn_confirm.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        GlobalContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        GlobalContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        GlobalContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onDetachedFromWindow();
//        ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
    }

    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CLIP_CHECKBOX_CHANGESELECT)) {
            int pos = (Integer) paramObj;
            handleChangeSelect(pos);
        }else if(eventName.equals(OEventName.SUGGEST_HTTP_RESULTBACK)){
            boolean sucess = (Boolean)paramObj;
            if(sucess){
                ViewAbout.needShowResult = true;
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about);
            }else{
//                handleShowResult();
            }
        }
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    public void invalidateUI() {
    }

    // ==============================================================
    public void handleChangeSelect(int pos) {
        Message message = new Message();
        message.what = 911;
        message.arg1 = pos;
        handler.sendMessage(message);
    }
    public void handleShowResult() {
        Message message = new Message();
        message.what = 912;
        handler.sendMessage(message);
    }

    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 911:
                    int pos = msg.arg1;
                    select_1.setSelected(false);
                    select_2.setSelected(false);
                    select_3.setSelected(false);
                    select_4.setSelected(false);
                    select_5.setSelected(false);
                    select_6.setSelected(false);
                    switch (pos){
                        case 1:select_1.setSelected(true);selectPos = 1;break;
                        case 2:select_2.setSelected(true);selectPos = 2;break;
                        case 3:select_3.setSelected(true);selectPos = 3;break;
                        case 4:select_4.setSelected(true);selectPos = 4;break;
                        case 5:select_5.setSelected(true);selectPos = 5;break;
                        case 6:select_6.setSelected(true);selectPos = 6;break;
                    }
                    if(selectPos >0 && txt_input.getText().toString().length()>0){
                        btn_confirm.setEnabled(true);
                    }else{
                        btn_confirm.setEnabled(false);
                    }
                    break;
                case 912:
                    ToastResult.getInstance().show(title_head,false,null);
                    break;
            }
        }
    }
}
