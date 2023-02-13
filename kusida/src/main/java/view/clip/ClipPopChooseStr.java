package view.clip;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;

import view.view4me.set.ClipTitleMeSet;

public class ClipPopChooseStr{
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;
	private String[] data;

	private        LinearLayout     thisView;
	private ClipTitleMeSet title_head;
	private        ListView         listView;
	private        String           mark;//选择标记
	private        OCallBack        callback;
	// ========================out======================
	private static ClipPopChooseStr _instance;
	public static ClipPopChooseStr getInstance() {
		if (_instance == null)
			_instance = new ClipPopChooseStr();
		return _instance;
	}
	//===================================================
	public void show(View parentView,String[] strArr,String titleStr,String mark,OCallBack callback) {
		if(strArr == null)return;
		this.mark = mark;
		this.callback = callback;
		this.parentView = parentView;
		context = parentView.getContext();
		this.data = strArr;
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (LinearLayout)layoutInflater.inflate(R.layout.clip_title_list, null);
		title_head = (ClipTitleMeSet) thisView.findViewById(R.id.title_head);
		listView = (ListView) thisView.findViewById(R.id.list_names);
		ListAdapter adapter = new ArrayAdapter<String>(context,R.layout.list_item_onlytext,this.data);
		listView.setAdapter(adapter);
		initViews();
		initEvents();
		title_head.setTitle(titleStr);
	}
	public void addRightImage(int resId){
		title_head.setRightRes(resId);
		title_head.img_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.callback("ClipPopChooseStr_Click_Right", "");
				popContain.dismiss();
			}
		});
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
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				String clickStr = data[position];
				callback.callback(mark, clickStr);
				popContain.dismiss();
			}
		});
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
 				popContain.dismiss();
			}
		});
	}
}

