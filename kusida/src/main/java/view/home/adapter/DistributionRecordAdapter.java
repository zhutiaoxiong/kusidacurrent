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

import model.locator.DistributionRecordBean;

public class DistributionRecordAdapter extends BaseQuickAdapter<DistributionRecordBean, BaseViewHolder> {
    public DistributionRecordAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<DistributionRecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, DistributionRecordBean item) {
        helper.setText(R.id.tv_work_mode,item.workMode);
        if(item.sendStatus==0){
            helper.setText(R.id.tv_send_status,"已发送");
        }
        if(item.replyStatus==0){
            helper.setText(R.id.tv_reply_status,"已回复");
        }
        String time=ODateTime.time2StringDateDetail(item.sendTimeOne);
        if(!TextUtils.isEmpty(time)){
            helper.setText(R.id.tv_send_time_one,time);
        }
        String timeTwo=ODateTime.time2StringDateDetail(item.sendTimeTwo);
        if(!TextUtils.isEmpty(timeTwo)){
            helper.setText(R.id.tv_send_time_two,timeTwo);
        }

    }
}
