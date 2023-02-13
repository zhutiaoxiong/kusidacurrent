package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.BitmapGetNetAsync;
import com.kulala.staticsview.image.CircleImgCut;

import java.util.List;

import common.GlobalContext;
import model.common.DataAuthoredUser;

public class AdapterUserListCodriver extends BaseAdapter {
    private         LayoutInflater         mInflater;
    private         Context                mContext;
    private         List<DataAuthoredUser> list;
    private         int                    currentPosition;
    protected final int                    mItemLayoutId;

    // private boolean checkNeed = false;
    // private int imageId = 0;
    public AdapterUserListCodriver(Context context, List<DataAuthoredUser> listUser, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.list = listUser;
        this.mItemLayoutId = itemLayoutId;
    }

    public DataAuthoredUser getCurrentItem() {
        return list.get(currentPosition);
    }

    public void setCurrentItem(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public DataAuthoredUser getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {// ��ʼ��
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_head_2string, null);
            holder.text_name = (TextView) convertView.findViewById(R.id.text_name);
            holder.text_phone = (TextView) convertView.findViewById(R.id.text_phone);
            holder.image_head = (CircleImgCut) convertView.findViewById(R.id.image_head);
            holder.image_check = (ImageView) convertView.findViewById(R.id.image_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
        }
//		holder.text_name.setTag(holder);
//		holder.text_phone.setTag(holder);
//		holder.image_head.setTag(holder);
//		holder.image_check.setTag(holder);
        // 设初始显示数据
        DataAuthoredUser info = list.get(position);
        holder.text_name.setText(mContext.getResources().getString(R.string.license_plate_number_simbol) + info.carinfo.num);
        holder.text_phone.setText(info.userinfo.phoneNum + " " + info.userinfo.name);
        if(!TextUtils.isEmpty(info.userinfo.avatarUrl)){
            final CircleImgCut head = holder.image_head;
            new BitmapGetNetAsync().findImage(mContext, info.userinfo.avatarUrl, new BitmapGetNetAsync.OnGetImageListener() {
                @Override
                public void onGetImage(final Bitmap bitmap) {
                    GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap!=null){
                                head.setImageBitmap(bitmap);
                            }else{
                                head.setImageResource(R.drawable.head_no);
                            }
                        }
                    });
                }
            });
        }else {
            holder.image_head.setImageResource(R.drawable.head_no);
        }
//        else{
//            int resid = DataUser.getUserHeadResId(info.userinfo.avatarUrl);
//            if (!(resid == 0)) holder.image_head.setImageResource(resid);
//        }
        if (currentPosition == position) {
            holder.image_check.setImageResource(R.drawable.select_true);
        } else {
            holder.image_check.setImageResource(R.drawable.select_false);
        }
        return convertView;
    }

    public final class ViewHolder {
        public TextView text_name, text_phone,text_car_num;
        public ImageView  image_check;
        private CircleImgCut image_head;
    }
    // ===================================================

}

