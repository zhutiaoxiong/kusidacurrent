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

import model.common.DataAuthorization;
@Deprecated
public class AdapterPickAuthor extends BaseAdapter
{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<DataAuthorization> list;
	protected final int mItemLayoutId;
	public AdapterPickAuthor(Context context, List<DataAuthorization> list, int itemLayoutId)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = list;
		this.mItemLayoutId = itemLayoutId;
	}

	public List<DataAuthorization> getDataList(){
		return list;
	}
	@Override
	public int getCount()
	{
		return list.size();
	}
	public void setSelectItem(int position) {
		list.get(position).isSelected = !list.get(position).isSelected;
		notifyDataSetChanged();
	}
	public int getIsSelectedCount(){
		int count = 0;
		for(DataAuthorization data : list){
			if(data.isSelected)count++;
		}
		return count;
	}

	@Override
	public DataAuthorization getItem(int position)
	{
		return list.get(position);
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
            convertView = mInflater.inflate(R.layout.list_item_name_check_pair, null);
            holder.textView = (TextView)convertView.findViewById(R.id.textView);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
            holder.textView.setPadding(100, 0, 0, 0);
        }else {
            holder = (ViewHolder)convertView.getTag();//��ʼ��holder
        }
		holder.textView.setText(list.get(position).name);
        if(list.get(position).isSelected){
    		holder.imageView.setImageResource(R.drawable.select_true);
        }else{
    		holder.imageView.setImageResource(R.drawable.select_false);
        }
        return convertView;
	}
	//===================================================
	public final class ViewHolder{
		public TextView textView;
		public ImageView imageView;
	}
	//===================================================

}
