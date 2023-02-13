package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_system.ODateTime;

import java.util.List;

import model.carcontrol.DataWarnings;

public class AdapterWarnings extends BaseAdapter{
	private LayoutInflater	mInflater;
	private Context			mContext;
	private List<DataWarnings>	list;
	protected final int		mItemLayoutId;
	// private boolean checkNeed = false;
	// private int imageId = 0;
	public AdapterWarnings(Context context, List<DataWarnings> listRecords, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = listRecords;
		this.mItemLayoutId = itemLayoutId;
	}
	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.list_item_warnings, null);
			holder.text_info = (TextView) convertView.findViewById(R.id.text_info);
			holder.text_time = (TextView) convertView.findViewById(R.id.text_time);
			holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
			holder.img_new = (ImageView) convertView.findViewById(R.id.img_new);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		// 设初始显示数据
		DataWarnings info = list.get(position);
		holder.text_info.setText(info.content);
		holder.text_time.setText(ODateTime.time2StringWithHH(info.createTime));
		holder.img_icon.setImageResource(info.getResId());
//		if(info.isNew){
//			holder.img_new.setVisibility(View.VISIBLE);
//		}else{
//			holder.img_new.setVisibility(View.INVISIBLE);
//		}
		return convertView;
	}
	// ===================================================
	public List<DataWarnings> getDataList() {
		return list;
	}
	public final class ViewHolder {
		public TextView		text_info, text_time;
		public ImageView	img_icon,img_new;
	}
	// ===================================================

}
