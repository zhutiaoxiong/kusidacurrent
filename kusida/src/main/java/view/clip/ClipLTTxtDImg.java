package view.clip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

public class ClipLTTxtDImg extends RelativeLayout {
	public ImageView image;
	public TextView  txt_left;
	public ClipLTTxtDImg(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_t_txt_d_image, this, true);
		txt_left = (TextView) findViewById(R.id.txt_left);
		image = (ImageView) findViewById(R.id.image);
		@SuppressLint({"Recycle", "CustomViewStyleable"}) TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.androidMe);
		String lefttxt = ta.getString(R.styleable.androidMe_lefttxt);
		if(lefttxt!=null && !lefttxt.equals("")){
			txt_left.setText(lefttxt);
		}
	}
}
