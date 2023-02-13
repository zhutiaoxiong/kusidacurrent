package annualreminder.view.clip;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

        import com.client.proj.kusida.R;
        import com.kulala.staticsview.image.smart.SmartImageView;

        import java.util.List;

/**
 * Created by qq522414074 on 2016/11/3.
 */
public class PopChooseCarWarpAdapter extends BaseAdapter {
    private Context                              context;
    private List<PopChooseCarWarp.DataCarChoose> carInfoList;

    public PopChooseCarWarpAdapter(Context context, List<PopChooseCarWarp.DataCarChoose> carInfoList) {
        this.context = context;
        this.carInfoList = carInfoList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_annual_reminder_select_car_item, null);
            holder.img_logo = (SmartImageView) convertView.findViewById(R.id.img_logo);
            holder.car_brand = (TextView) convertView.findViewById(R.id.car_brand);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (carInfoList != null && carInfoList.size() != 0 && carInfoList.get(position) != null) {
            holder.img_logo.setImageUrl(carInfoList.get(position).carLogo);
            holder.car_brand.setText(carInfoList.get(position).carName);
        }
        return convertView;
    }

    private class Holder {
        TextView       car_brand;
        SmartImageView img_logo;
    }
}
