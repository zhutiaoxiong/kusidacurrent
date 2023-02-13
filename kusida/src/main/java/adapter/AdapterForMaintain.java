package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.ManagerCarList;
import model.maintain.DataMaintain;
import model.maintain.ManagerMaintainList;

/**
 * Created by qq522414074 on 2016/10/31.
 */
public class AdapterForMaintain extends BaseAdapter {
    private Context context;
    private List<DataMaintain> list;
    private int currentPosition;

    public AdapterForMaintain(Context context, List<DataMaintain> list) {
        this.context = context;
        this.list = list;
    }

    public void changeData(List<DataMaintain> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setCurrentItem(int position) {
        currentPosition = position;
        // notifyDataSetChanged();
    }

    public DataMaintain getCurrentItem() {
        return list.get(currentPosition);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_maintance_item, null);
            holder.maintain_status = (TextView) convertView.findViewById(R.id.maintain_status);
            holder.license_plate = (TextView) convertView.findViewById(R.id.license_plate);
            holder.maintain_time = (TextView) convertView.findViewById(R.id.maintain_time);
            holder.maintain_time_specific = (TextView) convertView.findViewById(R.id.maintain_time_specific);
            holder.maintain_mileage_specific = (TextView) convertView.findViewById(R.id.maintain_mileage_specific);
            holder.car_percent = (TextView) convertView.findViewById(R.id.car_percent);
            holder.colour_line = (ImageView) convertView.findViewById(R.id.colour_line);
            holder.car = (ImageView) convertView.findViewById(R.id.car);
            holder.bg=convertView.findViewById(R.id.bg);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
            if(position==0){
               holder. bg.setBackground(context.getResources().getDrawable(R.drawable.maintain_bg_first));
            }else{
                holder. bg.setBackground(context.getResources().getDrawable(R.drawable.view_maintain_item_background));
            }

        if (list == null || list.size() == 0) return null;
        DataMaintain dataMaintain = list.get(position);
        if (dataMaintain == null) return null;
        String status = null;
        switch (dataMaintain.status) {
            case 0:
                status = context.getResources().getString(R.string.is_the_maintenance);
                holder.maintain_status.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case 1:
                status = context.getResources().getString(R.string.end_of_the_normal_maintenance);
                holder.maintain_status.setTextColor(context.getResources().getColor(R.color.green_text));
                break;
            case 2:
                status =context.getResources().getString(R.string.ahead_of_maintenance_in_advance_end);
                holder.maintain_status.setTextColor(context.getResources().getColor(R.color.yellow_smart_text));
                break;
        }

        holder.maintain_status.setText(status);
        holder.maintain_time.setText(context.getResources().getString(R.string.time) + dataMaintain.time + context.getResources().getString(R.string.months));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(dataMaintain.startTime);
        holder.maintain_time_specific.setText(formatter.format(date));
        holder.maintain_mileage_specific.setText(dataMaintain.miles + "km");
        int miles = dataMaintain.miles;
        int nowMiles = dataMaintain.nowMiles;
        String txt = ((int) (nowMiles * 100) / miles) + "%";
        holder.car_percent.setText(txt);
        holder.license_plate.setText(ManagerCarList.getInstance().getCurrentCar().num);
        //克隆view的width、height、margin的值生成margin对象
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(holder.car.getLayoutParams());
        int width = ManagerMaintainList.getInstance().width - ODipToPx.dipToPx(context, 20) - ODipToPx.dipToPx(context, 38);
        double percent = (double) nowMiles / (double) miles;
        int aaa = (int) (width * percent) + ODipToPx.dipToPx(context, 10);
        int bbb = (int) (width * (1 - percent)) + ODipToPx.dipToPx(context, 10);
//        margin.setMargins(1050,300,60,30);
        margin.setMargins(aaa, ODipToPx.dipToPx(context, 100), bbb, ODipToPx.dipToPx(context, 20));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
//设制view的新的位置
        holder.car.setLayoutParams(layoutParams);
        ViewGroup.MarginLayoutParams margin1 = new ViewGroup.MarginLayoutParams(holder.car_percent.getLayoutParams());
//        margin.setMargins(1050,300,60,30);
        margin1.setMargins(aaa, ODipToPx.dipToPx(context, 80), bbb, ODipToPx.dipToPx(context, 40));
        RelativeLayout.LayoutParams layoutParams1= new RelativeLayout.LayoutParams(margin1);
//设制view的新的位置
        holder.car_percent.setLayoutParams(layoutParams1);
        holder.car.setLayoutParams(layoutParams);
        return convertView;
    }

    class Holder {
        TextView maintain_status, license_plate, maintain_time, maintain_time_specific, maintain_mileage_specific, car_percent;
        ImageView colour_line, car;
        View bg;
    }
}
