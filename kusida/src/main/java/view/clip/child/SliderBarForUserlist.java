package view.clip.child;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

public class SliderBarForUserlist extends View {

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;


    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private int choose = -1;// ѡ�е�
    private Paint paint = new Paint();

    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }


    public SliderBarForUserlist(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SliderBarForUserlist(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SliderBarForUserlist(Context context) {
        super(context);
    }

    private void getTextSize() {
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight() - 30;// ��ȡ��Ӧ�߶�
        int width = getWidth(); //  ��ȡ��Ӧ���

        int singleHeight = height / b.length;// ��ȡÿһ����ĸ�ĸ߶�

        for (int i = 0; i < b.length; i++) {
            //����ÿһ����ĸ����ʽ
            paint.setColor(getResources().getColor(R.color.blue_text));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            int a = ODipToPx.dipToPx(getContext(), 14);
            paint.setTextSize(a);
            //ѡ��״̬
            if (i == choose) {
                paint.setColor(Color.parseColor("#F88701"));
                paint.setFakeBoldText(true);
            }
            // x�������м�-�ַ���һ��
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            //�߶����ӵ���
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// ���û���
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// ���Y���
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);// ���y�����ռ�ܸ߶ȵı���*checkIsServiceInManifist����ĳ��Ⱦ͵��ڵ��checkIsServiceInManifist�еĸ���.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundResource(R.color.white);
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.color.all_background_color);

                break;

            default:
                setBackgroundResource(R.color.all_background_color);
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}