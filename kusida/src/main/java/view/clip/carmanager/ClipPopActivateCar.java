package view.clip.carmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.zxing.activity.CaptureActivity;

import view.ActivityWeb;

public class ClipPopActivateCar implements OEventObject {
    private PopupWindow popContain; // 弹出管理
    private View        parentView; // 本对象显示
    private Context     context;

    private RelativeLayout thisView;
    private View           touch_exit;
    private ImageView      img_scan;
    private TextView       txt_licence;
    private EditText       txt_input;
    private CheckBox       check_for_licence;
    private Button         btn_cancel, btn_confirm;

    private        String             mark;                        // 选择标记
    private        OCallBack          callback;
    private MyHandler handler = new MyHandler();
    // ========================out======================
    private static ClipPopActivateCar _instance;

    public static ClipPopActivateCar getInstance() {
        if (_instance == null)
            _instance = new ClipPopActivateCar();
        return _instance;
    }

    // ===================================================
    public void show(View parentView,String carName, String mark, OCallBack callback) {
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_activatecar, null);
        touch_exit = (View) thisView.findViewById(R.id.touch_exit);
        img_scan = (ImageView) thisView.findViewById(R.id.img_scan);
        txt_licence = (TextView) thisView.findViewById(R.id.txt_licence);
        txt_input = (EditText) thisView.findViewById(R.id.txt_input);
        check_for_licence = (CheckBox) thisView.findViewById(R.id.check_for_licence);
        btn_cancel = (Button) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) thisView.findViewById(R.id.btn_confirm);
        initViews();
        initEvents();
        txt_input.setHint(context.getResources().getString(R.string.please_enter_the)+carName+context.getResources().getString(R.string.equipment_activation_code));
        ODispatcher.addEventListener(OEventName.SCAN_RESULT_BACK, this);
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        // popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popContain.dismiss();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    public void initEvents() {
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(check_for_licence.isChecked() && !txt_input.getText().toString().equals("")){
                    callback.callback(mark, txt_input.getText().toString());
                    popContain.dismiss();
                }else{
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,context.getResources().getString(R.string.please_make_sure_the_selected_user_agreement_and_enter_the_correct_activation_code));
                }
            }
        });
        btn_cancel.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                popContain.dismiss();
            }
        });
        txt_licence.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
//                ClipPopShowLicence.getInstance().show(parentView);
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about_licence);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
                String address;
                try {
                    ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                } catch (PackageManager.NameNotFoundException e) {
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                }
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                intent.putExtras(bundle);
                intent.setClass(context, ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        touch_exit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                popContain.dismiss();
            }
        });
        img_scan.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                Intent intent = new Intent();
                intent.setClass(parentView.getContext(), CaptureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("scantype", "oned");
                intent.putExtras(bundle);
                parentView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.SCAN_RESULT_BACK)) {
            String tst = (String) paramObj;
            handleScanBack(tst);
        }
    }
    private void handleScanBack(String result) {
        Message message = new Message();
        message.what = 325;
        message.obj = result;
        handler.sendMessage(message);
    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 325 :
                    String tst = (String) msg.obj;
                    txt_input.setText(tst);
                    break;
            }
        }
    }
}
