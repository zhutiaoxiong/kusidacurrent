package view.view4info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;

import view.clip.ClipLineBtnTxt;
import com.kulala.staticsview.titlehead.ClipTitleHead;

/**
 * Created by qq522414074 on 2016/9/2.发现主页面
 */
public class ViewFind extends LinearLayoutBase {
    private ClipLineBtnTxt dressup;
    private ClipLineBtnTxt friend;
    private ClipLineBtnTxt news;
    private ClipLineBtnTxt cardsystem,view_find_score;
    private ClipTitleHead titleHead;

    public ViewFind(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_find,this,true);
        dressup=(ClipLineBtnTxt) findViewById(R.id.view_find_dressup);
//        friend=(ClipLineBtnTxt)findViewById(R.id.view_find_friend);
//        news=(ClipLineBtnTxt)findViewById(R.id.view_find_news);
//        cardsystem=(ClipLineBtnTxt)findViewById(R.id.view_find_cardsystem);
//        view_find_score=(ClipLineBtnTxt)findViewById(R.id.view_find_score);
        titleHead=(ClipTitleHead)findViewById(R.id.title_head);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.LANGUAGE_CHANGE,this);
    }

    @Override
    protected void initViews() {
        handleChangeData();
    }

    @Override
    protected void initEvents() {
//        friend.setOnClickListener(new OnClickListenerMy(){
//            @Override
//            public void onClickNoFast(View view) {
//
//            }
//        });
//        news.setOnClickListener(new OnClickListenerMy(){
//            @Override
//            public void onClickNoFast(View view) {
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_info_infolist);
//            }
//        });
//        dressup.setOnClickListener(new OnClickListenerMy(){
//            @Override
//            public void onClickNoFast(View view) {
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_find_car_dressup);
//            }
//        });
//        cardsystem.setOnClickListener(new OnClickListenerMy(){
//            @Override
//            public void onClickNoFast(View view) {
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_collection_achievement);
//            }
//        });
//        view_find_score.setOnClickListener(new OnClickListenerMy(){
//            @Override
//            public void onClickNoFast(View view) {
//                Intent intent = new Intent();
//                intent.setClass(getContext(), ViewUserInfoActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ViewUserInfoActivity.PRE_NEED_POPVIEW_ID = R.layout.view_find;
//                getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.LANGUAGE_CHANGE)){
            handleChangeData();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.LANGUAGE_CHANGE,this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void invalidateUI() {
//        titleHead.setTitle(getResources().getString(R.string.flag_info));
//        dressup.setText(getResources().getString(R.string.personality_is_dressed_up));
//        news.setText(getResources().getString(R.string.information));
//        cardsystem.setText(getResources().getString(R.string.collection_of_card_game));
    }
}
