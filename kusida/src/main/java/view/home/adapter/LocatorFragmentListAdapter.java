package view.home.adapter;

import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.client.proj.kusida.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import model.locator.LocatorFragmentListBean;

public class LocatorFragmentListAdapter extends BaseQuickAdapter<LocatorFragmentListBean, BaseViewHolder> {
    public LocatorFragmentListAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<LocatorFragmentListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, LocatorFragmentListBean item) {
        if(item.isOnLine==1){
            helper.setImageResource(R.id.loc_state,R.drawable.locator_icon_list_item_true) ;
            helper.setTextColor(R.id.tv_time, Color.parseColor("#0066ff"));
        }else{
            helper.setTextColor(R.id.tv_time, Color.parseColor("#888888"));
            helper.setImageResource(R.id.loc_state,R.drawable.locator_icon_list_item_false) ;
        }
        if(!TextUtils.isEmpty(item.username)){
            helper.setText(R.id.tv_username,item.username) ;
        }else{
            helper.setText(R.id.tv_username,"") ;
        }
        if(!TextUtils.isEmpty(item.equipmentNumber)){
            helper.setText(R.id.tv_equipment_number,"设备编号:"+item.equipmentNumber) ;
        }else{
            helper.setText(R.id.tv_equipment_number,"设备编号:") ;
        }
        if(!TextUtils.isEmpty(item.msiNumber)){
            helper.setText(R.id.msi_number,"MSI号:"+item.msiNumber) ;
        }else{
            helper.setText(R.id.msi_number,"MSI号:") ;
        }
        helper.setText(R.id.tv_last_record,"录音剩余: "+item.recordCount) ;
        if(!TextUtils.isEmpty(item.note)){
            helper.setText(R.id.tv_note,item.note) ;
        }else{
            helper.setText(R.id.tv_note,"") ;
        }
        if(item.isOnLine==1){
            if(!TextUtils.isEmpty(item.time)){
                helper.setText(R.id.tv_time,"在线"+item.time) ;
            }else{
                helper.setText(R.id.tv_time,"在线") ;
            }
        }else{
            if(!TextUtils.isEmpty(item.time)){
                helper.setText(R.id.tv_time,"离线"+item.time) ;
            }else{
                helper.setText(R.id.tv_time,"离线") ;
            }
        }
        if(item.isSelect){
            helper.setImageResource(R.id.select_status,R.drawable.locator_icon_list_item_check_true) ;
        }else{
            helper.setImageResource(R.id.select_status,R.drawable.locator_icon_list_item_check_false) ;
        }
    }
}
