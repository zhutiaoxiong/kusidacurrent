package view.clip.child;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


import com.client.proj.kusida.R;

import view.EquipmentManager;

/**
 * 启动车的时间选择
 */
public class ClipStartCarMinutes extends ImageView {
	private Paint paint_black;
	private Paint paint_blue;
	private float moveX;
	private int selectValue = 10;
	private boolean isTouched = false;
	public ClipStartCarMinutes(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint_black = new Paint();
		paint_black.setColor(getResources().getColor(R.color.white));
		paint_black.setAlpha(170);
		paint_black.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
		paint_black.setStrokeWidth(3.0f);
		paint_blue = new Paint();
		paint_blue.setColor(getResources().getColor(R.color.red_dark));
		paint_blue.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
		paint_blue.setStrokeWidth(3.0f);
	}
	public void setChooseNum(int time){selectValue = time;}
	public int getChooseNum(){
		return selectValue;
	}
	public void setLineColor(String color){
		paint_black.setColor(Color.parseColor(color));
		invalidate();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() ==MotionEvent.ACTION_MOVE){
			moveX= event.getX();
			isTouched = true;
			invalidate();
		}
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Point sceneSize = new Point(canvas.getWidth(), canvas.getHeight());
		int splitWidth = sceneSize.x/9;
		if(!isTouched){
			moveX = splitWidth*selectValue/5;
		}
		//黑线底
		canvas.drawLine(splitWidth, 30, sceneSize.x-splitWidth, 30, paint_black);
		//draw black Point
		for(int i=1;i<9;i++){
			canvas.drawCircle(splitWidth*i, 30, 5.0f, paint_black);
		}
		//draw black text
		paint_black.setStrokeWidth(1.0f);
		paint_black.setTextSize(30.0f);
		for(int i=1;i<9;i++){
			if(EquipmentManager.isMinJiaQiang()){
				if(i==1){
					canvas.drawText("0.8", splitWidth*i-15, 80, paint_black);
				}else if(i==2){
					canvas.drawText("1", splitWidth*i-15, 80, paint_black);
				}else if(i==3){
					canvas.drawText("1.2", splitWidth*i-15, 80, paint_black);
				}else{
					canvas.drawText(""+(float)(1.2+(i-3)*0.3), splitWidth*i-15, 80, paint_black);
				}
			}else{
				canvas.drawText(""+(i*5), splitWidth*i-15, 80, paint_black);
			}
		}
		//draw black text min
		if(EquipmentManager.isMinJiaQiang()){
			canvas.drawText("秒", sceneSize.x-splitWidth+20+10, 80, paint_black);
		}else{
			canvas.drawText(getContext().getResources().getString(R.string.mark), sceneSize.x-splitWidth+20+10, 80, paint_black);
		}
		//蓝线
		if(moveX<splitWidth ){
			selectValue = 5;
			canvas.drawCircle(splitWidth, 30, 15.0f, paint_blue);
		}else if(moveX>sceneSize.x-splitWidth){
			selectValue = 40;
			canvas.drawLine(splitWidth, 30, sceneSize.x-splitWidth, 30, paint_blue);//line
			for(int i=1;i<moveX/splitWidth;i++){
				canvas.drawCircle(splitWidth*i, 30, 5.0f, paint_blue);//Point
			}
			canvas.drawCircle(sceneSize.x-splitWidth, 30, 15.0f, paint_blue);//bigger
		}else{
			// time只有0到7档，每档5分钟，0档位5分钟
//			canvas.drawLine(splitWidth, 30, moveX, 30, paint_blue);//line
//			canvas.drawCircle(moveX, 30, 15.0f, paint_blue);//bigger
			int maxP = 0;
			for(int i=1;i<=moveX/splitWidth;i++){
				canvas.drawCircle(splitWidth*i, 30, 5.0f, paint_blue);//Point
				maxP = i;
			}
			canvas.drawLine(splitWidth, 30, maxP*splitWidth, 30, paint_blue);//line
			canvas.drawCircle(maxP*splitWidth, 30, 15.0f, paint_blue);//bigger
			selectValue = 5*maxP;
		}
		super.onDraw(canvas);
	}

}
