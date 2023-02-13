package common.global;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/1/25.
 */

public class TextViewEC extends androidx.appcompat.widget.AppCompatTextView {
    public TextViewEC(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        AssetManager assets = context.getAssets();
        Typeface     font   = Typeface.createFromAsset(assets, "digital-7.ttf");
        setTypeface(font);
    }
}
