package view.clip.carmanager;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.dataType.NameValuePair;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_view_change.ODipToPx;


import common.GlobalContext;
import model.ManagerCommon;
import model.common.DataBrands;

import java.util.ArrayList;
import java.util.List;

import view.clip.letter.LetterBaseListAdapter;
import view.clip.letter.LetterListView;
import view.view4me.set.ClipTitleMeSet;

/**
 * 品牌选择器
 */
public class ClipPopChooseBrand {
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;
	private String[] data;

	private RelativeLayout thisView;
	private ClipTitleMeSet title_head;

	private TestAdapter adapter;
	private String mark;//选择标记
	private OCallBack callback;
	private LetterListView letter_listview ;
//	public boolean isShowing = false;
	// ========================out======================
	private static ClipPopChooseBrand _instance;
	public static ClipPopChooseBrand getInstance() {
		if (_instance == null)
			_instance = new ClipPopChooseBrand();
		return _instance;
	}
	//===================================================
	public void show(View parentView,String mark,OCallBack callback) {
//		if(isShowing)return;
//		isShowing = true;
		this.mark = mark;
		this.callback = callback;
		this.parentView = parentView;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		if(thisView == null){
//			thisView = new LetterListView(context,null);
			thisView = (RelativeLayout) layoutInflater.inflate(R.layout.pop_car_brand, null);
			letter_listview=thisView.findViewById(R.id.letter_listview) ;
			title_head = letter_listview.title_head;
			if(adapter == null)adapter = new TestAdapter();
			letter_listview.setAdapter(adapter);
			title_head.setTitle(GlobalContext.getContext().getResources().getString(R.string.select_the_vehicle_brands));
		}

		initViews();
		initEvents();

	}
	private void exitThis(){
		if(popContain!=null)popContain.dismiss();
		parentView = null;
		callback = null;
		thisView = null;
		context = null;
		popContain = null;

//		isShowing = false;
	}
	public void exit(){
		exitThis();
	}
	public void initViews() {
		if(popContain==null){
			popContain = new PopupWindow(thisView);
		}
		popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		popContain.setFocusable(true);
		popContain.setTouchable(true);
		popContain.setOutsideTouchable(true);
		popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
		popContain.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					exitThis();
					return true;
				}
				return false;
			}
		});
		popContain.showAtLocation(parentView, Gravity.BOTTOM,  0, 0);
	}
	public void initEvents() {
		letter_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				NameValuePair clickvv = (NameValuePair)letter_listview.mListView.getAdapter().getItem(position);
				String clickStr = clickvv.getValue();
				if(clickStr.length()>1) {
					callback.callback(mark, clickStr);
					exitThis();
				}
			}
		});
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				exitThis();
			}
		});
	}

	class TestAdapter extends LetterBaseListAdapter<NameValuePair>
	{
		/** 字母对应的key,因为字母是要插入到列表中的,为了区别,所有字母的item都使用同一的key.  **/
		private static final String LETTER_KEY = "letter";

		/** 这里的数据都已经按着字母排序好了, 所以传入进来的数据也应排序好,不然会出现跳转问题.  **/
//		String[] dataArray = {
//				"鞍山", "案场", "白宫", "白云", "白俄", "长沙", "常州", "常熟", "大厂", "大娜迦",
//				"福州", "福建", "富豪", "广州", "湖南", "湖北", "胡同", "加州","加拉大", "家具","奇点",
//				"开门", "开始", "可能", "连接", "利用","煤化工", "密度","漫画",  "你好", "你的",
//				"哪些", "欧版", "排行", "贫困", "平时", "请问", "确认", "其他", "染发", "让他",
//				"头像", "是个", "数据", "天空", "退出", "提示", "为空", "维护", "新建", "想到",
//				"用户", "阅读", "知道", "这本", "足球"};
		String[] dataArray = DataBrands.getBrandsArr(ManagerCommon.getInstance().getBrandsList());
		public TestAdapter()
		{
			super();

			List<NameValuePair> dataList = new ArrayList<NameValuePair>();
			for(int i=0; i<dataArray.length; i++)
			{
				NameValuePair pair = new NameValuePair(String.valueOf(i), dataArray[i]);
				dataList.add(pair);
			}
			setContainerList(dataList);
		}

		@Override
		public Object getItem(int position)
		{
			return list.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public String getItemString(NameValuePair t)
		{
			return t.getValue();
		}

		@Override
		public NameValuePair create(char letter)
		{
			return new NameValuePair(LETTER_KEY, String.valueOf(letter));
		}

		@Override
		public boolean isLetter(NameValuePair t)
		{
			//判断是不是字母行,通过key比较,这里是NameValuePair对象,其他对象,就由你自己决定怎么判断了.
			return t.getName().equals(LETTER_KEY);
		}

		@Override
		public View getLetterView(int position, View convertView, ViewGroup parent)
		{
			//这里是字母的item界面设置.
			if(convertView == null)
			{
				convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_onlytext, null);
//				convertView = new TextView(ClipPopChooseBrand.this.context);
				((TextView)convertView).setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				((TextView)convertView).setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(parent.getContext(),35)));
				convertView.setBackgroundColor(ClipPopChooseBrand.this.context.getResources().getColor(R.color.white));
			}
			((TextView)convertView).setText(list.get(position).getValue());

			return convertView;
		}

		@Override
		public View getContainerView(int position, View convertView, ViewGroup parent)
		{
			//这里是其他正常数据的item界面设置.
			if(convertView == null)
			{
				convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_onlytext, null);
//				convertView = new TextView(ClipPopChooseBrand.this.context);
				((TextView)convertView).setGravity(Gravity.CENTER);
				((TextView)convertView).setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,ODipToPx.dipToPx(parent.getContext(),45)));
//				((TextView)convertView).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,ODipToPx.dipToPx(parent.getContext(),45)));
				convertView.setBackgroundColor(ClipPopChooseBrand.this.context.getResources().getColor(R.color.white));
			}
			((TextView)convertView).setText(list.get(position).getValue());

			return convertView;
		}
	}
}

