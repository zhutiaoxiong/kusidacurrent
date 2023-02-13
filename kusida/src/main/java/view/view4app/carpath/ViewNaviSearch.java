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

import common.GlobalContext;
import common.map.DataPos;
import common.map.MapPosGet;
import common.pinyinzhuanhuan.KeyBoard;

/**
 * 导航搜索
 */

public class ViewNaviSearch extends RelativeLayoutBase{
    private static String SEARCH_COMPLETED = "SEARCH_COMPLETED";
    public static int TYPE, TYPE_PICK_POINT, TYPE_GOTO_NAVI;//preset
    public static int PRE_VIEW_ID = 0;//跳转回去使用,预设
    private EditText             edit_input;
    private TextView             cancle;
    private RelativeLayout       lin_candel;
    private ImageView            delete;
    private ListView             list_show;
    private SwipeListView listview_adress;
    private AdapterNaviAddress   adapterNaviAddress;//表示导航实时地点列表
    private AdapterForNaviHistory adapterForNativeAderess;//表示历史纪录 adapter
    public static List<DataPos > listHistory;
    public static String searchStr;//搜索字符串
    public ViewNaviSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_navi_search, this, true);
        edit_input= (EditText) findViewById(R.id.edit_input);
        cancle= (TextView) findViewById(R.id.cancle);
        lin_candel= (RelativeLayout) findViewById(R.id.lin_candel);
        delete= (ImageView) findViewById(R.id.delete);
        list_show= (ListView) findViewById(R.id.list_show);
        listview_adress= (SwipeListView) findViewById(R.id.listview_adress);
        initViews();
        initEvents();
        ODispatcher.addEventListener(SEARCH_COMPLETED,this);
    }

    @Override
    protected void initViews() {
        listview_adress.setRightViewWidth(ODipToPx.dipToPx(getContext(),80f));
        KeyBoard.openKeyBoard(edit_input);
        listHistory=DataPos.loadSearchHistory();
        if(listHistory==null|| listHistory.size()==0){
            lin_candel.setVisibility(View.INVISIBLE);
        }else{
            lin_candel.setVisibility(View.VISIBLE);
            listview_adress.setVisibility(View.VISIBLE);
        }
        //加载历史纪录
        adapterForNativeAderess=new AdapterForNaviHistory(getContext(),ODipToPx.dipToPx(getContext(),80f), listHistory, new AdapterForNaviHistory.OnClickItemListener() {
            @Override
            public void onClickNavi(DataPos data) {
                ViewMapPoi.ALLPOI_PRESET=DataPos.DataPos2PoiInfo(listHistory);
                ViewMapPoi.PRE_VIEW_ID=R.layout.view_navi_search;
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewMapPoi.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }

            @Override
            public void onClicDelete(DataPos data) {
                if(data==null)return;
                listHistory.remove(data);
                DataPos.deleteOneSearchHistory(data);
                adapterForNativeAderess.notifyDataSetChanged();
            }
        });
        listview_adress.setAdapter(adapterForNativeAderess);
//        //点击了MapPoi返回
//        if(ViewMapPoi.BACK_IS_FROM_CONFIRM){
//            openNavi(MapPosGet.getPrePos(),DataPos.PoiInfo2DataPos(ViewMapPoi.CONFIRM_SELECT_DATA));
//        }
    }

    @Override
    protected void initEvents() {
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, PRE_VIEW_ID);
            }
        });
        edit_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(final Editable s) {

                if (!s.toString().equals("")) {
                    edit_input.setAlpha(1);
                    list_show.setVisibility(View.VISIBLE);
                    listview_adress.setVisibility(View.INVISIBLE);
                    lin_candel.setVisibility(View.INVISIBLE);
                    searchStr = s.toString();
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
//                    MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
//                        @Override
//                        public void onCurrentPosGet(DataPos posData) {
//                            OPoiSerch.getInstance().setOnSearchResultBack(new OPoiSerch.OnSearchResultBack() {
//                                @Override
//                                public void onResultBack(PoiResult poiResult) {
//                                    if(poiResult == null)return;
//                                    List<PoiInfo> allPoi = poiResult.getAllPoi();
//                                    if(allPoi == null || allPoi.size() == 0)return;
//                                    if(TYPE == TYPE_GOTO_NAVI) {
//                                        adapterNaviAddress = new AdapterNaviAddress(getContext(), allPoi, new AdapterNaviAddress.OnClickListener() {
//                                            @Override
//                                            public void onClickNavi(PoiInfo data) {
//                                                DataPos end = new DataPos(data.location,data.name,data.address);
//                                                ViewMapWatchPos.usePos=end;
//                                                ViewMapWatchPos.PRE_VIEW_ID=R.layout.view_navi_search;
//                                                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.map_watch_pos);
//                                                //保存到历史
//                                                DataPos.saveSearchHistory(end);
//                                            }
//                                        });
//                                        list_show.setAdapter(adapterNaviAddress);
//                                    }else if(TYPE == TYPE_PICK_POINT){
//
//                                    }
//                                }
//                            });
//                            OPoiSerch.getInstance().searchPos(posData.pos,s.toString(),20);
//                            ViewNaviSearch.searchStr=s.toString();
//                        }
//                    });
                } else {
                    edit_input.setAlpha(0.5f);
                    listHistory = DataPos.loadSearchHistory();
                    if (listHistory == null || listHistory.size() == 0) {
                        lin_candel.setVisibility(View.INVISIBLE);
                    } else {
                        lin_candel.setVisibility(View.VISIBLE);
                        listview_adress.setVisibility(View.VISIBLE);
                    }
                    list_show.setVisibility(View.INVISIBLE);
                    adapterForNativeAderess.changeUI(listHistory);
                }
            }
        });
        delete.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withInfo("清空全部搜索历史纪录？")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm) {
                                    DataPos.deleteSearchHistoryAll();
                                    lin_candel.setVisibility(INVISIBLE);
                                    listHistory.clear();
                                    adapterForNativeAderess.notifyDataSetChanged();
                                    if (listHistory == null || listHistory.size() == 0) {
                                        listview_adress.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        })
                        .show();

            }
        });
    }
    private List<DataPos> allPoi;
    private void searchPosInfo(final List<SuggestionResult.SuggestionInfo> list, final int searchPos) {
        if (list == null || searchPos>=list.size()) {
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
                    if (!haveData){
                        posData.addressName = list.get(searchPos).key;
                        allPoi.add(posData);
                    }
                    searchPosInfo(list, searchPos + 1);
                }
            });
        }else{//no pos
            searchPosInfo(list, searchPos + 1);
        }
    }
    @Override
    public void receiveEvent(String key, Object value) {
        if (key.equals(SEARCH_COMPLETED)) {
            if (TYPE == TYPE_GOTO_NAVI) {
                adapterNaviAddress = new AdapterNaviAddress(getContext(), allPoi, new AdapterNaviAddress.OnClickListener() {
                    @Override
                    public void onClickNavi(DataPos end,int position) {
                        ViewMapPoi.ALLPOI_PRESET=DataPos.DataPos2PoiInfo(allPoi);
                        ViewMapPoi.PRE_POS=position;
                        ViewMapPoi.PRE_VIEW_ID=R.layout.view_navi_search;
//                        ViewMapWatchPos.usePos = end;
//                        ViewMapWatchPos.PRE_VIEW_ID = R.layout.view_navi_search;
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
                        list_show.setAdapter(adapterNaviAddress);
                    }
                });
            } else if (TYPE == TYPE_PICK_POINT) {

            }
        }
    }
    @Override
    protected void invalidateUI() {

    }
    private void openNavi(DataPos start, DataPos end) {
        if (start == null || start.pos == null) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无法获取自已定位");
            return;
        }
//        BNRoutePlanNode startNode = new BNRoutePlanNode(start.pos.longitude, start.pos.latitude, start.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode endNode   = new BNRoutePlanNode(end.pos.longitude, end.pos.latitude, end.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        Intent          intent    = new Intent(getContext(), ActivityNaviMap.class);
//        Bundle          bundle    = new Bundle();
//        bundle.putSerializable(ActivityNaviMap.START_NODE, startNode);
//        bundle.putSerializable(ActivityNaviMap.END_NODE, endNode);
//        intent.putExtras(bundle);
//        getContext().startActivity(intent);
    }

}