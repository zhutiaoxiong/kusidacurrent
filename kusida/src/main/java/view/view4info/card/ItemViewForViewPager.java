package view.view4info.card;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import ctrl.OCtrlCard;
import model.find.CardDetails;
import model.find.ManagerCardInfo;

/**
 * Created by qq522414074 on 2016/9/21.
 */
public class ItemViewForViewPager extends LinearLayoutBase {
    private ImageView guide;
    private TextView cardType;
    private List<CardDetails> list = new ArrayList<>();
    private Cardslot cardslot_1, cardslot_2, cardslot_3, cardslot_4, cardslot_5, cardslot_6;
    public int mark;
    private Button button_layout;
    private Button touch_exit_1;

    public ItemViewForViewPager(Context context, AttributeSet attributeSet, int mark) {
        super(context, attributeSet);
        this.mark = mark;
//        "初级卡" "中级卡""高级卡""豪华卡""奢华卡"
        LayoutInflater.from(context).inflate(R.layout.card_viewpager_itemview, this, true);
        button_layout = (Button) findViewById(R.id.button_layout);
        findView();
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GET_CARDINFO_LIST_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.HIDE_BG_GREEN, this);
    }

    private void findView() {
        guide = (ImageView) findViewById(R.id.guide);
        cardType = (TextView) findViewById(R.id.cardtype);
        cardslot_1 = (Cardslot) findViewById(R.id.cardlost_1);
        cardslot_2 = (Cardslot) findViewById(R.id.cardlost_2);
        cardslot_3 = (Cardslot) findViewById(R.id.cardlost_3);
        cardslot_4 = (Cardslot) findViewById(R.id.cardlost_4);
        cardslot_5 = (Cardslot) findViewById(R.id.cardlost_5);
        cardslot_6 = (Cardslot) findViewById(R.id.cardlost_6);
        touch_exit_1 = (Button) findViewById(R.id.touch_exit_1);
        cardslot_1.location = 0+mark*6;
        cardslot_2.location = 1+mark*6;
        cardslot_3.location = 2+mark*6;
        cardslot_4.location = 3+mark*6;
        cardslot_5.location = 4+mark*6;
        cardslot_6.location = 5+mark*6;
    }

    @Override
    protected void initViews() {
        handleChangeData();
    }

    public void firstShow() {
        list = ManagerCardInfo.getInstance().useCardList(mark);
        switch (mark) {
            case 0:
                cardType.setText(getResources().getString(R.string.primary_card));
                break;
            case 1:
                cardType.setText(getResources().getString(R.string.intermediate_card));
                break;
            case 2:
                cardType.setText(getResources().getString(R.string.advanced_card));
                break;
            case 3:
                cardType.setText(getResources().getString(R.string.suxury_card));
                break;
            case 4:
                cardType.setText(getResources().getString(R.string.extravagant_card));
                break;
        }
        if (list == null || list.size() == 0) {
            return;
        }
        cardslot_1.setData(list.get(0));
        cardslot_2.setData(list.get(1));
        cardslot_3.setData(list.get(2));
        cardslot_4.setData(list.get(3));
        cardslot_5.setData(list.get(4));
        cardslot_6.setData(list.get(5));
    }

    @Override
    protected void initEvents() {
        guide.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.POP_NAVIGATION);
            }
        });
        button_layout.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OCtrlCard.getInstance().ccmd1409_synthetic_card(mark+1);
            }
        });
        touch_exit_1.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.HIDE_BG_GREEN,-1);
            }
        });
    }

    @Override
    protected void invalidateUI() {
        firstShow();
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.GET_CARDINFO_LIST_RESULTBACK)) {
            handleChangeData();
        }else if (s.equals(OEventName.SYSTHETIC_CARD)) {
            if (mark != 6)
                OCtrlCard.getInstance().ccmd1409_synthetic_card(mark);
        }else if(s.equals(OEventName.HIDE_BG_GREEN)){
            int state=(Integer) o;
            if(state==-1){
                handleTransparentWindowDismiss();
            }else{
                handleTransparentWindowShow();
            }
        }
    }
    private void handleTransparentWindowShow(){
        Message message=Message.obtain();
        message.what=120;
        handler.sendMessage(message);
    }
    private void handleTransparentWindowDismiss(){
        Message message=Message.obtain();
        message.what=121;
        handler.sendMessage(message);
    }
    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.GET_CARDINFO_LIST_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.HTTP_CONN_ERROR, this);
        ODispatcher.removeEventListener(OEventName.HIDE_BG_GREEN, this);
        super.onDetachedFromWindow();
    }
    @Override
    protected void onAttachedToWindow() {
        ODispatcher.addEventListener(OEventName.GET_CARDINFO_LIST_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.HIDE_BG_GREEN, this);
        ODispatcher.addEventListener(OEventName.HTTP_CONN_ERROR, this);
        super.onAttachedToWindow();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
             if(msg.what==120){
                touch_exit_1.setVisibility(View.VISIBLE);
            }else if(msg.what==121){
                touch_exit_1.setVisibility(View.INVISIBLE);
            }
        }
    };
}
