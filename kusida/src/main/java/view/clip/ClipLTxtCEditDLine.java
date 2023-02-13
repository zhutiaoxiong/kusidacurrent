package view.clip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;


public class ClipLTxtCEditDLine extends RelativeLayout {
	public  EditText  txt_input;
	public TextView  txt_left;

	public ClipLTxtCEditDLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_l_txt_c_edit_d_line, this, true);
		txt_input = findViewById(R.id.txt_input);
		txt_left = findViewById(R.id.txt_left);
	    @SuppressLint("CustomViewStyleable") TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.androidMe);
        String lefttxt = ta.getString(R.styleable.androidMe_lefttxt);
		String hint = ta.getString(R.styleable.androidMe_hint);
		boolean isShow = ta.getBoolean(R.styleable.androidMe_isShow,false);

        if(lefttxt!=null && !lefttxt.equals("")){
			txt_left.setText(lefttxt);
        }
		if(hint!=null && !hint.equals("")){
			txt_input.setHint(hint);
		}
		if(isShow){
			txt_input.setFocusable(false);
			txt_input.setKeyListener(null);
		}
        ta.recycle();
	}
}
