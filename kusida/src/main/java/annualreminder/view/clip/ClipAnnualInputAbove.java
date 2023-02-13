package annualreminder.view.clip;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

/**
 * Created by Administrator on 2017/2/23.
 * 在输入法上方弹出的输入框
 */

public class ClipAnnualInputAbove extends LinearLayout {
    public  EditText               edit_input;
    private TextView               txt_left;
    private Button                 btn_confirm;
    private int                    maxNum;
    private OnClickConfirmListener listener;
    public ClipAnnualInputAbove(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_input_above, this, true);
        edit_input = (EditText) findViewById(R.id.edit_input);
        txt_left = (TextView) findViewById(R.id.txt_left);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickConfirm(edit_input.getText().toString());
            }
        });
    }
    public void show(String hint, String text, int maxCharNum) {
        edit_input.setHint(hint);
        edit_input.setText(text);
        //显示可输字数
        if (maxCharNum > 0) {
            this.maxNum = maxCharNum;
            //android:maxLength=”100“意思是最大输入长度100
            //代码中editText.setFilters(new  InputFilter[]{ new  InputFilter.LengthFilter(100)});
            edit_input.setFilters(new InputFilter[]{ new  InputFilter.LengthFilter(maxCharNum)});
            txt_left.setText("" + (maxNum - edit_input.getText().toString().length()));
            edit_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    String ttt = edit_input.getText().toString();
                    txt_left.setText("" + (maxNum - ttt.length()));
                }
            });
        }
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//
        edit_input.requestFocus();
    }
    public interface OnClickConfirmListener {
        void onClickConfirm(String InputTxt);
    }
    public void SetOnClickConfirmListener(OnClickConfirmListener listener) {
        this.listener = listener;
    }
}
