package view.find;

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
public class ProItemTop extends RelativeLayout {
    public TextView txt_center;

    public ProItemTop(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pro_item_top, this, true);
        txt_center = findViewById(R.id.txt_center);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String name = ta.getString(R.styleable.androidMe_text);
        if (name != null && !name.equals("")) {
            txt_center.setText(name);
        }
        ta.recycle();
    }
}
