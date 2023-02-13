package view.view4me.shake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;

import java.util.List;

import model.carlist.DataCarInfo;

/**
 * Created by qq522414074 on 2017/1/12.
 */

public class ViewSwitchShakeAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DataCarInfo> list;
    private DataCarInfo preCar;
    public ViewSwitchShakeAdapter(Context context,DataCarInfo preCar, List<DataCarInfo> list)
    {
        this.preCar = preCar;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.list = list;
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
        ViewHolder holder;
        if (convertView == null) {//��ʼ��
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.view_me_switch_shake_item, null);
            holder.txt_name = (TextView)convertView.findViewById(R.id.txt_name);
            holder.img_logo = (SmartImageView)convertView.findViewById(R.id.img_logo);
            holder.img_right=(SmartImageView)convertView.findViewById(R.id.img_right) ;
            convertView.setTag(holder);
        }else {
            holder = (ViewSwitchShakeAdapter.ViewHolder)convertView.getTag();//��ʼ��holder
        }
        DataCarInfo info = list.get(position);
        //设初始显示数据
        holder.txt_name.setText(info.num);
        if(info.logo!=null)holder.img_logo.setImageUrl(info.logo);
        if(info.ide == preCar.ide){
            holder.img_right.setVisibility(View.VISIBLE);
        }else{
            holder.img_right.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
    //===================================================
    private class ViewHolder{
        public TextView txt_name;
        public SmartImageView img_logo,img_right;
    }
}
