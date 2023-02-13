package view.view4me.shake;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import common.GlobalContext;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2017/10/25.
 */

public class ViewSwitchShakeTips extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private JCVideoPlayerStandard player_list_video;

    public ViewSwitchShakeTips(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_me_switch_tips, this, true);
        title_head =  findViewById(R.id.title_head);
        player_list_video =  findViewById(R.id.player_list_video);
        String s1 = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";
        player_list_video.setUp(s1, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
        Glide.with(GlobalContext.getCurrentActivity()).load(R.drawable.img_shake_vedeo_res
        ).into(player_list_video.thumbImageView);
        initEvents();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (JCVideoPlayer.backPress()) {
                    return;
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_shake);
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    protected void onDetachedFromWindow() {
        JCVideoPlayer.releaseAllVideos();
        super.onDetachedFromWindow();
    }
}
