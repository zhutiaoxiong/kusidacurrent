package view.view4me.shake;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;

import common.GlobalContext;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import view.view4me.set.ClipTitleMeSet;

public class JiecaoVideoPlayerShakeActivity extends AppCompatActivity {
    private ClipTitleMeSet title_head;
    private JCVideoPlayerStandard player_list_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_me_switch_tips);
        title_head = findViewById(R.id.title_head);
         player_list_video = findViewById(R.id.player_list_video);
        String s1 = "http://manage.kcmoco.com/app/huaweibaohuo.mp4";
        player_list_video.setUp(s1, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        Glide.with(JiecaoVideoPlayerShakeActivity.this).load(R.drawable.img_shake_vedeo_res
        ).into(player_list_video.thumbImageView);
        initEvent();
    }
    private void initEvent() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (JCVideoPlayer.backPress()) {
                    return;
                }
//                Intent intent = n
//                ew Intent();
//                intent.setClass(JiecaoVideoPlayerShakeActivity.this, ActivityKulalaMain.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(player_list_video!=null){
            Glide.with(GlobalContext.getCurrentActivity()).load(R.drawable.img_shake_vedeo_res
            ).into(player_list_video.thumbImageView);
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
//        Intent intent = new Intent();
//        intent.setClass(JiecaoVideoPlayerShakeActivity.this, ActivityKulalaMain.class);
//        startActivity(intent);
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
