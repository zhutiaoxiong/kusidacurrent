package view.home.commonview;

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

public class DeviceItem extends ConstraintLayout {
    private TextView tv2;
    private ImageView iv;
    public TextView tv1;
    private TextView tv3;
    public DeviceItem(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_device_item, this, true);
        tv2 =  findViewById(R.id.tv2);
        iv =  findViewById(R.id.iv);
        tv1 = findViewById(R.id.tv1);
        tv3 =  findViewById(R.id.tv3);
        TypedArray ta = context.obtainStyledAttributes(attrs, com.kulala.staticsview.R.styleable.androidMe);
        //title
        String text  = ta.getString(R.styleable.androidMe_text);
        if (text != null && !text.equals("")) {
            tv2.setText(text);
        } else {
            tv2.setText("");
        }

        int resId = ta.getResourceId(com.kulala.staticsview.R.styleable.androidMe_res, 0);
        if (resId != 0) {
            iv.setImageResource(resId);
        }
        ta.recycle();
    }
    public void setTextAndBackGround(boolean isDefault,int count){
        if(isDefault){
            tv1.setText("当前默认");
            tv1.setBackgroundResource(R.drawable.main_select_default);
        }else{
            tv1.setText("设为默认");
            tv1.setBackgroundResource(R.drawable.main_select_red);
        }
        tv3.setText("设备:"+count);
    }
    public void setBackGround(boolean isDefault){
        if(isDefault){
            tv1.setText("当前默认");
            tv1.setBackgroundResource(R.drawable.main_select_default);
        }else{
            tv1.setText("设为默认");
            tv1.setBackgroundResource(R.drawable.main_select_red);
        }
    }
}
