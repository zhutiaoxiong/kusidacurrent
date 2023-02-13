package view.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ant.liao.GifView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import model.locator.SoundRecordingBean;

public class SoundRecordingAdapter extends BaseQuickAdapter<SoundRecordingBean, BaseViewHolder> {
    private Context context;
    public SoundRecordingAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<SoundRecordingBean> data, Context context) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, SoundRecordingBean item) {
        if(item.type==0){
            helper.setImageResource(R.id.img_sound_card,R.drawable.icon_sound_play_card_one);
        }else  if(item.type==1){
            helper.setImageResource(R.id.img_sound_card,R.drawable.icon_sound_play_card_two);
        }
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
        helper.setText(R.id.tv_count,(int)(item.percent*100)+"");
        View  bg=helper.getView(R.id.img_sound_bg);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) bg.getLayoutParams();
        layoutParams.width= ODipToPx.dipToPx(context,item.percent*100+89);
        //动态计算，设置item的宽高一致，总宽度-左右margin-左右padding /总列数-item左右margin-item左右padding
        if(item.isPlay){
            helper.setVisible(R.id.img_sound_play_gif,true);
            helper.setVisible(R.id.img_sound_play,false);
            GifView gifView=helper.getView(R.id.img_sound_play_gif);
            int width = ODipToPx.dipToPx(context, 12);
            int height = ODipToPx.dipToPx(context, 16.5f);
            gifView.setShowDimension(width, height);
            gifView.setGifImage(R.drawable.icon_sound_emission_gif);
        }else{
            helper.setVisible(R.id.img_sound_play,true);
            helper.setVisible(R.id.img_sound_play_gif,false);
        }
    }
}
