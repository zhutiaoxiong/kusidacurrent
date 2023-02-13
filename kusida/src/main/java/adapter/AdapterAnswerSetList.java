package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;

import model.answer.DataAnswer;

public class AdapterAnswerSetList extends BaseAdapter {
	private LayoutInflater		mInflater;
	private Context				mContext;
	private List<DataAnswer> list;
	protected final int			mItemLayoutId;
	public AdapterAnswerSetList(Context context, List<DataAnswer> list, int itemLayoutId) {
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
			convertView = mInflater.inflate(R.layout.view_answer_set_item, null);
			holder.txt_question = (TextView) convertView.findViewById(R.id.txt_question);
//			holder.txt_answer1 = (TextView) convertView.findViewById(R.id.txt_answer1);
//			holder.txt_answer2 = (TextView) convertView.findViewById(R.id.txt_answer2);
//			holder.lin_answer2 = (RelativeLayout) convertView.findViewById(R.id.lin_answer2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		DataAnswer info = list.get(position);
		// 设初始显示数据
//		holder.txt_question.setText(info.title);
//		if (info.type== 1) {//二个框
//			holder.lin_answer2.setVisibility(View.VISIBLE);
//			holder.txt_answer1.setText("广东");
//			holder.txt_answer2.setText("中山");
//		} else {//一个框
//			holder.lin_answer2.setVisibility(View.INVISIBLE);
//			holder.txt_answer1.setText("菜单");
//		}
		return convertView;
	}
	// ===================================================
	public final class ViewHolder {
		public TextView	txt_question,txt_answer1,txt_answer2;
		public RelativeLayout lin_answer2;
	}
	// ===================================================

}
