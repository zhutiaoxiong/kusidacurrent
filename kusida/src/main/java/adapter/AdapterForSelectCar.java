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

import java.util.ArrayList;
import java.util.List;

import model.carlist.DataCarInfo;

/**
 * Created by qq522414074 on 2016/9/1.
 */
public class AdapterForSelectCar extends BaseAdapter {
    private List<DataCarInfo> list = new ArrayList<DataCarInfo>();
    private Context context;
    private int skinId;
    public AdapterForSelectCar(List<DataCarInfo> list,int skinId, Context context) {
        this.list = list;
        this.skinId = skinId;
        this.context = context;
    }

    public void changeData(List<DataCarInfo> listCar){
        this.list = listCar;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public DataCarInfo getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_find_select_car_item, null);
            holder = new ViewHolder();
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.img_icon = (SmartImageView) convertView.findViewById(R.id.img_icon);
            holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
            convertView.setTag(holder);
            holder.img_check.setTag(holder);
            holder.txt_name.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DataCarInfo info = list.get(position);
        holder.info = info;
        holder.txt_name.setText(info.num);
        if (info.logo != null) {
            holder.img_icon.setImageUrl(info.logo);
        }
//        if (info.carTypeId == skinId) {
//            holder.img_check.setImageResource(R.drawable.select_true);
//            info.skinSelect = true;
//        } else {
//            holder.img_check.setImageResource(R.drawable.select_false);
//            info.skinSelect = false;
//        }
        holder.img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                if (holder.info.skinSelect) {
                    holder.img_check.setImageResource(R.drawable.select_false);
                    holder.info.skinSelect = false;
                } else {
                    holder.img_check.setImageResource(R.drawable.select_true);
                    holder.info.skinSelect = true;
                }
            }
        });
        holder.txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                if (holder.info.skinSelect) {
                    holder.img_check.setImageResource(R.drawable.select_false);
                    holder.info.skinSelect = false;
                } else {
                    holder.img_check.setImageResource(R.drawable.select_true);
                    holder.info.skinSelect = true;
                }
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public DataCarInfo info;
        public SmartImageView img_icon;
        public ImageView img_check;
        public TextView txt_name;
    }
}
