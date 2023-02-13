package view.clip.me;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;

import com.kulala.staticsview.titlehead.ClipTitleHead;
@Deprecated
public class ClipPopShowLicence{
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;

	private        LinearLayout       thisView;
	private        ClipTitleHead      title_head;
	private        TextView           txt_info;
	// ========================out======================
	private static ClipPopShowLicence _instance;
	public static ClipPopShowLicence getInstance() {
		if (_instance == null)
			_instance = new ClipPopShowLicence();
		return _instance;
	}
	//===================================================
	public void show(View parentView) {
		this.parentView = parentView;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (LinearLayout)layoutInflater.inflate(R.layout.clip_pop_show_licence, null);
		title_head = (ClipTitleHead) thisView.findViewById(R.id.title_head);
		txt_info = (TextView) thisView.findViewById(R.id.txt_info);
		txt_info.setText(Html.fromHtml((context.getResources().getString(R.string.me_licence))));
		initViews();
		initEvents();
	}
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	public void initViews() {
		popContain = new PopupWindow(thisView);
		popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		popContain.setFocusable(true);
		popContain.setTouchable(true);
		popContain.setOutsideTouchable(true);
		popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
		popContain.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popContain.dismiss();
					return true;
				}
				return false;
			}
		});
		popContain.showAtLocation(parentView, Gravity.BOTTOM,  0, 0);
	}
	public void initEvents() {
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				popContain.dismiss();
			}
		});
	}
}

