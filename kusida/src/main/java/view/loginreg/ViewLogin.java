package view.loginreg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_view_change.BitmapGetNetAsync;
import com.kulala.staticsfunc.static_view_change.OInputValidation;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.AdapterUerNameHistory;
import common.GlobalContext;
import common.pinyinzhuanhuan.CircleImg;
import common.timetick.OTimeSchedule;
import ctrl.OCtrlRegLogin;
import model.ManagerLoginReg;
import model.loginreg.DataUser;
import view.ActivityWeb;
import view.clip.ClipPopLoading;
import view.clip.child.ClipAutoCompText;
import view.clip.gesture.GestureVerityPage;

public class ViewLogin extends LinearLayoutBase {
    //    private ClipTitleHead title_head;
    private final TextView txt_to_register;
    private final TextView txt_findpass;
    private final TextView txt_exitlogin;
    private final TextView txt_permission;
    private final TextView txt_licence;
    private final Button btn_confirm_login;
    private final EditText txt_input_password;
    private final ClipAutoCompText txt_input_username;
    private final ImageView img_gabage;
    private final ImageView img_permission;
    private final ImageView delete;
    private final CircleImg head_pic;
    private String checkedUsername;
    private String checkedPassword;
    private boolean isPermission = false;
    private final ListView username_listview;
    private List<String> list;

    public ViewLogin(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_loginreg_login, this, true);
//        title_head = (ClipTitleHead) findViewById(R.id.title_head);
        txt_to_register = (TextView) findViewById(R.id.txt_to_register);
        txt_findpass = (TextView) findViewById(R.id.txt_findpass);
        txt_exitlogin = (TextView) findViewById(R.id.txt_exitlogin);
        txt_permission = (TextView) findViewById(R.id.txt_permission);
        txt_licence = (TextView) findViewById(R.id.txt_licence);
        txt_input_username = (ClipAutoCompText) findViewById(R.id.txt_input_username);
        txt_input_password = (EditText) findViewById(R.id.txt_input_password);
        btn_confirm_login = (Button) findViewById(R.id.btn_confirm_login);
        img_gabage = (ImageView) findViewById(R.id.img_gabage);
        img_permission = (ImageView) findViewById(R.id.img_permission);
        head_pic = (CircleImg) findViewById(R.id.head_pic);
        username_listview = (ListView) findViewById(R.id.username_listview);
        delete = (ImageView) findViewById(R.id.delete);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.LOGIN_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.LOGIN_FAILED, this);
        ODispatcher.addEventListener(OEventName.LOGIN_USERNAME_SELECT, this);
    }

    @Override
    public void initViews() {
        img_gabage.setVisibility(View.VISIBLE);
        delete.setVisibility(View.INVISIBLE);
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isPermission");
        isPermission = ODBHelper.queryResult2boolean(result);
        if (isPermission) {
            img_permission.setImageResource(R.drawable.check_ok_white);
        } else {
            img_permission.setImageResource(R.drawable.check_fail_white);
        }
        username_listview.setVisibility(View.INVISIBLE);
        list = new ArrayList<>();
        List<DataUser> listaaa = ManagerLoginReg.getInstance().getUserHistory();
        if (listaaa != null) {
            for (int i = 0; i < listaaa.size(); i++) {
                list.add(listaaa.get(i).phoneNum);
            }
        }
        username_listview.setAdapter(new AdapterUerNameHistory(getContext(), list, new AdapterUerNameHistory.OnLastOneDeleteListner() {
            @Override
            public void onDeleteLast() {
                img_gabage.setImageResource(R.drawable.arrow_down_new);
                username_listview.setVisibility(View.INVISIBLE);
            }
        }));
    }

    @Override
    public void initEvents() {
        // ȥע��
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNum = txt_input_username.getText().toString();
                String password = txt_input_password.getText().toString();
                setLoginButtonBgAndTextColor();
                if (password.length() >= 1) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.INVISIBLE);
                }
//                if (phoneNum.length() >= 11) {
//                    img_gabage.setVisibility(VISIBLE);
//                } else {
//                    img_gabage.setVisibility(INVISIBLE);
//                }
                if (phoneNum.length() == 11) {
                    if (ManagerLoginReg.getInstance().getUserHistory() == null) return;
                    for (int i = 0; i < ManagerLoginReg.getInstance().getUserHistory().size(); i++) {
                        DataUser user = ManagerLoginReg.getInstance().getUserHistory().get(i);
                        if (user == null || user.avatarUrl == null) return;
                        if (phoneNum.equals(user.phoneNum)) {
                            new BitmapGetNetAsync().findImage(getContext(), user.avatarUrl, new BitmapGetNetAsync.OnGetImageListener() {
                                @Override
                                public void onGetImage(Bitmap bitmap) {
                                    Message message = Message.obtain();
                                    message.what = 1100;
                                    message.obj = bitmap;
                                    handler.sendMessage(message);
                                }
                            });
                        }
                    }
                }
            }
        };
        txt_input_username.addTextChangedListener(watcher);
        txt_input_password.addTextChangedListener(watcher);
        OnClickListenerMy onClickPermission = new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                isPermission = !isPermission;
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isPermission", String.valueOf(isPermission));
                if (isPermission) {
                    img_permission.setImageResource(R.drawable.check_ok_white);
                } else {
                    img_permission.setImageResource(R.drawable.check_fail_white);
                }
                String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isPermission");
                isPermission = ODBHelper.queryResult2boolean(result);
                setLoginButtonBgAndTextColor();
            }
        };
        img_permission.setOnClickListener(onClickPermission);
        txt_permission.setOnClickListener(onClickPermission);
        txt_licence.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
//                ClipPopShowLicence.getInstance().show(title_head);
//                ClipPopShowLicence.getInstance().show(txt_findpass);
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about_licence);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
                String address = "http://manage.kcmoco.com/protocol_kusida.html";
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                intent.putExtras(bundle);
                intent.setClass(getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        txt_to_register.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_reg);
            }
        });
        txt_exitlogin.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, 0);
            }
        });
        // ȥ�һ�����
        txt_findpass.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {

                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_username);
            }
        });
        // ִ�е�¼���
        btn_confirm_login.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (!isPermission) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, GlobalContext.getContext().getResources().getString(R.string.loginreg_please_see_agree) + getContext().getResources().getString(R.string.user_agreement));
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txt_input_password.getWindowToken(), 0);
                String result = checkPhoneNumResult();
                String result1 = checkPassWordResult();
                if (result.equals("")) {
                    // 存入历史记录
//                    ManagerCommon.getInstance().putOneNameHistory(checkedUsername);
//                    txt_input_username.setData(OConver.ListGetStringArr(ManagerCommon.getInstance().usernameHistory));
                    if (result1.equals("")) {
                        handleermesscircleshow();
                        OCtrlRegLogin.getInstance().ccmd1103_login(checkedUsername, checkedPassword);
                        ManagerLoginReg.loginPhoneNum = checkedUsername;
                    } else {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, result1);
                    }
                } else {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, result);
                }
            }
        });
//        txt_input_username.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View view) {
//                handleerSwitchUserNameList();
//            }
//        });
        img_gabage.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                handleerSwitchUserNameList();
            }
        });
        delete.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                txt_input_password.setText("");
            }
        });
        this.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                username_listview.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void setLoginButtonBgAndTextColor(){
        String phoneNum = txt_input_username.getText().toString();
        String passWord = txt_input_password.getText().toString();
        if(!TextUtils.isEmpty(phoneNum)&&!TextUtils.isEmpty(phoneNum)&&phoneNum.length()>=11&&passWord.length()>=6&&isPermission){
            btn_confirm_login.setSelected(true);
            btn_confirm_login.setTextColor(Color.parseColor("#000000"));
        }else{
            btn_confirm_login.setSelected(false);
            btn_confirm_login.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.LOGIN_SUCCESS)) {
            timeCount = 0;
            ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, ViewLogin.this);
            handlemessstopcircle();
            int isFirst = (Integer) paramObj;
            if (isFirst == 1) {
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("finishInfo", String.valueOf(false));
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_finshinfo);
            } else {
                String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenGesture");
                int isOpenGesture = ODBHelper.queryResult2Integer(result, 0);
                if (isOpenGesture == 1) {
                    GestureVerityPage.fromPage = "viewLogin";
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.clip_gesture_verify);
                } else {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, 0);
                }
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("finishInfo", String.valueOf(true));
            }
        } else if (OEventName.LOGIN_FAILED.equals(eventName)) {
            timeCount = 0;
            ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, ViewLogin.this);
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.login_failed_please_try_again));
            handlemessstopcircle();
        } else if (OEventName.TIME_TICK_SECOND.equals(eventName)) {
            timeCount++;
            if (timeCount >= 5) {
                ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, ViewLogin.this);
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.login_failed_please_try_again));
                handlemessstopcircle();
            }
        } else if (OEventName.LOGIN_USERNAME_SELECT.equals(eventName)) {
            //如果用户名满十一位历史记录中有这个用户名就获取头像
            String username = (String) paramObj;
            setHeadPic(username);
        }
    }

    /**
     * @param picUrl 用户名满十一位历史记录中有这个用户名就获取头像
     */
    private void setHeadPic(String picUrl) {
        Message message = Message.obtain();
        message.what = 1120;
        message.obj = picUrl;
        handler.sendMessage(message);
        final DataUser dataUser = ManagerLoginReg.getInstance().getUserFromHistory(picUrl);
        if (dataUser == null || dataUser.avatarUrl == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = DataUser.getBitmap(dataUser.avatarUrl);
                    Message message = Message.obtain();
                    message.what = 1100;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    setDeaufaltHeadPic();
                }
            }
        }).start();
    }

    /**
     * 设置默认头像
     */
    private void setDeaufaltHeadPic() {
        Message message = Message.obtain();
        message.what = 1101;
        handler.sendMessage(message);
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.LOGIN_SUCCESS, this);
        ODispatcher.removeEventListener(OEventName.LOGIN_FAILED, this);
        ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, this);
        ODispatcher.removeEventListener(OEventName.LOGIN_USERNAME_SELECT, this);

        super.onDetachedFromWindow();
    }
    // =====================================================

    /**
     * 检查用户名
     **/
    private String checkPhoneNumResult() {
        checkedUsername = txt_input_username.getText().toString();
        if (!OInputValidation.chkInputPhoneNum(checkedUsername))
            return getResources().getString(R.string.please_enter_your_correct_phone_number);
        return "";
    }

    /**
     * 检查密码
     **/
    private String checkPassWordResult() {
        checkedPassword = txt_input_password.getText().toString();
        if (checkedPassword.length() < 6 || checkedPassword.length() > 18)
//        if (!OInputValidation.chkInputPassword(checkedPassword))
            return getResources().getString(R.string.enter_the_password_for_six_to_eightteen);
        return "";
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    public void invalidateUI() {
    }

    private int timeCount;
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 110:
                    timeCount = 0;
                    ClipPopLoading.getInstance().stopLoading();
                    break;
                case 111:
                    ClipPopLoading.getInstance().show(txt_findpass);
//                    ClipPopLoading.getInstance().show(title_head);
                    timeCount = 0;
                    OTimeSchedule.getInstance().init();
                    ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, ViewLogin.this);
                    break;
                case 1100:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (bitmap == null) {
                        head_pic.setImageResource(R.drawable.img_loginreg_logo);
                    } else {
                        head_pic.setImageBitmap(bitmap);
                    }
                    break;
                case 1101:
                    head_pic.setImageResource(R.drawable.img_loginreg_logo);
                    break;
                case 1120:
                    String aaa = (String) msg.obj;
                    handleerSwitchUserNameList();
                    txt_input_username.setText(aaa);
                    break;
                case 1121:
                    if (username_listview.getVisibility() == View.VISIBLE) {
                        setUserNameListViewIsVisible(0);
                    } else {
                        if (list == null || list.size() == 0) {
                            setUserNameListViewIsVisible(0);
                        } else {
                            setUserNameListViewIsVisible(1);
                        }
                    }
                    break;
            }
        }
    };

    /**
     * @param isVisible 设置用户名历史视图是否可见0不可见1可见
     */
    private void setUserNameListViewIsVisible(int isVisible) {
        if (isVisible == 0) {
            img_gabage.setImageResource(R.drawable.arrow_down_new);
            username_listview.setVisibility(View.INVISIBLE);
        } else if (isVisible == 1) {
            img_gabage.setImageResource(R.drawable.arrow_top_new);
            username_listview.setVisibility(View.VISIBLE);
        }
    }

    public void handlemessstopcircle() {
        Message message = Message.obtain();
        message.what = 110;
        handler.sendEmptyMessage(110);
    }

    public void handleermesscircleshow() {
        Message message = Message.obtain();
        message.what = 111;
        handler.sendEmptyMessage(111);
    }

    public void handleerSwitchUserNameList() {
        Message message = Message.obtain();
        message.what = 1121;
        handler.sendMessage(message);
    }
}
