package view.view4info.card;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;

import common.global.OWXShare;
import model.ManagerCommon;
import model.ManagerLoginReg;
import model.find.CardDetails;

/**
 * Created by qq522414074 on 2016/10/23.
 */
public class ClippopShareCard  {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private RelativeLayout thisView;
    private Context context;
    private ImageView icon_share_wx_friend,icon_share_wx;
    private Button cancle;
    private CardDetails details;
    // ========================out======================
    private static ClippopShareCard _instance;
    public static ClippopShareCard getInstance() {
        if (_instance == null)
            _instance = new ClippopShareCard();
        return _instance;
    }
    //===================================================
    public void show(View parentView, CardDetails details) {
        this.parentView = parentView;
        this.details=details;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_card_share, null);
        icon_share_wx = (ImageView) thisView.findViewById(R.id.icon_share_wx);
        icon_share_wx_friend = (ImageView) thisView.findViewById(R.id.icon_share_wx_friend);
        cancle=(Button) thisView.findViewById(R.id.cancle);
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    exitThis();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    public void exitThis() {
        thisView=null;
        popContain.dismiss();
    }

    public void initEvents() {
        cancle.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                exitThis();
            }
        });
        icon_share_wx.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                long userId = ManagerLoginReg.getInstance().getCurrentUser() == null ? 0 : ManagerLoginReg.getInstance().getCurrentUser().userId;
                String url=ManagerCommon.cardShareUrl+"cardId="+details.card.ide+"&userId="+ userId;
                String title=details.card.typeStr;
                    OWXShare.ShareFriendURL(context.getResources().getString(R.string.collection_of_card_game_aa)+title,context.getResources().getString(R.string.I_am_is_with_a_cool_smart_car_control_system_for_a_card)+title+context.getResources().getString(R.string.everybody_cyclists_to_pk),url);
               exitThis();
            }
        });
        icon_share_wx_friend.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                long userId = ManagerLoginReg.getInstance().getCurrentUser() == null ? 0 : ManagerLoginReg.getInstance().getCurrentUser().userId;
                String url=ManagerCommon.cardShareUrl+"cardId="+details.card.ide+"&userId="+ userId;
                String title=details.card.typeStr;
                OWXShare.ShareTimeLineURL(context.getResources().getString(R.string.collection_of_card_game_aa)+title,context.getResources().getString(R.string.I_am_is_with_a_cool_smart_car_control_system_for_a_card)+title+context.getResources().getString(R.string.everybody_cyclists_to_pk),url);
               exitThis();
            }
        });
    }
}
