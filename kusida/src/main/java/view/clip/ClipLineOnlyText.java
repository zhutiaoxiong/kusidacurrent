package view.clip;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ClipLineOnlyText extends RelativeLayout {
	public  ImageView img_splitline;
	private TextView  txt_show;
	public ClipLineOnlyText(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_line_only_text, this, true);
		img_splitline = (ImageView) findViewById(R.id.img_splitline);
		txt_show = (TextView) findViewById(R.id.txt_show);

	    TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.androidMe);   
        String text = ta.getString(R.styleable.androidMe_text);
		boolean splitLine = ta.getBoolean(R.styleable.androidMe_splitLine,true);
		txt_show.setText(text);
		if(splitLine){
			img_splitline.setVisibility(View.VISIBLE);
		}else{
			img_splitline.setVisibility(View.INVISIBLE);
		}
        ta.recycle();
	}
	public void setText(String txt){
		txt_show.setText(txt);
	}
	public String getText(){
		return txt_show.getText().toString();
	}
}
