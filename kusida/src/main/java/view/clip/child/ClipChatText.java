package view.clip.child;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.client.proj.kusida.R;

public class ClipChatText extends TextView {

	private Paint	paint;
//	private Paint   paintGray;
	private boolean	pointLeftt;
	public ClipChatText(Context context, AttributeSet attrs) {
		super(context, attrs);
		//绿底
		paint = new Paint();
		paint.setColor(getResources().getColor(R.color.green_light));
		//边框
//		paintGray.setColor(getResources().getColor(R.color.gray_dark));
//		paintGray.setStyle(Style.STROKE);
	}

	public void pointLeft(boolean left) {
		pointLeftt = left;
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Point sceneSize = new Point(canvas.getWidth(), canvas.getHeight());
		Path path = new Path();
		if (pointLeftt) {
			canvas.drawRoundRect(new RectF(20, 0, sceneSize.x, sceneSize.y), 20, 20, paint);
			path.moveTo(25, 20);// 此点为多边形的起点
			path.lineTo(25, 60);
			path.lineTo(0, 40);
			path.close(); // 使这些点构成封闭的多边形
		}else{
			canvas.drawRoundRect(new RectF(0, 0, sceneSize.x-20, sceneSize.y), 20, 20, paint);
			path.moveTo(sceneSize.x-25, 20);// 此点为多边形的起点
			path.lineTo(sceneSize.x-25, 60);
			path.lineTo(sceneSize.x, 40);
			path.close(); // 使这些点构成封闭的多边形
		}
		canvas.drawPath(path, paint);
		super.onDraw(canvas);
	}
}
