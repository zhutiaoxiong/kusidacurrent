package view.home.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_system.ODateTime;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import model.locator.LocatorTrackListBean;

public class LocTrackListAdapter extends BaseQuickAdapter<LocatorTrackListBean, BaseViewHolder> {
    public LocTrackListAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<LocatorTrackListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, LocatorTrackListBean item) {
        helper.setText(R.id.tv_start_aderess,"|  "+item.startAderess);
        helper.setText(R.id.tv_end_aderess,"|  "+item.endAderess);
        helper.setText(R.id.tv_name,item.name);
        String startTime=ODateTime.time2StringDateDetail(item.startTime);
        if(!TextUtils.isEmpty(startTime)){
            helper.setText(R.id.tv_start_time,startTime);
        }
        String endTime=ODateTime.time2StringDateDetail(item.endTime);
        if(!TextUtils.isEmpty(endTime)){
            helper.setText(R.id.tv_end_time,endTime);
        }
        helper.setVisible(R.id.select_status,item.isShow) ;
        if(item.isSelect){
            helper.setImageResource(R.id.select_status,R.drawable.locator_icon_list_item_check_true) ;
        }else{
            helper.setImageResource(R.id.select_status,R.drawable.locator_icon_list_item_check_false) ;
        }
        if(item.isColect){
            helper.setImageResource(R.id.iv_colect,R.drawable.icon_loc_track_colect_true) ;
        }else{
            helper.setImageResource(R.id.iv_colect,R.drawable.icon_loc_track_colect_false) ;
        }
    }
}
