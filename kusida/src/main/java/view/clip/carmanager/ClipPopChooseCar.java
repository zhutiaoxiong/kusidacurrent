package view.clip.carmanager;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import adapter.AdapterShowCarList;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import com.kulala.staticsview.titlehead.ClipTitleHead;

public class ClipPopChooseCar{
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;

	private        LinearLayout       thisView;
	private        ClipTitleHead      title_head;
	private        ListView           listView;
	private        AdapterShowCarList adapter;
	private        String             mark;//选择标记
	private        OCallBack          callback;
	// ========================out======================
	private static ClipPopChooseCar   _instance;
	public static ClipPopChooseCar getInstance() {
		if (_instance == null)
			_instance = new ClipPopChooseCar();
		return _instance;
	}
	//===================================================
	public void show(View parentView,String titleStr,String mark,OCallBack callback) {
		this.mark = mark;
		this.callback = callback;
		this.parentView = parentView;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (LinearLayout)layoutInflater.inflate(R.layout.clip_title_list, null);
		title_head = (ClipTitleHead) thisView.findViewById(R.id.title_head);
		listView = (ListView) thisView.findViewById(R.id.list_names);
		title_head.setTitle(titleStr);
		List<DataCarInfo> list = ManagerCarList.getInstance().getCarInfoList();
		if(list == null || list.size()==0)OCtrlCar.getInstance().ccmd1203_getcarlist();
		adapter = new AdapterShowCarList(context, list, R.layout.list_item_carname);
		adapter.setOnlyForChoose();
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
		popContain.setAnimationStyle(R.style.WindowEnterExitAnimation);
		popContain.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					listView.setAdapter(null);
					adapter = null;
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
				adapter.setCurrentItem(position);
				callback.callback(mark, adapter.getCurrentItem());
				popContain.dismiss();
			}
		});
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				listView.setAdapter(null);
				adapter = null;
				popContain.dismiss();
			}
		});
	}
}

