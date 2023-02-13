package view.clip;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;

/**
 * 时间选择器
 */
@Deprecated
public class ClipPopChooseDate{
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;

	private LinearLayout thisView;
	private ClipDatetimePicker timePicker;
	private View touch_exit;
	private String mark;
	private OCallBack call;
	// ========================out======================
	private static ClipPopChooseDate _instance;
	private ClipPopChooseDate() {
	}
	public static ClipPopChooseDate getInstance() {
		if (_instance == null)
			_instance = new ClipPopChooseDate();
		return _instance;
	}
	//===================================================
	public void show(View parentView,String mark,boolean isOnlyDate,OCallBack call) {
		this.mark = mark;
		this.call = call;
		this.parentView = parentView;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (LinearLayout)layoutInflater.inflate(R.layout.clip_pop_datetime, null);
		touch_exit = (View) thisView.findViewById(R.id.touch_exit);
		timePicker = (ClipDatetimePicker) thisView.findViewById(R.id.date_picker);
		timePicker.setTitle(context.getResources().getString(R.string.please_select_a_date));
		if(isOnlyDate)timePicker.setTypeOnlyShowDate();
		initViews();
		initEvents();
	}
	public void initViews() {
		popContain = new PopupWindow(thisView);
		popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popContain.setFocusable(true);
		popContain.setTouchable(true);
		popContain.setOutsideTouchable(true);
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
		timePicker.btn_confirm.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				long time = timePicker.getPickerTime();
				call.callback(mark,time);
				popContain.dismiss();
			}
		});
		timePicker.btn_cancel.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				popContain.dismiss();
			}
		});
		touch_exit.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				popContain.dismiss();
			}
		});
	}
}

