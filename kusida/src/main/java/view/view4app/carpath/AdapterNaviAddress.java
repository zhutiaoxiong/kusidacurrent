package view.view4app.carpath;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.map.DataPos;

/**
 * Created by qq522414074 on 2017/3/30.
 */

public class AdapterNaviAddress extends BaseAdapter {
    private Context       context;
    private List<DataPos> list;
    private OnClickListener onClickListener;

    public AdapterNaviAddress(Context context, List<DataPos> allPoi, OnClickListener listener) {
        this.context = context;
        this.list = allPoi;
        this.onClickListener = listener;
    }

    @Override
    public int getCount() {
        if(list == null)return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.view_navi_theaddress_listitem,null);
            holder.txt_address_name= (TextView) convertView.findViewById(R.id.txt_address_name);
            holder.txt_address_name_long= (TextView) convertView.findViewById(R.id.txt_address_name_long);
            holder.layout= (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        DataPos data = list.get(position);
        SpannableString s = new SpannableString(data.addressName);
        Pattern p = Pattern.compile(ViewNaviSearch.searchStr);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#0179ff")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.txt_address_name.setText(s);
        holder.txt_address_name_long.setText(data.address);
        holder.layout.setTag(data);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPos data = (DataPos)v.getTag();
                if(onClickListener!=null)onClickListener.onClickNavi(data,position);
            }
        });
        return convertView;
    }
    public class ViewHolder{
        TextView txt_address_name;
        TextView txt_address_name_long;
        LinearLayout layout;
    }
    public interface OnClickListener {
        void onClickNavi(DataPos data,int position);
    }
    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }
}
