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
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.toast.ToastConfirmNormal;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.GlobalContext;
import common.map.MapPosGet;

/**
 * Created by qq522414074 on 2017/3/30.
 */

public class AdapterNearSearch extends BaseAdapter {
    private Context         context;
    private List<PoiInfo>   list;
    private OnClickListener onClickListener;
    private String searchStr;

    public AdapterNearSearch(Context context,String searchStr, List<PoiInfo> allPoi, OnClickListener listener) {
        this.context = context;
        this.list = allPoi;
        this.searchStr = searchStr;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.map_search_near_item,null);
            holder.txt_address_name= (TextView) convertView.findViewById(R.id.txt_address_name);
            holder.txt_address= (TextView) convertView.findViewById(R.id.txt_address);
            holder.img_navi= (ImageView) convertView.findViewById(R.id.img_navi);
            holder.img_call = (ImageView) convertView.findViewById(R.id.img_call);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        PoiInfo info = list.get(position);
        SpannableString s = new SpannableString(position+" "+info.name);
        Pattern p = Pattern.compile(searchStr);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#0179ff")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.txt_address_name.setText(s);
        String dis = MapPosGet.getLatLngDistance(MapPosGet.getPrePos().pos,info.location);
        holder.txt_address.setText(dis+ "  "+info.address);
        holder.img_call.setVisibility((info.phoneNum!=null && info.phoneNum.length()>0) ? View.VISIBLE : View.INVISIBLE);

        holder.txt_address_name.setTag(info);
        holder.txt_address.setTag(info);
        holder.img_navi.setTag(info);
        holder.img_call.setTag(info);
        holder.txt_address_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoiInfo data = (PoiInfo)v.getTag();
                if(onClickListener!=null)onClickListener.onClickText(data);
            }
        });
        holder.txt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoiInfo data = (PoiInfo)v.getTag();
                if(onClickListener!=null)onClickListener.onClickText(data);
            }
        });
        holder.img_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoiInfo data = (PoiInfo)v.getTag();
                if(onClickListener!=null)onClickListener.onClickNavi(data);
            }
        });
        holder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PoiInfo data = (PoiInfo)v.getTag();
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle(data.phoneNum)
                        .withButton("取消","呼叫")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if(isClickConfirm)
                                    ODispatcher.dispatchEvent(OEventName.CALL_MY_PHONE,data.phoneNum);
                            }
                        })
                        .show();
            }
        });
        return convertView;
    }
    public class ViewHolder{
        TextView  txt_address_name;
        TextView  txt_address;
        ImageView img_navi,img_call;
    }
    public interface OnClickListener {
        void onClickText(PoiInfo info);
        void onClickNavi(PoiInfo info);
    }
    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }
}
