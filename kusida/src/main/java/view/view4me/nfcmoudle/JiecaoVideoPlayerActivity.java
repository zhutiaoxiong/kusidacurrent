package view.view4me.nfcmoudle;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;


import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import model.ManagerPublicData;
import model.vedeo.VedeoBean;
import view.view4me.set.ClipTitleMeSet;

public class JiecaoVideoPlayerActivity extends AppCompatActivity {
    private ClipTitleMeSet title_head;
    private ListView list_view;
    private int firstVisible;//当前第一个可见的item
    private int visibleCount;//当前可见的item个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiecao);
        title_head = findViewById(R.id.title_head);
        if(ManagerPublicData.nfcInfo.equals("1")){
            title_head.txt_title_show.setText("如何添加手机NFC");
        }else{
            title_head.txt_title_show.setText("如何添加NFC卡片");
        }
        list_view = findViewById(R.id.list_view);
        initDatas();
        initListener();
        initEvent();
    }

    private void initDatas() {
        ArrayList<VedeoBean> datas = new ArrayList<>();
        if(ManagerPublicData.nfcInfo.equals("1")){
            for (int i = 0; i < 4; i++) {
                String s1 = "http://manage.kcmoco.com/app/huaweiNFC.mp4";
                String s2 = "http://manage.kcmoco.com/app/vivoNFC.mp4";
                String s3 = "http://manage.kcmoco.com/app/iphoneNFC.mp4";
                String s4 = "http://manage.kcmoco.com/app/baika.mp4";
                switch (i) {
                    case 0:
                        datas.add(new VedeoBean(s1, R.drawable.img_nfc_set_huawei, "华为手机如何在手机中生成NFC智卡"));
                        break;
                    case 1:
                        datas.add(new VedeoBean(s2, R.drawable.img_nfc_set_opvivo, "oppo vivo手机如何在手机中生成NFC智卡"));
                        break;
                    case 2:
                        datas.add(new VedeoBean(s3, R.drawable.img_nfc_set_no_apple, "苹果手机如何在手机中生成NFC智卡"));
                        break;
                    case 3:
                        datas.add(new VedeoBean(s4, R.drawable.img_nfc_set_no_card, "针对部分手机有NFC却无法生成白卡解决方案"));
                        break;
                }
            }
        }else{
            String s5 = "http://manage.kcmoco.com/app/write_iphoneNFC.mp4";
            datas.add(new VedeoBean(s5, R.drawable.img_nfc_set_haoh, "如何添加NFC卡片"));
        }
        VideoAdapter adapter = new VideoAdapter(JiecaoVideoPlayerActivity.this, datas, R.layout.item_video);
        list_view.setAdapter(adapter);
    }

    /**
     * 滑动监听
     */
    private void initListener() {
        //滑动停止自动播放视频
        AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //滑动停止自动播放视频
                        autoPlayVideo(view);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisible == firstVisibleItem) {
                    return;
                }

                firstVisible = firstVisibleItem;
                visibleCount = visibleItemCount;
            }
        };

        list_view.setOnScrollListener(onScrollListener);
    }

    /**
     * 滑动停止自动播放视频
     */
    private void autoPlayVideo(AbsListView view) {

        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.player_list_video) != null) {
                JCVideoPlayerStandard currPlayer = view.getChildAt(i).findViewById(R.id.player_list_video);
                Rect rect = new Rect();
                //获取当前view 的 位置
                currPlayer.getLocalVisibleRect(rect);
                int videoheight = currPlayer.getHeight();
                if (rect.top == 0 && rect.bottom == videoheight) {
                    if (currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                            || currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {
                        currPlayer.startButton.performClick();
                    }
                    return;
                }
            }
        }
        //释放其他视频资源
        JCVideoPlayer.releaseAllVideos();
    }

    private void initEvent() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (JCVideoPlayer.backPress()) {
                    return;
                }
//                Intent intent = new Intent();
//                intent.setClass(JiecaoVideoPlayerActivity.this, ActivityKulalaMain.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
//        Intent intent = new Intent();
//        intent.setClass(JiecaoVideoPlayerActivity.this, ActivityKulalaMain.class);
//        startActivity(intent);
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
