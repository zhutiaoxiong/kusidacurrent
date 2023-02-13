package view.view4control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ViewFan extends View {

	private Paint paint;
	private Bitmap bitmap_fan;
	public ViewFan(Context context, AttributeSet attrs) {
		super(context, attrs);
//		bitmap_fan = BitmapFactory.decodeResource(getResources(), R.drawable.fan);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		paint = new Paint();
		int size = bitmap_fan.getWidth();
		Point sceneSz = new Point(canvas.getWidth(),canvas.getHeight());
		float sc = sceneSz.y/10f/size;
		float sizefan = size*sc;
		Rect rec = new Rect((int)(sceneSz.x/2-sizefan/2),
				sceneSz.y*5/40,
				(int)(sceneSz.x/2+sizefan/2),
				(int)(sceneSz.y*5/40+sizefan));
		canvas.drawBitmap(bitmap_fan, null, rec, null);
		super.onDraw(canvas);
	}

}
