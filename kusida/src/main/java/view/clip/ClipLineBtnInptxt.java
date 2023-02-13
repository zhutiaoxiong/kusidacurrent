package view.clip;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.ReplacementTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.io.UnsupportedEncodingException;

public class ClipLineBtnInptxt extends LinearLayout {
	public  ImageView img_right,img_red_point,img_splitline;
	public  EditText  txt_input;
	public TextView  txt_title,txt_input_show;
	public ClipLineBtnInptxt(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_line_inptxt, this, true);
		txt_input = (EditText) findViewById(R.id.txt_input);
		img_right = (ImageView) findViewById(R.id.img_right);
		img_red_point = (ImageView) findViewById(R.id.img_red_point);
		img_splitline = (ImageView) findViewById(R.id.img_splitline);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_input_show = (TextView) findViewById(R.id.txt_input_show);
	    TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.androidMe);   
        String righttxt = ta.getString(R.styleable.androidMe_righttxt);
		String righthint = ta.getString(R.styleable.androidMe_righthint);
        int righttxtcolor = ta.getColor(R.styleable.androidMe_righttxtcolor,0);
        String gravity = ta.getString(R.styleable.androidMe_gravityTxt); 
        String lefttxt = ta.getString(R.styleable.androidMe_lefttxt);
        int rightId = ta.getResourceId(R.styleable.androidMe_rightres,0);
        if(lefttxt!=null && !lefttxt.equals("")){
    		txt_title.setText(lefttxt);
        }
        if(righttxt!=null && !righttxt.equals("")){
        	txt_input.setText(righttxt);
			txt_input_show.setText(righttxt);
        }
        if(righthint!=null && !righthint.equals("")){
        	txt_input.setHint(righthint);
			txt_input_show.setHint(righthint);
        }
        if(gravity!=null && !gravity.equals("")){
        	if(gravity.equals("left")){
        		txt_input.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				txt_input_show.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        	}else if(gravity.equals("center")){
				txt_input.setGravity(Gravity.CENTER);
				txt_input_show.setGravity(Gravity.CENTER);
			}
        }
         if(rightId!=0){
        	img_right.setImageResource(rightId);
        	img_right.setVisibility(View.VISIBLE);
        }
         if(righttxtcolor!=0){
        	 txt_input.setTextColor(righttxtcolor);
			 txt_input_show.setTextColor(righttxtcolor);
         }

		boolean splitLine = ta.getBoolean(R.styleable.androidMe_splitLine,true);
		if(!splitLine){
			img_splitline.setVisibility(INVISIBLE);
		}
        ta.recycle();
	}
	public void openInput(int inputType ,int maxnum){
		txt_input_show.setVisibility(INVISIBLE);
		txt_input.setVisibility(VISIBLE);
		txt_input.setEnabled(true);
		txt_input.setInputType(inputType);
		if(inputType == InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS){
			txt_input.setTransformationMethod(new AllCapTransformationMethod ());
		}
		if(maxnum>0){
			InputFilter[] filters = {new InputFilter.LengthFilter(maxnum)};
			txt_input.setFilters(filters);
		}
	}
	public void setRedPointVisible(boolean defaultFalse){
		if(defaultFalse) {
			img_red_point.setVisibility(INVISIBLE);
		}else{
			img_red_point.setVisibility(INVISIBLE);
		}
	}
	public void setText(String txt){
		txt_input.setText(txt);
		txt_input_show.setText(txt);
	}
	public void setRightImage(int resId){
		img_right.setImageResource(resId);
		img_right.setVisibility(View.VISIBLE);
	}
	public String getText(){
		return txt_input.getText().toString();
	}
	public void setRightTextColorResid(int resid){
		txt_input.setTextColor(getResources().getColor(resid));
	}
	public void setEditTextOnlyInputChar(){
//		DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,true);
//		txt_input.setKeyListener(numericOnlyListener);
		txt_input_show.setVisibility(INVISIBLE);
		txt_input.setVisibility(VISIBLE);
		txt_input.setEnabled(true);
		txt_input.setInputType(InputType.TYPE_CLASS_TEXT);
		txt_input.setFilters(new InputFilter[]{
				new InputFilter.LengthFilter(6),new DigitsKeyListener(false ,true){
			@Override
			protected char[] getAcceptedChars() {
				return new char[] { '1', '2', '3', '4', '5', '6', '7', '8','9', '0' ,'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
			}

			@Override
			public int getInputType() {

				return android.text.InputType.TYPE_CLASS_TEXT;
			}
		}});

	}

	public boolean isCN(String str){
		try {
			byte [] bytes = str.getBytes("UTF-8");
			if(bytes.length == str.length()){
				return false;
			}else{
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}



	public class AllCapTransformationMethod extends ReplacementTransformationMethod {
		@Override
		protected char[] getOriginal() {
			char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
			return aa;
		}
		@Override
		protected char[] getReplacement() {
			char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
			return cc;
		}
	}
}
