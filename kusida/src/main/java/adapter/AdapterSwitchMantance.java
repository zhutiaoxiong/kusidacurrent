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

import model.carlist.DataCarInfo;

/**
 * Created by qq522414074 on 2017/1/12.
 */

public class AdapterSwitchMantance extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DataCarInfo> list;
    protected final int mItemLayoutId;
    public AdapterSwitchMantance(Context context, List<DataCarInfo> list, int itemLayoutId)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.list = list;
        this.mItemLayoutId = itemLayoutId;
    }
    public void changeUI(List<DataCarInfo> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount()
    {
        if(list == null)return 0;
        return list.size();
    }

    @Override
    public DataCarInfo getItem(int position)
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
        AdapterSwitchMantance.ViewHolder holder = null;
        if (convertView == null) {//��ʼ��
            holder=new AdapterSwitchMantance.ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_name_switch_mantance, null);
            holder.textView = (TextView)convertView.findViewById(R.id.textView);
            holder.imageBtn = (ImageView)convertView.findViewById(R.id.imageBtn);
            holder.img_cartype=(SmartImageView)convertView.findViewById(R.id.img_cartype) ;
            convertView.setTag(holder);
        }else {
            holder = (AdapterSwitchMantance.ViewHolder)convertView.getTag();//��ʼ��holder
        }
        DataCarInfo info = list.get(position);
        //设初始显示数据
        holder.textView.setText(info.num);
        if(info.logo!=null){
        holder.img_cartype.setImageUrl(info.logo);}
        if(info.washMould==1){
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
        public SmartImageView img_cartype;
    }
}
