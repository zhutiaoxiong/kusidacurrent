package view.view4control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

/**
 * Created by qq522414074 on 2017/12/16.
 */

public class ViewControlHotOrCold extends View {
    private Paint paintBlue;
    private Paint paintRed;
    private Paint paintGray;
    private Paint paintWhite;
    private float moveX;
    private float paintWidth;
    private boolean isOnTouch=false;
    private int thumbX=4;//七个档位，1,2,3,4,5,6,7
    private  int radius=3;
    private int thumbRadius;


    public ViewControlHotOrCold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        paintWidth= 1.0f;
        paintWidth= ODipToPx.dipToPx(context,1);
        radius= ODipToPx.dipToPx(context,3);
        thumbRadius=ODipToPx.dipToPx(context,16);
        paintBlue = new Paint();
        paintBlue.setColor(Color.parseColor("#0044ce"));
        paintBlue.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
        paintBlue.setStrokeWidth(paintWidth);

        paintRed = new Paint();
        paintRed.setColor(Color.parseColor("#b61a00"));
        paintRed.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
        paintRed.setStrokeWidth(paintWidth);

        paintGray = new Paint();
        paintGray.setColor(getResources().getColor(R.color.gray));
        paintGray.setAlpha(170);
        paintGray.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
        paintGray.setStrokeWidth(paintWidth);

        paintWhite = new Paint();
        paintWhite.setColor(getResources().getColor(R.color.white));
        paintWhite.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
        paintWhite.setStrokeWidth(paintWidth);
    }
    public void setThumbX(int thumbX){
        this.thumbX=thumbX;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                 if (BuildConfig.DEBUG) Log.e("OOOOOO", "DownX: "+event.getX() );
                break;
                case MotionEvent.ACTION_MOVE:
                    moveX= event.getX();
                     if (BuildConfig.DEBUG) Log.e("OOOOOO", "MoveX: "+moveX );
                    isOnTouch = true;
                    invalidate();
                    break;
                    case MotionEvent.ACTION_UP:
                        if(onThumbChangeListner!=null){
                            onThumbChangeListner.thumbChange();
                        }
                         if (BuildConfig.DEBUG) Log.e("OOOOOO", "UpX "+event.getX() );
                        break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Point sceneSize = new Point(canvas.getWidth(), canvas.getHeight());
//         if (BuildConfig.DEBUG) Log.e("OOOOOO", "总长度 "+sceneSize.x );
        Bitmap resizeBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.control_hot_or_cold_thumb)).getBitmap();
        //精确缩放到指定大小
//        Bitmap thumbImgNow = Bitmap.createScaledBitmap(resizeBmp,2*radius,2*radius, true);
        Bitmap thumbImgNow = Bitmap.createScaledBitmap(resizeBmp,thumbRadius,thumbRadius, true);
//        int thumbBitWidth = thumbImgNow.getWidth();
//        int thumbBitHeight = thumbImgNow.getHeight();
        int thumbBitWidth = 2*radius;
        int thumbBitHeight = 2*radius;

//        Matrix matrix = new Matrix();
//        matrix.postScale(0.5f,0.5f); //长和宽放大缩小的比例
//        Bitmap thumb = Bitmap.createBitmap(resizeBmp,0,0,resizeBmp.getWidth(),resizeBmp.getHeight(),matrix,true);
//        int thumbBitWidth = thumb.getWidth();
//        int thumbBitHeight = thumb.getHeight();
//        radius=thumbBitWidth/2;
//        float splitWidth = (sceneSize.x -thumbBitWidth) / 6;//每个圆圈中心点的位置
        float splitWidth = (sceneSize.x -2*radius) / 6;//每个圆圈中心点的位置
        canvas.drawLine(thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, sceneSize.x -thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, paintGray);
        for (int i = 0; i < 7; i++) {
            canvas.drawCircle(thumbBitWidth/2 + splitWidth * i, thumbBitWidth/2+5*paintWidth, radius, paintWhite);
        }
        if(0<moveX&&moveX<=thumbBitWidth/2+splitWidth/2){
            thumbX=1;
        }else if(thumbBitWidth/2+splitWidth/2<moveX&&moveX<=(thumbBitWidth/2+splitWidth+splitWidth/2)){
            thumbX=2;
        }else if((thumbBitWidth/2+splitWidth+splitWidth/2)<moveX&&moveX<=(thumbBitWidth/2+splitWidth*2+splitWidth/2)){
            thumbX=3;
        }else if((thumbBitWidth/2+splitWidth*2+splitWidth/2)<moveX&&moveX<=(thumbBitWidth/2+splitWidth*3+splitWidth/2)){
            thumbX=4;
        }else if((thumbBitWidth/2+splitWidth*3+splitWidth/2)<moveX&&moveX<=(thumbBitWidth/2+splitWidth*4+splitWidth/2)){
            thumbX=5;
        }else if((thumbBitWidth/2+splitWidth*4+splitWidth/2)<moveX&&moveX<=(thumbBitWidth/2+splitWidth*5+splitWidth/2)){
            thumbX=6;
        }else if((thumbBitWidth/2+splitWidth*5+splitWidth/2)<moveX){
            thumbX=7;
        }
        if(thumbX==1){
            canvas.drawLine(3*splitWidth+thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, paintBlue);
            for (int i = 3; i >=0; i--) {
                canvas.drawCircle(thumbBitWidth/2 + splitWidth * i, thumbBitWidth/2+5*paintWidth, radius, paintBlue);
            }
        }else if(thumbX==2){
            canvas.drawLine(3*splitWidth+thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, thumbBitWidth/2+splitWidth, thumbBitHeight/2+5*paintWidth, paintBlue);
            for (int i = 3; i >=1; i--) {
                canvas.drawCircle(thumbBitWidth/2 + splitWidth * i, thumbBitWidth/2+5*paintWidth, radius, paintBlue);
            }
        }else if(thumbX==3){
            canvas.drawLine(3*splitWidth+thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, thumbBitWidth/2+2*splitWidth, thumbBitHeight/2+5*paintWidth, paintBlue);
            for (int i = 3; i >=2; i--) {
                canvas.drawCircle(thumbBitWidth/2 + splitWidth * i, thumbBitWidth/2+5*paintWidth, radius, paintBlue);
            }
        }else if(thumbX==4){
            canvas.drawLine(thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, sceneSize.x -thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, paintGray);
            for (int i = 0; i < 7; i++) {
                canvas.drawCircle(thumbBitWidth/2 + splitWidth * i, thumbBitWidth/2+5*paintWidth, radius, paintWhite);
            }
        }else if(thumbX==5){
            canvas.drawLine(3*splitWidth+thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, thumbBitWidth/2+4*splitWidth, thumbBitHeight/2+5*paintWidth, paintRed);
            for (int i = 3; i <=4; i++) {
                canvas.drawCircle(thumbBitWidth/2 + splitWidth * i, thumbBitWidth/2+5*paintWidth, radius, paintRed);
            }
        }else if(thumbX==6){
            canvas.drawLine(3*splitWidth+thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, thumbBitWidth/2+5*splitWidth, thumbBitHeight/2+5*paintWidth, paintRed);
            for (int i = 3; i <=5; i++) {
                canvas.drawCircle(thumbBitWidth/2 + splitWidth * i, thumbBitWidth/2+5*paintWidth, radius, paintRed);
            }
        }else if(thumbX==7){
            canvas.drawLine(3*splitWidth+thumbBitWidth/2, thumbBitHeight/2+5*paintWidth, thumbBitWidth/2+6*splitWidth, thumbBitHeight/2+5*paintWidth, paintRed);
            for (int i = 3; i <=6; i++) {
                canvas.drawCircle(thumbBitWidth/2 + splitWidth * i, thumbBitWidth/2+5*paintWidth, radius, paintRed);
            }
        }
        if(thumbX==1){
            canvas.drawBitmap(thumbImgNow, 0+(thumbX-1)*splitWidth,0 , null);
        }else if(thumbX==7){
            canvas.drawBitmap(thumbImgNow, 0+(thumbX-1)*splitWidth-thumbRadius/2-radius+2*paintWidth,0 , null);
        }else{
            canvas.drawBitmap(thumbImgNow, 0+(thumbX-1)*splitWidth+radius-thumbRadius/2,0 , null);
        }
//         canvas.drawBitmap(thumbImgNow, 0+(thumbX-1)*splitWidth, 0, null);
//        canvas.drawBitmap(thumb, 0+(thumbX-1)*splitWidth, 0, null);
//         Rect mSrcRect = new Rect(0, 0, 8*radius,8* radius);
//        Rect  mDestRect = new Rect((int)(radius+(thumbX-1)*splitWidth), 0, (int)(0+(thumbX-1)*splitWidth+2*radius), radius*2);
////        Rect  mDestRect = new Rect(50, 0,0, 0);
//        canvas.drawBitmap(thumb,mSrcRect,mDestRect,null);
        if(onThumbChangeListner!=null){
            onThumbChangeListner.backCurrentThumb(thumbX);
        }
        super.onDraw(canvas);
    }
    public interface  OnThumbChangeListner{
        void backCurrentThumb(int level);
        void thumbChange();
    }
    private OnThumbChangeListner onThumbChangeListner;
    public void setOnThumbChangeListner(OnThumbChangeListner onThumbChangeListner){
        this.onThumbChangeListner=onThumbChangeListner;
    }
}
