package view.view4me.userinfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;

import common.GlobalContext;
@Deprecated
public class ClipPopChooseSex {
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;
	private RelativeLayout thisView;
	private OCallBack    callback;

	private TextView     txt_boy,txt_girl,txt_cancel;
	// ========================out======================
	private static ClipPopChooseSex _instance;
	public static ClipPopChooseSex getInstance() {
		if (_instance == null)
			_instance = new ClipPopChooseSex();
		return _instance;
	}
	//===================================================
	public void showAsDropdown(View parentView,OCallBack callback) {
		this.parentView = parentView;
		this.callback = callback;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (RelativeLayout)layoutInflater.inflate(R.layout.view_me_userinfo_sex_b, null);
		txt_boy = (TextView) thisView.findViewById(R.id.txt_boy);
		txt_girl = (TextView) thisView.findViewById(R.id.txt_girl);
		txt_cancel = (TextView) thisView.findViewById(R.id.txt_cancel);
		initViews();
		initEvents();
	}
	public void initViews() {
		popContain = new PopupWindow(thisView);
		popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popContain.setFocusable(true);//设了这个就不能点外面了
		popContain.setTouchable(true);
		popContain.setOutsideTouchable(true);
		Drawable dw = GlobalContext.getContext().getResources().getDrawable(R.color.background_all);//no color no initClick
		popContain.setBackgroundDrawable(dw);
//		popContain.setAnimationStyle(R.style.WindowEnterExitAnimation);
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
		popContain.showAsDropDown(parentView,0, 0);
	}
	public void initEvents() {
		txt_boy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callback.callback("selectBoy", null);
				exitThis();
			}
		});
		txt_girl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callback.callback("selectGirl", null);
				exitThis();
			}
		});
		txt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				exitThis();
			}
		});
	}
	private void exitThis(){
		parentView = null;
		thisView = null;
		callback = null;
		context = null;
		popContain.dismiss();
	}
}

