package common.pinyinzhuanhuan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.client.proj.kusida.R;


/**
 * Created by qq522414074 on 2016/8/5.
 */
public class ClipImageBorderView extends View {
    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding;
    /**
     * 边框的宽度 单位dp
     */
    private int mBorderWidth = 2;

    private Paint mPaint, mPaintBlack;
    private Path path = new Path();

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                        .getDisplayMetrics());
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
////        mPaint.setColor(getResources().getColor(R.color.normal_txt_color_white));
//        mPaint.setColor(Color.parseColor("#FF000000"));
//        mPaint.setStrokeWidth(5);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setAlpha(255);

        mPaintBlack = new Paint();
        mPaintBlack.setColor(getResources().getColor(R.color.all_background_color));
        mPaintBlack.setStyle(Paint.Style.FILL);
        mPaintBlack.setAlpha(125);
//      mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(), 255,
//                Canvas.ALL_SAVE_FLAG);
        canvas.save();
        path.reset();
        path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, Path.Direction.CW);
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        canvas.drawPaint(mPaintBlack);
        canvas.restore();
//        mPaintBlack.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaintBlack);
//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mPaint);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }
}
