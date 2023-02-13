package adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;

public class AdapterShowString extends BaseAdapter
{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<String> dataList;
	private int currentPosition;
	protected final int mItemLayoutId;
	public AdapterShowString(Context context, List<String> dataList, int itemLayoutId)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.dataList = dataList;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount()
	{
		return dataList.size();
	}
	public String getCurrentItem(){
		return dataList.get(currentPosition);
	}
	public List<String> getDataList(){
		return dataList;
	}

	@Override
	public String getItem(int position)
	{
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
        ViewHolder holder = null;
        if (convertView == null) {//��ʼ��
            holder=new ViewHolder(); 
            convertView = mInflater.inflate(R.layout.list_item_onlytext, null);
            holder.text_view = (TextView)convertView.findViewById(R.id.text_view);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();//��ʼ��holder
        }
        holder.text_view.setText(dataList.get(position));
        return convertView;
	}
	//===================================================
	public final class ViewHolder{
		public TextView text_view;
	}
	//===================================================

}
