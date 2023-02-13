package view.view4app.carpath;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.listview.SwipeListView;
import com.kulala.staticsview.OnClickListenerMy;


import java.util.ArrayList;
import java.util.List;

import adapter.AdapterForSearchAderessReSult;
import common.GlobalContext;
import common.global.NAVI;
import common.map.DataPos;
import common.map.MapPosGet;
import ctrl.OCtrlGps;

import static view.view4app.carpath.ViewNaviSearch.listHistory;

/**
 * Created by qq522414074 on 2017/3/29.
 */

public class ViewHomePosChoose extends RelativeLayoutBase {
    private static String SEARCH_COMPLETED = "SEARCH_COMPLETED";
    public static int TYPE, TYPE_PICK_POINT, TYPE_GOTO_NAVI;//preset
    public static boolean IS_CHOOSE_HOME_POS = true;
    private EditText edit;
    private ImageView img_adress_history;
    private TextView cancle, txt_my_location, txt_map_select_point;
    private SwipeListView listview_adress;
    private  ListView list_show;
    private RelativeLayout head_view;
    private LinearLayout layout_lishi;
    private AdapterForSearchAderessReSult adapterReSult;
    private AdapterForHomeAndCompanySearch adapterForHomeAndCompanySearch;
    public static String searchStr;
    public ViewHomePosChoose(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_navi_home_pos_choose, this, true);
        edit = (EditText) findViewById(R.id.edit);
        img_adress_history = (ImageView) findViewById(R.id.img_adress_history);
        cancle = (TextView) findViewById(R.id.cancle);
        txt_my_location = (TextView) findViewById(R.id.txt_my_location);
        txt_map_select_point = (TextView) findViewById(R.id.txt_map_select_point);
        listview_adress = (SwipeListView) findViewById(R.id.listview_adress);
        list_show = (ListView) findViewById(R.id.list_show);
        head_view = (RelativeLayout) findViewById(R.id.head_view);
        layout_lishi = (LinearLayout) findViewById(R.id.layout_lishi);
        MapPosGet.searchCurrentPos(null);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_NAVI_HOME_SET_SUCESS, this);
        ODispatcher.addEventListener(SEARCH_COMPLETED, this);
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.GPS_NAVI_HOME_SET_SUCESS)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "设置成功!");
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_navi_main);
        } else if (eventName.equals(SEARCH_COMPLETED)) {
            if (TYPE == TYPE_GOTO_NAVI) {
                adapterReSult = new AdapterForSearchAderessReSult(getContext(), allPoi, new AdapterForSearchAderessReSult.OnClickListener() {


                    @Override
                    public void onClickChoosePoint(DataPos end,int position) {
                        ViewMapPoi.ALLPOI_PRESET=DataPos.DataPos2PoiInfo(allPoi);
                        ViewMapPoi.PRE_VIEW_ID = R.layout.view_navi_home_pos_choose;
                        ViewMapPoi.PRE_POS=position;
                        Intent intent = new Intent();
                        intent.setClass(getContext(), ViewMapPoi.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                        //保存到历史
                        DataPos.saveSearchHistory(end);
                    }
                });
                GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list_show.setAdapter(adapterReSult);
                    }
                });
            } else if (TYPE == TYPE_PICK_POINT) {

            }
        }
        super.receiveEvent(eventName, paramObj);
    }

    @Override
    protected void initViews() {
//        if(ViewMapPoi.BACK_IS_FROM_CONFIRM){
//            ViewMapPoi.CONFIRM_SELECT_DATA
//  }
        listview_adress.setRightViewWidth(ODipToPx.dipToPx(getContext(),80f));
        listHistory = DataPos.loadSearchHistory();
        if (listHistory == null || listHistory.size() == 0) {
            head_view.setVisibility(View.GONE);
        } else {
            head_view.setVisibility(View.VISIBLE);
            layout_lishi.setVisibility(View.VISIBLE);
        }
        //加载历史纪录
        adapterForHomeAndCompanySearch = new AdapterForHomeAndCompanySearch(getContext(), ODipToPx.dipToPx(getContext(),80f),listHistory, new AdapterForHomeAndCompanySearch.OnClickItemListener() {
            @Override
            public void onClicDelete(DataPos data) {
                if(data==null)return;
                listHistory.remove(data);
                DataPos.deleteOneSearchHistory(data);
                adapterForHomeAndCompanySearch.notifyDataSetChanged();
            }

            @Override
            public void onClickNavi(DataPos data) {
                int num = ViewHomePosChoose.IS_CHOOSE_HOME_POS ? 0 : 1;
                OCtrlGps.getInstance().ccmd1246_setNavigation(data.addressName, NAVI.Latlng2Str(data.pos), num);
            }
        });
        listview_adress.setAdapter(adapterForHomeAndCompanySearch);
    }

    @Override
    protected void initEvents() {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (!s.toString().equals("")) {
                    edit.setAlpha(1);
//                    MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
//                        @Override
//                        public void onCurrentPosGet(final DataPos posData) {
                    list_show.setVisibility(View.VISIBLE);
                    layout_lishi.setVisibility(View.INVISIBLE);
                    head_view.setVisibility(View.INVISIBLE);
                    searchStr=s.toString();
                    MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
                        @Override
                        public void onCurrentPosGet(DataPos posData) {
                            final SuggestionSearch search = SuggestionSearch.newInstance();
                            search.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
                                @Override
                                public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                                    if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                                        return;//未找到相关结果
                                    } else {
                                        List<SuggestionResult.SuggestionInfo> list = suggestionResult.getAllSuggestions();
                                        allPoi = new ArrayList<DataPos>();
                                        searchPosInfo(list, 0);
                                    }
                                    search.destroy();
                                }
                            });
                            search.requestSuggestion(new SuggestionSearchOption().keyword(searchStr).city(MapPosGet.getPreAddress().city));
                        }
                    });
//                            OPoiSerch.getInstance().setOnSearchResultBack(new OPoiSerch.OnSearchResultBack() {
//                                @Override
//                                public void onResultBack(PoiResult poiResult) {
//                                    if (poiResult == null) return;
//                                    List<PoiInfo> allPoi = poiResult.getAllPoi();
//                                    if (allPoi == null || allPoi.size() == 0) return;
//                                    if (TYPE == TYPE_GOTO_NAVI) {
//                                        adapterReSult = new AdapterForSearchAderessReSult(getContext(), allPoi, new AdapterForSearchAderessReSult.OnClickListener() {
//                                            @Override
//                                            public void onClickNavi(PoiInfo data) {
//                                                DataPos end = new DataPos(data.location, data.name,data.address);
//                                                ViewMapPoi.usePos=end;
//                                                ViewMapPoi.PRE_VIEW_ID=R.layout.view_navi_home_pos_choose;
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_poi);
////                                        openNavi(MapPosGet.getPrePos(), end);
//                                                //保存到历史
//                                                DataPos.saveSearchHistory(end);
//                                            }
//                                        });
//                                        list_show.setAdapter(adapterReSult);
//                                    } else if (TYPE == TYPE_PICK_POINT) {
//
//                                    }
//                                }
//                            });
//                            OPoiSerch.getInstance().searchPos(posData.pos, s.toString(), 20);
//                            ViewNaviSearch.searchStr = s.toString();
//                        }
//                    });
                } else {
                    edit.setAlpha(0.5f);
                    listHistory = DataPos.loadSearchHistory();
                    if (listHistory == null || listHistory.size() == 0) {
                        head_view.setVisibility(View.GONE);
                    } else {
                        head_view.setVisibility(View.VISIBLE);
                        layout_lishi.setVisibility(View.VISIBLE);
                    }
                    list_show.setVisibility(View.INVISIBLE);
                    adapterForHomeAndCompanySearch.changeUI(listHistory);
                }
            }

        });
        //点击返回上一个页面
        cancle.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_navi_main);
            }
        });
        //点击这个删除所有的地址纪录
        img_adress_history.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle("清空全部搜索历史纪录？")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm) {
                                    DataPos.deleteSearchHistoryAll();
                                    head_view.setVisibility(GONE);
                                    listHistory.clear();
                                    adapterForHomeAndCompanySearch.notifyDataSetChanged();
                                    if (listHistory == null || listHistory.size() == 0) {
                                        layout_lishi.setVisibility(View.GONE);
                                    }
                                }
                            }
                        })
                        .show();

            }
        });
        //到地图中自己的位置去
        txt_my_location.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
                    @Override
                    public void onCurrentPosGet(DataPos posData) {
                        int num = IS_CHOOSE_HOME_POS ? 0 : 1;
                        OCtrlGps.getInstance().ccmd1246_setNavigation(posData.addressName, NAVI.Latlng2Str(posData.pos), num);
                    }
                });
            }
        });
        //到地图中选点
        txt_map_select_point.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ViewMapPickPos.PRE_VIEW_ID = R.layout.view_navi_home_pos_choose;
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewMapPickPos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

    }

    private List<DataPos> allPoi;

    private void searchPosInfo(final List<SuggestionResult.SuggestionInfo> list, final int searchPos) {
        if (list == null || searchPos >= list.size()) {
            ODispatcher.dispatchEvent(SEARCH_COMPLETED);
            return;
        }
        if (allPoi == null) return;
        if (list.get(searchPos).pt != null) {
            MapPosGet.searchAddressByPos(list.get(searchPos).pt, new MapPosGet.OnAddressGetListener() {
                @Override
                public void onAddressGet(DataPos posData) {
                    boolean haveData = false;
                    for (DataPos pos : allPoi) {
                        if (pos.addressName.equals(list.get(searchPos))) {
                            haveData = true;
                            break;
                        }
                    }
                    if (!haveData) {
                        posData.addressName = list.get(searchPos).key;
                        allPoi.add(posData);
                    }
                    searchPosInfo(list, searchPos + 1);
                }
            });
        } else {//no pos
            searchPosInfo(list, searchPos + 1);
        }
    }

    private void openNavi(DataPos start, DataPos end) {
        if (start == null || start.pos == null) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无法获取自已定位");
            return;
        }
//        BNRoutePlanNode startNode = new BNRoutePlanNode(start.pos.longitude, start.pos.latitude, start.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode endNode = new BNRoutePlanNode(end.pos.longitude, end.pos.latitude, end.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        Intent intent = new Intent(getContext(), ActivityNaviMap.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ActivityNaviMap.START_NODE, startNode);
//        bundle.putSerializable(ActivityNaviMap.END_NODE, endNode);
//        intent.putExtras(bundle);
//        getContext().startActivity(intent);
    }

    @Override
    protected void invalidateUI() {

    }
}
