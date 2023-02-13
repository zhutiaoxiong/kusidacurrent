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

import model.ManagerLoginReg;

/**
 * Created by qq522414074 on 2017/1/19.
 */

public class AdapterUerNameHistory extends BaseAdapter {
    private Context context;
    private List<String> list;


    public AdapterUerNameHistory(Context context,List<String> list,OnLastOneDeleteListner liss){
        this.context=context;
        this.list=list;
        this.listner=liss;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list==null?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
          Holder holder=null;
        if(convertView==null){
            holder=new Holder();
            convertView= LayoutInflater.from(context).inflate(R.layout.view_login_username_list_item,null);
            holder.tv= (TextView) convertView.findViewById(R.id.username);
            holder.delete= (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
            holder.tv.setTag(holder);
            holder.delete.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }
        if(list!=null){
            holder.tv.setText(list.get(position));
        }

        holder.tv.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                Holder holder1=  (Holder)view.getTag();
             String a=  holder1.tv.getText().toString();
                ODispatcher.dispatchEvent(OEventName.LOGIN_USERNAME_SELECT,a);
            }
        });
        holder.delete.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                Holder holder1=  (Holder)view.getTag();
                list.remove(holder1.tv.getText().toString());
                ManagerLoginReg.getInstance().clearOneUserHistory(holder1.tv.getText().toString());
                notifyDataSetChanged();
                if(list==null||list.size()==0){
                    if(listner!=null){
                        listner.onDeleteLast();
                    }
                }
            }
        });
        return convertView;
    }
    class Holder{
        public int position;
        public TextView tv;
        public ImageView delete;
    }
    public interface  OnLastOneDeleteListner{
        void onDeleteLast();
    }
    private OnLastOneDeleteListner listner;
}
