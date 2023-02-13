package view.find;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ProItem extends RelativeLayout {
    public TextView txt_top;
    public ImageView img_right;
    public TextView txt_bottom;

    public ProItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pro_item, this, true);
        txt_top = findViewById(R.id.txt_top);
        img_right = findViewById(R.id.img_right);
        txt_bottom = findViewById(R.id.txt_bottom);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        int rightId = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        String name = ta.getString(R.styleable.androidMe_topText);
        String bottomStr = ta.getString(R.styleable.androidMe_bottomTxt);
        if (name != null && !name.equals("")) {
            txt_top.setText(name);
        }
        if (bottomStr != null && !bottomStr.equals("")) {
            txt_bottom.setText(bottomStr);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
            img_right.setVisibility(View.VISIBLE);
        }else{
            img_right.setVisibility(View.INVISIBLE);
        }
        ta.recycle();
    }
}
