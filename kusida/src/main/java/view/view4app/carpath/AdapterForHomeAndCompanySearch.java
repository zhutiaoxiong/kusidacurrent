package view.view4app.carpath;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;

import common.map.DataPos;

/**
 * Created by qq522414074 on 2017/4/1.
 */

class AdapterForHomeAndCompanySearch extends BaseAdapter {
    private Context context;
    private List<DataPos> list;
    private OnClickItemListener onClickListener;
    private  int deleteBtnWidth;
    public AdapterForHomeAndCompanySearch(Context context, int deleteBtnWidth,List<DataPos> allPoi,OnClickItemListener listener) {
        this.context = context;
        this.list = allPoi;
        this.onClickListener = listener;
        this.deleteBtnWidth = deleteBtnWidth;
    }
    public void changeUI( List<DataPos> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
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
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.view_navi_address_listitem,null);
            holder.txt_address_name= (TextView) convertView.findViewById(R.id.txt_address_name);
            holder.navigaton= (ImageView) convertView.findViewById(R.id.navigaton);
            holder.layout= (LinearLayout) convertView.findViewById(R.id.layout);
            holder.item_left = (View)convertView.findViewById(R.id.item_left);
            holder.item_right = (View)convertView.findViewById(R.id.item_right);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        DataPos data = list.get(position);
        holder.txt_address_name.setText(data.addressName);
        holder.navigaton.setVisibility(View.INVISIBLE);
        holder.layout.setTag(data);
        holder.txt_address_name.setTag(data);
        holder.item_right.setTag(data);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(deleteBtnWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        holder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPos data = (DataPos)v.getTag();
                if (onClickListener != null) {
                    if(data==null)return;
                    onClickListener.onClicDelete(data);
                }
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPos data = (DataPos)v.getTag();
                if(onClickListener!=null)onClickListener.onClickNavi(data);
            }
        });
//        holder.txt_address_name.setOnClickListener(new OnClickListenerMy(){
//            @Override
//            public void onClickNoFast(View v) {
//                DataPos data = (DataPos)v.getTag();
//                ViewMapPoi.usePos=data;
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_poi);
//            }
//        });
        return convertView;
    }
    public class ViewHolder{
        TextView txt_address_name;
        ImageView navigaton;
        LinearLayout layout;
        View item_left;
        View item_right;
    }
    public interface OnClickItemListener {
        void onClicDelete(DataPos data);
        void onClickNavi(DataPos data);
    }
    public void setOnClickListener(OnClickItemListener listener) {
        this.onClickListener = listener;
    }
}
