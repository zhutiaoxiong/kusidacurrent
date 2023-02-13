package view.clip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import com.kulala.staticsview.titlehead.ClipTitleHead;

public class ClipAnswerDropDown {
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;
	private String[] data;

	private LinearLayout  thisView;
	private ClipTitleHead title_head;
	private ListView      listView;
	private String        mark;//选择标记
	private OCallBack     callback;
	//===================================================
	public void show(View parentView,String[] strArr,String mark,OCallBack callback) {
		if(strArr == null)return;
		this.mark = mark;
		this.callback = callback;
		this.parentView = parentView;
		context = parentView.getContext();
		this.data = strArr;
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (LinearLayout)layoutInflater.inflate(R.layout.clip_title_list, null);
		title_head = (ClipTitleHead) thisView.findViewById(R.id.title_head);
		title_head.setVisibility(View.GONE);
		listView = (ListView) thisView.findViewById(R.id.list_names);
		ListAdapter adapter = new ArrayAdapter<String>(context,R.layout.list_item_onlytext,this.data);
		listView.setAdapter(adapter);
		initViews();
		initEvents();
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
		popContain.showAsDropDown(parentView);
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
	}
}

