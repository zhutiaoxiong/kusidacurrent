package view.safetyneed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;

public class ViewAnswerGetItem extends RelativeLayoutBase {
    public  TextView txt_question_name,txt_question;
    public   EditText txt_answer;
    private String question;
    private int myPos;
    public ViewAnswerGetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_answer_get_item, this, true);
        txt_question_name = (TextView) findViewById(R.id.txt_question_name);
        txt_question = (TextView) findViewById(R.id.txt_question);
        txt_answer = (EditText) findViewById(R.id.txt_answer);
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
    }
    @Override
    public void initEvents() {
    }
    public void showQuestion(String ques,int pos){
        this.question = ques;
        myPos = pos;
        handleChangeData();
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        txt_question.setText(question);
    }
}
