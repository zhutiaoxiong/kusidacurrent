package view.home.commonview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;

import org.jetbrains.annotations.NotNull;

public class CommonTopItem extends ConstraintLayout {
    public EditText  device_input;
    public ImageView iv;
    public ImageView  scan;
    public ImageView  back;
    public CommonTopItem(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_top_item, this, true);
        device_input =  findViewById(R.id.device_input);
        iv =  findViewById(R.id.iv);
        scan = findViewById(R.id.scan);
        back = findViewById(R.id.back);
        @SuppressLint("CustomViewStyleable") TypedArray ta = context.obtainStyledAttributes(attrs, com.kulala.staticsview.R.styleable.androidMe);
        boolean isShow = ta.getBoolean(com.kulala.staticsview.R.styleable.androidMe_splitLine, true);
        if (!isShow) {
            back.setVisibility(View.INVISIBLE);
        }
        ta.recycle();
    }
}
