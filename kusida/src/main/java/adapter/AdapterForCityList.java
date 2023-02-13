package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import java.util.List;


/**
 * Created by qq522414074 on 2017/3/20.
 */

public class AdapterForCityList extends BaseAdapter {
    private Context context;
    private List<String> list;
    private String str;

    public AdapterForCityList(Context context, List<String> list, String str) {
        this.context = context;
        this.list = list;
        this.str = str;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.city_list_item, null);
            holder.city = (TextView) convertView.findViewById(R.id.city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).equals("其他")) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ODipToPx.dipToPx(context, 90), ODipToPx.dipToPx(context, 51));
            convertView.setLayoutParams(params);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ODipToPx.dipToPx(context, 90), ODipToPx.dipToPx(context, 51));
            holder.city.setLayoutParams(params1);
        }
        if (str.equals(list.get(position))) {
            holder.city.setBackgroundResource(R.drawable.button_selector_round_blue);
            holder.city.setTextColor(context.getResources().getColor(R.color.white));
        }
        holder.city.setText(list.get(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView city;
    }
}
