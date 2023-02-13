package view.view4me.set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ClipSetItem extends RelativeLayout {
    public ImageView img_left;
    public TextView txt_left;

    public ClipSetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.me_set_item, this, true);
        img_left = (ImageView) findViewById(R.id.img_left);
        txt_left = (TextView) findViewById(R.id.txt_left);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String name = ta.getString(R.styleable.androidMe_text);
        int leftId = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        String textColor = ta.getString(R.styleable.androidMe_textcolor);
        if (name != null && !name.equals("")) {
            txt_left.setText(name);
        }
        if (leftId != 0) {
            img_left.setImageResource(leftId);
        }
        if (!TextUtils.isEmpty(textColor)) {
            txt_left.setTextColor(Color.parseColor(textColor));
        }else{
            txt_left.setTextColor(Color.parseColor("#000000"));
        }

        ta.recycle();
    }
}
