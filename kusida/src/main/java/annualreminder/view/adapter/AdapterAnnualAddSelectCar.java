package annualreminder.view.adapter;


    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.client.proj.kusida.R;
    import annualreminder.view.clip.PopChooseCarWarp;

    import java.util.List;

/**
     * Created by qq522414074 on 2016/11/3.
     */
    public class AdapterAnnualAddSelectCar extends BaseAdapter {
        private Context                              context;
        private List<PopChooseCarWarp.DataCarChoose> carInfoList;

        public AdapterAnnualAddSelectCar(Context context, List<PopChooseCarWarp.DataCarChoose> carInfoList) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.list_annualadd_select_car_item, null);
                holder.img_arrow = (ImageView) convertView.findViewById(R.id.img_arrow);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            //设下拉箭头显示
            if (carInfoList != null){
                holder.txt_name.setText(carInfoList.get(position).carName);
                if(position!=0){
                    holder.img_arrow.setVisibility(View.INVISIBLE);
                }else{
                    holder.img_arrow.setVisibility(View.VISIBLE);
                    if(carInfoList.size()>1){
                        holder.img_arrow.setImageResource(R.drawable.annual_arrow_up);
                    }else{
                        holder.img_arrow.setImageResource(R.drawable.annual_arrow_down);
                    }
                }
            }
            return convertView;
        }

        private class Holder {
            TextView  txt_name;
            ImageView img_arrow;
        }
    }
