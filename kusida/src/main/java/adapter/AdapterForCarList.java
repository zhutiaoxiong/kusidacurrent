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

import model.carlist.DataCarInfo;

/**
 * Created by qq522414074 on 2016/11/3.
 */
public class AdapterForCarList extends BaseAdapter {
    private Context context;
    private List<DataCarInfo> carInfoList;
    private int					currentPosition;

    public AdapterForCarList(Context context, List<DataCarInfo> carInfoList) {
        this.context = context;
        this.carInfoList = carInfoList;
    }
    public void setCurrentItem(int position) {
        currentPosition = position;
        // notifyDataSetChanged();
    }
    public DataCarInfo getCurrentItem() {
        return carInfoList.get(currentPosition);
    }
    @Override
    public int getCount() {
        return carInfoList == null ? 0 : carInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return carInfoList == null ? null : carInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_maintain_car_num_item, null);
            holder.img_dropdown = (SmartImageView) convertView.findViewById(R.id.img_dropdown);
            holder.car_brand = (TextView) convertView.findViewById(R.id.car_brand);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (carInfoList != null && carInfoList.size() != 0 && carInfoList.get(position) != null) {
            holder.img_dropdown.setImageUrl(carInfoList.get(position).logo);
            holder.car_brand.setText(carInfoList.get(position).num);
        }
        return convertView;
    }

    class Holder {
        TextView car_brand;
        SmartImageView img_dropdown;
    }
}
