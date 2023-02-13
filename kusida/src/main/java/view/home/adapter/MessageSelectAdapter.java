package view.home.adapter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.client.proj.kusida.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import model.locator.MessageSelectBean;
public class MessageSelectAdapter extends BaseQuickAdapter<MessageSelectBean, BaseViewHolder> {
    public MessageSelectAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<MessageSelectBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, MessageSelectBean item) {
        Log.e("ActivityMessageSelect", "convert ");
        helper.setText(R.id.name,item.name);
        if(item.isSelect){
            helper.setImageResource(R.id.select_status,R.drawable.locator_icon_list_item_check_true) ;
        }else{
            helper.setImageResource(R.id.select_status,R.drawable.locator_icon_list_item_check_false) ;
        }
        if(item.isShowLine){
            helper.setVisible(R.id.line,true) ;
        }else{
            helper.setVisible(R.id.line,false) ;
        }
    }
}
