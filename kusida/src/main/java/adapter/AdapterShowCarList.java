package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;

import java.util.List;

import model.ManagerCarList;
import model.carlist.DataCarInfo;

public class AdapterShowCarList extends BaseAdapter {
	private LayoutInflater		mInflater;
	private Context				mContext;
	private List<DataCarInfo>	listCar;
	private int					currentPosition;
	protected final int			mItemLayoutId;
	private boolean				isOnlyForChoose	= false;
	public AdapterShowCarList(Context context, List<DataCarInfo> listCar, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.listCar = listCar;
		this.mItemLayoutId = itemLayoutId;
	}

	public void changeData(List<DataCarInfo> listCar){
		this.listCar = listCar;
	}
	public void setCurrentItem(DataCarInfo data) {
		if (data == null)
			return;
		for (int i = 0; i < listCar.size(); i++) {
			DataCarInfo info = listCar.get(i);
			if (info.ide == data.ide) {
				currentPosition = i;
				// notifyDataSetChanged();
				return;
			}
		}
	}
	public void setCurrentItem(int position) {
		currentPosition = position;
		// notifyDataSetChanged();
	}
	public DataCarInfo getCurrentItem() {
		return listCar.get(currentPosition);
	}
	public void setOnlyForChoose() {
		isOnlyForChoose = true;
	}
	@Override
	public int getCount() {
		if (listCar == null)
			return 0;
		return listCar.size();
	}

	@Override
	public Object getItem(int position) {
		return listCar.get(position);
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
			convertView = mInflater.inflate(R.layout.list_item_carname, null);
			holder.textView = (TextView) convertView.findViewById(R.id.textView);
			holder.image_check = (ImageView) convertView.findViewById(R.id.image_check);
			holder.image_icon = (SmartImageView) convertView.findViewById(R.id.image_icon);
			holder.txt_active = (TextView) convertView.findViewById(R.id.txt_active);
			holder.img_bluetooth = (ImageView) convertView.findViewById(R.id.img_bluetooth);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		DataCarInfo info = listCar.get(position);
		// 设初始显示数据
		holder.textView.setText(info.num);
		holder.txt_active.getBackground().setAlpha(128);
		if (info.isActive == 1) {
			if(info.isAuthority==1){
				holder.txt_active.setText("已授权");
			}else if(info.isMyCar == 0){
				holder.txt_active.setText("副车主");
			}else{
				holder.txt_active.setText("已激活");
			}
			holder.txt_active.setTextColor(Color.parseColor("#e93232"));
		} else {
			holder.txt_active.setText("未激活");
			holder.txt_active.setTextColor(Color.parseColor("#ffffff"));
		}
		//蓝牙
		if (info.isBindBluetooth == 1) {
			holder.img_bluetooth.setVisibility(View.VISIBLE);
		} else {
			holder.img_bluetooth.setVisibility(View.INVISIBLE);
		}
//		DataBrands brand = ManagerCommon.getInstance().getBrands(info.brand);
		if(info.logo!=null){
			holder.image_icon.setImageUrl(info.logo);
//			LoadImageTT.getInstance().asyncloadImage(holder.image_icon, info.logo, MD5.getImageName(info.logo));
		}
//		if(info.logo!=null)ImageHttpLoader.getInstance().asyncloadImage(holder.image_icon, info.logo, MD5.getImageName(info.logo));

		if (listCar.get(position).ide == ManagerCarList.getInstance().getCurrentCarID()) {
			holder.image_check.setImageResource(R.drawable.select_true);
		} else {
			// if (holder.image_check.getVisibility() == View.VISIBLE)
			holder.image_check.setImageResource(R.drawable.select_false);
		}
		return convertView;
	}
	// ===================================================
	public List<DataCarInfo> getDataList() {
		return listCar;
	}
	public final class ViewHolder {
		public TextView	textView,txt_active;
		public ImageView	image_check,img_bluetooth;
		public SmartImageView image_icon;
	}
	// ===================================================

}
