package view.view4app.carpath;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import common.global.NAVI;
import common.map.DataPos;
import common.map.MapPosGet;
import ctrl.OCtrlGps;
import model.ManagerNavi;
import model.navigation.DataNavigation;
import view.view4me.set.ClipTitleMeSet;

import static com.kulala.dispatcher.param.ODispatcher.dispatchEvent;



/**
 * 选家和公司
 */

public class ViewHomePosSet extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private TextView txt_company,txt_gocompany,txt_home,txt_gohome;
    private ImageView delete1,delete2;
    private DataPos myLocation;
    public ViewHomePosSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_navi_home_pos_set,this,true);
        txt_gohome = (TextView) findViewById(R.id.txt_gohome);
        txt_home = (TextView) findViewById(R.id.txt_home);
        txt_company = (TextView) findViewById(R.id.txt_company);
        txt_gocompany = (TextView) findViewById(R.id.txt_gocompany);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        delete1=(ImageView) findViewById(R.id.delete1);
        delete2=(ImageView) findViewById(R.id.delete2);
        this.setBackgroundColor(Color.parseColor("#efeff4"));
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_NAVI_HOME_SET_SUCESS,this);
        OCtrlGps.getInstance().ccmd1247_getNavigationInfomation();
        getMyLocation();
    }
    public void getMyLocation(){
        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
            @Override
            public void onCurrentPosGet(DataPos posData) {
                myLocation=posData;
            }
        });
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.GPS_NAVI_HOME_SET_SUCESS)){
            handleChangeData();
        }
        super.receiveEvent(eventName, paramObj);
    }
    @Override
    protected void initViews() {
        //如果家和公司已经设置了设置删除按钮可见，没有就不可见
        handleChangeData();
    }

    @Override
    protected void initEvents() {
        //返回上一个页面
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_main);
            }
        });
        txt_home.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataNavigation data = ManagerNavi.getInstance().naviInfo;
                if(data != null && data.home!=null && data.home.length()>0){
                    DataPos end = new DataPos(NAVI.Str2Latlng(data.homeLatitude),data.home);
                    openNavi(myLocation,end);
                }else{
                    ViewHomePosChoose.IS_CHOOSE_HOME_POS = true;
                    dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_choose);
                }
            }
        });
        txt_gohome.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataNavigation data = ManagerNavi.getInstance().naviInfo;
                if(data != null && data.home!=null && data.home.length()>0){
                    DataPos end = new DataPos(NAVI.Str2Latlng(data.homeLatitude),data.home);
                    openNavi(myLocation,end);
                }else{
                    ViewHomePosChoose.IS_CHOOSE_HOME_POS = true;
                    dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_choose);
                }
            }
        });
        txt_gocompany.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataNavigation data = ManagerNavi.getInstance().naviInfo;
                if(data != null && data.company!=null && data.company.length()>0){
                    DataPos end = new DataPos(NAVI.Str2Latlng(data.companyLatitude),data.company);
                    openNavi(myLocation,end);
                }else{
                    ViewHomePosChoose.IS_CHOOSE_HOME_POS = false;
                    dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_choose);
                }
            }
        });
        txt_company.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataNavigation data = ManagerNavi.getInstance().naviInfo;
                if(data != null && data.company!=null && data.company.length()>0){
                    DataPos end = new DataPos(NAVI.Str2Latlng(data.companyLatitude),data.company);
                    openNavi(myLocation,end);
                }else{
                    ViewHomePosChoose.IS_CHOOSE_HOME_POS = false;
                    dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_navi_home_pos_choose);
                }
            }
        });
        delete1.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                OCtrlGps.getInstance().ccmd1246_setNavigation("","",0);
            }
        });
        delete2.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                OCtrlGps.getInstance().ccmd1246_setNavigation("","",1);
            }
        });

    }

    @Override
    protected void invalidateUI() {
        //default
        delete1.setVisibility(INVISIBLE);
        delete2.setVisibility(INVISIBLE);
        txt_home.setText("点击设置");
        txt_company.setText("点击设置");
        DataNavigation data = ManagerNavi.getInstance().naviInfo;
        if(data == null)return;
        if(data.home!=null && data.home.length()>0){
            txt_home.setText(data.home);
            delete1.setVisibility(VISIBLE);
        }
        if(data.company!=null && data.company.length()>0){
            txt_company.setText(data.company);
            delete2.setVisibility(VISIBLE);
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
