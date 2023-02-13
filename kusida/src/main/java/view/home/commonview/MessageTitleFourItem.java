package view.home.commonview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;

import org.jetbrains.annotations.NotNull;

public class MessageTitleFourItem extends ConstraintLayout {
    public TextView tv;
    public ImageView  left;
    public ImageView  delete;
    public ImageView  select;
    public MessageTitleFourItem(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.message_title_four_item, this, true);
        tv =  findViewById(R.id.tv);
        left = findViewById(R.id.left);
        select = findViewById(R.id.select);
        delete = findViewById(R.id.delete);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String name = ta.getString(R.styleable.androidMe_text);
        if (name != null && !name.equals("")) {
            tv.setText(name);
        } else {
            tv.setText("报警信息");
        }
        int resId = ta.getResourceId(com.kulala.staticsview.R.styleable.androidMe_res, 0);
        if (resId != 0) {
            select.setImageResource(resId);
        }else{
            select.setImageResource(R.drawable.locator_icon_select);
        }
        boolean isShow = ta.getBoolean(com.kulala.staticsview.R.styleable.androidMe_isShow, true);
        if (isShow) {
            select.setVisibility(View.VISIBLE);
        }else{
            select.setVisibility(View.INVISIBLE);
        }
        ta.recycle();
    }
    public void setText(String text){
        tv.setText(text);
    }
}
