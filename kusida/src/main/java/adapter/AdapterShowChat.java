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

import model.chat.DataChat;
import view.clip.child.ClipChatText;

public class AdapterShowChat extends BaseAdapter{
	private LayoutInflater		mInflater;
	private Context				mContext;
	private List<DataChat>	list;
	private int					currentPosition;
	protected final int			mItemLayoutId;
	// private boolean checkNeed = false;
	// private int imageId = 0;
	public AdapterShowChat(Context context, List<DataChat> list, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = list;
		this.mItemLayoutId = itemLayoutId;
	}
	public DataChat getCurrentItem() {
		return list.get(currentPosition);
	}
	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
	}

	@Override
	public DataChat getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.list_item_chat, null);
			holder.txt_left = (TextView) convertView.findViewById(R.id.txt_left);
			holder.txt_right = (TextView) convertView.findViewById(R.id.txt_right);
			holder.txt_info = (ClipChatText) convertView.findViewById(R.id.txt_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		// 设初始显示数据
		DataChat info = list.get(position);
		if(info.fromId == 1){
			holder.txt_right.setVisibility(View.INVISIBLE);
			holder.txt_info.pointLeft(true);
			holder.txt_info.setPadding(30, 5, 5, 5);
		}else{
			holder.txt_left.setVisibility(View.INVISIBLE);
			holder.txt_info.pointLeft(false);
			holder.txt_info.setPadding(5, 5, 30, 5);
		}
		holder.txt_info.setText(ToDBC(ODateTime.time2StringHHmm(info.time)+"  "+info.content));
//		holder.txt_info_face.setText(ToDBC(ODateTime.time2StringHHmm(info.time)+"  "+info.content));
		return convertView;
	}
	public static String ToDBC(String input) {  
        char[] c = input.toCharArray();  
        for (int i = 0; i < c.length; i++) {  
            if (c[i] == 12288) {  
                c[i] = (char) 32;  
                continue;  
            }  
            if (c[i] > 65280 && c[i] < 65375)  
                c[i] = (char) (c[i] - 65248);  
        }  
        return new String(c);  
    }
	// ===================================================
	public List<DataChat> getDataList() {
		return list;
	}
	public final class ViewHolder {
		public TextView		txt_left, txt_right;
		public ClipChatText	txt_info;
	}
	// ===================================================

}
