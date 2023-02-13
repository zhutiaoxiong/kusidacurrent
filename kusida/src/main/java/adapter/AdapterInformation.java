package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;

import java.util.List;

import model.information.DataInformation;

public class AdapterInformation extends BaseAdapter
{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<DataInformation> list;
	protected final int mItemLayoutId;
	public AdapterInformation(Context context, List<DataInformation> list, int itemLayoutId)
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
	public DataInformation getItem(int position)
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
            convertView = mInflater.inflate(R.layout.list_item_information, null);
            holder.txt_title = (TextView)convertView.findViewById(R.id.txt_title);
            holder.txt_info = (TextView)convertView.findViewById(R.id.txt_info);
            holder.img_left = (SmartImageView)convertView.findViewById(R.id.img_left);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();//��ʼ��holder
        }
        //设初始显示数据
        DataInformation info = list.get(position);
        holder.txt_title.setText(info.title);
        holder.txt_info.setText(info.comment);
		holder.img_left.setImageUrl(info.pic);
//        ImageHttpLoader.getInstance().asyncloadImage(holder.img_left, info.pic,  MD5.getImageName(info.pic));
        return convertView;
	}
	//===================================================
	public final class ViewHolder{
		public TextView txt_title,txt_info;
		public SmartImageView img_left;
	}
	//===================================================

}
