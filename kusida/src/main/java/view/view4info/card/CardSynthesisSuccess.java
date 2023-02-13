package view.view4info.card;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import model.find.CardInfo;
import model.find.ManagerCardInfo;

/**
 * Created by qq522414074 on 2016/10/23.
 */
public class CardSynthesisSuccess {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private RelativeLayout thisView;
    private Context context;
    private SmartImageView pic;
    private Button iknow,recive_iknow,look_card;
    private TextView txt_title,recive_who;
    public static CardInfo recivieCardInfo;
    public static  String recicieUserName;
    // ========================out======================
    private static CardSynthesisSuccess _instance;
    public static CardSynthesisSuccess getInstance() {
        if (_instance == null)
            _instance = new CardSynthesisSuccess();
        return _instance;
    }
    //===================================================

    /**
     *
     * @param parentView
     * @param picUrl   图片地址
     * @param txtTittle  标题
     * @param isShow    设置当收到卡片的提示框的知道了和查看按钮是否显示
     * @param who       收到谁的卡片
     */
    public void show(View parentView, String picUrl,String txtTittle,boolean isShow,String who) {
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.card_synthesis_success, null);
        pic = (SmartImageView) thisView.findViewById(R.id.pic);
        iknow = (Button) thisView.findViewById(R.id.iknow);
        recive_iknow=(Button) thisView.findViewById(R.id.recive_iknow);
        look_card=(Button) thisView.findViewById(R.id.look_card);
        recive_who=(TextView)  thisView.findViewById(R.id.recive_who);
        txt_title=(TextView)thisView.findViewById(R.id.txt_title);
        pic.setImageUrl(picUrl);
        txt_title.setText(txtTittle);
        iknow.setVisibility(View.INVISIBLE);
        recive_iknow.setVisibility(View.INVISIBLE);
        look_card.setVisibility(View.INVISIBLE);
        recive_who.setVisibility(View.INVISIBLE);
        if(isShow){
            iknow.setVisibility(View.VISIBLE);
        }else{
            recive_iknow.setVisibility(View.VISIBLE);
            look_card.setVisibility(View.VISIBLE);
            recive_who.setVisibility(View.VISIBLE);
            recive_who.setText(who);
        }
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
        popContain.showAtLocation(parentView, Gravity.TOP, 0, 0);
    }


    public void exitThis() {
        thisView=null;
        popContain.dismiss();
    }

    public void initEvents() {
        iknow.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                exitThis();
                ODispatcher.dispatchEvent(OEventName.TO_NEWCARDPOSITION, ManagerCardInfo.syntheticCard.type);
            }
        });
        recive_iknow.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                exitThis();
            }
        });
        look_card.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                exitThis();
                //查看卡片跳转到指定页面
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_collection_achievement);
            }
        });
    }
}
