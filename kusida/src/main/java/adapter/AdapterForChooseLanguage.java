package adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.dbHelper.ODBHelper;

import java.util.List;

/**
 * Created by qq522414074 on 2016/11/17.
 */

public class  AdapterForChooseLanguage extends BaseAdapter {
    private List<String> list;
    private Context context;
    private int courrentPosition;
    public AdapterForChooseLanguage(List<String> list,Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    public void setCurrentItem(int position){
       this.courrentPosition=position;
    }
    public int getCourrentPosition(){
        return courrentPosition;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            holder=new Holder();
            convertView= LayoutInflater.from(context).inflate(R.layout.view_me_choose_language_item,null);
            holder.languageType=(TextView) convertView.findViewById(R.id.language);
            holder.biaoji=(ImageView) convertView.findViewById(R.id.biaoji);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
            if(list==null||list.size()==0) return convertView;
            holder.languageType.setText(list.get(position));
        String currentItemposition= ODBHelper.getInstance(context).queryCommonInfo("position");
        int currentItemPosition = -1;
        if(!TextUtils.isEmpty(currentItemposition))currentItemPosition = Integer.valueOf(currentItemposition);

        if(currentItemPosition==-1){
            holder.biaoji.setImageResource(R.drawable.select_false);
           return convertView;
        }
        courrentPosition=currentItemPosition;
        if(position==courrentPosition) {
            holder.biaoji.setImageResource(R.drawable.select_true);
        }else{
            holder.biaoji.setImageResource(R.drawable.select_false);
        }
        return convertView;
    }
  public   class Holder{
        TextView languageType;
        ImageView biaoji;
    }
}
