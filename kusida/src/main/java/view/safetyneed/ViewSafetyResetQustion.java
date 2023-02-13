package view.safetyneed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import ctrl.OCtrlAnswer;
import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import model.ManagerLoginReg;
import model.safety.DataSafeTy;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/10/28.
 */
public class ViewSafetyResetQustion extends LinearLayoutBase {

    private ClipTitleMeSet title_head;
    private ViewAnswerSetItem question1,question2,question3;
    private Button submmit;
    public ViewSafetyResetQustion(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_safety_reset_qustion, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        question1 = (ViewAnswerSetItem) findViewById(R.id.question1);
        question2 = (ViewAnswerSetItem) findViewById(R.id.question2);
        question3 = (ViewAnswerSetItem) findViewById(R.id.question3);
        submmit   =(Button)findViewById(R.id.view_answer_submmit);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SECRETINFOS_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.SUBMMIT_PASSWORD_PROBLEM, this);
    }
    @Override
    public void initViews() {
        title_head.setTitle(getResources().getString(R.string.modify_security_issues));
        OCtrlAnswer.getInstance().ccmd1119_answer("", 2);
        handleChangeData();
    }
    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(DataSafeTy.from==7){
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
                }else{
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, DataSafeTy.back);
                }
            }
        });
        submmit.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                String answer1= question1.txt_answer.getText().toString();
                String answer2= question2.txt_answer.getText().toString();
                String answer3= question3.txt_answer.getText().toString();
                Integer[] que=new Integer[3];
                que[0]=ManagerAnswer.getInstance().getQuestionFromStr(question1.question).ide;
                que[1]=ManagerAnswer.getInstance().getQuestionFromStr(question2.question).ide;
                que[2]=ManagerAnswer.getInstance().getQuestionFromStr(question3.question).ide;
                if (!answer1.equals("") && answer1 != null || !answer2.equals("") && answer2 != null || !answer3.equals("") && answer3 != null) {
                        OCtrlRegLogin.getInstance().ccmd1120_submit_password_problem(que,
                                new String[]{answer1,answer2,answer3}, 1,null,DataSafeTy.from, ManagerAnswer.verifyStr);
                }else{
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.the_answer_can_not_be_empty_please_retry_after_input));
                }
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.SECRETINFOS_RESULTBACK)) {
            ManagerAnswer.getInstance().changeUsedQuestion(ManagerAnswer.getInstance().secretInfoslist.get(0).title,0);
            ManagerAnswer.getInstance().changeUsedQuestion(ManagerAnswer.getInstance().secretInfoslist.get(5).title,1);
            ManagerAnswer.getInstance().changeUsedQuestion(ManagerAnswer.getInstance().secretInfoslist.get(4).title,2);
            question1.showQuestion(ManagerAnswer.getInstance().secretInfoslist.get(0),0);
            question2.showQuestion(ManagerAnswer.getInstance().secretInfoslist.get(5),1);
            question3.showQuestion(ManagerAnswer.getInstance().secretInfoslist.get(4),2);
        }else if(eventName.equals(OEventName.SUBMMIT_PASSWORD_PROBLEM)){
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.reset_success));
            ManagerLoginReg.getInstance().saveUserInfoforSetQue(true);
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_safety);
        }
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.SECRETINFOS_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.SUBMMIT_PASSWORD_PROBLEM, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        question1.txt_question_name.setText(getResources().getString(R.string.problem_a));
        question2.txt_question_name.setText(getResources().getString(R.string.question_two));
        question3.txt_question_name.setText(getResources().getString(R.string.question_three));
    }
}
