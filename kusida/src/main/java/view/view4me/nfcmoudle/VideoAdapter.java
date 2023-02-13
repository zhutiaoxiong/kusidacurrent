package view.view4me.nfcmoudle;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.client.proj.kusida.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import model.vedeo.VedeoBean;

public class VideoAdapter extends CommonAdapter<VedeoBean> {
    private Context mContext;

    public VideoAdapter(Context context, List<VedeoBean> datas, int layoutId) {
        super(context, layoutId, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(ViewHolder viewHolder, VedeoBean bean, int position) {
        JCVideoPlayerStandard player = viewHolder.getView(R.id.player_list_video);

//        if (player != null) {
//            player.release();
//        }
        player.setUp(bean.vedeoUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST, bean.title);
//        if (setUp) {
            Glide.with(mContext).load(bean.vedeoRes).into(player.thumbImageView);
//        }
    }
}
