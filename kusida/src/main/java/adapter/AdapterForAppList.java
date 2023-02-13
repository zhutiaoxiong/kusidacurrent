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

import model.AppList.AppListData;

/**
 * Created by qq522414074 on 2016/12/13.
 */

public class AdapterForAppList extends BaseAdapter {
    private List<AppListData> list;
    private Context context;
    public AdapterForAppList(List<AppListData>list,Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    public void setData(List<AppListData> list){
        this.list=list;
//        notifyDataSetChanged();

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
             holder=new Holder();
           convertView= LayoutInflater.from(context).inflate(R.layout.grid_item_app,null);
            holder.iv=(ImageView) convertView.findViewById(R.id.imageView);
            holder.tv=(TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        holder.iv.setImageResource(list.get(position).pic);
        holder.tv.setText(list.get(position).name);
        return convertView;
    }
    class Holder{
        TextView tv;
        ImageView iv;
    }
}
