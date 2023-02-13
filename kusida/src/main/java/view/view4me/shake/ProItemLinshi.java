package view.view4me.shake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ProItemLinshi extends RelativeLayout {
    public TextView txt_bottom;

    public ProItemLinshi(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pro_item_linshi, this, true);
        txt_bottom = findViewById(R.id.txt_bottom);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String bottomStr = ta.getString(R.styleable.androidMe_bottomTxt);
        if (bottomStr != null && !bottomStr.equals("")) {
            txt_bottom.setText(bottomStr);
        }
        ta.recycle();
    }
}
