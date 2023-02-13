package view.clip;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import adapter.AdapterWarnings;
import model.ManagerWarnings;
import model.carcontrol.DataWarnings;

/**
 * 弹出四警告
 */
public class ClipPopWarning {
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;

	private RelativeLayout thisView; 
	private View view_background;
	private ListView list;
	private TextView title;
	private String mark;//选择标记
	private OCallBack callback;
	
	private List<DataWarnings> adapter;
	// ========================out======================
	private static ClipPopWarning _instance;
	public static ClipPopWarning getInstance() {
		if (_instance == null)
			_instance = new ClipPopWarning();
		return _instance;
	}
	//===================================================
	public void show(View parentView,List<DataWarnings> listRecords,String mark,OCallBack callback) {
		this.mark = mark;
		this.callback = callback;
		this.parentView = parentView;
		this.adapter = listRecords;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (RelativeLayout)layoutInflater.inflate(R.layout.clip_warning_list, null);
		view_background = (View) thisView.findViewById(R.id.view_background);
		list = (ListView) thisView.findViewById(R.id.list);
		title = (TextView) thisView.findViewById(R.id.title);
		
		title.setText(context.getResources().getString(R.string.received_a_new_message)+ManagerWarnings.getInstance().getNewWarningNum()+context.getResources().getString(R.string.article));
		AdapterWarnings adapter = new AdapterWarnings(context, listRecords, R.layout.list_item_warnings);
		list.setAdapter(adapter);
		initViews();
		initEvents();
	}
	public void initViews() {
		popContain = new PopupWindow(thisView);
		popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		popContain.setFocusable(true);
		popContain.setTouchable(true);
		popContain.setOutsideTouchable(true);
//		popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
		popContain.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					list.setAdapter(null);
					exitThis();
					return true;
				}
				return false;
			}
		});
		popContain.showAtLocation(parentView, Gravity.BOTTOM,  0, 0);
	}
	public void initEvents() {
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				popContain.dismiss();
				callback.callback(mark, adapter.get(position));
				exitThis();
			}
		});
		view_background.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				callback.callback(mark+"_cancel", null);
				list.setAdapter(null);
				exitThis();
			}
		});
	}
	private void exitThis(){
		callback = null;
		context = null;
		list.setAdapter(null);
		adapter = null;
		popContain.dismiss();
	}
}

