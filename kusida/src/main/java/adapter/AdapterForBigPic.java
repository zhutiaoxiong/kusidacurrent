package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;

import java.util.List;

/**
 * Created by qq522414074 on 2016/10/14.
 */
public class AdapterForBigPic extends BaseAdapter {
    private List<PicTureArray> list;
    private Context context;
    public AdapterForBigPic(List<PicTureArray> list, Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PicTureArray getItem(int position) {
        return list.get(position);
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
            convertView= LayoutInflater.from(context).inflate(R.layout.picture,null);
            holder.pic=(SmartImageView)convertView.findViewById(R.id.bigpic);
            convertView.setTag(holder);
        }else{
            holder=(Holder) convertView.getTag();
        }
        holder.pic.setImageResource(list.get(position).picid);
        return convertView;
    }
    class Holder{
        SmartImageView pic;
    }
}
