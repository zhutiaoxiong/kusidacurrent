package view.view4info.card;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import model.find.CardDetails;
import model.find.ManagerCardInfo;

/**
 * Created by qq522414074 on 2016/9/26.
 */
public class LastItemViewForViewPager extends LinearLayoutBase {
    private ImageView guide;
    private TextView cardType;
    public SmartImageView card_pic, bg_green;
    private Button count;
    public TextView txt_card_count;
    public Button give;
    public Button share;
    public int location = 30;
    private boolean bgIsShow = false;
    private int mark;
    private List<CardDetails> list = new ArrayList<>();
    private int cardcount;
    private RelativeLayout layout_1;
    private Button touch_exit_1;

    public LastItemViewForViewPager(Context context, AttributeSet attributeSet, int mark) {
        super(context, attributeSet);
        this.mark = mark;
        LayoutInflater.from(context).inflate(R.layout.last_item_for_viewpager, this, true);
        findViews();
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.HIDE_BG_GREEN, this);
        ODispatcher.addEventListener(OEventName.GET_CARDINFO_LIST_RESULTBACK, this);
    }

    private void findViews() {
        guide = (ImageView) findViewById(R.id.guide);
        cardType = (TextView) findViewById(R.id.cardtype);
        card_pic = (SmartImageView) findViewById(R.id.card_pic);
        bg_green = (SmartImageView) findViewById(R.id.bg_green);
        count = (Button) findViewById(R.id.count);
        txt_card_count = (TextView) findViewById(R.id.txt_card_count);
        give = (Button) findViewById(R.id.give);
        share = (Button) findViewById(R.id.share);
        layout_1 = (RelativeLayout) findViewById(R.id.layout_1);
        touch_exit_1 = (Button) findViewById(R.id.touch_exit_1);
    }

    @Override
    protected void initViews() {
        handleChangeData();
    }

    @Override
    protected void initEvents() {
        guide.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.POP_NAVIGATION);
            }
        });

        card_pic.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (cardcount > 0) {
                    if (bgIsShow) {
                        handleSetButtonShow(false);
                    } else {
                        ODispatcher.dispatchEvent(OEventName.HIDE_BG_GREEN, 30);
                    }
                }
            }
        });
        give.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (list.get(0) == null) return;
                if (list.get(0).count == 0) return;
                ODispatcher.dispatchEvent(OEventName.SHOW_GIVE_CARD_PROMEBOX, list.get(0));
            }
        });
        share.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                handleSetButtonShow(false);
                ODispatcher.dispatchEvent(OEventName.SHOW_GIVE_SHARE_PROMEBOX, mark);
            }
        });
        touch_exit_1.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.HIDE_BG_GREEN, -1);
            }
        });
    }

    public void firstShow() {
        list = ManagerCardInfo.getInstance().useCardList(mark);
        cardType.setText(getResources().getString(R.string.ultimate_card));
        if (list == null || list.size() == 0 || list.get(0) == null)
            return;
        cardcount = list.get(0).count;
        setData(list.get(0).count, list.get(0).card.pic);
    }

    @Override
    protected void invalidateUI() {
        firstShow();
    }

    public void setData(int cardCount, String pic) {
        if (pic == null || pic.equals("")) return;
        card_pic.setImageUrl(pic);
        if (cardCount > 99) {
            count.setText(99 + "+");
        } else {
            count.setText(cardCount + "");
        }
        txt_card_count.setText(cardCount + "");
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.HIDE_BG_GREEN)) {
            int state = (Integer) o;
            if (state == -1) {
                handleSetButtonShow(false);
                handleTransparentWindowDismiss();
            } else {
                if (state == location) {
                    handleSetButtonShow(true);
                } else {
                    if (bgIsShow) {
                        handleSetButtonShow(false);
                    }
                }
                handleTransparentWindowShow();
            }
        } else if (s.equals(OEventName.GET_CARDINFO_LIST_RESULTBACK)) {
            handleChangeData();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.HIDE_BG_GREEN, this);
        ODispatcher.removeEventListener(OEventName.GET_CARDINFO_LIST_RESULTBACK, this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        ODispatcher.addEventListener(OEventName.HIDE_BG_GREEN, this);
        ODispatcher.addEventListener(OEventName.GET_CARDINFO_LIST_RESULTBACK, this);
        super.onAttachedToWindow();
    }

    /**
     * @param isShow 设置按钮状态
     */
    public void handleSetButtonShow(final boolean isShow) {
        Message message = Message.obtain();
        message.what = 111;
        message.obj = isShow;
        handler.sendMessage(message);
    }

    private void handleTransparentWindowShow() {
        Message message = Message.obtain();
        message.what = 120;
        handler.sendMessage(message);
    }

    private void handleTransparentWindowDismiss() {
        Message message = Message.obtain();
        message.what = 121;
        handler.sendMessage(message);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 111) {
                boolean isShow = (Boolean) msg.obj;
                if (isShow) {
                    layout_1.setBackgroundResource(R.drawable.cardbg);
                    bg_green.setVisibility(View.VISIBLE);
                    give.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                    txt_card_count.setVisibility(View.VISIBLE);
                    bgIsShow = true;
                } else {
                    layout_1.setBackgroundResource(R.color.background_all);
                    bg_green.setVisibility(View.INVISIBLE);
                    give.setVisibility(View.INVISIBLE);
                    share.setVisibility(View.INVISIBLE);
                    txt_card_count.setVisibility(View.INVISIBLE);
                    bgIsShow = false;
                }
            }  else if(msg.what==120){
                touch_exit_1.setVisibility(View.VISIBLE);
            }else if(msg.what==121){
                touch_exit_1.setVisibility(View.INVISIBLE);
            }
        }
    };
}
