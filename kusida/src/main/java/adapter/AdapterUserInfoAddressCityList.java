package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;

public class AdapterUserInfoAddressCityList extends BaseAdapter{
	private LayoutInflater    mInflater;
	private Context           mContext;
	private List<String> list;
	private int               currentPosition;
	public AdapterUserInfoAddressCityList(Context context, List<String> list) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = list;
	}
	public String getCurrentItem() {
		return list.get(currentPosition);
	}
	public void setCurrentItem(int position) {
		currentPosition = position;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.clip_line_btntxt, null);
			holder.img_left = (ImageView) convertView.findViewById(R.id.img_left);
			holder.img_right = (ImageView) convertView.findViewById(R.id.img_right);
			holder.img_splitline = (ImageView) convertView.findViewById(R.id.img_splitline);
			holder.img_red_point = (ImageView) convertView.findViewById(R.id.img_red_point);
			holder.txt_info = (TextView) convertView.findViewById(R.id.txt_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		// 设初始显示数据
		String info = list.get(position);
		holder.img_left.setVisibility(View.GONE);
		holder.img_right.setImageResource(R.drawable.right_chacha);
		holder.img_red_point.setVisibility(View.GONE);
		holder.txt_info.setText(info);
		holder.img_splitline.setVisibility(View.INVISIBLE);
		return convertView;
	}
	// ===================================================
	public List<String> getDataList() {
		return list;
	}
	public final class ViewHolder {
		public ImageView img_left, img_right, img_splitline, img_red_point;
		private TextView txt_info;
	}


}
