package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import model.loginreg.DataUser;

public class AdapterShowUserList extends BaseAdapter {
	private LayoutInflater	mInflater;
	private Context			mContext;
	private List<DataUser>	list;
	private int				currentPosition	= 0;	// 默认选中第一个
	protected final int		mItemLayoutId;
	public AdapterShowUserList(Context context, List<DataUser> listUser, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = listUser;
		this.mItemLayoutId = itemLayoutId;
	}
	public DataUser getCurrentData(){
		if(list==null || list.size()==0)return null;
		return list.get(currentPosition);
	}

	@Override
	public int getCount() {
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
			convertView = mInflater.inflate(R.layout.list_item_name_check_pair, null);
			holder.textView = (TextView) convertView.findViewById(R.id.textView);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		holder.textView.setText(list.get(position).name + ":" + list.get(position).phoneNum);
		holder.imageView.setImageResource(R.drawable.select_false);
		// ÿ����ʾˢ��
		if (currentPosition == position) {
			list.get(position).isSelected = true;
			holder.imageView.setImageResource(R.drawable.select_true);
		} else {
			list.get(position).isSelected = false;
			holder.imageView.setImageResource(R.drawable.select_false);
		}
		holder.textView.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				currentPosition = position;
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	// ===================================================
	public List<DataUser> getDataList() {
		return list;
	}
	public final class ViewHolder {
		public TextView		textView;
		public ImageView	imageView;
	}
	// ===================================================

}
