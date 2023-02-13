package view.safetyneed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;

import model.answer.DataFindway;

/**
 * Created by qq522414074 on 2016/10/27.
 */
public class AdapterChooseWay extends BaseAdapter {
    private List<DataFindway> list;
    private Context context;

    public AdapterChooseWay(List<DataFindway> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public DataFindway getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_safety_chooseway_item, null);
            holder = new Holder();
            holder.tv = (TextView) convertView.findViewById(R.id.findway);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (list != null) {
            holder.tv.setText(list.get(position).name);
        }
        return convertView;
    }

    class Holder {
        TextView tv;
    }
}
