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

public class CommonMoreItem extends ConstraintLayout {
    public View line;
    public TextView centertxt;
    public ImageView left;
    public ImageView right;
    public CommonMoreItem(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_more_item, this, true);
        line =  findViewById(R.id.line);
        centertxt =  findViewById(R.id.centertxt);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        @SuppressLint("CustomViewStyleable") TypedArray ta = context.obtainStyledAttributes(attrs, com.kulala.staticsview.R.styleable.androidMe);
        //title
        String text  = ta.getString(R.styleable.androidMe_text);
        if (text != null && !text.equals("")) {
            centertxt.setText(text);
        } else {
            centertxt.setText("");
        }

        int leftresId = ta.getResourceId(com.kulala.staticsview.R.styleable.androidMe_leftres, 0);
        if (leftresId != 0) {
            left.setImageResource(leftresId);
        }
        boolean isShow = ta.getBoolean(com.kulala.staticsview.R.styleable.androidMe_splitLine, true);
        if (isShow) {
            line.setVisibility(View.INVISIBLE);
        }
        ta.recycle();
    }
}
