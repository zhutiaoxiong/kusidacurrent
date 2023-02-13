package view.clip.child;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.FrameLayoutBase;
import com.kulala.staticsview.static_interface.OCallBack;

import java.util.List;

import adapter.AdapterPicker;
import model.common.DataPicker;
public class ClipPicker extends FrameLayoutBase{
	private ListView		list_values;
	private TextView		view1, view2;
	private AdapterPicker	adapter;
	private int				currentPos;

	private String			mark;			// 选择标记
	private OCallBack		callback;
	private List<DataPicker> dataArr;
	public ClipPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_picker, this, true);
		list_values = (ListView) findViewById(R.id.list_values);
		view1 = (TextView) findViewById(R.id.view1);
		view2 = (TextView) findViewById(R.id.view2);

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
		String name = ta.getString(R.styleable.androidMe_text);
		if (name != null && !name.equals("")) {
			view1.setText(name);
			view2.setText(name);
		}
		initViews();
		initEvents();
		ta.recycle();
	}
	/** setdata **/
	public void setDataArray(List<DataPicker> dataArr, int value, String mark, OCallBack callback) {
		this.mark = mark;
		this.callback = callback;
		this.dataArr = dataArr;
		currentPos = value;
		handleChangeData();
	}
	public int getCurrentNumber() {
		return adapter.getCurrentItem().value;
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		super.receiveEvent(eventName, paramObj);
	}
	@Override
	public void initViews() {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view1.measure(w, h);
		int width = view1.getMeasuredWidth();
		android.view.ViewGroup.LayoutParams params = list_values.getLayoutParams();
		params.width = width;
		list_values.setLayoutParams(params);
	}
	private int	firstVisibleNum	= 0;
	@Override
	public void initEvents() {
		list_values.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == 0) {
					adapter.setCurrentPos(firstVisibleNum + 2);
					list_values.setSelection(firstVisibleNum + 1);
					callback.callback(mark, null);
				} else if (scrollState == 1) {
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (adapter == null)
					return;
				if (firstVisibleItem <= 2) {
					currentPos = adapter.getCount() + 2;
					list_values.setSelection(currentPos);
				} else if (firstVisibleItem + visibleItemCount > adapter.getCount() - 2) {
					currentPos = firstVisibleItem - adapter.getCount();
					list_values.setSelection(currentPos);
				}
				firstVisibleNum = firstVisibleItem;
			}
		});
	}
	@Override
	public void callback(String key, Object value) {
		super.callback(key, value);
	}

	@Override
	protected void onDetachedFromWindow() {
		list_values.setAdapter(null);
		adapter = null;
		super.onDetachedFromWindow();
	}

	@Override
	public void invalidateUI() {
		if (dataArr.size() < 3)
			return;
		adapter = new AdapterPicker(getContext(), dataArr, R.layout.list_item_onlytext_wrap);
		list_values.setAdapter(adapter);
		// 计算移位
		int pos = 0;
		for (int i = 0; i < dataArr.size(); i++) {
			DataPicker data = dataArr.get(i);
			if (data.value == currentPos)
				pos = i;
		}
		int itemPos = Integer.MAX_VALUE / 2;
		int cut = itemPos / dataArr.size();
		int pp = cut * dataArr.size() + pos - 1;
		// 计算移位
		adapter.setCurrentPos(pp + 1);
		list_values.setSelection(pp);
	}
}
