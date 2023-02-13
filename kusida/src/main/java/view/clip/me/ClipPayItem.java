package view.clip.me;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
public class ClipPayItem extends RelativeLayout{
	private ImageView img_check,image;
	private TextView txt_fee,txt_time;
	private LinearLayout lin_board;
	public ClipPayItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_pay_item, this, true);
		image = (ImageView) findViewById(R.id.image);
		img_check = (ImageView) findViewById(R.id.img_check);
		txt_fee = (TextView) findViewById(R.id.txt_fee);
		txt_time = (TextView) findViewById(R.id.txt_timee);
		lin_board = (LinearLayout) findViewById(R.id.lin_board);

	    TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.androidMe);   
        int background = ta.getResourceId(R.styleable.androidMe_background,0);
        if(background!=0){
        	image.setImageResource(background);
        	txt_fee.setVisibility(View.INVISIBLE);
        	txt_time.setVisibility(View.INVISIBLE);
        }else{
        	image.setVisibility(INVISIBLE);
        }
        ta.recycle();
	}
	public void setFee(String Fee){
		txt_fee.setText(Fee);
	}
	public void setTime(String time){
		txt_time.setText(time);
	}
	public boolean getIsChecked(){
		return img_check.getVisibility() == VISIBLE ? true : false;
	}
	public void setIsChecked(boolean checked){
		lin_board.setBackgroundResource(R.drawable.button_selector_allback_color_select);
		if(checked){
			img_check.setVisibility(VISIBLE);
//	    	lin_board.setBackgroundResource(R.drawable.pay_select);
		}else{
			img_check.setVisibility(INVISIBLE);
//	    	lin_board.setBackgroundResource(R.drawable.pay_unselect);
		}
	}
}
