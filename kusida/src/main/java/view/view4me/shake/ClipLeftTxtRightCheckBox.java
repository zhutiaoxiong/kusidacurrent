package view.view4me.shake;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ClipLeftTxtRightCheckBox extends RelativeLayout {
    public TextView txt_left;
    public ImageView img_right;

    public ClipLeftTxtRightCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_lefttxt_right_img, this, true);
        txt_left =  findViewById(R.id.txt_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        TypedArray ta             = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     name           = ta.getString(R.styleable.androidMe_text);
        if (name != null && !name.equals("")) {
            txt_left.setText(name);
        }
        ta.recycle();
    }
    public void setRightImg(int resId){
        img_right.setImageResource(resId);
    }
}
