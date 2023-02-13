package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;

import common.GlobalContext;
import model.common.DataPicker;

public class AdapterPicker extends BaseAdapter{
	private LayoutInflater	mInflater;
	private Context			mContext;
	private List<DataPicker>	list;
	private int				currentPosition;
	protected final int		mItemLayoutId;
	// private boolean checkNeed = false;
	// private int imageId = 0;
	public AdapterPicker(Context context, List<DataPicker> listpicker, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = listpicker;
		this.mItemLayoutId = itemLayoutId;
	}
	public DataPicker getCurrentItem() {
		return list.get(currentPosition%list.size());
	}
	public void setCurrentPos(int pos) {
		currentPosition = pos;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return Integer.MAX_VALUE;
	}

	@Override
	public DataPicker getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {// ��ʼ��
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_onlytext_wrap, null);
			holder.text_view = (TextView) convertView.findViewById(R.id.text_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		// 设初始显示数据
		String info = list.get(position%list.size()).name;
		holder.text_view.setText(info);
		if(position == currentPosition){
			holder.text_view.setTextColor(GlobalContext.getContext().getResources().getColor(R.color.blue));
			holder.text_view.setTextSize(18);
		}else{
			holder.text_view.setTextColor(GlobalContext.getContext().getResources().getColor(R.color.gray_dark));
			holder.text_view.setTextSize(16);
		}
		return convertView;
	}
	// ===================================================
	public final class ViewHolder {
		public TextView	text_view;
	}
	// ===================================================

}
