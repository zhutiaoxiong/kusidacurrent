package annualreminder.view.style;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.method.ReplacementTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

/**
 * 按扭左右图标，中间文字
 */
public class StyleSingleLineAnnual extends LinearLayout {
    private TextView  txt_title,txt_right,txt_imgr;
    public EditText txt_input;
    public  ImageView img_left,img_right, img_splitline;

    private boolean openinput;
    public StyleSingleLineAnnual(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.style_single_line_annual, this, true);
        txt_title = (TextView) findViewById(R.id.txt_info);
        txt_right = (TextView) findViewById(R.id.txt_right);
        txt_imgr = (TextView) findViewById(R.id.txt_imgr);
        txt_input = (EditText) findViewById(R.id.txt_input);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        img_splitline = (ImageView) findViewById(R.id.img_splitline);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        //title
        String textLeft = ta.getString(R.styleable.androidMe_text);
        if (textLeft != null && !textLeft.equals("")) {
            txt_title.setText(textLeft);
        } else {
            txt_title.setText("");
        }
        int textColor = ta.getColor(R.styleable.androidMe_textcolor, 0);
        if (textColor != 0) {
            txt_title.setTextColor(textColor);
        }
//        String gravity = ta.getString(R.styleable.androidMe_gravityTxt);
//        if(gravity!=null) {
//            if (gravity.equals("left")) {
//                txt_title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            } else if (gravity.equals("center")) {
//                txt_title.setGravity(Gravity.CENTER);
//            }
//        }
        //left img
        int leftId = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        if (leftId != 0) {
            img_left.setImageResource(leftId);
            img_left.setVisibility(View.VISIBLE);
        } else {
            img_left.setVisibility(View.GONE);
        }
        //textimgr
        String textimgr = ta.getString(R.styleable.androidMe_textimgr);
        if (textimgr != null && !textimgr.equals("")) {
            txt_imgr.setText(textimgr);
            txt_imgr.setVisibility(VISIBLE);
        } else {
            txt_imgr.setVisibility(GONE);
        }
        //right img
        int rightId = ta.getResourceId(R.styleable.androidMe_rightres, -1);
        if (rightId > 0) {
            img_right.setImageResource(rightId);
            img_right.setVisibility(View.VISIBLE);
        }
        boolean rightimgvisible = ta.getBoolean(R.styleable.androidMe_rightimgvisible, true);
        if (rightimgvisible) {
            img_right.setVisibility(VISIBLE);
        } else {
            img_right.setVisibility(INVISIBLE);
        }
        //splitLine default false
        boolean splitLine = ta.getBoolean(R.styleable.androidMe_splitLine, false);
        if (splitLine) {
            img_splitline.setVisibility(View.VISIBLE);
        } else {
            img_splitline.setVisibility(View.INVISIBLE);
        }
        //openinput default false
        openinput = ta.getBoolean(R.styleable.androidMe_openinput, false);
        if (openinput) {
            txt_input.setVisibility(View.VISIBLE);
        } else {
            txt_input.setVisibility(View.GONE);
        }
        ta.recycle();
    }
    public void setImgRightVisible(int visible){
        img_right.setVisibility(visible);
    }


    public void setText(String txt) {
        txt_title.setText(txt);
    }
    public void setRightText(String txt) {
        if(openinput){
            if(txt!=null && txt.length()>0) {
                txt_input.setText(txt);
                txt_input.setVisibility(VISIBLE);
            }else{
                txt_input.setVisibility(GONE);
            }
        }else{
            if(txt!=null && txt.length()>0) {
                txt_right.setText(txt);
                txt_right.setVisibility(VISIBLE);
            }else{
                txt_right.setVisibility(GONE);
            }
        }
    }

    public String getText() {
        return txt_title.getText().toString();
    }
    public String getTextRight() {
        if(openinput){
            if(txt_input.getText() == null)return null;
            return txt_input.getText().toString();
        }else{
            if(txt_right.getText() == null)return null;
            return txt_right.getText().toString();
        }
    }
    //=================================================
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
