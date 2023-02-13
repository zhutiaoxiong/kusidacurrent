package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;

/**
 * Created by qq522414074 on 2017/1/12.
 */

public class AdapterMeHelp extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> list;
    public AdapterMeHelp(Context context, List<String> list)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.list = list;
    }
    public void changeUI(List<String> list){
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
    public String getItem(int position)
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
            convertView = mInflater.inflate(R.layout.list_item_me_help, null);
            holder.textView = (TextView)convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();//��ʼ��holder
        }
        String info = list.get(position);
        //设初始显示数据
        holder.textView.setText(info);
        return convertView;
    }
    //===================================================
    public final class ViewHolder{
        public TextView textView;
    }
}
