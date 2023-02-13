package view.view4control;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.ant.liao.GifView;
import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import common.pinyinzhuanhuan.StreamTool;
import ctrl.OCtrlCard;
import model.find.ManagerCardInfo;

/**
 * Created by qq522414074 on 2016/9/18.
 */
public class ViewTreasure extends LinearLayoutBase {
    private SmartImageView treasure;
    private GifView treasureGif;

    public ViewTreasure(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_treasure, this, true);
        treasureGif = (GifView) findViewById(R.id.treasure_gif);
        treasure=(SmartImageView)findViewById(R.id.treasure_pic) ;
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GET_CARDINFO_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.COMMIT_CARDINFO_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.COMMIT_CARDINFO_RESULTBACK_FAILED, this);
    }

    @Override
    protected void initViews() {
      OCtrlCard.getInstance().ccmd1404_get_cardinfo();
//        ManagerCardInfo.getInstance().saveCardInfo(null);
//        ODispatcher.dispatchEvent(OEventName.GET_CARDINFO_RESULTBACK);
    }

    @Override
    protected void initEvents() {
        treasure.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OCtrlCard.getInstance().ccmd1406_commit_card(ManagerCardInfo.cardInfo.ide);
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.treasure_get);
            }
        });
        treasureGif.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OCtrlCard.getInstance().ccmd1406_commit_card(ManagerCardInfo.cardInfo.ide);
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.treasure_get);
            }
        });


    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.GET_CARDINFO_RESULTBACK)) {
            handleChangeData();
        } else if (s.equals(OEventName.COMMIT_CARDINFO_RESULTBACK)) {
           ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.treasure_get);
        } else if (s.equals(OEventName.COMMIT_CARDINFO_RESULTBACK_FAILED)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.the_failure_please_try_again));
        }
    }

    @Override
    protected void invalidateUI() {
        initTreasure();
    }

    private void initTreasure() {
        treasure.setVisibility(View.INVISIBLE);
        treasureGif.setVisibility(View.INVISIBLE);
        if (ManagerCardInfo.isShow != 1)return;
        if (ManagerCardInfo.closePic == null)return;
        if (ManagerCardInfo.closePic.equals(""))return;
        if (ManagerCardInfo.openPic  == null)return;
        if (ManagerCardInfo.openPic.equals(""))return;
        if (ManagerCardInfo.cardInfo ==null)return;
        if (ManagerCardInfo.cardInfo.pic==null)return;
        if (ManagerCardInfo.cardInfo.pic.equals(""))return;
            final String closePic=ManagerCardInfo.closePic;
            if ((closePic.substring(closePic.length() - 3, closePic.length())).equals("gif")) {
                treasureGif.setVisibility(View.VISIBLE);
                treasure.setVisibility(View.INVISIBLE);
                int i = ODipToPx.dipToPx(ViewTreasure.this.getContext(), 119);
                treasureGif.setShowDimension(i, i);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] byteGif = getImageByte(closePic);
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = byteGif;
                            handler1.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                treasure.setVisibility(View.VISIBLE);
                treasureGif.setVisibility(View.INVISIBLE);
                treasure.setImageUrl(closePic);
            }
        }


    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.GET_CARDINFO_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.COMMIT_CARDINFO_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.COMMIT_CARDINFO_RESULTBACK_FAILED, this);
        super.onDetachedFromWindow();
    }

    public static byte[] getImageByte(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // 设置请求方法为GET
        conn.setReadTimeout(5 * 1000); // 设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream(); // 通过输入流获得图片数据
        byte[] data = StreamTool.read(inputStream); // 获得图片的二进制数据
        return data;
    }

    private android.os.Handler handler1 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    byte[] bytegif = (byte[]) msg.obj;
                    treasureGif.setGifImage(bytegif);
                }
            }
        }
    };
}



