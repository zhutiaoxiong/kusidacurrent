package view.clip;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.time.CountDownTimerMy;

import common.GlobalContext;
import ctrl.OCtrlRegLogin;
import model.safety.DataSafeTy;

public class ClipPopSpeechReceive {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private Context context;

    private RelativeLayout thisView;
    private TextView txt_click;
    private Button btn_confirm;
    private ImageView view_background;
    private int entrance;//表示从哪个页面发送的验证码

    public static String phoneNum = "";
    //    private static boolean canCountTime=true;
    // ========================out======================
    private static ClipPopSpeechReceive _instance;

    public static ClipPopSpeechReceive getInstance() {
        if (_instance == null)
            _instance = new ClipPopSpeechReceive();
        return _instance;
    }

    //===================================================
    public void show(View parentView, int entrance) {
        this.parentView = parentView;
        this.entrance = entrance;
        DataSafeTy.entrance = entrance;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (thisView == null) {
            thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_speech_receive, null);
            txt_click = (TextView) thisView.findViewById(R.id.txt_click);
            btn_confirm = (Button) thisView.findViewById(R.id.btn_confirm);
            view_background = (ImageView) thisView.findViewById(R.id.view_background);
            txt_click.setText(Html.fromHtml(GlobalContext.getContext().getResources().getString(R.string.not_receive_verification_code_ooo) + "<u><font color=red>" + GlobalContext.getContext().getResources().getString(R.string.click_here_to) + "</font></u>" + GlobalContext.getContext().getResources().getString(R.string.check_the_solution_ooo)
            ));

            popContain = new PopupWindow(thisView);
            popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popContain.setFocusable(true);
            popContain.setTouchable(true);
            popContain.setOutsideTouchable(false);
        }
        initEvents();
        initViews();
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    //private static  int timeCount=0;
    private static long timeOld;

    public void initViews() {
//        long nowTime=System.currentTimeMillis();
//       long timeDifference= nowTime-timeOld;
//        if(timeCount-(int)((timeDifference/1000)%1000L)>0){
//            timeCount=timeCount-(int)(timeDifference/1000%1000L);
//            searchTime();
//        }else{
//            timeCount=0;
//        }
    }

    private void changeText() {
//        Message message=Message.obtain();
//        message.what=110;
//        handler.sendMessage(message);
    }

    private void searchTime() {
//          handler.postDelayed(new Runnable() {
//              @Override
//              public void run() {
//                  if(timeCount>=0){
//                      changeText();
//                  }
//              }
//          },1000L);
    }


    private void exitThis() {
        popContain.dismiss();
    }

    public void initEvents() {
        view_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitThis();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimerMy(60000, 1000) {

                    @Override
                    public void onTick(long l) {
                        btn_confirm.setEnabled(false);
                        btn_confirm.setText(l / 1000 + "");
                    }

                    @Override
                    public void onFinish() {
                        btn_confirm.setEnabled(true);
                        btn_confirm.setText(GlobalContext.getContext().getResources().getString(R.string.voice_verification));
                    }
                }.start();

//                timeCount=60;
//                searchTime();
                OCtrlRegLogin.getInstance().ccmd1101_getVerificationCode(phoneNum, entrance, 2);
            }
        });
        txt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitThis();
                if (entrance == 1 || entrance == 7) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_verificationcode_how);
                } else {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_loginreg_verificationcode_how);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 110) {
//                if(timeCount<=0){
//                    btn_confirm.setEnabled(true);
//                    btn_confirm.setText(GlobalContext.getContext().getResources().getString(R.string.voice_verification));
//                    return;
//                }else{
//                    btn_confirm.setEnabled(false);
//                    btn_confirm.setText(timeCount+"");
//                    timeCount--;
//                    timeOld=System.currentTimeMillis();
//                    searchTime();
//                }
            }
        }
    };

}

