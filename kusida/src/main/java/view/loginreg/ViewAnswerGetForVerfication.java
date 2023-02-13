package view.loginreg;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import common.timetick.OTimeSchedule;
import ctrl.OCtrlAnswer;
import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import view.clip.me.ClipPopCheckVerficodeError;
import view.safetyneed.ViewAnswerGetItem;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/8/11.
 */
public class ViewAnswerGetForVerfication extends LinearLayoutBase {

    private ClipTitleMeSet title_head;
    private ViewAnswerGetItem question1, question2, question3;
    private Button submmit;
    private int timeCount = 0;
    private SmartImageView error;

    public ViewAnswerGetForVerfication(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_answer_get_for_login, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        question1 = (ViewAnswerGetItem) findViewById(R.id.question1);
        question2 = (ViewAnswerGetItem) findViewById(R.id.question2);
        question3 = (ViewAnswerGetItem) findViewById(R.id.question3);
        submmit = (Button) findViewById(R.id.view_answer_submmit);


        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SECRETINFOS_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.SUBMMIT_PASSWORD_PROBLEM, this);
        ODispatcher.addEventListener(OEventName.CHECK_VERFICODE_FAILED_THREE, this);
        ODispatcher.addEventListener(OEventName.CHECK_VERFICODE_FAILED_FIVE, this);
    }

    @Override
    public void initViews() {
        OCtrlAnswer.getInstance().ccmd1119_answer(ManagerAnswer.phoneNum, 1);
        handleChangeData();
    }

    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                //SECRETINFOS_RESULTBACK
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_reset_password_logreg);
            }
        });
        submmit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                String answer1 = question1.txt_answer.getText().toString();
                String answer2 = question2.txt_answer.getText().toString();
                String answer3 = question3.txt_answer.getText().toString();
                if (!answer1.equals("") && answer1 != null || !answer2.equals("") && answer2 != null || !answer3.equals("") && answer3 != null) {
                    Integer[] que = new Integer[3];
                    que[0] = ManagerAnswer.getInstance().getQuestionFromStr(question1.txt_question.getText().toString()).ide;
                    que[1] = ManagerAnswer.getInstance().getQuestionFromStr(question2.txt_question.getText().toString()).ide;
                    que[2] = ManagerAnswer.getInstance().getQuestionFromStr(question3.txt_question.getText().toString()).ide;
                    OCtrlRegLogin.getInstance().ccmd1120_submit_password_problem(que,
                            new String[]{answer1, answer2, answer3}, 2, ManagerAnswer.phoneNum, 3, null);
                } else {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.the_answer_can_not_be_empty));
                }
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.SECRETINFOS_RESULTBACK)) {
            if(ManagerAnswer.getInstance().secretInfoslist!=null&&ManagerAnswer.getInstance().secretInfoslist.size()>=3){
                question1.showQuestion(ManagerAnswer.getInstance().secretInfoslist.get(0).title, 0);
                question2.showQuestion(ManagerAnswer.getInstance().secretInfoslist.get(1).title, 1);
                question3.showQuestion(ManagerAnswer.getInstance().secretInfoslist.get(2).title, 2);
            }
        } else if (eventName.equals(OEventName.SUBMMIT_PASSWORD_PROBLEM)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_input_two_password);
        } else if (eventName.equals(OEventName.CHECK_VERFICODE_FAILED_THREE)) {
            handleErrorTimeOne();
        } else if (eventName.equals(OEventName.CHECK_VERFICODE_FAILED_FIVE)) {
            handleErrorTimeThree();
        } else if (eventName.equals(OEventName.TIME_TICK_SECOND)) {
            timeCount++;
            if (timeCount >= 5) {
                ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, ViewAnswerGetForVerfication.this);
                handleErrorpopdismiss();
            }
        }
    }

    private MyHandler handler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 107:
                    ClipPopCheckVerficodeError.getInstance().show(title_head, R.drawable.passworderrorone, "cuowuyici", ViewAnswerGetForVerfication.this);
                    timeCount = 0;
                    OTimeSchedule.getInstance().init();
                    ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, ViewAnswerGetForVerfication.this);
                    break;
                case 109:
                    ClipPopCheckVerficodeError.getInstance().show(title_head, R.drawable.passworderrorthree, "cuowusanci", ViewAnswerGetForVerfication.this);
                    timeCount = 0;
                    OTimeSchedule.getInstance().init();
                    ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, ViewAnswerGetForVerfication.this);
                    break;
                case 108:
                    ClipPopCheckVerficodeError.getInstance().exitThis();
            }
        }
    }

    public void handleErrorTimeOne() {
        Message message = new Message();
        message.what = 107;
        this.handler.sendMessage(message);
    }

    public void handleErrorTimeThree() {
        Message message = new Message();
        message.what = 109;
        this.handler.sendMessage(message);
    }

    public void handleErrorpopdismiss() {
        Message message = new Message();
        message.what = 108;
        this.handler.sendMessage(message);
    }


    @Override
    public void callback(String key, Object value) {
//        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.SECRETINFOS_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.CHECK_VERFICODE_FAILED_THREE, this);
        ODispatcher.removeEventListener(OEventName.CHECK_VERFICODE_FAILED_FIVE, this);
        ODispatcher.removeEventListener(OEventName.SUBMMIT_PASSWORD_PROBLEM, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        question1.txt_question_name.setText(getResources().getString(R.string.problem_a));
        question2.txt_question_name.setText(getResources().getString(R.string.question_two));
        question3.txt_question_name.setText(getResources().getString(R.string.question_three));
        if(ManagerAnswer.getInstance().secretInfoslist!=null&&ManagerAnswer.getInstance().secretInfoslist.size()>0) {
            if (!TextUtils.isEmpty(ManagerAnswer.getInstance().secretInfoslist.get(0).title)) {
                question1.txt_question.setText(ManagerAnswer.getInstance().secretInfoslist.get(0).title);
            }
            if (!TextUtils.isEmpty(ManagerAnswer.getInstance().secretInfoslist.get(1).title)) {
                question2.txt_question.setText(ManagerAnswer.getInstance().secretInfoslist.get(1).title);
            }
            if (!TextUtils.isEmpty(ManagerAnswer.getInstance().secretInfoslist.get(2).title)) {
                question3.txt_question.setText(ManagerAnswer.getInstance().secretInfoslist.get(2).title);
            }
        }
    }
    // ===================================================== {
}
