package view.clip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.GlobalContext;

public class ClipPopChooseHead{
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;

	private LinearLayout thisView;
	private GridView gridview;
	private OCallBack callback;
	private static int[] images = {
		R.drawable.head_1, R.drawable.head_2, R.drawable.head_3, R.drawable.head_4,
		R.drawable.head_5, R.drawable.head_6, R.drawable.head_7, R.drawable.head_8};
	// ========================out======================
	private static ClipPopChooseHead _instance;
	public static ClipPopChooseHead getInstance() {
		if (_instance == null)
			_instance = new ClipPopChooseHead();
		return _instance;
	}
	//===================================================
	public void show(View parentView,OCallBack callback) {
		this.parentView = parentView;
		this.callback = callback;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (LinearLayout)layoutInflater.inflate(R.layout.clip_pop_choose_head, null);
		gridview = (GridView) thisView.findViewById(R.id.gridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridview.setAdapter(getAdapter());
		initViews();
		initEvents();
	}
	/**加载gridview*/
	private ListAdapter getAdapter() {
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("image", images[i]);
			data.add(item);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(ClipPopChooseHead.this.context, data, R.layout.grid_item_single_image, new String[]{"image"}, new int[]{R.id.imageView});
		return simpleAdapter;
	}
	public void initViews() {
		popContain = new PopupWindow(thisView);
		popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popContain.setFocusable(true);//设了这个就不能点外面了
		popContain.setTouchable(true);
		popContain.setOutsideTouchable(true);
		Drawable dw = GlobalContext.getContext().getResources().getDrawable(R.color.background_all);//no color no initClick
		popContain.setBackgroundDrawable(dw);
		popContain.setAnimationStyle(R.style.WindowEnterExitAnimation);
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
		popContain.showAtLocation(parentView,Gravity.BOTTOM, 0, 0);
	}
	public void initEvents() {
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				callback.callback("chooseHead", position+1);
				popContain.dismiss();
			}
		});
	}
}

