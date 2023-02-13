package view.view4app.carpath;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.ActivityBase;

import java.util.List;

import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import view.view4me.set.ClipTitleMeSet;

/**
 * 导航搜索
 */

public class ViewMapSearchNear extends ActivityBase {
    public static String PRE_SEARCH_STR = "加油站";

    private ImageView     img_left;
    private EditText      edit_input;
    private FullScreenMap map_full;
    private ListView      list_address;
    private TextView      txt_title;
    private ClipTitleMeSet title_head;
    private List<PoiInfo> allPoiList;
    private AdapterNearSearch adapterNearSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_search_near);
        edit_input = (EditText) findViewById(R.id.edit_input);
        map_full = (FullScreenMap) findViewById(R.id.map_full);
        img_left = (ImageView) findViewById(R.id.img_left);
        list_address = (ListView) findViewById(R.id.list_address);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        View title = View.inflate(getApplicationContext(),R.layout.map_search_near_title,null);
        list_address.addHeaderView(title);
        txt_title = (TextView) title.findViewById(R.id.txt_title);
        MapPosGet.searchCurrentPos(null);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        title_head.setTitle(ViewMapSearchNear.PRE_SEARCH_STR);
        //先搜索
        searchPlaceList();
    }
    private void searchPlaceList(){
        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
            @Override
            public void onCurrentPosGet(DataPos posData) {
                OPoiSerch.getInstance().setOnSearchResultBack(new OPoiSerch.OnSearchResultBack() {
                    @Override
                    public void onResultBack(PoiResult poiResult) {
                        if (poiResult == null) return;
                        allPoiList = poiResult.getAllPoi();
                        if (allPoiList == null) return;
                        map_full.clearOverlay();
                        map_full.placeSelfPos(MapPosGet.getPrePos().pos,0,1);
                        map_full.placeMapPoi(allPoiList);
                        map_full.moveToPos(allPoiList.get(0).location);
                        handleChangeData();
                    }
                });
                OPoiSerch.getInstance().searchPos(posData.pos, PRE_SEARCH_STR, 30);
            }
        });
    }

    @Override
    protected void initEvents() {

        title_head.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        edit_input.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void afterTextChanged(final Editable s) {
//                if (!s.toString().equals("")) {
//                    PRE_SEARCH_STR = s.toString();
//                    searchPlaceList();
//                }
//            }
//        });
        txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_address.getHeight()> ODipToPx.dipToPx(getApplicationContext(),50)){//弹出的
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ODipToPx.dipToPx(getApplicationContext(),30));
                    list_address.setLayoutParams(params);
                }else{//缩进的
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ODipToPx.dipToPx(getApplicationContext(),320));
                    list_address.setLayoutParams(params);
                }
            }
        });
    }
    @Override
    protected void invalidateUI() {
        if(allPoiList!=null && allPoiList.size()>0){
            txt_title.setText("搜索到\""+PRE_SEARCH_STR+"\"相关"+allPoiList.size()+"个结果");
            adapterNearSearch = new AdapterNearSearch(getApplicationContext(), PRE_SEARCH_STR, allPoiList, new AdapterNearSearch.OnClickListener() {
                @Override
                public void onClickText(PoiInfo info) {
                    map_full.moveToPos(info.location);
                }
                @Override
                public void onClickNavi(PoiInfo info) {
                    DataPos end = new DataPos(info.location,info.address);
                    openNavi(MapPosGet.getPrePos(),end);
                }
            });
            list_address.setAdapter(adapterNearSearch);
        }else{
            txt_title.setText("搜索到0个结果");
        }
    }
    @Override
    protected void popView(int resId) {

    }
    private void openNavi(DataPos start, DataPos end) {
        if (start == null || start.pos == null) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无法获取自已定位");
            return;
        }
//        BNRoutePlanNode startNode = new BNRoutePlanNode(start.pos.longitude, start.pos.latitude, start.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode endNode   = new BNRoutePlanNode(end.pos.longitude, end.pos.latitude, end.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        Intent          intent    = new Intent(getApplicationContext(), ActivityNaviMap.class);
//        Bundle          bundle    = new Bundle();
//        bundle.putSerializable(ActivityNaviMap.START_NODE, startNode);
//        bundle.putSerializable(ActivityNaviMap.END_NODE, endNode);
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
    }
}