package view.home.commonview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;

import org.jetbrains.annotations.NotNull;

public class CommonTitletem extends ConstraintLayout {
    public TextView title;
    public ImageView back;
    public ImageView right;
    public CommonTitletem(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_title_item, this, true);
        title =  findViewById(R.id.my_title);
        back = findViewById(R.id.back);
        right= findViewById(R.id.right);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String name = ta.getString(R.styleable.androidMe_text);
        if (name != null && !name.equals("")) {
            title.setText(name);
        } else {
            title.setText("");
        }
        int rightResId = ta.getResourceId(com.kulala.staticsview.R.styleable.androidMe_rightres, 0);
        if(rightResId!=0){
            right.setImageResource(rightResId);
        }
        ta.recycle();
    }
}
