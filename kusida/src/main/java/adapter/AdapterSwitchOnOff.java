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

import model.status.DataSwitch;

public class AdapterSwitchOnOff extends BaseAdapter
{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<DataSwitch> list;
	protected final int mItemLayoutId;
	public AdapterSwitchOnOff(Context context, List<DataSwitch> list, int itemLayoutId)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = list;
		this.mItemLayoutId = itemLayoutId;
	}
	@Override
	public int getCount()
	{
		if(list == null)return 0;
		return list.size();
	}

	@Override
	public DataSwitch getItem(int position)
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
            convertView = mInflater.inflate(R.layout.list_item_name_switch_pair, null);
            holder.textView = (TextView)convertView.findViewById(R.id.textView);
            holder.imageBtn = (ImageView)convertView.findViewById(R.id.imageBtn);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();//��ʼ��holder
        }
        DataSwitch info = list.get(position);
        //设初始显示数据
        holder.textView.setText(info.name);
        if(info.isOpen == 1){
        	holder.imageBtn.setImageResource(R.drawable.car_set_on);
        }else{
        	holder.imageBtn.setImageResource(R.drawable.car_set_off);
        }
        return convertView;
	}
	//===================================================
	public final class ViewHolder{
		public TextView textView;
		public ImageView imageBtn;
	}
	//===================================================

}
