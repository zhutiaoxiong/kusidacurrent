package view.view4info.card;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import model.find.CardDetails;

/**
 * Created by qq522414074 on 2016/10/22.
 */
public class Cardslot extends LinearLayoutBase {
    public SmartImageView card_pic, bg_green;
    private Button count;
    public TextView txt_card_count;
    public Button give;
    public Button share;
    public int location;
    private boolean bgIsShow=false;
    private CardDetails cardDetail;
    private RelativeLayout black_bg;
    public Cardslot(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.cardslot, this, true);
        findViews();
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.HIDE_BG_GREEN, this);
        ODispatcher.addEventListener(OEventName.CARD_GIVE_RESULT, this);
    }

    private void findViews() {
        card_pic = (SmartImageView) findViewById(R.id.card_pic);
        bg_green = (SmartImageView) findViewById(R.id.bg_green);
        count = (Button) findViewById(R.id.count);
        txt_card_count = (TextView) findViewById(R.id.txt_card_count);
        give = (Button) findViewById(R.id.give);
        share = (Button) findViewById(R.id.share);
        black_bg = (RelativeLayout) findViewById(R.id.black_bg);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {

        card_pic.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(cardDetail==null)return;
                if(cardDetail.count==0)return;
                if (bgIsShow) {
                    handleSetButtonShow(false);
                } else {
                    ODispatcher.dispatchEvent(OEventName.HIDE_BG_GREEN, location);
                }
            }
        });
        give.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(cardDetail==null)return;
                if(cardDetail.count==0)return;
                ODispatcher.dispatchEvent(OEventName.SHOW_GIVE_CARD_PROMEBOX,cardDetail);
            }
        });
        share.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.SHOW_GIVE_SHARE_PROMEBOX,cardDetail );
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }

    public void setData(CardDetails cardDetails) {
        if(cardDetails==null)return;
        cardDetail=cardDetails;
        if (cardDetails.card.pic == null || cardDetails.card.pic .equals("")) return;
        card_pic.setImageUrl(cardDetails.card.pic );
        if (cardDetails.count > 99) {
            count.setText(99 + "+");
        } else {
            count.setText(cardDetails.count + "");
        }
        txt_card_count.setText(cardDetails.count + "");
    }


    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.HIDE_BG_GREEN)) {
            int loc=(Integer)o;
            if(loc==location){
                handleSetButtonShow(true);
            }else{
            if(bgIsShow)
            handleSetButtonShow(false);
            }
        }else if(s.equals(OEventName.CARD_GIVE_RESULT)){
//            int resultCode=(Integer) o;
//            if(resultCode==1&&bgIsShow){
            String resultCode=(String) o;
          if(resultCode.equals("")&&bgIsShow){
                handleSetButtonShow(false);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        ODispatcher.addEventListener(OEventName.HIDE_BG_GREEN, this);
        ODispatcher.addEventListener(OEventName.CARD_GIVE_RESULT, this);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.HIDE_BG_GREEN, this);
        ODispatcher.removeEventListener(OEventName.CARD_GIVE_RESULT, this);
        super.onDetachedFromWindow();
    }

    /**
     * @param isShow 设置按钮状态
     */
    public void handleSetButtonShow(final boolean isShow) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(50L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                Message message = Message.obtain();
                message.what = 111;
                message.obj = isShow;
                handler.sendMessage(message);
//            }
//        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 111) {
                boolean isShow = (Boolean) msg.obj;
                if (isShow) {
                    bg_green.setVisibility(View.VISIBLE);
                    give.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                    txt_card_count.setVisibility(View.VISIBLE);
                    black_bg.setBackgroundResource(R.drawable.cardbg);
                    bgIsShow=true;
                } else {
                    black_bg.setBackgroundResource(R.color.background_all);
                    bg_green.setVisibility(View.INVISIBLE);
                    give.setVisibility(View.INVISIBLE);
                    share.setVisibility(View.INVISIBLE);
                    txt_card_count.setVisibility(View.INVISIBLE);
                    bgIsShow=false;
                }
            }
        }
    };
}
