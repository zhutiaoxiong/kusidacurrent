package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_system.ODateTime;

import java.util.List;

import model.common.DataViolation;

public class AdapterVioList extends BaseAdapter {
	private LayoutInflater		mInflater;
	private Context				mContext;
	private List<DataViolation>	list;
	protected final int			mItemLayoutId;
	public AdapterVioList(Context context, List<DataViolation> list, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = list;
		this.mItemLayoutId = itemLayoutId;
	}
	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
	}

	@Override
	public DataViolation getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.list_item_violation, null);
			holder.txt_fee = (TextView) convertView.findViewById(R.id.txt_fee);
			holder.txt_place = (TextView) convertView.findViewById(R.id.txt_place);
			holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();//
		}
		DataViolation info = list.get(position);
		// 设显示数据
		holder.txt_time.setText(ODateTime.time2StringOnlyDate(info.time));
		holder.txt_place.setText(info.area);
		holder.txt_fee.setText(mContext.getResources().getString(R.string.the_window_has_been_opened)+info.fee+mContext.getResources().getString(R.string.yuan));
		return convertView;
	}
	// ===================================================
	public final class ViewHolder {
		public TextView	txt_time,txt_place,txt_fee;
	}
	// ===================================================

}
