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

import model.score.DataScore;


public class AdapterUserInfoScoreDetailList extends BaseAdapter{
	private LayoutInflater  mInflater;
	private Context         mContext;
	private List<DataScore> list;
	private int             currentPosition;
	public AdapterUserInfoScoreDetailList(Context context, List<DataScore> list) {
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
			convertView = mInflater.inflate(R.layout.view_me_userinfo_score_detail_item, null);
			holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.txt_score = (TextView) convertView.findViewById(R.id.txt_score);
			holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		// 设初始显示数据
		DataScore info = list.get(position);
		if(info==null)return convertView;
		if(info.createTime==0){
			holder.txt_time.setText("");
		}else{
			holder.txt_time.setText(ODateTime.time2StringWithHH(info.createTime));
		}
		if(info.score>=0){
			holder.txt_score.setTextColor(convertView.getResources().getColor(R.color.green_light));
			holder.txt_score.setText("+"+info.score);
		}else{
			holder.txt_score.setTextColor(convertView.getResources().getColor(R.color.red_dark));
			holder.txt_score.setText(""+info.score);
		}
		//加载跳转
		return convertView;
	}
	// ===================================================
	public List<DataScore> getDataList() {
		return list;
	}
	public final class ViewHolder {
		private TextView txt_name,txt_score,txt_time;
	}


}
