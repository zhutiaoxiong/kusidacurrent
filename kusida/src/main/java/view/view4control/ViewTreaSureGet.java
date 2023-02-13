package view.view4control;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import common.timetick.OTimeSchedule;
import ctrl.OCtrlCard;
import model.find.ManagerCardInfo;

/**
 * Created by qq522414074 on 2016/9/12.
 */
public class ViewTreaSureGet extends LinearLayoutBase {
    private SmartImageView treasureopen, card;
    private TextView car, card_level, congratulation_get;
    private GifView treasureopen_gif;
    private int timeCount;

    public ViewTreaSureGet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.treasure_get, this, true);
        card = (SmartImageView) findViewById(R.id.card);
        treasureopen = (SmartImageView) findViewById(R.id.treasure_open);
        car = (TextView) findViewById(R.id.car);
        card_level = (TextView) findViewById(R.id.card_level);
        treasureopen_gif = (GifView) findViewById(R.id.treasure_open_gif);
        congratulation_get = (TextView) findViewById(R.id.congratulation_get);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        if (ManagerCardInfo.cardInfo!=null&&ManagerCardInfo.cardInfo.pic != null&&!ManagerCardInfo.cardInfo.pic.equals("")) {
            card.setImageUrl(ManagerCardInfo.cardInfo.pic);
        car.setText(ManagerCardInfo.cardInfo.name);
        card_level.setText(ManagerCardInfo.cardInfo.typeStr);
        }
        initTreasure();
        congratulation_get.setText(getResources().getString(R.string.congratulations_to_you_to_get_a_card));
        timeCount = 0;
        OTimeSchedule.getInstance().init();
        ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, ViewTreaSureGet.this);

    }

    public void handlemessstopToast() {
        Message message = Message.obtain();
        message.what = 110;
        handler.sendEmptyMessage(110);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 110:
                    congratulation_get.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.TIME_TICK_SECOND)) {
            timeCount++;
            if (timeCount >= 2) {
                handlemessstopToast();
                ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, ViewTreaSureGet.this);
            }
        }
    }

    private void initTreasure() {

       final String openPic=ManagerCardInfo.openPic;
        if(openPic==null||openPic.equals("")){return;}
        if ((openPic.substring(openPic.length() - 3,openPic.length())).equals("gif")) {
            treasureopen_gif.setVisibility(View.VISIBLE);
            int i = ODipToPx.dipToPx(ViewTreaSureGet.this.getContext(), 254);
            treasureopen_gif.setShowDimension(i, i);
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] imageByte = ViewTreasure.getImageByte(openPic);
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = imageByte;
                            handler1.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            treasureopen.setVisibility(View.VISIBLE);
            treasureopen.setImageUrl(openPic);
        }
    }

    @Override
    protected void initEvents() {
        this.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                OCtrlCard.getInstance().ccmd1404_get_cardinfo();
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });


        treasureopen.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OCtrlCard.getInstance().ccmd1404_get_cardinfo();
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        treasureopen_gif.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OCtrlCard.getInstance().ccmd1404_get_cardinfo();
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }

    private android.os.Handler handler1 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    byte[] bytegif = (byte[]) msg.obj;
                    treasureopen_gif.setGifImage(bytegif);
                }
            }
        }
    };
}
