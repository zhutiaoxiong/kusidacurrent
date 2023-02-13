package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import model.ManagerGps;
import model.gps.SearchHistory;

/**
 * Created by qq522414074 on 2017/3/14.
 */

public class AdapterForSearchHistory extends BaseAdapter {
    private List<SearchHistory> list;
    private Context context;
    public  AdapterForSearchHistory(List<SearchHistory> list, Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    public void changeUI( List<SearchHistory> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public SearchHistory getItem(int position) {
        return list==null?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            holder=new Holder();
            convertView= LayoutInflater.from(context).inflate(R.layout.app_path_find_item,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.imageView);
            holder.textView=(TextView)convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
            holder.textView.setTag(holder);
            holder.imageView.setTag(holder);
        }else{
            holder=(Holder) convertView.getTag();
        }
        if(list!=null&&list.size()!=0&&list.get(position).searchtxt!=null){
            holder.textView.setText(list.get(position).searchtxt);
        }
        holder.imageView.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ManagerGps.getInstance().deleteSearchHistory(list.get(position).searchtxt);
                list.remove(list.get(position));
                ODispatcher.dispatchEvent(OEventName.DELETE_SEARCH_HISTORY);
            }
        });
        return convertView;
    }

   public final class Holder{
        public ImageView imageView;
       public  TextView textView;
    }


}
