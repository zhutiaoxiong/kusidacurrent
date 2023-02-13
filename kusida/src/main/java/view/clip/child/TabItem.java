package view.clip.child;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.client.proj.kusida.R;

/**
 * 四主页面下方item
 */
public class TabItem extends LinearLayout {
	public  ImageView tab_image;
//	private TextView  tab_text;
	public TabItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_item_tab, this, true);
		tab_image = (ImageView) findViewById(R.id.tab_image);
//		tab_text = (TextView) findViewById(R.id.tab_text);
	}
	public void show(int imgResid){
		tab_image.setImageResource(imgResid);
//		tab_text.setText(showText);
//		tab_text.setTextColor(getResources().getColor(txtColorRes));
	}
}
