package view.me;

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

import common.pinyinzhuanhuan.CircleImg;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class MeViewItemTop extends RelativeLayout {
    public CircleImg img_left;
    public ImageView img_right;
    public TextView txt_center;

    public MeViewItemTop(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.meview_item_top, this, true);
        img_left = findViewById(R.id.img_left);
        img_right = findViewById(R.id.img_right);
        txt_center = findViewById(R.id.txt_center);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String name = ta.getString(R.styleable.androidMe_text);
        int leftId = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        int rightId = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        boolean hideRightImage = ta.getBoolean(R.styleable.androidMe_hideRightImage, false);

        if (name != null && !name.equals("")) {
            txt_center.setText(name);
        }
        if (leftId != 0) {
            img_left.setImageResource(leftId);
            img_left.setVisibility(View.VISIBLE);
        } else {
            img_left.setVisibility(View.GONE);
        }
        if (rightId != 0) {
            img_right.setImageResource(rightId);
            img_right.setVisibility(View.VISIBLE);
        }
        if (hideRightImage) {
            img_right.setVisibility(View.GONE);
        }
        ta.recycle();
    }
}
