package com.kulala.staticsview.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kulala.staticsview.R;
import com.kulala.staticsview.image.smart.SmartImageView;

import java.util.List;
/**
 * Created by Administrator on 2017/6/7.
 * 组图片,默认方形
 */

public class ImageGroup extends RelativeLayout {
    private static final int             MAX_IMAGE = 9;//最大图片数
    private              MyHandlerlerler handler   = new MyHandlerlerler();
    private List<String> imgUrlList;
    private int          specSize;//检测尺寸

    private SmartImageView img_single;
    private SmartImageView imgb_1, imgb_2, imgb_3, imgb_4;
    private SmartImageView imgs_1, imgs_2, imgs_3, imgs_4, imgs_5, imgs_6, imgs_7, imgs_8, imgs_9;
    private LinearLayout lin_b, lin_s;
    // ===================================================
    public ImageGroup(Context context) {
        super(context);
    }
    public ImageGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.image_group, this, true);
        img_single = (SmartImageView) findViewById(R.id.img_single);
        lin_b = (LinearLayout) findViewById(R.id.lin_b);//四图
        lin_s = (LinearLayout) findViewById(R.id.lin_s);//九图
        img_single.setVisibility(GONE);
        lin_b.setVisibility(GONE);
        lin_s.setVisibility(GONE);
    }
    // ===================================================
    /**
     * 总数超过9，只显示9
     */
    public void setImageList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
        if (imgUrlList == null) return;
        if (imgUrlList.size() >= 5) {//最小尺寸 17*17
            handler.obtainMessage(HANDLER_SHOW_MAX9_IMAGE).sendToTarget();
        } else if (imgUrlList.size() >= 2) {//中尺寸 27*27
            handler.obtainMessage(HANDLER_SHOW_MAX4_IMAGE).sendToTarget();
        } else if (imgUrlList.size() == 1) {//一张
            handler.obtainMessage(HANDLER_SHOW_SINGLE_IMAGE).sendToTarget();
        }
    }
    // ===================================================
    private static final int HANDLER_SHOW_SINGLE_IMAGE = 1;
    private static final int HANDLER_SHOW_MAX4_IMAGE = 2;
    private static final int HANDLER_SHOW_MAX9_IMAGE = 3;
    @SuppressLint("HandlerLeak")
    class MyHandlerlerler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            img_single.setVisibility(GONE);
            lin_b.setVisibility(GONE);
            lin_s.setVisibility(GONE);
            Resources res = getResources();
            switch (msg.what) {
                case HANDLER_SHOW_SINGLE_IMAGE://只显示一张图
                    if(imgUrlList.get(0) == null || imgUrlList.get(0).length()==0){
                        img_single.setVisibility(GONE);
                    }else{
                        img_single.setVisibility(VISIBLE);
//                        img_single.setImageUrl(imgUrlList.get(0), null);
                        img_single.setImageResource(R.drawable.group_default_heaad);
                    }
                    break;
                case HANDLER_SHOW_MAX4_IMAGE://显示4张图<
                    lin_b.setVisibility(VISIBLE);
                    //获取R 资源
                    for (int i = 1; i <= 4; i++) {//1-4
                        int            id   = res.getIdentifier("imgb_" + i, "id", getContext().getPackageName());
                        SmartImageView imgB = (SmartImageView) findViewById(id);
                        if (i > imgUrlList.size() || imgUrlList.get(i-1) == null || imgUrlList.get(i-1).length()==0) {
                            imgB.setVisibility(GONE);
                        } else {
                            imgB.setVisibility(VISIBLE);
                            imgB.setImageUrl(imgUrlList.get(i-1), null);
                        }
                    }
                    break;
                case HANDLER_SHOW_MAX9_IMAGE://显示9张图<
                    lin_s.setVisibility(VISIBLE);
                    for (int i = 1; i <= 9; i++) {//1-4
                        int            id   = res.getIdentifier("imgs_" + i, "id", getContext().getPackageName());
                        SmartImageView imgB = (SmartImageView) findViewById(id);
                        if (i > imgUrlList.size() || imgUrlList.get(i-1) == null || imgUrlList.get(i-1).length()==0) {
                            imgB.setVisibility(GONE);
                        } else {
                            imgB.setVisibility(VISIBLE);
                            imgB.setImageUrl(imgUrlList.get(i-1), null);
                        }
                    }
                    break;
            }
        }
    }
}
