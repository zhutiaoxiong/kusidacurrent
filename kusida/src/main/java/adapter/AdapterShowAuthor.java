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

import model.common.DataAuthorization;

public class AdapterShowAuthor extends BaseAdapter
{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<DataAuthorization> dataList;
	private int currentPosition;
	protected final int mItemLayoutId;
	public AdapterShowAuthor(Context context, List<DataAuthorization> dataList, int itemLayoutId)
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

	@Override
	public Object getItem(int position)
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
            convertView = mInflater.inflate(R.layout.list_item_name_check_pair, null);
            holder.textView = (TextView)convertView.findViewById(R.id.textView);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();//��ʼ��holder
        }
        //ÿ����ʾˢ��
        if(currentPosition == position){
//        	dataList.get(position).isChecked = true;
    		holder.imageView.setImageResource(R.drawable.select_true);
        }else{
//        	dataList.get(position).isChecked = false;
    		holder.imageView.setImageResource(R.drawable.select_false);
        }
//		holder.textView.setText(dataList.get(position).carName);
		holder.textView.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				currentPosition = position;
				notifyDataSetChanged();
			}
		});
        return convertView;
	}
	//===================================================
	public List<DataAuthorization> getDataList(){
		return dataList;
	}
	public final class ViewHolder{
		public TextView textView;
		public ImageView imageView;
	}
	//===================================================

}
