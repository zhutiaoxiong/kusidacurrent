package view.view4info.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.tencent.tauth.TencentCommon;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import adapter.MyPagerAdapter;
import common.global.OWXShare;
import common.pinyinzhuanhuan.CircleImg;
import ctrl.OCtrlCard;
import model.ManagerCommon;
import model.ManagerLoginReg;
import model.find.CardDetails;
import model.find.ManagerCardInfo;
import model.loginreg.DataUser;
import com.kulala.staticsview.titlehead.ClipTitleHead;
import view.view4app.carpath.OToastSharePath;

/**
 * Created by qq522414074 on 2016/9/12.
 */
public class ViewCollectAchievement extends LinearLayoutBase {
    private ClipTitleHead titleHead;
    private CircleImg img_face;
    private TextView txt_nickname, txt_phone, card_rule_content;
    private ImageView a1, a2, a3, a4, a5, a6, cancleReadRule, the_canchange_badge;
    private RelativeLayout card_indicator;
    private ViewPager viewpager;
    private List<LinearLayoutBase> list;
    private MyPagerAdapter adapter;
    private int firstShow = 0;
    private Button touch_exit;
    private ImageView no_touch;
    private  CardDetails details;

    private MyHandler handler = new MyHandler();

    public ViewCollectAchievement(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_collection_achievement, this, true);
        titleHead = (ClipTitleHead) findViewById(R.id.title_head);
        img_face = (CircleImg) findViewById(R.id.img_face);
        txt_nickname = (TextView) findViewById(R.id.txt_nickname);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        a1 = (ImageView) findViewById(R.id.a1);
        a2 = (ImageView) findViewById(R.id.a2);
        a3 = (ImageView) findViewById(R.id.a3);
        a4 = (ImageView) findViewById(R.id.a4);
        a5 = (ImageView) findViewById(R.id.a5);
        a6 = (ImageView) findViewById(R.id.a6);
        the_canchange_badge = (ImageView) findViewById(R.id.the_canchange_badge);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        card_indicator = (RelativeLayout) findViewById(R.id.card_indicator_layout);
        cancleReadRule = (ImageView) findViewById(R.id.cancle_read_rule);
        card_rule_content = (TextView) findViewById(R.id.card_rule_content);
        touch_exit = (Button) findViewById(R.id.touch_1);
        no_touch = (ImageView) findViewById(R.id.no_touch);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.POP_NAVIGATION, this);
        ODispatcher.addEventListener(OEventName.SHOW_GIVE_CARD_PROMEBOX, this);
        ODispatcher.addEventListener(OEventName.CARD_GIVE_RESULT, this);
        ODispatcher.addEventListener(OEventName.CARD_SYNTHETIC_RESULT, this);
        ODispatcher.addEventListener(OEventName.HTTP_CONN_ERROR, this);//网络不良
        ODispatcher.addEventListener(OEventName.TO_NEWCARDPOSITION, this);
        ODispatcher.addEventListener(OEventName.SHOW_GIVE_SHARE_PROMEBOX, this);
        ODispatcher.addEventListener(OEventName.HIDE_BG_GREEN, this);


    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    @Override
    protected void initViews() {
        initViewPageritem();
        handleChangeData();
        card_rule_content.setMovementMethod(new ScrollingMovementMethod());
        card_rule_content.setText(ToDBC(card_rule_content.getText().toString()));
        ManagerCardInfo.getInstance().loadCardListLocal();
        OCtrlCard.getInstance().ccmd1405_get_cardarray();
    }

    @Override
    protected void initEvents() {

        titleHead.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        no_touch.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {

            }
        });
        touch_exit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.HIDE_BG_GREEN, -1);
            }
        });
        cancleReadRule.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                card_indicator.setVisibility(View.GONE);
                no_touch.setVisibility(View.GONE);
            }
        });
        a1.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                viewpager.setCurrentItem(0);
                a1.setImageResource(R.drawable.b1);
            }
        });
        a2.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                viewpager.setCurrentItem(1);
                a2.setImageResource(R.drawable.b2);
            }
        });
        a3.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                viewpager.setCurrentItem(2);
                a3.setImageResource(R.drawable.b3);
            }
        });
        a4.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                viewpager.setCurrentItem(3);
                a4.setImageResource(R.drawable.b4);
            }
        });
        a5.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                viewpager.setCurrentItem(4);
                a5.setImageResource(R.drawable.b5);
            }
        });
        a6.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                viewpager.setCurrentItem(5);
                a6.setImageResource(R.drawable.b6);
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                a1.setImageResource(R.drawable.a1);
                a2.setImageResource(R.drawable.a2);
                a3.setImageResource(R.drawable.a3);
                a4.setImageResource(R.drawable.a4);
                a5.setImageResource(R.drawable.a5);
                a6.setImageResource(R.drawable.a6);
                switch (i) {
                    case 0:
                        a1.setImageResource(R.drawable.b1);
                        break;
                    case 1:
                        a2.setImageResource(R.drawable.b2);
                        break;
                    case 2:
                        a3.setImageResource(R.drawable.b3);
                        break;
                    case 3:
                        a4.setImageResource(R.drawable.b4);
                        break;
                    case 4:
                        a5.setImageResource(R.drawable.b5);
                        break;
                    case 5:
                        a6.setImageResource(R.drawable.b6);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if(i==2){
                    ODispatcher.dispatchEvent(OEventName.HIDE_BG_GREEN, -1);
                }
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object o) {
        if (eventName.equals(OEventName.POP_NAVIGATION)) {
            firstShow = 2;
            handleChangeData();
        } else if (eventName.equals(OEventName.SHOW_GIVE_CARD_PROMEBOX)) {
            CardDetails details = (CardDetails) o;
            handleShowCardGivePromebox(details.card.ide);
        } else if (eventName.equals(OEventName.CARD_GIVE_RESULT)) {
//            int result = (Integer)o;
            String result = (String) o;
            handleShowGiveCardResult(result, true);
        } else if (eventName.equals(OEventName.CARD_SYNTHETIC_RESULT)) {
//            int result = (Integer)o;
            String result = (String) o;
            handleShowGiveCardResult(result, false);
        } else if (eventName.equals(OEventName.HTTP_CONN_ERROR)) {
            int result = (Integer) o;
            if (result == 1408 || result == 1409)
                handleShowGiveCardResult("马蓉婬妇", true);
        } else if (eventName.equals(OEventName.TO_NEWCARDPOSITION)) {
            int type = (int) o;
            handleToNewCardLocation(type);
        } else if (eventName.equals(OEventName.SHOW_GIVE_SHARE_PROMEBOX)) {
            handleShowCardSharePromebox(o);
        } else if (eventName.equals(OEventName.HIDE_BG_GREEN)) {
            int state = (Integer) o;
            if (state == -1) {
                handleTransparentWindowDismiss();
            } else {
                handleTransparentWindowShow();
            }
        }
    }

    private void handleTransparentWindowShow() {
        Message message = Message.obtain();
        message.what = 120;
        handler1.sendMessage(message);
    }

    private void handleTransparentWindowDismiss() {
        Message message = Message.obtain();
        message.what = 121;
        handler1.sendMessage(message);
    }

    /**
     * 定位到合成成功卡片的那一栏
     */
    private void handleToNewCardLocation(int type) {
        Message message = Message.obtain();
        message.what = 119;
        message.arg1 = type;
        handler1.sendMessage(message);
    }

    /**
     * 弹出合成赠送卡片结果
     */
//    private void handleShowGiveCardResult(int result,boolean isGive){
//        Message message=Message.obtain();
//        message.what=111;
//        message.arg1 = result;
//        if(isGive){
//            message.arg2 = 1;
//        }else{
//            message.arg2 = 0;
//        }
//        handler1.sendMessage(message);
//    }
    private void handleShowGiveCardResult(String result, boolean isGive) {
        Message message = Message.obtain();
        message.what = 111;
//        message.arg1 = result;
        message.obj = result;
        if (isGive) {
            message.arg2 = 1;
        } else {
            message.arg2 = 0;
        }
        handler1.sendMessage(message);
    }

    /**
     * 弹出卡片赠予的对话框
     *
     * @param ide
     */
    private void handleShowCardGivePromebox(int ide) {
        Message message = Message.obtain();
        message.what = 116;
        message.arg1 = ide;
        handler1.sendMessage(message);
    }

    private void handleShowCardSharePromebox(Object o) {
        Message message = Message.obtain();
        message.what = 130;
        message.obj = o;
        handler1.sendMessage(message);
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.POP_NAVIGATION, this);
        ODispatcher.removeEventListener(OEventName.GET_CARDINFO_LIST_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.SHOW_GIVE_CARD_PROMEBOX, this);
        ODispatcher.removeEventListener(OEventName.CARD_SYNTHETIC_RESULT, this);
        ODispatcher.removeEventListener(OEventName.CARD_GIVE_RESULT, this);
        ODispatcher.removeEventListener(OEventName.TO_NEWCARDPOSITION, this);
        ODispatcher.removeEventListener(OEventName.SHOW_GIVE_SHARE_PROMEBOX, this);
        ODispatcher.removeEventListener(OEventName.HIDE_BG_GREEN, this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void invalidateUI() {
        if (firstShow == 0) {
            final DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            if (user == null) return;
            if (user.name.equals("")) {
                txt_nickname.setText(getResources().getString(R.string.nickname));
                txt_nickname.setTextColor(getResources().getColor(R.color.gray_text));
            } else {
                txt_nickname.setText(user.name);
                txt_nickname.setTextColor(getResources().getColor(R.color.white));
            }
//        img_face.setImageResource(DataUser.getUserHeadResId(user.avatarUrl));

            txt_phone.setText(user.phoneNum);
            if (user.avatarUrl.length() <= 1 || user.avatarUrl == null) {
                img_face.setImageResource(R.drawable.head_no);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = DataUser.getBitmap(user.avatarUrl);
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = bitmap;
                            handler1.sendMessage(message);
                        } catch (UnknownHostException e) {
                            handleHeadNo();
                        } catch (IOException e) {
                            handleHeadNo();
                        }
                    }
                }).start();
            }
        } else if (firstShow == 2) {
            card_indicator.setVisibility(View.VISIBLE);
            no_touch.setVisibility(View.VISIBLE);
        }
        initViewPageritem();
    }

    private void initViewPageritem() {
        if (adapter == null) {
            list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                ItemViewForViewPager item = new ItemViewForViewPager(ViewCollectAchievement.this.getContext(), null, i);
                list.add(item);
            }
            list.add(new LastItemViewForViewPager(ViewCollectAchievement.this.getContext(), null, 5));
            adapter = new MyPagerAdapter(list);
            viewpager.setAdapter(adapter);
            viewpager.setCurrentItem(0);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("HandlerLeak")
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 111) {
//                    int result = msg.arg1;
                    String result = (String) msg.obj;
                    int isGive = msg.arg2;
//                    if(result == 1){
                    if (result.equals("")) {
                        if (isGive == 1) {
                            ClipPopCardGive.getInstance().show(titleHead, R.drawable.givesuccess, getResources().getString(R.string.presented_a_successful));
                        } else {
                            if (ManagerCardInfo.syntheticCard == null) return;
                            CardSynthesisSuccess.getInstance().show(titleHead, ManagerCardInfo.syntheticCard.pic, getResources().getString(R.string.synthesis_of_success), true, "");
                        }
                    }
//                    else if(result == 0){
                    else if (!result.equals("")) {
                        if (!result.equals("马蓉婬妇")) {
                            if (isGive == 1) {
//                            ClipPopCardGive.getInstance().show(titleHead, R.drawable.givefailed, "赠送失败");
                                ClipPopCardGive.getInstance().show(titleHead, R.drawable.givefailed, result);
                            } else
//                            ClipPopCardGive.getInstance().show(titleHead,R.drawable.givefailed,"合成失败，卡片不足");
                                ClipPopCardGive.getInstance().show(titleHead, R.drawable.givefailed, result);
                        } else {
                            ClipPopCardGive.getInstance().show(titleHead, R.drawable.netexception, getResources().getString(R.string.network_bad));
                        }
                    }
                } else if (msg.what == 1) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    img_face.setImageBitmap(bitmap);
                } else if (msg.what == 116) {
                    ClipPopGiveCardPromBox.getInstance().showInput(titleHead, msg.arg1, ViewCollectAchievement.this);
                } else if (msg.what == 119) {
                    viewpager.setCurrentItem(msg.arg1 - 1);
                } else if (msg.what == 120) {
                    touch_exit.setVisibility(View.VISIBLE);
                } else if (msg.what == 121) {
                    touch_exit.setVisibility(View.INVISIBLE);
                } else if (msg.what == 130) {
                   details= (CardDetails) msg.obj;
                    if (details == null) return;

//                    ClippopShareCard.getInstance().show(titleHead, details);
                    OToastSharePath.getInstance().show(titleHead, "sharepath007", new OToastSharePath.OnClickButtonListener() {
                        @Override
                        public void onClick(int pos) {
                            long userId = ManagerLoginReg.getInstance().getCurrentUser() == null ? 0 : ManagerLoginReg.getInstance().getCurrentUser().userId;
                            switch(pos){
                                case 1:
                                    String url= ManagerCommon.cardShareUrl+"cardId="+details.card.ide+"&userId="+ userId;
                                    String title=details.card.typeStr;
                                    OWXShare.ShareFriendURL(getContext().getResources().getString(R.string.collection_of_card_game_aa)+title,getContext().getResources().getString(R.string.I_am_is_with_a_cool_smart_car_control_system_for_a_card)+title+getContext().getResources().getString(R.string.everybody_cyclists_to_pk),url);
                                    break;
                                case 2:
                                    String url1=ManagerCommon.cardShareUrl+"cardId="+details.card.ide+"&userId="+ userId;
                                    String title1=details.card.typeStr;
                                    OWXShare.ShareTimeLineURL(getContext().getResources().getString(R.string.collection_of_card_game_aa)+title1,getContext().getResources().getString(R.string.I_am_is_with_a_cool_smart_car_control_system_for_a_card)+title1+getContext().getResources().getString(R.string.everybody_cyclists_to_pk),url1);
                                    break;
                                case 3:
                                    String url13=ManagerCommon.cardShareUrl+"cardId="+details.card.ide+"&userId="+ userId;
                                    String title13=details.card.typeStr;
//                    OWXShare.ShareTimeLineURL(getContext().getResources().getString(R.string.collection_of_card_game_aa)+title13,getContext().getResources().getString(R.string.I_am_is_with_a_cool_smart_car_control_system_for_a_card)+title13+getContext().getResources().getString(R.string.everybody_cyclists_to_pk),url13);
                                    TencentCommon.toTencent(getContext(),getContext().getResources().getString(R.string.collection_of_card_game_aa)+title13,getContext().getResources().getString(R.string.I_am_is_with_a_cool_smart_car_control_system_for_a_card)+title13+getContext().getResources().getString(R.string.everybody_cyclists_to_pk),url13,0,"");
                                    break;
                                case 4:
                                    String url14=ManagerCommon.cardShareUrl+"cardId="+details.card.ide+"&userId="+ userId;
                                    String title14=details.card.typeStr;
//                    OWXShare.ShareTimeLineURL(getContext().getResources().getString(R.string.collection_of_card_game_aa)+title14,getContext().getResources().getString(R.string.I_am_is_with_a_cool_smart_car_control_system_for_a_card)+title14+getContext().getResources().getString(R.string.everybody_cyclists_to_pk),url14);
                                    TencentCommon.toTencent(getContext(),getContext().getResources().getString(R.string.collection_of_card_game_aa)+title14,getContext().getResources().getString(R.string.I_am_is_with_a_cool_smart_car_control_system_for_a_card)+title14+getContext().getResources().getString(R.string.everybody_cyclists_to_pk),url14,1,"");
                                    break;
                            }
                        }
                    });
                }
            }
        }
    };

    @Override
    public void callback(String key, Object value) {
        if(key.equals("sharepath007")){

        }
    }

    public void handleHeadNo() {
        Message message = new Message();
        message.what = 504;
        handler.sendMessage(message);
    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 504:
                    img_face.setImageResource(R.drawable.head_no);
                    break;
            }
        }
    }
    // ===================================================
}
