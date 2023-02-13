package view.view4app.carpath;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.listview.SwipeListView;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.input.ViewAboveInput;

import java.util.List;

import adapter.AdapterForFavouriteList;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.ManagerGps;
import model.gps.DataGpsPoint;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2017/3/30.
 */

public class ViewCarPosCollect extends LinearLayoutBase {
    private SwipeListView listview_location;
    private ClipTitleMeSet title_head;
    private List<DataGpsPoint> listFavorite;
    private AdapterForFavouriteList adapterForFavouriteList;
    private RelativeLayout re_nolayout;
    private RelativeLayout lin_pop_input;
    private String inputStr;//输入的备注信息
    private ViewAboveInput inputAbove;
    public ViewCarPosCollect(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_car_pos_collect, this, true);
        listview_location = (SwipeListView) findViewById(R.id.listview_location);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        lin_pop_input = (RelativeLayout) findViewById(R.id.lin_pop_input);
        re_nolayout = (RelativeLayout) findViewById(R.id.re_nolayout);
        initViews();
        initEvents();
        OCtrlGps.getInstance().ccmd1218_favoriteList();
        AdapterForFavouriteList.whereUse=1;
        ODispatcher.addEventListener(OEventName.GPS_FAVORITE_LISTCHANGE,this);
        ODispatcher.addEventListener(OEventName.GPS_PATHLIST_RESULTBACK,this);
    }

    @Override
    protected void initViews() {
        listview_location.setRightViewWidth(ODipToPx.dipToPx(getContext(),80));
//        handleChangeData();
    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_main);
            }
        });
        listview_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewMapWatchPos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.GPS_FAVORITE_LISTCHANGE)){
            handleChangeData();
        }else if (eventName.equals(OEventName.GPS_PATHLIST_RESULTBACK)) {
           int result= (Integer)paramObj;
         if(result == 1243){
             adapterForFavouriteList.addComment(inputStr);
        }
        super.receiveEvent(eventName, paramObj);
    }
    }
    @Override
    protected void invalidateUI() {
        listFavorite = ManagerGps.getInstance().favoriteList;
        if(listFavorite!=null&&listFavorite.size()!=0){
            re_nolayout.setVisibility(View.INVISIBLE);
            if(adapterForFavouriteList == null) {
                adapterForFavouriteList = new AdapterForFavouriteList(listFavorite, getContext(), ODipToPx.dipToPx(getContext(),80f));
                listview_location.setAdapter(adapterForFavouriteList);
            }else{
                adapterForFavouriteList.notifyDataSetInvalidated();
            }
            if (adapterForFavouriteList != null) {
                adapterForFavouriteList.setonClickBianJi(new AdapterForFavouriteList.onClickBianJi() {

                    @Override
                    public void click(final DataGpsPoint info) {
                        if (lin_pop_input.getChildCount() > 0) return;//已加入过
//                    TurnOffKeyBoard.openKeyBoard(GlobalContext.getCurrentActivity());
                        if (inputAbove == null)inputAbove = new ViewAboveInput(getContext(), null);
                        inputAbove.show("请输入备注内容", "", 20);
                        lin_pop_input.addView(inputAbove);
                        inputAbove.SetOnClickConfirmListener(new ViewAboveInput.OnClickConfirmListener() {
                            @Override
                            public void onClickConfirm(String InputTxt) {
                                inputStr=InputTxt;
                                if(ManagerCarList.getInstance().getCurrentCar().ide>=0){
                                    OCtrlGps.getInstance().ccmd1243_addComment(ManagerCarList.getInstance().getCurrentCar().ide,info.ide,InputTxt,0,0,0,20);
                                }
                                lin_pop_input.removeAllViews();
                            }
                        });
                    }
                });
            }

        }else{
            re_nolayout.setVisibility(View.VISIBLE);
        }
    }
}
