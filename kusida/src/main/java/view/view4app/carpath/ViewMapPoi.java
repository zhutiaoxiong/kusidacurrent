package view.view4app.carpath;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.search.core.PoiInfo;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import common.global.NAVI;
import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import ctrl.OCtrlGps;
import view.view4me.set.ClipTitleMeSet;

/**
 * initView
 * //加载历史纪录
 * adapterForNativeAderess=new AdapterForNaviHistory(getContext(), listHistory, new AdapterForNaviHistory.OnClickListener() {
 *
 * @Override public void onClickNavi(DataPos data) {
 * ViewMapPoi.ALLPOI_PRESET=DataPos.DataPos2PoiInfo(listHistory);
 * ViewMapPoi.PRE_VIEW_ID=R.layout.view_navi_search;
 * ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_poi);
 * }
 * });
 * listview_adress.setAdapter(adapterForNativeAderess);
 * //点击了MapPoi返回
 * if(ViewMapPoi.BACK_IS_FROM_CONFIRM){
 * openNavi(MapPosGet.getPrePos(),DataPos.PoiInfo2DataPos(ViewMapPoi.CONFIRM_SELECT_DATA));
 * }
 */
public class ViewMapPoi extends ActivityBase {
    public static  int PRE_VIEW_ID = 0;//跳转回去使用,预设
    public static int PRE_POS     = 0;//默认为0
    public static List<PoiInfo> ALLPOI_PRESET;//预设数据
//    public static PoiInfo       CONFIRM_SELECT_DATA;//点确认返回保存的数据，使用后要清空
//    public static boolean BACK_IS_FROM_CONFIRM = false;//如果是返回点了确认，就是true

    private ClipTitleMeSet title_head;
    private FullScreenMap          map_full;
    private ViewBelowMapItemSwitch view_pos;
    private DataPos selfLocation;
    private int currentPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_poi);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        map_full = (FullScreenMap) findViewById(R.id.map_full);
        view_pos = (ViewBelowMapItemSwitch) findViewById(R.id.view_pos);
        initViews();
        initEvents();
        MapPosGet.searchCurrentPos(null);
        ODispatcher.addEventListener(OEventName.MAP_POI_CLICKED, this);
    }
    @Override
    protected void initViews() {
//        OPoiSerch.getInstance().setOnSearchResultBack(new OPoiSerch.OnSearchResultBack() {
//            @Override
//            public void onResultBack(PoiResult poiResult) {
//                poi_result = poiResult;
//                currentPos = 0;
//                if(poi_result == null)return;
//                List<PoiInfo> allPoi = poi_result.getAllPoi();
//                if(allPoi == null)return;
//                map_full.clearOverlay();
//                map_full.placeMapPoi(poiResult);
//                map_full.moveToPos(allPoi.get(0).location);
//                handleChangeData();
//            }
//        });
//        OPoiSerch.getInstance().searchPos(posData.pos,SEARCH_STR,30);

        if (PRE_POS >= ALLPOI_PRESET.size()) return;
        currentPos = PRE_POS;
        if (ALLPOI_PRESET == null) return;
        map_full.clearOverlay();
        map_full.placeMapPoi(ALLPOI_PRESET);
        map_full.moveToPos(ALLPOI_PRESET.get(currentPos).location);
        handleChangeData();
        getCurrentLocation();
    }
    public void getCurrentLocation(){
        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
            @Override
            public void onCurrentPosGet(DataPos posData) {
                selfLocation=posData;
            }
        });
    }
    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                finish();
            }
        });
        view_pos.txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ALLPOI_PRESET == null) return;
//                BACK_IS_FROM_CONFIRM = true;
//                CONFIRM_SELECT_DATA = ALLPOI_PRESET.get(currentPos);
                if(PRE_VIEW_ID==R.layout.view_navi_home_pos_choose){
                    int num = ViewHomePosChoose.IS_CHOOSE_HOME_POS ? 0 : 1;
                    OCtrlGps.getInstance().ccmd1246_setNavigation(ALLPOI_PRESET.get(currentPos).name, NAVI.Latlng2Str(ALLPOI_PRESET.get(currentPos).location), num);
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, PRE_VIEW_ID);
                }else{
                    PoiInfo info= ALLPOI_PRESET.get(currentPos);
                    DataPos end=new DataPos(info.location,info.address,info.name);//LatLng pos, String address, String addressName
                    openNavi(selfLocation,end);
                }

//                openNavi(posData,end);
            }
        });
        view_pos.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ALLPOI_PRESET == null) return;
                if (currentPos == 0) {
                    currentPos = ALLPOI_PRESET.size() - 1;
                } else {
                    currentPos--;
                }
                handleChangeData();
            }
        });
        view_pos.img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ALLPOI_PRESET == null) return;
                if (currentPos == ALLPOI_PRESET.size() - 1) {
                    currentPos = 0;
                } else {
                    currentPos++;
                }
                handleChangeData();

            }
        });

    }
    @Override
    public void receiveEvent(String key, Object value) {
        if (key.equals(OEventName.MAP_POI_CLICKED)) {
            currentPos = (Integer) value;
            handleChangeData();
        }
        super.receiveEvent(key, value);
    }

    @Override
    protected void invalidateUI() {
        if (ALLPOI_PRESET == null) return;
        MapPosGet.searchCurrentPos(null);

        PoiInfo curr = ALLPOI_PRESET.get(currentPos);
        String  dis  = (MapPosGet.getPrePos() == null) ? "0km" : MapPosGet.getLatLngDistance(MapPosGet.getPrePos().pos, curr.location);
        title_head.setTitle(curr.name);
        view_pos.setData(currentPos + ". " + curr.name, curr.address);
        map_full.moveToPos(curr.location);
    }
    @Override
    protected void popView(int resId) {

    }
    private void openNavi(DataPos start, DataPos end){
        if(start == null || start.pos == null){
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"无法获取自已定位");
            return;
        }
//        BNRoutePlanNode startNode = new BNRoutePlanNode(start.pos.longitude, start.pos.latitude, start.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode endNode   = new BNRoutePlanNode(end.pos.longitude, end.pos.latitude, end.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        Intent intent    = new Intent(getApplicationContext(), ActivityNaviMap.class);
//        Bundle bundle    = new Bundle();
//        bundle.putSerializable(ActivityNaviMap.START_NODE,startNode);
//        bundle.putSerializable(ActivityNaviMap.END_NODE,endNode);
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
    }
}
