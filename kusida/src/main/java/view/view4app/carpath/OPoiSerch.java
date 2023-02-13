package view.view4app.carpath;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;
/**
 * Created by Administrator on 2017/3/31.
 */

public class OPoiSerch {
    private PoiSearch search;
    private OnSearchResultBack onSearchResultBack;
    // ========================out======================
    private static OPoiSerch	_instance;
    protected OPoiSerch() {
    }
    public static OPoiSerch getInstance() {
        if (_instance == null)
            _instance = new OPoiSerch();
        return _instance;
    }
    // ==============================================
    public void searchPos(LatLng pos,String searchWord,int maxNum){
        search = PoiSearch.newInstance();
        search.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
//                List<PoiInfo> allAddr = poiResult.getAllPoi();
                if(onSearchResultBack!=null)onSearchResultBack.onResultBack(poiResult);
                search.destroy();
//                for (PoiInfo p: allAddr) {
//                    Log.d("MainActivity", "p.name--->" + p.name +"p.phoneNum" + p.phoneNum +" -->p.address:" + p.address + "p.location" + p.location);
//                }Error:(34, 83) 错误: <匿名view.view4app.carpath.OPoiSerch$1>不是抽象的, 并且未覆盖OnGetPoiSearchResultListener中的抽象方法onGetPoiIndoorResult(PoiIndoorResult)
            }
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.location(pos).keyword(searchWord).radius(3000).pageNum(0).pageCapacity(maxNum);
        search.searchNearby(option);
    }
    // 详情搜索
    public void searchDetail(PoiInfo poi){
        search = PoiSearch.newInstance();
        search.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
            }
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                search.destroy();

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        search.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
    }
    public interface OnSearchResultBack {
        void onResultBack(PoiResult poiResult);
    }
    public void setOnSearchResultBack(OnSearchResultBack listener) {
        this.onSearchResultBack = listener;
    }
}
