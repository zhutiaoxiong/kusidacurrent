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

import model.locator.ChangEquipmentBean;

public class ChanEquipmentAdapter extends BaseQuickAdapter<ChangEquipmentBean, BaseViewHolder> {
    public ChanEquipmentAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<ChangEquipmentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, ChangEquipmentBean item) {
        if(item.isOnLine==1){
            helper.setTextColor(R.id.tv_time, Color.parseColor("#0066ff"));
            helper.setTextColor(R.id.tv_username, Color.parseColor("#0066ff"));
            if(!TextUtils.isEmpty(item.time)){
                helper.setText(R.id.tv_time,"在线"+item.time) ;
            }else{
                helper.setText(R.id.tv_time,"在线") ;
            }
        }else{
            helper.setTextColor(R.id.tv_time, Color.parseColor("#888888"));
            helper.setTextColor(R.id.tv_username, Color.parseColor("#888888"));
            if(!TextUtils.isEmpty(item.time)){
                helper.setText(R.id.tv_time,"离线"+item.time) ;
            }else{
                helper.setText(R.id.tv_time,"离线") ;
            }
        }

        if(!TextUtils.isEmpty(item.username)){
            helper.setText(R.id.tv_username,item.username) ;
        }else{
            helper.setText(R.id.tv_username,"") ;
        }
        if(item.type==0){
            helper.setImageResource(R.id.loc_state,R.drawable.icon_head_type_0) ;
        }else   if(item.type==1){
            helper.setImageResource(R.id.loc_state,R.drawable.icon_head_type_1) ;
        }else   if(item.type==2){
            helper.setImageResource(R.id.loc_state,R.drawable.icon_head_type_2) ;
        }else   if(item.type==3){
            helper.setImageResource(R.id.loc_state,R.drawable.icon_head_type_3) ;
        }else   if(item.type==4){
            helper.setImageResource(R.id.loc_state,R.drawable.icon_head_type_4) ;
        }
//        helper.setImageResource(R.id.loc_state,R.drawable.locator_icon_list_item_true) ;

    }
}
