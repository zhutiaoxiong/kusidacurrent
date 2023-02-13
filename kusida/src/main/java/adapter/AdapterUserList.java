package adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.client.proj.kusida.R;

import java.util.List;

import common.GlobalContext;
import model.loginreg.DataUser;

public class AdapterUserList extends BaseAdapter{
	private LayoutInflater	mInflater;
	private Context			mContext;
	private List<DataUser>	list;
	private int				currentPosition;
	protected final int		mItemLayoutId;
	// private boolean checkNeed = false;
	// private int imageId = 0;
	public AdapterUserList(Context context, List<DataUser> listUser, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = listUser;
		this.mItemLayoutId = itemLayoutId;
	}
	/**
	 * @param list
	 */
	public void updateListView(List<DataUser> list){
		this.list = list;
		notifyDataSetChanged();
	}
	public DataUser getCurrentItem() {
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
	public DataUser getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.list_item_head_2string, null);
			holder.text_name = (TextView) convertView.findViewById(R.id.text_name);
			holder.text_phone = (TextView) convertView.findViewById(R.id.text_phone);
			holder.image_head = (ImageView) convertView.findViewById(R.id.image_head);
			holder.image_check = (ImageView) convertView.findViewById(R.id.image_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
//		holder.text_name.setTag(holder);
//		holder.text_phone.setTag(holder);
//		holder.image_head.setTag(holder);
//		holder.image_check.setTag(holder);
		// 设初始显示数据
		DataUser info = list.get(position);
		if (!info.name.equals(""))
			holder.text_name.setText(info.name);
		holder.text_phone.setText(info.phoneNum);
		if(!TextUtils.isEmpty(info.avatarUrl)&&info.avatarUrl.startsWith("http")){
			Glide.with(GlobalContext.getContext())
					.load(info.avatarUrl)
					.into(holder.image_head);
		}else{
			holder.image_head.setImageResource(R.drawable.head_no);
		}
//		int resid = DataUser.getUserHeadResId(info.avatarUrl);
//		if (!(resid == 0))holder.image_head.setImageResource(resid);
		if(currentPosition == position){
			holder.image_check.setImageResource(R.drawable.select_true);
		}else{
			holder.image_check.setImageResource(R.drawable.select_false);
		}
		return convertView;
	}
	// ===================================================
	public List<DataUser> getDataList() {
		return list;
	}






	public final class ViewHolder {
		public TextView	text_name, text_phone;
		public ImageView	image_head, image_check;
	}


}
