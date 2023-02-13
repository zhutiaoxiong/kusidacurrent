package view.view4app.carpath;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.listview.SwipeListView;
import com.kulala.staticsview.OnClickListenerMy;


import java.util.List;

import common.GlobalContext;
import common.global.NAVI;
import common.map.DataPos;
import common.map.MapPosGet;
import ctrl.OCtrlGps;
import model.ManagerNavi;
import model.navigation.DataNavigation;

import static com.kulala.dispatcher.param.ODispatcher.dispatchEvent;

/**
 * Created by qq522414074 on 2017/3/29.
 */

public class ViewNaviMain extends RelativeLayoutBase  {
    private ImageView img_left,find,set,img_adress_history;
    private TextView edit;
    private TextView txt_gohome,txt_home,txt_gocompany,txt_company;
    private LinearLayout li_gas,li_carpark,li_collect;
    private RelativeLayout head_view;
    private SwipeListView listview_adress;
    private AdapterForAdress adapterForAdress;
    public static  DataPos currentPos;
    private DataNavigation data;
    private List<DataPos> list;
    public ViewNaviMain(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_navi_main, this, true);
        img_left= (ImageView) findViewById(R.id.img_left);
        find= (ImageView) findViewById(R.id.find);
        set= (ImageView) findViewById(R.id.set);
        img_adress_history= (ImageView) findViewById(R.id.img_adress_history);
        edit= (TextView) findViewById(R.id.edit);
        txt_gohome= (TextView) findViewById(R.id.txt_gohome);
        txt_home= (TextView) findViewById(R.id.txt_home);
        txt_company= (TextView) findViewById(R.id.txt_company);
        txt_gocompany= (TextView) findViewById(R.id.txt_gocompany);
        li_gas= (LinearLayout) findViewById(R.id.li_gas);
        li_collect= (LinearLayout) findViewById(R.id.li_collect);
        li_carpark= (LinearLayout) findViewById(R.id.li_carpark);
        head_view= (RelativeLayout) findViewById(R.id.head_view);
        listview_adress= (SwipeListView) findViewById(R.id.listview_adress);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_NAVI_INFO_BACK,this);
        OCtrlGps.getInstance().ccmd1247_getNavigationInfomation();

    }

    @Override
    protected void initViews() {
        listview_adress.setRightViewWidth(ODipToPx.dipToPx(getContext(),80f));
       list=DataPos.loadSearchHistory();
        if(list==null|| list.size()==0){
            head_view.setVisibility(View.GONE);
        }else{
            head_view.setVisibility(View.VISIBLE);
            listview_adress.setVisibility(View.VISIBLE);
        }
//        if(!LoadPermissions.isOpenGps(getContext())){
//            LoadPermissions.openGPS(getContext());
//        }
        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
            @Override
            public void onCurrentPosGet(DataPos posData) {
                currentPos=posData;
            }
        });
        adapterForAdress=new AdapterForAdress(getContext(), ODipToPx.dipToPx(getContext(),80f),list, new AdapterForAdress.OnClickItemListener() {
            @Override
            public void onClicDelete(DataPos data) {
                if(data==null)return;
                list.remove(data);
                DataPos.deleteOneSearchHistory(data);
                adapterForAdress.notifyDataSetChanged();
            }

            @Override
            public void onClickNavi(DataPos data) {
//                ViewMapWatchPos.usePos=data;
//                ViewMapWatchPos.PRE_VIEW_ID=R.layout.view_navi_main;
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_watch_pos);
                openNavi(currentPos,data);

            }
        });
        listview_adress.setAdapter(adapterForAdress);
        MapPosGet.searchCurrentPos(null);
        handleChangeData();


    }

    @Override
    protected void initEvents() {
        //点击返回上一个页面
        img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,0);
            }
        });
        //输入信息改变
        edit.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ViewNaviSearch.TYPE = ViewNaviSearch.TYPE_GOTO_NAVI;
                ViewNaviSearch.PRE_VIEW_ID = R.layout.view_navi_main;
                dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_search);
            }
        });
        //设置按钮设置家和公司
        set.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_set);
            }
        });
        //点击这个删除所有的地址纪录
        img_adress_history.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withInfo("清空全部搜索历史纪录？")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if(isClickConfirm){
                                    list.clear();
                                    DataPos.deleteSearchHistoryAll();
                                    adapterForAdress.notifyDataSetChanged();
                                    head_view.setVisibility(View.GONE);
                                    listview_adress.setVisibility(View.INVISIBLE);
                                }
                            }
                        })
                        .show();

            }
        });
        //点击进导航选家的地址完了设置值
        txt_home.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataNavigation data = ManagerNavi.getInstance().naviInfo;
                if(data!=null && data.homeLatitude!=null && data.homeLatitude.length()>0){//有坐标,去导航
                    DataPos end = new DataPos(NAVI.Str2Latlng(data.homeLatitude),data.home);
                    openNavi(currentPos,end);
//                    ViewMapWatchPos.usePos=end;
//                    ViewMapWatchPos.PRE_VIEW_ID=R.layout.view_navi_main;
//                     ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_watch_pos);
                }else{//无坐标，去设坐标
                    ViewHomePosChoose.IS_CHOOSE_HOME_POS = true;
                    dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_choose);
                }
            }
        });
        //same up
        txt_gohome.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataNavigation data = ManagerNavi.getInstance().naviInfo;
                if(data!=null && data.homeLatitude!=null && data.homeLatitude.length()>0){//有坐标,去导航
                    DataPos end = new DataPos(NAVI.Str2Latlng(data.homeLatitude),data.home);
                    openNavi(currentPos,end);
//                    ViewMapWatchPos.usePos=end;
//                    ViewMapWatchPos.PRE_VIEW_ID=R.layout.view_navi_main;
//
//                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_watch_pos);
                }else{//无坐标，去设坐标
                    ViewHomePosChoose.IS_CHOOSE_HOME_POS = true;
                    dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_choose);
                }
            }
        });
        //点击进导航选公司的地址完了设置值
        txt_company.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataNavigation data = ManagerNavi.getInstance().naviInfo;
                if(data!=null && data.companyLatitude!=null && data.companyLatitude.length()>0){//有坐标,去导航
                    DataPos end = new DataPos(NAVI.Str2Latlng(data.companyLatitude),data.company);
                    openNavi(currentPos,end);
//                    ViewMapWatchPos.usePos=end;
//                    ViewMapWatchPos.PRE_VIEW_ID=R.layout.view_navi_main;
//                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_watch_pos);
                }else{//无坐标，去设坐标
                    ViewHomePosChoose.IS_CHOOSE_HOME_POS = false;
                    dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_choose);
                }
            }
        });
        //same up
        txt_gocompany.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataNavigation data = ManagerNavi.getInstance().naviInfo;
                if(data!=null && data.companyLatitude!=null && data.companyLatitude.length()>0){//有坐标,去导航
                    DataPos end = new DataPos(NAVI.Str2Latlng(data.companyLatitude),data.company);
                    openNavi(currentPos,end);
//                    ViewMapWatchPos.usePos=end;
//                    ViewMapWatchPos.PRE_VIEW_ID=R.layout.view_navi_main;
//                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_watch_pos);
                }else{//无坐标，去设坐标
                    ViewHomePosChoose.IS_CHOOSE_HOME_POS = false;
                    dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_choose);
                }
            }
        });
        //点击选加油站
        li_gas.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ViewMapSearchNear.PRE_SEARCH_STR = "加油站";
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewMapSearchNear.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        //点击选停车场
        li_carpark.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ViewMapSearchNear.PRE_SEARCH_STR = "停车场";
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewMapSearchNear.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        //点击选收藏点
        li_collect.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_car_pos_collect);
            }
        });
    }
    @Override
    public void receiveEvent(String key, Object value) {
        if(key.equals(OEventName.GPS_NAVI_INFO_BACK)){
            handleChangeData();
        }
        super.receiveEvent(key, value);
    }
    @Override
    protected void invalidateUI() {
        DataNavigation data = ManagerNavi.getInstance().naviInfo;
        if(data!=null && data.companyLatitude!=null && data.companyLatitude.length()>0){//有坐标,去导航
            txt_company.setText(data.company);
        }else{//无坐标
            txt_company.setText("点击设置");
        }
        if(data!=null && data.homeLatitude!=null && data.homeLatitude.length()>0){//有坐标,去导航
            txt_home.setText(data.home);
        }else{//无坐标
            txt_home.setText("点击设置");
        }

    }
    private void openNavi(DataPos start,DataPos end){
        if(start == null || start.pos == null){
            dispatchEvent(OEventName.GLOBAL_POP_TOAST,"无法获取自已定位");
            return;
        }
//        BNRoutePlanNode startNode = new BNRoutePlanNode(start.pos.longitude, start.pos.latitude, start.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode endNode   = new BNRoutePlanNode(end.pos.longitude, end.pos.latitude, end.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        Intent intent    = new Intent(getContext(), ActivityNaviMap.class);
//        Bundle bundle    = new Bundle();
//        bundle.putSerializable(ActivityNaviMap.START_NODE,startNode);
//        bundle.putSerializable(ActivityNaviMap.END_NODE,endNode);
//        intent.putExtras(bundle);
//        getContext().startActivity(intent);
    }
}
