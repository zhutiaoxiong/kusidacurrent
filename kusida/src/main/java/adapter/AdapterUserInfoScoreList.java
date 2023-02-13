package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;

import java.util.List;

import model.score.DataScore;


public class AdapterUserInfoScoreList extends BaseAdapter{
	private LayoutInflater  mInflater;
	private Context         mContext;
	private List<DataScore> list;
	private int             currentPosition;
	public AdapterUserInfoScoreList(Context context, List<DataScore> list) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = list;
	}
	public DataScore getCurrentItem() {
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
	public DataScore getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.view_me_userinfo_score_item, null);
			holder.img_icon = (SmartImageView) convertView.findViewById(R.id.img_icon);
			holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
			holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.txt_score = (TextView) convertView.findViewById(R.id.txt_score);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		// 设初始显示数据
		DataScore info = list.get(position);
		if(info.pic==null || info.pic.length() == 0){
			holder.img_icon.setImageDrawable(null);//设为无图
		}else{
			holder.img_icon.setImageUrl(info.pic);//设为有图
		}
		if(info.title!=null)holder.txt_name.setText(info.title);
		holder.txt_score.setText("+"+info.score);
		if(info.isDone == 1){
			holder.img_check.setVisibility(View.VISIBLE);
		}else{
			holder.img_check.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	// ===================================================
	public List<DataScore> getDataList() {
		return list;
	}
	public final class ViewHolder {
		public SmartImageView img_icon;
		public ImageView      img_check;
		private TextView txt_name,txt_score;
	}


}
