package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.client.proj.kusida.R;

import java.util.List;

import model.loginreg.DataUser;

/**
 * Created by xiong on 2016/7/19.
 */
public class AdapterUserListLocal extends BaseAdapter implements SectionIndexer {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DataUser> list;
    private int currentPosition;
    protected final int mItemLayoutId;

    // private boolean checkNeed = false;
    // private int imageId = 0;
    public AdapterUserListLocal(Context context, List<DataUser> listUser, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.list = listUser;
        this.mItemLayoutId = itemLayoutId;
    }

    /**
     * @param list
     */
    public void updateListView(List<DataUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }

//    public DataUser getCurrentItem() {
//
//        return list.get(currentPosition);
//    }

//    public void setCurrentItem(int position) {
//        currentPosition = position;
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public DataUser getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_local_head_string, null);
            holder.text_name = (TextView) convertView.findViewById(R.id.title);
            holder.text_phone = (TextView) convertView.findViewById(R.id.number);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            holder.cross_line = (ImageView) convertView.findViewById(R.id.cross_line);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();

        }
//		holder.text_name.setTag(holder);
//		holder.text_phone.setTag(holder);
//		holder.image_head.setTag(holder);
//		holder.image_check.setTag(holder);
        // 设初始显示数据
        //// 获取首字母的assii值
         DataUser info = list.get(position);
        int section = getSectionForPosition(position);
        //通过首字母的assii值来判断是否显示字母
        int positionForSelection = getPositionForSection(section);

        holder.tvLetter.setOnClickListener(null);
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.cross_line.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(info.sortLetters);
        } else {
            holder.tvLetter.setVisibility(View.GONE);
            holder.cross_line.setVisibility(View.GONE);
        }
      if (!info.name.equals("")){
          holder.text_name.setText(info.name);
           holder.text_phone.setText(info.phoneNum);}
//        int resid = DataUser.getUserHeadResId(info.avatarUrl);


        return convertView;
    }


    class MyViewHolder {
        public TextView text_name, text_phone, tvLetter;
        public ImageView cross_line;

    }


    // ===================================================
    public List<DataUser> getDataList() {
        return list;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).sortLetters.charAt(0);
    }


}
