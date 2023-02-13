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

import model.locator.MessageBean;

public class MessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public MessageAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<MessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, MessageBean item) {
        if(item.type==0){
            helper.setText(R.id.tv_type,"震动报警");
        }else  if(item.type==1){
            helper.setText(R.id.tv_type,"超速报警");
        }else  if(item.type==2){
            helper.setText(R.id.tv_type,"出点子围栏");
        }
        helper.setText(R.id.tv_aderess,item.aderess);
        String time=ODateTime.time2StringDateDetail(item.time);
        if(!TextUtils.isEmpty(time)){
            helper.setText(R.id.tv_time,time);
        }
        helper.setVisible(R.id.select_status,item.isShow) ;
        if(item.isSelect){
            helper.setImageResource(R.id.select_status,R.drawable.locator_icon_list_item_check_true) ;
        }else{
            helper.setImageResource(R.id.select_status,R.drawable.locator_icon_list_item_check_false) ;
        }
    }
}
