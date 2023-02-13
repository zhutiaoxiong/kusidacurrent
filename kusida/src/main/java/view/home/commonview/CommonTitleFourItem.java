package view.home.commonview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;

import org.jetbrains.annotations.NotNull;

public class CommonTitleFourItem extends ConstraintLayout {
    public TextView tv;
    public ImageView  left;
    public ImageView  right;
    public ImageView  right_more;
    public CommonTitleFourItem(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_title_four_item, this, true);
        tv =  findViewById(R.id.tv);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        right_more = findViewById(R.id.right_more);
//        TypedArray ta = context.obtainStyledAttributes(attrs, com.kulala.staticsview.R.styleable.androidMe);
//        //title
//        ta.recycle();
    }
    public void setText(String text){
        tv.setText(text);
    }
}
