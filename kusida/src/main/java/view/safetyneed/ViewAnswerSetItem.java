package view.safetyneed;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OArrayConvent;

import java.util.List;

import model.ManagerAnswer;
import model.answer.DataAnswer;

public class ViewAnswerSetItem extends RelativeLayoutBase {
    public  TextView txt_question_name;
    private Spinner  spinner_question;//, spinner_answer
    public EditText txt_answer;
    public  String question;
//    private TextView txt_answer1, txt_answer2;
//    private RelativeLayout lin_answer2;

    private ArrayAdapter<String> arr_adapter;
    private DataAnswer           currentAnswer;
    private int myPos;
    private static long changedTime;

    public ViewAnswerSetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_answer_set_item, this, true);
        txt_question_name = (TextView) findViewById(R.id.txt_question_name);
        spinner_question = (Spinner) findViewById(R.id.spinner_question);
        txt_answer = (EditText) findViewById(R.id.txt_answer);
//        spinner_answer = (Spinner) findViewById(R.id.spinner_answer);
//        txt_answer1 = (TextView) findViewById(R.id.txt_answer1);
//        txt_answer2 = (TextView) findViewById(R.id.txt_answer2);
//        lin_answer2 = (RelativeLayout) findViewById(R.id.lin_answer2);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SECRETINFO_CHANGE_QUESTION,ViewAnswerSetItem.this);
    }

    @Override
    public void initViews() {
    }

    public void showQuestion(DataAnswer data,int pos) {
        changedTime = System.currentTimeMillis();
        myPos = pos;
        this.currentAnswer = data;
        handleChangeData();
    }

    @Override
    public void initEvents() {
        spinner_question.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long now  = System.currentTimeMillis();
                if (now - changedTime < 500) {
                    //没有改数据
                    Log.i("itemSelect","没有改数据");
                } else {
                    //改变了数据
                    question = arr_adapter.getItem(position);
                    DataAnswer data = ManagerAnswer.getInstance().getQuestionFromStr(question);
                    ManagerAnswer.getInstance().changeUsedQuestion(data.title,myPos);
                    currentAnswer = data;
                    changedTime = now;
                    //更新其它提问的问题
                    ODispatcher.dispatchEvent(OEventName.SECRETINFO_CHANGE_QUESTION);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.SECRETINFO_CHANGE_QUESTION)){
            //更新其它提问的问题
            handleChangeData();
//            if(arr_adapter!=null){
//                List<String> list = ManagerAnswer.getInstance().getUnUsedQuestion();
//                if (list == null) return;
//                list.add(currentAnswer.title);
//                arr_adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_onlytext_answer, list);
//            }
        }
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
        if (currentAnswer == null) return;
        List<String> list = ManagerAnswer.getInstance().getUnUsedQuestion();
        if (list == null) return;
        list.add(currentAnswer.title);
        arr_adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_onlytext_answer, list);
        arr_adapter.setDropDownViewResource(R.layout.list_item_onlytext_answer);
        spinner_question.setAdapter(arr_adapter);
        spinner_question.setSelection(OArrayConvent.ListGetStringPos(list, currentAnswer.title), false);
        spinner_question.setDropDownHorizontalOffset(100);
        spinner_question.setDropDownVerticalOffset(3);
        question=currentAnswer.title;
    }
//    public void invalidateChild() {
//        spinner_answer.setVisibility(INVISIBLE);
//        txt_answer1.setVisibility(View.INVISIBLE);
//        lin_answer2.setVisibility(View.INVISIBLE);
//        if (currentAnswer.type == 1) {//二个框
//            txt_answer1.setVisibility(View.VISIBLE);
//            lin_answer2.setVisibility(View.VISIBLE);
//            if (currentAnswer.androidMultipleBoxInfos == null || currentAnswer.androidMultipleBoxInfos.size() == 0)
//                return;
//            DataAnswer.DataPlace place = currentAnswer.androidMultipleBoxInfos.get(0);//默认的第一个数据
//            txt_answer1.setText(place.name);
//            txt_answer2.setText(place.boxInfos.get(0).name);
//            //选地点
//        } else if (currentAnswer.type == 2) {//一个框
//            spinner_answer.setVisibility(VISIBLE);
//            lin_answer2.setVisibility(View.INVISIBLE);
//            //汽车品牌空数据
//            if (currentAnswer.singleBoxInfos == null || currentAnswer.singleBoxInfos.size() == 0)
//                return;
//            txt_answer1.setText(currentAnswer.singleBoxInfos.get(0).name);
//            //选列表
//            ArrayAdapter<String> arr_adapter2 = new ArrayAdapter<String>(getContext(), R.layout.list_item_onlytext_answer, OArrayConvent.ListGetStringArr(currentAnswer.singleBoxInfos, "name"));
//            arr_adapter2.setDropDownViewResource(R.layout.list_item_onlytext_answer);
//            spinner_answer.setAdapter(arr_adapter2);
//        } else if (currentAnswer.type == 3) {//一个框
//            spinner_answer.setVisibility(VISIBLE);
//            lin_answer2.setVisibility(View.INVISIBLE);
//            if (currentAnswer.toastBoxInfos == null || currentAnswer.toastBoxInfos.size() == 0)
//                return;
//            txt_answer1.setText(currentAnswer.toastBoxInfos.get(0).name);
//            //选列表
//            ArrayAdapter<String> arr_adapter3 = new ArrayAdapter<String>(getContext(), R.layout.list_item_onlytext_answer, OArrayConvent.ListGetStringArr(currentAnswer.toastBoxInfos, "name"));
//            arr_adapter3.setDropDownViewResource(R.layout.list_item_onlytext_answer);
//            spinner_answer.setAdapter(arr_adapter3);
//        }
//
//    }
    // =====================================================
}
